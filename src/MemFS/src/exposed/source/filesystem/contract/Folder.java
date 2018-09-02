package exposed.source.filesystem.contract;

import exposed.source.filesystem.contract.exceptions.EntryDeletedException;

import java.io.IOException;

public interface Folder extends FileAndFolderParent {
    default FolderParent getFolderParent() throws IOException {
        if (isDeleted()) throw new EntryDeletedException(getName());
        return (FolderParent) getParent();
    }
}
