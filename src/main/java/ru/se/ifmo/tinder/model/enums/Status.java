package ru.se.ifmo.tinder.model.enums;

public enum Status {
    ALL("ALL"),
    DECLINED("DECLINED"),
    READY("READY"),
    IN_PROGRESS("IN_PROGRESS");

    private final String status;

    Status(String status){this.status = status;}

    public static void showStatusList(){
        for ( Status status : values()){
            System.out.println(status);
        }
    }

    @Override
    public String toString() {
        return super.toString().toUpperCase();
    }

}

