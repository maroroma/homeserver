
export enum HttpMethods {
    DELETE = "DELETE",
    PATCH = "PATCH",
    POST = "POST",
    PUT = "PUT",
    GET = "GET",
}


export class RequesterUtils {
    public static defaultJsonHeaders() {
        return {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        };
    }

    public static handleErrors(response: Response): any {
        if (response.status >= 300) {
            throw new Error("plop");
        }
        return response.json();
    }

    public static delete(uri: string): Promise<any> {
        return fetch(uri,
            {
                method: HttpMethods.DELETE,
                headers: RequesterUtils.defaultJsonHeaders()
            })
            .then(response => RequesterUtils.handleErrors(response));
    }


    private static options(method: HttpMethods, body: any = undefined): any {

        if (!body) {
            return {
                method: method,
                headers: RequesterUtils.defaultJsonHeaders()
            }
        } else {
            return {
                method: method,
                headers: RequesterUtils.defaultJsonHeaders(),
                body: JSON.stringify(body)
            }
        }

    }


    public static update<OUTPUT>(uri: string, body: any = undefined): Promise<OUTPUT> {
        return fetch(uri, this.options(HttpMethods.PATCH, body))
            .then(response => RequesterUtils.handleErrors(response))
    }

    public static post<OUTPUT>(uri: string, body: any): Promise<OUTPUT> {
        return fetch(uri, this.options(HttpMethods.POST, body))
            .then(response => RequesterUtils.handleErrors(response))

    }

    public static put<OUTPUT>(uri: string, body: any): Promise<OUTPUT> {
        return fetch(uri, this.options(HttpMethods.PUT, body))
            .then(response => RequesterUtils.handleErrors(response))
    }

    public static get<OUTPUT>(uri: string): Promise<OUTPUT> {
        return fetch(uri, this.options(HttpMethods.GET))
            .then(response => RequesterUtils.handleErrors(response))

    }

}