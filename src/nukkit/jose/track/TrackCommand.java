package nukkit.jose.track;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.Command;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.utils.TextFormat;

public class TrackCommand extends VanillaCommand {

    public TrackCommand(String name) {
        super(name,"Admin Track Command","/track [string:player/string:close]");
        this.setPermission("track.command");
        this.commandParameters.clear();
        this.commandParameters.put("default",
                new CommandParameter[]{
                        new CommandParameter("[string:player/string:close]", CommandParamType.STRING,true)
                });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof  Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equals("close")) {
                    if (Loader.cache.containsKey(player.getName())) {
                        Loader.remove(player);
                    } else {
                        player.sendMessage(TextFormat.RED+"OH NO! Apparently you are not in a follow-up session");
                    }
                } else {
                    Loader.add(player,args[0]);
                }
            } else {
                player.sendMessage(this.getUsage());
            }
        } else {
            sender.sendMessage("TrackSystem is a moderation tool, it cannot be used in console!");
        }
        return false;
    }
}
