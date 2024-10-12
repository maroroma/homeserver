import AllLogEvents from "../model/administration/AllLogEvents";
import {HomeServerProperty} from "../model/administration/HomeServerProperty";
import ServerStatus from "../model/administration/ServerStatus";
import Task from "../model/administration/Task";
import TaskCancelRequest from "../model/administration/TaskCancelRequest";
import {RequesterUtils} from "./RequesterUtils";


export class AdministrationRequester {

    static getAllProperties(): Promise<HomeServerProperty[]> {
        return fetch(`../api/administration/configs`)
            .then(reponse => RequesterUtils.handleErrors(reponse))
            .then(response => response)
    }

    static getRunningTasks(): Promise<Task[]> {
        return RequesterUtils.get(`../api/administration/tasks`);
    }

    static getAllEvents(): Promise<AllLogEvents> {
        return RequesterUtils.get(`../api/administration/logEvents`);
    }

    static deleteAllEvents(logEvents:AllLogEvents):Promise<boolean> {
        return RequesterUtils.delete(`../api/administration/repo/${logEvents.repoId}`)
    }

    static deleteTask(aRunningTask: Task): Promise<boolean> {
        return RequesterUtils.post(`../api/administration/tasks`, new TaskCancelRequest(aRunningTask));
    }

    static saveOneProperty(propertyToUpdate: HomeServerProperty): Promise<HomeServerProperty> {
        return RequesterUtils.update(`../api/administration/config/${propertyToUpdate.id}`, propertyToUpdate);
    }

    static getServerStatus(): Promise<ServerStatus> {
        return RequesterUtils.get("../api/administration/server/status");
    }
}