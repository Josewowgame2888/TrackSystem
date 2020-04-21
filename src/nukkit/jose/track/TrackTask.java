package nukkit.jose.track;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;

public class TrackTask extends Task {

    @Override
    public void onRun(int i) {
        for (Player player : Loader.instance.getServer().getOnlinePlayers().values()) {
            if (Loader.cache.containsKey(player.getName())) {
                Player track = Loader.instance.getServer().getPlayer(Loader.cache.get(player.getName()));
                if (track != null) {
                    player.sendPopup(TextFormat.YELLOW+"Track: "+track.getName()+" || Distance: "+(int) player.distance(track));

                    if ((int) player.distance(track) >= 21) {
                        player.teleport(new Vector3(track.x,track.y,track.z));
                    }

                    if (track.getLevel().getFolderName() != player.getLevel().getFolderName()) {
                        player.teleport(track.getLevel().getSpawnLocation());
                    }

                    if (player.getGamemode() != Player.SPECTATOR) {
                        player.setGamemode(Player.SPECTATOR);
                    }
                } else {
                    Loader.remove(player);
                    player.sendMessage(TextFormat.YELLOW+"Player disconnected from this server!");
                }
            }
        }
    }
}
