import { FileDescriptor } from './../../../shared/file-descriptor.modele';
export class RunningTorrent {
    /**
	 * Identifiant technique du torrent.
	 */
    public id: string;

	/**
	 * Nom du torrent.
	 */
    public name: string;
	/**
	 * données téléchargées.
	 */
    public done: number;
	/**
	 * Données restantes à télécharger.
	 */
    public remaining: number;
	/**
	 * Total des données à télécharger.
	 */
    public total: number;

	/**
	 * Temps restant estimé.
	 */
    public estimatedTime: string;

	/**
	 * Pourcentage accompli.
	 */
    public percentDone: number;

    /**
     * Utilisé pour la réutilisation de composants communs.
     * @type {FileDescriptor}
     * @memberof RunningTorrent
     */
    public pseudoFileDescriptor: FileDescriptor;

	/**
	 * Détermine si le torrent est terminé.
	 */
    public completed: boolean;

    public static fromRaw(raw: any): RunningTorrent {
        const returnValue = new RunningTorrent();

        returnValue.completed = raw.completed;
        returnValue.id = raw.id;
        returnValue.done = raw.done;
        returnValue.estimatedTime = raw.estimatedTime;
        returnValue.name = raw.name;
        returnValue.percentDone = raw.percentDone;
        returnValue.remaining = raw.remaining;
        returnValue.total = raw.total;
        returnValue.pseudoFileDescriptor = new FileDescriptor();
        returnValue.pseudoFileDescriptor.name = returnValue.name;

        return returnValue;
    }
}
