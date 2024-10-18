export default class NewTorrents {

    static empty(): NewTorrents {
        return new NewTorrents([]);
    }

    constructor(
        public magnetLinks: string[]
    ) { }
}