package exposed.source.filesystem.inmemory;

import exposed.source.filesystem.contract.FileSystemEntry;
import exposed.source.filesystem.contract.exceptions.EntryDeletedException;

import java.io.IOException;
import java.util.Objects;

public abstract class RamFileSystemEntryBase implements FileSystemEntry {
    protected final EntryObserver observer;
    protected boolean deleted;
    RamFileSystemEntryBase parent;
    String name;

    protected RamFileSystemEntryBase(EntryObserver observer, RamFileSystemEntryBase parent, String name) {
        this.observer = observer;
        this.parent = parent;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileSystemEntry == false) return false;
        FileSystemEntry other = (FileSystemEntry) o;

        return (
            Objects.equals(deleted, other.isDeleted()) &&
                Objects.equals(name, other.getName())
        );
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public abstract void delete() throws IOException;

    @Override
    public void rename(String newName) throws IOException {
        throwIfDeleted();
        String oldName = this.name;
        this.name = newName;
        observer.notifyRenamed(oldName, newName);
    }

    @Override
    public FileSystemEntry getParent() throws IOException {
        throwIfDeleted();
        return parent;
    }

    @Override
    public String getName() {
        return name;
    }

    void throwIfDeleted() throws IOException {
        if (deleted) throw new EntryDeletedException(name);
    }
}
