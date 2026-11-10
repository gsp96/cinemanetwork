package koseungpyo.movie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("koseungpyo.movie")
public class AppConfig implements WebMvcConfigurer {
	@Value("${attachPath}")
	private String attachPath;
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/cinemanetwork").setViewName("main");
		registry.addViewController("/admin").setViewName("admin/main");
   }
	
	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry) { 
		registry.addResourceHandler("res/**").addResourceLocations("/WEB-INF/view/res/");
		registry.addResourceHandler("attach/**").addResourceLocations("file:///" + attachPath + "/");
	}	
}