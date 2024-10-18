import {FC} from "react"
import HomeServerRoute from "../../HomeServerRoute"


export type BrandRendererProps = {
    labeledRoute: HomeServerRoute
}

const BrandRenderer : FC<BrandRendererProps> = ({labeledRoute}) => {

    return <>{labeledRoute.icon} {labeledRoute.labelForNavBar}</>

}

export default BrandRenderer;