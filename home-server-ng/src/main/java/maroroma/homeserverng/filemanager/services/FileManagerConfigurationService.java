package maroroma.homeserverng.filemanager.services;

import maroroma.homeserverng.filemanager.model.RootDirectoryConfiguration;
import maroroma.homeserverng.filemanager.model.RootDirectoryConfigurationCreationRequest;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;

import java.util.*;

public interface FileManagerConfigurationService {
    List<FileDirectoryDescriptor> getRootDirectories();

    List<RootDirectoryConfiguration> addRootDirectoryConfiguration(RootDirectoryConfigurationCreationRequest rootDirectoryConfigurationCreationRequest);

    List<RootDirectoryConfiguration> deleteRootDirectoryConfiguration(String id);

    List<RootDirectoryConfiguration> updateRootDirectoryConfiguration(String id, RootDirectoryConfiguration rootDirectoryConfiguration);

    List<RootDirectoryConfiguration> resolveRootDirectoriesConfiguration();

    List<RootDirectoryConfiguration> getRawRootDirectoriesConfiguration();
}
