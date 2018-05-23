export class NewTorrent {
    public magnetLinks = new Array<string>();

    public clear(): void {
        this.magnetLinks.length = 0;
    }
}