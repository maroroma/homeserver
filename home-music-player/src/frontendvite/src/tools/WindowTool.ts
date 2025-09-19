export default class WindowTool {
    static scrollToTop(): void {
        window.scrollTo({ top: 0, left: 0, behavior: 'instant' });
    }
}