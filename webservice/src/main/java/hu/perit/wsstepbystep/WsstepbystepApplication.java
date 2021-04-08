package hu.perit.wsstepbystep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"hu.perit.spvitamin", "hu.perit.wsstepbystep"})
public class WsstepbystepApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsstepbystepApplication.class, args);
	}

}
