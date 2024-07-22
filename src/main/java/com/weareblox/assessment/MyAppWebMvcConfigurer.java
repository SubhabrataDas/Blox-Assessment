package com.weareblox.assessment;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.PathResourceResolver;

import reactor.core.publisher.Mono;

@Configuration
public class MyAppWebMvcConfigurer  implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/{path:(?!static$)[^.]*}/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Mono<Resource> getResource(String resourcePath, Resource location){
                    Resource requestedResource;
					try {
						requestedResource = location.createRelative(resourcePath);
						 return requestedResource.exists() && requestedResource.isReadable() ? 
		                    		Mono.just(requestedResource) : Mono.just(new ClassPathResource("/static/index.html"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						 return Mono.error(e);
					}	
                }
            });
    }
}