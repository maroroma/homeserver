import { ApiConstants } from './../../shared/api-constants.modele';
export class Repository {
    public id: string;
    public shortId: string;
    public filename: string;
    public exists: boolean;
    public downloadUrl: string;

    public static fromRaw(rawRepo: any): Repository {
        const returnValue = new Repository();

        returnValue.filename = rawRepo.file.name;
        returnValue.id = rawRepo.propertyKey;
        returnValue.shortId = returnValue.id.replace('homeserver.', '');
        returnValue.exists = rawRepo.exists;


        returnValue.downloadUrl = ApiConstants.ADMIN_REPO_API + '/' + returnValue.id + '/export';

        return returnValue;
    }
}

