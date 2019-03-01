/* File: MiniGoogle.java
 * Name: Theresa McNeil (tnmcneil@bu.edu)
 * Purpose: a program that allows the user to search the text of an article
 *          by keywords using cosine similarity
 * Date: 12.3.16
 */ 

import java.util.*;

public class MiniGoogle{
    
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
    
    private static Article[] getArticleList(DatabaseIterator db){
        
        int numArticles = db.getNumArticles();
        
        Article[] list = new Article [numArticles];
        for(int i = 0; i < numArticles; i++)
            list[i] = db.next();
        return list;
    }       
    
    private static DatabaseIterator setupDatabase(String path){
        return new DatabaseIterator(path);
    }
    
    private static void addArticle(Scanner s, ArticleTable T){
        System.out.println();
        System.out.println("Add an article");
        System.out.println("=================");
        
        System.out.println("Enter article title:");
        String title = s.nextLine();
        
        System.out.println("You may now enter the body of the article.");
        System.out.println("Press return two times when you are done.");
        
        String body = "";
        String line = "";
        do{
            line = s.nextLine();
            body += line + "\n";
        } while(!line.equals(""));
        
        T.insert(new Article(title, body));
    }
    
    private static void removeArticle(Scanner s, ArticleTable T){
        System.out.println();
        System.out.println("Remove an article");
        System.out.println("=====================");
        
        System.out.println("Enter article title:");
        String title = s.nextLine();
        
        T.delete(title);
    }    
    
    private static void titleSearch(Scanner s, ArticleTable T){
        System.out.println();
        System.out.println("Search by article title");
        System.out.println("===========================");
        
        System.out.println("Enter article title:");
        String title = s.nextLine();
        
        Article a = T.lookup(title);
        if(a != null)
            System.out.println(a);
        else{
            System.out.println("Article not found!");
            return ;
        }
        
        System.out.println("Press return when finished reading.");
        s.nextLine();
    }       
    
    // changes the input String to all lowercase and removes all characters
    // except for letters, digits, and whitespace
    
    private static String preprocess(String s){
        char[] charsToRemove = {'.',',',':',';','!','?','"','\'','/','-','(',
            ')','~'};
        s = s.toLowerCase();
        for(int i = 0; i < charsToRemove.length; i++){
            String punctuationString = Character.toString(charsToRemove[i]);
            s = s.replace(punctuationString, " ");
        }                
        return s;
    }
    
    // determine if the input String is a member of the blackList
    // note: only works for one word Strings at a time
    
    private static boolean blacklisted(String s){
        int count = 0;
        for(int i = 0; i < blackList.length; i++){
            if(s.equals(blackList[i]))
                count ++;
        }
        if(count == 0)
            return false;
        else
            return true;
    }
 
    private static double getCosineSimilarity(String s, String t){
        TermFrequencyTable TFT = new TermFrequencyTable();
        TFT.process(s, t);
        return TFT.cosineSimilarity();
    }
    
    // Search an ArticleTable for articles most similar to a phrase and return
    // the top three (ie: 3 with the highest cosine similarities)    
    
    public static String phraseSearch(String phrase, ArticleTable T){
        ArticleHeap H = new ArticleHeap();
        for(Article a : T){
            double cs = getCosineSimilarity(phrase, a.getBody());
            a.putCS(cs);
            if(cs > 0.001){
                H.insert(a);
            }
        }
        if(H.size() >= 3){
            try{
                Article a = H.getMax();
                Article b = H.getMax();
                Article c = H.getMax();
                String x = "\nMatch 1 with cosine similarity of " + a.getCS() + 
                    ":\n" + a;
                String y = "\nMatch 2 with cosine similarity of " + b.getCS() + 
                    ":\n" + b;
                String z = "\nMatch 3 with cosine similarity of " + c.getCS() + 
                    ":\n" + c;
                return "Top 3 Matches: \n \n" + x + y + z;
            }
            catch(HeapUnderflowException e){
                return e.getMessage();
            }
        }
        else if(H.size() == 2){
            try{
                Article a = H.getMax();
                Article b = H.getMax();
                String x = "Match 1 with cosine similarity of " + a.getCS() + 
                    ":\n" + a;
                String y = "Match 2 with cosine similarity of " + b.getCS() + 
                    ":\n" + b;
                return "Top 2 Matches: \n \n" + x + y;
            }
            catch(HeapUnderflowException e){
                return e.getMessage();
            }
        }
        else if(H.size() == 1){
            try{
                Article a = H.getMax();
                String x = "Match 1 with cosine similarit of " + a.getCS() + 
                    ":\n" + a;
                return "Top Match: \n \n" + x;
            }
            catch(HeapUnderflowException e){
                return e.getMessage();
            }            
        }
        else{
            return "There are no matching articles.";
        }        
    }
    
    private static void phraseSearch2(Scanner s, ArticleTable T){
        System.out.println();
        System.out.println("Search by article content");
        System.out.println("===========================");
        System.out.print("Enter search phrase:");
        String phrase = s.nextLine();             
        System.out.println(phraseSearch(phrase, T));
    }
    
    private final static String [] blackList = { "the", "of", "and", "a", "to", 
    "in", "is", "you", "that", "it", "he", "was", "for", "on", "are", "as", 
    "with", "his", "they", "i", "at", "be", "this", "have", "from", "or", "one", 
    "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", 
    "your", "can", "said", "there", "use", "an", "each", "which", "she", 
    "do", "how", "their", "if", "will", "up", "other", "about", "out", "many", 
    "then", "them", "these", "so", "some", "her", "would", "make", "like", 
    "him", "into", "time", "has", "look", "two", "more", "write", "go", "see", 
    "number", "no", "way", "could", "people",  "my", "than", "first", "water", 
    "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", 
    "did", "get", "come", "made", "may", "part" }; 
    
    public static void main (String [] args){                
        
        Scanner user = new Scanner(System.in);
        
        String dbPath = "articles/";
        
        DatabaseIterator db = setupDatabase(dbPath);
        
        System.out.println("Read " + db.getNumArticles() +
                            " articles from disk.");
        
        System.out.println("Created in-memory hash table of articles.");
        
        ArticleTable T = new ArticleTable();
        Article[] A = getArticleList(db);
        T.initialize(A);        
        
        
        int choice = -1;
        
        do{
            System.out.println();
            System.out.println("Welcome to Mini-Google!");
            System.out.println("=========================");
            System.out.println("Make a selection from the following options");
            System.out.println();            
            System.out.println("   1. Add a new article");
            System.out.println("   2. Remove an article");
            System.out.println("   3. Search by exact article title");
            System.out.println("   4. Search by phrase (list of keywords)");
            
            System.out.println("Enter a selection (1-4, or 0 to quit):");
            
            choice = user.nextInt();
            
            user.nextLine();
            
            switch(choice){
                case 0:
                    return;
                
                case 1:
                    addArticle(user, T);
                    break;
                
                case 2:
                    removeArticle(user, T);
                    break;
                    
                case 3:
                    titleSearch(user, T);
                    break;
                
                case 4:
                    phraseSearch2(user, T);
                    break;
                    
                default:
                    break;
                    
            }
            
            choice = -1;
            
        } while(choice < 0 || choice > 5);
    }
}
