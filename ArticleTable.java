/*
 * ArticleTable.java
 * 
 * Dumblist rewritten as a hashtable
 *
 * Author: Theresa McNeil (tnmcneil@bu.edu)
 * Date: March 24, 2014
 */

import java.util.Iterator;

public class ArticleTable implements Iterable<Article>{
    
    private final int SIZE = 2503; 
   
    Node [] Table = new Node [SIZE];
    
    public static class Node {
      public Article data;
      public Node next;
      
      public Node(Article data, Node n) {
         this.data = data;
         this.next = n;
      }
      
      public Node(Article data) {
         this(data, null);
      }
   }
    
    // print out a string representation of the Article Table
    
    public String toString(){
        String t = "";
        for(int i = 0; i < SIZE; i++){
            if(Table[i] != null){
                String s = Integer.toString(sfold(Table[i].data.getTitle(), SIZE));                
                t += "[" + s + "]->";
                for(Node q = Table[i]; q != null; q = q.next){
                    t += q.data.getTitle();
                }
                t += "\n";
            }
        }
        return t;
    }
    
    // insert a into the table using the title of a as the hash key
   
    public void insert(Article a){
       Table[hash(a.getTitle())] = insert(a, Table[hash(a.getTitle())]);
   }
   
   private Node insert(Article a, Node p){
       Node q = new Node(a, p);
       return q;
   }
   
   // deletes a title from the table
   
   public void delete(String title){
       Table[hash(title)] = delete(title, Table[hash(title)]);
   }
   
   private Node delete(String title, Node p){
       for(Node q = p; q != null; q = q.next){
           if(title.equals(q.data.getTitle())){
               if(q.next == null)
                   return null;
               else
                   q = q.next;
           }
       }
       return p;
   }
   
   public boolean member(String title){
       return(member(title, Table[hash(title)]));
   }
   
   private boolean member(String title, Node q){
       for(Node p = q; p != null; p = p.next){
           if(p.data.getTitle().compareTo(title) == 0)
               return true;
       }
       return false;
   }
   
   public Article lookup(String title){
       return lookup(title, Table[hash(title)]);      
   }
   
   private Article lookup(String title, Node q){
       if(member(title)){
           for(Node p = q; p != null; p = p.next){           
               if(p.data.getTitle().equals(title))
                   return new Article(p.data.getTitle(), p.data.getBody());
               else
                   return null;
           }
           return null;
       }
       else
           return null;
   }
   
   public int getSize(){
       return SIZE;
   }
   
   public void initialize(Article[] A) {
      for(int i = 0; i < A.length; ++i) 
         insert(A[i]); 
   }
    
    public Iterator<Article> iterator(){
        return new It();
    }
    
    private class It implements Iterator<Article> {
        private int cursor;
        private Node head;
        
        public It(){
            for(; cursor < SIZE; cursor++){
                if(Table[cursor] != null){
                    head = Table[cursor];
                    break;
                }
            }
        }
    
        public boolean hasNext(){
            return head != null;
        }
    
        public Article next(){
            Node temp = head;
            head = head.next;
            if(head == null){
                cursor++;
                for(; cursor < SIZE; cursor++){
                    if(Table[cursor] != null){
                        head = Table[cursor];
                        break;
                    }
                }
            }
            return temp.data;
        }
        
        public void remove(){
        }
    }                             
   
   // hash function for strings
   
   int sfold(String s, int M){
       int intLength = s.length() / 4;
       int sum = 0;
       for(int j = 0; j < intLength; j ++){
           char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
           int mult = 1;
           for(int k = 0; k < c.length; k++){
               sum += c[k] * mult;
               mult *= 256;
           }
       }
       char c[] = s.substring(intLength * 4).toCharArray();
       int mult = 1;
       for(int k = 0; k < c.length; k++){
           sum += c[k] * mult;
           mult *= 256;
       }
       return (Math.abs(sum) % M);
   }
   
   int hash(String s){
       int k = sfold(s, SIZE);
       return(k % SIZE);
   }
   
   // Unit Tests
   
   public static void main(String[] args){
       
       System.out.println("Unit Test for Article Table");
       
       ArticleTable Table = new ArticleTable();
       
       ArticleTable TestTable = new ArticleTable();
       
       Iterator<Article> it = Table.iterator();
       
       // don't change next 2 lines
       
       String dbPath = "articles/";
       DatabaseIterator db = new DatabaseIterator(dbPath);
       
       while(db.hasNext()){
           Article a = db.next();
           Table.insert(a);
           //System.out.println(a);
       }
       
       //System.out.println(Table);
       
       Article ocean = new Article("ocean", "the ocean is blue");
       Article California = new Article("California", "California is on the west coast");
       Article coast = new Article("coast", "the coast is near the ocean");
       
       System.out.println("Testing insert...");
       
       TestTable.insert(ocean);
       
       System.err.println("inserting ocean, should give you \n[1163]->ocean");
       System.out.println(TestTable.toString());       
       
       TestTable.insert(California);
       TestTable.insert(coast);
       
       System.err.println("inserting California and coast, should give you");
       System.err.println("[329]->coast \n[1163]->ocean \n[1594]->California");
       System.out.println(TestTable.toString());
       
       System.out.println("Testing delete...");
       
       TestTable.delete("California");
       
       System.err.println("deleting California, new ArticleTable is");
       System.err.println("[329]->coast \n[1163]->ocean");
       System.out.println(TestTable);
       
       System.out.println("Testing member...");
       
       System.err.println("member(\"ocean\") should return \ntrue");
       System.out.println(TestTable.member("ocean"));
       
       System.err.println("\nmember(\"California\") should return \nfalse");
       System.out.println(TestTable.member("California"));
       
       System.out.println("\nTesting lookup...");
       
       System.err.println("\nlooking up \"ocean\" should return");
       System.err.println("ocean \n===== \nthe ocean is blue");
       System.out.println(TestTable.lookup("ocean"));
       
       System.err.println("looking up \"California\" should return");
       System.err.println("null");
       System.out.println(TestTable.lookup("California"));              
   }   
}
