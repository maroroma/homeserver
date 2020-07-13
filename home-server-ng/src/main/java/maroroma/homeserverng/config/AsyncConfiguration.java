package maroroma.homeserverng.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Bean de configuration pour la gestion des méthodes asynchrones du projet.
 * @author rlevexie
 *
 */
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

	
	/**
	 * Taille par défaut pour la file d'attente de threads.
	 */
	private static final int DEFAULT_QUEUE_CAPACITY = 10;
	
	/**
	 * Taille par défaut pour la taille maximum du pool de thread.
	 */
	private static final int DEFAULT_MAX_POOL_SIZE = 10;
	
	/**
	 * Taille par défaut du core pool size.
	 */
	private static final int DEFAULT_CORE_POOL_SIZE = 5;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(DEFAULT_CORE_POOL_SIZE);
        executor.setMaxPoolSize(DEFAULT_MAX_POOL_SIZE);
        executor.setQueueCapacity(DEFAULT_QUEUE_CAPACITY);
        executor.initialize();
        return executor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}



}
