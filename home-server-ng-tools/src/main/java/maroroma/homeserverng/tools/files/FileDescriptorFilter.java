package maroroma.homeserverng.tools.files;

import java.util.function.Predicate;


public interface FileDescriptorFilter extends Predicate<FileDescriptor> {
    static FileDescriptorFilter fileFilter() {
        return file -> file.isFile();
    }

    static FileDescriptorFilter directoryFilter() {
        return file -> file.isDirectory();
    }

    static FileDescriptorFilter noFilter() {
        return file -> true;
    }
}
