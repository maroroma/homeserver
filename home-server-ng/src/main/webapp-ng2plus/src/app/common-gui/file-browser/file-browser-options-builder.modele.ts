import { FileBrowserResolver } from './file-browser-resolver.modele';
import { FileBrowserOptions } from './file-browser-options.modele';
export class FileBrowserOptionsBuilder {

    private target = new FileBrowserOptions();

    public static prepare(): FileBrowserOptionsBuilder {
        const returnValue = new FileBrowserOptionsBuilder();
        return returnValue;
    }

    private constructor() {

    }

    public directoryCreationOnStarter(): FileBrowserOptionsBuilder {
        this.target.allowDirectoryCreationOnStarter = true;
        return this;
    }
    public directoryCreation(): FileBrowserOptionsBuilder {
        this.target.allowDirectoryCreation = true;
        return this.toolbar();
    }
    public deletion(): FileBrowserOptionsBuilder {
        this.target.allowDeletion = true;
        return this;
    }
    public selection(): FileBrowserOptionsBuilder {
        this.target.allowItemSelection = true;
        return this;
    }
    public refresh(): FileBrowserOptionsBuilder {
        this.target.allowRefresh = true;
        return this;
    }
    public toolbar(): FileBrowserOptionsBuilder {
        this.target.displayToolBar = true;
        return this;
    }

    public renaming(): FileBrowserOptionsBuilder {
        this.target.allowRenaming = true;
        return this;
    }

    public downloading(): FileBrowserOptionsBuilder {
        this.target.allowDownload = true;
        return this;
    }

    public withResolver(resolver: FileBrowserResolver): FileBrowserOptionsBuilder {
        this.target.resolver = resolver;
        return this;
    }

    public build(): FileBrowserOptions {
        return this.target;
    }





}

