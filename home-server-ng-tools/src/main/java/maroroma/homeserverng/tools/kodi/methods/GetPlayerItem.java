package maroroma.homeserverng.tools.kodi.methods;

import maroroma.homeserverng.tools.kodi.model.GetPlayerItemResponse;
import maroroma.homeserverng.tools.kodi.model.GetPlayerPropertiesResponse;

import java.util.List;

public class GetPlayerItem extends AbstractKodiMethodBuilder<String, GetPlayerItemResponse, GetPlayerItem>{

    private Integer playerId;

    public static GetPlayerItem create() {
        return new GetPlayerItem();
    }

    protected GetPlayerItem() {
        super("Player.GetItem", GetPlayerItemResponse.class);
    }

    public GetPlayerItem playerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    @Override
    protected void prepareDatasForBuild() {
        super.prepareDatasForBuild();
        this.params.putIfAbsent("playerid", this.playerId);
    }
}
