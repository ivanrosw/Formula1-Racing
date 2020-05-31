package ua.com.foxminded.formula1racing.raceobjects;

public class Racer {

    private String name;
    private String carName;

    public Racer(String name, String carName) {
        this.name = name;
        this.carName = carName;
    }

    public String getName() {
        return name;
    }

    public String getCarName() {
        return carName;
    }

}
