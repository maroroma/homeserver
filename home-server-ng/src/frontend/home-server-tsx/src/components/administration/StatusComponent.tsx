import {FC, useEffect, useState} from "react";
import {ListGroup, ProgressBar} from "react-bootstrap";
import {DeviceHdd, HouseGear, PcDisplay} from "react-bootstrap-icons";
import ServerStatus from "../../model/administration/ServerStatus";
import {AdministrationRequester} from "../../api/AdministrationRequester";
import Formatters from "../Formatters";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import {BootstrapText} from "../bootstrap/BootstrapText";
import {CustomClassNames} from "../bootstrap/CssTools";


const StatusComponent: FC = () => {

    const [serverStatus, setServerStatus] = useState(ServerStatus.empty())

    const {dispatch} = useHomeServerContext();

    useEffect(() => {

        AdministrationRequester.getServerStatus()
            .then(response => setServerStatus(response))
            .catch(response => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la récupération des status du server")));


    }, []);


    return <div>
        <ListGroup horizontal data-bs-theme="light" className={BootstrapText.AlignLeft}>
            <ListGroup.Item className={CustomClassNames.VerticallyCentered}><PcDisplay size="50"></PcDisplay></ListGroup.Item>
            <ListGroup.Item className={CustomClassNames.FullWidth}>
                <p>{serverStatus.hostName}</p>
                <p>{serverStatus.ipAddress}</p>
                <p>{serverStatus.operatingSystem}</p>
            </ListGroup.Item>
        </ListGroup>
        <ListGroup horizontal data-bs-theme="light" className={BootstrapText.AlignLeft}>
            <ListGroup.Item className={CustomClassNames.VerticallyCentered}><HouseGear size="50"></HouseGear></ListGroup.Item>
            <ListGroup.Item className={CustomClassNames.FullWidth}>
                <p>{serverStatus.version}</p>
                <p className="text-break-anywhere">{serverStatus.startUpTime}</p>
            </ListGroup.Item>
        </ListGroup>
        <ListGroup horizontal data-bs-theme="light" className={BootstrapText.AlignLeft}>
            <ListGroup.Item className={CustomClassNames.VerticallyCentered}><DeviceHdd size="50"></DeviceHdd></ListGroup.Item>
            <ListGroup.Item className={CustomClassNames.FullWidth}>
                {serverStatus.drives.map(aDrive => <>
                    <p>{aDrive.name}</p>
                    <ProgressBar
                        now={aDrive.percentageUsed}
                        label={`${Formatters.octets(aDrive.usedSpace)} / ${Formatters.octets(aDrive.totalSpace)}`}
                        variant=""
                    />
                </>)}
            </ListGroup.Item>
        </ListGroup>
    </div>
}

export default StatusComponent;