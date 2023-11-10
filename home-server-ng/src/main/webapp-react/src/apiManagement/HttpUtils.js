import toaster from "../components/commons/Toaster";


export function apiRoot() {
    return '/api';
}

export function defaultJsonHeaders() {
    return {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    };
}


export function errorHandler(errorMessage, successMessage, debug = () => {}) {

    return (response) => {
        if (response.status > 300) {
            toaster().plopError(errorMessage);
            throw new Error(errorMessage);
        }

        if (successMessage) {
            toaster().plopSuccess(successMessage);
        }

        debug(response);

        return response.json();
    }
}

export function errorHandlerWithoutJson(errorMessage, successMessage) {
    return (response) => {
        console.log("delete", response);

        const errors = response.filter(aResponse => aResponse.status > 300);
        const successes = response.filter(aResponse => aResponse.status < 300);

        if (successes.length > 0 ) {
            if (successMessage) {
                toaster().plopSuccess(successMessage);
            }
        }

        if (errors.length > 0) {
            toaster().plopError(errorMessage);
            throw new Error(errorMessage);
        }


        // if (response.status > 300) {
        //     toaster().plopError(errorMessage);
        //     throw new Error(errorMessage);
        // }

        // if (successMessage) {
        //     toaster().plopSuccess(successMessage);
        // }


        return response;
    }
}