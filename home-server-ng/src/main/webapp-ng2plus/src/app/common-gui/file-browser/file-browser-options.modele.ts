import { FileBrowserResolver } from './file-browser-resolver.modele';

/**
 * Options de configuration pour le FileBrowser
 * @export
 * @class FileBrowserOptions
 */
export class FileBrowserOptions {
    /**
     * Détermine si la création de répertoire est possible.
     * @memberOf FileBrowserComponent
     */
    public allowDirectoryCreation = false;

    /**
     * Détermine si la sélection d'items est possible.
     * @memberof FileBrowserOptions
     */
    public allowItemSelection = false;

    /**
     * Détermine si le rafraichissement est possible
     * @memberof FileBrowserOptions
     */
    public allowRefresh = false;

    /**
     * Détermine si un téléchargement est possible.
     */
    public allowDownload = false;


    /**
     * Détermine si un upload est possible
     */
    public allowUpload = false;
    /**
     * 
     * Détermine si la barre d'outil doit être affichée.
     * 
     * @memberof FileBrowserOptions
     */
    public displayToolBar = false;

    /**
     * Détermine si la création de répertoire possible sur le répertoire racine.
     * @memberOf FileBrowserComponent
     */
    public allowDirectoryCreationOnStarter = false;

    /**
     * Détermine si la suppression de répertoire est possible
     * @memberOf FileBrowserComponent
     */
    public allowDeletion = false;

    /**
     * PErmet de renommer un fichier
     */
    public allowRenaming = false;

    /**
     * Resolver utilisé pour les opérations distantes.
     * 
     * @type {FileBrowserResolver}@memberof FileBrowserOptions
     */
    public resolver: FileBrowserResolver;

    /**
     * Détermine si on doit afficher le bouton pour remonter en haut de la fenêtre
     */
    public displayScrollToTop = false;


}
