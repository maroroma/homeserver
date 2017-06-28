import { DirectoryDescriptor } from './../../shared/directory-descriptor.modele';
export class DirectoryCreationRequest {
    public parentDirectory: DirectoryDescriptor;
    public directoryName: string;

    public reinit(): void {
        this.parentDirectory = null;
        this.directoryName = null;
    }
}
