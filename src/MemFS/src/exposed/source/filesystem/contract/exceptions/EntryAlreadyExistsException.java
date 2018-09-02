package exposed.source.filesystem.contract.exceptions;

public class EntryAlreadyExistsException extends FileSystemException {
    protected EntryAlreadyExistsException(String name) {
        super(name);
    }
}
