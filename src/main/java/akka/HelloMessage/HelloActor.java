package akka.HelloMessage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

//http://www.javaworld.com/article/2078775/scripting-jvm-languages/open-source-java-projects-akka.html

public class HelloActor extends UntypedActor {
    public void onReceive(Object message) {
        if (message instanceof HelloMessage) {
            System.out.println("My message is: " + ((HelloMessage) message).getMessage());
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("MySystem");
        ActorRef actorRef = actorSystem.actorOf(Props.create(HelloActor.class), "myActor");
        actorRef.tell(new HelloMessage("Hello, Akka!"), actorRef);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        actorSystem.stop(actorRef);
        actorSystem.shutdown();


    }
}