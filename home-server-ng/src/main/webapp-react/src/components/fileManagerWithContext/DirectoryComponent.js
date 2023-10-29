import React from 'react';
import {useFileBrowserContext} from './FileBrowserContextDefinition';
import OneDirectoryRenderer from './OneDirectoryRenderer';

import "./FileBrowserCommon.scss";
import OneFileRenderer from './OneFileRenderer';
import {ImageViewerComponent} from '../commons/ImageViewer/ImageViewerComponent';
import {DirectoryDisplayMode} from './DirectoryDisplayMode';
import {MusicPlayerComponent} from '../commons/MusicPlayer/MusicPlayerComponent';


export default function DirectoryComponent() {

    const { currentDirectory, computedOptions, imageViewerState, dispatchCloseViewer, musicPlayerState } = useFileBrowserContext();


    return <>
        <div>
            <ul className="collection">
                {
                    currentDirectory.directories.displayList
                        .map((oneDirectory, oneDirectoryIndex) => <OneDirectoryRenderer
                            key={oneDirectoryIndex}
                            directory={oneDirectory}
                            selectionDisabled={currentDirectory.itemSelectionDisabled || currentDirectory.displayMode === DirectoryDisplayMode.DOWNLOADING}
                            iconResolver={computedOptions.directoryIconResolver}>
                        </OneDirectoryRenderer>)
                }
                {
                    currentDirectory.files.displayList
                        .map((oneFile, oneFileIndex) => <OneFileRenderer
                            key={oneFileIndex}
                            file={oneFile}
                            selectionDisabled={currentDirectory.itemSelectionDisabled || currentDirectory.displayMode === DirectoryDisplayMode.DOWNLOADING}
                            iconResolver={computedOptions.fileIconResolver}>
                        </OneFileRenderer>)
                }

            </ul>
        </div>
        <ImageViewerComponent
            display={imageViewerState.display}
            imageUrlList={imageViewerState.imageUrlList}
            imageBaseUrl={imageViewerState.imageBaseUrl}
            selectedStartupIndex={imageViewerState.selectedIndex}
            onClose={dispatchCloseViewer}>
        </ImageViewerComponent>
        <MusicPlayerComponent
            display={musicPlayerState.display}
            onClose={dispatchCloseViewer}
            fileToPlay={musicPlayerState.fileToPlay}
            musicBaseUrl={musicPlayerState.musicBaseUrl}>

        </MusicPlayerComponent>
    </>
}