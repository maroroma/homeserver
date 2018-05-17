package maroroma.homeserverng.tools.kodi.methods;


import maroroma.homeserverng.tools.kodi.model.GetSourcesResponse;

/**
 * Méthode Kodi permettant de lister les sources videos d'une instance kodi.
 */
public class GetSources extends AbstractKodiMethodBuilder<Integer, GetSourcesResponse, GetSources> {

    /**
     * Nom de la méthode.
     */
    private static final String KODI_METHOD_NAME_GetSources = "Files.GetSources";

    /**
     * Constructeur.
     */
    private GetSources() {
        super(KODI_METHOD_NAME_GetSources, GetSourcesResponse.class);
    }

    /**
     * Retourne une nouvelle instance de {@link GetSources}
     * @return -
     */
    public static GetSources create() {
        return new GetSources().id(1);
    }

    @Override
    protected void prepareDatasForBuild() {
        super.prepareDatasForBuild();
        this.params.putIfAbsent("media", "video");
    }
}
