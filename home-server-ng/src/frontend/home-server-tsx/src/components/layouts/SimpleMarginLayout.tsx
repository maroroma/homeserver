import {FC} from "react";
import {CustomClassNames} from "../bootstrap/CssTools";


export type SimpleMarginLayoutProps = {
    children: any
}


const SimpleMarginLayout: FC<SimpleMarginLayoutProps> = ({ children }) => {
    return <div className={CustomClassNames.LayoutWithMargin}>
        {children}
    </div>
}

export default SimpleMarginLayout;