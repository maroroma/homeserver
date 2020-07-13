import eventReactor from "../../eventReactor/EventReactor";

export default function iotSubEventReactor() {

    const SEND_ACTION_REQUEST = "SEND_ACTION_REQUEST";
    const EDIT_SPRITE = "EDIT_SPRITE";
    const SAVE_SPRITE = "SAVE_SPRITE";
    const CREATE_SPRITE = "CREATE_SPRITE";

    const sendActionRequest = (iotComponent) => eventReactor().emit(SEND_ACTION_REQUEST, iotComponent);
    const onSendActionRequest = (eventHandler) => eventReactor().subscribe(SEND_ACTION_REQUEST, eventHandler);


    const editSprite = (miniSprite) => eventReactor().emit(EDIT_SPRITE, miniSprite);
    const onEditSprite = (eventHandler) => eventReactor().subscribe(EDIT_SPRITE, eventHandler);

    const saveSprite = (spriteToSave) => eventReactor().emit(SAVE_SPRITE, spriteToSave);
    const onSaveSprite = (eventHandler) => eventReactor().subscribe(SAVE_SPRITE, eventHandler);
    const createSprite = (spriteToSave) => eventReactor().emit(CREATE_SPRITE, spriteToSave);
    const onCreateSprite = (eventHandler) => eventReactor().subscribe(CREATE_SPRITE, eventHandler);

    return {
        sendActionRequest: sendActionRequest,
        onSendActionRequest: onSendActionRequest,
        editSprite: editSprite,
        onEditSprite: onEditSprite,
        saveSprite: saveSprite,
        onSaveSprite: onSaveSprite,
        createSprite: createSprite,
        onCreateSprite: onCreateSprite
    }
}