import { SimpleDatagridHeaderBuilder } from './simple-datagrid-header-builder.modele';
import { SimpleDatagridFieldDescriptor } from './simple-datagrid-field-descriptor.modele';
export class SimpleDatagridFieldDescriptorBuilder {

    private target: SimpleDatagridFieldDescriptor;
    private parent: SimpleDatagridHeaderBuilder;


    public static forField(fieldName: string, parent: SimpleDatagridHeaderBuilder): SimpleDatagridFieldDescriptorBuilder {
        return new SimpleDatagridFieldDescriptorBuilder(parent, fieldName);
    }

    public static forAction(parent: SimpleDatagridHeaderBuilder): SimpleDatagridFieldDescriptorBuilder {
        const returnValue = new SimpleDatagridFieldDescriptorBuilder(parent);
        returnValue.target.isActionField = true;
        returnValue.target.isTextField = false;
        return returnValue;
    }

    public forAction(): SimpleDatagridFieldDescriptorBuilder {
        return SimpleDatagridFieldDescriptorBuilder.forAction(this.parent);
    }

    public forField(fieldName: string): SimpleDatagridFieldDescriptorBuilder {
        return SimpleDatagridFieldDescriptorBuilder.forField(fieldName, this.parent);
    }

    private constructor(parent: SimpleDatagridHeaderBuilder, fieldName?: string) {
        this.parent = parent;
        this.parent.fieldsBuilder.push(this);
        this.target = new SimpleDatagridFieldDescriptor(fieldName);
    }

    public display(displayName: string): SimpleDatagridFieldDescriptorBuilder {
        this.target.displayName = displayName;
        this.target.hasDisplayName = true;
        return this;
    }

    public link(linkLabelField?: string): SimpleDatagridFieldDescriptorBuilder {
        this.target.isLink = true;
        this.target.linkLabelField = linkLabelField;
        this.target.isGlyphiconField = false;
        this.target.isTextField = false;
        this.target.isOnOffField = false;
        return this;
    }

    public glyphicon(): SimpleDatagridFieldDescriptorBuilder {
        this.target.isGlyphiconField = true;
        this.target.isTextField = false;
        return this;
    }

    public onOff(): SimpleDatagridFieldDescriptorBuilder {
        this.target.isOnOffField = true;
        this.target.isTextField = false;
        return this;
    }

    public textBox(): SimpleDatagridFieldDescriptorBuilder {
        this.target.isTextBox = true;
        this.target.isTextField = false;
        return this;
    }

    public saveButton(): SimpleDatagridFieldDescriptorBuilder {
        this.target.hasSaveButton = true;
        return this;
    }

    public deleteButton(): SimpleDatagridFieldDescriptorBuilder {
        this.target.hasDeleteButton = true;
        return this;
    }

    public exportButton(): SimpleDatagridFieldDescriptorBuilder {
        this.target.hasExportButton = true;
        return this;
    }
    public importButton(): SimpleDatagridFieldDescriptorBuilder {
        this.target.hasImportButton = true;
        return this;
    }

    public sortable(): SimpleDatagridFieldDescriptorBuilder {
        this.target.isSortable = true;
        return this;
    }

    public hideForSD(): SimpleDatagridFieldDescriptorBuilder {
        this.target.hideForSmallDevice = true;
        return this;
    }

    public hideForHD(): SimpleDatagridFieldDescriptorBuilder {
        this.target.hideForBigDevice = true;
        return this;
    }

    public zoomable(): SimpleDatagridFieldDescriptorBuilder {
        this.target.zoomable = true;
        return this;
    }

    public build(): SimpleDatagridFieldDescriptor {
        return this.target;
    }

    public buildAll(): Array<SimpleDatagridFieldDescriptor> {
        return this.parent.build();
    }




}
