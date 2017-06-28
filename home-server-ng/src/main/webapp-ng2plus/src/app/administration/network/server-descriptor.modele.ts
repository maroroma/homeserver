import { JsonTools } from './../../shared/json-tools.service';
import { UrlDescriptor } from './url-descriptor.modele';


export class ServerDescriptor {

    public nom: string;
    public ip: string;
    public description: string;
    public urls: Array<UrlDescriptor>;

    public static fromRaw(rawJson: any): ServerDescriptor {
        const returnValue = new ServerDescriptor();

        returnValue.nom = rawJson.nom;
        returnValue.ip = rawJson.ip;
        returnValue.description = rawJson.description;
        returnValue.urls = JsonTools.map(rawJson.urls, UrlDescriptor.fromRaw);


        return returnValue;
    }

}
