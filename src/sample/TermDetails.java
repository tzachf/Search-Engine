package sample;

import java.util.HashMap;


/**
 * this class is created to provide more information about each term compare to all the docs
 */
public class TermDetails {

    private int totalFreqInCorpus;
    private HashMap<Integer,Integer> id_freq;


    public TermDetails(int idDoc) {

        id_freq = new HashMap<>();
        this.totalFreqInCorpus = 1;
        id_freq.put(idDoc,1);
    }


    /**
     *
     * @return the hash map that contains pairs of < id of doc , the num of occurrence in specific doc >
     */
    public HashMap<Integer, Integer> getId_freq() {
        return id_freq;
    }

    /**
     *
     * @return the num of occurrence in different docs
     */
    public int getNumOfDocsFreq() {
        return id_freq.size();
    }

    /**
     *
     * @return the num of occurrence in all the corpus
     */
    public int getTotalFreqInCorpus() {
        return totalFreqInCorpus;
    }


    /**
     * adding occurrences of specific term in different docs
     * @param idDoc
     */
    public void addToPost(int idDoc){
        if(!id_freq.containsKey(idDoc)){
            id_freq.put(idDoc,1);                                           /**first time in doc*/
            totalFreqInCorpus++;
        }else {
            id_freq.replace(idDoc, id_freq.get(idDoc) + 1);
            totalFreqInCorpus++;
        }
    }


}
