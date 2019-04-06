import { VisualItem } from './../../shared/visual-item.modele';
export class ImportedFiles {
    files: Array<File>;
    associatedItem: any;

    constructor(rawFiles: FileList|Array<File>, associatedItem?: any) {
        this.associatedItem = associatedItem;
        this.files = new Array<File>();
        this.addFileList(rawFiles);
    }

    public addFileList(rawFiles: FileList|Array<File>) {
        for (let index = 0; index < rawFiles.length; index++) {
            this.files.push(rawFiles[index]);
        }
    }

    mapUniqueFileToFormData(fileName: string): FormData {
        return this.mapToFormData(file => fileName);
    }

    mapToFormData(mapper?: (File, number?) => string): FormData {
        const finalMapper = mapper ? mapper : (file, index) => 'file' + index;
        return this.files.reduce((formData, fileToAdd, index) => {
            formData.append(finalMapper(fileToAdd, index), fileToAdd);
            return formData;
        }, new FormData());
    }

    mapAllFileToFormData(mapper?: (File, number?) => string): Array<FormData> {
        const finalMapper = mapper ? mapper : (file, index) => 'file' + index;
        return this.files.map((oneFile, index) => {
            const fd = new FormData();
            fd.append(finalMapper(oneFile, index), oneFile);
            return fd;
        });
    }

}
