export default function on() {
    const stringContains = (expectedString, extractor = alreadyAString => alreadyAString) => {
        return oneItemToTest => extractor(oneItemToTest).toLowerCase().indexOf(expectedString.toLowerCase()) !== -1
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
        emptyString: emptyString
    }
}


