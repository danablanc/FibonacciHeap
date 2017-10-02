
/**
 * FibonacciHeap
 * 
 * Students:
 * kapustin - Ariel Klein i.d 308548692
 * danielab1 - Daniela Blanc Zusmanovich i.d 307969352
 *
 * An implementation of fibonacci heap over non-negative integers.
 * 
 */
public class FibonacciHeap
{
	public HeapNode min_node;
	public int size;
	public int marked_cnt;
	public static int total_links;
	public int num_of_trees;
	public static int total_cuts;
	public static final double PHI = (1+Math.sqrt(5))/2;
	
	
	
	/**
	    * public HeapNode getMin_node()
	    *
	    * returns this node with the minimun key of the FibonacciHeap
	    * 
	    * Complexity- O(1)
	    */
   public HeapNode getMin_node() {
		return min_node;
	}

   

	/**
    * public int getSize()
    *
    * returns the size of the FibonacciHeap
    * 
    * Complexity- O(1)
    */
	public int getSize() {
		return size;
	}


	/**
	    * public int getMarked_cnt()
	    *
	    * returns the number of the marked nodes in the FibonacciHeap
	    * 
	    * Complexity- O(1)
	    */
	public int getMarked_cnt() {
		return marked_cnt;
	}


	/**
	    * public int getNum_of_trees()
	    *
	    * returns the number of the trees (number of roots) in the FibonacciHeap
	    * 
	    * Complexity- O(1)
	    */
	public int getNum_of_trees() {
		return num_of_trees;
	}



/**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    * 
    * Complexity- O(1)
    */
    public boolean empty(){
    	return size == 0;
    }
		
    
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * 
    *  Complexity- O(1) using functions connect and empty which are also O(1) complexity
    */
    public HeapNode insert(int key){
    	HeapNode node = new HeapNode((Integer)key);
    	if(empty()){
    		min_node = node;
    	}
    	else{
    		connect(min_node,node);
    		if(key < min_node.key)
    			min_node = node;
    	}
    	size++;
    	num_of_trees++;
    	return node;
    }

    
   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    * 
    * O(n) complexity w.c - using the functions reset, empty, connect  (which are all O(1) and consolidating
    * which is O(n) complexity w.c) and amortized O(logn)
    *
    */
    public void deleteMin(){
    	if(empty()) // no HeapNodes in Heap - nothing to delete
    		return;
    	num_of_trees += min_node.rank - 1;
    	size --;
    	if(min_node.next == min_node && size == 0){ // one HeapNode in Heap - delete it
     		reset();
     		return;
     	}
     	if(min_node.next == min_node){ // one tree in heap, this tree has at least one child 
     		HeapNode x = min_node.child;
     		int count = min_node.rank;
     		min_node = x; 
     		for(int i = 1 ; i <= count; i++){ // all children become roots by itself
     			x.parent = null;
     			if(x.mark ==1){
     				x.mark = 0;
     				marked_cnt --;
     			}
     			if(x.key < min_node.key) // updating new minimum
     				min_node = x;
     			x = x.next;
     		}
     		return;
     	}
     	HeapNode x = min_node.next;  // more then one tree in Heap 
     	min_node.prev.next = x;
     	x.prev = min_node.prev;
     	if (min_node.child != null){ // min_node has children
     		HeapNode y = min_node.child;
     		for(int i = 1; i<=min_node.rank ; i++){ // while there are still children to connect to root list
     			y.parent = null;
     			if(y.mark ==1){
     				y.mark = 0;
     				marked_cnt --;
     			}
     			y = y.next;
     		}
     		connect(y,x); // connect children to root list
     	}
     	consolidating(x);
    }

    
   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    * 
    * O(1) complexity
    *
    */
    public HeapNode findMin(){
    	return min_node;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    * 
    * O(1) complexity using connect, findMin and empty - O(1)
    *
    */
    public void meld (FibonacciHeap heap2){
    	  if(heap2 == null || heap2.empty())
    		  return;
    	  if(empty())
    		  min_node = heap2.findMin();
    	  else{
    		  connect(min_node,heap2.findMin());
    		  if(min_node.key > heap2.findMin().key)
    			  min_node = heap2.findMin();
    	  }
    	  size += heap2.getSize();
    	  num_of_trees += heap2.getNum_of_trees();
    	  marked_cnt += heap2.getMarked_cnt();
    }
    

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    * 
    * O(1) complexity
    *   
    */
    public int size(){
    	return size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep(){
    	int[] arr = new int[42];
    	if (empty())
    		return arr;
    	HeapNode tmp=min_node;
    	arr[min_node.rank]++;
    	while (tmp.next!=min_node){
    		tmp=tmp.next;
    		arr[tmp.rank]++;
    	}
    	return arr;
    }

    
    /**
     * public void reset()
     *
     * resets the heap to an empty heap
     * 
     * O(1) complexity
     * 
     */
    public void reset(){
    	min_node=null;
    	size=0;
    	num_of_trees=0;
    	marked_cnt = 0;
    }
    
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    * 
    * O(n) complexity w.c - using decreaseKey - O(n) w.c and deleteMin O(n) w.c
    */
    public void delete(HeapNode x) {
    	if(x == null)
    		return;
    	decreaseKey(x,Integer.MAX_VALUE);
    	deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    * 
    * O(n) complexity w.c - using CascadingCut - O(n) complexity w.c
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	if (x== null)
    		return;
    	x.key=x.key-delta;
    	if (x.parent!=null){
    		if (x.key<x.parent.key)
    			cascadingCut(x,x.parent);
    	}
    	if (x.key<min_node.key)
    		min_node=x;
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    * 
    * O(1) complexity
    */
    public int potential() {    
    	return num_of_trees +2*marked_cnt;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    * 
    * O(1) complexity
    */
    public static int totalLinks(){    
    	return total_links;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods).
    * 
    *  O(1) complexity
    */
    public static int totalCuts(){    
    	return total_cuts;
    }
    
    
    /**
     * public static void connect(HeapNode n1, HeapNode n2)
     * n1 - min_node of the heap
     * n2 - other node
     * 
     * O(1) complexity
     * 
     */
    public static void connect(HeapNode n1, HeapNode n2){
    	 HeapNode temp = n1.next;
         n1.next = n2.next;
         n1.next.prev = n1;
         n2.next = temp;
         n2.next.prev = n2;
    }
    /**
     * private void cascadingCut(HeapNode x,HeapNode y)
     * gets a node x and it's parent y and cuts the connection between them two and makes x a root
     * if needed the function runs recursively according to the marked nodes and cuts some more connections
     * 
     * O(n) complexity w.c - using cut - O(1) complexity
     *
     */
    private void cascadingCut(HeapNode x,HeapNode y){
    	cut(x,y);
    	if (y.parent!=null){
    		if (y.mark==0){
    			y.mark=1;
    			marked_cnt++;
    		}
    		else
    			cascadingCut(y,y.parent);
    	}
    }
    
    /**
     * private void cut(HeapNode x,HeapNode y)
     * help function to cascading cut - gets two nodes x and y and cuts the connection between them two
     * 
     * O(1) complexity - uses the function connect - O(1) complexity
     */
    private void cut(HeapNode x,HeapNode y){
    	total_cuts++;
    	num_of_trees++;
    	x.parent=null;
    	if (x.mark==1){
    		x.mark=0;
    		marked_cnt--;
    	}
    	y.rank--;
    	if (x.next==x)
    		y.child=null;
    	else{
    		y.child=x.next;
    		x.prev.next=x.next;
    		x.next.prev=x.prev;
    		x.next = x;
    		x.prev = x;
    	}
    	connect(x,min_node);
    }
    
    
    /**
     * private void Consolidating()
     * 
     * the function makes the successive linking / consolidating progress
     * turns the FibonacciHeap into a heap, which contains at most
     * one tree of each rank
     * 
     * Complexity - w.c. - O(n) , amortized - O(logn)
     * using the functions toBuckets(O(n)) and fromBuckets(O(logn)), removeFromRootList (O(1)), connect (O(1))
     */
    private void consolidating(HeapNode node){
    	toBuckets(node);
    }
    
    
    /**
     * private void toBuckets()
     * 
     * the function puts the trees into an array in order to make
     * the successive linking progress 
     * 
     * O(n) complexity w.c when n represents the nodes in the heap.
     * using the functions fromBuckets(O(logn)), removeFromRootList (O(1)), connect (O(1))
     */
    private void toBuckets(HeapNode node){
    	HeapNode [] buckets = new HeapNode[(int) (Math.ceil((Math.log(size) / Math.log(PHI)))+1)]; // maximal rank of heap with size - size
    	HeapNode x = node;
    	HeapNode y;
    	int count = num_of_trees;
    	for(int i =1; i <= count; i++){ //  while there more roots
    		y = x;
    		x = x.next;
    		removeFromRootList(y);
    		while(buckets[y.rank] != null){ // there is already one tree of same rank
    			int check = 0;
    			if(y.key >= buckets[y.rank].key)	// flag to know who will be the common root
    				check = 1;
    			link(y,buckets[y.rank]); // making the linking progress
    			if(check == 1)
    				y = buckets[y.rank];
    			buckets[y.rank-1] = null; // clear the cell
    		}
    		buckets[y.rank] = y; // put the linked tree in the next rank cell
    	}
    	fromBuckets(buckets); 
    }
    
    
    /**
     * private void fromBuckets(HeapNode [] buckets)
     * 
     * the function connects the linked trees into one heap
     * 
     * Complexity- O(logn)
     * using the functions removeFromRootList (O(1)), connect (O(1))
     */
    private void fromBuckets(HeapNode [] buckets){
    	min_node = new HeapNode(Integer.MAX_VALUE); //to update new minimum
    	HeapNode x = null;
    	for(HeapNode node : buckets){ //running on all cells
    		if(node != null){ // there is a tree
    			if(node.key < min_node.key) // looking for new minimum
    				min_node = node;
    			if(x==null){ //the first connected tree
    				x = node;
    				x.next = x;
    				x.prev = x;
    			}
    			else // there were be connected trees already, connect to them
    				connect(x,node);
    		}
    	}
    }
    
    
    /**
     * private void link(HeapNode h1, HeapNode h2)
     * 
     * the function links two trees with same rank
     * the tree with lower root key will be the father of the second tree root
     * 
     * Complexity- O(1), uses the function insertNext - O(1) complexity
     */
    private void link(HeapNode h1, HeapNode h2){
    	total_links++;
    	if(h1.key < h2.key){ // checks who will be the common father 
    		if(h1.child != null)
    			insertNext(h1.child,h2); // now a child
    		h2.parent = h1;
    		h1.child = h2;
    		h1.rank++; // fathers rank increased by one
    	}
    	else{
    		if(h2.child != null)
    			insertNext(h2.child,h1);
    		h1.parent = h2;
    		h2.child = h1;
    		h2.rank++;
    	}
    	num_of_trees--; // two trees were linked into one 
    }
    
    /**
     * private void insertNext(HeapNode h1, HeapNode h2)
     * 
     * the function inserts the most left child to the root
     * 
     * Complexity- O(1)
     */
    public void insertNext(HeapNode h1, HeapNode h2){
    	h2.prev = h1.prev;
    	h1.prev.next = h2;
    	h2.next = h1;
    	h1.prev = h2;
    }
    
    /**
     * private void removeFromRootList(HeapNode x)
     * 
     * the function removes the given node from the root list
     * 
     * Complexity- O(1)
     */
    private void removeFromRootList(HeapNode x){
    	HeapNode prev = x.prev;
        HeapNode next = x.next;
        prev.next = next;
        next.prev = prev;
        x.next = x;
        x.prev = x;
    }
    
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	
    	public Integer key;
    	public int rank;
    	public short mark;
    	public HeapNode child;
    	public HeapNode next;
    	public HeapNode prev;
    	public HeapNode parent;
    	
    	
    	public HeapNode(int k){
    		this.key = k;
    		this.prev = this;
    		this.next = this;
    	}
    	
    	/**
 	    * public int getKey()
 	    *
 	    * returns the node's key
 	    * 
 	    * Complexity- O(1)
 	    */
    	public int getKey() {
    	    return this.key;
    	}
  	
    }
}
