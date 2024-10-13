import {FC, useEffect, useState} from "react"
import {Carousel, CloseButton, Image, Modal} from "react-bootstrap";
import FileDescriptor from "../../../model/filemanager/FileDescriptor";

import "./ImageViewerModal.css";
import "./MusicPlayerModal.css";
import {BootstrapText} from "../../bootstrap/BootstrapText";
import CssTools, {CustomClassNames} from "../../bootstrap/CssTools";
import {ChevronLeft, ChevronRight} from "react-bootstrap-icons";
import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";
import FileExtension from "../../../model/filemanager/FileExtension";

export type MusicPlayerModalProps = {
    show: boolean,
    onHide: () => void,
    musicsToPlay: FileDescriptor[],
    selectedMusic: FileDescriptor,
    currentDirectory: FileDirectoryDescriptor
}

const MusicPlayerModal: FC<MusicPlayerModalProps> = ({ show, onHide, musicsToPlay, selectedMusic, currentDirectory }) => {

    const [currentIndex, setCurrentIndex] = useState(0);
    const [currentTrack, setCurrentTrack] = useState(FileDescriptor.emptyFileDescriptor());
    const [folderImage, setFolderImage] = useState<FileDescriptor | undefined>(FileDescriptor.emptyFileDescriptor());

    useEffect(() => {
        setCurrentIndex(musicsToPlay.findIndex(aMusic => aMusic.id === selectedMusic.id))


        const albumart = currentDirectory.files
            .filter(FileExtension.IMAGES.fileDescriptorFilter())
            .find(FileDescriptor.filter("FOLDER", "albumart", "album_art"))
        setFolderImage(albumart);



    }, [musicsToPlay, selectedMusic, currentDirectory])

    useEffect(() => {
        setCurrentTrack(musicsToPlay[currentIndex])
    }, [currentIndex, musicsToPlay])


    const increaseIndex = () => {
        const newIndex = (currentIndex + 1 <= musicsToPlay.length - 1) ? currentIndex + 1 : 0
        setCurrentIndex(newIndex);
    }


    return <Modal show={show} fullscreen={true} onHide={() => onHide()}>
        <Modal.Body>

            <Carousel
                interval={null}
                touch
                variant="dark"
                className="music-player-carousel"
                activeIndex={currentIndex}
                onSelect={(newIndex) => {
                    setCurrentIndex(newIndex)
                }}
                nextIcon={<ChevronRight size={100} className={BootstrapText.ColorSecondary} />}
                prevIcon={<ChevronLeft size={100} className={BootstrapText.ColorSecondary} />}
            >
                {musicsToPlay.map((anImage, index) => <Carousel.Item key={index} >
                    <div className={CssTools.of("folder-container")
                        .then(CustomClassNames.FullWidth)
                        .then(BootstrapText.AlignCenter)
                        .then(CustomClassNames.VerticallyCentered)
                        .css()}>
                        {folderImage ?
                            <Image
                                loading="lazy"
                                src={FileDescriptor.downloadUrl(folderImage)}
                                className="folder-image"
                                onClick={() => increaseIndex()}
                            />
                            : <></>
                        }
                    </div>
                    <Carousel.Caption>
                        <h2>
                            {anImage.name}
                        </h2>
                    </Carousel.Caption>

                </Carousel.Item>)}
            </Carousel>
            <div className={BootstrapText.AlignCenter}>
                {currentTrack ?
                    <audio
                        controls={true}
                        autoPlay={true}
                        onEnded={() => increaseIndex()}
                        src={FileDescriptor.downloadUrl(currentTrack)}></audio>
                    : <></>
                }

            </div>

            <div className="image-viewer-close-button">
                <h2><CloseButton onClick={() => onHide()} /></h2>
            </div>
            <div className="image-viewer-legend">

            </div>

        </Modal.Body>
    </Modal>
}

export default MusicPlayerModal;