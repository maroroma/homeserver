import {FC, useEffect, useState} from "react";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import StartWIPAction from "../../context/actions/StartWIPAction";
import FileManagerRequester from "../../api/FileManagerRequester";
import LoadedRootDirectoriesAction from "../../context/actions/filemanager/LoadedRootDirectoriesAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import DirectoryStackComponent from "./DirectoryStackComponent";
import {ListGroup} from "react-bootstrap";
import SwitchSelectDirectoryAction from "../../context/actions/filemanager/SwitchSelectDirectoryAction";
import SelectableItem from "../../model/SelectableItem";
import LoadedDirectoryAction from "../../context/actions/filemanager/LoadedDirectoryAction";
import FileDescriptor from "../../model/filemanager/FileDescriptor";

import "./filemanager.css";
import FileDescriptorRenderer from "./FileDescriptorRenderer";
import DirectoryDescriptorIconResolver from "../../model/filemanager/DirectoryDescriptorIconResolver";
import FileDescriptorIconResolver from "../../model/filemanager/FileDescriptorIconResolver";
import SwitchSelectFileAction from "../../context/actions/filemanager/SwitchSelectFileAction";
import SelectableItems from "../../model/SelectableItems";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionUnselectAllButton from "../actionmenu/ActionUnselectAllButton";
import ActionSelectAllButton from "../actionmenu/ActionSelectAllButton";
import SwitchAllDirectoryContentAction from "../../context/actions/filemanager/SwitchAllDirectoryContentAction";
import ActionDeleteButton from "../actionmenu/ActionDeleteButton";
import ActionEditButton from "../actionmenu/ActionEditButton";
import RenameFileModal from "./modals/RenameFileModal";
import DeleteFilesModal from "./modals/DeleteFilesModal";
import ActionDownloadButton from "../actionmenu/ActionDownloadButton";
import ActionUploadButton from "../actionmenu/ActionUploadButton";
import FileDirectoryDescriptor from "../../model/filemanager/FileDirectoryDescriptor";
import DownloadableFileDescriptorRenderer from "./DownloadableFileDescriptorRenderer";
import DownloadIconResolver from "../../model/filemanager/DownloadIconResolver";
import ActionBackButton from "../actionmenu/ActionBackButton";
import UploadFilesModal from "./modals/UploadFilesModal";
import ImageViewerModal from "./modals/ImageViewerModal";
import FileExtension from "../../model/filemanager/FileExtension";
import {FileViewers} from "../../model/filemanager/FileViewers";
import ActionConfigButton from "../actionmenu/ActionConfigButton";
import RootDirectoriesModal from "./modals/RootDirectoriesModal";
import ActionPlusButton from "../actionmenu/ActionPlusButton";
import AddDirectoryModal from "./modals/AddDirectoryModal";
import MusicPlayerModal from "./modals/MusicPlayerModal";

const FileManagerComponent: FC = () => {


    const { fileManagerSubState, dispatch, searchString } = useHomeServerContext();

    const [filteredDirectories, setFilteredDirectories] = useState<SelectableItems<FileDescriptor>>(SelectableItems.empty())
    const [filteredFiles, setFilteredFiles] = useState<SelectableItems<FileDescriptor>>(SelectableItems.empty())

    const [displayRenameModal, setDisplayRenameModal] = useState(false);
    const [selectedFileForRename, setSelectedFileForRename] = useState(FileDescriptor.emptyFileDescriptor())

    const [displayDeleteFilesModal, setDisplayDeleteFilesModal] = useState(false);
    const [filesToDelete, setFilesToDelete] = useState<FileDescriptor[]>([]);

    const [downloadMode, setDownloadMode] = useState(false);

    const [displayUploadFilesModal, setDisplayUploadFilesModal] = useState(false);

    const [displayImageViewer, setDisplayImageViewer] = useState(false);
    const [imagesToDisplay, setImagesToDisplay] = useState<FileDescriptor[]>([]);
    const [selectedImageDisplay, setSelectedImageDiplay] = useState(FileDescriptor.emptyFileDescriptor());

    const [displayMusicPlayer, setDisplayMusicPlayer] = useState(false);
    const [musicsToPlay, setMusicsToPlay] = useState<FileDescriptor[]>([]);
    const [selectedMusic, setSelectedMusic] = useState(FileDescriptor.emptyFileDescriptor());

    const [displayRootConfig, setDisplayRootConfig] = useState(false);

    const [displayAddDirectory, setDisplayAddDirectory] = useState(false);

    const resolveViewerToUse = (fileDescriptor: FileDescriptor) => {
        const extension = FileExtension.resolve(fileDescriptor);
        switch (extension.viewer) {
            case FileViewers.IMAGE:
                setDisplayImageViewer(true);
                setImagesToDisplay(fileManagerSubState
                    .filesFromCurrentDirectory
                    .filter(extension.fileDescriptorFilter())
                    .items
                    .map(selectableItem => selectableItem.item)
                    .sort(FileDescriptor.sorter())
                )
                setSelectedImageDiplay(fileDescriptor)
                break;
            case FileViewers.MUSIC:
                setDisplayMusicPlayer(true);
                setMusicsToPlay(fileManagerSubState
                    .filesFromCurrentDirectory
                    .filter(extension.fileDescriptorFilter())
                    .items
                    .map(selectableItem => selectableItem.item)
                    .sort(FileDescriptor.sorter())
                )
                setSelectedMusic(fileDescriptor)
                break;
        }
    }


    useEffect(() => {

        if (fileManagerSubState.rootDirectories.length === 0) {
            dispatch(new StartWIPAction("Chargement des répertoires racines"));

            FileManagerRequester.getRootDirectories()
                .then(response => dispatch(new LoadedRootDirectoriesAction(response)))
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur recontrée lors du chargement des répertoires racines")))
        }


    }, [fileManagerSubState.rootDirectories]);

    useEffect(() => {
        setFilteredDirectories(fileManagerSubState.directoriesFromCurrentDirectory.filter(FileDescriptor.filter(searchString)))
        setFilteredFiles(fileManagerSubState.filesFromCurrentDirectory.filter(FileDescriptor.filter(searchString)))

    }, [fileManagerSubState.directoriesFromCurrentDirectory, fileManagerSubState.filesFromCurrentDirectory, searchString])


    const loadSubDirectory = (aDirectory: SelectableItem<FileDescriptor>) => {
        dispatch(new StartWIPAction("Chargement du répertoire"));

        FileManagerRequester.getDirectoryDetails(aDirectory.item)
            .then(response => {
                dispatch(new LoadedDirectoryAction(response));
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du chargement du répertoire")))
    }

    const startRenamingFile = () => {
        if (fileManagerSubState.directoriesFromCurrentDirectory.someSelected()) {
            setSelectedFileForRename(fileManagerSubState.directoriesFromCurrentDirectory.selectedItems()[0])
        } else {
            setSelectedFileForRename(fileManagerSubState.filesFromCurrentDirectory.selectedItems()[0])
        }
        setDisplayRenameModal(true);
    }

    const startDeletingFiles = () => {
        setFilesToDelete(
            fileManagerSubState.directoriesFromCurrentDirectory.selectedItems()
                .concat(fileManagerSubState.filesFromCurrentDirectory.selectedItems())
        );
        setDisplayDeleteFilesModal(true);
    }


    return <div>
        <DirectoryStackComponent />

        {downloadMode !== true
            ? <>
                <ListGroup>
                    {filteredDirectories.items.map((aDirectory, directoryIndex) => <FileDescriptorRenderer
                        key={directoryIndex}
                        onCheck={(selectableItem) => dispatch(new SwitchSelectDirectoryAction(selectableItem))}
                        onClick={(selectableItem) => loadSubDirectory(selectableItem)}
                        selectableFileDescriptor={aDirectory}
                        fileIconResolver={new DirectoryDescriptorIconResolver()}
                    />
                    )}

                    {filteredFiles.items.map((aFile, fileIndex) => <FileDescriptorRenderer
                        key={fileIndex}
                        onCheck={(selectableItem) => dispatch(new SwitchSelectFileAction(selectableItem))}
                        onClick={(selectableItem) => resolveViewerToUse(selectableItem.item)}
                        selectableFileDescriptor={aFile}
                        fileIconResolver={new FileDescriptorIconResolver()}

                    />)}
                </ListGroup>
                <ActionMenuComponent alreadyOpen={true} small>
                    <ActionDeleteButton
                        disabled={fileManagerSubState.directoriesFromCurrentDirectory.noneSelected() && fileManagerSubState.filesFromCurrentDirectory.noneSelected()}
                        onClick={() => startDeletingFiles()}
                        hidden={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory)}
                    />
                    <ActionEditButton
                        disabled={(fileManagerSubState.directoriesFromCurrentDirectory.nbSelected() + fileManagerSubState.filesFromCurrentDirectory.nbSelected()) !== 1}
                        onClick={() => startRenamingFile()}
                        hidden={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory)}
                    />
                    <ActionPlusButton
                        onClick={() => setDisplayAddDirectory(true)}
                        hidden={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory)}
                    />
                    <ActionDownloadButton
                        onClick={() => setDownloadMode(!downloadMode)}
                        hidden={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory) || filteredFiles.items.length === 0}
                    />
                    <ActionUploadButton
                        disabled={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory)}
                        onClick={() => setDisplayUploadFilesModal(true)}
                        hidden={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory)}
                    />
                    <ActionUnselectAllButton
                        onClick={() => dispatch(SwitchAllDirectoryContentAction.unselectAll())}
                        hidden={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory) || (filteredFiles.noneSelected() && filteredDirectories.noneSelected())}
                    />
                    <ActionSelectAllButton
                        onClick={() => dispatch(SwitchAllDirectoryContentAction.selectAll())}
                        hidden={FileDirectoryDescriptor.isRoot(fileManagerSubState.currentDirectory) || (filteredDirectories.allSelected() && filteredFiles.allSelected())}
                    />
                    <ActionConfigButton onClick={() => setDisplayRootConfig(true)} />
                </ActionMenuComponent>
            </>
            : <>
                <ListGroup>

                    {filteredDirectories.items.map((aDirectory, directoryIndex) => <DownloadableFileDescriptorRenderer
                        key={directoryIndex}
                        selectableFileDescriptor={aDirectory}
                        fileIconResolver={new DirectoryDescriptorIconResolver()}
                        disabled
                    />
                    )}

                    {filteredFiles.items.map((aFile, fileIndex) => <DownloadableFileDescriptorRenderer
                        key={fileIndex}
                        selectableFileDescriptor={aFile}
                        fileIconResolver={new DownloadIconResolver()}
                    />)}
                </ListGroup>
                <ActionMenuComponent alreadyOpen={true}>
                    <ActionBackButton onClick={() => setDownloadMode(false)} />
                </ActionMenuComponent>
            </>
        }







        <RenameFileModal
            fileToRename={selectedFileForRename}
            show={displayRenameModal}
            onHide={() => setDisplayRenameModal(false)}
            parentDirectory={fileManagerSubState.currentDirectory}
        />

        <DeleteFilesModal
            show={displayDeleteFilesModal}
            onHide={() => setDisplayDeleteFilesModal(false)}
            parentDirectory={fileManagerSubState.currentDirectory}
            filesToDelete={filesToDelete}
        />
        <UploadFilesModal
            show={displayUploadFilesModal}
            onHide={() => setDisplayUploadFilesModal(false)}
            parentDirectory={fileManagerSubState.currentDirectory}
        />
        <ImageViewerModal
            show={displayImageViewer}
            onHide={() => { setDisplayImageViewer(false) }}
            imagesToDisplay={imagesToDisplay}
            selectedImage={selectedImageDisplay}
        />

        <MusicPlayerModal
            show={displayMusicPlayer}
            onHide={() => { setDisplayMusicPlayer(false) }}
            musicsToPlay={musicsToPlay}
            selectedMusic={selectedMusic}
            currentDirectory={fileManagerSubState.currentDirectory}
        />

        <RootDirectoriesModal
            show={displayRootConfig}
            onHide={() => setDisplayRootConfig(false)}
        />

        <AddDirectoryModal
            show={displayAddDirectory}
            onHide={() => setDisplayAddDirectory(false)}
            parentDirectory={fileManagerSubState.currentDirectory}
        />



    </div>
}

export default FileManagerComponent;