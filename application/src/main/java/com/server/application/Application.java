package com.server.application;

import com.server.application.enumeration.Status;
import com.server.application.model.Server;
import com.server.application.repo.ServerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner runner(ServerRepo server) {
		return args -> {
			server.save(new Server(null, "192.168.1.58", "Linux", "32GB", "Virtual Machine VM", "https://cdn-icons-png.flaticon.com/512/1202/1202760.png?w=740&t=st=1679592213~exp=1679592813~hmac=8f8b26d9157fa742070fe5353cc9693b0ad53ad8624edb63986c85b52a8280e2",
					Status.SERVER_UP));
			server.save(new Server(null, "172.21.192.1", "Windows ", "8GB", "Virtual Machine VM", "https://cdn-icons-png.flaticon.com/512/1202/1202760.png?w=740&t=st=1679592213~exp=1679592813~hmac=8f8b26d9157fa742070fe5353cc9693b0ad53ad8624edb63986c85b52a8280e2",
					Status.SERVER_UP));
			server.save(new Server(null, "192.168.0.1", "Linux Ubuntu", "16GB", "Personal PC", "https://cdn-icons-png.flaticon.com/512/1202/1202760.png?w=740&t=st=1679592213~exp=1679592813~hmac=8f8b26d9157fa742070fe5353cc9693b0ad53ad8624edb63986c85b52a8280e2",
					Status.SERVER_DOWN));
			server.save(new Server(null, "192.168.2.20", "Linux Ubuntu", "16GB", "Personal PC", "https://cdn-icons-png.flaticon.com/512/1202/1202760.png?w=740&t=st=1679592213~exp=1679592813~hmac=8f8b26d9157fa742070fe5353cc9693b0ad53ad8624edb63986c85b52a8280e2",
					Status.SERVER_UP));

		};

	}
}