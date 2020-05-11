export default function formatters() {

    const readableOctets = (dataToFormat) => {
        const converters = [
            {
                value: Math.pow(1024, 3),
                unit: 'Go'
            },
            {
                value: Math.pow(1024, 2),
                unit: 'Mo'
            },
            {
                value: 1024,
                unit: 'Ko'
            }
        ]


        let selectedConverter = converters.find(oneConverter => dataToFormat > oneConverter.value)

        
        selectedConverter = selectedConverter ? selectedConverter : {
            value: 1, unit: 'o'
        };
        return Math.ceil(dataToFormat / selectedConverter.value) + ' ' + selectedConverter.unit;

    }



    return {
        readableOctets: readableOctets
    };
}