package me.springKey.ApplicationContext.Environment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfiguration {
    @Bean
    public CustRepository custRepository() {
        return new TestCustRepository();
    }
}
