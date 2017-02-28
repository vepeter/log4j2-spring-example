package com.vepeter.example.log4j2spring.web.rest;

import static org.apache.logging.log4j.ThreadContext.clearMap;
import static org.apache.logging.log4j.ThreadContext.put;

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
public class AnotherHelloResource {

    private static Logger AUDIT_LOG = LogManager.getLogger("com.vepeter.example.log4j2spring.logger.audit.anotherHello");

    @GET
    @Path("{name}")
    public HelloResponse sayHello(@PathParam("name") String name) {
        put("param", "param named");
        AUDIT_LOG.info(name);
        clearMap();
        return new HelloResponse(name);
    }

    @GET
    public HelloResponse sayHello() {
        put("param", "param default");
        AUDIT_LOG.info("unknown");
        clearMap();
        return new HelloResponse("unknown");
    }

}
