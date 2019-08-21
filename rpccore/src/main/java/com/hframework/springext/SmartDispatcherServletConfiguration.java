package com.hframework.springext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hframework.springext.requestmapping.SmartRequestMappingHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
@EnableAsync
public class SmartDispatcherServletConfiguration {
  @Autowired
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @Bean(name="smartRequestMappingHandlerMapping")
  public SmartRequestMappingHandlerMapping smartRequestMappingHandlerMapping() {
    return new SmartRequestMappingHandlerMapping(requestMappingHandlerMapping, new Object[0]);
  }

}
