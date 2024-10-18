import {FC} from "react"


export type IfComponentProps = {
    displayIf?: boolean,
    hideIf?: boolean,
    children: any
}

const IfComponent: FC<IfComponentProps> = ({ displayIf, children, hideIf }) => {

    if (hideIf !== undefined && displayIf !== undefined) {
        throw new Error("hideIF and displayIf can't be both defined")
    }

    if (hideIf === undefined && displayIf === undefined) {
        throw new Error("hideIF and displayIf can't be both undefined")
    }

    let display = false;

    if (hideIf !== undefined) {
        display = !hideIf;
    }

    if (displayIf !== undefined) {
        display = displayIf;
    }


    return <>
        {display ? children : <></>}
    </>

}

export default IfComponent