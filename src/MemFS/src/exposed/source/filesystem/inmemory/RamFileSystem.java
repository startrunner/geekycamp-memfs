package exposed.source.filesystem.inmemory;

import exposed.source.filesystem.contract.File;
import exposed.source.filesystem.contract.FileSystem;
import exposed.source.filesystem.contract.Folder;

public class RamFileSystem extends RamFolderAndFileParentBase implements FileSystem {
    public RamFileSystem(String name) {
        super(StubObserver.instance, null, name);
    }

    static class StubObserver implements EntryObserver {
        public static final StubObserver instance = new StubObserver();

        private StubObserver() {
        }

        @Override
        public void notifyRenamed(String oldName, String newName) {
        }

        @Override
        public void notifyDeleted(String name) {
        }
    }
}
