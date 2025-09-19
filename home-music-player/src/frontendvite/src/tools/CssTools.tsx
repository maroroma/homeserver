/* eslint-disable @typescript-eslint/no-explicit-any */

export default class CssTools {
  static of(css?: string): CssTools {
    return new CssTools(css ? css : "");
  }

  // static smallPlayerButton(playerSubState: PlayerSubState): string {
  //     return CssTools
  //         .of("small-player-button")
  //         .disableOnLoading(playerSubState, "clickable")
  //         .css();
  // }

  // static fullScreenPlayerButton(playerSubState: PlayerSubState): string {
  //     return CssTools
  //         .of("fullscreen-player-button")
  //         .disableOnLoading(playerSubState, "clickable")
  //         .css();
  // }

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

  notDefined(object: any, className: string): CssTools {
    return this.if(object === undefined, className);
  }

  if(condition: boolean, className: string): CssTools {
    if (condition === true) {
      return this.then(className);
    } else {
      return this;
    }
  }

  ifElse(
    condition: boolean,
    classNameIf: string,
    classNameElse: string
  ): CssTools {
    if (condition === true) {
      return this.then(classNameIf);
    } else {
      return this.then(classNameElse);
    }
  }

  clickable(condition?: boolean): CssTools {
    if (condition !== undefined) {
      return this.if(condition, "clickable");
    }
    return this.then("clickable");
  }

  // disableOnLoading(playerSubState: PlayerSubState, notDisableCss?: string) {
  //     return this.ifElse(playerSubState.isLoading, "disable", notDisableCss ? notDisableCss : "");
  // }
}
