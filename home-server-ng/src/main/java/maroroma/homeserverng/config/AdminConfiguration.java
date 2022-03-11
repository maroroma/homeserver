package maroroma.homeserverng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class AdminConfiguration {
    /**
     * Va permettre de programmer des taches au niveau admin
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler adminTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "adminTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
