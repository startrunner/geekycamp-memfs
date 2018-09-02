package exposed.source.filesystem.contract.exceptions;

public class FolderDoesNotExistException extends EntryDoesNotExistException {
    public FolderDoesNotExistException(String folderName) {
        super(folderName);
    }
}
