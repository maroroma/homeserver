export default function dropDownUtils() {
    const dropDownDescription = (prefix, id) => {
        return {
            triggerId: `dropdown-${prefix}-trigger-${id}`,
            targetId: `dropdown-${prefix}-target-${id}`
        }
    }

    return {
        dropDownDescription: dropDownDescription
    }
}