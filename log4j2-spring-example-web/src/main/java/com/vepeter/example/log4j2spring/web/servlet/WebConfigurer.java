package com.vepeter.example.log4j2spring.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vepeter.example.log4j2spring.web.config.ApplicationConfiguration;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
public class WebConfigurer implements ServletContextListener {

    private final Logger LOG = LoggerFactory.getLogger(WebConfigurer.class);

    public AnnotationConfigWebApplicationContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        LOG.debug("Configuring Spring root application context");

        context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfiguration.class);
        context.refresh();

        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

        initCxfServlet(servletContext, context);

        LOG.debug("Web application fully configured");
    }

    /**
     * Initializes CXF.
     */
    private ServletRegistration.Dynamic initCxfServlet(ServletContext servletContext,
            AnnotationConfigWebApplicationContext rootContext) {
        ServletRegistration.Dynamic cxfServlet = servletContext.addServlet("cxf", new CXFServlet());
        cxfServlet.addMapping("/api/*");
        cxfServlet.setLoadOnStartup(1);
        cxfServlet.setAsyncSupported(true);
        return cxfServlet;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getRequiredWebApplicationContext(sce.getServletContext());
        AnnotationConfigWebApplicationContext.class.cast(webApplicationContext).close();
    }
}
