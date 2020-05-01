export default function on() {
    const stringContains = (expectedString, extractor = alreadyAString => alreadyAString) => {
        return oneItemToTest => extractor(oneItemToTest).toLowerCase().indexOf(expectedString.toLowerCase()) !== -1
    }

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
        defined: defined
    }
}


