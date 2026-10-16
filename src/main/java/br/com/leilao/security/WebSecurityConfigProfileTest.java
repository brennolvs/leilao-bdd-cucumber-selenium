package br.com.leilao.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile("test")
public class WebSecurityConfigProfileTest extends WebSecurityConfigurerAdapter {
	
	@Autowired DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/db/**").permitAll()	
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/leiloes").permitAll()
			.antMatchers("/seed").permitAll()
			.antMatchers("/css/**").permitAll();
        
		http.authorizeRequests().anyRequest().authenticated();

		http.formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/leiloes", true)
            .permitAll()
        );
		
		http.logout(logout -> {
			logout.logoutUrl("/logout")
				.logoutSuccessUrl("/leiloes");
		});
		
		http.headers().frameOptions().disable();
		http.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder encoder = passwordEncoder();
		auth.inMemoryAuthentication().passwordEncoder(encoder)
			.withUser("fulano").password(encoder.encode("pass")).roles("USER")
			.and()
			.withUser("cigano").password(encoder.encode("pass")).roles("USER")
			.and()
			.withUser("beltrano").password(encoder.encode("pass")).roles("USER");
	}
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
