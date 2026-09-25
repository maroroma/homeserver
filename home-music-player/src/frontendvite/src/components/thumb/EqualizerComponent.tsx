import type { FC } from "react";

import "./EqualizerComponent.css";

const EqualizerComponent: FC = () => {
    return <button className="equalizer">
        <span className="eq1"></span>
        <span className="eq2"></span>
        <span className="eq3"></span>
        <span className="eq4"></span>
        <span className="eq5"></span>
    </button>
}

export default EqualizerComponent;
