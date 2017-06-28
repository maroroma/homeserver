import { FileDescriptor } from './../../shared/file-descriptor.modele';
export class ReducedImage extends FileDescriptor {

    public static fromRaw(rawReducedImage: any): ReducedImage {
        return FileDescriptor.genericFromRaw(rawReducedImage, ReducedImage);
    }

}
