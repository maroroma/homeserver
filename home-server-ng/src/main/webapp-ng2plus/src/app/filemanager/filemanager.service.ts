import { JsonTools } from './../shared/json-tools.service';
import { ApiConstants } from './../shared/api-constants.modele';
import { NotifyerService } from './../common-gui/notifyer/notifyer.service';
import { DirectoryDescriptor } from './../shared/directory-descriptor.modele';
import { Observable } from 'rxjs/Rx';
import { Http } from '@angular/http';
import { Injectable } from '@angular/core';

@Injectable()
export class FileManagerService {

    constructor(private http: Http, private notifyer: NotifyerService) { }

    public getRootDirectories(): Observable<Array<DirectoryDescriptor>> {
        return this.http.get(ApiConstants.FILEMANAGER_ROOT_DIRECTORIES_API)
            .map(res => {
                return JsonTools.map(res.json(), DirectoryDescriptor.dfFromRaw);
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des répertoires racines');
                return Observable.of(new Array<DirectoryDescriptor>());
            });
    }
}