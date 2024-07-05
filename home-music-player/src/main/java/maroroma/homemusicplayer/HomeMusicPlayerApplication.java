package maroroma.homemusicplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Classe principale de l'application.
 * @author RLEVEXIE
 *
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
public class HomeMusicPlayerApplication {

	/**
	 * boucle principale .
	 * @param args -
	 */
	public static void main(final String[] args) {
		SpringApplication.run(HomeMusicPlayerApplication.class, args);
	}

}
