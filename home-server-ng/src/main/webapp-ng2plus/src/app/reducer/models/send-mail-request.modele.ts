import { ReducedImageInput } from './reduced-image-input.modele';
export class SendMailRequest {
    public imagesToSend: Array<ReducedImageInput>;
    public emailList = new Array<string>();
    public subject: string;
    public message: string;
    public isValid: boolean;

    public validate(): void {
        this.isValid = this.emailList.length > 0
            && this.subject !== undefined
            && this.message !== undefined
            && this.subject !== ''
            && this.message !== '';
    }
}

