export class ModuleStatus {
    public id: string;
    public enabled: boolean;
    constructor(id: string, enabled: boolean) {
        this.id = id;
        this.enabled = enabled;
    }
}