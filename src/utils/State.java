package utils;

import java.util.*;

public class State implements Comparable<State> {
    public Map<Character, Vehicle> vehicles;
    public int cost;
    public State parent;
    public String move;


    public State(Map<Character, Vehicle> vehicles, int cost, State parent, String move) {
        this.vehicles = vehicles;
        this.cost = cost;
        this.parent = parent;
        this.move = move;
    }

    public int getCost(){
        return cost;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.cost, other.cost);
    }

    public String getStateString() {
        List<String> ids = new ArrayList<>();
        for (Vehicle v : vehicles.values()) {
            ids.add(v.getId() + ":" + v.getRow() + "," + v.getCol());
        }
        Collections.sort(ids);
        return String.join("|", ids);
    }

    public Map<Character, Vehicle> getVehicle() {
        return vehicles;
    }
}
