package ru.se.ifmo.tinder.model.enums;

import java.io.Serializable;

public enum Location {
    EARTH("EARTH"),
    MARS("MARS");

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

