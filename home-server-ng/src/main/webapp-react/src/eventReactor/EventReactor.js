import { SELECT_ITEM, SEARCH_EVENT, FORCE_CLEAR_SEARCH_EVENT, MODAL_POPUP_CLOSE, MODAL_POPUP_OK, ALARM_STATUS_CHANGED, DATAGRID_DELETE_ALL } from './EventIds';

const eventReactorSubscription = {};

export default function eventReactor() {
    const subscribe = (eventId, eventListener) => {
        const existingEventListeners = eventReactorSubscription[eventId] !== undefined ? eventReactorSubscription[eventId] : [];
        eventReactorSubscription[eventId] = [...existingEventListeners, eventListener];
        return () => eventReactorSubscription[eventId] = [...existingEventListeners];
    }

    const emit = (eventId, data) => {
        if (eventReactorSubscription[eventId] !== undefined) {
            eventReactorSubscription[eventId].forEach(oneEventHandler => oneEventHandler(data));
        }
    }

    const shortcuts = () => {
        const selectItem = (id, status, source) => emit(SELECT_ITEM, {
            itemId: id,
            newStatus: status,
            source: source
        });

        const onSearchEvent = (onSearchHandler) => subscribe(SEARCH_EVENT, onSearchHandler);
        const forceClearSearch = () => emit(FORCE_CLEAR_SEARCH_EVENT);

        const modalClose = (driver) => emit(MODAL_POPUP_CLOSE, driver);
        const onModalClose = (eventListener) => subscribe(MODAL_POPUP_CLOSE, eventListener);
        const modalOk = (driver) => emit(MODAL_POPUP_OK, driver);
        const onModalOk = (eventListener) => subscribe(MODAL_POPUP_OK, eventListener);

        const alarmStatusChanged = (newAlarmStatus) => emit(ALARM_STATUS_CHANGED, newAlarmStatus);
        const onAlarmStatusChanged = (eventListener) => subscribe(ALARM_STATUS_CHANGED, eventListener);

        const dataGridDeleteAll = () => emit(DATAGRID_DELETE_ALL);
        const onDataGridDeleteAll = (eventListener) => subscribe(DATAGRID_DELETE_ALL, eventListener);

        return {
            selectItem: selectItem,
            onSearchEvent: onSearchEvent,
            forceClearSearch: forceClearSearch,
            modalClose: modalClose,
            onModalClose: onModalClose,
            modalOk: modalOk,
            onModalOk: onModalOk,
            alarmStatusChanged: alarmStatusChanged,
            onAlarmStatusChanged: onAlarmStatusChanged,
            dataGridDeleteAll: dataGridDeleteAll,
            onDataGridDeleteAll: onDataGridDeleteAll
        }
    }

    return {
        subscribe: subscribe,
        emit: emit,
        shortcuts: shortcuts
    }
}

