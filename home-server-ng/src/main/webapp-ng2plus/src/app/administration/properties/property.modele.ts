import { PrettyCheckboxComponent } from './../../common-gui/pretty-checkbox/pretty-checkbox.component';
export class Property {
    public id: string;
    public description: string;
    public value: string;
    public displayName: string;
    public readOnly: boolean;
    public moduleId: string;

    public static fromRaw(rawProperty: any): Property {
        const returnValue = new Property();
        returnValue.id = rawProperty.id;
        returnValue.moduleId = rawProperty.moduleId;
        returnValue.description = rawProperty.description;
        returnValue.value = rawProperty.value;
        returnValue.displayName = rawProperty.id.replace('homeserver.' + returnValue.moduleId + '.', '');
        returnValue.readOnly = rawProperty.readOnly;
        return returnValue;
    }
}

