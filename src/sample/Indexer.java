package sample;

import java.io.*;
import java.util.*;


/**
 * this class is the main class of Part A in this project
 * this class is building the inverted index of the terms in all the corpus and doing it by parts
 * to create this inverted index this class is using the ReadFile and Parse & after that writing the posting files, merge them and building the dictionary of the words in corpus
 */
public class Indexer {

    private TreeMap <String, Term> dic;
    private String postingPathForReset;
    private int ourDocID = 1;
    private boolean stemIndexer;
    private boolean firstTime = true;
    private int postingIndex=0;
    private int mergeIndex=0;
    private int celebMergeIndex=0;
    private Parse parse;
    private Queue <File> allPostingFiles = new LinkedList<>();
    private Queue <File> celebPostingFiles = new LinkedList<>();
    private int countterCelebs =0;
    private HashMap<Integer,String> alphabet;
    private final int CHUNKFOLDERS = 10;
    private final int NUMOFALLFOLDERS = 190;
    private int celebPostingIndex;

    public Indexer() {
        dic = new TreeMap<>();
        alphabet = new HashMap<>();
        alphabet.put(1,"A");alphabet.put(2,"B");alphabet.put(3,"C");alphabet.put(4,"D");alphabet.put(5,"E");alphabet.put(6,"F");alphabet.put(7,"G");
        alphabet.put(8,"H");alphabet.put(9,"I");alphabet.put(10,"J");alphabet.put(11,"K");alphabet.put(12,"L");alphabet.put(13,"M");alphabet.put(14,"N");
        alphabet.put(15,"O");alphabet.put(16,"P");alphabet.put(17,"Q");alphabet.put(18,"R");alphabet.put(19,"S");alphabet.put(20,"T");alphabet.put(21,"U");
        alphabet.put(22,"V");alphabet.put(23,"W");alphabet.put(24,"X");alphabet.put(25,"Y");alphabet.put(26,"Z");
    }


    /**
     * MAIN FUNCTION-
     * this func is using all the other functions after the user clicked "Start The Engine"
     * this func is reading files and after it parse it , and then the same chunk of words are being writed to posting files
     * after that we're adding the entities to posting files also
     * then we're merging the posting files by merge sort
     * and for the end we're splitting the big one postingFile to sorted file by alphabetic
     * @param corpusPath - path to corpus
     * @param postingPath - path to posting
     * @param stem - On or Off as the user choosed
     */
    public boolean createIndexer(String corpusPath, String postingPath , String stem){

        postingPathForReset =postingPath;
        parse = new Parse();
        parse.setPostingPath(postingPath);
        setBoolStem(stem);
        invertIndexInParts(corpusPath,postingPath);
        //addCelebPosting(postingPath);
        celebPostingFiles.add(parse.changeSizeCelebs(celebPostingIndex,postingPath));
        mergeCelebPosting(postingPath);
        filterCeleb(postingPath);
        mergePosting(postingPath);
        branching(postingPath);
        mergeIndex=0;
        postingIndex=0;
        allPostingFiles.clear();
        ourDocID=1;
        return true;
    }

    private void filterCeleb(String postingPath) {

        File celebPostingBeforeFilter = celebPostingFiles.poll();
        File celebPostingFinal = new File(postingPath + "\\celebPostingFinal" + celebMergeIndex + ".txt");
        FileReader celebPostingReader = null;
        FileWriter writer=null;
        BufferedReader bf= null;

        try{
            celebPostingFinal.createNewFile();
            celebPostingReader = new FileReader(celebPostingBeforeFilter);
            bf = new BufferedReader(celebPostingReader);
            writer = new FileWriter(celebPostingFinal,true);


        }catch (IOException e) {
            e.printStackTrace();
        }
        String str="";
        String[] str1 = new String[0];

        try {
            str = bf.readLine();
            str1 = str.split("\\|");
            while (str!=null){
                if(!str1[3].equals("1")){
                    writer.write(str + System.lineSeparator());
                }
                str=bf.readLine();
                if(str!=null) {
                    str1 = str.split("\\|");
                }
            }
            writer.close();
            bf.close();
            celebPostingReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        celebPostingBeforeFilter.delete();
        allPostingFiles.add(celebPostingFinal);
    }

    private void mergeCelebPosting(String postingPath) {

        while (celebPostingFiles.size() != 1) {
            File postingFile11 = celebPostingFiles.poll();
            File postingFile22 = celebPostingFiles.poll();
            FileReader postingFile1 = null;
            FileReader postingFile2 = null;
            try {
                postingFile1 = new FileReader(postingFile11);
                postingFile2 = new FileReader(postingFile22);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            File mergedCelebPostingFile = new File(postingPath + "\\mergedCelebPosting" + celebMergeIndex + ".txt");

            celebMergeIndex++;

            BufferedReader bf1 = null;
            BufferedReader bf2 = null;
            FileWriter writer = null;
            try {
                writer = new FileWriter(mergedCelebPostingFile, true);
                bf1 = new BufferedReader(postingFile1);
                bf2 = new BufferedReader(postingFile2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] str1 = new String[0];
            String[] str2 = new String[0];
            try {
                str1 = bf1.readLine().split("\\s+\\|", 2);
                str2 = bf2.readLine().split("\\s+\\|", 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (str1!=null && str2!=null) {
                if(str1[0].equals("B FISCAL")||str2[0].equals("B FISCAL")){
                    System.out.println("fsdf");
                }
                if (str1[0].compareToIgnoreCase(str2[0]) < 0) {
                    try {
                        writer.write(str1[0] + " |" + str1[1] + System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String nextLine = bf1.readLine();
                        if(nextLine!=null) {
                            str1 = nextLine.split("\\s+\\|", 2);
                        }else{
                            str1=null;
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (str1[0].compareToIgnoreCase(str2[0]) > 0) {
                    try {
                        writer.write(str2[0] + " |" + str2[1] + System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String nextLine2 =  bf2.readLine();
                        if(nextLine2!=null) {
                            str2 = nextLine2.split("\\s+\\|", 2);
                        }else{
                            str2=null;
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (str1[0].compareToIgnoreCase(str2[0]) > 0) {
                        try {
                            writer.write(str2[0] + " |" + str2[1] + System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            String nextLine2 =  bf2.readLine();
                            if(nextLine2!=null) {
                                str2 = nextLine2.split("\\s+\\|", 2);
                            }else{
                                str2=null;
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        if(Character.isLowerCase(str1[0].charAt(0))||Character.isLowerCase(str2[0].charAt(0))) {
                            writer.write(str2[0].toLowerCase() + " |");
                        }else{
                            writer.write(str2[0] + " |");
                        }
                        String[] restStr1 = str1[1].split("\\|");
                        String[] restStr2 = str2[1].split("\\|");
                        int Freq1 = Integer.parseInt(restStr1[0]);
                        int Freq2 = Integer.parseInt(restStr2[0]);
                        int sumNewFreq = Freq1 + Freq2;
                        int numOfDocs1 = Integer.parseInt(restStr1[2]);
                        int numOfDocs2 = Integer.parseInt(restStr2[2]);
                        int newNumOfDocs = numOfDocs1 + numOfDocs2;
                        writer.write(sumNewFreq + "| |" +newNumOfDocs + "|"+ restStr1[3].substring(0,restStr1[3].length()-1) + restStr2[3]+ System.lineSeparator() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String nextLine1 = bf1.readLine();
                        if(nextLine1!=null) {
                            str1 = nextLine1.split("\\s+\\|", 2);
                        }else{
                            str1=null;
                        }
                        String nextLine2 = bf2.readLine();
                        if(nextLine2!=null) {
                            str2 = nextLine2.split("\\s+\\|", 2);
                        }else{
                            str2=null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (str2!=null){
                try {
                    writer.write(str2[0] + " |" + str2[1] + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String nextLine2 =  bf2.readLine();
                    if(nextLine2!=null) {
                        str2 = nextLine2.split("\\s+\\|", 2);
                    }else{
                        str2=null;
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while (str1!=null){
                try {
                    writer.write(str1[0] + " |" + str1[1] + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String nextLine1 =  bf1.readLine();
                    if(nextLine1!=null) {
                        str1 = nextLine1.split("\\s+\\|", 2);
                    }else{
                        str1=null;
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            celebPostingFiles.add(mergedCelebPostingFile);
            try {
                writer.close();
                bf1.close();
                bf2.close();
                postingFile1.close();
                postingFile2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            postingFile11.delete();
            postingFile22.delete();
        }

    }

    /**
     * reading , parsing & writing the posting files
     * doing it all by parts of 10 files of text that contain in avg 300 docs  ==> 3000 docs in one run
     * @param corpusPath -  path to corpus
     * @param postingPath - path to posting
     */
    private void invertIndexInParts(String corpusPath, String postingPath) {
        ReadFile rf = new ReadFile(corpusPath);
        for (int i = 0; i < NUMOFALLFOLDERS; i++) {
            HashMap partOfDocs= rf.readFile(corpusPath,CHUNKFOLDERS);
            if(partOfDocs==null){
                break;
            }
            HashMap <String, TermDetails> currTerms = parse.parsePartOfDocs(partOfDocs,ourDocID);
            Map <String, TermDetails> sortedCurrTerms = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    int cmp = s.compareToIgnoreCase(t1);
                    if(cmp!=0){
                        return cmp;
                    }
                    return s.compareTo(t1);
                }
            });
            sortedCurrTerms.putAll(currTerms);
            File postingFile;
            if(stemIndexer) {
                postingFile = new File(postingPath + "\\Posting" + postingIndex + ".txt");
            }else{
                postingFile = new File(postingPath + "\\PostingNoStem" + postingIndex + ".txt");
            }
            try {
                postingFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileWriter writer = new FileWriter(postingFile);
                for (String key :sortedCurrTerms.keySet()) {
                    writer.write(key + " |");
                    writer.write(sortedCurrTerms.get(key).getTotalFreqInCorpus()+ "| |" +sortedCurrTerms.get(key).getNumOfDocsFreq()+"| ");
                    HashMap<Integer,Integer> id_Freq = sortedCurrTerms.get(key).getId_freq();
                    for (Integer docID: sortedCurrTerms.get(key).getId_freq().keySet()) {
                        writer.write("(" + docID + " ," + id_Freq.get(docID) + ") ");
                    }
                    writer.write(System.lineSeparator());
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(i%10==0){
                //addCelebPosting(postingPath);
                celebPostingFiles.add(parse.changeSizeCelebs(celebPostingIndex,postingPath));
                celebPostingIndex++;
            }
            ourDocID = ourDocID + partOfDocs.size();
            allPostingFiles.add(postingFile);
            postingIndex++;
            parse.clearTerms();
            currTerms.clear();
            sortedCurrTerms.clear();
            partOfDocs.clear();
        }
    }


    /**
     * converting the chosed option of stemming to boolean
     * @param stem
     */
    private void setBoolStem(String stem){
        if(stem!=null){
            if(stem.equals("ON")){
                stemIndexer = true;
                parse.setStem(true);
            }else{
                parse.setStem(false);
                stemIndexer=false;
            }
        }else {
            parse.setStem(false);
            stemIndexer=false;
        }
    }

    /**
     * splitting the big one postingFile to sorted file by alphabetic & creating the dictionary
     * @param postingPath - path to posting
     */
    private void branching(String postingPath){
        int counter = 0;
        long currPointer =0;
        FileReader postingFile = null;
        File dicFile = null;
        String currLine = "";
        try {
            if(stemIndexer) {
                postingFile = new FileReader(postingPath + "\\mergedPosting" + (mergeIndex - 1) + ".txt");
                dicFile = new File(postingPath + "\\dictionary.txt");
            }else{
                postingFile = new FileReader(postingPath + "\\mergedPostingNoStem" + (mergeIndex - 1) + ".txt");
                dicFile = new File(postingPath + "\\dictionaryNoStem.txt");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bf = new BufferedReader(postingFile);
        File nonAlphabeticPosting;
        if(stemIndexer) {
            nonAlphabeticPosting = new File(postingPath + "\\0-9.txt");
        }else{
            nonAlphabeticPosting = new File(postingPath + "\\0-9_NoStem.txt");
        }
        FileWriter dicWriter = null;
        FileWriter writer = null;
        try {
            dicWriter = new FileWriter(dicFile);
            writer = new FileWriter(nonAlphabeticPosting);
            currLine = bf.readLine();
            counter++;
            int digitCounter = 0;
            while(currLine != null && !Character.isLetter(currLine.charAt(0))){
                writer.write(currLine );
                String [] currLineTerm = currLine.split("\\|", 3);
                dic.put(currLineTerm[0],new Term(currLineTerm[0],currPointer,Integer.parseInt(currLineTerm[1])));
                dicWriter.write(currLineTerm[0] + "|" + currPointer + "|" + Integer.parseInt(currLineTerm[1]) + System.lineSeparator());
                currPointer+=currLine.length()+1;
                currLine = bf.readLine();
                counter++;
                digitCounter++;
                if(!Character.isLetter(currLine.charAt(0))){
                    writer.write(System.lineSeparator());
                }
            }
            System.out.println(digitCounter);
            currPointer = 0;
            writer.close();
            for (int i = 1; i < 27; i++) {
                String currLetter = ""+currLine.charAt(0);
                File currFilePost;
                if(stemIndexer) {
                    currFilePost = new File(postingPath + "\\" + alphabet.get(i) + ".txt");
                }else{
                    currFilePost = new File(postingPath + "\\" + alphabet.get(i) + "_NoStem.txt");
                }
                writer = new FileWriter(currFilePost);
                while(currLetter.compareToIgnoreCase(alphabet.get(i))==0&&currLine!=null){
                    writer.write(currLine);
                    String [] currLineTerm = currLine.split("\\|", 3);
                    dic.put(currLineTerm[0],new Term(currLineTerm[0],currPointer,Integer.parseInt(currLineTerm[1])));
                    dicWriter.write(currLineTerm[0] + "|" + currPointer + "|" + Integer.parseInt(currLineTerm[1]) + System.lineSeparator());
                    currPointer+=currLine.length()+1;
                    currLine=bf.readLine();
                    counter++;
                    if(currLine!=null){
                        currLineTerm = currLine.split("\\|", 3);
                        currLetter =""+currLine.charAt(0);
                    }
                    if(currLetter.compareToIgnoreCase(alphabet.get(i))==0&&currLine!=null){
                        writer.write(System.lineSeparator());
                    }
                }
                writer.close();
                currPointer = 0 ;
            }
            dicWriter.close();
            bf.close();
            File fileToDel;
            if(stemIndexer) {
                fileToDel = new File(postingPath + "\\mergedPosting" + (mergeIndex - 1) + ".txt");
            }else{
                fileToDel = new File(postingPath + "\\mergedPostingNoStem" + (mergeIndex - 1) + ".txt");
            }
            fileToDel.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TERMS SIZE: " + counter);
    }


//    /**
//     * every 75,000 docs in avg - creating posting file for entities
//     * @param postingPath - path to posting
//     */
//    private void addCelebPosting(String postingPath) {
//        HashMap<String, TermDetails> celebTermsBeforeSotrting = parse.getCelebTerms();
//        Map<String, TermDetails> celebTerms = new TreeMap(celebTermsBeforeSotrting);
//        File postingFile = new File(postingPath + "\\celebPosting" + postingIndex + ".txt");
//        FileWriter writer = null;
//        try {
//            postingFile.createNewFile();
//            writer = new FileWriter(postingFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (String key : celebTerms.keySet()) {
//                if(celebTerms.get(key).getNumOfDocsFreq()>2){
//                    countterCelebs +=1;
//                try {
//                    writer.write(key+ " |");
//                    writer.write(celebTerms.get(key).getTotalFreqInCorpus() + "| |" +celebTerms.get(key).getNumOfDocsFreq()+"| { ");
//                    HashMap<Integer,Integer> id_Freq = celebTerms.get(key).getId_freq();
//                    for (Integer docID: celebTerms.get(key).getId_freq().keySet()) {
//                        writer.write("(" + docID + " ," + id_Freq.get(docID) + ") ");
//                    }
//                    writer.write( " } " +System.lineSeparator());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        try {
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        postingIndex++;
//        allPostingFiles.add(postingFile);
//    }
//

    /**
     * after all the posting files were writen this func is merging the into one by MERGE SORT
     * this is happening to save space in the RAM
     * @param postingPath - path to posting
     */
    private void mergePosting(String postingPath) {

        while (allPostingFiles.size() != 1) {
            File postingFile11 = allPostingFiles.poll();
            File postingFile22 = allPostingFiles.poll();
            FileReader postingFile1 = null;
            FileReader postingFile2 = null;
            try {
                postingFile1 = new FileReader(postingFile11);
                postingFile2 = new FileReader(postingFile22);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            File mergedPostingFile;
            if(stemIndexer) {
                mergedPostingFile = new File(postingPath + "\\mergedPosting" + mergeIndex + ".txt");
            }else{
                mergedPostingFile = new File(postingPath + "\\mergedPostingNoStem" + mergeIndex + ".txt");
            }
            mergeIndex++;

            BufferedReader bf1 = null;
            BufferedReader bf2 = null;
            FileWriter writer = null;
            try {
                writer = new FileWriter(mergedPostingFile, true);
                bf1 = new BufferedReader(postingFile1);
                bf2 = new BufferedReader(postingFile2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] str1 = new String[0];
            String[] str2 = new String[0];
            try {
                str1 = bf1.readLine().split("\\s+\\|", 2);
                str2 = bf2.readLine().split("\\s+\\|", 2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (str1!=null && str2!=null) {

                if (str1[0].compareToIgnoreCase(str2[0]) < 0) {
                    try {
                        writer.write(str1[0] + " |" + str1[1] + System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String nextLine = bf1.readLine();
                        if(nextLine!=null) {
                            str1 = nextLine.split("\\s+\\|", 2);
                        }else{
                            str1=null;
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (str1[0].compareToIgnoreCase(str2[0]) > 0) {
                    try {
                        writer.write(str2[0] + " |" + str2[1] + System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String nextLine2 =  bf2.readLine();
                        if(nextLine2!=null) {
                            str2 = nextLine2.split("\\s+\\|", 2);
                        }else{
                            str2=null;
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (str1[0].compareToIgnoreCase(str2[0]) > 0) {
                        try {
                            writer.write(str2[0] + " |" + str2[1] + System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            String nextLine2 =  bf2.readLine();
                            if(nextLine2!=null) {
                                str2 = nextLine2.split("\\s+\\|", 2);
                            }else{
                                str2=null;
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {

                        if(Character.isLowerCase(str1[0].charAt(0))||Character.isLowerCase(str2[0].charAt(0))) {
                            writer.write(str2[0].toLowerCase() + " |");
                        }else{
                            writer.write(str2[0] + " |");
                        }
                        String[] restStr1 = str1[1].split("\\|");
                        String[] restStr2 = str2[1].split("\\|");
                        int Freq1 = Integer.parseInt(restStr1[0]);
                        int Freq2 = Integer.parseInt(restStr2[0]);
                        int sumNewFreq = Freq1 + Freq2;
                        int numOfDocs1 = Integer.parseInt(restStr1[2]);
                        int numOfDocs2 = Integer.parseInt(restStr2[2]);
                        int newNumOfDocs = numOfDocs1 + numOfDocs2;

                        writer.write(sumNewFreq + "| |" +newNumOfDocs + "|"+ restStr1[3].substring(0,restStr1[3].length()-1) + restStr2[3]+ System.lineSeparator() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String nextLine1 = bf1.readLine();
                        if(nextLine1!=null) {
                            str1 = nextLine1.split("\\s+\\|", 2);
                        }else{
                            str1=null;
                        }
                        String nextLine2 = bf2.readLine();
                        if(nextLine2!=null) {
                            str2 = nextLine2.split("\\s+\\|", 2);
                        }else{
                            str2=null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (str2!=null){
                try {
                    writer.write(str2[0] + " |" + str2[1] + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String nextLine2 =  bf2.readLine();
                    if(nextLine2!=null) {
                        str2 = nextLine2.split("\\s+\\|", 2);
                    }else{
                        str2=null;
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while (str1!=null){
                try {
                    writer.write(str1[0] + " |" + str1[1] + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String nextLine1 =  bf1.readLine();
                    if(nextLine1!=null) {
                        str1 = nextLine1.split("\\s+\\|", 2);
                    }else{
                        str1=null;
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            allPostingFiles.add(mergedPostingFile);
            try {
            writer.close();
            bf1.close();
            bf2.close();
            postingFile1.close();
            postingFile2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
            if(!postingFile11.getName().contains("celebPostingFinal")) {
                postingFile11.delete();
            }else{
                File file = new File(postingPath + "\\celebDictionary.txt");
                postingFile11.renameTo(file);
            }



            if(!postingFile22.getName().contains("celebPostingFinal")) {
                postingFile22.delete();
            }else{
                File file = new File(postingPath + "\\celebDictionary.txt");
                postingFile22.renameTo(file);
            }

        }

    }


    /**
     * deleting all the posting files after splitting them
     * @return true if succeeded
     */
    public boolean reset() {
        dic.clear();

        if(postingPathForReset==null){
            return false;
        }else{
            File postingDir = new File(postingPathForReset);
            File [] postingFilesToDelete = postingDir.listFiles();
            for (File fileToDel: postingFilesToDelete) {
                fileToDel.delete();
            }
        }
        return true;
    }


    /**
     *
     * @return the dictionary
     */
    public TreeMap<String, Term> getDic() {
        return dic;
    }
}
