package sample;
import com.medallia.word2vec.Word2VecModel;
import java.io.*;
import java.util.*;

/**
 * this class handle the searching process, the class gets a query
 * and search all documents having appearances of query's terms in them
 * the class uses Ranker class to rank all documents and get to 50 most relevant.
 */

public class Searcher {
    //fields
    private Ranker ranker;
    private HashMap <String,String> postingByFirstChar;
    private HashMap <String, HashMap<String,String>> relvantDocsBeforeRanker;
    private List<String> relevantDocsSorted;
    private Map<String, Map<String,Integer>> celebsInDocs;
    private int numOfTermInQuery;
    private HashMap <Integer,List<String>> resultsByQuery;
    private HashMap <Integer,Map<String, Map<String,Integer>>> celebsByQuery;
    private boolean stem;
    private boolean semantic;
    private String query;

    //Constructors
    public Searcher() {
        ranker = new Ranker();
        resultsByQuery=new HashMap<>();
        celebsByQuery=new HashMap<>();
        relvantDocsBeforeRanker = new HashMap<>();
        postingByFirstChar = new HashMap();
        postingByFirstChar.put("A","A");postingByFirstChar.put("a","A");postingByFirstChar.put("B","B");postingByFirstChar.put("b","B");postingByFirstChar.put("C","C");postingByFirstChar.put("c","C");postingByFirstChar.put("d","D");postingByFirstChar.put("D","D");postingByFirstChar.put("E","E");postingByFirstChar.put("e","E");postingByFirstChar.put("F","F");postingByFirstChar.put("f","F");
        postingByFirstChar.put("G","G");postingByFirstChar.put("g","G");postingByFirstChar.put("H","H");postingByFirstChar.put("h","H");postingByFirstChar.put("I","I");postingByFirstChar.put("i","I");postingByFirstChar.put("J","J");postingByFirstChar.put("j","J");postingByFirstChar.put("K","K");postingByFirstChar.put("k","K");postingByFirstChar.put("L","L");postingByFirstChar.put("l","L");
        postingByFirstChar.put("M","M");postingByFirstChar.put("m","M");postingByFirstChar.put("N","N");postingByFirstChar.put("n","N");postingByFirstChar.put("O","O");postingByFirstChar.put("o","O");postingByFirstChar.put("P","P");postingByFirstChar.put("p","P");postingByFirstChar.put("Q","Q");postingByFirstChar.put("q","Q");postingByFirstChar.put("R","R");postingByFirstChar.put("r","R");
        postingByFirstChar.put("S","S");postingByFirstChar.put("s","S");postingByFirstChar.put("t","T");postingByFirstChar.put("T","T");postingByFirstChar.put("U","U");postingByFirstChar.put("u","U");postingByFirstChar.put("V","V");postingByFirstChar.put("v","V");postingByFirstChar.put("W","W");postingByFirstChar.put("w","W");postingByFirstChar.put("X","X");postingByFirstChar.put("x","X");
        postingByFirstChar.put("Y","Y");postingByFirstChar.put("y","Y");postingByFirstChar.put("Z","Z");postingByFirstChar.put("z","Z");postingByFirstChar.put("0","0-9");postingByFirstChar.put("1","0-9");postingByFirstChar.put("2","0-9");postingByFirstChar.put("3","0-9");postingByFirstChar.put("4","0-9");postingByFirstChar.put("5","0-9");postingByFirstChar.put("6","0-9");postingByFirstChar.put("7","0-9");
        postingByFirstChar.put("8","0-9");postingByFirstChar.put("9","0-9");
        relevantDocsSorted=new LinkedList<>();
        celebsInDocs =new HashMap<>();
    }

    //getters
    public HashMap<Integer, List<String>> getResultsByQuery() {
        return resultsByQuery;
    }

    public HashMap<Integer, Map<String, Map<String, Integer>>> getCelebsByQuery() {
        return celebsByQuery;
    }

    /**
     * this func set the stem and semantic boolean fields according to
     * user's choice in GUI
     * @param semantic - as the user choose the start with or without semantic process
     * @param stem        - as the user choose the start with or without stemmer process
     */
    private void setBool(String stem,String semantic){
        if(stem!=null){
            if(stem.equals("ON")){
                this.stem = true;
                ranker.setStem(true);
            }else{
                ranker.setStem(false);
                this.stem=false;
            }
        }else {
            ranker.setStem(false);
            this.stem=false;
        }
        if(semantic!=null){
            if(semantic.equals("ON")){
                this.semantic = true;
                ranker.setSemantic(true);
            }else{
                ranker.setSemantic(false);
                this.semantic=false;
            }
        }else {
            ranker.setSemantic(false);
            this.semantic=false;
        }
    }

    /**
     * this func add semantic words to the query according to the words in the query
     * @param query - query
     * @return HashSet with all words add to the initial query
     */
    public HashSet<String> getQueryWithSemantic(String query){
        HashSet <String> semanticWords = new HashSet<>();
        String [] querySpilt = query.split(" ");
        for (String term :querySpilt) {
            List<String> sem = getSemanticWords(term.toLowerCase());
            for (String str: sem) {
                String strStartsWithUpperCase = str.substring(0,1).toUpperCase()+ str.substring(1);
                if(!query.contains(str)&&!query.contains(strStartsWithUpperCase)) {
                    semanticWords.add(str);
                    query += " " + str;
                }
            }
        }
        this.query=query;
        return semanticWords;
    }

    /**
     * this func search relevant docs for single query
     * @param queryNum - query ID
     * @param query - query
     * @param stem - as the user choose the start with or without stemmer process
     * @param semantic - as the user choose the start with or without semantic process
     * @param postingFilePath - path to the postingFiles
     * @param pathToResults - path to wished dir for result file
     * @return - search accomplished or not
     */
    public boolean searchDocsByQuery(int queryNum, String query, String stem, String semantic, String postingFilePath,String pathToResults){
        setBool(stem,semantic);
        ranker.setPostingFile(postingFilePath);
        this.query=query;
        if(this.semantic) {
            getQueryWithSemantic(query);
        }
        relvantDocsBeforeRanker = getRelevantDocsForQuery(this.query,stem,postingFilePath);
        if(relvantDocsBeforeRanker==null){
            return false;
        }
        if(this.semantic) {
            relevantDocsSorted = ranker.rank(queryNum, relvantDocsBeforeRanker, null, true, stem, numOfTermInQuery, getQueryWithSemantic(query),pathToResults);
        }else{
            relevantDocsSorted = ranker.rank(queryNum, relvantDocsBeforeRanker, null, false, stem, numOfTermInQuery, null,pathToResults);
        }
        resultsByQuery.put(queryNum,relevantDocsSorted);
        celebsByQuery.put(queryNum,ranker.addCelebToDocs(relevantDocsSorted));
        relvantDocsBeforeRanker.clear();
        return true;
    }

    /**
     * this func read the query file search relevant docs for each query in the query file
     * updates the relevant hashmap
     * @param filePath - path to the folder holding the query file
     * @param stem - as the user choose the start with or without stemmer process
     * @param semantic - as the user choose the start with or without semantic process
     * @param postingFilePath - path to the postingFiles
     * @param pathToResults - path to wished dir for result file
     * @return search accomplished or not
     */

    public boolean searchDocsByFile(String filePath,String stem, String semantic,String postingFilePath,String pathToResults){
        ranker.setPostingFile(postingFilePath);
        setBool(stem,semantic);
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bf = new BufferedReader(fileReader);
            String currLine = bf.readLine();
            while(currLine!=null){
                String query ="";
                String description = "";
                currLine=bf.readLine();
                currLine=bf.readLine();
                String[] sep = currLine.split("\\s");
                int queryNum = Integer.parseInt(sep[2]);
                currLine=bf.readLine();
                query=currLine.substring(8,currLine.length()-1);
                this.query = query;
                if(this.semantic) {
                    getQueryWithSemantic(query);
                }
                currLine=bf.readLine();
                currLine=bf.readLine();
                currLine=bf.readLine();
                while (!currLine.startsWith("<narr>")){
                    description+=currLine;
                    currLine=bf.readLine();
                }
                HashMap <String, HashMap<String,String>> relevantDocsForDesc = getRelevantDocsForQuery(description,stem,postingFilePath);
                HashMap <String, HashMap<String,String>> relevantDocsForQuery = getRelevantDocsForQuery(this.query,stem,postingFilePath);
                if(relevantDocsForDesc==null||relevantDocsForQuery==null){
                    return false;
                }
                if(this.semantic) {
                    relevantDocsSorted = ranker.rank(queryNum, relevantDocsForQuery, relevantDocsForDesc, true, stem, numOfTermInQuery, getQueryWithSemantic(query),pathToResults);
                }else {
                        relevantDocsSorted = ranker.rank(queryNum, relevantDocsForQuery, relevantDocsForDesc, false, stem, numOfTermInQuery, getQueryWithSemantic(query), pathToResults);
                }

                resultsByQuery.put(queryNum,relevantDocsSorted);
                celebsByQuery.put(queryNum,ranker.addCelebToDocs(relevantDocsSorted));
                System.out.println();
                while (!currLine.equals("<top>")){
                    currLine=bf.readLine();
                    if(currLine==null){
                        break;
                    }
                }
            }
            bf.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * this func search all docs having query's terms in them
     * @param query - query
     * @param stem - as the user choose the start with or without stemmer process
     * @param postingFilePath - path to the postingFiles
     * @return HashMap holding docID as ID with value of HashMap holding Term as key and freq of term in doc as value
     */
    public HashMap <String, HashMap<String,String>> getRelevantDocsForQuery(String query,String stem,String postingFilePath){

        HashMap<String,Integer> termNumOfDocs = new HashMap<>();
        ranker.setPostingFile(postingFilePath);
        Parse parse = new Parse();
        Document queryDoc = new Document();
        queryDoc.setText(query);
        if(stem!=null) {
            if (stem.equals("ON")) {
                parse.setStem(true);
            }
        }
        parse.parseDoc(queryDoc);
        System.out.println(query);
        HashMap<String,String> parsedQuery = new HashMap<>();
        HashMap<String,TermDetails> parsedTerms = parse.getTerms();
        HashMap<String,TermDetails> celebsQuery = parse.getCelebTerms();

        for (String key : parsedTerms.keySet()) {
            parsedQuery.put(key,key);
        }
        for (String key : celebsQuery.keySet()) {
            parsedQuery.put(key,key);
        }
        HashMap<String,String> word_pointer_Map = new HashMap<>();
        HashMap <String, HashMap<String,String>> returnVal = new HashMap<>();
        try {
            BufferedReader bf;
            FileReader fileReader;
            if (this.stem) {
                fileReader = new FileReader(postingFilePath + "\\dictionary.txt");
            } else {
                try {
                    fileReader = new FileReader(postingFilePath + "\\dictionaryNoStem.txt");
                }catch (Exception e){
                    return null;
                }
            }
            bf = new BufferedReader(fileReader);
            String line = bf.readLine();
            String[] lineSeperate = line.split("\\|");
            int counter = 0;
            while(line!=null){
                //System.out.println(lineSeperate[0]);
                if(parsedQuery.containsKey(lineSeperate[0].substring(0,lineSeperate[0].length()-1).toUpperCase())||parsedQuery.containsKey(lineSeperate[0].substring(0,lineSeperate[0].length()-1).toLowerCase())) {
                    word_pointer_Map.put(lineSeperate[0], lineSeperate[1]);
                    counter++;
                }
                if(counter==parsedQuery.size()){
                    break;
                }
                line = bf.readLine();
                if(line != null) {
                    lineSeperate = line.split("\\|");
                }
            }
            bf.close();
            fileReader.close();
            for (String s : word_pointer_Map.keySet()) {
                if (this.stem) {
                    fileReader = new FileReader(postingFilePath + "\\"+ postingByFirstChar.get(s.charAt(0)+"")+ ".txt");
                } else {
                    fileReader = new FileReader(postingFilePath + "\\"+postingByFirstChar.get(s.charAt(0)+"")+"_NoStem.txt");
                }
                bf = new BufferedReader(fileReader);
                long pointer =Long.parseLong(word_pointer_Map.get(s));
                String lineInPosting = bf.readLine();
                int charCounter = 0;
                while (pointer!=charCounter&&lineInPosting!=null){
                    charCounter+=lineInPosting.length()+1;
                    lineInPosting = bf.readLine();
                }
                String [] strSpilt = lineInPosting.split("\\|");
                String term = strSpilt[0].substring(0,strSpilt[0].length()-1);
                int numOfDocs= Integer.parseInt(strSpilt[3]);
                termNumOfDocs.put(term,numOfDocs);
                strSpilt[4]=strSpilt[4].substring(2);
                strSpilt = strSpilt[4].split("\\s+\\(");
                for (int i = 0; i < strSpilt.length; i++) {
                    String [] details = strSpilt[i].split(" ,");
                    String str = details[1].substring(0, details[1].length() - 1);
                    if(str.contains(")")){
                        str = str.substring(0,str.length()-1);
                    }
                    if(!returnVal.containsKey(details[0])) {
                        HashMap <String,String> hm = new HashMap<>();
                        hm.put(term, str);
                        returnVal.put(details[0], hm);
                    }else{
                        HashMap <String,String> hm = returnVal.get(details[0]);
                        hm.put(term, str);

                        returnVal.put(details[0], hm);
                    }
                }
            }
            bf.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ranker.setTermNumOfDocs(termNumOfDocs);
        numOfTermInQuery = word_pointer_Map.size();
        return returnVal;

    }

    /**
     * this func searches for semantic words for specific word
     * @param term - word
     * @return arraylist holding 3 semantic words
     */
    public ArrayList<String> getSemanticWords(String term){
        ArrayList <String> semantics = new ArrayList<>();
        try{
            Word2VecModel word2VecModel = Word2VecModel.fromTextFile(new File("resources\\word2vec.c.output.model.txt"));
            com.medallia.word2vec.Searcher sc = word2VecModel.forSearch();

            List<com.medallia.word2vec.Searcher.Match> results = sc.getMatches(term,3);

            for (com.medallia.word2vec.Searcher.Match m:results) {
                semantics.add(m.match());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.medallia.word2vec.Searcher.UnknownWordException e) {
        }
        return semantics;
    }

}