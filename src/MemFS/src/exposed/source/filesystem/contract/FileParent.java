package exposed.source.filesystem.contract;

import exposed.source.filesystem.contract.exceptions.EntryDeletedException;
import exposed.source.filesystem.contract.exceptions.FileAlreadyExistsException;

import java.io.IOException;
import java.util.Iterator;

public interface FileParent extends FileSystemEntry {
    Iterator<File> iterateFiles() throws IOException;


    File getFile(String name, EntryDoesNotExistBehavior notExistingBehavior) throws IOException;
    File getFileOrNull(String relativePath) throws IOException;

    default ReadOnlyFile getReadOnlyFile(String name, EntryDoesNotExistBehavior notExistBehavior)throws IOException{
        File file = getFile(name, notExistBehavior);
        return file.toReadOnly();
    };

    default File createFile(String name, String content) throws IOException {
        if (isDeleted()) throw new EntryDeletedException(getName());
        if (fileExists(name)) throw new FileAlreadyExistsException(name);
        return getFile(name, EntryDoesNotExistBehavior.CREATE_EMPTY).writeAllText(content);
    }

    boolean fileExists(String fileName);
}
