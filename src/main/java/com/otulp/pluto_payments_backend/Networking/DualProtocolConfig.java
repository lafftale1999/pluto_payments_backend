package com.otulp.pluto_payments_backend.Networking;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DualProtocolConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> httpConnectorCustomizer(
            @Value("${http.port:8080}") int httpPort) {
        return factory -> {
            Connector http = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            http.setScheme("http");
            http.setPort(httpPort);
            http.setSecure(false);
            http.setRedirectPort(443); // endast om du vill att Tomcat ska veta vart den ska redirecta vid "CONFIDENTIAL"-krav
            factory.addAdditionalTomcatConnectors(http);
        };
    }
}
