package me.springKey.ApplicationContext.ApplicationEventPublisher;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class MyEventHandler {
    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handler(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("handler1에서 My Event를 받았고, 데이터는 " + myEvent.getData());
    }

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handler(ContextRefreshedEvent event) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextRefreshedEvent");
    }
}
