import { ReflectionTools } from './reflection-tools.service';
/**
 * Classe utilitaire pour la gestion du tri.
 * @export
 * @class SortTools
 */
export class SortTools {

    /**
     * Retourne une fonction de tri pour deux items, avec la gestion dynamique du nom de champ
     * et la gestion du tri inversé.
     * @static
     * @template T
     * @param {string} fieldName
     * @param {boolean} reverse
     * @returns {(item1: T, item2: T) => number}
     * @memberOf SortTools
     */
    public static sorter<T>(fieldName: string, reverse: boolean): (item1: T, item2: T) => number {
        return ((item1: T, item2: T) => {

            // récupération des deux valeurs à comparer sur l'objet pour le champ pointé
            let item1Field = ReflectionTools.resolveData(item1, fieldName);
            let item2Field = ReflectionTools.resolveData(item2, fieldName);

            // cas particulier des strings, qui doivent être normalisées en minuscules pour assurer une 
            // comparaison qui ressemble à qqc ....
            if (ReflectionTools.isString(item1Field)) {
                item1Field = (item1Field as string).toLocaleLowerCase();
                item2Field = (item2Field as string).toLocaleLowerCase();
            }


            // on détermine si le tri doit être inversé
            const reverseFactor = reverse ? -1 : 1;

            if (item1Field > item2Field) {
                return 1 * reverseFactor;
            } else if (item1Field < item2Field) {
                return -1 * reverseFactor;
            } else {
                return 0;
            }
        });
    }

    /**
     * retourne une fonction de tri croissant pour le champ donné.
     * @static
     * @template T
     * @param {string} fieldName
     * @returns {(item1: T, item2: T) => number}
     * @memberOf SortTools
     */
    public static asc<T>(fieldName: string): (item1: T, item2: T) => number {
        return SortTools.sorter(fieldName, false);
    }

    /**
     * Retourne une fonction de tri décroissant pour un champ donné.
     * @static
     * @template T
     * @param {string} fieldName
     * @returns {(item1: T, item2: T) => number}
     * @memberOf SortTools
     */
    public static desc<T>(fieldName: string): (item1: T, item2: T) => number {
        return SortTools.sorter(fieldName, true);
    }
}
