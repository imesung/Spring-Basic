package me.springKey.ApplicationContext.ResourceLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    ApplicationContext resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(resourceLoader.getClass());


        Resource resource = resourceLoader.getResource("classpath:test.txt");

        System.out.println(resource.getClass());
        System.out.println(resource.exists());
        System.out.println(resource.getDescription());
    }
}
