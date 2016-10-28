package eu.nimble.core.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan
@Configuration
@EnableCircuitBreaker
@EnableAutoConfiguration
@EnableEurekaClient
@EnableFeignClients
@RestController
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private UserRegistrationClient userClient;

    @RequestMapping("/")
    public String home() {

        logger.info("Requested Homepage");
        logger.info("Sample 1");
        logger.info("Sample 3");
        return "HOMEPAGE";
    }

    @RequestMapping("/user/{userId}")
    public String getUser(@PathVariable("userId") String userId) {
        return userClient.getUser(userId);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
}

@FeignClient(value = "user-registration", fallback = UserRegistrationClientFallback.class)
interface UserRegistrationClient {
    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}")
    String getUser(@PathVariable("userId") String userId);
}

@Component
class UserRegistrationClientFallback implements UserRegistrationClient {
    @Override
    public String getUser(@PathVariable("userId") String userId) {
        return "fallback user";
    }
}