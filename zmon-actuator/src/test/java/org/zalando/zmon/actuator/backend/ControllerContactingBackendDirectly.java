package org.zalando.zmon.actuator.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class ControllerContactingBackendDirectly {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.GET, value = "/timeConsumingCall")
    public String callBackendThroughRestTemplate() {
        restTemplate.delete(URI.create("http://localhost:9999/something"));

        return "success";
    }
}
