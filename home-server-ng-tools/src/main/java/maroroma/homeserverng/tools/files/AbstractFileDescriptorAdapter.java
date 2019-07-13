package maroroma.homeserverng.tools.files;

import lombok.Getter;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.security.SecurityManager;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

/**
 * Classe intermédiaire permettant de récupérer les informations des fichiers ou de les manipuler
 * pour les offrir à un {@link FileDescriptor}
 */
public abstract class AbstractFileDescriptorAdapter {

    /**
     * Gestionnaire de sécurité
     */
    @Getter
    private final SecurityManager securityManager;

    /**
     * Constructeur
     * @param securityManager .
     */
    protected AbstractFileDescriptorAdapter(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Liste l'ensemble des {@link AbstractFileDescriptorAdapter} de ce repertoire
     * @return
     */
    protected abstract Stream<AbstractFileDescriptorAdapter> listAllFileAdapters();

    /**
     * Suppression non recursive
     * @return
     */
    protected abstract boolean simpleDelete();

    /**
     * Déplacement par renommage
     * @param target -
     * @return -
     */
    protected abstract boolean moveByRename(FileDescriptor target);


    /**
     * Retourne le nom du fichier
     * @return
     */
    public abstract String getName();

    /**
     * Retourne le chemin complet du fichier
     * @return -
     */
    public abstract String getFullName();

    /**
     * Retourne le chemin du répertoire parent
     * @return -
     */
    public abstract String getParent();

    /**
     * Retourne la taille du fichier
     * @return
     */
    public abstract long size();

    /**
     * Détermine si le fichier existe
     * @return -
     */
    public abstract boolean exists();

    /**
     * Détermine si le fichier est un fichier ^^
     * @return -
     */
    public abstract boolean isFile();

    /**
     * Détermine si le fichier est un répertoire
     * @return-
     */
    public abstract boolean isDirectory();

    /**
     * Création du répertoire
     * @return -
     */
    public abstract boolean mkdir();

    /**
     * Création du chemin complet du répertoire et du répertoire
     * @return -
     */
    public abstract boolean mkdirs();

    /**
     * REnommage du fichier
     * @param newName -
     * @return -
     */
    public abstract boolean rename(final String newName);

    public abstract OutputStream getOutputStream();
    public abstract InputStream getInputStream();

    /**
     * Retourne la source technique du fichier (ie implémentation locale ou samba)
     * @return -
     */
    protected String getTechnicalSource() {
        return this.getClass().getName();
    }

    /**
     * Détermine si ce fichier et l'autre ont la même source technique
     * @param fileDescriptor -
     * @return -
     */
    protected boolean hasSameTechnicalSource(final FileDescriptor fileDescriptor) {
        return this.getTechnicalSource().equals(fileDescriptor.getAdapter().getTechnicalSource());
    }

    /**
     * Copie ce fichier vers la cible donnée
     * @param target -
     */
    protected void copyTo(FileDescriptor target) {
        this.copyTo(target.getOutputStream());
    }

    /**
     * Copie ce fichier vers la cible donnée
     * @param outputStream -
     */
    protected void copyTo(OutputStream outputStream) {
        Traper.trap(() -> FileCopyUtils.copy(this.getInputStream(), outputStream));
    }

    /**
     * Déplacement de fichier par recopie de stream (cas de deux source techniques différentes)
     * @param target -
     * @return -
     */
    protected boolean moveByStreamCopy(FileDescriptor target) {
        this.copyTo(target);
        return this.simpleDelete();
    }

    /**
     * Suppression. Récursive si repertoire
     * @return
     */
    public boolean delete() {
        if (this.isDirectory()) {
            return this.listAllFileAdapters()
                    .map(AbstractFileDescriptorAdapter::delete)
                    .allMatch(Boolean.TRUE::equals)
                    && this.simpleDelete();
        } else {
            return this.simpleDelete();
        }
    }

    /**
     * Déplacement du fichier
     * @param target
     * @return
     */
    public boolean moveTo(final FileDescriptor target) {
        // si la cible a le meme support physique, on renomme
        if (this.hasSameTechnicalSource(target)) {
            return this.moveByRename(target);
        } else {
            // sinon on réalise une copie
            return this.moveByStreamCopy(target);
        }
    }


    /**
     * Retourne l'identifiant unique du fichier
     * @return
     */
    public String getId() {
        return Base64Utils.encodeToString(this.getFullName().getBytes());
    }

    /**
     * Liste l'ensemble des {@link FileDescriptor} sous jacents
     * @return
     */
    public Stream<FileDescriptor> listAllFileDescriptors() {
        return this.listAllFileAdapters().map(FileDescriptor::new);
    }



}
