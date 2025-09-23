package com.otulp.pluto_payments_backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Acceptera alla betrodda klientcert och ge rollen MTLS (CN blir username)
    @Bean
    AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preauthUds() {
        return token -> User.withUsername(token.getName())
                .password("{noop}N/A").roles("MTLS").build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 8080 (http) -> 443 (https) vid redirects
        http.portMapper(pm -> pm.http(8080).mapsTo(443));

        // X.509 (mTLS) – krävs på /private/**
        http.x509(x -> x.authenticationUserDetailsService(preauthUds()));

        // Kanal-krav: /public/** ska vara HTTP; /private/** ska vara HTTPS
        http.requiresChannel(ch -> ch
                .requestMatchers("/public/**").requiresInsecure()
                .requestMatchers("/private/**").requiresSecure()
        );

        // Auktorisering:
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/public/**").permitAll() // CORS preflight
                .requestMatchers(HttpMethod.GET, "/public/**").permitAll()     // öppet över HTTP
                .requestMatchers(HttpMethod.POST, "/public/**").permitAll()     // öppet över HTTP
                .requestMatchers("/private/**").hasRole("MTLS")                // kräver mTLS över HTTPS
                .anyRequest().denyAll()
        );
        
        http.cors(Customizer.withDefaults());
        http.formLogin(form -> form.disable());
        http.logout(logout -> logout.disable());
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
          
        // CSRF: behåll på, men ignorera för mTLS-vägen
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/private/**"));

        // Aktivera CORS-stödet (använder CorsConfig ovan)
        http.cors(cors -> {});

        return http.build();
    }
}
