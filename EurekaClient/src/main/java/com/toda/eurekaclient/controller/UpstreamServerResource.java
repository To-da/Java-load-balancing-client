package com.toda.eurekaclient.controller;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.UnknownHostException;
import java.util.Optional;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpstreamServerResource {
    private Logger log = getLogger(UpstreamServerResource.class);

    @RequestMapping("/message/{number}")
    String msg(ServletRequest request, @PathVariable int number)  throws UnknownHostException {
        logIncomingMessage(request, number, "I serving the message req to {}:{} / {} with nr {}");
        return "Hello from " + request.getServerName() + ":" + request.getServerPort() + " with MSG number " +  number;
    }

    @RequestMapping("/param")
    String msg(@RequestHeader("Custom-Header") Optional<String> foo) {
        return "Fooo "+ foo.orElse("empty");
    }

    @RequestMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    void ping(ServletRequest request){
        logIncomingMessage(request, 0, "I have been pinged from {}:{} /{} {}");
    }

    private void logIncomingMessage(ServletRequest request, int nr, String s) {
        log.info(s, request.getRemoteAddr(), request.getRemotePort(), request.getRemoteHost(), nr);
    }
}
