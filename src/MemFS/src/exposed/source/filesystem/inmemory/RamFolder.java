package exposed.source.filesystem.inmemory;

import exposed.source.filesystem.contract.Folder;

public class RamFolder extends RamFolderAndFileParentBase implements Folder {
    public RamFolder(EntryObserver observer, RamFolderAndFileParentBase parent, String name) {
        super(observer, parent, name);
    }
}
