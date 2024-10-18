import {FC, useEffect, useState} from "react";
import {Container, Form, Nav, Navbar} from "react-bootstrap";
import {ArrowLeftRight, Bell, Book, BookmarkPlus, Boxes, BoxSeam, FolderSymlink, InfoCircle, ListUl, Tools, UiRadios} from "react-bootstrap-icons";
import SimpleMenuButton from "./SimpleMenuButton";
import BootstrapBreakPoints from "../bootstrap/BootstrapBreakPoints";
import DropDownMenuButton, {DropDownButton} from "./DropDownMenuButton";
import "./MenuComponent.css";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import UpdateSearchStringAction from "../../context/actions/UpdateSearchStringAction";
import HomeServerRoutes from "../../HomeServerRoutes";
import {useLocation} from "react-router-dom";
import {BootstrapText} from "../bootstrap/BootstrapText";
import BrandRenderer from "./BrandRenderer";
import HomeServerRoute from "../../HomeServerRoute";

const MenuComponent: FC = () => {

    const { searchString, dispatch } = useHomeServerContext()

    const [menuExpanded, setMenuExpanded] = useState(false);

    const location = useLocation();

    const [currentLabeledRoute, setCurrentLabeledRoute] = useState(HomeServerRoute.default())

    useEffect(() => {
        setCurrentLabeledRoute(HomeServerRoutes.getLabeledRoute(location.pathname))
    }, [location])

    const booksMenuButtons =
        DropDownButton.unique("ajouter", <BookmarkPlus />, HomeServerRoutes.BOOKS_SERIE_SELECTION)



    const administrationMenuButtons = [
        DropDownButton.of("status", <InfoCircle />, HomeServerRoutes.ADMINISTRATION_STATUS),
        DropDownButton.of("tasks", <UiRadios />, HomeServerRoutes.ADMINISTRATION_TASKS),
        DropDownButton.of("events", <ListUl />, HomeServerRoutes.ADMINISTRATION_EVENTS)
    ]

    const seedBoxMenuButtons = DropDownButton.unique("todo", <ArrowLeftRight />, HomeServerRoutes.SEEDBOX_TODO)


    return <Navbar bg="dark" data-bs-theme="dark" sticky="top"
        expand={BootstrapBreakPoints.EXTRA_EXTRA_LARGE.value}
        expanded={menuExpanded}
        onToggle={(value) => setMenuExpanded(value)}>

        <Container>
            <Navbar.Brand className={BootstrapText.ColorPrimary}><BrandRenderer labeledRoute={currentLabeledRoute} /></Navbar.Brand>
            <Navbar.Toggle></Navbar.Toggle>
            <Navbar.Collapse>
                <Nav className="me-auto">
                    <DropDownMenuButton title="Administration" icon={<Tools />} path={HomeServerRoutes.ADMINISTRATION_PROPERTIES} dropDownButtons={administrationMenuButtons} onClick={() => setMenuExpanded(false)} />
                    <SimpleMenuButton icon={<Bell />} label="Buzzer" path={HomeServerRoutes.IOT_BUZZER} onClick={() => setMenuExpanded(false)}></SimpleMenuButton>
                    <SimpleMenuButton icon={<FolderSymlink />} label="Files" path={HomeServerRoutes.FILE_MANAGER} onClick={() => setMenuExpanded(false)}></SimpleMenuButton>
                    <SimpleMenuButton icon={<Boxes />} label="Lego" path={HomeServerRoutes.LEGO} onClick={() => setMenuExpanded(false)}></SimpleMenuButton>
                    <DropDownMenuButton title="Seedbox" icon={<BoxSeam />} path={HomeServerRoutes.SEEDBOX_TORRENTS} dropDownButtons={seedBoxMenuButtons} onClick={() => setMenuExpanded(false)} />
                    <DropDownMenuButton title="Books" icon={<Book />} path={HomeServerRoutes.BOOKS_ALL} dropDownButtons={booksMenuButtons} onClick={() => setMenuExpanded(false)} />
                </Nav>
                <Form className="d-flex" onSubmit={(event) => {
                    event.preventDefault();
                    event.stopPropagation();
                }}>
                    <Form.Control
                        type="search"
                        placeholder="Rechercher"
                        className="me-2"
                        aria-label="Search"
                        value={searchString}
                        onChange={(event) => dispatch(new UpdateSearchStringAction(event.target.value))}
                        onSubmit={(event) => {
                            event.preventDefault();
                            event.stopPropagation();
                        }}
                    />
                </Form>
            </Navbar.Collapse>
        </Container>
    </Navbar>
}

export default MenuComponent;