package framework;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class TreeTest {

	private Tree<Object> tree;
	private Object root;
	Object child1;
	Object child2;
	Tree<Object> otherTree;

	@BeforeEach
	void setup() {
		root = new Object();
		tree = new Tree<Object>(root);
		child1 = new Object();
		child2 = new Object();
		otherTree = new Tree<>(new Object());
	}

	@Test
	void testGetRoot() {
		assertSame(root, tree.getRoot());
	}

	void doAddChild() {
		tree.addChild(tree.getRoot(), child1);
		tree.addChild(tree.getRoot(), child2);
	}

	@Test
	void testAddChild() {
		doAddChild();
		assertTrue(tree.contains(child1) && tree.contains(child2));
		assertSame(tree.getRoot(), tree.findClosestAncestor(child1, (elem) -> {
			if (elem != child1)
				return true;
			return false;
		}));
	}

	void doAddSubtree() {
		otherTree.addSubtree(otherTree.getRoot(), tree);
	}

	@Test
	void testAddSubtree() {
		doAddChild();
		doAddSubtree();
		assertTrue(otherTree.contains(root));
		assertTrue(otherTree.contains(child1));
		assertTrue(otherTree.contains(child2));
		assertEquals(root, otherTree.findClosestAncestor(child1, elem -> elem == child1 ? false : true));
		assertEquals(
				otherTree.getRoot(),
				otherTree.findClosestAncestor(child1, elem -> elem == child1 || elem == root ? false : true));
	}

	@Test
	void testFindClosestAncestor() {
		doAddChild();
		doAddSubtree();
		assertEquals(root, otherTree.findClosestAncestor(child1, elem -> elem == child1 ? false : true));
		assertEquals(
				otherTree.getRoot(),
				otherTree.findClosestAncestor(child1, elem -> elem == child1 || elem == root ? false : true));
		assertEquals(otherTree.getRoot(), otherTree.findClosestAncestor(otherTree.getRoot(), null));
		assertEquals(null, otherTree.findClosestAncestor(otherTree.getRoot(), elem -> false));
	}

	@Test
	void testContains() {
		doAddChild();
		doAddSubtree();
		assertTrue(otherTree.contains(otherTree.getRoot()));
		assertTrue(otherTree.contains(root));
		assertTrue(otherTree.contains(child1));
		assertTrue(otherTree.contains(child2));
	}

	@Test
	void testRemoveSubtree() {
		doAddChild();
		doAddSubtree();
		otherTree.removeSubtree(root);
		assertTrue(!otherTree.contains(tree.getRoot()));
		assertTrue(!otherTree.contains(root));
		assertTrue(!otherTree.contains(child1));
		assertTrue(!otherTree.contains(child2));
		assertThrows(RuntimeException.class, () -> otherTree.removeSubtree(new Tree<Object>(new Object())));
	}

}
