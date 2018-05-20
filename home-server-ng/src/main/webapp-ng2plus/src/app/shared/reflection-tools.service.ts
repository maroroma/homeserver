export class ReflectionTools {

    public static isString(objectToTest:any) : boolean {
        return typeof objectToTest === 'string';
    }

    /**
     * Permet à partir d'un path objet de récupérer la propriété sur l'objet data.
     * @param data Objet à parser
     * @param fullFieldName chemin complet de la propriété
     */
    public static resolveData(data: any, fullFieldName: string): any {
        if (fullFieldName) {
            const propsHierachy = fullFieldName.split('.');
            let returnValue = data;
            propsHierachy.forEach(fieldName => {
                returnValue = returnValue[fieldName];
                // returnValue = returnValue;
            });
            return returnValue;
        } else {
            return data;
        }
    }

    /**
     * Permet d'écrire une valeur sur l'objet item pointé par le path en entrée
     * @param item grappe d'objet dans laquelle on veut écrire
     * @param fullFieldName chemin complet de la valeur à écrire
     * @param dataToWrite données à écrire
     */
    public static writeData(item: any, fullFieldName: string, dataToWrite: any): void {
        if (fullFieldName && item) {
            const propsHierachy = fullFieldName.split('.');
            const lastPropertyName = propsHierachy.pop();
            let fieldToWrite = item;
            propsHierachy.forEach(fieldName => {
                fieldToWrite = fieldToWrite[fieldName];
            });

            fieldToWrite[lastPropertyName] = dataToWrite;
        }
    }
}
