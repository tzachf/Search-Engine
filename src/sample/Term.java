package sample;


/**
 * this class is created to represent word after parse process
 */
public class Term {

    private String content;
    private long lineInPosting;

    public int getTotalFreqInCorpus() {
        return totalFreqInCorpus;
    }

    private int totalFreqInCorpus;

    public Term(String con,long lineInPosting, int freq){
        content=con;
        this.lineInPosting= lineInPosting;
        totalFreqInCorpus= freq;
    }

    /**
     *
     * @return - the actual text inside the term
     */
    public String getContent(){
        return content;
    }

    /**
     * setting the content of the term
     * @param content - the actual text inside the term
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     * overriding the func equals of object to compare between terms by their content
     * @param obj - the other term that we're comparing to it
     * @return - true if it's equal
     */
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }else {
            if(Character.isDigit(((Term)obj).content.charAt(0))){
                if(((Term)obj).content.equals(this.content)){
                    return true;
                }else {
                    return false;
                }
            }else{
                return ((Term)obj).content.equals(this.content);
            }
        }
    }


}