package com.frs.apigateway.Filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ApiTokenFilter  extends AbstractGatewayFilterFactory<ApiTokenFilter.Config> {

    private final WebClient webClient;

    private String AUTH_SERVER_URL = "http://127.0.0.1:8085";

    private String AUTH_SERVER_URI = "/auth/authenticate";
    public ApiTokenFilter() {
        super(Config.class);
        this.webClient = WebClient.builder().baseUrl(AUTH_SERVER_URL).build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Extract JWT token from the request
            String jwtToken = extractJwtToken(exchange.getRequest().getHeaders());
            String ServiceId = getServiceId(String.valueOf(exchange.getRequest().getPath()),1);
            if(jwtToken == null){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            // Call authentication server to verify JWT and get API token
            return authenticate(jwtToken,ServiceId)
                    .flatMap(apiToken -> {
                        // Set the API token in the request headers
                        exchange.getRequest().mutate().headers(httpHeaders -> {
                            httpHeaders.add("Api-Token", apiToken);
                        });
                        return chain.filter(exchange);
                    })
                    .onErrorResume(throwable -> {
                        // Handle authentication error
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }).then();
        };
    }

    private String extractJwtToken(HttpHeaders headers) {
        List<String> authorizationHeaders = headers.get(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
            String authorizationHeader = authorizationHeaders.get(0);
            if (authorizationHeader.startsWith("Bearer ")) {
                return authorizationHeader.substring(7);
            }
        }
        return null;
    }

    private Mono<String> authenticate(String jwtToken, String ServiceId) {
        return webClient.post()
                .uri(AUTH_SERVER_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .header("Service-Id", ServiceId)
                .retrieve()
                .bodyToMono(String.class);
    }

    private String getServiceId(String uri,int index){
        String[] pathComponents = uri.split("/");
        String serviceName = null;
        if (pathComponents.length > index) {
            serviceName = pathComponents[index];
        }
        return serviceName;
    }

    public static class Config {
        // Put the configuration properties
    }
}
