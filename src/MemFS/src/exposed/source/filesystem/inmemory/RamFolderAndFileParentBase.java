package exposed.source.filesystem.inmemory;

import exposed.source.filesystem.contract.*;
import exposed.source.filesystem.contract.exceptions.FileDoesNotExistException;
import exposed.source.filesystem.contract.exceptions.FolderDoesNotExistException;
import exposed.source.filesystem.inmemory.collections.EntryMap;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public abstract class RamFolderAndFileParentBase extends RamFileSystemEntryBase implements FileAndFolderParent {
    private EntryMap<File> files = new EntryMap<>();
    private EntryMap<Folder> folders = new EntryMap<>();

    protected RamFolderAndFileParentBase(EntryObserver observer, RamFolderAndFileParentBase parent, String name) {
        super(observer, parent, name);
    }

    private static void deleteOrThrowRuntime(FileSystemEntry entry) {
        try {
            entry.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RamFolderAndFileParentBase == false) return false;
        RamFolderAndFileParentBase other = (RamFolderAndFileParentBase) o;

        return (
            super.equals(other) &&
                Objects.equals(files, other.files) &&
                Objects.equals(folders, other.folders)
        );
    }

    @Override
    public Iterator<File> iterateFiles() throws IOException {
        throwIfDeleted();
        return files.entryIterator();
    }

    @Override
    public File getFile(String name, EntryDoesNotExistBehavior notExistingBehavior) throws IOException {
        throwIfDeleted();
        File file = files.getEntryOrNull(name);

        if (file == null) {
            switch (notExistingBehavior) {
                case THROW:
                    throw new FileDoesNotExistException(name);
                case CREATE_EMPTY:
                    return files.register(new RamFile(files.renameObserver, this, name));
                case RETURN_NULL:
                    return null;
            }
        }

        return file;
    }

    @Override
    public void delete() throws IOException {
        folders.entryIterator().forEachRemaining(RamFolderAndFileParentBase::deleteOrThrowRuntime);
        files.entryIterator().forEachRemaining(RamFolderAndFileParentBase::deleteOrThrowRuntime);
        deleted = true;
        parent = null;
        observer.notifyDeleted(name);
    }

    @Override
    public Iterator<Folder> iterateFolders() throws IOException {
        throwIfDeleted();
        return folders.entryIterator();
    }

    @Override
    public Folder getFolder(String folderName, EntryDoesNotExistBehavior missingBehavior) throws IOException {
        throwIfDeleted();
        Folder folder = folders.getEntryOrNull(folderName);

        if (folder == null) {
            switch (missingBehavior) {
                case THROW:
                    throw new FolderDoesNotExistException(folderName);
                case CREATE_EMPTY:
                    return folders.register(new RamFolder(folders.renameObserver, this, folderName));
                case RETURN_NULL:
                    return null;
            }
        }

        return folder;
    }

    @Override
    public boolean folderExists(String folderName) {
        return folders.checkExists(folderName);
    }

    @Override
    public boolean fileExists(String fileName) {
        return files.checkExists(fileName);
    }
}
