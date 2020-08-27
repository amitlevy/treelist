
/**
 *
 * Tree list
 *
 * An implementation of a Tree list with  key and info
 *
 */
 public class TreeList{
	 /**
   * public Item retrieve(int i)
   *
   * returns the item in the ith position if it exists in the list.
   * otherwise, returns null
   */
  private AVLTree lst; //The AVL tree which keeps the data
  private int length; //number of elements in current list 
  
  public TreeList()
  {
	  this.lst = new AVLTree();
	  this.length = 0;
  }
  
  public int getlength()
  {
	  return this.length;
  }
  
  public Item retrieve(int i) //O(log(n))
  {
	if(i >= this.lst.size() || i < 0)
		return null;
	else
		return new Item(lst.Select(i+1).getKey(),lst.Select(i+1).getValue()); //getting copy of the i'th element
  }

  /**
   * public int insert(int i, int k, String s) 
   *
   * inserts an item to the ith position in list  with key k and  info s.
   * returns -1 if i<0 or i>n otherwise return 0.
   */
   
  public int insert(int i, int k, String s) //O(log(n))
  {
	  int output = this.lst.rankinsertion(i, k, s);  //see details in class AVLTree
	  if(output == 0)
		  this.length++; //we added one element
	  return output;
  }

  /**
   * public int delete(int i)
   *
   * deletes an item in the ith posittion from the list.
	* returns -1 if i<0 or i>n-1 otherwise returns 0.
   */
   
  public int delete(int i) //O(log(n))
   {
	   if(this.lst == null)
		   return -1;
	   if(i < 0 || i >= this.length)
		   return -1;
	   this.length--; //one element removed
	   this.lst.correctFrom(this.lst.deletenode(this.lst.Select(i+1))); //see details in class AVLTree
	   return 0;
   }
	  
 }