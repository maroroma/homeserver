import {FC} from "react"
import HomeServerRoute from "../../HomeServerRoute"


export type BrandRendererProps = {
    labeledRoute: HomeServerRoute
}

// jack o lanter &#127875;
// pere noel &#127877;
// oeuf &#129370;
// poule &#128020;
// anniversaire &#127874;
// tête de mort &#128128;
// lapin &#128007;
// cadeau &#127873;
// sapin de noel : &#127876;





const BrandRenderer : FC<BrandRendererProps> = ({labeledRoute}) => {

    return <>{labeledRoute.icon} {labeledRoute.labelForNavBar}</>

}

export default BrandRenderer;