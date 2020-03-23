import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    private int _size;
    private LinkedList<String>[] _store;

    private static double MIN_LOAD = 0.2;
    private static double MAX_LOAD = 5;

    ECHashStringSet() {
        _size = 0;
        _store = new LinkedList[(int)(1/MIN_LOAD)];
        for (int i = 0; i < _store.length; i++) {
            _store[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        if ((double)_size / (double)_store.length > MAX_LOAD) {
            reSize();
        }
        _store[s.hashCode() & 0x7fffffff % _store.length].add(s);
        _size++;
    }

    private void reSize() {
        LinkedList<String>[] prevStorage = _store;
        _store = new LinkedList[prevStorage.length * 2];
        _size = 0;
        for (LinkedList<String> l : prevStorage) {
            if (l != null) {
                for (String s: l) {
                    put(s);
                }
            }
        }
    }

    @Override
    public boolean contains(String s) {
        return _store[s.hashCode() & 0x7fffffff % _store.length].contains(s);
    }

    @Override
    public List<String> asList() {
        LinkedList<String> result = new LinkedList<String>();
        for (LinkedList<String> l: _store) {
            result.addAll(l);
        }
        return result;
    }
}