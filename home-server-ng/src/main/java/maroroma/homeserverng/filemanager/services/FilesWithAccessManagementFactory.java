package maroroma.homeserverng.filemanager.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.filemanager.model.RootDirectoryConfiguration;
import maroroma.homeserverng.filemanager.model.RootDirectoryConfigurationCreationRequest;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.config.PropertyValueResolver;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.files.AbstractFileDescriptorAdapter;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.files.LocalFileDescriptorAdapter;
import maroroma.homeserverng.tools.files.SmbFileDescriptorAdapter;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.util.*;
import java.util.function.*;

/**
 * Effort de centralisation pour la gestion de la production et de la sécurisation
 * des files descriptors
 */
@Service
@RequiredArgsConstructor
public class FilesWithAccessManagementFactory implements FileManagerConfigurationService, FilesFactory {

    @Property("homeserver.administrations.sambaUser")
    HomeServerPropertyHolder sambaUser;

    @InjectNanoRepository(
            file = @Property("homeserver.filemanager.specific.configuration"),
            persistedType = RootDirectoryConfiguration.class
    )
    private NanoRepository specificConfigurationRepository;

    private final PropertyValueResolver propertyValueResolver;


    /**
     * Renvoie les root en tant que fichier
     * <strong>attention, masque les dossiers flaggués comme hidden</strong>
     *
     * @return
     */
    @Override
    public List<FileDirectoryDescriptor> getRootDirectories() {
        return this.resolveRootDirectoriesConfiguration()
                .stream()
                .map(this::configToDirectory)
                .filter(Predicate.not(FileDirectoryDescriptor::isHidden))
                .toList();
    }

    @Override
    public List<RootDirectoryConfiguration> addRootDirectoryConfiguration(RootDirectoryConfigurationCreationRequest rootDirectoryConfigurationCreationRequest) {
        this.specificConfigurationRepository.save(RootDirectoryConfiguration.builder()
                .id(UUID.randomUUID().toString())
                .rawPath(rootDirectoryConfigurationCreationRequest.getRawPath())
                .isHidden(false)
                .isProtected(false)
                .build());
        return getRawRootDirectoriesConfiguration();
    }

    @Override
    public List<RootDirectoryConfiguration> deleteRootDirectoryConfiguration(String id) {
        this.specificConfigurationRepository.delete(id);
        return getRawRootDirectoriesConfiguration();
    }

    @Override
    public List<RootDirectoryConfiguration> updateRootDirectoryConfiguration(String id, RootDirectoryConfiguration rootDirectoryConfiguration) {
        this.specificConfigurationRepository.findByIdMandatory(id);
        this.specificConfigurationRepository.update(rootDirectoryConfiguration);
        return this.getRawRootDirectoriesConfiguration();
    }


    /**
     * Point d'entrée unique pour la récupération des configurations, qui doivent être retransformées pour récupérer
     * Les paths résolus
     *
     * @return
     */
    @Override
    public List<RootDirectoryConfiguration> resolveRootDirectoriesConfiguration() {
        return this.specificConfigurationRepository.<RootDirectoryConfiguration>getAll()
                .stream()
                .map(aConfiguration -> aConfiguration.applyResolver(this.propertyValueResolver))
                .toList();
    }

    @Override
    public List<RootDirectoryConfiguration> getRawRootDirectoriesConfiguration() {
        return this.specificConfigurationRepository.getAll();
    }


    @Override
    public FileDescriptor fileFromPath(String path) {
        var fileDescriptor = new FileDescriptor(resolverAdapter(path));

        return this.applyAccessManagement(fileDescriptor);
    }

    @Override
    public FileDirectoryDescriptor directoryFromPath(String path, DirectoryParsingOptions... directoryParsingOptions) {
        var directoryDescriptor = new FileDirectoryDescriptor(resolverAdapter(path),
                DirectoryParsingOptions.shouldParseFiles(directoryParsingOptions),
                DirectoryParsingOptions.shouldParseDirectories(directoryParsingOptions));


        return this.applyAccessManagement(directoryDescriptor);
    }

    private FileDirectoryDescriptor configToDirectory(RootDirectoryConfiguration rootDirectoryConfiguration) {
        var directoryDescriptor = new FileDirectoryDescriptor(resolverAdapter(rootDirectoryConfiguration.getRawPath()), false, false);

        directoryDescriptor.setProtected(rootDirectoryConfiguration.isProtected());
        directoryDescriptor.setHidden(rootDirectoryConfiguration.isHidden());

        return directoryDescriptor;

    }

    private AbstractFileDescriptorAdapter resolverAdapter(String path) {
        if (path.startsWith("smb://")) {
            return new SmbFileDescriptorAdapter(path, () -> sambaUser.asUser());
        } else {
            return new LocalFileDescriptorAdapter(path);
        }
    }

    private <T extends FileDescriptor> T applyAccessManagement(T fileDescriptor) {


        var matchingRootDirectory =
                getRootDirectoryForFile(fileDescriptor)
                        .orElseThrow(() -> new RuntimeHomeServerException("file " + fileDescriptor.getFullName() + " is not managed"));

        fileDescriptor.setProtected(matchingRootDirectory.isProtected());
        fileDescriptor.setHidden(matchingRootDirectory.isHidden());

        return fileDescriptor;
    }


    private Optional<RootDirectoryConfiguration> getRootDirectoryForFile(FileDescriptor fileDescriptor) {
        return this.resolveRootDirectoriesConfiguration()
                .stream()
                .filter(aRootConfiguration -> resolverAdapter(aRootConfiguration.getRawPath())
                        .toFileDescriptorPath()
                        .isParentOf(fileDescriptor.toFileDescriptorPath()))
                .min((config1, config2) -> Comparators.comparable().compare(
                        config1.getRawPath().length(),
                        config2.getRawPath().length()));

    }
}
