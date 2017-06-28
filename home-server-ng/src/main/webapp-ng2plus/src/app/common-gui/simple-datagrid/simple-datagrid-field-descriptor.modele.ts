import { ReflectionTools } from './../../shared/reflection-tools.service';
/**
 * Descripteur pour les données à afficher dans une colonne de la datagrid
 * @export
 * @class SimpleDatagridFieldDescriptor
 */
export class SimpleDatagridFieldDescriptor {

    /**
     * Nom du champ de données à afficher.
     * @type {string}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public fieldName: string;
    /**
     * Affichage du nom de champ dans l'entête de la colonne.
     * @type {string}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public displayName: string;


    /**
     * Détermine si la colonne dispose d'un titre.
     * @type {string}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public hasDisplayName = false;

    /**
     * Détermine si la colonne est triable
     * @type {boolean}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isSortable: boolean;

    /**
     * Détermine si la colonne est triée.
     * @type {boolean}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isSorted: boolean;
    /**
     * Détermine si la colonne est triée de manière inverse
     * @type {boolean}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isReverseSort: boolean;
    /**
     * Masquer la colonne sur les petits écrans.
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public hideForSmallDevice = false;

    /**
     * Masque la colonne sur les grands écrans
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public hideForBigDevice = false;

    /**
     * Détermine si l'item est zoomable
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public zoomable = false;

    /**
     * Affichage de texte ?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isTextField = true;


    /**
     * Zone de saisie.
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isTextBox = false;


    /**
     * Lien clickable
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isLink = false;

    public linkLabelField: string;

    /**
     * Affichage d'icone ?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isGlyphiconField = false;
    /**
     * Affichage d'un interrupteur ?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isOnOffField = false;
    /**
     * Affichage de boutons ?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public isActionField = false;
    /**
     * Affichage du bouton de sauvegarde ?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public hasSaveButton = false;

    /**
     * Affichage d'un bouton de suppression ?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public hasDeleteButton = false;
    /**
     * Affichage du bouton d'export?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public hasExportButton = false;
    /**
     * Affichage du bouton d'import?
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public hasImportButton = false;



    /**
     * Creates an instance of SimpleDatagridFieldDescriptor.
     * @param {string} fieldName
     * @memberOf SimpleDatagridFieldDescriptor
     */
    constructor(fieldName: string) {
        this.fieldName = fieldName;
    }

    /**
     * Permet de récupérer la donnée à afficher
     * @param {*} data
     * @returns {*}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public resolveData(item: any): any {
        return ReflectionTools.resolveData(item, this.fieldName);
    }

    public resolveLinkLabel(item: any): any {
        if (this.linkLabelField) {
            return ReflectionTools.resolveData(item, this.linkLabelField);
        } else {
            return this.resolveData(item);
        }
    }

    /**
     * PErmet d'écrire la donnée dans le champ correspondant.
     * @param {*} item
     * @param {*} dataToWrite
     * @returns {void}
     * @memberOf SimpleDatagridFieldDescriptor
     */
    public writeData(item: any, dataToWrite: any): void {
        return ReflectionTools.writeData(item, this.fieldName, dataToWrite);
    }

}
