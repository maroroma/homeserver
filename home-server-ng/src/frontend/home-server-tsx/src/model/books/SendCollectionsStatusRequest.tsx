export default class SendCollectionsStatusRequest {

    static default(): SendCollectionsStatusRequest {
        return new SendCollectionsStatusRequest([
            "rodolphe.levexier@gmail.com",
            "marie.levexier@gmail.com",
            "tom.levexier@gmail.com",
            "liam.levexier@gmail.com",
            "simon.levexier@gmail.com",
        ])
    }
    constructor(public emails: string[]) { }
}