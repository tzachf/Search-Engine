package sample;
import java.io.*;
import java.util.*;

public class Ranker {
    //fields
    private int numOfDocsInCorpus;
    private double avgNumOfWordsInDoc;
    private String postingFile;
    private double k;
    private double b;
    private boolean stem;
    private boolean semantic;
    private double queryW;
    private double descW;
    private double semanticWeight;
    private HashMap <String,Integer> termNumOfDocs;
    private HashMap <String,Document> docsMap;

    //Constructors
    public Ranker(){
        docsMap = new HashMap<>();
        termNumOfDocs=new HashMap<>();
        this.k = 0.1;
        this.b=0.89;
        queryW=1;
        descW = 0.56;
        semanticWeight = 0.05;
    }

    //setters
    public void setTermNumOfDocs(HashMap<String, Integer> termNumOfDocs) {
        this.termNumOfDocs = termNumOfDocs;
    }

    public void setPostingFile(String postingFile) {
        this.postingFile = postingFile;
    }

    public void setStem(boolean stem) {
        this.stem = stem;
    }

    public void setSemantic(boolean semantic) {
        this.semantic = semantic;
    }

    /**
     * this func rates all docs from searcher class using various rates
     * @param queryNum - query ID
     * @param relvantDocsBeforeRanker - HashMap holding docID as ID with value of HashMap holding Term as key and freq of term in doc as value
     * @param relvantDocsForDescription - HashMap holding docID as ID with value of HashMap holding Term as key and freq of term in doc as value
     * @param semantic - as the user choose the start with or without semantic process
     * @param stem - as the user choose the start with or without stemmer process
     * @param numOfTermsInQuery - num of terms in query after parse
     * @param semanticWords - HashSet with all words add to the initial query
     * @param pathToResults - path to wished dir for result file
     * @return list of 50 relevant doc's ID
     */
    public  List<String> rank(int queryNum, HashMap<String, HashMap<String, String>> relvantDocsBeforeRanker, HashMap<String, HashMap<String, String>> relvantDocsForDescription, Boolean semantic, String stem, double numOfTermsInQuery, HashSet <String> semanticWords,String pathToResults) {


        List<String> relevantDocs = new LinkedList<>();
        HashMap<String,Double[]> docRates = new HashMap<>();
        HashMap<String,Double> weightPerTerm = new HashMap<>();

        docsMap = getDocsDetails(relvantDocsBeforeRanker,relvantDocsForDescription,stem);
        double maxTermInTitle = 0;
        double maxquerySizePropotion =0;
        double maxTermFreqIncludedInQuery=0;
        double maxTermBM=0;
        double maxTermWordsPropotion = 0;

        for (String doc: relvantDocsBeforeRanker.keySet()) {
            int numOfWordsFromQueryInDocNw=relvantDocsBeforeRanker.get(doc).size();
            double BM25Rate=0;
            double maxTerm=0;
            double numOfWordsPropotion = 0 ;
            double termInTitle =0;
            docRates.put(docsMap.get(doc).getOriginalDocID(),new Double[10]);
            for (String term : relvantDocsBeforeRanker.get(doc).keySet()) {
                /**BM25*/
                int freq = Integer.parseInt(relvantDocsBeforeRanker.get(doc).get(term));
                double numerator = freq*(1+k);
                double denuminator = freq + k*((1-b)+((b*docsMap.get(doc).getNumOfWordInDoc())/(avgNumOfWordsInDoc)));
                double ans = (numerator/denuminator)*Math.log((numOfDocsInCorpus-termNumOfDocs.get(term)+b)/(termNumOfDocs.get(term)+b))/Math.log(2);
                if(semantic){
                    if(semanticWords.contains(term)) {
                        ans = ans * semanticWeight;
                    }
                }
                BM25Rate+=ans;
                ans=(double)freq/docsMap.get(doc).getNumOfTerms() + (double)freq/docsMap.get(doc).getNumOfWordInDoc();
                if(semantic){
                    if(semanticWords.contains(term)) {
                        ans = ans * semanticWeight;
                    }
                }
                numOfWordsPropotion = ans;

                if(docsMap.get(doc).getTitle().contains(term)){
                    if(term.contains(" ")){
                        if(semantic){
                            if(semanticWords.contains(term)) {
                                ans = 2 * semanticWeight;
                            }
                        }
                    }else {
                        if(semantic){
                            if(semanticWords.contains(term)) {
                                ans = 1 * semanticWeight;
                            }
                        }
                    }
                }else{
                    ans=0;
                }
                termInTitle+=ans;
                if(term.equals(docsMap.get(doc).getMaxTerm())){
                    if(semantic){
                        if(semanticWords.contains(term)) {
                            ans = 1 * semanticWeight;
                        }
                    }
                }else{
                    ans=0;
                }
                maxTerm+=ans;
            }
            double querySizePropotion =numOfWordsFromQueryInDocNw/numOfTermsInQuery;
            if(querySizePropotion>maxquerySizePropotion){
                maxquerySizePropotion=querySizePropotion;
            }
            if(BM25Rate > maxTermBM){
                maxTermBM = BM25Rate;
            }
            if(numOfWordsPropotion> maxTermWordsPropotion){
                maxTermWordsPropotion = numOfWordsPropotion;
            }
            if(maxTerm>maxTermFreqIncludedInQuery){
                maxTermFreqIncludedInQuery=maxTerm;
            }
            if(maxTermInTitle<termInTitle){
                maxTermInTitle=termInTitle;
            }
            Double[] testsResults = docRates.get(docsMap.get(doc).getOriginalDocID());

            testsResults[0] = BM25Rate;
            testsResults[1] = termInTitle;
            testsResults[2] = numOfWordsPropotion;
            testsResults[3] = maxTerm;
            testsResults[4] = querySizePropotion;

            docRates.replace(docsMap.get(doc).getOriginalDocID(),testsResults);
        }
        if(relvantDocsForDescription==null) {
            for (String doc : docRates.keySet()) {
                Double[] rates = docRates.get(doc);
                rates[0] = rates[0] / maxTermBM;
                if (maxTermInTitle == 0.0) {
                    rates[1] = 0.0;
                } else {
                    rates[1] = rates[1] / maxTermInTitle;
                }
                rates[2] = rates[2] / maxTermWordsPropotion;
                if (maxTermFreqIncludedInQuery == 0.0) {
                    rates[3] = 0.0;
                } else {
                    rates[3] = rates[3] / maxTermFreqIncludedInQuery;
                }
                rates[4] = rates[4] / maxquerySizePropotion;

            }

            HashMap<String, Double> docFinalRate = new HashMap<>();

            for (String doc : docRates.keySet()) {

                double BMw = 2.5;
                double termInTitleW = 0;
                if (maxTermInTitle > 0) {
                    termInTitleW = 0.2;
                }
                double termWordsPropotionW = 1.0;
                double maxTermW = 0;
                if (maxTermFreqIncludedInQuery > 0) {
                    maxTermW = 0.2;
                }
                double querySizePropotionW = 1.0;
                double totalW = BMw + termInTitleW + termWordsPropotionW + maxTermW + querySizePropotionW;

                double finalRate = (BMw * docRates.get(doc)[0] + termInTitleW * docRates.get(doc)[1] + termWordsPropotionW * docRates.get(doc)[2] + maxTermW * docRates.get(doc)[3] + querySizePropotionW * docRates.get(doc)[4]) / totalW;

                docFinalRate.put(doc, finalRate);
            }

            Map<String, Double> sorted = sortMapByValue(docFinalRate);
            HashMap<String, Double> temp = new HashMap<>();
            temp.putAll(sorted);


            File file = new File(pathToResults +"\\results.txt");
            try {
                FileWriter fw = new FileWriter(file, true);
                int index = 0;

                for (String doc : sorted.keySet()) {
                    if (index == 50) {
                        break;
                    }
                    ((LinkedList<String>) relevantDocs).addLast(doc);
                    fw.write(queryNum + " " + "0" + " " + doc + " " + docFinalRate.get(doc) + " " + "1.290" + " mt" + System.lineSeparator());
                    index++;
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return relevantDocs;
        }else{
            double maxTermInTitleDesc = 0;
            double maxquerySizePropotionDesc =0;
            double maxTermFreqIncludedInQueryDesc=0;
            double maxTermBMDesc=0;
            double maxTermWordsPropotionDesc = 0;
            for (String doc: relvantDocsForDescription.keySet()) {
                int numOfWordsFromQueryInDocNw=relvantDocsForDescription.get(doc).size();
                double BM25Rate=0;
                double maxTerm=0;
                double numOfWordsPropotion = 0 ;
                double termInTitle =0;
                if(!docRates.containsKey(docsMap.get(doc).getOriginalDocID())) {
                    docRates.put(docsMap.get(doc).getOriginalDocID(), new Double[10]);
                }
                for (String term : relvantDocsForDescription.get(doc).keySet()) {
                    /**BM25*/
                    int freq = Integer.parseInt(relvantDocsForDescription.get(doc).get(term));
                    double numerator = freq*(1+k);
                    double denuminator = freq + k*((1-b)+(b*docsMap.get(doc).getNumOfWordInDoc())/(avgNumOfWordsInDoc));
                    double ans = (numerator/denuminator)*Math.log((numOfDocsInCorpus-numOfWordsFromQueryInDocNw+b)/(numOfWordsFromQueryInDocNw+b))/Math.log(1.7);
                    BM25Rate+=ans;
                    numOfWordsPropotion = (double)freq/docsMap.get(doc).getNumOfTerms() + (double)freq/docsMap.get(doc).getNumOfWordInDoc();
                    if(docsMap.get(doc).getTitle().contains(term)){
                        if(term.contains(" ")){
                            termInTitle+=2;
                        }else {
                            termInTitle += 1;
                        }
                    }
                    if(term.equals(docsMap.get(doc).getMaxTerm())){
                        maxTerm+=1.0;
                    }
                }
                double querySizePropotion =numOfWordsFromQueryInDocNw/numOfTermsInQuery;
                if(querySizePropotion>maxquerySizePropotionDesc){
                    maxquerySizePropotionDesc=querySizePropotion;
                }
                if(BM25Rate > maxTermBMDesc){
                    maxTermBMDesc = BM25Rate;
                }
                if(numOfWordsPropotion> maxTermWordsPropotionDesc){
                    maxTermWordsPropotionDesc = numOfWordsPropotion;
                }
                if(maxTerm>maxTermFreqIncludedInQueryDesc){
                    maxTermFreqIncludedInQueryDesc=maxTerm;
                }
                if(maxTermInTitleDesc<termInTitle){
                    maxTermInTitleDesc=termInTitle;
                }
                Double[] testsResults = docRates.get(docsMap.get(doc).getOriginalDocID());
                testsResults[5] = BM25Rate;
                testsResults[6] = termInTitle;
                testsResults[7] = numOfWordsPropotion;
                testsResults[8] = maxTerm;
                testsResults[9] = querySizePropotion;

                docRates.replace(docsMap.get(doc).getOriginalDocID(),testsResults);
            }
            for (String doc : docRates.keySet()) {
                Double[] rates = docRates.get(doc);
                if(rates[0]!=null) {
                    rates[0] = rates[0] / maxTermBM;
                    if (maxTermInTitle == 0.0) {
                        rates[1] = 0.0;
                    } else {
                        rates[1] = rates[1] / maxTermInTitle;
                    }
                    rates[2] = rates[2] / maxTermWordsPropotion;
                    if (maxTermFreqIncludedInQuery == 0.0) {
                        rates[3] = 0.0;
                    } else {
                        rates[3] = rates[3] / maxTermFreqIncludedInQuery;
                    }
                    rates[4] = rates[4] / maxquerySizePropotion;
                }else{
                    rates[0]=0.0;
                    rates[1]=0.0;
                    rates[2]=0.0;
                    rates[3]=0.0;
                    rates[4]=0.0;
                }
                if(rates[5]!=null) {
                    rates[5] = rates[5] / maxTermBMDesc;
                    if (maxTermInTitleDesc == 0.0) {
                        rates[6] = 0.0;
                    } else {
                        rates[6] = rates[6] / maxTermInTitleDesc;
                    }
                    rates[7] = rates[7] / maxTermWordsPropotionDesc;
                    if (maxTermFreqIncludedInQueryDesc == 0.0) {
                        rates[8] = 0.0;
                    } else {
                        rates[8] = rates[8] / maxTermFreqIncludedInQueryDesc;
                    }
                    rates[9] = rates[9] / maxquerySizePropotionDesc;
                }else{
                    rates[5]=0.0;
                    rates[6]=0.0;
                    rates[7]=0.0;
                    rates[8]=0.0;
                    rates[9]=0.0;
                }
            }
            HashMap<String, Double> docFinalRate = new HashMap<>();
            for (String doc : docRates.keySet()) {

                double BMw = 2.5*queryW;
                double termInTitleW = 0;
                if (maxTermInTitle > 0) {
                    termInTitleW = 0.3*queryW;
                }
                double termWordsPropotionW = 1.0*queryW;
                double maxTermW = 0;
                if (maxTermFreqIncludedInQuery > 0) {
                    maxTermW = 0.2*queryW;
                }

                double querySizePropotionW = 1.0;
                double BMwDesc = 3.5*descW;
                double termInTitleWDesc = 0;
                if (maxTermInTitleDesc > 0) {
                    termInTitleW = 0.2*descW;
                }
                double termWordsPropotionWDesc = 1.0*descW;
                double maxTermWDesc = 0;
                if (maxTermFreqIncludedInQueryDesc > 0) {
                    maxTermW = 0.2*descW;
                }
                double querySizePropotionWDesc = 1.0*descW;
                double totalW = BMw + termInTitleW + termWordsPropotionW + maxTermW + querySizePropotionW + BMwDesc + termInTitleWDesc + termWordsPropotionW + maxTermWDesc + querySizePropotionWDesc;

                double finalRate = ((BMw * docRates.get(doc)[0] + termInTitleW * docRates.get(doc)[1] + termWordsPropotionW * docRates.get(doc)[2] + maxTermW * docRates.get(doc)[3] + querySizePropotionW * docRates.get(doc)[4])+(BMwDesc * docRates.get(doc)[5] + termInTitleWDesc * docRates.get(doc)[6] + termWordsPropotionWDesc * docRates.get(doc)[7] + maxTermWDesc * docRates.get(doc)[8] + querySizePropotionWDesc * docRates.get(doc)[9])) / totalW;

                docFinalRate.put(doc, finalRate);
            }

            Map<String, Double> sorted = sortMapByValue(docFinalRate);
            HashMap<String, Double> fiftyDocs = new HashMap<>();
            HashMap<String, Double> temp = new HashMap<>();
            temp.putAll(sorted);

            File file = new File(pathToResults +"\\results.txt");
            try {
                FileWriter fw = new FileWriter(file, true);
                int index = 0;

                for (String doc : sorted.keySet()) {
                    if (index == 50) {
                        break;
                    }

                    //fiftyDocs.put(doc,temp.get(doc));
                    ((LinkedList<String>) relevantDocs).addLast(doc);
                    // BigDecimal str = sorted.get(doc);
                    fw.write(queryNum + " " + "0" + " " + doc + " " + docFinalRate.get(doc) + " " + "1.290" + " mt" + System.lineSeparator());
                    index++;
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return relevantDocs;
        }
    }



    public HashMap<String, Map<String,Integer>> addCelebToDocs( List<String> sortedDocs) {

        HashMap<String, Map<String, Integer>> doc_Celeb_Map = new HashMap<>();
        for (String str : sortedDocs) {
            doc_Celeb_Map.put(str,new TreeMap<>());
        }
        try {
            FileReader fileReader = new FileReader(postingFile + "\\celebDictionary.txt");
            BufferedReader bf = new BufferedReader(fileReader);
            String line = bf.readLine();
            String[] lineSeperate = line.split("\\|");
            String celeb = lineSeperate[0];
            if(celeb.equals("A ")){
                line = bf.readLine();
                lineSeperate = line.split("\\|");
                celeb = lineSeperate[0];
            }
            String[] parentheses = lineSeperate[4].split("\\s+\\(");
            celeb = celeb.substring(0, celeb.length() - 1);

            while (line != null) {
                for (int i = 1; i < parentheses.length; i++) {
                    String[] details = parentheses[i].split(" ,");
                    String str = details[1].substring(0, details[1].length() - 1);
                    if (str.contains(")")||str.contains(") ")) {
                        str = str.substring(0, str.length() - 1);
                    }
                    if(docsMap.containsKey(details[0])) {
                        if (sortedDocs.contains(docsMap.get(details[0]).getOriginalDocID())) {
                            if (doc_Celeb_Map.containsKey(docsMap.get(details[0]).getOriginalDocID())) {
                                Map<String, Integer> temp = doc_Celeb_Map.get(docsMap.get(details[0]).getOriginalDocID());
                                temp.put(celeb, Integer.parseInt(str));
                                doc_Celeb_Map.replace(docsMap.get(details[0]).getOriginalDocID(), temp);
                            } else {
                                Map<String, Integer> temp = new HashMap<>();
                                temp.put(celeb, Integer.parseInt(str));
                                doc_Celeb_Map.put(docsMap.get(details[0]).getOriginalDocID(), temp);
                            }
                        }
                    }
                }
                line = bf.readLine();
                if(line!=null) {
                    lineSeperate = line.split("\\|");
                    celeb = lineSeperate[0];
                    parentheses = lineSeperate[4].split("\\s+\\(");
                    celeb = celeb.substring(0, celeb.length() - 1);
                }
            }

            for(String doc: doc_Celeb_Map.keySet()){
                TreeMap <String, Integer> temp = sortMapByValueInteger(doc_Celeb_Map.get(doc));
                doc_Celeb_Map.replace(doc,temp);

            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }



        return doc_Celeb_Map;
    }

    public static TreeMap<String, Double> sortMapByValue(HashMap<String, Double> map) {
        Comparator<String> comparator = new ValueComparatorDouble(map);

        TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
        result.putAll(map);
        return result;
    }
    public static TreeMap<String, Integer> sortMapByValueInteger(Map<String, Integer> map) {

        Comparator<String> comparator = new ValueComparatorInteger(map);

        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }
    private HashMap<String, Document> getDocsDetails(HashMap<String, HashMap<String, String>> relvantDocsBeforeRanker,HashMap<String, HashMap<String, String>> relvantDocsForDescription,String stem) {

        HashMap<String,Document> docsMaps = new HashMap<>();

        long numOfWordsInCorpus =0;
        try {
            FileReader fileReader;
            if (this.stem) {
                fileReader = new FileReader(postingFile+ "\\Documents_Information.txt");
            } else {
                fileReader = new FileReader(postingFile + "\\Documents_Information_NoStem.txt");
            }
            BufferedReader bf = new BufferedReader(fileReader);
            String line = bf.readLine();
            String [] sep = line.split("\\|");
            int numOfDocs = 1;
            while(line!=null){
                numOfWordsInCorpus += Integer.parseInt(sep[4]);

                if(relvantDocsBeforeRanker.containsKey(sep[0])){
                    Document documentInfo = new Document();
                    documentInfo.setOriginalDocID(sep[1]);
                    documentInfo.setTitle(sep[2]);
                    documentInfo.setDocLanguage(sep[3]);
                    documentInfo.setNumOfWordInDoc(Integer.parseInt(sep[4]));
                    documentInfo.setNumOfTerms(Integer.parseInt(sep[5]));
                    documentInfo.setMaxTerm(sep[6]);
                    documentInfo.setMaxFreqTerm(Integer.parseInt(sep[7]));
                    documentInfo.setOurDocID(sep[0]);
                    docsMaps.put(sep[0],documentInfo);
                }else if(relvantDocsForDescription!=null){
                    if(relvantDocsForDescription.containsKey(sep[0])){
                        Document documentInfo = new Document();
                        documentInfo.setOriginalDocID(sep[1]);
                        documentInfo.setTitle(sep[2]);
                        documentInfo.setDocLanguage(sep[3]);
                        documentInfo.setNumOfWordInDoc(Integer.parseInt(sep[4]));
                        documentInfo.setNumOfTerms(Integer.parseInt(sep[5]));
                        documentInfo.setMaxTerm(sep[6]);
                        documentInfo.setMaxFreqTerm(Integer.parseInt(sep[7]));
                        documentInfo.setOurDocID(sep[0]);
                        docsMaps.put(sep[0],documentInfo);
                    }
                }
                line = bf.readLine();
                numOfDocs++;
                if(line!=null) {
                    sep = line.split("\\|");
                }
            }
            numOfDocsInCorpus = numOfDocs;
            avgNumOfWordsInDoc = numOfWordsInCorpus/numOfDocs;

            bf.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return docsMaps;
    }

}
class ValueComparatorDouble implements Comparator<String>{

    HashMap<String, Double> map = new HashMap<String, Double>();

    public ValueComparatorDouble(HashMap<String, Double> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1) >= map.get(s2)){
            return -1;
        }else{
            return 1;
        }
    }
}
class ValueComparatorInteger implements Comparator<String>{

    Map<String, Integer> map = new HashMap<String, Integer>();

    public ValueComparatorInteger(Map<String, Integer> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1) >= map.get(s2)){
            return -1;
        }else{
            return 1;
        }
    }
}