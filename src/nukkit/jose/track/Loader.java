package nukkit.jose.track;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;

public class Loader extends PluginBase {

    public static HashMap<String, String> cache = new HashMap<String, String>();
    public static Loader instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getLogger().info(TextFormat.GREEN+"TrackSystem enable by Josewowgame");
        this.getServer().getCommandMap().register("nukkit",new TrackCommand("track"));
        this.getServer().getScheduler().scheduleRepeatingTask(new TrackTask(),15);
        new TrackEvent();
    }

    public static void add(Player player, String track) {
        if (cache.containsKey(player.getName())) {
            player.sendMessage(TextFormat.AQUA+"You are already in a follow-up session, you must finish the current one to open a new one");
        } else {
            Player trackPlayer = instance.getServer().getPlayer(track);
            if (trackPlayer != null) {
                cache.put(player.getName(),track);
                player.teleport(trackPlayer.getLevel().getSpawnLocation());
                player.teleport(new Vector3(trackPlayer.x,trackPlayer.y,trackPlayer.z));
                player.setGamemode(Player.SPECTATOR);
                player.getInventory().setHeldItemIndex(0);
                player.getInventory().setItem(8, Item.get(Item.REDSTONE).setCustomName(TextFormat.RED+"Remove Session"));
                player.sendMessage(TextFormat.GREEN+"You are now in a follow-up session: "+trackPlayer.getName());
            } else {
                player.sendMessage(TextFormat.RED+"OH NO! The player is not online or does not exist!");
            }
        }
    }
    public static void remove(Player player) {
        cache.remove(player.getName());
        player.teleport(instance.getServer().getDefaultLevel().getSpawnLocation());
        player.getInventory().clearAll();
        player.setGamemode(Player.SURVIVAL);
        player.sendMessage(TextFormat.GREEN+"You have finished the follow-up session");
    }
}
