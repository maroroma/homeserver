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

    const onEscape = (keyHandler) => {
        if (keyEvent.keyCode === 27) {
            keyHandler(keyEvent);
        }
    }

    const debug = () => {
        console.log("KEYEVENT", keyEvent);
    }

    const onLeft = (keyHandler) => {
        if (keyEvent.keyCode === 37) {
            keyHandler(keyEvent);
        }
    }
    const onRight = (keyHandler) => {
        if (keyEvent.keyCode === 39) {
            keyHandler(keyEvent);
        }
    }

    const onBottom = (keyHandler) => {
        if (keyEvent.keyCode === 40) {
            keyHandler(keyEvent);
        }
    }

    const onUp = (keyHandler) => {
        if (keyEvent.keyCode === 38) {
            keyHandler(keyEvent);
        }
    }

    const register = (keyEventHandler) => {
        document.addEventListener("keydown", keyEventHandler);
    }
    const unregister = (keyEventHandler) => {
        document.removeEventListener('keydown', keyEventHandler);
    }



    return {
        onEnter: onEnter,
        onCtrlEnter: onCtrlEnter,
        onEscape: onEscape,
        debug: debug,
        onLeft: onLeft,
        onRight: onRight,
        onBottom: onBottom,
        onUp: onUp,
        register: register,
        unregister: unregister
    }
}