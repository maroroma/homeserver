export default class WindowTool {
    static scrollToTop(): void {
        // @ts-ignore: Type '"instant"' is not assignable to type 'ScrollBehavior | undefined'.
        window.scrollTo({ top: 0, left: 0, behavior: 'instant' });
    }

    static onScroll(eventHandler: (pageYOffSet: number) => void): () => void {
        const adapter = () => eventHandler(window.pageYOffset);
        window.addEventListener("scroll", adapter);
        return () => window.removeEventListener("scroll", adapter);
    }
}