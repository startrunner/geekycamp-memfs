package exposed.source.filesystem.inmemory;

import java.io.IOException;

public interface EntryObserver {
    void notifyRenamed(String oldName, String newName) throws IOException;

    void notifyDeleted(String name) throws IOException;
}
