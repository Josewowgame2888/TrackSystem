package nukkit.jose.track.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.command.defaults.VanillaCommand;
import nukkit.jose.track.Loader;

public class CustomStopCommand extends VanillaCommand {
    public CustomStopCommand(String name) {
        super(name, "%nukkit.command.stop.description", "%commands.stop.usage");
        this.setPermission("nukkit.command.stop");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        for (Player player : Loader.instance.getServer().getOnlinePlayers().values()) {
            Loader.remove(player);
        }


        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.stop.start"));

        sender.getServer().shutdown();

        return true;
    }
}
