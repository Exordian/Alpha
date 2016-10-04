package at.ac.tuwien.kr.alpha.grounder;

import at.ac.tuwien.kr.alpha.common.ConstantTerm;
import at.ac.tuwien.kr.alpha.common.Term;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Copyright (c) 2016, the Alpha Team.
 */
public class IndexedInstanceStorageTest {
	@Test
	public void testIndexedInstanceStorage() {
		IndexedInstanceStorage storage = new IndexedInstanceStorage("A test storage of arity 4", 4);
		storage.addIndexPosition(0);
		storage.addIndexPosition(2);
		ConstantTerm t0 = ConstantTerm.getConstantTerm("0");
		ConstantTerm t1 = ConstantTerm.getConstantTerm("1");
		ConstantTerm t2 = ConstantTerm.getConstantTerm("2");
		ConstantTerm t3 = ConstantTerm.getConstantTerm("3");
		ConstantTerm t4 = ConstantTerm.getConstantTerm("4");
		ConstantTerm t5 = ConstantTerm.getConstantTerm("5");


		Instance badInst1 = new Instance(new Term[]{t1, t1, t0});
		Instance badInst2 = new Instance(new Term[]{t5, t5, t5, t5, t5 });

		try {
			storage.addInstance(badInst1);
			fail();
		} catch (Exception e) {
			assertTrue(e.getMessage().startsWith("Instance length does not match arity of IndexedInstanceStorage"));
		}

		try {
			storage.addInstance(badInst2);
			fail();
		} catch (Exception e) {
			assertTrue(e.getMessage().startsWith("Instance length does not match arity of IndexedInstanceStorage"));
		}

		Instance inst1 = new Instance(new Term[]{t1, t1, t1, t1});
		Instance inst2 = new Instance(new Term[]{t1, t2, t3, t4});
		Instance inst3 = new Instance(new Term[]{t4, t3, t3, t5});
		Instance inst4 = new Instance(new Term[]{t1, t2, t1, t1});
		Instance inst5 = new Instance(new Term[]{t5, t4, t3, t2});

		storage.addInstance(inst1);
		storage.addInstance(inst2);
		storage.addInstance(inst3);
		storage.addInstance(inst4);
		storage.addInstance(inst5);

		List<Instance> matching3 = storage.getInstancesMatchingAtPosition(t3, 2);
		assertEquals(matching3.size(), 3);
		assertTrue(matching3.contains(new Instance(new Term[]{t1, t2, t3, t4})));
		assertTrue(matching3.contains(new Instance(new Term[]{t4, t3, t3, t5})));
		assertTrue(matching3.contains(new Instance(new Term[]{t5, t4, t3, t2})));
		assertFalse(matching3.contains(new Instance(new Term[]{t1, t1, t1, t1})));

		List<Instance> matching1 = storage.getInstancesMatchingAtPosition(t2, 0);
		assertEquals(matching1.size(), 0);
	}

}