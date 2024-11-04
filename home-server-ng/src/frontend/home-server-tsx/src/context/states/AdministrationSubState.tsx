import AllLogEvents from "../../model/administration/AllLogEvents"
import {CalendarEvent} from "../../model/administration/CalendarEvent"
import {HomeServerProperty} from "../../model/administration/HomeServerProperty"
import Task from "../../model/administration/Task"

export type AdministrationSubState = {

    properties: HomeServerProperty[],
    tasks: Task[],
    allLogEvents: AllLogEvents,
    activeCalendarEvents: CalendarEvent[] | undefined

}