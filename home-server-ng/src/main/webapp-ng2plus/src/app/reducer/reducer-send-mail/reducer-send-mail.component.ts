import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { ContactDescriptor } from './../models/contact-descriptor.modele';
import { ReducedImageInput } from './../models/reduced-image-input.modele';
import { SendMailRequest } from './../models/send-mail-request.modele';
import { ReducerService } from './../reducer.service';
import { ReducedImage } from './../models/reduced-image.modele';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-reducer-send-mail',
    templateUrl: 'reducer-send-mail.component.html',
    styleUrls: ['reducer-send-mail.component.scss', '../../common-gui/styles/edit-zone.scss',
        '../../common-gui/styles/image-container.scss',
        '../../common-gui/styles/dropdown-menu.scss'
    ]
})
export class ReducerSendMailComponent implements OnInit {

    @Input()
    public imagesToSend: Array<ReducedImage>;
    @Output()
    public cancel = new EventEmitter<any>();

    public sendMailRequest = new SendMailRequest();

    public mailAlreadyPresent = false;

    public displayContactSearchResult = false;

    public availableContacts: Array<ContactDescriptor>;

    public contactSearch: string;

    constructor(private reducerService: ReducerService, private notifyer: NotifyerService) { }

    ngOnInit() { }

    public doSearchContact(keyBoardEvent: KeyboardEvent): void {
        if (this.contactSearch !== '') {
            if (keyBoardEvent.key === 'Enter') {
                this.addContact();
            } else {
                this.reducerService.searchContact(this.contactSearch).subscribe(res => {
                    this.availableContacts = res;
                    this.displayContactSearchResult = (this.availableContacts.length > 0);
                });
            }
            this.mailAlreadyPresent = this.sendMailRequest.emailList.filter(oneMail => oneMail === this.contactSearch).length > 0;
        } else {
            this.displayContactSearchResult = false;
        }
    }

    public selectContactProposition(contact: ContactDescriptor): void {
        this.contactSearch = contact.email;
        this.displayContactSearchResult = false;
    }

    public addContact(): void {
        // si mail pas déjà ajouté
        if (this.sendMailRequest.emailList.filter(oneMail => oneMail === this.contactSearch).length === 0) {
            this.sendMailRequest.emailList.push(this.contactSearch);
            this.contactSearch = '';
        }

        this.sendMailRequest.validate();
    }

    public removeContact(contactToRemove: string): void {
        this.sendMailRequest.emailList = this.sendMailRequest.emailList.filter(oneMail => oneMail !== contactToRemove);
        this.sendMailRequest.validate();
    }

    public innerCancel(): void {
        this.cancel.emit();
    }

    public innerSendMail(): void {
        this.notifyer.waitingInfo('envoi du mail en cours');
        this.sendMailRequest.imagesToSend = this.imagesToSend.map(oneImage => new ReducedImageInput(oneImage.id));
        this.reducerService.sendMail(this.sendMailRequest).subscribe(res => {
            this.notifyer.hide();
            this.cancel.emit();
        });
    }
}

