import { useEffect, useState, type FC } from "react";
import FadeInPage from "../FadeInPage";
import { useAlbumProject, useCustomNavigate } from "../hooks/CustomHooks";
import { ConeStriped } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import CssTools from "../../tools/CssTools";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { Form } from "react-bootstrap";
import MenuItemApplyComponent from "../menu/MenuItemApplyComponent";
import { AlbumProjectRequester } from "../../api/requesters/AlbumProjectRequester";
import FilesUploadComponent, { type FileWithNewName } from "../uploaders/FilesUploadComponent";
import FileInProgressRenderer from "./FileInProgressRenderer";
import { FileInProgress } from "../../api/model/albumproject/FileInProgress";
import WindowTool from "../../tools/WindowTool";


const AddTracksToAlbumProjectComponent: FC = () => {

    const navigate = useCustomNavigate();
    const { albumProject } = useAlbumProject();


    const [workInProgress, setWorkInProgress] = useState(false);


    const [filesToUpload, setFilesToUpload] = useState<FileWithNewName[]>([]);
    const [filesInProgress, setFilesInProgress] = useState<FileInProgress[]>([]);
    const [fileInProgressForAlbumUpload, setFileInProgressForAlbumUpload] = useState<FileInProgress>();

    const [wipIndex, setWipIndex] = useState<number>(-1);

    const createAlbumDirectoryOnMusicSource = () => {

        setFileInProgressForAlbumUpload(FileInProgress.fromAlbum(albumProject))



        AlbumProjectRequester.createAlbumOnMusicSource(albumProject.projectId)
            .then(() => {
                setFileInProgressForAlbumUpload(FileInProgress.albumSuccessFull(albumProject))
                launchUploadOfAllFiles()
            });
    }


    const launchUploadOfAllFiles = () => {

        const newWorkList = filesToUpload.map(aFile => FileInProgress.fromFile(aFile));

        setFilesInProgress(newWorkList);
        WindowTool.scrollToTop();

        setWorkInProgress(true);
        setWipIndex(0);
    }

    useEffect(() => {

        if (wipIndex == filesInProgress.length && filesInProgress.length > 0) {
            setWipIndex(-1);
            setFilesInProgress([]);
            setWorkInProgress(false);


            AlbumProjectRequester.deleteProject(albumProject.projectId)
                .then(() => navigate(Paths.ADD_ALBUM.resolve(albumProject.artistId)));

            return;
        }

        if (wipIndex > -1) {
            const currentFile = filesInProgress[wipIndex];
            AlbumProjectRequester.uploadTrackToAlbumProject(albumProject.projectId, currentFile)
                .then(uploadedFile => updateStatusForOneFile(uploadedFile))
                .then(uploadedFile => AlbumProjectRequester.renameOneFile(albumProject.projectId, uploadedFile))
                .then(renamedFile => updateStatusForOneFile(renamedFile))
                .then(renamedFile => AlbumProjectRequester.applyMp3Tags(albumProject.projectId, renamedFile))
                .then(taggedFile => updateStatusForOneFile(taggedFile))
                .then(taggedFile => AlbumProjectRequester.copyFileToMusicSource(albumProject.projectId, taggedFile))
                .then(copiedFile => updateStatusForOneFile(copiedFile))
                .then(() => setWipIndex(wipIndex + 1))
                .catch((error) => {
                    console.error("error while managing file", currentFile, error);
                    updateStatusForOneFile(currentFile.failed())
                })
        }

    }, [wipIndex]);

    const updateStatusForOneFile = (modifiedFile: FileInProgress): FileInProgress => {
        const updatedList = filesInProgress.map(aFileFromList => {
            if (aFileFromList.fileWithNewName.file?.name === modifiedFile.fileWithNewName.file?.name) {
                return modifiedFile;
            } else {
                return aFileFromList;
            }
        })

        setFilesInProgress(updatedList);

        return modifiedFile

    }

    return (
        <FadeInPage
            label={`Uploader les tracks à l'album ${albumProject.albumName}`}
            icon={<ConeStriped />}
            onClick={() => navigate(Paths.ONE_ARTIST.resolve(albumProject.artistId))}
        >
            <div className={CssTools.of().defaultPadding().css()}>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                    <Form.Label>Nom de l'album</Form.Label>
                    <Form.Control type="text"
                        value={albumProject.albumName}
                        disabled
                    />
                </Form.Group>
                {workInProgress === false ?
                    <FilesUploadComponent title="Sélectionnez les fichiers" onFilesUpdated={(event) => setFilesToUpload(event)} />
                    : <>

                        {fileInProgressForAlbumUpload ? <FileInProgressRenderer fileInProgress={fileInProgressForAlbumUpload} /> : <></>}

                        {filesInProgress.map(aFileInProgress => <FileInProgressRenderer
                            fileInProgress={aFileInProgress}
                            key={aFileInProgress.fileWithNewName.file?.name}
                        />)}
                    </>

                }
            </div>

            <MenuComponent>
                <MenuItemBackComponent
                    disabled={workInProgress}
                    onClick={() => navigate(Paths.ONE_ARTIST.resolve(albumProject.artistId))}
                />
                <MenuItemApplyComponent
                    valid={filesToUpload.length !== 0 && !workInProgress}
                    onClick={() => createAlbumDirectoryOnMusicSource()}
                />
            </MenuComponent>
        </FadeInPage>
    );
}

export default AddTracksToAlbumProjectComponent;