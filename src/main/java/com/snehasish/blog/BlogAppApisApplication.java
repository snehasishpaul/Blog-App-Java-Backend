package com.snehasish.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.snehasish.blog.config.AppConstant;
import com.snehasish.blog.entity.Role;
import com.snehasish.blog.repository.RoleRepo;

@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("password : wow$");
		System.out.println(this.passwordEncoder.encode("wow$"));

		try {
			// add the roles on project startup (if not already defined)
			Role role1 = new Role();
			role1.setId(AppConstant.ROLE_ADMIN);
			role1.setName("ROLE_ADMIN");

			Role role2 = new Role();
			role2.setId(AppConstant.ROLE_NORMAL);
			role2.setName("ROLE_NORMAL");

			List<Role> roles = List.of(role1, role2);

			List<Role> savedRoles = this.roleRepo.saveAll(roles);

			savedRoles.forEach(r -> System.out.println(r));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
