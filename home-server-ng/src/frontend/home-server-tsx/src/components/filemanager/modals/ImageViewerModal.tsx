import {FC, useEffect, useState} from "react"
import {Carousel, CloseButton, Image, Modal} from "react-bootstrap";
import FileDescriptor from "../../../model/filemanager/FileDescriptor";

import "./ImageViewerModal.css";
import {BootstrapText} from "../../bootstrap/BootstrapText";
import CssTools, {CustomClassNames} from "../../bootstrap/CssTools";
import {ChevronLeft, ChevronRight} from "react-bootstrap-icons";

export type ImageViewerModalProps = {
    show: boolean,
    onHide: () => void,
    imagesToDisplay: FileDescriptor[],
    selectedImage: FileDescriptor
}

const ImageViewerModal: FC<ImageViewerModalProps> = ({ show, onHide, imagesToDisplay, selectedImage }) => {

    const [currentIndex, setCurrentIndex] = useState(0);

    useEffect(() => {
        setCurrentIndex(imagesToDisplay.findIndex(anImage => anImage.id === selectedImage.id))
    }, [imagesToDisplay, selectedImage])


    const increaseIndex = () => {
        const newIndex = (currentIndex + 1 <= imagesToDisplay.length - 1) ? currentIndex + 1 : 0
        setCurrentIndex(newIndex);
    }


    return <Modal show={show} fullscreen={true} onHide={() => onHide()}>
        <Modal.Body>

            <Carousel
                interval={null}
                touch
                variant="dark"
                className="image-viewer-carousel"
                activeIndex={currentIndex}
                onSelect={(newIndex) => setCurrentIndex(newIndex)}
                nextIcon={<ChevronRight size={100} className={BootstrapText.ColorSecondary} />}
                prevIcon={<ChevronLeft size={100} className={BootstrapText.ColorSecondary} />}
            >
                {imagesToDisplay.map((anImage, index) => <Carousel.Item key={index} >
                    <div className={CssTools.of("image-viewer-container")
                        .then(CustomClassNames.FullWidth)
                        .then(BootstrapText.AlignCenter)
                        .then(CustomClassNames.VerticallyCentered)
                        .css()}>
                        <Image
                            loading="lazy"
                            src={FileDescriptor.downloadUrl(anImage)}
                            className="image-viewer-image"
                            onClick={() => increaseIndex()}
                        />
                    </div>

                </Carousel.Item>)}
            </Carousel>

            <div className="image-viewer-close-button">
                <CloseButton onClick={() => onHide()} />
            </div>
            <div className="image-viewer-legend">

            </div>

        </Modal.Body>
    </Modal>
}

export default ImageViewerModal;