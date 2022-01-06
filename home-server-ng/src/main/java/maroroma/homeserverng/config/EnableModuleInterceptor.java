package maroroma.homeserverng.config;

import lombok.extern.slf4j.Slf4j;
import maroroma.homeserverng.tools.annotations.HomeServerModuleDescriptor;
import maroroma.homeserverng.tools.config.HomeServerModuleManager;
import maroroma.homeserverng.tools.exceptions.DisableModuleException;
import maroroma.homeserverng.tools.helpers.BeanDefinitionsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Permet de controller qu'un appel rest est bien appelé sur un module activé.
 * <br /> si le module est désactivé, on fait claqué l'appel.
 * @author RLEVEXIE
 *
 */
@Component
@Slf4j
public class EnableModuleInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Permet de retourner le module et son état.
	 */
	@Autowired
	private HomeServerModuleManager service;


	@Override
	public boolean preHandle(final HttpServletRequest request, final  HttpServletResponse response, final  Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			// cast de la méthode
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			// récupération du descriptor
			HomeServerModuleDescriptor descriptor =  BeanDefinitionsHelper.getModuleDescriptor(handlerMethod.getBeanType());

			if (descriptor != null) {
				// on rejete les appels sur les modules désactivés.
				if (!this.service.isModuleEnabled(descriptor.moduleId())) {
					throw new DisableModuleException(descriptor.moduleId());
				}
			} else {
				log.debug("L'appel REST ne correspond pas à un module du serveur.");
			}
		}


		return true;
	}



}
