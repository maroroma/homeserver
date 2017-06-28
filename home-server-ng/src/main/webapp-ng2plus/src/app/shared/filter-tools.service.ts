import { ReflectionTools } from './reflection-tools.service';
export class FilterTools {
    /**
     * PErmet de générer un filtre simple pour tester la présence d'une chaine dans une liste d'item pour un champ donné.
     * @static
     * @template T type d'item de la liste à filtrer
     * @param {string} searchValue valeur à rechercher
     * @param {string} [fieldName] champ de l'item pour le test.
     * @returns {(toTest: T) => boolean} prédicat à appliquer.
     * @memberOf FilterTools
     */
    public static simpleFilter<T>(searchValue: string, fieldName?: string): (toTest: T) => boolean {
        return (toTest) => {
            if (toTest && searchValue) {
                const resolvedData = ReflectionTools.resolveData(toTest, fieldName) as string;
                if (resolvedData) {
                    return resolvedData.toLowerCase().indexOf(searchValue.toLowerCase()) >= 0;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        };
    }


    /**
     * Détermine si la donnée est contenue dans la liste.
     * @static
     * @param {Array<string>} list
     * @param {string} data
     * @returns {boolean}
     * @memberOf FilterTools
     */
    public static contains(list: Array<string>, data: string): boolean {
        return list.filter(FilterTools.simpleFilter(data)).length > 0;
    }

    /**
     * Retourne le dernier élément d'une liste.
     * @static
     * @template T
     * @param {Array<T>} list
     * @returns {T}
     * @memberOf FilterTools
     */
    public static last<T>(list: Array<T>): T {
        if (list && list.length > 0) {
            return list[list.length - 1];
        } else {
            return null;
        }
    }

    /**
     * Retourne l'index du premier item répondant au prédicat en entrée.
     * REtourne -1 si aucun item est trouvé, ou si plusieurs items rencontrés.
     * @static
     * @template T
     * @param {Array<T>} list
     * @param {(item: T) => boolean} predicate
     * @returns {number}
     * @memberOf FilterTools
     */
    public static indexOf<T>(list: Array<T>, predicate: (item: T) => boolean): number {
        let indexOf = 0;
        const res = list.filter((item, index) => {
            if (predicate(item)) {
                indexOf = index;
                return true;
            }
            return false;
        });

        if (res.length > 1 || res.length === 0) {
            return -1;
        } else {
            return indexOf;
        }

    }

    /**
     * Construit un prédicat en réalisant un et logique sur une liste de prédicats.
     * @static
     * @template T
     * @param {Array<(item: T) => boolean>} predicates
     * @returns {(item: T) => boolean}
     * @memberOf FilterTools
     */
    public static and<T>(predicates: Array<(item: T) => boolean>): (item: T) => boolean {
        return (toTest) => {
            // console.log('and aggregage of ', predicates);
            let returnValue = true;

            predicates.forEach(predicate => {
                returnValue = returnValue && predicate(toTest);
            });

            return returnValue;
        };
    }

    /**
     * Génère un prédicat en réalisant un ou logique entre liste de prédicats.
     * @static
     * @template T
     * @param {Array<(item: T) => boolean>} predicates
     * @returns {(item: T) => boolean}
     * @memberOf FilterTools
     */
    public static or<T>(predicates: Array<(item: T) => boolean>): (item: T) => boolean {
        return (toTest) => {
            // console.log('or aggregage of ', predicates);
            let returnValue = false;

            predicates.forEach(predicate => {
                returnValue = returnValue || predicate(toTest);
            });

            return returnValue;
        };
    }


    /**
     * Supprime tous les items de la liste répondant au prédicat en entrée.
     * @static
     * @template T
     * @param {Array<T>} list
     * @param {(item: T) => boolean} predicate
     * @returns {Array<T>}
     * @memberOf FilterTools
     */
    public static removeAny<T>(list: Array<T>, predicate: (item: T) => boolean): Array<T> {
        return list.filter(item => !predicate(item));
    }
}
