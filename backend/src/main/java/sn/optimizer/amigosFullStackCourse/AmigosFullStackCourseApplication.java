package sn.optimizer.amigosFullStackCourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories(enableDefaultTransactions = false)
public class AmigosFullStackCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmigosFullStackCourseApplication.class, args);
	}

}
