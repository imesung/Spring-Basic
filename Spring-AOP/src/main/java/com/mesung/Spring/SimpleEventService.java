package com.mesung.Spring;

import org.springframework.stereotype.Service;

//Real Subject
@Service
public class SimpleEventService implements EventService {
    @Override
    public void createEvent() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Created an event");
    }

    @Override
    public void publishEvent() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Published an event");

    }

    @Override
    public void deleteEvent() {
        System.out.println("Delete an event");
    }
}
