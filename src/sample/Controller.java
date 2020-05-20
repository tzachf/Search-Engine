package sample;


import java.util.Map;

/**
 * this class is part of the MVC design pattern and it's purpose is to connect between the view and the model
 */
public class Controller {


    private Model model = new Model();


    /**
     * passing to the model to delete the posting files
     * @return
     */
    public boolean reset(){
        return  model.reset();
    }


    /**
     * passing to the model to show the dictionary of terms
     */
    public void showDic(){
        model.showDic();
    }


    /**
     * passing to the model to load existing posting files to the system
     * @param pathToPosting
     * @param stem - as the user choosed to load with or without stemming
     * @return if loaded successfully
     */
    public boolean loadDictionary(String pathToPosting, String stem){
        return model.loadDictionary(pathToPosting,stem);
    }


    /**
     * passing to the model to start the engine
     * @param corpusPath - path to the corpus
     * @param postingPath - path to the postingFiles
     * @param stem - as the user choosed the start with or without stemmer
     * @return if started successfully
     */
    public boolean startIndexer(String corpusPath, String postingPath, String stem) {
        return model.startIndexer(corpusPath, postingPath, stem);
    }


    /**
     * this func is passing to the model to run simple query
     * @param postingPath - path to posting files
     * @param query - the query that the user entered
     * @param semantic - variable as the user choose - to retrieve with or without semantic search
     * @param stem - variable as the user choose - to load the dic with or without stemming
     * @param pathToResult - the path to the relevant docs results
     */
    public boolean runSimpleQuery(String postingPath,String query,String semantic,String stem,String pathToResult) {
       return model.runSingleQuery(postingPath,query,semantic,stem,pathToResult);
    }



    /**
     * this func is passing to the model to run many queries from a file
     * @param postingPath - path to posting files
     * @param semantic - variable as the user choose - to retrieve with or without semantic search
     * @param stem - variable as the user choose - to load the dic with or without stemming
     * @param pathToResult - the path to the relevant docs results
     */
    public boolean runFileQueries(String pathToQueriesFile,String postingPath, String semantic, String stem,String pathToResult) {
        return model.runFilesQuerries(pathToQueriesFile,postingPath,semantic,stem,pathToResult);
    }
}
