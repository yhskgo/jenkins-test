package com.robotics.demowebflux;

import java.util.HashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class RoutsConfig implements ApplicationContextAware, WebFluxConfigurer {
	
	ApplicationContext context;

	@Bean
	public RouterFunction<ServerResponse> route(FirstHandler handler) {
		
		return RouterFunctions.route(
				RequestPredicates.GET("/hello").and(
						RequestPredicates.accept(MediaType.TEXT_PLAIN)),
						handler::hello);
	}
	
	@Bean
	public RouterFunction<ServerResponse> index() {
		return RouterFunctions.route(RequestPredicates.GET("/").and(RequestPredicates.accept(MediaType.TEXT_HTML)),
				req->ServerResponse.ok().render("index", new HashMap<Object, Object>()));
	}
	
	@Bean
	public ITemplateResolver theymeleafTemplateResolver() {
		final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(this.context);
		resolver.setPrefix("classpath:static/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCacheable(false);
		resolver.setCheckExistence(false);
		
		return resolver;
	}
	
	@Bean
	public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {
		SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
		
		templateEngine.setTemplateResolver(theymeleafTemplateResolver());
		return templateEngine;
	}
	
	@Bean
	public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {
		ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
		viewResolver.setTemplateEngine(thymeleafTemplateEngine());
		
		return viewResolver;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.viewResolver(thymeleafReactiveViewResolver());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		
	}
	
	

}
