package exposed.source.filesystem.contract.exceptions;

import java.io.IOException;

public abstract class FileSystemException extends IOException {
    public final String name;

    protected FileSystemException(String name) {
        this.name = name;
    }
}
