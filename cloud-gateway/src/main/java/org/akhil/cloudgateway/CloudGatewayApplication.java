package org.akhil.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


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
                                .rewritePath(apiPrefix+"(?<segment>.*)","/$\\{segment}")
                           )

                        .uri("http://localhost:8083")
                        )
                .route(route->route
                        .path("/**")
                        .uri("http://localhost:4200"))
                .build();

    }


}
