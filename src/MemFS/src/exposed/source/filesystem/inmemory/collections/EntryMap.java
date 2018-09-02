package exposed.source.filesystem.inmemory.collections;

import exposed.source.filesystem.contract.FileSystemEntry;
import exposed.source.filesystem.contract.exceptions.EntryDoesNotExistException;
import exposed.source.filesystem.inmemory.EntryObserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class EntryMap<TEntry extends FileSystemEntry> {
    public final EntryObserver renameObserver = new MapEntryObserver();
    private final Map<String, TEntry> map = new HashMap<>();

    private void handleRenamed(TEntry entry, String oldName, String newName) {
        map.remove(oldName);
        map.put(oldName, entry);
    }

    private void handleDeleted(TEntry entry, String name) {
        map.remove(name);
    }

    public boolean checkExists(String name) {
        return map.containsKey(name);
    }

    public TEntry getEntryOrNull(String name) {
        return map.getOrDefault(name, null);
    }

    public TEntry register(TEntry entry) {
        map.put(entry.getName(), entry);
        return entry;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EntryMap == false) return false;
        EntryMap<TEntry> other = ((EntryMap<TEntry>) o);

        for (String name : map.keySet()) {
            TEntry myEntry = map.get(name);
            TEntry otherEntry = other.map.getOrDefault(name, null);
            if (!Objects.equals(myEntry, otherEntry)) return false;
        }

        return true;
    }

    public Iterator<TEntry> entryIterator() {
        return map.values().iterator();
    }

    public boolean tryDelete(String name) {
        TEntry entry = map.getOrDefault(name, null);
        if (entry == null) return false;
        map.remove(name);
        return true;
    }

    class MapEntryObserver implements EntryObserver {
        @Override
        public void notifyRenamed(String oldName, String newName) throws IOException {
            TEntry entry = map.getOrDefault(oldName, null);
            if (entry == null) throw new EntryDoesNotExistException(oldName);
            handleRenamed(entry, oldName, newName);
        }

        @Override
        public void notifyDeleted(String name) throws IOException {
            TEntry entry = map.getOrDefault(name, null);
            if (entry == null) throw new EntryDoesNotExistException(name);
            handleDeleted(entry, name);
        }
    }
}
