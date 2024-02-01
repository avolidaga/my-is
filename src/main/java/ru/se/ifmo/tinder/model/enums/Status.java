package ru.se.ifmo.tinder.model.enums;

public enum Status {
    DECLINED("declined"),
    ON_CHECKING("on checking"),
    IN_PROGRESS("in progress"),
    ACCEPTED("accepted");

    private final String status;

    Status(String status){this.status = status;}

    public static void showStatusList(){
        for ( Status status : values()){
            System.out.println(status);
        }
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

