package at.ac.tuwien.kr.alpha.common;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NoGoodTest {
	@Test
	public void iteration() throws Exception {
		Iterator<Integer> i = new NoGood(1).iterator();
		assertEquals(1, (int)i.next());
		assertFalse(i.hasNext());
	}

	@Test(expected = NullPointerException.class)
	public void compareToNull() throws Exception {
		new NoGood().compareTo(null);
	}

	@Test
	public void compareToSame() throws Exception {
		assertEquals(0, new NoGood(1).compareTo(new NoGood(1)));
	}

	@Test
	public void compareToLengthShort() throws Exception {
		assertEquals(-1, new NoGood(1).compareTo(new NoGood(1, 2)));
	}

	@Test
	public void compareToLengthLong() throws Exception {
		assertEquals(+1, new NoGood(1, 2).compareTo(new NoGood(1)));
	}

	@Test
	public void compareToLexicographicSmall() throws Exception {
		assertEquals(-1, new NoGood(1, 2).compareTo(new NoGood(2, 3)));
	}

	@Test
	public void compareToLexicographicBig() throws Exception {
		assertEquals(+1, new NoGood(2, 3).compareTo(new NoGood(1, 2)));
	}

	@Test
	public void deleteDuplicates() {
		NoGood ng = new NoGood(new int[]{1, -2, -2, 3}, 3);
		assertEquals("Duplicate entry must be removed.", 3, ng.size());
		assertEquals("Head pointer must be moved to correct position.", 2, ng.getHead());
		assertEquals(-2, ng.getLiteral(0));
		assertEquals(1, ng.getLiteral(1));
		assertEquals(3, ng.getLiteral(2));

		NoGood ng2 = new NoGood(new int[]{3, 3, -6, -1, 2, 5, 5, -6, 7}, 4);
		assertEquals("Duplicate entries must be removed.", 6, ng2.size());
		assertEquals("Head pointer must be moved to correct position.", 2, ng2.getHead());
		assertEquals(-6, ng2.getLiteral(0));
		assertEquals(-1, ng2.getLiteral(1));
		assertEquals(2, ng2.getLiteral(2));
		assertEquals(3, ng2.getLiteral(3));
		assertEquals(5, ng2.getLiteral(4));
		assertEquals(7, ng2.getLiteral(5));

		NoGood ng3 = new NoGood(new int[]{1, 2, -3, -4}, 0);
		assertEquals("NoGood contains no duplicates, size must stay the same.", 4, ng3.size());
		assertEquals("Head pointer must be moved to correct position.", 2, ng3.getHead());
		assertEquals(-4, ng3.getLiteral(0));
		assertEquals(-3, ng3.getLiteral(1));
		assertEquals(1, ng3.getLiteral(2));
		assertEquals(2, ng3.getLiteral(3));
	}
}