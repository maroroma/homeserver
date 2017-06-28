import { SimpleDatagridFieldDescriptor } from './simple-datagrid-field-descriptor.modele';
import { SimpleDatagridFieldDescriptorBuilder } from './simple-datagrid-field-descriptor-builder.modele';
export class SimpleDatagridHeaderBuilder {

    public fieldsBuilder = new Array<SimpleDatagridFieldDescriptorBuilder>();


    public static forField(fieldName: string): SimpleDatagridFieldDescriptorBuilder {
        return SimpleDatagridFieldDescriptorBuilder.forField(fieldName, new SimpleDatagridHeaderBuilder());
    }

    public build(): Array<SimpleDatagridFieldDescriptor> {
        const returnValue = new Array<SimpleDatagridFieldDescriptor>();

        this.fieldsBuilder.forEach(builder => returnValue.push(builder.build()));

        return returnValue;
    }
}
