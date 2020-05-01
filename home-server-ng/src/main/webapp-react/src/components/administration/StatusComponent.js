import React from 'react';
import { useEffect, useState } from 'react';
import { administrationApi } from '../../apiManagement/AdministrationApi';
import { ProgressBarComponent, range } from '../commons/ProgressBarComponent';
import formatters from '../commons/formatters';

export default function StatusComponent() {


    const [status, setStatus] = useState({});

    useEffect(() => {
        administrationApi().getServerStatus().then(setStatus)
    }, []);




    return (<ul className="collection">
        <li className="collection-item avatar">
            <i className="material-icons circle blue">computer</i>
            <p className="title">{status.hostName}</p>
            <p className="title">{status.ipAddress}</p>
            <p className="title">{status.operatingSystem}</p>
        </li>
        <li className="collection-item avatar">
            <i className="material-icons circle blue">home</i>
            <p className="title">{status.version}</p>
            <p className="title">{status.startUpTime}</p>
        </li>
        <li className="collection-item avatar">
            <i className="material-icons circle blue">data_usage</i>
            {status.drives?.map(oneDrive => {
                return <div>
                    <p className="title">{oneDrive.name}</p>
                    <ProgressBarComponent driver={{
                        currentValue: oneDrive.percentageUsed,
                        ranges: [
                            range(0, 70, "green"),
                            range(71, 80, "orange"),
                            range(81, 100, "red")
                        ],
                        labels: {
                            current: formatters().readableOctets(oneDrive.usedSpace),
                            max: formatters().readableOctets(oneDrive.totalSpace),
                        }
                    }}></ProgressBarComponent>
                </div>
            })}

        </li>
    </ul>);
}