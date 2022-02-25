package test;

import com.mechalikh.pureedgesim.locationmanager.Location;

public class Timestep {
    //Time of simulation
    private int step_time;
    //Coordinates in which the device is at step_time
    private Location step_location;

    public Timestep(int t, Location l){
        this.step_time=t;
        this.step_location=l;
    }
    public void setLocation(Location location) {
        this.step_location = location;
    }

    public void setTime(int time) {
        this.step_time = time;
    }

    public Location getLocation() {
        return step_location;
    }

    public int getTime() {
        return step_time;
    }
}
