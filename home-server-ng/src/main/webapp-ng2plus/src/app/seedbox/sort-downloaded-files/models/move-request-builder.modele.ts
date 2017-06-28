import { MoveRequest } from './move-request.modele';
import { VisualItem } from './../../../shared/visual-item.modele';
import { DirectoryDescriptor } from './../../../shared/directory-descriptor.modele';
import { VisualItemDataSource } from './../../../shared/visual-item-datasource.modele';
import { Subject } from 'rxjs/Subject';
import { CompletedFile } from './completed-file.modele';
export class MoveRequestBuilder {


    public completedFiles: VisualItemDataSource<CompletedFile>;

    public targetDirectory: DirectoryDescriptor;

    public selectedFiles: Array<VisualItem<CompletedFile>>;

    constructor() {
        this.completedFiles = new VisualItemDataSource<CompletedFile>();
    }

    public backupCompletedFiles() {
        this.selectedFiles = this.completedFiles.getSelectedItem();
        // console.log(this.selectedFiles);
    }

    public buildRequest(): MoveRequest {
        const returnValue = new MoveRequest();
        returnValue.filesToMove = this.selectedFiles.map(vi => vi.item);
        returnValue.target = this.targetDirectory;
        return returnValue;
    }
}
