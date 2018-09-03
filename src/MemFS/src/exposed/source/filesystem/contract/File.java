package exposed.source.filesystem.contract;

import java.io.IOException;

public interface File extends FileSystemEntry, ReadOnlyFile {
    File writeAllText(String newText) throws IOException;

    ReadOnlyFile toReadOnly() throws IOException;

    default FileParent getFileParent() throws IOException {
        return (FileParent) getParent();
    }

    default String getExtension() {
        String[] split = getName().split(".");
        return split[split.length - 1];
    }

    default String getNameWithoutExtension() {
        String name = getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex == -1) lastDotIndex = name.length() - 1;
        return name.substring(0, lastDotIndex + 1);
    }

    default File appendText(String suffix) throws IOException {
        writeAllText(readAllText() + suffix);
        return this;
    }
}
