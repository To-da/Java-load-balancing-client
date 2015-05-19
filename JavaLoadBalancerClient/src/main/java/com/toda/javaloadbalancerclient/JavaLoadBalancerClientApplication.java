package com.toda.javaloadbalancerclient;

import static com.toda.javaloadbalancerclient.JavaLoadBalancerClientApplication.UPSTREAM_SERVER_CLIENT;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.toda.javaloadbalancerclient.config.ExcludeFromComponentScan;
import com.toda.javaloadbalancerclient.config.clientsconfig.LoadBalancerConfig;

@SpringBootApplication
@ComponentScan(excludeFilters={
        @Filter(type = FilterType.ANNOTATION, value = ExcludeFromComponentScan.class)})
@RibbonClient(name = JavaLoadBalancerClientApplication.UPSTREAM_SERVER_CLIENT, configuration = LoadBalancerConfig.class)
public class JavaLoadBalancerClientApplication {

    public static final String UPSTREAM_SERVER_CLIENT = "upstreamServerClient";

    public static void main(String[] args) {
        new SpringApplicationBuilder(JavaLoadBalancerClientApplication.class).web(false).run(args);
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

        for (int i = 0; i < 20; i++) {
            try {
                log.info(getMessage());
            }
            catch (RuntimeException e) {
               log.error("Exception in run {}. -> {}", i, e.getMessage());
            }
            Thread.sleep(1000);
        }
    }

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

