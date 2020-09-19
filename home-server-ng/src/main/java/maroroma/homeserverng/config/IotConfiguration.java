package maroroma.homeserverng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Conf spécifique IOT
 */
@Configuration
public class IotConfiguration {

    /**
     * Va permettre de programmer des taches au niveau IOT
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler iotTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "iotTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
