export class ReflectionTools {
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
