package com.proje.security.configuration;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfiguration{




	@Bean(name = "bEncoder")
	public PasswordEncoder encoder() {

		return new BCryptPasswordEncoder();
	}


	@Bean
	public AuthenticationManager authenticationManager(@Lazy @Qualifier("accountDetailService")
																UserDetailsService userDetailsService,
															@Qualifier("bEncoder") PasswordEncoder encoder) {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(encoder);
		provider.setUserDetailsService(userDetailsService);

		return new ProviderManager(provider);

	}


	@Bean(name = "memoryUserDetailService")
	public UserDetailsService inMemoryUsers(){
		UserDetails user1 = User.builder()
				.username("user")
				.password(encoder().encode("1234"))
				.roles("USER")
				.build();

		UserDetails user2 = User.builder()
				.username("admin")
				.password(encoder().encode("1234"))
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user1, user2);

	}



	@Bean(name = "filterChain")
	public SecurityFilterChain filterChain(HttpSecurity security, AuthTokenFilter authTokenFilter) throws Exception{

		security

				.cors(Customizer.withDefaults())
				.headers(x -> {
					x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
				})


				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(x ->
				{

					// Authenticated
					x.requestMatchers("/h2-console").hasRole("ADMIN");
					x.requestMatchers(HttpMethod.GET,"/api/user").hasRole("ADMIN");
					x.requestMatchers(HttpMethod.GET,"/api/user/tickets").hasRole("ADMIN");
					x.requestMatchers(HttpMethod.POST,"/api/user/createTicket").hasAnyRole("USER", "ADMIN");
					x.requestMatchers(HttpMethod.PUT, "/api/user/sendMessage").hasAnyRole("USER", "ADMIN");
					x.requestMatchers(HttpMethod.PUT,"/api/user/closeTicket").hasRole("ADMIN");


					// permitall
					x.requestMatchers(HttpMethod.POST, "/api/user/register").permitAll();
					x.requestMatchers(HttpMethod.POST, "/api/user/login").permitAll();
					x.requestMatchers(HttpMethod.GET, "/api/user/loginJWT").authenticated();


					x.anyRequest().authenticated();
				})
				.httpBasic(Customizer.withDefaults())
				.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);


		return  security.build();

	}




	



	

}
