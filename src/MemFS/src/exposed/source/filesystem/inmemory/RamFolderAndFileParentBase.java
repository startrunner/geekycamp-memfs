package exposed.source.filesystem.inmemory;

import exposed.source.filesystem.contract.*;
import exposed.source.filesystem.contract.exceptions.FileDoesNotExistException;
import exposed.source.filesystem.contract.exceptions.FolderDoesNotExistException;
import exposed.source.filesystem.inmemory.collections.EntryMap;

import javax.swing.text.Segment;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public abstract class RamFolderAndFileParentBase extends RamFileSystemEntryBase implements FileAndFolderParent {
    private EntryMap<File> files = new EntryMap<>();
    private EntryMap<Folder> folders = new EntryMap<>();

    protected RamFolderAndFileParentBase(EntryObserver observer, RamFolderAndFileParentBase parent, String name) {
        super(observer, parent, name);
    }

    private static void deleteOrThrowRuntime(FileSystemEntry entry) {
        try {
            entry.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RamFolderAndFileParentBase == false) return false;
        RamFolderAndFileParentBase other = (RamFolderAndFileParentBase) o;

        return (
            super.equals(other) &&
                Objects.equals(files, other.files) &&
                Objects.equals(folders, other.folders)
        );
    }

    @Override
    public Iterator<File> iterateFiles() throws IOException {
        throwIfDeleted();
        return files.entryIterator();
    }

    @Override
    public File getFile(String name, EntryDoesNotExistBehavior notExistingBehavior) throws IOException {
        throwIfDeleted();
        File file = files.getEntryOrNull(name);

        if (file == null) {
            switch (notExistingBehavior) {
                case THROW:
                    throw new FileDoesNotExistException(name);
                case CREATE_EMPTY:
                    return files.register(new RamFile(files.renameObserver, this, name));
                case RETURN_NULL:
                    return null;
            }
        }

        return file;
    }

    @Override
    public void delete() throws IOException {
        List<Folder> folders = new ArrayList<>();
        List<File> files = new ArrayList<>();
        this.folders.entryIterator().forEachRemaining(x->folders.add(x));
        this.files.entryIterator().forEachRemaining(x->files.add(x));
        folders.iterator().forEachRemaining(RamFolderAndFileParentBase::deleteOrThrowRuntime);
        files.iterator().forEachRemaining(RamFolderAndFileParentBase::deleteOrThrowRuntime);
        deleted = true;
        parent = null;
        observer.notifyDeleted(name);
    }

    @Override
    public Iterator<Folder> iterateFolders() throws IOException {
        throwIfDeleted();
        return folders.entryIterator();
    }

    @Override
    public Folder getFolder(String folderName, EntryDoesNotExistBehavior missingBehavior) throws IOException {
        throwIfDeleted();
        Folder folder = folders.getEntryOrNull(folderName);

        if (folder == null) {
            switch (missingBehavior) {
                case THROW:
                    throw new FolderDoesNotExistException(folderName);
                case CREATE_EMPTY:
                    return folders.register(new RamFolder(folders.renameObserver, this, folderName));
                case RETURN_NULL:
                    return null;
            }
        }

        return folder;
    }

    @Override
    public boolean folderExists(String folderName) {
        return folders.checkExists(folderName);
    }

    @Override
    public boolean fileExists(String fileName) {
        return files.checkExists(fileName);
    }

    @Override public File getFileOrNull(String relativePath) throws IOException {
        List<String> segments = Arrays.asList(trimPath(relativePath).split("/"));

        Folder folder = getFolderOrNull(segments, segments.size()-1);
        if(folder == null)return null;
        String fileName = segments.get(segments.size()-1);
        if(folder.fileExists(fileName)){
            return folder.getFile(fileName, EntryDoesNotExistBehavior.THROW);
        }
        else return null;
    }

    @Override public Folder getFolderOrNull(String relativePath) throws IOException {
        List<String> segments = Arrays.asList(trimPath(relativePath).split("/"));
        return getFolderOrNull(segments, segments.size());
    }

    private Folder getFolderOrNull(List<String> segments, int segmentCount)throws  IOException{
        FileSystemEntry entry = this;

        for(int i=0;i<segmentCount;i++){
            String segment = segments.get(i);
            boolean isLastSegment = i+1 == segments.size();
            if(entry == null)return null;
            else if(Objects.equals(segment, "..")){
                entry = entry.getParent();
            }
            else if(entry instanceof FolderParent){
                FolderParent folder = (FolderParent)entry;
                if(folder.folderExists(segment)) {
                    entry = folder.getFolder(segment, EntryDoesNotExistBehavior.THROW);
                }
            }
            else return null;
        }

        if(entry instanceof Folder)return (Folder)entry;
        return null;
    }

    private static String trimPath(String path){
        Deque<Character> chars=new ArrayDeque<>();
        for(int i=0;i<path.length();i++)chars.add(path.charAt(i));

        while (chars.size()!=0 && chars.peekFirst().equals('/')){
            chars.removeFirst();
        }

        while (chars.size()!=0 && chars.peekLast().equals('/')){
            chars.removeLast();
        }

        StringBuilder builder = new StringBuilder();
        chars.forEach(x->{ if(!x.equals(' '))builder.append(x); });
        return builder.toString();
    }
}
