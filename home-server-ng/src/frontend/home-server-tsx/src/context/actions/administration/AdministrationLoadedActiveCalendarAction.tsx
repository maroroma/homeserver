import {CalendarEvent} from "../../../model/administration/CalendarEvent";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class AdministrationLoadedActiveCalendarAction implements HomeServerAction {


    constructor(private activeCalendarEvents: CalendarEvent[]) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return {
            ...previousState,
            administrationSubState: {
                ...previousState.administrationSubState,
                activeCalendarEvents: this.activeCalendarEvents
            }
        }
    }

}