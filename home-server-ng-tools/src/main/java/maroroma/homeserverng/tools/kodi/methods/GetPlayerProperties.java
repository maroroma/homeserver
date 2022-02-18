package maroroma.homeserverng.tools.kodi.methods;

import maroroma.homeserverng.tools.kodi.model.GetPlayerPropertiesResponse;

import java.util.List;

public class GetPlayerProperties extends AbstractKodiMethodBuilder<String, GetPlayerPropertiesResponse, GetPlayerProperties>{

    private Integer playerId;

    public static GetPlayerProperties create() {
        return new GetPlayerProperties();
    }

    protected GetPlayerProperties() {
        super("Player.GetProperties", GetPlayerPropertiesResponse.class);
    }

    public GetPlayerProperties playerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    @Override
    protected void prepareDatasForBuild() {
        super.prepareDatasForBuild();
        this.params.putIfAbsent("properties",
                List.of("percentage",
                        "totaltime",
                        "time")
        );
        this.params.putIfAbsent("playerid", this.playerId);
    }
}
