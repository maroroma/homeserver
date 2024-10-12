import Brick from "../model/lego/Brick";
import {RequesterUtils} from "./RequesterUtils";

export default class LegoRequester {
    static getAllBricks(): Promise<Brick[]> {
        return RequesterUtils.get("/api/lego/bricks");
    }

    static updateBrick(brick: Brick): Promise<Brick[]> {
        return RequesterUtils.put("/api/lego/bricks", [brick]);
    }

    static createBrick(brick: Brick): Promise<Brick[]> {
        return RequesterUtils.post("/api/lego/bricks", brick);
    }

    static deleteBrick(brick: Brick): Promise<Brick[]> {
        return RequesterUtils.delete(`/api/lego/bricks/${brick.id}`)
    }
}