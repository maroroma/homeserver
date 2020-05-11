import React from 'react';
import IconComponent from '../commons/IconComponent';
import { when } from "../../tools/when";
import { ProgressBarComponent, range } from '../commons/ProgressBarComponent';
import eventReactor from '../../eventReactor/EventReactor';
import './TorrentRendererComponent.scss';
import formatters from '../commons/formatters';

export default function TorrentRendererComponent({ torrent }) {

    const iconeToDisplay = torrent.completed ? "done" : "file_download"

    return <li className={
        when()
            .selected(torrent)
            .thenDefaultSelectColor("collection-item waves-effect waves-teal torrent-renderer-item")}
        onClick={() => eventReactor().shortcuts().selectItem(torrent.id, !torrent.selected)}>
        <div>
            <IconComponent icon={iconeToDisplay} classAddons="left"></IconComponent>
            <span className="truncate">
                {torrent.name}
            </span>

            <ProgressBarComponent driver={
                {
                    colorScheme: "blue",
                    currentValue: torrent.percentDone,
                    ranges: [
                        range(0, 1, "orange"),
                        range(2, 99, "blue"),
                        range(100, 101, "green")],
                    labels: {
                        current: formatters().readableOctets(torrent.done),
                        max: formatters().readableOctets(torrent.total)
                    }
                }
            }></ProgressBarComponent>
        </div>
    </li >;
}