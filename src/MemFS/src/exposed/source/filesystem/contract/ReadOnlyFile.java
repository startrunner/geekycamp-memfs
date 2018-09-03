package exposed.source.filesystem.contract;

import java.io.IOException;

public interface ReadOnlyFile {
    String readAllText() throws IOException;
}
