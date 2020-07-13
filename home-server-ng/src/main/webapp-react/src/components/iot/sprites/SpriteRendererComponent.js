import React from 'react';

import './SpriteRendererComponent.scss'
import { when } from '../../../tools/when';
import IconComponent from '../../commons/IconComponent';
import iotSubEventReactor from '../iotSubEventReactor';
import eventReactor from '../../../eventReactor/EventReactor';




export default function SpriteRendererComponent({ sprite, editMode }) {


    return <li
        onClick={() => eventReactor().shortcuts().selectItem(sprite.name, !sprite.selected)}
        className={
            when()
                .selected(sprite)
                .thenDefaultSelectColor("collection-item  selectable-sprite-item waves-effect waves-teal")}
    >
        <div className="row">
            <div className="col m1 s2">
                <table className="sprite-renderer">
                    {
                        sprite.lines.map((oneLine, lineIndex) =>
                            <tr key={lineIndex} className="sprite-renderer-line">
                                {oneLine.map((oneCell, cellIndex) =>
                                    <td key={cellIndex} className={when(oneCell.on).css("sprite-renderer-pixel-on", "sprite-renderer-pixel sprite-renderer-pixel-off")}></td>
                                )}
                            </tr>)}
                </table>
            </div>
            <div className="col m10 s8">
                <div className="sprite-description-name">{sprite.name}</div>
                <div>{sprite.description}</div>
            </div>
            <div className={when(!editMode).thenHideElement("col m1 s2 right-align")}>
                <btn className="btn-floating" onClick={() => iotSubEventReactor().editSprite(sprite)}><IconComponent icon="edit"></IconComponent></btn>
            </div>
        </div>
    </li>;

}