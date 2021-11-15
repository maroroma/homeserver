import { SELECT_ITEM, SEARCH_EVENT, FORCE_CLEAR_SEARCH_EVENT, MODAL_POPUP_CLOSE, MODAL_POPUP_OK, ALARM_STATUS_CHANGED, DATAGRID_DELETE_ALL, DATAGRID_REFRESH_ALL, DATAGRID_DELETE_ONE, DATAGRID_ADD_ITEM, MODAL_POPUP_OPENED, DATAGRID_SAVE_ITEMS } from './EventIds';

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

        const modalClose = (driver) => emit(MODAL_POPUP_CLOSE, driver);
        const onModalClose = (eventListener) => subscribe(MODAL_POPUP_CLOSE, eventListener);
        const onModalCloseFor = (popupId, eventListener) => subscribe(MODAL_POPUP_CLOSE, (driver) => {
            if (driver.id === popupId) {
                eventListener(driver);
            }
        });
        const modalOk = (driver) => emit(MODAL_POPUP_OK, driver);
        const onModalOk = (eventListener) => subscribe(MODAL_POPUP_OK, eventListener);
        const onModalOkFor = (popupId, eventListener) => subscribe(MODAL_POPUP_OK, (driver) => {
            if (driver.id === popupId) {
                eventListener(driver);
            }
        });
        const modalOpened = (driver) => emit(MODAL_POPUP_OPENED, driver);
        const onModalOpened = (eventListener) => subscribe(MODAL_POPUP_OPENED, eventListener);
        const onModalOpenedFor = (popupId, eventListener) => subscribe(MODAL_POPUP_OPENED, (driver) => {
            if (driver.id === popupId) { eventListener(driver); }
        });

        const alarmStatusChanged = (newAlarmStatus) => emit(ALARM_STATUS_CHANGED, newAlarmStatus);
        const onAlarmStatusChanged = (eventListener) => subscribe(ALARM_STATUS_CHANGED, eventListener);

        const dataGridDeleteAll = () => emit(DATAGRID_DELETE_ALL);
        const onDataGridDeleteAll = (eventListener) => subscribe(DATAGRID_DELETE_ALL, eventListener);

        const dataGridDeleteOne = (oneRowOfData) => emit(DATAGRID_DELETE_ONE, oneRowOfData);
        const onDataGridDeleteOne = (eventListener) => subscribe(DATAGRID_DELETE_ONE, eventListener);

        const dataGridRefreshAll = () => emit(DATAGRID_REFRESH_ALL);
        const onDataGridRefreshAll = (eventListener) => subscribe(DATAGRID_REFRESH_ALL, eventListener);

        const dataGridAddItem = () => emit(DATAGRID_ADD_ITEM);
        const onDataGridAddItem = (eventListener) => subscribe(DATAGRID_ADD_ITEM, eventListener);

        const dataGridSaveItems = (modifiedItems) => emit(DATAGRID_SAVE_ITEMS, modifiedItems);
        const onDataGridSaveItems = (eventListener) => subscribe(DATAGRID_SAVE_ITEMS, eventListener);

        return {
            selectItem: selectItem,
            modalClose: modalClose,
            onModalClose: onModalClose,
            onModalCloseFor: onModalCloseFor,
            modalOk: modalOk,
            onModalOkFor: onModalOkFor,
            onModalOk: onModalOk,
            alarmStatusChanged: alarmStatusChanged,
            onAlarmStatusChanged: onAlarmStatusChanged,
            dataGridDeleteAll: dataGridDeleteAll,
            onDataGridDeleteAll: onDataGridDeleteAll,
            dataGridRefreshAll: dataGridRefreshAll,
            onDataGridRefreshAll: onDataGridRefreshAll,
            dataGridDeleteOne: dataGridDeleteOne,
            onDataGridDeleteOne: onDataGridDeleteOne,
            dataGridAddItem: dataGridAddItem,
            onDataGridAddItem: onDataGridAddItem,
            dataGridSaveItems: dataGridSaveItems,
            onDataGridSaveItems: onDataGridSaveItems,
            modalOpened: modalOpened,
            onModalOpened: onModalOpened,
            onModalOpenedFor: onModalOpenedFor
        }
    }

    return {
        subscribe: subscribe,
        emit: emit,
        shortcuts: shortcuts
    }
}

