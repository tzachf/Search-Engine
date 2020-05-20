package sample;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * this class is the view part from MVC design pattern
 * this class represent the interface that the user use - all the labels and buttons
 */
public class View  {

    public javafx.scene.control.TextField pathToCorpus;


    public  ComboBox<String> comboStem ;
    public  ComboBox<String> comboSemantic ;
    public TextField pathToPosting;
    public TextField simpleQuery;
    public TextField pathToQueriesFile;
    public TextField pathToResult;
    Controller controller= new Controller();
    private boolean pathQueriesLoaded;
    private boolean pathToResultLoaded;


    /**
     * this func is passing the controller to delete all the posting files
     * @param actionEvent
     */
    public void startOver(ActionEvent actionEvent) {
        if(controller.reset()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Reset Successfully!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"Reset Failed!");
            alert.showAndWait();
        }
    }

    /**
     * this func is passing the controller to show the dictionary
     * @param actionEvent
     */
    public void showDictionary(ActionEvent actionEvent) {
        controller.showDic();

    }

    /**
     * this func is passing to the controller to load existing posting files to the system
     * @param actionEvent
     */
    public void loadDictionary(ActionEvent actionEvent) {
        if(controller.loadDictionary(pathToPosting.getText(),comboStem.getValue())){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Loaded Successfully!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"Loaded Failed!");
            alert.showAndWait();
        }
    }

    /**
     * this func is opening a window to the user for choosing the path to the corpus
     * @param actionEvent
     */
    public void openBrowseWindow(ActionEvent actionEvent) {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory == null){
            //No Directory selected
        }else{
            TextField path = new TextField();
            path.setText(selectedDirectory.getAbsolutePath());
            pathToCorpus.setText(selectedDirectory.getAbsolutePath());
            pathToCorpus = path;
            System.out.println(pathToCorpus.getText());
        }
    }

    /**
     * this func is opening a window to the user for choosing the path to the posting
     * @param actionEvent
     */
    public void openBrowseWindowForPosting(ActionEvent actionEvent) {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory == null){
        }else{
            TextField path = new TextField();
            path.setText(selectedDirectory.getAbsolutePath());
            pathToPosting.setText(selectedDirectory.getAbsolutePath());
            pathToPosting = path;
            System.out.println(pathToPosting.getText());

        }
    }

    /**
     * this func is passing to the controller to start the engine
     * @param actionEvent
     */
    public void startTheEngine(ActionEvent actionEvent) {
        long startTime = System.currentTimeMillis();
        if(!pathToPosting.getText().contains("\\") || !pathToCorpus.getText().contains("\\")){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please insert the paths properly !!!");
            alert.showAndWait();
        }else {
            if(controller.startIndexer(pathToCorpus.getText(),pathToPosting.getText(),comboStem.getValue())){
                long estimatedTime = System.currentTimeMillis() - startTime;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Done processing the corpus ! \n Time: " + estimatedTime/60000 + " Min");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"The process failed  :( !!!");
                alert.showAndWait();
            }
        }
    }


    /**
     * this func is observing for the run simple query button and when it's pressed it passing to the controller to run simple query func
     * @param actionEvent
     */
    public void runSimpleQuery(ActionEvent actionEvent) {
        long startTime = System.currentTimeMillis();
        if(!pathToResultLoaded|| pathToPosting == null || simpleQuery.getText().equals("Enter Query")){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please load the result path AND posting path \n AND write query properly :)");
            alert.showAndWait();
        }else {
            long estimatedTime = System.currentTimeMillis() - startTime;
            controller.runSimpleQuery(pathToPosting.getText(), simpleQuery.getText(), comboSemantic.getValue(), comboStem.getValue(), pathToResult.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Found The Relevant Documents ! \n Time: " + estimatedTime/60000 + " Min");
            alert.showAndWait();
        }
    }


    /**
     * this func is opening a window to the user for choosing the path to the queries file
     * @param actionEvent
     */
    public void openBrowseQueryFile(ActionEvent actionEvent) {
        pathQueriesLoaded = true;
        Stage stage = new Stage();
        FileChooser directoryChooser = new FileChooser();
        File selectedDirectory = directoryChooser.showOpenDialog(stage);
        if(selectedDirectory == null){
        }else{
            TextField path = new TextField();
            path.setText(selectedDirectory.getAbsolutePath());
            pathToQueriesFile.setText(selectedDirectory.getAbsolutePath());
            pathToQueriesFile = path;
            System.out.println(pathToQueriesFile.getText());

        }
    }

    /**
     * this func is observing for the run queries button and when it's pressed it passing to the controller to run many queries from a file func
     * @param actionEvent
     */
    public void runFileQueries(ActionEvent actionEvent) {
        long startTime = System.currentTimeMillis();


        if(!pathQueriesLoaded || !pathToResultLoaded || pathToPosting == null){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Please load the result path AND the queries path \n AND posting path:)");
            alert.showAndWait();
        }else {
            long estimatedTime = System.currentTimeMillis() - startTime;
            if(!controller.runFileQueries(pathToQueriesFile.getText(), comboStem.getValue(), comboSemantic.getValue(), pathToPosting.getText(), pathToResult.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR,"There is no posting file that match your arguments");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Found The Relevant Documents ! \n Time: " + estimatedTime / 60000 + " Min");
                alert.showAndWait();
            }
        }
    }

    /**
     * this func is opening a window to the user for choosing the path to the results of relevant docs
     * @param actionEvent
     */
    public void openBrowseWindowForResult(ActionEvent actionEvent) {
        pathToResultLoaded = true;
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory == null){
        }else{
            TextField path = new TextField();
            path.setText(selectedDirectory.getAbsolutePath());
            pathToResult.setText(selectedDirectory.getAbsolutePath());
            pathToResult = path;
            System.out.println(pathToResult.getText());

        }

    }
}
