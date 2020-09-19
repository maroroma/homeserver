export default function orElse(optionalValue, defaultValue) {
    return optionalValue !== undefined ? optionalValue : defaultValue;
}