package exposed.source.filesystem.contract.exceptions;

public class FolderAlreadyExistsException extends FileSystemException {
    protected FolderAlreadyExistsException(String name) {
        super(name);
    }
}
