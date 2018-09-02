package exposed.source.filesystem.contract;

import exposed.source.filesystem.contract.exceptions.EntryDeletedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface FileSystemEntry {
    void rename(String newName) throws IOException;

    FileSystemEntry getParent() throws IOException;

    String getName();

    void delete() throws IOException;

    boolean isDeleted();

    default boolean isRoot() throws IOException {
        if (isDeleted()) throw new EntryDeletedException(getName());
        return getParent() == null;
    }

    default String getFullPath() throws IOException {
        if (isDeleted()) throw new EntryDeletedException(getName());
        List<String> segments = new ArrayList<>();

        FileSystemEntry currentEntry = this;
        for (; ; ) {
            segments.add(currentEntry.getName());
            if (currentEntry.isRoot()) break;
            currentEntry = currentEntry.getParent();
        }

        Collections.reverse(segments);

        return String.join("/", segments);
    }
}
