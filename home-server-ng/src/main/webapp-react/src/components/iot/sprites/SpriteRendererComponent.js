import React from 'react';

import './SpriteRendererComponent.scss'
import { when } from '../../../tools/when';
import IconComponent from '../../commons/IconComponent';
import iotSubEventReactor from '../iotSubEventReactor';
import eventReactor from '../../../eventReactor/EventReactor';




export default function SpriteRendererComponent({ sprite, editMode = true}) {

    const spriteCellClassName = editMode ? "col m1 s2" : "col m1 s3";

    return <li
        onClick={() => eventReactor().shortcuts().selectItem(sprite.name, !sprite.selected, "SPRITE_SELECTION")}
        className={
            when()
                .selected(sprite)
                .thenDefaultSelectColor("collection-item  selectable-sprite-item waves-effect waves-teal")}
    >
        <div className="row">
            <div className={spriteCellClassName}>
                <table className="sprite-renderer">
                    <tbody>
                        {
                            sprite.lines.map((oneLine, lineIndex) =>
                                <tr key={lineIndex} className="sprite-renderer-line">
                                    {oneLine.map((oneCell, cellIndex) =>
                                        <td key={cellIndex} className={when(oneCell.on).css("sprite-renderer-pixel-on", "sprite-renderer-pixel sprite-renderer-pixel-off")}></td>
                                    )}
                                </tr>)}
                    </tbody>
                </table>
            </div>
            <div className="col m10 s8">
                <div className="sprite-description-name">{sprite.name}</div>
                <div>{sprite.description}</div>
            </div>
            <div className={when(!editMode).thenHideElement("col m1 s2 right-align")}>
                <button className="btn-floating" onClick={() => iotSubEventReactor().editSprite(sprite)}><IconComponent icon="edit"></IconComponent></button>
            </div>
        </div>
    </li>;

}