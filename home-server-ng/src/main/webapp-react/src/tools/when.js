export function when(firstPredicate) {
    return new When(firstPredicate);
}

export class When {

    constructor(firstPredicate) {
        this.predicates = [(previousResult) => previousResult && firstPredicate()];
        this.and = this.and.bind(this);
        this.then = this.then.bind(this);
        this.thenHideElement = this.thenHideElement.bind(this);
        this.thenDisableElement = this.thenDisableElement.bind(this);
    }

    and(otherPredicate) {
        this.predicates.push((previousResult) =>  previousResult && otherPredicate());
        return this;
    }

    or(otherPredicate) {
        this.predicates.push((previousResult) =>  previousResult || otherPredicate());
        return this;
    }

    then(producer) {

        let previousResult = true;

        this.predicates.forEach((onePredicate, index) => {
            previousResult = onePredicate(previousResult);
        });

        return producer(previousResult);
    }

    thenHideElement(initialClassName = "") {
        return this.then((condition) => condition ? (initialClassName + " none") : initialClassName);
    }

    thenDisableElement(initialClassName = "") {
        return this.then((condition) => condition ? (initialClassName + " disabled") : initialClassName);
    }

}