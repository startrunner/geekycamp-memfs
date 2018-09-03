package exposed.source.filesystem.demo;

import exposed.source.filesystem.contract.*;
import exposed.source.filesystem.contract.exceptions.FileDoesNotExistException;
import exposed.source.filesystem.inmemory.RamFileSystem;

public class DemoProgram {
    public static void main(String[] args) throws Exception {
        FileSystem system = new RamFileSystem("PC");
        Folder osFolder = system.getFolder("windows", EntryDoesNotExistBehavior.CREATE_EMPTY);
        Folder system32Folder = osFolder.getFolder("misSpelledsystem32", EntryDoesNotExistBehavior.CREATE_EMPTY);
        File systemFile1 = system32Folder.createFile("file1", "content 1");
        File systemFile2 = system32Folder.createFile("file2", "content 2");
        Folder dlls = system32Folder.getFolder("dlls", EntryDoesNotExistBehavior.CREATE_EMPTY);

        File peshoDll = dlls.getFile("pesho.dll", EntryDoesNotExistBehavior.CREATE_EMPTY).writeAllText("123 content");

        System.out.println(dlls.getFullPath());
        system32Folder.rename("system32");
        System.out.println(dlls.getFullPath());

        System.out.println(peshoDll.getFullPath());
        System.out.println(peshoDll.readAllText());

        File system1 = null;

        system1 = system.getFileOrNull( "/windows/system32/file1");
        System.out.println(system1.readAllText());

        system1 = dlls.getFileOrNull("/../file1");
        System.out.println(system1.readAllText());


        print(system, 0);
    }

    private static void print(FileSystemEntry entry, int indent) throws Exception {
        for (int i = 0; i < indent; i++) System.out.print("  ");
        System.out.printf("[%s]%s\n", entry.getClass().getSimpleName(), entry.getName());
        if (entry instanceof FileAndFolderParent) {
            ((FileParent) entry).iterateFiles().forEachRemaining(x -> printOrThrowRuntime(x, indent + 1));
            ((FolderParent) entry).iterateFolders().forEachRemaining(x -> printOrThrowRuntime(x, indent + 1));
        } else if (entry instanceof File) {
            String[] lines = ((File) entry).readAllText().split("\n");
            for (String line : lines) {
                for (int i = 0; i < indent; i++) System.out.print("  ");
                System.out.printf("|%s", line);
            }
            System.out.println();
        }
    }

    private static void printOrThrowRuntime(FileSystemEntry entry, int indent) {
        try {
            print(entry, indent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
