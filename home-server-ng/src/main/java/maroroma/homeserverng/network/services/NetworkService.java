package maroroma.homeserverng.network.services;

import java.util.List;

import maroroma.homeserverng.network.model.ServerDescriptor;
import maroroma.homeserverng.tools.exceptions.HomeServerException;

/**
 * Interface de définition du DAO pour la manipulation de la liste des serveurs.
 * @author rlevexie
 *
 */
public interface NetworkService {

	/**
	 * Retourne la liste des serveurs déclarés.
	 * @return -
	 * @throws HomeServerException  -
	 */
	List<ServerDescriptor> getServerDescriptors() throws HomeServerException;
	
	/**
	 * Enregistre la description d'un nouveau serveur.
	 * @param serverDescriptor -
	 * @return -
	 * @throws HomeServerException - 
	 */
	List<ServerDescriptor> saveServerDescriptor(ServerDescriptor serverDescriptor) throws HomeServerException;
	
	/**
	 * Supprime un serveur.
	 * @param id -
	 * @return -
	 * @throws HomeServerException 
	 */
	List<ServerDescriptor> deleteServerDescriptor(String id) throws HomeServerException;

	/**
	 * Retourne si le serveur est joignable.
	 * @param id -
	 * @return -
	 * @throws HomeServerException -
	 */
	boolean getServerStatus(String id) throws HomeServerException;

	/**
	 * Permet de mettre à jour un serveur.
	 * @param newServer nouvelle valeur pour le serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<ServerDescriptor> updateServer(ServerDescriptor newServer) throws HomeServerException;
	
	/**
	 * Permet de récupérer la liste des serveurs disponibles sur le réseau, pour faciliter
	 * l'ajout de serveur.
	 * @return -
	 * @throws HomeServerException -
	 */
	List<ServerDescriptor> getAvailableServer() throws HomeServerException;
	
}
