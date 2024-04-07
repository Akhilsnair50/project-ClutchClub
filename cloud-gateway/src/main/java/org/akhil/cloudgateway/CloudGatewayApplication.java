package org.akhil.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class CloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayApplication.class, args);
    }

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder){

        var apiPrefix = "/api/";

        return builder
                .routes()
                .route(route->route
                        .path(apiPrefix+"user/"+"**")
                        .filters(f->f
                                .tokenRelay()
                                .rewritePath(apiPrefix+"user/"+"(?<segment>.*)","/$\\{segment}"))
                                .uri("http://localhost:8081"))
                .route(route->route
                        .path(apiPrefix+"resource/"+"**")
                        .filters(f->f
                                .tokenRelay()
                                .rewritePath(apiPrefix+"(?<segment>.*)","/$\\{segment}"))
                        .uri("http://localhost:8083"))
                .route(route->route
                        .path("/**")
                        .uri("http://localhost:4200"))
                .build();

    }

//    @Bean
//    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){
//        httpSecurity
//                .authorizeExchange((authorize)->authorize
//                        .anyExchange().authenticated())
////                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .cors(ServerHttpSecurity.CorsSpec::disable)
//                .oauth2Login(Customizer.withDefaults())
//                .oauth2Client(Customizer.withDefaults());
//
//        return httpSecurity.build();
//    }
}
