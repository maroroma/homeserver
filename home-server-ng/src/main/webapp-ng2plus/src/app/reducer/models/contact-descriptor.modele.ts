export class ContactDescriptor {
    public id: string;
    public email: string;

    public static fromRawJson(rawContact: any): ContactDescriptor {
        const returnValue = new ContactDescriptor();

        returnValue.id = rawContact.id;
        returnValue.email = rawContact.email;

        return returnValue;
    }
}