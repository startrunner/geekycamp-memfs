package exposed.source.filesystem.contract.exceptions;

public class EntryDoesNotExistException extends FileSystemException {
    public EntryDoesNotExistException(String entryName) {
        super(entryName);
    }
}
