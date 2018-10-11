package org.zalando.boot.jetty.controller;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Component
@Path("/simple")
public class SimpleController {

    @GET
    @Path("/{id}/complex")
    public String invoke(@PathParam("id") String id) {
        return id;
    }
}
