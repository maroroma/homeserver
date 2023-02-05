export default function popup(popupId, setStateFunction, onCloseHandler = () => {}) {
    var elems = document.querySelectorAll(`#${popupId}`);
    var instance = window.M.Modal.init(elems, { onCloseEnd: onCloseHandler })[0];
    setStateFunction(instance);
    return instance;
}