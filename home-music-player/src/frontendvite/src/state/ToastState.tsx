export type ToastType = "Info" | "Warning" | "Error" | "InProgress";

export class ToastMessage {
  constructor(
    public message: string,
    public type: ToastType,
    public title: string
  ) {}

  public static info(title: string, message: string): ToastMessage {
    return new ToastMessage(message, "Info", title);
  }

  public static warning(title: string, message: string): ToastMessage {
    return new ToastMessage(message, "Warning", title);
  }
}

export type ToastState = {
  toastMessage?: ToastMessage;
  autoHideToastMessage?: ToastMessage;
};
