package gamerek.regeneracja.helpers;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

import java.util.List;

public class Terrain {

    private List<BlockState> states;
    private Location location;
    private long time;

    public Terrain (List<BlockState> states, Location location, long time) {
        this.states = states;
        this.location = location;
        this.time = time;
    }

    public List<BlockState> getStates() {
        return states;
    }

    public Location getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    public void setStates(List<BlockState> states) {
        this.states = states;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
