package org.iteration.matrix.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MatrixEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatrixEurekaApplication.class, args);
	}

}
