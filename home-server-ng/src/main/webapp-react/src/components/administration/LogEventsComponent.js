import React, { useEffect, useState } from 'react';
import { administrationApi } from '../../apiManagement/AdministrationApi';
import eventReactor from '../../eventReactor/EventReactor';
import { useDisplayList } from '../../tools/displayList';
import DataGridComponent from '../commons/DataGridComponent';

export default function LogEventComponent() {

    const [allLogs, setAllLogs] = useDisplayList();
    const [repoId, setRepoId] = useState("");

    useEffect(() => {
        administrationApi().getAllLogEvents().then(response => {
            setRepoId(response.repoId);
            setAllLogs({ ...allLogs.update(response.persistantNotifications) })
        });
    }, []);

    useEffect(() => {
        return eventReactor().shortcuts().onDataGridDeleteAll(() => administrationApi()
            .deleteRepo(repoId)
            .then(() => administrationApi().getAllLogEvents())
            .then(response => {
                setRepoId(response.repoId);
                setAllLogs({ ...allLogs.update(response.persistantNotifications) })
            })
        );
    }, [repoId]);



    const dataGridConfiguration = {
        itemUniqueId: 'id',
        displaySaveButton: false,
        displayDeleteAllButton: true,
        // onSaveHandler: saveNewModuleStatus,
        columns: [
            {
                header: 'Date',
                dataField: 'creationDate',
                defaultSort: true
            },
            {
                header: 'Titre',
                dataField: 'title'
            },
            {
                header: 'Message',
                dataField: 'message',
                hideOnSmallDevice: true
            }
        ]
    };

    return <div>
        <DataGridComponent configuration={dataGridConfiguration} data={allLogs.displayList}></DataGridComponent>
    </div>
}