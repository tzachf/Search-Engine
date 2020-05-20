package sample;

import java.util.HashMap;

public class Posting {

    private int totalFreqInCorpus;


    private HashMap<Integer,Integer> id_freq;
    private int celebDocID;


    public HashMap<Integer, Integer> getId_freq() {
        return id_freq;
    }


//    public void setCelebDocID(int celebDocID) {
//        this.celebDocID = celebDocID;
//    }

    public int getCelebDocID() {
        return celebDocID;
    }


    public int getNumOfDocsFreq() {
        return id_freq.size();
    }
    // private HashMap<Integer,Integer> freqInDocByID;


    public Posting(int idDoc) {

        id_freq = new HashMap<>();
        this.totalFreqInCorpus = 1;
        id_freq.put(idDoc,1);
    }


    public int getTotalFreqInCorpus() {
        return totalFreqInCorpus;
    }

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
