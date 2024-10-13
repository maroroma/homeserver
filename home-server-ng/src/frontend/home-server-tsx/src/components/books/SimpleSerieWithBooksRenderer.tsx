import {FC} from "react";
import {Card} from "react-bootstrap";
import SerieWithFullBooks from "../../model/books/SerieWithFullBooks";
import Serie from "../../model/books/Serie";
import {BookFill, BookHalf} from "react-bootstrap-icons";
import {BootstrapText} from "../bootstrap/BootstrapText";
import {useNavigate} from "react-router-dom";
import {CustomClassNames} from "../bootstrap/CssTools";
import HomeServerRoutes from "../../HomeServerRoutes";


export type SimpleSerieWithBooksRendererProps = {
    aSerieWithItsBooks: Serie
}

const SimpleSerieWithBooksRenderer: FC<SimpleSerieWithBooksRendererProps> = ({ aSerieWithItsBooks }) => {

    const navigate = useNavigate();


    return <Card className="serie" onClick={() => navigate(HomeServerRoutes.BOOKS_SERIE_DETAIL_ID(aSerieWithItsBooks.id))}>
        <Card.Img variant="top" loading="lazy" src={SerieWithFullBooks.seriePicture(aSerieWithItsBooks)} className="serie-picture" />
        <Card.Body>
            <Card.Title className={`${BootstrapText.AlignLeft} serie-title`}>
                {aSerieWithItsBooks.completed ? <BookFill className={CustomClassNames.SpaceAfterIcon}/> : <BookHalf  className={CustomClassNames.SpaceAfterIcon}/>}
                <span>{aSerieWithItsBooks.title}</span>
            </Card.Title>
        </Card.Body>

    </Card>
}

export default SimpleSerieWithBooksRenderer;