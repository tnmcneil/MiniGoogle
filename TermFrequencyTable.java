/* File: TermFrequencyTable.java
 * Name: Theresa McNeil (tnmcneil@bu.edu
 * Purpose: calculates the Cosine Similarity of two doccuments by storing the
 *          words from both documents
 * Date: 12.3.16
 */

import java.util.*;

public class TermFrequencyTable{
    
    private final int SIZE = 103;
    
    private Node [] T = new Node[SIZE]; 
    
    public String toString(){
        int [] A = new int [SIZE];
        int [] B = new int [SIZE];
        for(int i = 0; i < SIZE; i++){
            for(Node p = T[i]; p != null; p = p.next){
                A[i] = p.termFreq[0];
                B[i] = p.termFreq[1];
            }
        }
        String s = "A: " + Arrays.toString(A);
        String t = "B: " + Arrays.toString(B);
        return s + "\n" + t;
    }
    
    private String toString2(){
        String A = "A: ";
        String B = "B: ";
        String total = "Total: ";
        for(int i = 0; i < SIZE; i ++){
            for(Node p = T[i]; p != null; p = p.next){
                A += "(" + p.term + "," + p.termFreq[0] + "), ";
                B += "(" + p.term + "," + p.termFreq[1] + "), ";
                total += p.term + ", ";
            }
        }
        return A + "\n" + B + "\n" + total;
    }
    
    // bucket node
    private class Node{
        String term;
        int[] termFreq = new int[2];
        Node next;
        
        private Node(String term, int docNum, Node n){
            this.term = term;
            this.next = n;
            this.termFreq[docNum] = 1;            
        }                        
    }
    
    // insert a term from a document docNum into the table; if the term is not
    // already in the table then add it to the table with a termFreq of 1 for
    // docNum; if the term is already in the table then just increment the
    // appropriate termFreq value
    
    public void insert(String term, int docNum){
        int count = 0;
        for(int i = 0; i < blackList.length; i++){
            if(term.equals(blackList[i]))
                count ++;
        }
        if(count == 0 && member(term, T[hash(term)]))
            T[hash(term)] = increment(term, docNum, T[hash(term)]);
        else if(count == 0 && !member(term, T[hash(term)]))
            T[hash(term)] = insert(term, docNum, T[hash(term)]);
    }
    
    private Node insert(String term, int docNum, Node p){
        return new Node(term, docNum, p);
    }
    
    private Node increment(String term, int docNum, Node p){
        for(; p != null; p = p.next){
            if(p.term.equals(term)){
                p.termFreq[docNum] ++;
                break;
            }
        }
        return p;
    }
    
    // returns the cosine similarity of the terms for the two documents stored
    // in this table
    
    public double cosineSimilarity(){
        double AdotB = 0.0;
        double lengthA = 0.0;
        double lengthB = 0.0;
        for(int i = 0; i < SIZE; i++){
            for(Node p = T[i]; p != null; p = p.next){
                AdotB += (p.termFreq[0] * p.termFreq[1]);
                lengthA += (p.termFreq[0] * p.termFreq[0]);
                lengthB += (p.termFreq[1] * p.termFreq[1]);
            }
        }
        lengthA = Math.sqrt(lengthA);
        lengthB = Math.sqrt(lengthB);
        double similarity = AdotB / (lengthA * lengthB);
        return similarity;
    }
    

    // creates a hash table containing every non-blacklist word in documents
    // A and B and how many times it occurs in the respective document
    // (frequency of the words in A is stored in the first slot in the arrays 
    // for each node, and the frequency of the words in B is stored in the 
    // second slot in the arrays for each node
    
    public void process(String A, String B){
        // remove punctuation from A
        char[] charsToRemove = {'.',',',':',';','!','?','"','\'','/','-','(',
            ')','~'};
        A = A.toLowerCase();
        for(int i = 0; i < charsToRemove.length; i++){
            String punctuationString = Character.toString(charsToRemove[i]);
            A = A.replace(punctuationString, " ");
        }
                
        // turn A into an array of Strings split on white space
        String [] arrayA = A.split("\\s+");        
        
        // insert each non blacklist word in the array into the term frequency 
        // table in the first slot in the array for each node
        for(int k = 0; k < arrayA.length; k++)
            insert(arrayA[k], 0);        
        
        // now repeat for B, storing in the second slot in the array in each node
        B = B.toLowerCase();
        for(int i = 0; i < charsToRemove.length; i++){
            String punctuationString = Character.toString(charsToRemove[i]);
            B = B.replace(punctuationString, "");
        }
        String [] arrayB = B.split("\\s+");
        for(int k = 0; k < arrayB.length; k++)
            insert(arrayB[k], 1);            
    }
    
    private boolean member(String term){
       return(member(term, T[hash(term)]));
   }
   
   private boolean member(String term, Node q){
       for(Node p = q; p != null; p = p.next){
           if(term.equals(p.term))
               return true;
       }
       return false;
   }   
   
   private int hash(int k){
        return(k * 7369) % SIZE;
    }
    
    private int sfold(String s, int M){
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
   
   private int hash(String s){
       int k = sfold(s, SIZE);
       return(k % SIZE);
   }
    
    private final String [] blackList = { "the", "of", "and", "a", "to", "in",
    "is", "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", 
    "his", "they", "i", "at", "be", "this", "have", "from", "or", "one", 
    "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", 
    "your", "can", "said", "there", "use", "an", "each", "which", "she", 
    "do", "how", "their", "if", "will", "up", "other", "about", "out", "many", 
    "then", "them", "these", "so", "some", "her", "would", "make", "like", 
    "him", "into", "time", "has", "look", "two", "more", "write", "go", "see", 
    "number", "no", "way", "could", "people",  "my", "than", "first", "water", 
    "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", 
    "did", "get", "come", "made", "may", "part" }; 
   
    
   public static void main (String[] args){
       System.out.println("Unit Test for Term Frequency Table");
       
       TermFrequencyTable Table1 = new TermFrequencyTable();
       TermFrequencyTable Table2 = new TermFrequencyTable();
       TermFrequencyTable Table3 = new TermFrequencyTable();
       
       String A = "The man with the hat ran up to the man with the dog.";
       String B = "A man with a hat approached a dog and a man.";
       
       String C = "ocean, OCEAN! Beach, BEAch, coast, California "
           + "california water, Sun, sand. coast sand sun";
       String D = "Ocean Beach Coast California Water Sun Sand";
       
       String E = "dogs are pets. so are cats and hampsters";
       
       Table1.process(A, B);
       
       System.err.println("\nCalculating cosine similarity of \n\""
                              + A + "\"\nand\n\"" + B + "\"");
       System.err.println("Should be 0.857142857142857");
       System.out.println(Table1.toString2());
       System.out.println(Table1.cosineSimilarity());       
       
       Table2.process(C, D);
       
       System.err.println("\nCalculating cosine similarity of \n\""
                              + C + "\"\nand\n\"" + D + "\"");
       System.err.println("Should be 1.0");
       System.out.println(Table2.toString2());
       System.out.println(Table2.cosineSimilarity());
       
       Table3.process(D, E);
       
       System.err.println("\nCalculating cosine similarity of \n\""
                              + D + "\"\nand\n\"" + E + "\"");
       System.err.println("Should be 0.0");
       System.out.println(Table3.toString2());
       System.out.println(Table3.cosineSimilarity());
       
   }      
       
}
    
    
    
        
    
    
            
            
            
            
            
            
            