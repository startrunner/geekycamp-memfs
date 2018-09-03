package exposed.source.filesystem.inmemory;

import exposed.source.filesystem.contract.File;
import exposed.source.filesystem.contract.ReadOnlyFile;

import java.io.IOException;
import java.util.Objects;

public class RamFile extends RamFileSystemEntryBase implements File {
    private String text;

    private class ReadOnlyHandle implements ReadOnlyFile {

        @Override
        public String readAllText() throws IOException {
            return RamFile.this.readAllText();
        }
    }

    public RamFile(EntryObserver observer, RamFolderAndFileParentBase parent, String name) {
        super(observer, parent, name);
    }

    @Override
    public ReadOnlyFile toReadOnly() throws IOException {
        throwIfDeleted();
        return  new ReadOnlyHandle();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof File == false) return false;
        File other = (File) o;

        try {
            return (
                super.equals(o) &&
                    Objects.equals(this.isDeleted(), other.isDeleted()) &&
                    Objects.equals(isDeleted(), false) &&
                    Objects.equals(this.readAllText(), other.readAllText())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String readAllText() throws IOException {
        throwIfDeleted();
        return text;
    }

    @Override
    public File writeAllText(String newText) throws IOException {
        throwIfDeleted();
        text = newText;
        return this;
    }

    @Override
    public void delete() throws IOException {
        deleted = true;
        parent = null;
        this.observer.notifyDeleted(name);
    }
}
