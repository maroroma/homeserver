import React from 'react';
import iotSubEventReactor from '../iotSubEventReactor';
import { when } from '../../../tools/when';
import {iotResolver} from '../IotRendererResolver';

export default function IotComponentRenderer({ iotComponent }) {





    return <div className="masonry-item center-align">

        <a href="#!"
            className={when(!iotComponent.available).thenDisableElement("btn-floating waves-effect waves-light blue")}
            onClick={() => iotSubEventReactor().sendActionRequest(iotComponent)}
            title={iotResolver().resolveRender(iotComponent.componentDescriptor.componentType).title}
        >
            <i className="material-icons large">{iotResolver().resolveRender(iotComponent.componentDescriptor.componentType).icon}</i>
        </a>
        <div>
            {iotComponent.componentDescriptor.name}
        </div>

    </div>





}