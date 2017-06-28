/**
 * Description d'un module du homeserver.
 * @export
 * @class ModuleHandler
 */
export class ModuleHandler {
    /**
     * Identifiant
     * @type {string}
     * @memberOf ModuleHandler
     */
    public moduleId: string;
    /**
     * Module activé ?
     * @type {boolean}
     * @memberOf ModuleHandler
     */
    public enabled: boolean;
    /**
     * Description
     * @type {string}
     * @memberOf ModuleHandler
     */
    public moduleDescription: string;
    /**
     * Nom pour l'affichage
     * @type {string}
     * @memberOf ModuleHandler
     */
    public displayName: string;
    /**
     * Classe css pour le menu
     * @type {string}
     * @memberOf ModuleHandler
     */
    public cssMenu: string;
    /**
     * Plugin coté serveur ?
     * @type {boolean}
     * @memberOf ModuleHandler
     */
    public hasServerSide: boolean;
    /**
     * Plugin coté client ?
     * @type {boolean}
     * @memberOf ModuleHandler
     */
    public hasClientSide: boolean;
    /**
     * Plugin en lecture seule ?
     * @type {boolean}
     * @memberOf ModuleHandler
     */
    public readOnly: boolean;



    /**
     * Permet de convertir un flux json en ModuleHandler.
     * @private
     * @param {*} rawModule
     * @returns {ModuleHandler}
     * @memberOf AdministrationService
     */
    public static fromRaw(rawModule: any): ModuleHandler {
        const moduleHandler = new ModuleHandler();
        moduleHandler.cssMenu = rawModule.cssMenu;
        moduleHandler.displayName = rawModule.displayName;
        moduleHandler.enabled = rawModule.enabled;
        moduleHandler.hasClientSide = rawModule.hasClientSide;
        moduleHandler.hasServerSide = rawModule.hasServerSide;
        moduleHandler.readOnly = rawModule.readOnly;
        moduleHandler.moduleId = rawModule.moduleId;
        moduleHandler.moduleDescription = rawModule.moduleDescription;
        return moduleHandler;
    }
}

