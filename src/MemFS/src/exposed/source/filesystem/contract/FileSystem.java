package exposed.source.filesystem.contract;

import exposed.source.filesystem.contract.exceptions.EntryDeletedException;

import java.io.IOException;

public interface FileSystem extends FileAndFolderParent {

    @Override
    default FileSystemEntry getParent() throws IOException {
        if (isDeleted()) throw new EntryDeletedException(getName());
        return null;
    }
}
