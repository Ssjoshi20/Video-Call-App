package com.project.videocall;

import com.project.videocall.user.User;
import com.project.videocall.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VideocallApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideocallApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserService service) {
		return args -> {
			if (!service.userStoreData.userExists("ali@mail.com")) {
				service.register(User.builder()
						.username("Ali")
						.email("ali@mail.com")
						.password("aaa")
						.build());
			}

			if (!service.userStoreData.userExists("john@mail.com")) {
				service.register(User.builder()
						.username("John")
						.email("john@mail.com")
						.password("aaa")
						.build());
			}

			if (!service.userStoreData.userExists("anna@mail.com")) {
				service.register(User.builder()
						.username("Anny")
						.email("anna@mail.com")
						.password("aaa")
						.build());
			}
		};
	}
}
