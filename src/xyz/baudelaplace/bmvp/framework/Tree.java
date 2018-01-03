package xyz.baudelaplace.bmvp.framework;

import java.util.Enumeration;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.google.common.collect.HashBiMap;

public class Tree<T> {
	private HashBiMap<DefaultMutableTreeNode, T> map = HashBiMap.create();
	private DefaultTreeModel tree = null;

	public T getRoot() {
		return map.get(tree.getRoot());
	}

	Tree(T root) {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		map.put(rootNode, root);
		tree = new DefaultTreeModel(rootNode);
	}

	void addChild(T where, T child) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
		DefaultMutableTreeNode parentNode = map.inverse().get(where);
		map.put(childNode, child);
		tree.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
	}

	/**
	 * No cloning will be done. subtree will share its nodes with the current tree
	 * 
	 * @param where
	 * @param subtree
	 */
	void addSubtree(T where, Tree<T> subtree) {
		DefaultMutableTreeNode sourceNode = map.inverse().get(where);

		tree.insertNodeInto((MutableTreeNode) subtree.tree.getRoot(), sourceNode, sourceNode.getChildCount());
		map.putAll(subtree.map);
	}

	/**
	 * 
	 * @param source
	 * @param predicate
	 *            null for not filtering at all (returns source)
	 * @return An ancestor (possibly source itself), if one was found. null
	 *         otherwise
	 */
	@Nullable
	public T findClosestAncestor(T source, @Nullable Predicate<T> predicate) {
		if (predicate == null)
			return source;

		TreeNode[] nodePath = this.map.inverse().get(source).getPath();
		T currT = null;

		for (int i = nodePath.length - 1; i >= 0; i--) {
			currT = this.map.get(nodePath[i]);
			if (predicate.test(currT))
				return currT;
		}

		return null;

	}

	public boolean contains(T node) {
		return map.inverse().containsKey(node);
	}

	public void removeSubtree(T subtreeRoot) {
		if (!contains(subtreeRoot))
			throw new RuntimeException("Tried to remove a subtree, but it was not part of the tree");
		tree.removeNodeFromParent(map.inverse().get(subtreeRoot));
		
		Enumeration<?> keys = map.inverse().get(subtreeRoot).breadthFirstEnumeration();
		while (keys.hasMoreElements()) {
			map.remove(keys.nextElement());
		}
	}
}
