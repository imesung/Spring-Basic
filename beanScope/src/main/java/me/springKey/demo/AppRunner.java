package me.springKey.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("prototype by singleton");
        System.out.println(ctx.getBean(Singleton.class).getPrototype());
        System.out.println(ctx.getBean(Singleton.class).getPrototype());
        System.out.println(ctx.getBean(Singleton.class).getPrototype());

    }
}
