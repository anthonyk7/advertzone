package my.app.advertzone.config;

import my.app.advertzone.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;


    /**
     * it allows everyone to access api endpoint that starts with /auth because user must be authenticated
     * for further usage. and the /advert/upload api can only be accessed by authenticated users,
     * that means users who have logged in can only access this.
     * session is stateless because the authentication state is not stored in the server,
     * that is why it is stateless
     * it sets authentication provider then it adds JWT filter before authorization
     *
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/advert/upload").authenticated()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, AuthorizationFilter.class).build();
    }
}