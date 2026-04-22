import { useEffect, useRef, useState, type FC } from "react";
import { Form, Image } from "react-bootstrap";
import ThumbIconComponent from "../thumb/ThumbIconComponent";
import { Question } from "react-bootstrap-icons";
import CssTools from "../../tools/CssTools";

import "./ImageUploadComponent.css"

type ImageUploadComponentProps = {
    title: string,
    onImageAsBase64Loaded?: (imageAsBase64: string) => void
}

const ImageUploadComponent: FC<ImageUploadComponentProps> = ({ title, onImageAsBase64Loaded = () => { } }) => {

    const fileInputRef = useRef<HTMLInputElement>(null);

    const [imageFileToUpload, setImageFileToUpload] = useState<File>();
    const [imageAsBase64, setImageAsBase64] = useState("");

    useEffect(() => {
        if (imageFileToUpload) {
            console.log("thumbToUpload", imageFileToUpload);
            const reader = new FileReader();
            reader.readAsDataURL(imageFileToUpload);
            reader.addEventListener("load", () => {
                setImageAsBase64(reader.result as string)
            })
        } else {
            setImageAsBase64("");
        }

    }, [imageFileToUpload]);

    useEffect(() => {
        onImageAsBase64Loaded(imageAsBase64);
    }, [imageAsBase64])



    return <>

        <Form.Group>
            <Form.Label>{title}</Form.Label>
        </Form.Group>

        {!imageFileToUpload ?
            <div onClick={() => { fileInputRef.current?.click() }} className={CssTools.of().clickable().css()}>
                <ThumbIconComponent icon={<Question />} size="small" className="red"/>
            </div> : <Image src={imageAsBase64}
                onClick={() => { fileInputRef.current?.click() }}
                className={CssTools.of("image-to-upload").clickable().css()}
            />
        }

        <input ref={fileInputRef} type="file" className="hidden-input-file" onChange={(event: any) => setImageFileToUpload(event.target.files[0])} />
    </>
}


export default ImageUploadComponent;