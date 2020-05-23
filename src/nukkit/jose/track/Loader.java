package nukkit.jose.track;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import nukkit.jose.track.commands.CustomStopCommand;
import nukkit.jose.track.commands.TrackCommand;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Loader extends PluginBase {

    public static HashMap<String, String> cache = new HashMap<>();
    public static HashMap<String, Map<Integer, Item>> inventory = new HashMap<>();
    public static Loader instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getCommandMap().register("nukkit", new CustomStopCommand("stop"));
        getServer().getCommandMap().register("nukkit",new TrackCommand("track"));
        getServer().getScheduler().scheduleRepeatingTask(new TrackTask(),10);
        new TrackEvent();

        File resources = new File(String.valueOf(getDataFolder()));
        if (!(resources.exists())) {
            if (!resources.mkdir()) {
                getLogger().error("The necessary resources for the plugin could not be created");
                getServer().getPluginManager().disablePlugin(this);
            } else {
                Config config = new Config(getDataFolder() + "/config.yml", Config.YAML);
                config.set("save_inventory", true);
                config.set("teleport_spawn_end", true);
                config.save();
            }
        }
        getServer().getLogger().info(TextFormat.GREEN+"TrackSystem enable by Josewowgame");
    }

    public static void add(Player player, String track) {
        if (cache.containsKey(player.getName())) {
            player.sendMessage(TextFormat.AQUA+"You are already in a follow-up session, you must finish the current one to open a new one");
        } else {
            Config config = new Config(instance.getDataFolder() + "/config.yml", Config.YAML);
            Player trackPlayer = instance.getServer().getPlayer(track);
            if (trackPlayer != null) {
                cache.put(player.getName(),track);
                if (config.getBoolean("save_inventory")) {
                    inventory.put(player.getName(), player.getInventory().getContents());
                }
                player.teleport(trackPlayer.getLevel().getSpawnLocation());
                player.teleport(new Vector3(trackPlayer.x,trackPlayer.y,trackPlayer.z));
                player.setGamemode(Player.SPECTATOR);
                player.getInventory().setHeldItemIndex(0);
                player.getInventory().clearAll();
                player.getInventory().setItem(8, Item.get(Item.REDSTONE).setCustomName(TextFormat.RED+"Remove Session"));
                player.sendMessage(TextFormat.GREEN+"You are now in a follow-up session: "+trackPlayer.getName());
            } else {
                player.sendMessage(TextFormat.RED+"OH NO! The player is not online or does not exist!");
            }
        }
    }
    public static void remove(Player player) {
        if (cache.containsKey(player.getName())) {
            Config config = new Config(instance.getDataFolder() + "/config.yml", Config.YAML);
            player.getInventory().clearAll();
            if (config.getBoolean("save_inventory")) {
                player.getInventory().setContents(inventory.get(player.getName()));
                inventory.remove(player.getName());
            }
            if (config.getBoolean("teleport_spawn_end")) {
                player.teleport(instance.getServer().getDefaultLevel().getSpawnLocation());
            }
            player.setGamemode(Player.SURVIVAL);
            cache.remove(player.getName());
            player.sendMessage(TextFormat.GREEN+"You have finished the follow-up session");
        }
    }
}
