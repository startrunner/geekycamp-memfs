package exposed.source.filesystem.contract;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public interface FileAndFolderParent extends FileParent, FolderParent {
    File getFileOrNull(String relativePath) throws IOException;
    Folder getFolderOrNull(String relativePath) throws IOException;
}
