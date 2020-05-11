export function when(condition) {
    if (typeof condition === "boolean") 
    {
        return new When(input => condition);
    }
    return new When(condition);
}

export class When {

    constructor(firstPredicate = () => true) {
        this.predicates = [(previousResult) => previousResult && firstPredicate()];
        this.and = this.and.bind(this);
        this.then = this.then.bind(this);
        this.thenHideElement = this.thenHideElement.bind(this);
        this.thenDisableElement = this.thenDisableElement.bind(this);
        this.selected = this.selected.bind(this);
    }

    selected(oneSelectableItem) {
        return this.and(() => oneSelectableItem.selected);
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


    // spécialisation css
    css(classNameToAdd, initialClassName = "") {
        return this.then(condition => condition ? (`${initialClassName} ${classNameToAdd}`) : initialClassName);
    }


    thenHideElement(initialClassName = "") {
        return this.css("none", initialClassName);
    }
    
    // spécialisation css
    thenDisableElement(initialClassName = "") {
        return this.css("disabled", initialClassName);
    }

    thenDefaultSelectColor(initialClassName = "") {
        return this.css("light-blue lighten-4", initialClassName);
    }

    thenPlopSelectedItem(initialClassName = "") {
        return this.css("green-font jello-horizontal", initialClassName);
    }

}