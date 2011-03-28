package minijava.analysis.util;

import minijava.util.DefaultIndentable;
import minijava.util.IndentingWriter;
import minijava.util.List;


/**
 * A class that represent an "active" set. Basically this represent a Set of
 * some kind of objects which is related to other sets by means of some equations.
 * <p>
 * The assumption is that the equations are "monotonic", and that the sets can not
 * grow beyond a certain finite size. Thus, the equations can
 * be solved by starting with empty sets and keep adding elements to the active sets 
 * until there are no more changes. Typical data flow equations can be solved in 
 * this manner.
 * <p>
 * We call these sets "active" because they update themselves automatically: 
 * whenever an element gets added to an ActiveSet all of its listeners (other active
 * sets who's contents depends in some way on this set) will be notified 
 * and they may then do something to update themselves.
 * <p>
 * Note: I have used this class to implement liveness analysis, but perhaps it is
 * more intuitive to you to implement the "iterative fixpoint computation" more
 * directly. You are not required to use this implementation.
 * Personally, I like this implementation style because it lets you more or
 * less express the equations directly, by constructing activesets that obey the
 * equations. You don't have to worry about iteratively solving the equations since
 * the active sets are implemented in such a way that they are always kept "up to date"
 * with the equations. This is essentially equivalent with the iterative approach
 * (listener notification updates keep getting triggered until no more changes
 * occur in the active set contents).
 * 
 * @author kdvolder
 */
public class ActiveSet<E> extends DefaultIndentable {

	/**
	 * A list of parties interested in elements that are being added to this set.
	 */
	private List<ASListener<E>> listeners = List.theEmpty();
	
	public interface ASListener<E> {
		public abstract void elementAdded(E e);
	}

	public void addListener(ASListener<E> l) {
		listeners = List.cons(l, listeners);
		for (E e : elements) 
			l.elementAdded(e);
	}

	/**
	 * Elements that are currently in this set (this set can grow, but not shrink).
	 */
	private List<E> elements = List.theEmpty();
	
	public void add(E e) {
		if (!elements.contains(e)) {
			elements = List.cons(e, elements);
			for (ASListener<E> l : listeners)
				l.elementAdded(e);
		}
	}
	
	/**
	 * Add all the elements from another active set. If elements get added
	 * to the other set later, we will make sure to also add them to this
	 * set.
	 */
	public void addAll(ActiveSet<E> other) {
		other.addListener(new ASListener<E>() {
			@Override
			public void elementAdded(E e) {
				add(e);
			}
		});
	}

	/**
	 * Create an "active" union of several active sets. Whenever an element is
	 * added to one of the sets, it is also automatically added to the union set.
	 */
	public static <E> ActiveSet<E> union(ActiveSet<E>... sets) {
		final ActiveSet<E> union = new ActiveSet<E>();
		for (int i = 0; i < sets.length; i++) {
			union.addAll(sets[i]);
		}
		return union;
	}
	
	/**
	 * Create an "active" intersection of two active sets (the receiver and
	 * one other
	 */
	public ActiveSet<E> intersection(ActiveSet<E> other) {
		
		/** 
		 * Helper class to implement intersection of two active sets. It listens for
		 * new elements in one of the two active sets and adds the element to 
		 * the intersection set if it also exists in the other active set.
		 */
		class IntersectingListener implements ASListener<E> {

			private ActiveSet<E> intersection;
			private ActiveSet<E> other;

			public IntersectingListener(ActiveSet<E> intersection, ActiveSet<E> other) {
				this.intersection  = intersection;
				this.other = other;
			}

			@Override
			public void elementAdded(E e) {
				if (other.contains(e))
					intersection.add(e);
			}

		}
		
		final ActiveSet<E> result = new ActiveSet<E>();
		this.addListener(new IntersectingListener(result, other));
		other.addListener(new IntersectingListener(result, this));
		return result;
	}
	
	/**
	 * Create an active set that contains all of the elements of the receiver,
	 * except for some elements that are being explicitly excluded.
	 * <p>
	 * Note: that doing this does not violate monotonicity assumptions. We
	 * don't actually remove elements from an active set. Instead we create
	 * a new active set. The new active set can also only grow (not shrink)
	 * if the set from which it is computed grows.
	 */
	public ActiveSet<E> remove(final List<E> toRemove) {
		final ActiveSet<E> result = new ActiveSet<E>();
		ASListener<E> addElement = new ASListener<E>() {
			@Override
			public void elementAdded(E e) {
				if (toRemove.contains(e)) return;
				result.add(e);
			}
		};
		this.addListener(addElement);
		return result;
	}

	public void addAll(List<E> add) {
		for (E e : add)
			add(e);
	}

	public List<E> getElements() {
		return elements;
	}
	
	public boolean contains(E e) {
		return getElements().contains(e);
	}

	@Override
	public void dump(IndentingWriter out) {
		out.print("ActiveSet { ");
		boolean first = true;
		for (E e : getElements()) {
			if (!first) out.print(", ");
			out.print(e);
			first = false;
		}
		out.print(" }");
	}
	
}
