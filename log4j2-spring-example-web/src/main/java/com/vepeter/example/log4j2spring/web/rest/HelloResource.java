package com.vepeter.example.log4j2spring.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.vepeter.example.log4j2spring.web.model.HelloResponse;

@Component
@Produces("application/json")
public class HelloResource {

    private static Logger AUDIT_LOG = LogManager.getLogger("com.vepeter.example.log4j2spring.logger.audit");

    @GET
    @Path("{name}")
    public HelloResponse sayHello(@PathParam("name") String name) {
        AUDIT_LOG.info(name);
        return new HelloResponse(name);
    }

    @GET
    public HelloResponse sayHello() {
        AUDIT_LOG.info("unknown");
        return new HelloResponse("unknown");
    }

}
