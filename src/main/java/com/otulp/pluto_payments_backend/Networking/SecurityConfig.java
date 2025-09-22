package com.otulp.pluto_payments_backend.Networking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

@Configuration
public class SecurityConfig {

    @Bean
    AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preauthUds() {
        return token -> {
            String cn = token.getName();
            return User.withUsername(cn).password("{noop}N/A").roles("MTLS").build();
        };
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        // Mappa rätt redirect-port (annars antar Spring 8443)
        http.portMapper(pm -> pm
                .http(8080).mapsTo(443)
                .http(80).mapsTo(443)
        );

        // Kräv HTTPS för privata endpoints
        http.requiresChannel(ch -> ch
                        .requestMatchers("/private/**").requiresSecure()
                // Om du VILL tvinga att /public/** endast får gå över HTTP:
                // .requestMatchers("/public/**").requiresInsecure()
        );

        // Auktorisering: public öppet, private kräver auth
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**", "/actuator/health").permitAll()
                .requestMatchers("/private/**").authenticated()
                .anyRequest().denyAll() // eller .authenticated(), välj vad som passar
        );

        // Välj en auth-metod. Exempelvis basic för test:
        // http.httpBasic(Customizer.withDefaults());
        // Vill du köra helt stateless API:
        // http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
