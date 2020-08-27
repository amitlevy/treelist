/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	private IAVLNode root;
	private int pointer = 0; //pointer for recursive helper methods of infoToArray and keysToArray
	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */

	// O(1)
	public boolean empty() {
		return size() == 0;
	}
	
	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	public String search(int k)  //O(logn) exactly like we saw in class
	{
		// binary search
		IAVLNode current = root;
		while (current != null) {
			if (current.getKey()==k)
			{
				return current.getValue();
			}
			else if (current.getKey() < k)
			{
				current = current.getRight();
			}
			else {
				current = current.getLeft();
			}
		}

		return null;

	}

	// O(1)
	private int getLeftHeight(IAVLNode node) {
		IAVLNode left = node.getLeft(); // assuming node != null
		if (left != null)
			return left.getHeight();
		return -1;
	}

	// O(1)
	private int getRightHeight(IAVLNode node) {
		IAVLNode right = node.getRight(); // assuming node != null
		if (right != null)
			return right.getHeight();
		return -1;
	}

	// O(1)
	private int computeBF(IAVLNode node) { // assumes node != null
		return getLeftHeight(node) - getRightHeight(node);
	}

	// O(1)
	private void updateNode(IAVLNode node) { 
		node.setHeight(1+Integer.max(getLeftHeight(node),getRightHeight(node)));
		((AVLNode)node).updateSubSize();
	}


	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) { //O(logn) exactly like we saw in class

		IAVLNode newNode = new AVLNode(k,i);

		IAVLNode current = root;

		if (this.empty())
		{
			this.root = newNode;
			return 0;
		}

		// normal insert, the inserted node is called new node.
		while (current != null) {

			if (current.getKey()==k)
			{
				return -1;
			}
			else if (current.getKey() < k)
			{
				if (current.getRight() == null) {
					current.setRight(newNode);
					break;
				}
				current = current.getRight();
			}
			else {
				if (current.getLeft() == null) {
					current.setLeft(newNode);
					break;
				}
				current = current.getLeft();
			}
		}

		IAVLNode y = newNode.getParent();
		return correctFrom(y);

	}
	//This function was written especially for TreeList
	//Inserts the Item on index i as instructed, by looking for the node on index i+1
	//O(log(n))
	public int rankinsertion(int i , int k , String s)
	{
		if(i < 0 || i > this.size())
			   return -1;
		AVLNode A = this.new AVLNode(k,s);
		if(this.size() == 0) //Creating a new list
			{
			   this.insert(k, s);
			   return 0;
		    }
		   if(i == this.size()) //insert after the last argument
		   {
			   AVLNode rightest =(AVLTree.AVLNode) this.root;
			   while(rightest.getRight() != null) //all the way right
				   rightest = (AVLTree.AVLNode)rightest.getRight();
			   rightest.setRight(A);
			   this.correctFrom(rightest); //correct AVLTree (balance and update fields)
			   return 0;
		   }
		   if(this.Select(i+1).getLeft() == null) //first case for inserting by rank
		   {
			  IAVLNode S =  this.Select(i+1);
			  S.setLeft(A);
			  this.correctFrom(S);
			  return 0;
		   }
		  AVLTree.AVLNode current = (AVLTree.AVLNode)this.Select(i+1).getLeft();
		  while(current.getRight() != null) //second case for inserting by rank
		  {
			  current = (AVLTree.AVLNode) current.getRight();
		  }
		  current.setRight(A);
		  this.correctFrom(current); 
		  return 0;
	}

	// Helper function for insert and delete. It climbs up the tree, worst case from the bottom. So it's O(logn)
	// The return value is the count of basic rotations used.
	public int correctFrom(IAVLNode y) 
	{
		int count = 0;
		while (y != null) {

			int BF = computeBF(y);
			updateNode(y);

			if (BF < 2 && BF > -2) // I could add another check for the case where the height is unchanged, but that doesn't improve asymptotic complexity, but it does add to the complexity of the code, so I decided against it.
				y = y.getParent();
			else { // BF == 2 or BF == -2
				if (BF == 2)
				{
					IAVLNode left = y.getLeft();
					int leftBF = computeBF(left);
					if (leftBF == -1)
					{
						rotateL(left);
						rotateR(y);
						count += 2;
					}
					else
					{
						rotateR(y); // issue here.
						count++;
					}
				}
				else // BF == -2
				{
					IAVLNode right = y.getRight();
					int rightBF = computeBF(right); // assumes right != null
					if (rightBF == -1 || rightBF == 0) // left rotation
					{
						rotateL(y);
						count++;
					}
					else
					{
						assert (rightBF == 1);
						rotateR(right); //evil
						rotateL(y);
						count += 2;
					}
				}
				updateNode(y);
				((AVLNode)y).updateSubSize();
				y = y.getParent();
			}

		}
		return count;	
	}

	// Left rotation helper function, used for corrections like we saw in class. O(1)
	private void rotateL (IAVLNode y) 
	{
		IAVLNode right = y.getRight();

		if (root == y) { // if root...
			setRoot(right);
			y.setRight(right.getLeft());
			right.setLeft(y);
			updateNode(y);
			this.updateNode(right);
			return;
		}
		IAVLNode parent = y.getParent();

		if (parent.getLeft() == y) {
			parent.setLeft(right);
		}
		else
			parent.setRight(right);

		y.setRight(right.getLeft());
		right.setLeft(y);
		updateNode(y);
		this.updateNode(right);
	}

	// Right rotation helper function, used for corrections like we saw in class. O(1)
	private void rotateR (IAVLNode y)
	{
		IAVLNode left = y.getLeft();
		if (root == y) { // if root...
			setRoot(left);
			y.setLeft(left.getRight());
			left.setRight(y);
			updateNode(y);
			this.updateNode(left);
			return;
		}
		IAVLNode parent = y.getParent();
		if (parent.getLeft() == y) {
			parent.setLeft(left);
		}
		else
		{
			parent.setRight(left);
		}

		y.setLeft(left.getRight());
		left.setRight(y);
		updateNode(y);
		this.updateNode(left);
	}


	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) // O(logn), exactly like we saw in class
	{
		if (search(k) == null)
			return -1;
		// delete as usual
		IAVLNode parent = deleteAsUsual(k);
		int temp = correctFrom(parent);
		return temp;
	}

	// Normal BST deletion, helper function for delete(). Since it goes down the tree, and can reach all the way to the bottom, while doing O(1) at each step, complexity is O(logn). Assumes k in tree.
	private IAVLNode deleteAsUsual(int k)
	{ 
		// binary search
		IAVLNode current = root;
		while (current != null) { // current is x
			if (current.getKey()==k)
			{
				break;
			}
			else if (current.getKey() < k)
			{
				current = current.getRight();
			}
			else {
				current = current.getLeft();
			}
		}

		IAVLNode toDelete = current;
		return deletenode(toDelete);
	}
	
	// O(logn)
	// Deletes toDelete. Different from delete(), because it needs to receive a pointer. Worst case is O(logn).
	public IAVLNode deletenode(IAVLNode toDelete)
	{
		IAVLNode parent = null;
		if (isLeaf(toDelete)) // 3 options
		{
			if (toDelete == root)
			{
				root = null;
			}

			else 
			{
				parent = toDelete.getParent();
				if (parent.getLeft() == toDelete) {
					parent.setLeft(null);
				}
				else {
					parent.setRight(null);
				}
			}
		}
		else if (toDelete.getLeft() == null || toDelete.getRight() == null) // does toDelete have only one child?
		{
			parent = removeNodeWithOneChild(toDelete);
		}

		else // toDelete has two sons.
		{
			// we will go right then all the way left.
			IAVLNode right = toDelete.getRight();
			while (right.getLeft() != null)
			{
				right = right.getLeft();
			}
			// right is the successor
			IAVLNode successor = right;
			boolean is_close = false;
			if(successor.getParent() == toDelete)
				is_close = true;
			if (isLeaf(successor))
			{
				if (successor == root)
				{
					root = null;
				}

				else 
				{
					parent = successor.getParent();
					if (parent.getLeft() == successor) {
						parent.setLeft(null);
					}
					else {
						parent.setRight(null);
					}
				}
			}
			else {
				parent = removeNodeWithOneChild(successor); 
			}
			// remove y from the tree
			replace(toDelete,successor);
			if(is_close)
				parent = successor;
		}

		return parent;
	}
	

	// Helper function for deleteAsUsual. Replaces toDelete with successor. It does so in O(1).
	private void replace(IAVLNode toDelete, IAVLNode successor)
	{
		successor.setLeft(toDelete.getLeft());
		successor.setRight(toDelete.getRight());
		if (toDelete == root)
			setRoot(successor);
		else if (toDelete == toDelete.getParent().getLeft())
			toDelete.getParent().setLeft(successor);
		else
			toDelete.getParent().setRight(successor);
	}

	// Does exactly as the name says. Helper function for deleteAsUsual(). Works in O(1).
	private IAVLNode removeNodeWithOneChild(IAVLNode toDelete) {
		IAVLNode parent = null;
		if (toDelete.getLeft() == null) // toDelete has right child
		{
			if (toDelete == root)
			{
				setRoot(toDelete.getRight());
				return null;
			}
			else {
				parent = toDelete.getParent();
				if (parent.getLeft() == toDelete)
				{
					parent.setLeft(toDelete.getRight());
					return parent;
				}
				else
				{
					parent.setRight(toDelete.getRight());
					return parent;
				}
			}
		}

		else
		{
			if (toDelete == root)
			{
				setRoot(toDelete.getLeft());
				return null;
			}
			else {
				parent = toDelete.getParent();
				if (parent.getLeft() == toDelete)
				{
					parent.setLeft(toDelete.getLeft());
					return parent;
				}
				else
				{
					parent.setRight(toDelete.getLeft());
					return parent;
				}
			}
		}
	}

	// O(1)
	private void setRoot(IAVLNode node)
	{
		root = node;
		root.setParent(null);
	}

	// O(1) returns true iff node is a leaf.
	public static boolean isLeaf(IAVLNode node)
	{
		if (node.getLeft() == null && node.getRight() == null)
		{
			return true;
		}
		return false;
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 */
	public String min() //O(logn). Goes down and left the entire tree, while performing only O(1) operations at each node.
	{
		if (this.empty())
		{
			return null;
		}
		IAVLNode min = root;
		while (min.getLeft() != null)
		{
			min = min.getLeft();
		}
		return min.getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 */
	public String max() //O(logn). Goes down and right the entire tree, while performing only O(1) operations at each node.
	{
		if (this.empty())
		{
			return null;
		}
		IAVLNode max = root;
		while (max.getRight() != null)
		{
			max = max.getRight();
		}
		return max.getValue();
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray() // Using a recursive helper function, it goes through all the nodes in the tree, performing an O(1) operation on each 1. So O(n).
	{
		pointer = 0; // Makes sure the pointer is at the start, because another function may have moved it.

		int[] result = new int[size()];

		recKeysToArray(result,root);

		return result;

	}

	// Recursive helper function for keysToArray(). When called on root complexity is O(n).
	private void recKeysToArray(int[] result, IAVLNode node)
	{
		if (pointer == result.length || node == null)
			return;

		recKeysToArray(result,node.getLeft());
		result[pointer] = node.getKey();
		pointer++;
		recKeysToArray(result,node.getRight());

	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 */
	public String[] infoToArray() // O(n). See complexity explanation of keysToArray().
	{
		pointer = 0;

		String[] result = new String[size()];

		recInfoToArray(result,root);

		return result;
	}

	// Helper function for infoToArray. When called on root complexity is O(n).
	private void recInfoToArray(String[] result, IAVLNode node)
	{
		if (pointer == result.length || node == null)
			return;

		recInfoToArray(result,node.getLeft());
		result[pointer] = node.getValue();
		pointer++;
		recInfoToArray(result,node.getRight());

	}
	
	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none
	 * postcondition: none
	 */
	public int size() // O(1)
	{
		if(this.root == null)
			return 0;
		return ((AVLNode)this.root).getSubtreeSizeOfNode();
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 *
	 * precondition: none
	 * postcondition: none
	 */
	public IAVLNode getRoot() //O(1)
	{
		return root;
	}
	//returns item number k in the tree
	//O(log(n))
	public IAVLNode Select(int k)
	{
		IAVLNode current = this.root;
		int r = 0;
		while(current != null) //binary search by subsize as seen in class
		{
			if(current.getLeft() != null)
				r += ((AVLNode)current.getLeft()).getSubtreeSizeOfNode()+1;
			else
				r+=1;
			if(k == r)
				return current;
			if(k < r) //going left with appropriate r
			{
				r -= ((AVLNode)current.getLeft()).getSubtreeSizeOfNode()+1;
				current = current.getLeft();
			}
			else //going right
				current = current.getRight();
		}
		return null;
	}

	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{	
		public int getKey(); //returns node's key 
		public String getValue(); //returns node's value [info]
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public void setHeight(int height); // sets the height of the node
		public int getHeight(); // Returns the height of the node 
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in 
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode{
		private int height;
		private int subsize = 1; // For rank tree implementation
		private int key;
		private String value;
		private IAVLNode left = null;
		private IAVLNode right = null;
		private IAVLNode parent = null;
		
		
		// All methods are of complexity O(1).
		
		// Empty constructor cause I made a custom one.
		public AVLNode() {}
		
		// Custom constructor for tests and insert.
		public AVLNode(int key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public int getKey()
		{
			return key; 
		}
		public String getValue()
		{
			return value; 
		}
		
		// Sets left son while also maintaining everything with the exception of height.
		public void setLeft(IAVLNode node)
		{
			left = node;
			if (node != null)
				node.setParent(this);
			updateSubSize();
		}
		public IAVLNode getLeft()
		{
			return left;
		}
		public void setRight(IAVLNode node)
		{
			right = node;
			if (node != null)
				node.setParent(this);
			updateSubSize();
		}
		public IAVLNode getRight()
		{	
			return right;	
		}
		public void setParent(IAVLNode node)
		{
			parent = node;
		}
		public IAVLNode getParent()
		{
			return parent;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}
		
		public int getHeight()
		{
			return height;
		}
		
		public void updateSubSize()
		{
			subsize = 1 + getSubSize((AVLNode)this.getLeft()) + getSubSize((AVLNode)this.getRight());
		}

		// Only works on AVLNode. You must cast IAVLNode to AVLNode before calling the method.
		private int getSubSize(AVLNode node) 
		{
			if (node == null)
				return 0;
			else
				return node.subsize;
		}
		
		// Gets the size of the sub tree of a node
		public int getSubtreeSizeOfNode()
		{
			return getSubSize(this);
		}
	}

}