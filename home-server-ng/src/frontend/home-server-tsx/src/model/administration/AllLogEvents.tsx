import PeristentNotification from "./PeristentNotification";

export default class AllLogEvents {
    constructor(public repoId: string, public persistantNotifications: PeristentNotification[]) { }
}