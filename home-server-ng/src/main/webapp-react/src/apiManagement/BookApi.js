import {apiRoot, defaultJsonHeaders, errorHandler} from './HttpUtils';

export default function bookApi() {
    const findBooksByIsbn = (isbnCode) =>
        fetch(`${apiRoot()}/books/search/isbn/${isbnCode}`)
            .then(errorHandler("Erreur lors de la recherche par code isbn"))
            .catch(er => console.error(er));

    const findBooksByGenericQuery = (genericQuery) =>
        fetch(`${apiRoot()}/books/search/query/${genericQuery}`)
            .then(errorHandler("Erreur lors de la recherche par requête générique"))
            .catch(er => console.error(er));

    const findSeriesByBook = (bookCandidate) => {

        return fetch(`${apiRoot()}/books/series/book`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(bookCandidate)
        })
            .then(errorHandler(`Erreur rencontrée lors de la recherche des séries associées au livre sélectionné`))
            .catch(er => console.error(er));
    };

    const findBooksBySerieResource = (request) => {
        return fetch(`${apiRoot()}/books/import/serieUrl`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(request)
        })
            .then(errorHandler(`Erreur rencontrée lors de l'import de livres pour une série donnée`))
            .catch(er => console.error(er));
    }

    const findBooksByIsbnPicture = (base64Image) => {

        return fetch(`${apiRoot()}/books/search/isbn`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify({ photoAsBase64: base64Image })
        })
            .then(errorHandler(`Erreur rencontrée lors de la recherche d'un livre via photo d'un isbn`))
            .catch(er => console.error(er));
    };

    const addBooksToLibrary = (bookAddRequest) => {
        return fetch(`${apiRoot()}/books`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(bookAddRequest)
        })
            .then(errorHandler(`Erreur rencontrée lors de l'ajout des livres dans la librairie`, "Ajout du ou des livres terminé"))
            .catch(er => console.error(er));
    };

    const importBooksIntoSerie = (bookAddRequest) => {
        return fetch(`${apiRoot()}/books/import/bookProposals`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(bookAddRequest)
        })
            .then(errorHandler(`Erreur rencontrée lors de l'import des livres dans la librairie`, "Import du ou des livres terminé"))
            .catch(er => console.error(er));
    };

    const sendMailForMissingBooks = (emails) => {
        return fetch(`${apiRoot()}/books/mail/missing`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify({ emails: emails })
        })
            .then(errorHandler(`Erreur rencontrée lors l'émission du mail`, "Mail envoyé"))
            .catch(er => console.error(er));
    };

    const saveBook = (bookToSave) => {
        return fetch(`${apiRoot()}/books`, {
            method: 'PUT',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(bookToSave)
        })
            .then(errorHandler(`Erreur rencontrée lors de la sauvegarde du livre dans la librairie`, "Sauvegarde du livre terminée"))
            .catch(er => console.error(er));
    }

    const saveSerie = (serieToSave) => {
        return fetch(`${apiRoot()}/books/series`, {
            method: 'PUT',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(serieToSave)
        })
            .then(errorHandler(`Erreur rencontrée lors de la sauvegarde du nouveau status de la serie`, "Sauvegarde de la série terminée"))
            .catch(er => console.error(er));
    }

    const deleteBook = (bookToDelete) => {
        return fetch(`${apiRoot()}/books/${bookToDelete.id}`, {
            method: 'DELETE',
            headers: defaultJsonHeaders()
        })
            .then(errorHandler(`Erreur rencontrée lors de la suppression du livre dans la librairie`, "Suppression du livre terminée"))
            .catch(er => console.error(er));
    }

    const deleteSerie = (serieToDelete) => {
        return fetch(`${apiRoot()}/books/series/${serieToDelete.id}`, {
            method: 'DELETE',
            headers: defaultJsonHeaders()
        })
            .then(errorHandler(`Erreur rencontrée lors de la suppression d'une série`, "Suppression de la série terminée"))
            .catch(er => console.error(er));
    }

    const addNewSerie = (serieTitle) => {

        const newSerie = {
            title: serieTitle
        }

        return fetch(`${apiRoot()}/books/series`, {
            method: 'POST',
            headers: defaultJsonHeaders(),
            body: JSON.stringify(newSerie)
        })
            .then(errorHandler(`Erreur rencontrée lors de la création de la nouvelle série ${serieTitle}`,
                `Série ${serieTitle} créée avec succès`));

    }

    const getAllSeries = () => fetch(`${apiRoot()}/books/series`)
        .then(errorHandler("Erreur lors de la récupération des séries"))
        .catch(er => console.error(er));

    const getOneSerie = (id) => fetch(`${apiRoot()}/books/series/${id}`)
        .then(errorHandler("Erreur lors de la récupération d'une serie"))
        .catch(er => console.error(er));

    const getAllBooks = () => fetch(`${apiRoot()}/books`)
        .then(errorHandler("Erreur lors de la récupération des livres"))
        .catch(er => console.error(er));

    const getAllBooksFromSerie = (serieId) => fetch(`${apiRoot()}/books/series/${serieId}/books`)
        .then(errorHandler("Erreur lors de la récupération des livres de la série"))
        .catch(er => console.error(er));

    const getAllBooksGroupedBySeries = () => fetch(`${apiRoot()}/books/booksGroupedBySeries`)
        .then(errorHandler("Erreur lors de la récupération des livres groupés par série"))
        .catch(er => console.error(er));

    const downloadBaseUrl = () => {
        return `${apiRoot()}/books`;
    }

    const resolveSeriePicture = (serie) => {
        if (serie) {
            return downloadBaseUrl() + "/series/" + serie.id + "/picture"
        }
    }



    return {
        findBooksByIsbn: findBooksByIsbn,
        findBooksByGenericQuery: findBooksByGenericQuery,
        findSeriesByBook: findSeriesByBook,
        getAllSeries: getAllSeries,
        addNewSerie: addNewSerie,
        getAllBooks: getAllBooks,
        addBooksToLibrary: addBooksToLibrary,
        downloadBaseUrl: downloadBaseUrl,
        resolveSeriePicture: resolveSeriePicture,
        saveBook: saveBook,
        getAllBooksGroupedBySeries: getAllBooksGroupedBySeries,
        deleteBook: deleteBook,
        findBooksByIsbnPicture: findBooksByIsbnPicture,
        saveSerie: saveSerie,
        findBooksBySerieResource: findBooksBySerieResource,
        importBooksIntoSerie: importBooksIntoSerie,
        sendMailForMissingBooks: sendMailForMissingBooks,
        getOneSerie: getOneSerie,
        getAllBooksFromSerie: getAllBooksFromSerie,
        deleteSerie: deleteSerie
    }
}