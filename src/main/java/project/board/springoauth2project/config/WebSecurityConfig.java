package project.board.springoauth2project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.security.cert.Extension;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		String password = passwordEncoder().encode("1111");

		/**
		 * 얘네들을 선언해주면 더 이상 따로 비밀번호가 spring 시작하면서 생성되지 않는다.
		 */
		auth.inMemoryAuthentication().withUser("user").password(password).roles("USER");
		auth.inMemoryAuthentication().withUser("manager").password(password).roles("MANGER");
		auth.inMemoryAuthentication().withUser("admin").password(password).roles("ADMIN");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/").permitAll() // root page 에 대해서는 모두가 접근 하도록 허용
				.antMatchers("/my-page").hasRole("USER") // /mypage 에 접근할 수 있는 권한은 USER 이다. 라는 뜻
				.antMatchers("/message").hasRole("MANGER") // /message 에 접근할 수 있는 권한은 MANGER 이다. 라는 뜻
				.antMatchers("/config").hasRole("ADMIN") // /config 에 접근할 수 있는 권한은 ADMIN 이다. 라는 뜻
				.anyRequest().authenticated()

		.and()
				.formLogin()
		;
	}

}
