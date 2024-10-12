export default class RootDirectoryConfiguration {

    static mapFromResponse(aRootDirectoryRaw: { id: string; protected: boolean; hidden: boolean; rawPath: string }): RootDirectoryConfiguration {
        return new RootDirectoryConfiguration(aRootDirectoryRaw.id, aRootDirectoryRaw.rawPath, aRootDirectoryRaw.protected, aRootDirectoryRaw.hidden);
    }

    static sorter(): (rd1: RootDirectoryConfiguration, rd2: RootDirectoryConfiguration) => number {
        return (rd1, rd2) => rd1.rawPath.toLocaleLowerCase().localeCompare(rd2.rawPath.toLocaleLowerCase());
    }


    constructor(
        public id: string, public rawPath: string, public isProtected: boolean, public hidden: boolean
    ) { }
}