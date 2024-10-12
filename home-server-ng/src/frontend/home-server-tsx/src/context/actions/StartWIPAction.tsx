import {BootstrapVariants} from "../../components/bootstrap/BootstrapVariants";
import {HomeServerRootState} from "../states/HomeServerRootState";
import {HomeServerAction} from "./HomeServerAction";
import ToastAction from "./ToastAction";

export default class StartWIPAction implements HomeServerAction {
    constructor(private toastMessage: string = "", private toastVariant: BootstrapVariants = BootstrapVariants.Primary) { }
    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        var toastedState = new ToastAction(this.toastMessage, this.toastVariant)
            .applyToState(previousState);

        return {
            ...toastedState,
            workInProgress: true,
        }
    }

}