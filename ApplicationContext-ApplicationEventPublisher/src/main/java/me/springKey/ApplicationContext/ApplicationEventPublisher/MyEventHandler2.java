package me.springKey.ApplicationContext.ApplicationEventPublisher;

import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class MyEventHandler2 {
    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public void handler(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("handler2에서 MyEvent를 받았고, 데이터는 " + myEvent.getData());
    }
}
