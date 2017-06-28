package maroroma.homeserverng.administration.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import maroroma.homeserverng.tools.config.HomeServerModuleHandler;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.Tuple;

/**
 * Permet de porter l'ensemble des éléments de configuration du homeserver pour un export.
 * <br /> Cela comprend les propriétés, les repositories et l'état d'activation des plugins.
 * @author rlevexie
 *
 */
@Data
public class FullConfigurationHolder {
	
	private List<HomeServerPropertyHolder> properties;
	private List<Tuple<String, String>> base64Repositories;
	private List<HomeServerModuleHandler> modules;
	
	public FullConfigurationHolder() {
		this.properties = new ArrayList<>();
		this.base64Repositories = new ArrayList<>();
		this.modules = new ArrayList<>();
	}

}
