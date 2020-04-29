export default function toaster() {
    const plop = (content, classes) => window.M.toast({ html: content, classes: classes })

    const plopError = errorContent => plop(errorContent, "red")
    const plopSuccess = successContent => plop(successContent, "green")

    return {
        plopError: plopError,
        plopSuccess: plopSuccess
    }
}