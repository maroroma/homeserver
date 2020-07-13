import React, { useState, useEffect } from 'react';
import "./SpriteEditorComponent.scss";
import { when } from '../../../tools/when';
import eventReactor from '../../../eventReactor/EventReactor';
import iotSubEventReactor from '../iotSubEventReactor';

export default function SpriteEditorComponent({ spriteToEdit, popupDriver, newSprite = false }) {

    const [spriteUnderEdition, setSpriteUnderEdition] = useState({});

    useEffect(() => {
        setSpriteUnderEdition({ ...spriteToEdit });
    }, [spriteToEdit]);

    useEffect(() => {
        return eventReactor().shortcuts().onModalOk(onePopupDriver => {
            if (onePopupDriver.id === popupDriver.id) {
                if (!newSprite) {
                    iotSubEventReactor().saveSprite(spriteUnderEdition);
                } else {
                    iotSubEventReactor().createSprite(spriteUnderEdition);
                }
            }
        })
    }, [popupDriver, spriteUnderEdition])

    const updateDescription = (newDescription) => setSpriteUnderEdition({ ...spriteUnderEdition, description: newDescription });
    const updateName = (newNAme) => setSpriteUnderEdition({ ...spriteUnderEdition, name: newNAme });
    
    
    const updateCell = (lineIndex, cellIndex) => setSpriteUnderEdition({
        ...spriteUnderEdition,
        lines: spriteUnderEdition.lines.map((oneLine, oneLineIndex) => {
            if (oneLineIndex === lineIndex) {
                return oneLine.map((oneCell, oneCellIndex) => {
                    if (oneCellIndex === cellIndex) {
                        return { ...oneCell, on: !oneCell.on }
                    } else {
                        return oneCell;
                    }
                })
            } else {
                return oneLine;
            }
        })
    });

    const editNamePanel = newSprite ? <div>
            <input type="text" className="validate"
                value={spriteUnderEdition.name}
                placeholder="Nom / identifiant du sprite"
                onChange={(event) => updateName(event.target.value)} />
        </div> : null;


    return <div>
        {editNamePanel}
        <div>
            <input type="text" className="validate"
                value={spriteUnderEdition.description}
                placeholder={newSprite ? 'Description du sprite' : spriteToEdit.description}
                onChange={(event) => updateDescription(event.target.value)} />
        </div>
        <div className="center-align full-width">
            <table className="sprite-editor">
                {
                    spriteUnderEdition?.lines?.map((oneLine, lineIndex) =>
                        <tr key={lineIndex} className="sprite-editor-line">
                            {oneLine.map((oneCell, cellIndex) =>
                                <td key={cellIndex}
                                    className={when(oneCell.on).css("sprite-editor-pixel-on", "sprite-editor-pixel sprite-editor-pixel-off")}
                                    onClick={() => updateCell(lineIndex, cellIndex)}
                                >

                                </td>
                            )}
                        </tr>)}
            </table>
        </div>
    </div>
}