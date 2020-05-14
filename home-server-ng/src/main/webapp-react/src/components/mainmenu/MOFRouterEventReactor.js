import eventReactor from "../../eventReactor/EventReactor";


const SELECTED_MODULE_CHANGE = 'SELECTED_MODULE_CHANGE';


export default function mofRouterEventReactor() {

    const selectedModuleChange = newSelectedModule => eventReactor().emit(SELECTED_MODULE_CHANGE, newSelectedModule);
    const onSelectedModuleChange = eventHandler => eventReactor().subscribe(SELECTED_MODULE_CHANGE, eventHandler);

    return {
        selectedModuleChange: selectedModuleChange,
        onSelectedModuleChange: onSelectedModuleChange
    }




}