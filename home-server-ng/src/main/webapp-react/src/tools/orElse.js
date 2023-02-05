export default function orElse(optionalValue, defaultValue) {
    return (optionalValue === undefined || optionalValue === null) ? defaultValue : optionalValue;
}