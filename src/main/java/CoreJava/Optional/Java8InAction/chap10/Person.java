package CoreJava.Optional.Java8InAction.chap10;

import java.util.Optional;

public class Person {

    private Optional<Car> car;

    public Person(Optional<Car> car) {
        this.car = car;
    }

    public Optional<Car> getCar() {
        return car;
    }
}
