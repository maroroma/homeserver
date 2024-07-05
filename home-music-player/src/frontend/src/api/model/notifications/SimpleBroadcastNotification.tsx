export type SimpleBroadcastNotification = {
    message: string,
    level: "INFO" | "ERROR",
    persistent: boolean
}