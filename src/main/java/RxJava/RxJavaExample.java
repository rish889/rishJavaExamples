package RxJava;

//import io.reactivex.Observable;

import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class RxJavaExample {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("Hello", "Streams", "Not");

//        Observable.fromIterable(list)
//                .filter(s -> s.contains("e"))
//                .map(s -> s.toUpperCase())
//                .reduce(new StringBuilder(), StringBuilder::append)
//                .subscribe(System.out::print,Throwable::printStackTrace);

//        Observable.just(list)
//                .subscribe(new Observer<List<String>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(List<String> list) {
//                        System.out.println(list);
//                    }
//                });

        Observable.from(list).
                filter(s -> s.contains("e")).
                map(s -> s.toUpperCase()).
                reduce(new StringBuilder(), StringBuilder::append).
                subscribe(System.out::print, e -> {
                        },
                        () -> System.out.println("!"));
    }

}
