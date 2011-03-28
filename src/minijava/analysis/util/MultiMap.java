package minijava.analysis.util;

import static minijava.util.List.cons;
import static minijava.util.List.theEmpty;

import java.util.HashMap;
import java.util.Map;

import minijava.util.List;

/**
 * A Map that can associate one key with multiple values.
 * 
 * @author kdvolder
 */
public class MultiMap<K,V> {
	
	private Map<K, List<V>> map = new HashMap<K, List<V>>();
	
	/**
	 * Add an association between a Key and a Value. If an association
	 * between the key value already exist, this has no effect.
	 */
	public void add(K k, V v) {
		List<V> existing = get(k);
		if (existing.contains(v)) return;
		else {
			map.put(k, cons(v, existing));
		}
	}
	
	/**
	 * Get all values associated with a given key. If there are no
	 * values this returns an empty list, otherwise it returns a 
	 * List of the values.
	 */
	public List<V> get(K k) {
		List<V> existing = map.get(k);
		if (existing==null)
			return theEmpty();
		else return existing;
	}

	/**
	 * Remove the association between a key and a value. If there is no such 
	 * association, then this has no effect.
	 */
	public void remove(K k, V v) {
		List<V> existing = get(k);
		if (existing.contains(v)) {
			existing = existing.delete(v);
			if (existing.isEmpty())
				map.remove(k);
			else 
				map.put(k, existing);
		}
	}

	public boolean containsKey(K k) {
		return map.containsKey(k);
	}

}
