export default function keys(key) {
    const onEnter = (keyHandler) => {
        if (key.keyCode === 13) {
            keyHandler();
        }
    }

    return {
        onEnter: onEnter
    }
}