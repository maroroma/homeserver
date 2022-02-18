package maroroma.homeserverng.tools.kodi.methods;

public class StopPlayer extends AbstractKodiMethodBuilder<String, String, StopPlayer>{

    private Integer playerId;

    public static StopPlayer create() {
        return new StopPlayer();
    }

    protected StopPlayer() {
        super("Player.Stop", String.class);
    }

    public StopPlayer playerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    public StopPlayer playerId(String playerId) {
        return this.playerId(Integer.parseInt(playerId));
    }

    @Override
    protected void prepareDatasForBuild() {
        super.prepareDatasForBuild();
        this.params.putIfAbsent("playerid", this.playerId);
    }
}
