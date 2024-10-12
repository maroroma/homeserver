export default class BuzzRequest {

    static autoFirst(ledTemplate: string): BuzzRequest {
        return new BuzzRequest("", ledTemplate);
    }

    constructor(public id: string, public ledTemplate: string) { }
}