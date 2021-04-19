package hu.perit.wsstepbystep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

import hu.perit.spvitamin.spring.environment.EnvironmentPostProcessor;

@SpringBootApplication
@ComponentScan(basePackages = {"hu.perit.spvitamin", "hu.perit.wsstepbystep"})
@EnableRetry
public class WsstepbystepApplication
{

    public static void main(String[] args)
    {
        SpringApplication application = new SpringApplication(WsstepbystepApplication.class);
        application.addListeners(new EnvironmentPostProcessor());
        application.run(args);
    }

}
