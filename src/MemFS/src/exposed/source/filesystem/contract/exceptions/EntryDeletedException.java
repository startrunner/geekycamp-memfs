package exposed.source.filesystem.contract.exceptions;

public class EntryDeletedException extends FileSystemException {
    public EntryDeletedException(String name) {
        super(name);
    }
}
