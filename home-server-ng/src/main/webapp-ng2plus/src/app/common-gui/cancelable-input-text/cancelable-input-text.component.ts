import { Component, OnInit, Input, OnChanges, EventEmitter, Output, SimpleChanges } from '@angular/core';

@Component({
    selector: 'homeserver-cancelable-input-text',
    templateUrl: 'cancelable-input-text.component.html',
    styleUrls: ['cancelable-input-text.component.scss']
})
export class CancelableInputTextComponent implements OnInit, OnChanges {


    @Input()
    public text: string;

    @Output()
    public textChange = new EventEmitter<string>();

    public dataChanged = false;

    private initialText: string;

    constructor() { }

    ngOnInit() { }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.text && changes.text.isFirstChange()) {
            this.initialText = changes.text.currentValue;
        }
    }

    onKeyUp() {
        this.dataChanged = (this.initialText !== this.text);
        this.textChange.emit(this.text);
    }

    cancel() {
        this.text = this.initialText;
        this.dataChanged = false;
        this.textChange.emit(this.text);
    }
}
