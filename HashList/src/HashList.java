import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Resizable-array and Hash-table based implementation of the {@code Collection} interface. This
 * class provides a hybrid structure that combines the {@code O(1)} constant-time performance for
 * positional access ({@code get}) and attribute lookups ({@code contains}), while ensuring that all
 * elements in the collection are unique.
 *
 * <p>
 * Unlike a standard {@link HashSet}, this implementation maintains a defined iteration order via an
 * internal {@link ArrayList}. However, please note that the {@link #removeFast(int)} operation
 * performs a "swap-to-back" movement which <i>does not</i> preserve the insertion order of
 * elements.
 *
 * <p>
 * The {@code size}, {@code isEmpty}, {@code get}, {@code contains}, and {@code iterator} operations
 * run in constant time. The {@code add} operation runs in <i>amortized constant time</i>. All other
 * operations run in linear time.
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If multiple threads access a
 * {@code HashList} instance concurrently, and at least one of the threads modifies the list
 * structurally, it <i>must</i> be synchronized externally. This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the collection.
 *
 * <p>
 * The iterators returned by this class's {@link #iterator()} method are <i>fail-fast</i>: if the
 * collection is structurally modified at any time after the iterator is created, in any way except
 * through the iterator's own {@code remove} method, the iterator will throw a
 * {@link ConcurrentModificationException}.
 *
 * @param <T> the type of elements maintained by this collection * @see ArrayList
 * @see HashSet
 * @see AbstractCollection
 */
public class HashList<T> extends AbstractCollection<T> {
    private final ArrayList<T> list;
    private final HashSet<T> set;

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is negative
     */
    public HashList(int initialCapacity) {
        this.list = new ArrayList<>(initialCapacity);
        this.set = new HashSet<>(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public HashList() {
        this(10);
    }

    @Override
    public boolean add(T item) {
        if (set.add(item)) {
            return list.add(item);
        }
        return false;
    }

    @Override
    public void clear() {
        list.clear();
        set.clear();
    }

    @Override
    public boolean contains(Object item) {
        return set.contains(item);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public boolean remove(Object o) {
        if (set.remove(o)) {
            return list.remove(o);
        }
        return false;
    }

    @Override
    public int size() {
        return list.size();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public T get(int index) {
        return list.get(index);
    }

    /**
     * Returns {@code true} if this list contains no elements.
     *
     * @return {@code true} if this list contains no elements
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Removes the element at the specified position in this list. Shifts any subsequent elements to
     * the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public T remove(int index) {
        T item = list.remove(index);
        set.remove(item);
        return item;
    }

    /**
     * Removes the element at the specified position in this list. Replaces the specified position
     * with the last element. Does not preserve the order of the elements in this list.
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public T removeFast(int index) {
        int last = list.size() - 1;
        T item = list.get(index);
        T lastItem = list.get(last);
        if (index != last) {
            list.set(index, lastItem);
        }

        set.remove(item);
        list.remove(last);
        return item;
    }

    /**
     * Randomly permutes this list using the default source of randomness.
     * 
     * @see Collections#shuffle(java.util.List)
     */
    public void shuffle() {
        Collections.shuffle(list);
    }

    /**
     * Randomly permutes this list using the default source of randomness.
     * 
     * @see Collections#shuffle(java.util.List)
     * 
     * @param rnd the source of randomness to use to shuffle the list.
     */
    public void shuffle(Random rnd) {
        Collections.shuffle(list, rnd);
    }
}
