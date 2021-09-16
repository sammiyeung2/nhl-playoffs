/**
 * This class represents a binary tree. Each binary tree is created using a queue-based approach similar to level-order traversal.
 * @author sammi
 *
 * @param <T> generic type; can build a tree of any type
 */
public class TreeBuilder<T> {
	
	// linked binary tree of generic type
	private LinkedBinaryTree<T> tree;

	/**
	 * Constructor creates a binary tree with the given array of generic type T and inserts the array's values into the new tree's nodes.
	 * @param array an array of generic type T
	 */
	public TreeBuilder(T[] array) {
		
		// create a new linked queue of generic type T
		LinkedQueue<T> dataQueue = new LinkedQueue<T>();
		
		// initialize dataQueue with all elements (from the array) to be added in the nodes in order
		for (int i = 0; i < array.length; i++) {
			dataQueue.enqueue(array[i]);
		}
		
		// initialize parentQueue as empty queue which can hold binary tree nodes of any type
		LinkedQueue<BinaryTreeNode<T>> parentQueue = new LinkedQueue<BinaryTreeNode<T>>();
		
		// create the tree's root node with the first element of the data queue
		BinaryTreeNode<T> root = new BinaryTreeNode<T>(dataQueue.dequeue());
		
		// enqueue the root node on parentQueue
		parentQueue.enqueue(root);
		
		// while dataQueue has elements
		while (!dataQueue.isEmpty()) {
			
			// dequeue an element from dataQueue
			T a = dataQueue.dequeue();
			// dequeue a second element from dataQueue
			T b = dataQueue.dequeue();
			// make a new BinaryTreeNode representing the parent node by dequeueing from the parentQueue
			BinaryTreeNode<T> parent = parentQueue.dequeue();
			
			// if there is a value held in the first element dequeued
			if (a != null) {
				//  make a new node with the value from the first element
				BinaryTreeNode<T> leftChild = new BinaryTreeNode<T>(a);
				// set it as the left child of the parent
				parent.setLeft(leftChild);
				// enqueue it onto the parentQueue
				parentQueue.enqueue(leftChild);
			}
			
			// if there is a value held in the second element dequeued
			if (b != null) {
				// make a new node with the value from the second element
				BinaryTreeNode<T> rightChild = new BinaryTreeNode<T>(b);
				// set it as the right child of the parent
				parent.setRight(rightChild);
				// enqueue it onto the parentQueue
				parentQueue.enqueue(rightChild);
			}
			
		}
		
		
		//  initialize the LinkedBinaryTree instance variable with the root node 
		tree = new LinkedBinaryTree<T>(root);
		
	}
	
	/**
	 * Accessor method to get the binary tree
	 * @return the linked binary tree of generic type T
	 */
	public LinkedBinaryTree<T> getTree() {
		// return the tree
		return tree;
	}
	

}
