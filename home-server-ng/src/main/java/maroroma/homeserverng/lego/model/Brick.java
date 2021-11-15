package maroroma.homeserverng.lego.model;

import lombok.Data;

/**
 * Représente une brique lego
 */
@Data
public class Brick {
    private String id;
    private String name;
    private int drawerNumber;
    /**
     * Url de l'image originale de la brique. Permet notamment de la télécharger en local
     */
    private String pictureUrl;
    /**
     * Identifiant du fichier image représentant la brique
     */
    private String pictureFileId;
}
