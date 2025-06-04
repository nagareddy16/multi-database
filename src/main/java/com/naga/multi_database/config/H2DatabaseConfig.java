package com.naga.multi_database.config;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class H2DatabaseConfig {

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new Servlet() {
            @Override
            public void init(ServletConfig config) throws ServletException {

            }

            @Override
            public ServletConfig getServletConfig() {
                return null;
            }

            @Override
            public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

            }

            @Override
            public String getServletInfo() {
                return null;
            }

            @Override
            public void destroy() {

            }
        });
        registration.addUrlMappings("/console/*");
        return registration;
    }
}
