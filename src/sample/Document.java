package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

import java.util.*;


/**
 * this class is created to represent document
 */





public class Document {

    private String path;
    private String originalDocID;
    private String title;
    private String Text;
    private int ourDocIDInt;
    private String ourDocID;
    private int numOfTerms;
    private int  maxFreqTerm;
    private int numOfWordInDoc;
    private String maxTerm;
    private String docLanguage;
    private Map<String,Integer> celebs;
    private javafx.scene.control.Button Show;


    public Document(String docID, String title, String text) {
        this.originalDocID = docID;
        this.title = title;
        Text = text;
    }

    public Document() {
    }

    public Document(Button show) {
        this.Show = show;

    }

    public void setShow(Button show) {
        Show = show;
    }

    public Button getShow() {
        return Show;
    }


    public ObservableList<CelebFreq> getEntities(){
        HashMap<String,CelebFreq> celebFreqHashMap = getCelebFreq();
        Map<String, CelebFreq> sorted = sortMapByFreq(celebFreqHashMap);
        ObservableList<CelebFreq> celebs = FXCollections.observableArrayList();



        for (String celeb : sorted.keySet()) {
            celebs.add(celebFreqHashMap.get(celeb));
        }
        return celebs;
    }

    private HashMap<String, CelebFreq> getCelebFreq() {
        HashMap<String,CelebFreq> celebFreqHashMap = new HashMap<>();

        HashMap<String,Integer> tempHash = new HashMap<>();
        tempHash.putAll(celebs);
        int index = 0 ;
        for (String celeb:celebs.keySet()) {
            CelebFreq temp = new CelebFreq();
            temp.setCelebTerm(celeb);
            temp.setFreq(tempHash.get(celeb));
            celebFreqHashMap.put(celeb,temp);
            index++;
            if(index>4){
                break;
            }
        }
        return celebFreqHashMap;
    }


    public Map<String, Integer> getCelebs() {
        return celebs;
    }

    public void setCelebs(Map<String, Integer> celebs) {
        this.celebs = celebs;
    }

    public void setOurDocID(String ourDocID) {
        this.ourDocID = ourDocID;
    }


    public String getOriginalDocID() {
        return originalDocID;
    }

    public void setOriginalDocID(String originalDocID) {
        this.originalDocID = originalDocID;
    }

    public String getDocLanguage() {
        return docLanguage;
    }

    public void setDocLanguage(String docLanguage) {
        this.docLanguage = docLanguage;
    }

    public int getNumOfWordInDoc() {
        return numOfWordInDoc;
    }

    public void setNumOfWordInDoc(int numOfWordInDoc) {
        this.numOfWordInDoc = numOfWordInDoc;
    }



    public String getMaxTerm() {
        return maxTerm;
    }

    public void setMaxTerm(String maxTerm) {
        this.maxTerm = maxTerm;
    }

    public int getMaxFreqTerm() {
        return maxFreqTerm;
    }

    public void setMaxFreqTerm(int maxFreqTerm) {
        this.maxFreqTerm = maxFreqTerm;
    }

    public static TreeMap<String, CelebFreq> sortMapByFreq(HashMap<String, CelebFreq> map) {
        Comparator<String> comparator = new ValueComparatorCelebFreq(map);

        TreeMap<String, CelebFreq> result = new TreeMap<String, CelebFreq>(comparator);
        result.putAll(map);
        return result;
    }






    /**
     * setter of length of the doc (words)
     * @param numOfTotalWords - num of words inside the doc
     */
    public void setNumOfTotalWords(int numOfTotalWords) {
        this.numOfWordInDoc = numOfTotalWords;
    }

    /**
     * getter of the language of the doc
     * @return - language
     */
    public String getLanguage() {
        return docLanguage;
    }

    /**
     * setter of the language of the doc
     * @param language - language
     */
    public void setLanguage(String language) {
        this.docLanguage = language;
    }


    /**
     *
     * @return the number of the unique terms in the doc
     */
    public int getNumOfTerms() {
        return numOfTerms;
    }

    /**
     *
     * @return the number of occurrences of term that occurred the most of the time
     */
    public int getMaxTermFreq() {
        return maxFreqTerm;
    }

    /**
     *
     * @return getter of length of the doc (words)
     */
    public int getNumOfTotalWords() {
        return numOfWordInDoc;
    }


    /**
     *
     * @return the id of the doc (that one we gave)
     */
    public int getOurDocID() {
        return ourDocIDInt;
    }

    /**
     *
     * @param ourDocID
     */
    public void setOurDocID(int ourDocID) {
        this.ourDocIDInt = ourDocID;
    }

    /**
     *
     * @return the title of the doc
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter of the title of the doc
     * @param title - title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return the text of the doc (tag of text)
     */
    public String getText() {
        return Text;
    }

    /**
     * setter of the text
     * @param text
     */
    public void setText(String text) {
        Text = text;
    }

    /**
     * setter of the num of unique terms of the doc
     * @param numOfTerms
     */
    public void setNumOfTerms(int numOfTerms) {
        this.numOfTerms = numOfTerms;
    }

    /**
     * setter of the num of the term the occurred the most of the times
     * @param maxTermFreq
     */
    public void setMaxTermFreq(int maxTermFreq) {
        this.maxFreqTerm = maxTermFreq;
    }

    /**
     *
     * @return the actual content of the term that occurred the most
     */
    public String getTermWithMaxFreq() {
        return maxTerm;
    }

    /**
     * setter of the term that occurred the most
     * @param termWithMaxFreq
     */
    public void setTermWithMaxFreq(String termWithMaxFreq) {
        this.maxTerm = termWithMaxFreq;
    }

    public String getDocID() {
        return originalDocID;
    }

    public void setDocID(String docID) {
        this.originalDocID = docID;
    }
}


class ValueComparatorCelebFreq implements Comparator<String>{

    Map<String, CelebFreq> map = new HashMap<String, CelebFreq>();

    public ValueComparatorCelebFreq(Map<String, CelebFreq> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1).getFreq() >= map.get(s2).getFreq()){
            return -1;
        }else{
            return 1;
        }
    }
}
