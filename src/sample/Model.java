package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;


/**
 * the class is part of MVC design pattern and it's purpose is act as the logic part of the system
 */
public class Model {

    Indexer indexer = new Indexer();
    Searcher searcher = new Searcher();
    boolean loadClicked = false;
    TreeMap<String, Term> dicFromLoad = new TreeMap<>();
    TreeMap<String, Button> relevantDocs = new TreeMap<>();


    /**
     * this func is running the system using the indexer class
     *
     * @param corpusPath  - path to the corpus
     * @param postingPath - path to the postingFiles
     * @param stem        - as the user choosed the start with or without stemmer
     * @return - if the all the start process worked successfully
     */
    public boolean startIndexer(String corpusPath, String postingPath, String stem) {
        return indexer.createIndexer(corpusPath, postingPath, stem);
    }

    /**
     * running indexer reset to delete all the posting files
     *
     * @return if deleting worked as estimated
     */
    public boolean reset() {
        return indexer.reset();
    }

    /**
     * showing the dictionary to the user
     */
    public void showDic() {

        TableView<Term> table;
        Stage stage = new Stage();
        stage.setTitle("Dictionary");

        TableColumn<Term, String> nameTerm = new TableColumn<>("Term");
        nameTerm.setMinWidth(200);
        nameTerm.setCellValueFactory(new PropertyValueFactory<Term, String>("content"));

        TableColumn<Term, String> totalFreqTerm = new TableColumn<>("Total Frequency In Corpus");
        totalFreqTerm.setMinWidth(200);
        totalFreqTerm.setCellValueFactory(new PropertyValueFactory<Term, String>("totalFreqInCorpus"));


        table = new TableView<>();
        table.setItems(getTermsDic());
        table.getColumns().addAll(nameTerm, totalFreqTerm);

        final Label label = new Label("Dictoinary");
        label.setFont(new Font("Arial", 25));

        VBox box = new VBox();
        box.getChildren().addAll(label, table);

        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.show();

    }


    /**
     * adding the term of dictionary to the tableview - the table that the user watch
     *
     * @return
     */
    public ObservableList<Term> getTermsDic() {
        TreeMap<String, Term> dicSorted;
        ObservableList<Term> dic = FXCollections.observableArrayList();
        if (loadClicked) {
            dicSorted = dicFromLoad;
        } else {
            dicSorted = indexer.getDic();
        }
        for (String key : dicSorted.keySet()) {
            dic.add(dicSorted.get(key));
        }
        return dic;
    }


    /**
     * loading the dictionary to the system
     *
     * @param postingPath - path to posting files
     * @param stem        - variable as the user choose - to load the dic with or without stemming
     * @return true if loading worked successfully
     */
    public boolean loadDictionary(String postingPath, String stem) {
        loadClicked = true;
        dicFromLoad = new TreeMap<>();
        boolean stemIndexer;
        FileReader dicFile = null;
        BufferedReader bf = null;
        String currLine = null;
        Term currTerm = null;
        if (stem != null) {
            if (stem.equals("ON")) {
                stemIndexer = true;
            } else {
                stemIndexer = false;
            }
        } else {
            stemIndexer = false;
        }

        try {
            if (stemIndexer) {
                dicFile = new FileReader(postingPath + "\\dictionary.txt");
            } else {
                dicFile = new FileReader(postingPath + "\\dictionaryNoStem.txt");
            }
            bf = new BufferedReader(dicFile);
            currLine = bf.readLine();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        while (currLine != null) {
            String[] arrLine = currLine.split("\\|");
            dicFromLoad.put(arrLine[0], new Term(arrLine[0], Long.parseLong(arrLine[1]), Integer.parseInt(arrLine[2])));
            try {
                currLine = bf.readLine();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }


    /**
     * this function is retrieving relevant documents for a single query
     *
     * @param postingPath  - path to posting files
     * @param query        - the query that the user entered
     * @param semantic     - variable as the user choose - to retrieve with or without semantic search
     * @param stem         - variable as the user choose - to load the dic with or without stemming
     * @param pathToResult - the path to the relevant docs results
     */
    public boolean runSingleQuery(String postingPath, String query, String semantic, String stem, String pathToResult) {
        if(!searcher.searchDocsByQuery(351, query, stem, semantic, postingPath, pathToResult)){
            Alert alert = new Alert(Alert.AlertType.ERROR,"There is no posting file that match your arguments");
            alert.showAndWait();
        }


        TableView<Document> table;
        Stage stage = new Stage();
        stage.setTitle("RelevantDocs");

        TableColumn<Document, String> nameTerm = new TableColumn<>("Doc ID");
        nameTerm.setMinWidth(200);
        nameTerm.setCellValueFactory(new PropertyValueFactory<Document, String>("originalDocID"));

        TableColumn<Document, Button> totalFreqTerm = new TableColumn<>("Entities");
        totalFreqTerm.setMinWidth(200);
        totalFreqTerm.setCellValueFactory(new PropertyValueFactory<Document, Button>("Show"));

        table = new TableView<>();
        table.setItems(getDocs(false));
        table.getColumns().addAll(nameTerm, totalFreqTerm);

        final Label label = new Label("RelevantDocs");
        label.setFont(new Font("Arial", 25));

        VBox box = new VBox();
        box.getChildren().addAll(label, table);

        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.show();
        return true;

    }

    /**
     * this func is getting the relevant docs for every query
     *
     * @param manyQueries - variable that tell us if is it one query or many
     * @return relevant docs
     */
    public ObservableList<Document> getDocs(boolean manyQueries) {
        List<Document> finalResult = new LinkedList<>();
        HashMap<Integer, List<String>> query_relevantDocs = searcher.getResultsByQuery();
        HashMap<Integer, Map<String, Map<String, Integer>>> query_Doc_celebs = searcher.getCelebsByQuery();

        int index = 0;
        for (Integer query : query_relevantDocs.keySet()) {
            Document temp = new Document();
            for (String doc : query_relevantDocs.get(query)) {
                if (index % 50 == 0) {
                    if (manyQueries) {
                        temp.setOriginalDocID("----" + query + "----");
                        ((LinkedList<Document>) finalResult).addLast(temp);
                    } else {
                        temp.setOriginalDocID("----ZaZa----");
                        ((LinkedList<Document>) finalResult).addLast(temp);
                    }
                }

                Button button = new Button("Show Entities");
                Document temp1 = new Document(button);
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        TableView<CelebFreq> table;
                        Stage stage = new Stage();
                        stage.setMaxHeight(300);
                        stage.setTitle("Entities");

                        TableColumn<CelebFreq, String> nameTerm = new TableColumn<>("Top Entities");
                        nameTerm.setMinWidth(200);
                        nameTerm.setCellValueFactory(new PropertyValueFactory<CelebFreq, String>("celebTerm"));

                        TableColumn<CelebFreq, Integer> freqTerm = new TableColumn<>("Top Entities");
                        freqTerm.setMinWidth(200);
                        freqTerm.setCellValueFactory(new PropertyValueFactory<CelebFreq, Integer>("freq"));

                        table = new TableView<>();
                        table.setItems(temp1.getEntities());
                        table.getColumns().addAll(nameTerm, freqTerm);

                        final javafx.scene.control.Label label = new Label("Entities");
                        label.setFont(new Font("Arial", 25));

                        VBox box = new VBox();
                        box.getChildren().addAll(label, table);

                        Scene scene = new Scene(box);
                        stage.setScene(scene);
                        stage.show();

                    }
                });

                temp1.setShow(button);
                temp1.setOriginalDocID(query_relevantDocs.get(query).get(index));
                temp1.setCelebs(query_Doc_celebs.get(query).get(query_relevantDocs.get(query).get(index)));
                ((LinkedList<Document>) finalResult).addLast(temp1);
                index++;
            }
            index = 0;
        }
        ObservableList<Document> result = FXCollections.observableArrayList();
        for (Document dInfo : finalResult) {
            result.add(dInfo);
        }
        return result;
    }

    /**
     * this function is retrieving relevant documents for a many queries
     *
     * @param postingPath  - path to posting files
     * @param semantic     - variable as the user choose - to retrieve with or without semantic search
     * @param stem         - variable as the user choose - to load the dic with or without stemming
     * @param pathToResult - the path to the relevant docs results
     */
    public boolean runFilesQuerries(String pathToQueriesFile, String stem, String semantic, String postingPath, String pathToResult) {
        if (!searcher.searchDocsByFile(pathToQueriesFile, stem, semantic, postingPath, pathToResult)) {
            return false;
        }


        TableView<Document> table;
        Stage stage = new Stage();
        stage.setTitle("RelevantDocs");

        TableColumn<Document, String> nameTerm = new TableColumn<>("Doc ID");
        nameTerm.setMinWidth(200);
        nameTerm.setCellValueFactory(new PropertyValueFactory<Document, String>("originalDocID"));

        TableColumn<Document, Button> totalFreqTerm = new TableColumn<>("Entities");
        totalFreqTerm.setMinWidth(200);
        totalFreqTerm.setCellValueFactory(new PropertyValueFactory<Document, Button>("Show"));


        table = new TableView<>();
        table.setItems(getDocs(true));
        table.getColumns().addAll(nameTerm, totalFreqTerm);

        final Label label = new Label("RelevantDocs");
        label.setFont(new Font("Arial", 25));

        VBox box = new VBox();
        box.getChildren().addAll(label, table);

        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.show();
        return true;

    }


}
