package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



@Component
public class OrderSender {

    private static Logger log = LoggerFactory.getLogger(OrderSender.class);


    private JmsTemplate jmsTemplate;

    public void send(Greeting myMessage) {
        log.info("sending with convertAndSend() to queue <" + myMessage + ">");
        jmsTemplate.convertAndSend("queue/test", myMessage);
    }
}
