package com.otulp.pluto_payments_backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preauthUds() {
        return token -> User.withUsername(token.getName())
                .password("{noop}N/A").roles("MTLS").build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.portMapper(pm -> pm.http(8080).mapsTo(443));

        http.x509(x -> x.authenticationUserDetailsService(preauthUds()));

        http.requiresChannel(ch -> ch
                .requestMatchers("/api/**").requiresInsecure()
                .requestMatchers("/device/**").requiresSecure()
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // CORS preflight
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()     // öppet över HTTP
                .requestMatchers(HttpMethod.POST, "/api/**").permitAll()     // öppet över HTTP
                .requestMatchers("/device/**").hasRole("MTLS")                // kräver mTLS över HTTPS
                .anyRequest().denyAll()
        );
        
        http.cors(Customizer.withDefaults());
        http.formLogin(form -> form.disable());
        http.logout(logout -> logout.disable());
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/device/**"));

        http.cors(cors -> {});

        return http.build();
    }
}
