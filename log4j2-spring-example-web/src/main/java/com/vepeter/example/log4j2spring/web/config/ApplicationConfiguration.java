package com.vepeter.example.log4j2spring.web.config;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.vepeter.example.log4j2spring.web.rest.EventResource;
import com.vepeter.example.log4j2spring.web.rest.HelloResource;

@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
@ComponentScan(basePackages = { "com.vepeter.example.log4j2spring.logger.config", "com.vepeter.example.log4j2spring.web.rest" })
public class ApplicationConfiguration {

    @Autowired
    private SpringBus bus;

    @Bean
    public JacksonJsonProvider jsonProvider() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        return new JacksonJsonProvider(mapper);
    }

    @Bean
    public Server helloServiceRest(HelloResource helloResource) {
        return createEndpoint("/hello", helloResource);
    }

    @Bean
    public Server eventsServiceRest(EventResource eventResource) {
        return createEndpoint("/events", eventResource);
    }

    private Server createEndpoint(String address, Object resource) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setServiceBean(resource);
        endpoint.setAddress(address);
        endpoint.setBus(bus);
        endpoint.setProvider(jsonProvider());
        return endpoint.create();
    }

}
