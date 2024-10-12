import {FC} from "react"
import MiniSprite from "../../model/iot/MiniSprite"

import "./MiniSpriteRenderer.css"
import CssTools from "../bootstrap/CssTools"

export type MiniSpriteRendererProps = {
    miniSprite: MiniSprite
}

const MiniSpriteRenderer: FC<MiniSpriteRendererProps> = ({ miniSprite }) => {
    return <table className="small-renderer">
        {miniSprite.lines.map((aline, lineIndex) => {
            return <tr key={lineIndex}>
                {aline.map((aColumn, columnIndex) => {
                    return <td className={CssTools.of("pixel").if(aColumn.on, "pixel-on").css()} key={`${lineIndex}-${columnIndex}`}></td>
                })}
            </tr>
        })}
    </table>
}

export default MiniSpriteRenderer;