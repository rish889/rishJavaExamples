package CoreJava.Optional.Java8InAction.chap10;

import java.util.Optional;

public class OptionalMain {

    public static void main(String... args) {
        Insurance insurance1 = new Insurance("Rishabh");
        Insurance insurance2 = null;
        Insurance insurance3 = new Insurance(null);

        Optional<Insurance> optInsurance = Optional.ofNullable(insurance1);
//        System.out.println(optInsurance.toString());

        Optional<String> name = optInsurance.map(Insurance::getName);
//        System.out.println(name);

//        System.out.println(Optional.of(insurance2));

        Car car1 = new Car(Optional.ofNullable(insurance1));
        Car car2 = null;
        Car car3 = new Car(null);

        Person person1 = new Person(Optional.ofNullable(car1));
        Person person2 = new Person(Optional.ofNullable(car2));
        Person person3 = new Person(Optional.ofNullable(car3));

//        System.out.println(person3.getCar().get().getInsurance());

        Optional<Person> optPerson1 = Optional.of(person2);
        System.out.println(optPerson1
                .flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("aaa"));

    }
}
