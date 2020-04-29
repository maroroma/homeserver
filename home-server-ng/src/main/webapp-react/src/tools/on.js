export default function on() {
    const stringContains = (expectedString, extractor = alreadyAString => alreadyAString) => {
        return oneItemToTest => extractor(oneItemToTest).toLowerCase().indexOf(expectedString.toLowerCase()) !== -1
    }

    return {
        stringContains: stringContains
    }
}


