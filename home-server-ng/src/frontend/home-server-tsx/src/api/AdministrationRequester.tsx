import AllLogEvents from "../model/administration/AllLogEvents";
import {CalendarEvent} from "../model/administration/CalendarEvent";
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

    static deleteAllEvents(logEvents: AllLogEvents): Promise<boolean> {
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

    static getCalendarEvents(): Promise<CalendarEvent[]> {
        return RequesterUtils.get("/api/administration/calendarEvents")
    }
    static getActiveCalendarEvents(): Promise<CalendarEvent[]> {
        return RequesterUtils.get("/api/administration/calendarEvents/active")
    }

    static addCalendarEvent(newEvent: CalendarEvent): Promise<CalendarEvent[]> {
        return RequesterUtils.post("/api/administration/calendarEvents", newEvent);
    }

    static updateCalendarEvent(updateEvent: CalendarEvent): Promise<CalendarEvent[]> {
        return RequesterUtils.update("/api/administration/calendarEvents", updateEvent);
    }

    static deleteCalendarEvent(eventToDelete: CalendarEvent): Promise<CalendarEvent[]> {
        return RequesterUtils.delete(`/api/administration/calendarEvents/${eventToDelete.id}`);
    }
}