package exposed.source.filesystem.contract;

import java.io.IOException;
import java.util.Iterator;

public interface FolderParent extends FileSystemEntry {
    Iterator<Folder> iterateFolders() throws IOException;

    Folder getFolder(String folderName, EntryDoesNotExistBehavior missingBehavior) throws IOException;

    boolean folderExists(String folderName);
}
