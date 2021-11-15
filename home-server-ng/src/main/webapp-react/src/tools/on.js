export default function on() {
    const stringContains = (expectedString, extractor = alreadyAString => alreadyAString) => {
        return oneItemToTest => extractor(oneItemToTest).toLowerCase().indexOf(expectedString.toLowerCase()) !== -1;
    }

    /**
     * On est sur du ou logique pour ce test
     */
    const stringContainsOneOf = (expectedStrings, extractor = alreadyAString => alreadyAString) => {
        return oneItemToTest => expectedStrings
            .filter(oneExpectedString => extractor(oneItemToTest).toLowerCase().indexOf(oneExpectedString.toLowerCase()) !== -1).length > 0;

    }

    const stringContainsAllOf = (expectedStrings, extractor = alreadyAString => alreadyAString) => {
        return oneItemToTest => {
            // let previousResult = true;
            // expectedStrings
            //     .forEach(oneExpectedString => { previousResult = previousResult && stringContains(oneExpectedString, extractor)(oneItemToTest) });
            // return previousResult;

            return expectedStrings
            .map(oneExpectedString => stringContains(oneExpectedString, extractor))
            .reduce((previousResult, currentFilter) => previousResult && currentFilter(oneItemToTest), true);

        }

    }

    const stringMatches = (expectedRegex, extractor = alreadyAString => alreadyAString) => {
        const regex = new RegExp(expectedRegex);
        return oneItemToTest => regex.test(extractor(oneItemToTest));
    }

    const notNull = (extractor = underTest => underTest) => {
        return oneItemToTest => extractor(oneItemToTest) !== null;
    }

    const stringIsNotEmpty = (extractor = alreadyAString => alreadyAString) => (oneItemToTest) => extractor(oneItemToTest) !== "";

    const emptyString = (extractor = alreadyAString => alreadyAString) => (oneItemToTest) => extractor(oneItemToTest) === "";

    const selected = () => {
        return oneItem => oneItem.selected;
    }

    const passthrough = () => {
        return oneItem => true;
    }

    const defined = () => {
        return oneItem => oneItem !== undefined;
    }

    return {
        stringContains: stringContains,
        selected: selected,
        passthrough: passthrough,
        defined: defined,
        stringIsNotEmpty: stringIsNotEmpty,
        emptyString: emptyString,
        stringMatches: stringMatches,
        notNull: notNull,
        stringContainsOneOf: stringContainsOneOf,
        stringContainsAllOf: stringContainsAllOf
    }
}


