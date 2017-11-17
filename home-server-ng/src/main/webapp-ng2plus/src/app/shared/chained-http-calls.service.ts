import { Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { Tuple } from 'app/shared/tuple.modele';

/**
 * Classe utilitaire pour la réalisation d'appels http chainés, sans concurrence.
 * C'est une piste pour soulager le serveur sur du rasp, qui sature lors de l'émission de plusieurs requêtes en //
 * @export
 * @class ChainedHttpCalls
 * @template T
 * @template U
 */
export class ChainedHttpCalls<T, U> {

    public oneItemCompleted: Subject<Tuple<T, number>>;
    public allItemCompleted: Subject<Array<T>>;
    public sourceList: Array<U>;

    constructor(source: Array<U>) {
        this.sourceList = source;
        this.oneItemCompleted = new Subject<Tuple<T, number>>();
        this.allItemCompleted = new Subject<Array<T>>();
    }

    public submit(transformToHttpPromise: (U) => Observable<Response>, transformToOutput: (Response) => T): Subject<Array<T>> {
        const responsesPromises = this.sourceList.map(transformToHttpPromise);

        this.innerCall(responsesPromises, 0, new Array<T>(), transformToOutput);

        return this.allItemCompleted;
    }

    private innerCall(responsesPromises: Array<Observable<Response>>,
        currentIndex: number,
        returnValues: Array<T>,
        transformToOutput: (Response) => T) {

        // TODO : gérer les erreurs, parce que là c'est dégueu
        responsesPromises[currentIndex].subscribe(res => {
            returnValues.push(transformToOutput(res));
            this.oneItemCompleted.next(new Tuple<T, number>(returnValues[returnValues.length - 1], currentIndex));
            const nextindex = currentIndex++;
            // tous les appels sont complétés
            if (nextindex >= responsesPromises.length - 1) {
                console.log('fin des traitements chainés');
                this.allItemCompleted.next(returnValues);
            } else {
                // appel recursif
                this.innerCall(responsesPromises, currentIndex++, returnValues, transformToOutput);
            }
        });
    }

}

