import {FC, ReactElement} from "react";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import CssTools, {CustomClassNames} from "../bootstrap/CssTools";
import {CalendarEvent} from "../../model/administration/CalendarEvent";

import "./CalendarEventRenderer.css"


export type CalendarEventRendererProps = {
    hideOnLargeDevice?: boolean,
    hideOnSmallDevice?: boolean,
    defaultBehavior?: () => ReactElement
}

const CalendarEventRenderer: FC<CalendarEventRendererProps> = ({
    hideOnLargeDevice = false,
    hideOnSmallDevice = false,
    defaultBehavior = () => <></>
}) => {

    const { administrationSubState } = useHomeServerContext();

    return administrationSubState.activeCalendarEvents && administrationSubState.activeCalendarEvents.length > 0 ?
        <span className={CssTools.of()
            .if(hideOnLargeDevice, CustomClassNames.HideOnLargeDevice)
            .if(hideOnSmallDevice, CustomClassNames.HideOnSmallDevice)
            .if(administrationSubState.activeCalendarEvents.sort(CalendarEvent.sorter())[0].dayEvent, "vertical-shake")
            .css()
        }>{administrationSubState.activeCalendarEvents.sort(CalendarEvent.sorter())[0].utf8IconsCode}</span>
        : defaultBehavior()
}

export default CalendarEventRenderer;