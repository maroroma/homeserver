/**
 * Classe utilitaire pour la désérialisation json.
 * @export
 * @class JsonTools
 */
export class JsonTools {


    /**
     * Convertit un objet json brut en liste d'objet.
     * La fonction de conversion est fournie en entrée.
     * @static
     * @template T
     * @param {*} rawList
     * @param {(rawList: any) => T} convertItemFunction
     * @returns {Array<T>}
     * @memberOf JsonTools
     */
    public static map<T>(rawList: any, convertItemFunction: (rawList: any) => T): Array<T> {
        const returnValue = new Array<T>();
        if (rawList) {
            for (const rawItem of rawList) {
                returnValue.push(convertItemFunction(rawItem));
            }
        }

        return returnValue;
    }
}