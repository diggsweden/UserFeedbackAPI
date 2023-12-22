package se.digg.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import se.digg.api.utils.KeycloakLogoutHandler;

@Configuration
@EnableWebSecurity
@Profile({"dev", "prod"})
public class OAuth2SecurityConfig {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    OAuth2SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Order(1)
    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf ->
                        csrf.ignoringAntMatchers(
                                "/impression/**",
                                "/rating/**",
                                "/organisation/**"
                        ) // Disable CSRF for these paths
                )
                .authorizeRequests()
                .requestMatchers(
                        new AntPathRequestMatcher("/impression/**"),
                        new AntPathRequestMatcher("/rating/**"),
                        new AntPathRequestMatcher("/organisation/**"),
                        new AntPathRequestMatcher("/actuator"),
                        new AntPathRequestMatcher("/actuator/**"),
                        new AntPathRequestMatcher("/swagger*"),
                        new AntPathRequestMatcher("/swagger-ui*"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/api-docs*"),
                        new AntPathRequestMatcher("/api-docs/**")
                ).permitAll()
                .and()
                .authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/redis/**")).hasRole("USER")
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("USER")
                .anyRequest()
                .authenticated();

        http.oauth2Login()
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler)
                .logoutSuccessUrl("/");
        return http.build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
}