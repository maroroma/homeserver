import BuzzRequest from "../model/iot/BuzzRequest";
import MiniSprite from "../model/iot/MiniSprite";
import {RequesterUtils} from "./RequesterUtils";

export class IotRequester {
    static getAllSprites(): Promise<MiniSprite[]> {
        return RequesterUtils.get("/api/iot/minisprites")
    }

    static sendBuzz(ledTemplate: string): Promise<any> {
        return RequesterUtils.post<any>("/api/iot/components/buzzers", BuzzRequest.autoFirst(ledTemplate))
    }

    static updateSprite(sprite: MiniSprite): Promise<MiniSprite[]> {
        return RequesterUtils.put("/api/iot/minisprites", sprite);
    }

    static createSprite(sprite: MiniSprite): Promise<MiniSprite[]> {
        return RequesterUtils.post("/api/iot/minisprites", sprite);
    }

    static deleteSprite(sprite: MiniSprite): Promise<MiniSprite[]> {
        return RequesterUtils.delete(`/api/iot/minisprites/${sprite.name}`);
    }
}