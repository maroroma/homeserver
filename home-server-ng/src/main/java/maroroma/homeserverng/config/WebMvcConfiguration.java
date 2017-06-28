package maroroma.homeserverng.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.needers.StaticsNeeder;


/**
 * Bean de configuration pour la mise en place du serveur static et des intercepteurs spécifiques.
 * @author RLEVEXIE
 *
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Log4j2
public class WebMvcConfiguration  {

	
	/**
	 * Création de l'adapter de configuration pour la gestion des fichiers statiques de l'application.
	 * @return -
	 */
	@Bean
	public StaticHtmlConfigurerAdapter staticHtmlConfigurerAdapter() {
		return new StaticHtmlConfigurerAdapter();
	}
	

	/**
	 * Adapter pour la gestion des différents répertoires gérés par le serveur.
	 * @author rlevexie
	 *
	 */
	public class StaticHtmlConfigurerAdapter extends WebMvcConfigurerAdapter {

		/**
		 * Liste des composants nécessitant l'ajout de liens statiques sur le serveur http.
		 */
		@Autowired
		private List<StaticsNeeder> staticsNeeders;
		
		/**
		 * Liste des interceptors.
		 */
		@Autowired
		private List<AsyncHandlerInterceptor> interceptors;

//		/**
//		 * Gestion de la page d'index.
//		 */
//		@Override
//		public void addViewControllers(final ViewControllerRegistry registry) {
//			registry.addViewController("/").setViewName("forward:index.html");
//		}
		
		/**
		 * Gestion de la redirection vers les pages statiques.
		 */
		@Override
		public void addResourceHandlers(final ResourceHandlerRegistry registry) {
			
			for (StaticsNeeder staticsNeeder : staticsNeeders) {
				registry.addResourceHandler(staticsNeeder.getHandler()).addResourceLocations(staticsNeeder.getLocations());
				log.info("[homeserver - localdomain] - staticpath : [" + staticsNeeder.getHandler() + "] - " + staticsNeeder.getLocations());
			}
			
		}

		@Override
		public void addInterceptors(final InterceptorRegistry registry) {
			for (AsyncHandlerInterceptor asyncHandlerInterceptor : interceptors) {
				registry.addInterceptor(asyncHandlerInterceptor);	
			}
			
		}

		@Override
		public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		    builder
		    	.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		    	.modules(new JavaTimeModule());
		    converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
		}

	}
}
