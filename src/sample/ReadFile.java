package sample;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * the purpose of this class is to read num of files from a given path and put all of them inside an array
 *
 */
public class ReadFile {


    private static int ourDocID;
    private boolean hasLanguge;
    private String[] langugeStr;
    private File[] files;
    private File dir;
    private int docIndex;

    ReadFile(String path) {
        ourDocID = 1;
        hasLanguge = false;
        langugeStr = new String[0];
        dir = new File(path);
        files = dir.listFiles((direc, name) -> !name.equals(".DS_Store"));
        docIndex = 0;
    }




    public static int getOurDocID() {
        return ourDocID;
    }



    /**this func is readinf from the given path and separating the documents by tags
     *
     * @param path - path to the corpus that is full of folders including files of documents
     * @param numOfFolders - the function is running in parts for the sake of saving space in the RAM
     * @return - hashmap that contain pairs of Id of doc and the doc
     */
    public HashMap<Integer, Document> readFile(String path, int numOfFolders) {
        HashMap<Integer, Document> allDocs = new HashMap<>();

        if(docIndex>files.length){
            return null;
        }

        if (dir.isDirectory()) {
            for (int j = docIndex; j < docIndex + numOfFolders; j++) {
                if (j + 1 == files.length) {
                    break;
                }
                String folderName = files[j].getName().toString();
                File currentFile = new File(path + "/" + folderName + "/" + folderName);
                try {
                    BufferedReader br = new BufferedReader(new FileReader(currentFile));
                    String currentLine;
                    while ((currentLine = br.readLine()) != null) {
                        if(ourDocID == 130567){
                            System.out.println("zasdasda");
                        }
                        String docID = new String();
                        String title = new String();
                        String Text = new String();
                        while (!currentLine.equals("</DOC>")) {
                            String[] currentLineWords = currentLine.split("\\s+");
                            if (currentLineWords != null) {
                                if (currentLineWords[0].startsWith("<DOCNO>")) {
                                    int index = currentLine.indexOf('>') +1 ;
                                    while(currentLine.charAt(index)!='<'){
                                        docID += currentLine.charAt(index);
                                        index++;
                                    }
                                    docID = docID.replaceAll(" ","");
                                }
                                if (currentLineWords.length > 1) {
                                    if (currentLineWords[1].equals("<TI>")) {
                                        for (int i = 2; i < currentLineWords.length - 1; i++) {
                                            title += currentLineWords[i] + " ";
                                        }
                                    }
                                }else if(currentLineWords[0].contains("<HEADLINE>")){
                                    currentLine = br.readLine();
                                    if (currentLine == null) {
                                        break;
                                    }
                                    if(currentLine.contains("<P>")){
                                        currentLine = br.readLine();
                                        title = currentLine;
                                        if (currentLine == null) {
                                            break;
                                        }
                                    }else{
                                        title = currentLine;
                                    }

                                }
                                if (currentLineWords[0].equals("<TEXT>")) {
                                    currentLine = br.readLine();
                                    while (!(currentLine).contains("</TEXT>")) {

                                        if (currentLine.contains("P=105")) {
                                            langugeStr = currentLine.split("\\s");
                                            hasLanguge = true;
                                        } else {
                                            Text += " " + currentLine;
                                        }
                                        currentLine = br.readLine();
                                    }
                                }
                            }
                            currentLine = br.readLine();
                            if (currentLine == null) {
                                break;
                            }
                        }
                        Document newDoc = new Document(docID, title, Text);
                        if (hasLanguge) {
                            if (!langugeStr[3].equals("</F>")) {
                                newDoc.setLanguage(langugeStr[3]);
                            }
                            hasLanguge = false;
                        }
                        newDoc.setOurDocID(ourDocID);
                        allDocs.put(ourDocID, newDoc);
                        ourDocID++;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            docIndex = docIndex + numOfFolders;
        }
        return allDocs;
    }



}

