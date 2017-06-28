/**
 * Représentation d'un cache.
 * @export
 * @class Cache
 */
export class Cache {
    /**
     * Nom du cache. 
     * @type {string}
     * @memberOf Cache
     */
    public name: string;
    /**
     * 
     * NOmbre d'éléments dans le cache.
     * @type {number}
     * @memberOf Cache
     */
    public nbElements: number;

    /**
     * Reconstruction à partir du json.
     * @static
     * @param {*} rawCache
     * @returns {Cache}
     * @memberOf Cache
     */
    public static fromRaw(rawCache: any): Cache {
        const returnValue = new Cache();

        returnValue.name = rawCache.name;
        returnValue.nbElements = rawCache.nbElements;

        return returnValue;
    }

}

