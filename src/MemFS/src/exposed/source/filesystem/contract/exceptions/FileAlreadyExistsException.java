package exposed.source.filesystem.contract.exceptions;

public class FileAlreadyExistsException extends EntryAlreadyExistsException {
    public FileAlreadyExistsException(String name) {
        super(name);
    }
}
