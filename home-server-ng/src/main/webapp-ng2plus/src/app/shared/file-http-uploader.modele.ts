import { Subject } from "rxjs";
import { SimpleProgressEvent } from "./simple-progress-event.modele";
import { Observable } from 'rxjs/Observable';

/**
 * Permet de réaliser des upload de fichier avec events pour connaitre la progression
 * dudit upload
 */
export class FileHttpUploader {

    private progressEvent: Subject<SimpleProgressEvent>;
    private completeEvent: Subject<any>;
    private url: string;
    private files: FormData;


    constructor() {
        this.progressEvent = new Subject<SimpleProgressEvent>();
        this.completeEvent = new Subject<any>();
    }

    public progress(progressHandler: (SimpleProgressEvent) => void): FileHttpUploader {
        this.progressEvent.subscribe(progressHandler);
        return this;
    }

    public upload(files: FormData): Observable<any> {
        this.files = files;

        const xhr: XMLHttpRequest = new XMLHttpRequest();

        // gestion de la fin de la requete
        xhr.onreadystatechange = () => {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    this.completeEvent.next(xhr.response);
                    this.completeEvent.complete();
                } else {
                    this.completeEvent.error(xhr.response);
                }
            }
        };

        // avancement de l'upload
        xhr.upload.onprogress = (event) => {
            this.progressEvent.next(SimpleProgressEvent.progress(event.loaded, event.total));
        };

        xhr.open('POST', this.url, true);
        xhr.send(this.files);

        return this.completeEvent;
    }


    public static toUrl(url: string): FileHttpUploader {
        const returnValue = new FileHttpUploader();

        returnValue.url = url;

        return returnValue;
    }

}