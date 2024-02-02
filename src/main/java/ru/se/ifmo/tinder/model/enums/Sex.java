package ru.se.ifmo.tinder.model.enums;

public enum Sex {
    MEN("MEN"),
    WOMEN("WOMEN");

    private final String sex;

    Sex(String sex) { this.sex = sex; }

    public static void showSexList(){
        for ( Sex sex : values()){
            System.out.println(sex);
        }
    }

    @Override
    public String toString() {
        return super.toString().toUpperCase();
    }
}
