package minijava.analysis.util.example;

import static minijava.util.List.*;

import minijava.analysis.util.ActiveSet;
import minijava.analysis.util.ActiveSet.ASListener;
import minijava.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Some examples of the use of active sets. (Written as JUnit
 * tests).
 * <p>
 * Note: these are the same examples used in lecture to explain 
 * the ActiveSet class.
 * 
 * @author kdvolder
 */
public class ActiveSetExample {
	
	@Test
	public void simple() throws Exception {
		//Example 1: Active sets are just sets :-)
		//   (duplicate elements added are ignored)
		ActiveSet<Integer> oneTwo = new ActiveSet<Integer>();
		oneTwo.add(1);
		oneTwo.add(2);
		oneTwo.add(2);
		
		System.out.println("oneTwo = "+oneTwo);
		
		expectAll(List.list(1,2), oneTwo);
	}
	
	@Test 
	public void activeness() throws Exception {
		//Active sets are active. If one set is computed from another...
		//   then if elements are added to the "input set(s)" then the
		//   "output set" is automatically updated as well.
		
		ActiveSet<Integer> oneTwo = new ActiveSet<Integer>();
		oneTwo.add(1); 
		
		ActiveSet<Integer> threeFour = new ActiveSet<Integer>();
		
		ActiveSet<Integer> allFour = new ActiveSet<Integer>();
		allFour.addAll(oneTwo);
		allFour.addAll(threeFour);
		
		oneTwo.add(2);
		threeFour.add(3); threeFour.add(4);
		System.out.println("oneTwo = "+oneTwo);
		System.out.println("threeFour = "+threeFour);
		System.out.println("allFour = "+allFour);
		
		//allFour should contain all four elements from the two subsets,
		//  even though some of the elements were only added to the "subsets" 
		//  after adding the subsets to allFour.
		expectAll(List.list(1,2,3,4), allFour);
	}
	
	@Test 
	public void remove() throws Exception {
		// You can remove a list of elements from an active set...
		//  but only in a "functional" way (i.e. the result is a new
		//  active set.)
		
		ActiveSet<Integer> oneTwoThree = new ActiveSet<Integer>();
		ActiveSet<Integer> oneTwo = oneTwoThree.remove(list(3));
		
		oneTwoThree.add(1);
		oneTwoThree.add(2);
		oneTwoThree.add(3);
		
		System.out.println("oneTwo = "+oneTwo);
		System.out.println("oneTwoThree = "+oneTwoThree);
		
		expectAll(List.list(1,2), oneTwo);
		expectAll(List.list(1,2,3), oneTwoThree);
	}
	
	@Test
	public void function() throws Exception {
		//Example of elements in one set that are computed by 
		//mapping a function on another set
		
		final ActiveSet<Integer> oneTwoThree = new ActiveSet<Integer>();
		final ActiveSet<Integer> timesTwo = new ActiveSet<Integer>();
		
		oneTwoThree.addListener(new ASListener<Integer>() {
			@Override
			public void elementAdded(Integer e) {
				timesTwo.add(e*2);
			}
		});
		
		oneTwoThree.add(1);
		oneTwoThree.add(2);
		oneTwoThree.add(3);
		
		System.out.println("oneTwoTree = "+oneTwoThree);
		System.out.println("timesTwo = "+timesTwo);
		
		expectAll(List.list(1,2,3), oneTwoThree);
		expectAll(List.list(2,4,6), timesTwo);
	}
	
	@Test
	public void circular() throws Exception {
		// Problem:
		//  Solve the following equations 
		//    (The equations are circularly dependent
		//     on each other)
		
		// A = {1,2} U (B - {4})
		// B = {3,4} U (C - {5})
		// C = A U {4,5}
		
		ActiveSet<Integer> a = new ActiveSet<Integer>();
		ActiveSet<Integer> b = new ActiveSet<Integer>();
		ActiveSet<Integer> c = new ActiveSet<Integer>();

		// The equation for A:
		a.add(1); a.add(2);
		a.addAll(b.remove(list(4)));

		// The equation for B:
		b.add(3); b.add(4);
		b.addAll(c.remove(list(5)));
		
		// The equation for C:
		c.add(4); c.add(5);
		c.addAll(a);
		
		// Print out the solution:
		System.out.println("A = "+a);
		System.out.println("B = "+b);
		System.out.println("C = "+c);
	}
	
	@Test
	public void intersection() {
		ActiveSet<Integer> multiple2 = new ActiveSet<Integer>();
		   // multiple2 will be a set {2,4,6, ...} 
		ActiveSet<Integer> multiple3 = new ActiveSet<Integer>();
		   // multiple3 will be a set {3,6,9, ...} 
		ActiveSet<Integer> multiple6 = multiple2.intersection(multiple3);
		for (int i = 1; i <= 20; i++) {
			if (i>10) {
				if (i%2==0) multiple2.add(i);
				if (i%3==0) multiple3.add(i);
			}
			else {
				if (i%3==0) multiple3.add(i);
				if (i%2==0) multiple2.add(i);
			}
		}
		System.out.println(multiple6);
		expectAll(list(6,12,18), multiple6);
	}

	private <E> void expectAll(List<E> expect, ActiveSet<E> set) {
		Assert.assertEquals(expect.size(), set.getElements().size());
		for (E e : expect) {
			Assert.assertTrue(set.getElements().contains(e));
		}
	}
	
}
