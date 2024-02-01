package ru.se.ifmo.tinder.model.enums;

import java.io.Serializable;

public enum Location implements Serializable{
    EARTH("earth"),
    MARS("mars");

    private final String planet;

    Location(String planet) {
        this.planet = planet;
    }

    public static void showPlanetList(){
        for ( Location planet : values()){
            System.out.println(planet);
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

