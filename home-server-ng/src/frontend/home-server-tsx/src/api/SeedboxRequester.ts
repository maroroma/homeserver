import TodoFile from "../components/seedbox/TodoFile";
import FileDescriptor from "../model/filemanager/FileDescriptor";
import FileDirectoryDescriptor from "../model/filemanager/FileDirectoryDescriptor";
import MoveRequest from "../model/seedbox/MoveRequest";
import NewTorrents from "../model/seedbox/NewTorrents";
import RunningTorrent from "../model/seedbox/RunningTorrent";
import TargetDirectory from "../model/seedbox/TargetDirectory";
import {RequesterUtils} from "./RequesterUtils";

export default class SeedboxRequester {
    static getRunningTorrents(): Promise<RunningTorrent[]> {
        return RequesterUtils.get("/api/seedbox/torrents");
    }

    static addTorrent(magnetLinks: NewTorrents): Promise<any> {
        return RequesterUtils.post("/api/seedbox/torrents", magnetLinks);
    }

    static deleteTorrent(torrent: RunningTorrent): Promise<any> {
        return RequesterUtils.delete(`/api/seedbox/torrents/${torrent.id}`);
    }

    static getTodoList(): Promise<TodoFile[]> {
        return RequesterUtils.get("/api/seedbox/todo/completedtorrents")
    }

    static getTargetDirectories(): Promise<TargetDirectory[]> {
        return RequesterUtils.get("/api/seedbox/todo/targets")
    }

    static getSubTargetDirectories(directoryToScan: FileDescriptor): Promise<FileDirectoryDescriptor> {
        return RequesterUtils.get(`/api/seedbox/todo/targets/${directoryToScan.id}/files`)
    }

    static moveFiles(moveRequest: MoveRequest): Promise<any> {
        return RequesterUtils.post("/api/seedbox/todo/sortedfile", moveRequest);
    }
}