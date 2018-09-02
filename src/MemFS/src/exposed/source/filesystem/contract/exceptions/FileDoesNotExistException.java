package exposed.source.filesystem.contract.exceptions;

public class FileDoesNotExistException extends EntryDoesNotExistException {
    public FileDoesNotExistException(String fileName) {
        super(fileName);
    }
}
