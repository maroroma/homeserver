import {BootstrapText} from "./BootstrapText";

export enum CustomClassNames {
    FullWidth = "full-width",
    Clickable = "clickable",
    VerticallyCentered = "vertically-centered",
    PullRight = "pull-right",
    SpaceAfterIcon = "space-after-icon",
    LayoutWithMargin = "layout-with-margin",
    Ellipsis = "ellipsis"
}

export default class CssTools {
    static of(css?: string): CssTools {
        return new CssTools(css ? css : "");
    }

    constructor(private className: string) {
        this.css = this.css.bind(this);
        this.then = this.then.bind(this);
        this.defined = this.defined.bind(this);
    }

    css(): string {
        return this.className;
    }

    then(className: string): CssTools {
        return new CssTools(`${this.className} ${className}`);
    }

    defined(object: any, className: string): CssTools {
        return this.if(object !== undefined, className);
    }

    if(condition: boolean, className: string): CssTools {
        if (condition === true) {
            return this.then(className);
        } else {
            return this;
        }
    }

    ifElse(condition: boolean, classNameIf: string, classNameElse: string): CssTools {
        if (condition === true) {
            return this.then(classNameIf);
        } else {
            return this.then(classNameElse);
        }
    }

    clickable(condition?: boolean): CssTools {
        if (condition !== undefined) {
            return this.if(condition, CustomClassNames.Clickable)
        }
        return this.then(CustomClassNames.Clickable);
    }

    verticallyCentered(): CssTools {
        return this.then(CustomClassNames.VerticallyCentered);
    }

    uppercase(): CssTools {
        return this.then(BootstrapText.UpperCase);
    }

}