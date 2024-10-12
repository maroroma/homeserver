import {FC} from "react";
import Serie from "../../model/books/Serie";
import {Card, Image} from "react-bootstrap";
import SerieWithFullBooks from "../../model/books/SerieWithFullBooks";
import {BootstrapText} from "../bootstrap/BootstrapText";
import {BookFill, BookHalf} from "react-bootstrap-icons";
import CssTools, {CustomClassNames} from "../bootstrap/CssTools";

export type SerieCardHeaderProps = {
    serieToDisplay: Serie
    onClick?: () => void
}

const SerieCardHeader: FC<SerieCardHeaderProps> = ({ serieToDisplay, onClick }) => {
    return <Card.Header
        className={CssTools
            .of("full-serie-header")
            .then(BootstrapText.AlignLeft)
            .clickable(onClick !== undefined)
            .css()}
        onClick={() => {
            if (onClick) {
                onClick();
            }
        }}>
        <Image src={SerieWithFullBooks.seriePicture(serieToDisplay)} rounded></Image>
        <div className="full-serie-header-labels">
                <h1 className={BootstrapText.WordBreak}>
                    {serieToDisplay.title}
                </h1>
                <h3>
                    {(serieToDisplay.bookIds !== undefined && serieToDisplay.bookIds !== null) ? serieToDisplay.bookIds.length : 0} tomes
                </h3>
                <h4 className={serieToDisplay.completed ? BootstrapText.ColorSuccess : BootstrapText.ColorDanger}>
                    {serieToDisplay.completed ?
                        <><BookFill className={CustomClassNames.SpaceAfterIcon} />Complète</>
                        : <><BookHalf className={CustomClassNames.SpaceAfterIcon} />A Compléter</>}
                </h4>
        </div>
    </Card.Header>
}

export default SerieCardHeader;