package app.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import app.core.loginManager.LoginManager;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication	
@EnableSwagger2

public class MainRest {

	public static void main(String[] args) {
		SpringApplication.run(MainRest.class, args);
	}
}
