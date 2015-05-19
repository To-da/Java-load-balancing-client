package com.toda.javaloadbalancerserver.controller;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.UnknownHostException;
import java.util.Optional;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpstreamServerResource {
    private Logger log = getLogger(UpstreamServerResource.class);

    @RequestMapping("/message")
    String msg(ServletRequest request) throws UnknownHostException {
        logIncomingMessage(request, "I serving the message req to {}:{} / {}");
        return "Hello from " + request.getServerName() + ":" + request.getServerPort();
    }

    @RequestMapping("/param")
    String msg(@RequestHeader("Custom-Header") Optional<String> foo) {
        return "Fooo "+ foo.orElse("empty");
    }

    @RequestMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    void ping(ServletRequest request){
        logIncomingMessage(request, "I have been pinged from {}:{} /{}");
    }

    private void logIncomingMessage(ServletRequest request, String s) {
        log.info(s, request.getRemoteAddr(), request.getRemotePort(), request.getRemoteHost());
    }
}
