import React from 'react';
import iotSubEventReactor from '../iotSubEventReactor';
import { when } from '../../../tools/when';

export default function IotComponentRenderer({ iotComponent }) {


    const iconsMapper = {
        BUZZER: {
            icon: "record_voice_over",
            title: "Cliquer pour envoyer le buzz"
        },
        TRIGGER: {
            icon: "call_merge",
            title: "Cliquer pour controller le status"
        }
    }


    return <div className="masonry-item center-align">

        <a href="#!"
            className={when(!iotComponent.available).thenDisableElement("btn-floating waves-effect waves-light blue")}
            onClick={() => iotSubEventReactor().sendActionRequest(iotComponent)}
            title={iconsMapper[iotComponent.componentDescriptor.componentType].title}
        >
            <i className="material-icons large">{iconsMapper[iotComponent.componentDescriptor.componentType].icon}</i>
        </a>
        <div>
            {iotComponent.componentDescriptor.name}
        </div>

    </div>





}