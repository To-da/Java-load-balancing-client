package com.toda.hystrixclient;

import static com.toda.hystrixclient.JavaHystrixClientApplication.UPSTREAM_SERVER_CLIENT;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Configuration
@EnableAutoConfiguration
@EnableCircuitBreaker
public class JavaHystrixClientApplication {

    public static final String UPSTREAM_SERVER_CLIENT = "upstreamServerClient";

    public static void main(String[] args) {
        new SpringApplicationBuilder(JavaHystrixClientApplication.class).web(false).run(args);
    }
}

@Order(1)
@Component
class RestTemplateExample implements CommandLineRunner {

    private Logger log = getLogger(RestTemplateExample.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(String... strings) throws Exception {
        log.info("------------------------------");
        log.info("RestTemplate Example");

        for (int i = 0; i < 10; i++) {
            try {
                log.info(getMessage());
            }
            catch (RuntimeException e) {
               log.error("Exception in run {}. -> {}", i, e.getMessage());
            }
            Thread.sleep(2000);
        }
    }

    @HystrixCommand(fallbackMethod = "stubForGetMessage", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),//How many requests are needed in the time span to trigger the circuit breaker?
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "3000"), //After what time should the circuit breaker try a single request
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),  // After what time should the circuit breaker try a single request?
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")  //How many errors are allowed before the circuit breaker is activated ?
    })
    private String getMessage() {
        ResponseEntity<String> exchange = this.restTemplate.exchange(
                "http://" + UPSTREAM_SERVER_CLIENT + "/message",
                HttpMethod.GET, null, String.class);
        log.info(String.valueOf(exchange.getStatusCode()));
        return exchange.getBody();
    }

    private String stubForGetMessage() {
         return "FooResult";
    }
}

