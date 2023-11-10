package maroroma.homeserverng.tools.files;

public interface FileDescriptorPath {

    default boolean isParentOf(FileDescriptorPath otherPath) {
        String thisPath = this.resolvePathAsString();
        return otherPath.resolvePathAsString().startsWith(thisPath);
    }

    String resolvePathAsString();
}
