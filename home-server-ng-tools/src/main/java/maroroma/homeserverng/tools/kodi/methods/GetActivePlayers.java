package maroroma.homeserverng.tools.kodi.methods;

import maroroma.homeserverng.tools.kodi.model.GetActivePlayersResponse;

public class GetActivePlayers extends AbstractKodiMethodBuilder<Integer, GetActivePlayersResponse, GetActivePlayers>{

    // {"jsonrpc": "2.0", "method": "Player.GetActivePlayers", "id": 1}

    public static GetActivePlayers create() {
        return new GetActivePlayers().id(1);
    }


    /**
     * Constructeur.
     */
    protected GetActivePlayers() {
        super("Player.GetActivePlayers", GetActivePlayersResponse.class);
    }
}
