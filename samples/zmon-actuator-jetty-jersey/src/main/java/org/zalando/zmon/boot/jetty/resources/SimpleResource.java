package org.zalando.zmon.boot.jetty.resources;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Just for example.
 *
 * @author jbellmann
 *
 */
@Component
@Path("/simple")
public class SimpleResource {

    @GET
    @Path("/{id}/complex")
    public String invoke(@PathParam("id") String id) {
        return id;
    }
}
