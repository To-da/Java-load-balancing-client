package eurekawebappclient.controller;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.UnknownHostException;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UpstreamServerResource {
    private Logger log = getLogger(UpstreamServerResource.class);

    public static final String UPSTREAM_SERVER_CLIENT = "eureka-upstreamServer-client";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/message/{number}")
    String msg(ServletRequest request, @PathVariable int number)  throws UnknownHostException {
        final String message = getMessage(number);
        return "Received message: " +  message;
    }

    private String getMessage(int i) {
        ResponseEntity<String> exchange = this.restTemplate.exchange(
                "http://" + UPSTREAM_SERVER_CLIENT + "/message/{number}",
                HttpMethod.GET, null, String.class, i);
        log.info("Sent msg nr.{} with status code: {}", i, String.valueOf(exchange.getStatusCode()));
        return exchange.getBody();
    }
}
