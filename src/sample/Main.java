package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class that runs the user interface of the project
 */
public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ZaZa Engine");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Scene scene = new Scene(root, 650,540);
        scene.getStylesheets().add(getClass().getResource("/engineStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
        ///double x = Math.log(8);
        double d = 0;
        Double [] dsad = new Double[1];
        dsad[0] = d;
//
//        Searcher sc = new Searcher("d:\\documents\\users\\itzhakf\\Documents\\IR10.01\\PostingFile2230.09.01");
//        sc.searchDocsByQuery(351,"Falkland petroleum exploration" , "OFF","OFF");
//        sc.searchDocsByQuery(352,"British Chunnel impact" , "OFF","OFF");
//        sc.searchDocsByQuery(358,"blood-alcohol fatalities" , "OFF","OFF");
//        sc.searchDocsByQuery(359,"mutual fund predictors" , "OFF","OFF");
//        sc.searchDocsByQuery(362,"human smuggling" , "OFF","OFF");
//        sc.searchDocsByQuery(367,"piracy" , "OFF","OFF");
//        sc.searchDocsByQuery(373,"encryption equipment export" , "OFF","OFF");
//        sc.searchDocsByQuery(374,"Nobel prize winners" , "OFF","OFF");
//        sc.searchDocsByQuery(377,"cigar smoking" , "OFF","OFF");
//        sc.searchDocsByQuery(380,"obesity medical treatment" , "OFF","OFF");
//        sc.searchDocsByQuery(384,"space station moon" , "OFF","OFF");
//        sc.searchDocsByQuery(385,"hybrid fuel cars" , "OFF","OFF");
//        sc.searchDocsByQuery(387,"radioactive waste" , "OFF","OFF");
//        sc.searchDocsByQuery(388,"organic soil enhancement" , "OFF","OFF");
//        sc.searchDocsByQuery(390,"orphan drugs" , "OFF","OFF");


        long startTime = System.currentTimeMillis();
  //      Indexer indexer = new Indexer();
//        indexer.createIndexer("d:\\documents\\users\\zahik\\Downloads\\corpus\\corpus","d:\\documents\\users\\zahik\\Documents","OFF");

        long estimatedTime = System.currentTimeMillis() - startTime;
//
    }


}
