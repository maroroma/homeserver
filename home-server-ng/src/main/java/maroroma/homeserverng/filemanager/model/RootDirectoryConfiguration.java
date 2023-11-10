package maroroma.homeserverng.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.config.PropertyValueResolver;

/**
 * Décrit un répertoire racine pour la gestion des fichiers, avec des notions de sécurisation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RootDirectoryConfiguration {
    private String id;
    private String rawPath;

    private boolean isProtected;

    private boolean isHidden;

    public RootDirectoryConfiguration applyResolver(PropertyValueResolver propertyValueResolver) {
        this.rawPath = propertyValueResolver.resolve(this.rawPath);
        return this;
    }
}
