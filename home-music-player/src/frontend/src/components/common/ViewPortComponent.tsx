import {FC} from "react";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ViewState} from "../../state/ViewState";


export type ViewPortComponentProps = {
    children: any,
    view: ViewState
}


/** conditionne l'affichage des children en fonction de view, récupéré du contexte */
const ViewPortComponent: FC<ViewPortComponentProps> = ({view, children}) => {

    const { viewState } = useMusicPlayerContext();


    if (viewState !== view) {
        return <></>;
    }

    return <>{ children }</>

}

export default ViewPortComponent;
