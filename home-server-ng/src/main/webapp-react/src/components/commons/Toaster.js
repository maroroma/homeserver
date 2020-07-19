export default function toaster() {
    const plop = (content, classes, displayTime = 3000) => window.M.toast({ html: content, classes: classes, displayLength: displayTime })

    const plopError = errorContent => plop(errorContent, "red");
    const plopSuccess = successContent => plop(successContent, "green");
    const plopAndWait = (awaited, content) => {
        plop(content, "blue", undefined);
        return awaited().then(response => {
            window.M.Toast.dismissAll();
            return response;
        });
    };

    return {
        plopError: plopError,
        plopSuccess: plopSuccess,
        plopAndWait: plopAndWait
    }
}