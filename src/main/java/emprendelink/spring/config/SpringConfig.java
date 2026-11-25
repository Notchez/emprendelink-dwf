package emprendelink.spring.config;

import emprendelink.spring.security.RouteRules;
import emprendelink.spring.security.AccessPolicy;
import emprendelink.spring.rest.ApiErrorPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SpringConfig {
    @Bean public RouteRules routeRules() { return new RouteRules(); }
    @Bean public AccessPolicy accessPolicy(RouteRules rules) { return new AccessPolicy(rules); }
    @Bean public ApiErrorPolicy apiErrorPolicy() { return new ApiErrorPolicy(); }
}
