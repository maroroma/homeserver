package maroroma.homeserverng.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;
import maroroma.homeserverng.tools.cache.FileCache;
import maroroma.homeserverng.tools.cache.FileCacheManager;
import maroroma.homeserverng.tools.cache.OpenSimpleStringsKeyGenerator;
import maroroma.homeserverng.tools.cache.TwoLevelFileCache;
import maroroma.homeserverng.tools.config.HomeServerPluginPropertiesManager;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.CustomCollectors;
import maroroma.homeserverng.tools.helpers.FluentList;
import maroroma.homeserverng.tools.helpers.Tuple;
import maroroma.homeserverng.tools.needers.CacheNeed;
import maroroma.homeserverng.tools.needers.CacheNeeder;

/**
 * Classe de configuration pour la mise en place du cache.
 * <br />
 * Les caches sont déclarés en fonction des beans de type {@link CacheNeeder} qui sont trouvés dans le contexte.
 * <br />
 * Ainsi, si des plugins nécessite un cache, une implémentation de CacheNeeder présentée dans le contexte spring permettra
 * de connaitre le nom du cache qui sera à utilisé pour ce plugin
 * @author rlevexie
 *
 */
@Configuration
@Log4j2
public class CacheConfiguration extends CachingConfigurerSupport {

	/**
	 * Liste des beans demandeurs de cache.
	 */
	@Autowired(required = false)
	private List<CacheNeeder> cacheNeeders;

	/**
	 * Va permettre de récupérer les datasources de configuration.
	 */
	@Autowired
	private HomeServerPluginPropertiesManager propertiesSource;

	/**
	 * Construit le {@link CacheManager} en fonction des {@link CacheNeeder} présents dans le contexte.
	 * <br /> si aucun {@link CacheNeeder} n'est trouvé, un cache par défaut est créé.
	 * @return -
	 */
	@Bean
	@Override
	public CacheManager cacheManager() {

		CacheManager returnValue = null;

		if (this.cacheNeeders != null &&  !this.cacheNeeders.isEmpty()) {
			
			List<CacheManager> managers = 
			
			FluentList.<CacheManager>create()
			.addAnd(this.createSimpleCacheManager())
			.addAnd(this.createFileCacheManager())
			.addAnd(this.createTwoLevelFileCacheManager());
			// on crée un cache manager composite, permettant de proposer une instance unique injectable
			returnValue = new CompositeCacheManager(managers.toArray(new CacheManager[managers.size()]));

			
//			returnValue = new CompositeCacheManager(cacheManagers.toArray(new CacheManager[cacheManagers.size()]));
		} else {
			log.info("Aucun caches simple demandés");
			returnValue = new ConcurrentMapCacheManager("DEFAULT");
		}

		return returnValue;
	}

	/**
	 * Permet de créer les caches de fichiers simples.
	 * @return -
	 */
	private CacheManager createFileCacheManager() {
		return this.createFileCacheManager("[homeserver - caches] - Caches Fichiers simple demandés : {}",
				cn -> cn.getFileCaches(),
				tuple -> new FileCache(tuple.getItem1(), tuple.getItem2()));
	}

	/**
	 * Permet de créer le cache manager de base.
	 * @return -
	 */
	private CacheManager createSimpleCacheManager() {

		List<String> cacheNames =
				this.cacheNeeders.stream()
				.map(needer -> needer.getCacheNeeded().getSimpleCaches())
				.collect(CustomCollectors.toAgregatedList());


		log.info("[homeserver - caches] - Caches demandés : {}", cacheNames.stream().collect(CustomCollectors.toConcatenatedString(";")));

		return new ConcurrentMapCacheManager(cacheNames.toArray(new String[cacheNames.size()]));

	}

	/**
	 * Permet de créer le cache manager pour les caches de type {@link TwoLevelFileCache}.
	 * @return -
	 */
	private CacheManager createTwoLevelFileCacheManager() {
		return this.createFileCacheManager("[homeserver - caches] - Caches Fichiers à deux niveaux demandés : {}",
				cn -> cn.getTwoLevelFileCaches(),
				tuple -> new TwoLevelFileCache(tuple.getItem1(), tuple.getItem2()));
	}
	
	/**
	 * Méthode générique de création de {@link FileCacheManager}.
	 * @param logMsg message de log spécifique
	 * @param cacheNeededExtractor extraction des informations de configuration du {@link CacheNeed}
	 * @param cacheCreator création de l'implémentation du cache.
	 * @param <T> type de cache attendu en sortie
	 * @return -
	 */
	private <T extends Cache> CacheManager createFileCacheManager(final String logMsg,
			final Function<CacheNeed, List<Tuple<String, String>>> cacheNeededExtractor, 
			final Function<Tuple<String, HomeServerPropertyHolder>, T> cacheCreator) {
		
		
		List<Tuple<String, HomeServerPropertyHolder>> fileCacheConfigurations = new ArrayList<>();

		for (CacheNeeder needer : this.cacheNeeders) {
			for (Tuple<String, String> tuple : cacheNeededExtractor.apply(needer.getCacheNeeded())) {
				Tuple<String, HomeServerPropertyHolder> convertedTuple = Tuple
						.<String, HomeServerPropertyHolder>from(
								tuple.getItem1(),
								this.propertiesSource.getPropertyHolder(tuple.getItem2()));
				fileCacheConfigurations.add(convertedTuple);
			}
		}

		log.info(logMsg,
				fileCacheConfigurations.stream()
				.map(tuple -> tuple.getItem1())
				.collect(CustomCollectors.toConcatenatedString(";")));

		return new FileCacheManager<>(fileCacheConfigurations, 
				cacheCreator);
	}

	@Override
	@Bean
	public
	KeyGenerator keyGenerator() {
		return new OpenSimpleStringsKeyGenerator();
	}

}
