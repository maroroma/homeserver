import {FC} from "react";
import {useMusicPlayerContext} from "../state/MusicPlayerContext";
import {Button} from "react-bootstrap";
import {UpdateHelloWorldAction} from "../state/actions/UpdateHelloWorldAction";


const TestComponent: FC = () => {

    const {helloWorld, dispatch} = useMusicPlayerContext();


    const testCall = () => {
        // LibraryRequester.getAllArtists().then(artists => console.log(artists));
    }

    return <div>{helloWorld}
    <Button onClick={() => dispatch(new UpdateHelloWorldAction("nouvelle chaine"))}>CLICK</Button>
    <Button onClick={() => testCall()}>test api</Button>
    </div>
}


export default TestComponent;