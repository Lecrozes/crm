package crm.management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import crm.management.routes.Login;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		setLoginView(http, Login.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}
	
	@Bean
	public UserDetailsManager userDetailsService() {
		UserDetails user = User.withUsername("user").password("{noop}crm").roles("USER").build();
		UserDetails admin = User.withUsername("admin").password("{noop}crm").roles("ADMIN").build();
		return new InMemoryUserDetailsManager(user, admin);
	}
}