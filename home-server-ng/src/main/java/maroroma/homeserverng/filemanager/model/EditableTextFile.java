package maroroma.homeserverng.filemanager.model;

import lombok.Builder;
import lombok.Data;
import maroroma.homeserverng.tools.files.FileDescriptor;

@Data
@Builder
public class EditableTextFile {
    private final FileDescriptor fileDescriptor;
    private final String content;
}
