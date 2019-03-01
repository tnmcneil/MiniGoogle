/* File: ArticleHeap.java
 * Name: Theresa McNeil (tnmcneil@bu.edu)
 * Purpose: implements a PriorityQueue for articles ordered by cosine similarity
 * Date: 12.4.16
 */

public class ArticleHeap{
    
    private static final int SIZE = 2503;
    private int next = 0;
    private static Article[]A = new Article[SIZE];
    
    private void resize(){
        Article [] B = new Article[A.length*2];
        for(int i = 0; i < A.length; i++)
            B[i] = A[i];
        A = B;
    }
    
    private int parent(int i) { return (i - 1) / 2; }
    private int lchild(int i) { return 2 * i + 1; }
    private int rchild(int i) { return 2 * i + 2; }
    
    private boolean isLeaf(int i) { return (lchild(i) >= next); }
    private boolean isRoot(int i) { return i == 0; }
    
    private void swap(int i, int j){
        Article temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }
    
    public boolean isEmpty(){
        return(next == 0);
    }
    
    public int size(){
        return next;
    }
    
    public boolean member(String title){
        int count = 0;
        for(int i = 0; i < next; i++){
            if(A[i].getTitle().equals(title))
                   count++;
        }
        if(count == 0)
            return false;
        else
            return true;
    }
    
    // insert an article into array at next available location, ordered by 
    // cosine similarity
    
    public void insert(Article a){
        if(size() == A.length) resize();
        A[next] = a;
        
        int i = next;
        int p = parent(i);
        while(!isRoot(i) && A[i].getCS() > A[p].getCS()){
            swap(i,p);
            i = p;
            p = parent(i);
        }
        ++next;
    }
    
    // remove and return the Article in the heap with the largest cosine
    // similarity & replace with the last element in level order
    
    public Article getMax() throws HeapUnderflowException{
        if(isEmpty())
            throw new HeapUnderflowException("Heap is empty!");
        else{
            --next;
            swap(0, next);
            int i = 0;
            int mc = maxChild(i);
            while(!isLeaf(i) && A[i].getCS() < A[mc].getCS()){
                swap(i, mc);
                i = mc;
                mc = maxChild(i);
            }
            return A[next];
        }
    }
    
    // return index of Article with max cosineSimilarity of i
    // or -1 if i is a leaf node
    
    public int maxChild(int i){
        if(lchild(i) >= next)
            return -1;
        if(rchild(i) >= next)
            return lchild(i);
        else if(A[lchild(i)].getCS() > A[rchild(i)].getCS())
            return lchild(i);
      else
         return rchild(i); 
    }        
    
    public static void main (String [] args){
        System.out.println("Unit Test for Article Heap");
        
        ArticleHeap Heap = new ArticleHeap();
        
        System.err.println("Test 1, testing the empty heap. Should throw " +
                           "HeapUnderflowException");
        System.err.println("Calling getMax() on the empty ArticleHeap should" +
                           " return: ");
        System.err.println("Heap is empty! \nDone");
        
        try{
            Article a = Heap.getMax();
            System.out.println(a);
        }
        catch(HeapUnderflowException e){
            System.out.println(e.getMessage());
        }
        finally{
            System.out.println("Done");
        }
               
        Article A = new Article("A", "A A A");
        Article B = new Article("B", "B B B");
        Article AB = new Article("AB", "A B A B");
        Article ABC = new Article("ABC", "A B C");
        
        A.putCS(0.985);
        B.putCS(0.0);
        AB.putCS(0.5);
        ABC.putCS(0.624);
        
        System.err.println("\nInserting articles A, B, AB and ABC into " + 
                           "the Heap");
        System.err.println("A has CS = 0.985, B has CS = 0.0, AB has " +
                           "CS = 0.5 and ABC has CS = 0.624 (cosine " +
                           "similarity values chosen arbitrarily and assigned"+
                           " using putCS()");
        
        Heap.insert(A);
        Heap.insert(B);
        Heap.insert(AB);
        Heap.insert(ABC);
        
        System.err.println("\nTesting member");
        
        System.err.println("member(\"A\") should return true");
        System.out.println(Heap.member("A"));
        
        System.err.println("member(\"C\") should return false");
        System.out.println(Heap.member("C"));
        
        System.err.println("\nTesting getMax()");
        System.err.println("Should return the Articles in the following " +
                           "order: \nA, ABC, AB, B, and then throw the " +
                           "HeapUnderflowException");
        
        try{
            System.out.print(Heap.getMax());
            System.out.print(Heap.getMax());
            System.out.print(Heap.getMax());
            System.out.print(Heap.getMax());
            System.out.print(Heap.getMax());
        }
        catch(HeapUnderflowException e){
            System.out.println(e.getMessage());
        }
        finally{
            System.out.println("Done");
        }
        
    }
}

class HeapUnderflowException extends Exception{
    public String text;
    public HeapUnderflowException(String text){
        super(text);
    }
}