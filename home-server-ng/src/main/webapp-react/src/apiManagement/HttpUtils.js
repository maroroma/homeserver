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


export function errorHandler(errorMessage, successMessage) {

    return (response) => {
        if (response.status > 300) {
            toaster().plopError(errorMessage);
            throw new Error(errorMessage);
        }

        if (successMessage) {
            toaster().plopSuccess(successMessage);
        }


        return response.json();
    }
}