package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Controller
public class GreetingController {


    /*@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }*/


    private static final Logger log = LoggerFactory.getLogger(
            GreetingController.class);

    @Autowired
    private OrderSender  jmsSender;

    @Autowired
    private SimpMessageSendingOperations simpSender;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) {
        final Greeting greeting = new Greeting("Hello, " + message.getName() + "!");
        jmsSender.send(greeting);
        return greeting;
    }

    @JmsListener(destination = "greetings")
    @SendTo("/topic/greetings")
    public Greeting greeting(@Valid Greeting greeting) {
        log.info("Received greeting {}", greeting.getContent());
        simpSender.convertAndSend("/topic/greetings", greeting);
        return greeting;
    }
    @MessageMapping("/")
    @SendTo("/queue/test")
    public Greeting test(HelloMessage message) {
        final Greeting greeting = new Greeting("Hello, " + message.getName() + "!");


        for (int i = 0; i < 5; i++){
            Greeting myMessage =
                    new Greeting(i + " - Sending JMS Message using Embedded activeMQ");
            jmsSender.send(greeting);
        }

        log.info("Waiting for all ActiveMQ JMS Messages to be consumed");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //jmsSender.convertAndSend("minion", greeting);
        return greeting;
    }
}
