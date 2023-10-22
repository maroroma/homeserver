export default function keys(keyEvent) {
    const onEnter = (keyHandler) => {
        if (keyEvent.keyCode === 13) {
            keyHandler(keyEvent);
        }
    }

    const onCtrlEnter = (keyHandler) => {
        if (keyEvent.keyCode === 13 && keyEvent.ctrlKey) {
            keyHandler(keyEvent);
        }
    }

    return {
        onEnter: onEnter,
        onCtrlEnter: onCtrlEnter
    }
}