package maroroma.homeserverng.tools.repositories;

import java.util.function.Consumer;

/**
 * Processor par défaut, ne faisant rien, utilisé pour les {@link NanoRepository}.
 * @author rlevexie -
 *
 */
public class DefaultPreProcessor implements Consumer<Object> {

	@Override
	public void accept(final Object t) {
	}

}
