export class UrlDescriptor {
    public url: string;
    public name: string;

    public static fromRaw(rawJson: any): UrlDescriptor {
        const returnValue = new UrlDescriptor();
        returnValue.url = rawJson.url;
        returnValue.name = rawJson.name;

        return returnValue;
    }
}