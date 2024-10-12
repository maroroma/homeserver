export class HomeServerProperty {


    static filter(searchString:string) : (prop:HomeServerProperty) => boolean {
        return aProperty => aProperty.id.toLocaleLowerCase().includes(searchString.toLocaleLowerCase());
    }


    constructor(public id: string, public value: string, public description: string) { }

    public static empty(): HomeServerProperty {
        return new HomeServerProperty("", "", "");
    }
}