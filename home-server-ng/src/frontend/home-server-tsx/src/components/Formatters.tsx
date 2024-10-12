export default class Formatters {
    static octets(dataToFormat: number): string {
        const subConverters = [
            OctetConverter.giga(),
            OctetConverter.mega(),
            OctetConverter.kilo()
        ]

        let selectedConverter = subConverters.find(aConverter => aConverter.matches(dataToFormat));

        selectedConverter = selectedConverter ? selectedConverter : OctetConverter.default()

        return selectedConverter?.convert(dataToFormat);

    }
}

class OctetConverter {

    static giga(): OctetConverter {
        return new OctetConverter(Math.pow(1024, 3), "Go");
    }

    static mega(): OctetConverter {
        return new OctetConverter(Math.pow(1024, 2), "Mo");
    }

    static kilo(): OctetConverter {
        return new OctetConverter(Math.pow(1024, 1), "Ko");
    }

    static default(): OctetConverter {
        return new OctetConverter(1, "o")
    }

    constructor(private limit: number, public unit: string) { }

    public matches(dataToFormat: number): boolean {
        return dataToFormat > this.limit;
    }

    public convert(dataToFormat: number): string {
        return `${Math.floor((dataToFormat / this.limit) * 100) / 100} ${this.unit}`

    }


}