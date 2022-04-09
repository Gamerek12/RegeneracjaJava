package gamerek.regeneracja.helpers;

import gamerek.regeneracja.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import java.util.*;

public class Regenerate extends BukkitRunnable {

    private final Player player;
    private final Terrain terrain;

    private int i = 0;
    private final List<BlockState> states;

    public Regenerate(Player player, Terrain terrain) {
        this.player = player;
        this.terrain = terrain;
        this.states = terrain.getStates();
    }

    public void run() {
        if (i+1 >= states.size()) {
            ChatUtil.sendMessage(player, "&aRegeneracja zakonczona!");
            this.cancel();
        }

        BlockState state = states.get(i);
        if (state.getType().equals(Material.TNT)) {
            i++;
            return;
        }

        state.update(true, false);
        i++;
    }
}
