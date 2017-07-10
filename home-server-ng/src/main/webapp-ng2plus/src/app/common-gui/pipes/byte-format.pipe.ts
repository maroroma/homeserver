import { FilterTools } from './../../shared/filter-tools.service';
import { Pipe, PipeTransform } from '@angular/core';

/**
 * PErmet de convertir une valeur brute de byte en format lisible (Go, mo, etc)
 * 
 * @export
 * @class ByteFormatPipe
 * @implements {PipeTransform}
 */
@Pipe({
    name: 'byteFormat'
})
export class ByteFormatPipe implements PipeTransform {

    private converters = new Array<InnerByteConverter>();
    constructor() {
        this.converters.push(new InnerByteConverter(Math.pow(1024, 3), 'Go'));
        this.converters.push(new InnerByteConverter(Math.pow(1024, 2), 'Mo'));
        this.converters.push(new InnerByteConverter(1024, 'ko'));
        this.converters.push(new IdentityConverter(0, 'o'));
    }

    transform(rawValue: string): string {
        const value = parseInt(rawValue, 10);
        let converter = this.converters.find(oneConverter => value > oneConverter.value);
        if (converter == null) {
            converter = FilterTools.last(this.converters);
        }

        return converter.transform(value);
    }
}

export class InnerByteConverter {
    public value: number;
    public unit: string;
    constructor(value: number, unit: string) {
        this.value = value;
        this.unit = unit;
    }
    public transform(value: number): string {
        return Math.floor(value / this.value) + ' ' + this.unit;
    }
}

export class IdentityConverter extends InnerByteConverter {

    public transform(value: number): string {
        return this.value + ' ' + this.unit;
    }

}
