package sample;

import org.tartarus.snowball.ext.PorterStemmer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.*;

/**
 * The purpose of this class is to parse all the words inside every document that exists in the corpus
 */
public class Parse {


    private HashMap<String, TermDetails> terms;
    private HashMap<String, Integer> termsOfSpecificDoc;

    private HashMap<String, TermDetails> celebTerms;

    private HashMap<String, String> months;
    private List<String> percent;
    private List<String> prices;
    private PorterStemmer stemmer;
    private HashSet<String> stopWords;
    private HashSet<String> stopWordsAfterStem;
    private Document currDoc;
    private int TERMS_SIZE = 0;
    private boolean stem;
    private List<String> distances;
    private String postingPath;
    private File docInfoFile;
    int index = 0;


    public Parse() {
        terms = new HashMap<>();
        termsOfSpecificDoc = new HashMap<>();
        celebTerms = new HashMap<>();
        String[] percentArr = {"percent", "percentage", "%"};
        String[] pricesArr = {"Dollars", "dollars"};
        String[] distanceArr = {"km", "KM", "kilometer", "Kilometer", "meter", "Meter"};
        currDoc = new Document();
        stemmer = new PorterStemmer();
        percent = new LinkedList<>();
        distances = Arrays.asList(distanceArr);
        percent = Arrays.asList(percentArr);
        prices = new LinkedList<>();
        prices = Arrays.asList(pricesArr);
        months = new HashMap<>();
        stopWords = new HashSet<>();
        stopWordsAfterStem = new HashSet<>();


        allStopWords();

        months.put("Jan", "01");
        months.put("January", "01");
        months.put("Feb", "02");
        months.put("February", "02");
        months.put("Mar", "03");
        months.put("March", "03");
        months.put("Apr", "04");
        months.put("April", "04");
        months.put("May", "05");
        months.put("Jun", "06");
        months.put("June", "06");
        months.put("Jul", "07");
        months.put("July", "07");
        months.put("Aug", "08");
        months.put("August", "09");
        months.put("Sep", "09");
        months.put("September", "09");
        months.put("Oct", "10");
        months.put("October", "10");
        months.put("Nov", "11");
        months.put("November", "11");
        months.put("Dec", "12");
        months.put("December", "12");
        months.put("JANUARY", "01");
        months.put("FEBRUARY", "02");
        months.put("MARCH", "03");
        months.put("APRIL", "04");
        months.put("MAY", "05");
        months.put("JUNE", "06");
        months.put("JULY", "07");
        months.put("AUGUST", "08");
        months.put("SEPTEMBER", "09");
        months.put("OCTOBER", "10");
        months.put("NOVEMBER", "11");
        months.put("DECEMBER", "12");


    }

    /**
     * setter of option if to parse with stemming or not
     *
     * @param stem - param that the user choose , how he/she wants to parse with or without stemmer
     */
    public void setStem(boolean stem) {
        this.stem = stem;
    }


    /**
     * setting the posting path to save information about every document
     *
     * @param postingPath - the path
     */
    public void setPostingPath(String postingPath) {
        this.postingPath = postingPath;
    }

    /**
     * setting all the stop words to delete them after parse
     */
    public void allStopWords() {
        stopWords.add("a");
        stopWords.add("a's");
        stopWords.add("able");
        stopWords.add("about");
        stopWords.add("above");
        stopWords.add("according");
        stopWords.add("accordingly");
        stopWords.add("across");
        stopWords.add("actually");
        stopWords.add("after");
        stopWords.add("afterwards");
        stopWords.add("again");
        stopWords.add("against");
        stopWords.add("ain't");
        stopWords.add("all");
        stopWords.add("allow");
        stopWords.add("allows");
        stopWords.add("almost");
        stopWords.add("alone");
        stopWords.add("along");
        stopWords.add("already");
        stopWords.add("also");
        stopWords.add("although");
        stopWords.add("always");
        stopWords.add("am");
        stopWords.add("among");
        stopWords.add("amongst");
        stopWords.add("an");
        stopWords.add("and");
        stopWords.add("another");
        stopWords.add("any");
        stopWords.add("anybody");
        stopWords.add("anyhow");
        stopWords.add("anyone");
        stopWords.add("anything");
        stopWords.add("anyway");
        stopWords.add("anyways");
        stopWords.add("anywhere");
        stopWords.add("apart");
        stopWords.add("appear");
        stopWords.add("appreciate");
        stopWords.add("appropriate");
        stopWords.add("are");
        stopWords.add("aren't");
        stopWords.add("around");
        stopWords.add("as");
        stopWords.add("aside");
        stopWords.add("ask");
        stopWords.add("asking");
        stopWords.add("associated");
        stopWords.add("at");
        stopWords.add("available");
        stopWords.add("away");
        stopWords.add("awfully");
        stopWords.add("b");
        stopWords.add("be");
        stopWords.add("became");
        stopWords.add("because");
        stopWords.add("become");
        stopWords.add("becomes");
        stopWords.add("becoming");
        stopWords.add("been");
        stopWords.add("before");
        stopWords.add("beforehand");
        stopWords.add("behind");
        stopWords.add("being");
        stopWords.add("believe");
        stopWords.add("below");
        stopWords.add("beside");
        stopWords.add("besides");
        stopWords.add("best");
        stopWords.add("better");
        stopWords.add("between");
        stopWords.add("beyond");
        stopWords.add("both");
        stopWords.add("brief");
        stopWords.add("but");
        stopWords.add("by");
        stopWords.add("c");
        stopWords.add("c'mon");
        stopWords.add("c's");
        stopWords.add("came");
        stopWords.add("can");
        stopWords.add("can't");
        stopWords.add("cannot");
        stopWords.add("cant");
        stopWords.add("cause");
        stopWords.add("causes");
        stopWords.add("certain");
        stopWords.add("certainly");
        stopWords.add("changes");
        stopWords.add("clearly");
        stopWords.add("co");
        stopWords.add("com");
        stopWords.add("come");
        stopWords.add("comes");
        stopWords.add("concerning");
        stopWords.add("consequently");
        stopWords.add("consider");
        stopWords.add("considering");
        stopWords.add("contain");
        stopWords.add("containing");
        stopWords.add("contains");
        stopWords.add("corresponding");
        stopWords.add("could");
        stopWords.add("couldn't");
        stopWords.add("course");
        stopWords.add("currently");
        stopWords.add("d");
        stopWords.add("definitely");
        stopWords.add("described");
        stopWords.add("despite");
        stopWords.add("did");
        stopWords.add("didn't");
        stopWords.add("different");
        stopWords.add("do");
        stopWords.add("does");
        stopWords.add("doesn't");
        stopWords.add("doing");
        stopWords.add("don't");
        stopWords.add("done");
        stopWords.add("down");
        stopWords.add("downwards");
        stopWords.add("during");
        stopWords.add("e");
        stopWords.add("each");
        stopWords.add("edu");
        stopWords.add("eg");
        stopWords.add("eight");
        stopWords.add("either");
        stopWords.add("else");
        stopWords.add("elsewhere");
        stopWords.add("enough");
        stopWords.add("entirely");
        stopWords.add("especially");
        stopWords.add("et");
        stopWords.add("etc");
        stopWords.add("even");
        stopWords.add("ever");
        stopWords.add("every");
        stopWords.add("everybody");
        stopWords.add("everyone");
        stopWords.add("everything");
        stopWords.add("everywhere");
        stopWords.add("ex");
        stopWords.add("exactly");
        stopWords.add("example");
        stopWords.add("except");
        stopWords.add("f");
        stopWords.add("far");
        stopWords.add("few");
        stopWords.add("fifth");
        stopWords.add("first");
        stopWords.add("five");
        stopWords.add("followed");
        stopWords.add("following");
        stopWords.add("follows");
        stopWords.add("for");
        stopWords.add("former");
        stopWords.add("formerly");
        stopWords.add("forth");
        stopWords.add("four");
        stopWords.add("from");
        stopWords.add("further");
        stopWords.add("furthermore");
        stopWords.add("g");
        stopWords.add("get");
        stopWords.add("gets");
        stopWords.add("getting");
        stopWords.add("given");
        stopWords.add("gives");
        stopWords.add("go");
        stopWords.add("goes");
        stopWords.add("going");
        stopWords.add("gone");
        stopWords.add("got");
        stopWords.add("gotten");
        stopWords.add("greetings");
        stopWords.add("h");
        stopWords.add("had");
        stopWords.add("hadn't");
        stopWords.add("happens");
        stopWords.add("hardly");
        stopWords.add("has");
        stopWords.add("hasn't");
        stopWords.add("have");
        stopWords.add("haven't");
        stopWords.add("having");
        stopWords.add("he");
        stopWords.add("he's");
        stopWords.add("hello");
        stopWords.add("help");
        stopWords.add("hence");
        stopWords.add("her");
        stopWords.add("here");
        stopWords.add("here's");
        stopWords.add("hereafter");
        stopWords.add("hereby");
        stopWords.add("herein");
        stopWords.add("hereupon");
        stopWords.add("hers");
        stopWords.add("herself");
        stopWords.add("hi");
        stopWords.add("him");
        stopWords.add("himself");
        stopWords.add("his");
        stopWords.add("hither");
        stopWords.add("hopefully");
        stopWords.add("how");
        stopWords.add("howbeit");
        stopWords.add("however");
        stopWords.add("i");
        stopWords.add("i'd");
        stopWords.add("i'll");
        stopWords.add("i'm");
        stopWords.add("i've");
        stopWords.add("ie");
        stopWords.add("if");
        stopWords.add("ignored");
        stopWords.add("immediate");
        stopWords.add("in");
        stopWords.add("inasmuch");
        stopWords.add("inc");
        stopWords.add("indeed");
        stopWords.add("indicate");
        stopWords.add("indicated");
        stopWords.add("indicates");
        stopWords.add("inner");
        stopWords.add("insofar");
        stopWords.add("instead");
        stopWords.add("into");
        stopWords.add("inward");
        stopWords.add("is");
        stopWords.add("isn't");
        stopWords.add("it");
        stopWords.add("it'd");
        stopWords.add("it'll");
        stopWords.add("it's");
        stopWords.add("its");
        stopWords.add("itself");
        stopWords.add("j");
        stopWords.add("just");
        stopWords.add("k");
        stopWords.add("keep");
        stopWords.add("keeps");
        stopWords.add("kept");
        stopWords.add("know");
        stopWords.add("knows");
        stopWords.add("known");
        stopWords.add("l");
        stopWords.add("last");
        stopWords.add("lately");
        stopWords.add("later");
        stopWords.add("latter");
        stopWords.add("latterly");
        stopWords.add("least");
        stopWords.add("less");
        stopWords.add("lest");
        stopWords.add("let");
        stopWords.add("let's");
        stopWords.add("like");
        stopWords.add("liked");
        stopWords.add("likely");
        stopWords.add("little");
        stopWords.add("look");
        stopWords.add("looking");
        stopWords.add("looks");
        stopWords.add("ltd");
        stopWords.add("m");
        stopWords.add("mainly");
        stopWords.add("many");
        stopWords.add("may");
        stopWords.add("maybe");
        stopWords.add("me");
        stopWords.add("mean");
        stopWords.add("meanwhile");
        stopWords.add("merely");
        stopWords.add("might");
        stopWords.add("more");
        stopWords.add("moreover");
        stopWords.add("most");
        stopWords.add("mostly");
        stopWords.add("much");
        stopWords.add("must");
        stopWords.add("my");
        stopWords.add("myself");
        stopWords.add("n");
        stopWords.add("name");
        stopWords.add("namely");
        stopWords.add("nd");
        stopWords.add("near");
        stopWords.add("nearly");
        stopWords.add("necessary");
        stopWords.add("need");
        stopWords.add("needs");
        stopWords.add("neither");
        stopWords.add("never");
        stopWords.add("nevertheless");
        stopWords.add("new");
        stopWords.add("next");
        stopWords.add("nine");
        stopWords.add("no");
        stopWords.add("nobody");
        stopWords.add("non");
        stopWords.add("none");
        stopWords.add("noone");
        stopWords.add("nor");
        stopWords.add("normally");
        stopWords.add("not");
        stopWords.add("nothing");
        stopWords.add("novel");
        stopWords.add("now");
        stopWords.add("nowhere");
        stopWords.add("o");
        stopWords.add("obviously");
        stopWords.add("of");
        stopWords.add("off");
        stopWords.add("often");
        stopWords.add("oh");
        stopWords.add("ok");
        stopWords.add("okay");
        stopWords.add("old");
        stopWords.add("on");
        stopWords.add("once");
        stopWords.add("one");
        stopWords.add("ones");
        stopWords.add("only");
        stopWords.add("onto");
        stopWords.add("or");
        stopWords.add("other");
        stopWords.add("others");
        stopWords.add("otherwise");
        stopWords.add("ought");
        stopWords.add("our");
        stopWords.add("ours");
        stopWords.add("ourselves");
        stopWords.add("out");
        stopWords.add("outside");
        stopWords.add("over");
        stopWords.add("overall");
        stopWords.add("own");
        stopWords.add("p");
        stopWords.add("particular");
        stopWords.add("particularly");
        stopWords.add("per");
        stopWords.add("perhaps");
        stopWords.add("placed");
        stopWords.add("please");
        stopWords.add("plus");
        stopWords.add("possible");
        stopWords.add("presumably");
        stopWords.add("probably");
        stopWords.add("provides");
        stopWords.add("q");
        stopWords.add("que");
        stopWords.add("quite");
        stopWords.add("qv");
        stopWords.add("r");
        stopWords.add("rather");
        stopWords.add("rd");
        stopWords.add("re");
        stopWords.add("really");
        stopWords.add("reasonably");
        stopWords.add("regarding");
        stopWords.add("regardless");
        stopWords.add("regards");
        stopWords.add("relatively");
        stopWords.add("respectively");
        stopWords.add("right");
        stopWords.add("s");
        stopWords.add("said");
        stopWords.add("same");
        stopWords.add("saw");
        stopWords.add("say");
        stopWords.add("saying");
        stopWords.add("says");
        stopWords.add("second");
        stopWords.add("secondly");
        stopWords.add("see");
        stopWords.add("seeing");
        stopWords.add("seem");
        stopWords.add("seemed");
        stopWords.add("seeming");
        stopWords.add("seems");
        stopWords.add("seen");
        stopWords.add("self");
        stopWords.add("selves");
        stopWords.add("sensible");
        stopWords.add("sent");
        stopWords.add("serious");
        stopWords.add("seriously");
        stopWords.add("seven");
        stopWords.add("several");
        stopWords.add("shall");
        stopWords.add("she");
        stopWords.add("should");
        stopWords.add("shouldn't");
        stopWords.add("since");
        stopWords.add("six");
        stopWords.add("so");
        stopWords.add("some");
        stopWords.add("somebody");
        stopWords.add("somehow");
        stopWords.add("someone");
        stopWords.add("something");
        stopWords.add("sometime");
        stopWords.add("sometimes");
        stopWords.add("somewhat");
        stopWords.add("somewhere");
        stopWords.add("soon");
        stopWords.add("sorry");
        stopWords.add("specified");
        stopWords.add("specify");
        stopWords.add("specifying");
        stopWords.add("still");
        stopWords.add("sub");
        stopWords.add("such");
        stopWords.add("sup");
        stopWords.add("sure");
        stopWords.add("t");
        stopWords.add("t's");
        stopWords.add("take");
        stopWords.add("taken");
        stopWords.add("tell");
        stopWords.add("tends");
        stopWords.add("th");
        stopWords.add("than");
        stopWords.add("thank");
        stopWords.add("thanks");
        stopWords.add("thanx");
        stopWords.add("that");
        stopWords.add("that's");
        stopWords.add("thats");
        stopWords.add("the");
        stopWords.add("their");
        stopWords.add("theirs");
        stopWords.add("them");
        stopWords.add("themselves");
        stopWords.add("then");
        stopWords.add("thence");
        stopWords.add("there");
        stopWords.add("there's");
        stopWords.add("thereafter");
        stopWords.add("thereby");
        stopWords.add("therefore");
        stopWords.add("therein");
        stopWords.add("theres");
        stopWords.add("thereupon");
        stopWords.add("these");
        stopWords.add("they");
        stopWords.add("they'd");
        stopWords.add("theyll");
        stopWords.add("theyre");
        stopWords.add("theyve");
        stopWords.add("think");
        stopWords.add("third");
        stopWords.add("this");
        stopWords.add("thorough");
        stopWords.add("thoroughly");
        stopWords.add("those");
        stopWords.add("though");
        stopWords.add("three");
        stopWords.add("through");
        stopWords.add("throughout");
        stopWords.add("thru");
        stopWords.add("thus");
        stopWords.add("to");
        stopWords.add("together");
        stopWords.add("too");
        stopWords.add("took");
        stopWords.add("toward");
        stopWords.add("towards");
        stopWords.add("tried");
        stopWords.add("tries");
        stopWords.add("truly");
        stopWords.add("try");
        stopWords.add("trying");
        stopWords.add("twice");
        stopWords.add("two");
        stopWords.add("u");
        stopWords.add("un");
        stopWords.add("under");
        stopWords.add("unfortunately");
        stopWords.add("unless");
        stopWords.add("unlikely");
        stopWords.add("until");
        stopWords.add("unto");
        stopWords.add("up");
        stopWords.add("upon");
        stopWords.add("us");
        stopWords.add("use");
        stopWords.add("used");
        stopWords.add("useful");
        stopWords.add("uses");
        stopWords.add("using");
        stopWords.add("usually");
        stopWords.add("uucp");
        stopWords.add("v");
        stopWords.add("value");
        stopWords.add("various");
        stopWords.add("very");
        stopWords.add("via");
        stopWords.add("viz");
        stopWords.add("vs");
        stopWords.add("w");
        stopWords.add("want");
        stopWords.add("wants");
        stopWords.add("was");
        stopWords.add("wasn't");
        stopWords.add("way");
        stopWords.add("we");
        stopWords.add("we'd");
        stopWords.add("we'll");
        stopWords.add("we're");
        stopWords.add("we've");
        stopWords.add("welcome");
        stopWords.add("well");
        stopWords.add("went");
        stopWords.add("were");
        stopWords.add("weren't");
        stopWords.add("what");
        stopWords.add("what's");
        stopWords.add("whatever");
        stopWords.add("when");
        stopWords.add("whence");
        stopWords.add("whenever");
        stopWords.add("where");
        stopWords.add("where's");
        stopWords.add("whereafter");
        stopWords.add("whereas");
        stopWords.add("whereby");
        stopWords.add("wherein");
        stopWords.add("whereupon");
        stopWords.add("wherever");
        stopWords.add("whether");
        stopWords.add("which");
        stopWords.add("while");
        stopWords.add("whither");
        stopWords.add("who");
        stopWords.add("who's");
        stopWords.add("whoever");
        stopWords.add("whole");
        stopWords.add("whom");
        stopWords.add("whose");
        stopWords.add("why");
        stopWords.add("will");
        stopWords.add("willing");
        stopWords.add("wish");
        stopWords.add("with");
        stopWords.add("within");
        stopWords.add("without");
        stopWords.add("won't");
        stopWords.add("wonder");
        stopWords.add("would");
        stopWords.add("would");
        stopWords.add("wouldnt");
        stopWords.add("x");
        stopWords.add("y");
        stopWords.add("yes");
        stopWords.add("yet");
        stopWords.add("you");
        stopWords.add("you'd");
        stopWords.add("you'll");
        stopWords.add("you're");
        stopWords.add("youve");
        stopWords.add("your");
        stopWords.add("yours");
        stopWords.add("yourself");
        stopWords.add("yourselves");
        stopWords.add("z");
        stopWords.add("zero");
        stopWords.add("P");
        Iterator<String> iter = stopWords.iterator();
        while (iter.hasNext()) {
            String currKey = iter.next();
            stemmer.setCurrent(currKey);
            stemmer.stem();
            currKey = stemmer.getCurrent();
            stopWordsAfterStem.add(currKey);
        }
    }


    public HashMap<String, TermDetails> getTerms() {
        return terms;
    }


    /**
     * parsing chunk of docs (about 3000)
     *
     * @param allDocs      - the chunk of docs including their id
     * @param currOurDocID - we gave to every doc id - simple integer
     * @return return hashmap of the terms after saving information about them
     */
    public HashMap parsePartOfDocs(HashMap<Integer, Document> allDocs, int currOurDocID) {
        terms.clear();
        if (stem) {
            docInfoFile = new File(postingPath + "\\Documents_Information.txt");
        } else {
            docInfoFile = new File(postingPath + "\\Documents_Information_NoStem.txt");
        }
        for (int i = currOurDocID; i < currOurDocID + allDocs.size(); i++) {
            currDoc = allDocs.get(i);
            parseDoc(currDoc);
        }
        FileWriter docWriter = null;
        try {
            docWriter = new FileWriter(docInfoFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = currOurDocID; i < currOurDocID + allDocs.size(); i++) {
            try {
                docWriter.write(allDocs.get(i).getOurDocID() + "|" + allDocs.get(i).getDocID() + "|"+allDocs.get(i).getTitle()+"|" + allDocs.get(i).getLanguage() + "|" + allDocs.get(i).getNumOfTotalWords() + "|" + allDocs.get(i).getNumOfTerms() + "|" + allDocs.get(i).getTermWithMaxFreq() + "|" + allDocs.get(i).getMaxTermFreq() + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            docWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(celebTerms.size());
        return terms;
    }


    /**
     * parsing specific doc using regex and patterns to identify which term it is
     *
     * @param doc - the document
     */
    public void parseDoc(Document doc) {

        String text = doc.getText();
        String[] docWords = text.split(" +");
        index = 0;

        while (index < docWords.length) {
            docWords[index] = docWords[index].replaceAll("[^a-zA-Z0-9-.\\/]", "");
            if (docWords[index].equals("") || docWords[index].contains("<") || docWords[index].contains(">")) {
                index++;
            } else if ((docWords[index].charAt(docWords[index].length() - 1) == 34 || (docWords[index].charAt(docWords[index].length() - 1) == 41) || (docWords[index].charAt(docWords[index].length() - 1) == 63) || (docWords[index].charAt(docWords[index].length() - 1) == 46) || (docWords[index].charAt(docWords[index].length() - 1) == 44) || (docWords[index].charAt(docWords[index].length() - 1) == 33) || (docWords[index].charAt(docWords[index].length() - 1) == 58))) {
                docWords[index] = docWords[index].substring(0, docWords[index].length() - 1);
            } else if (docWords[index].contains("|")) {
                docWords[index] = docWords[index].replaceAll("\\|", "");
            } else if ((docWords[index].charAt(0) == 34 || (docWords[index].charAt(0) == 40))) {
                docWords[index] = docWords[index].substring(1);
            } else {
                if (docWords[index].charAt(docWords[index].length() - 1) == 46) {
                    docWords[index] = docWords[index].substring(0, docWords[index].length() - 1);
                } else if ((Character.isDigit(docWords[index].charAt(0))) || docWords[index].charAt(0) == 36) {
                    numbersRule(docWords);
                } else if (Character.isLetter(docWords[index].charAt(0))) {
                    wordsRule(docWords);
                } else if ((docWords[index].charAt(0) == 39)) {
                    if (docWords[index].length() > 1) {
                        if (docWords[index].charAt(docWords[index].length() - 1) == 39) {
                            String str = docWords[index].substring(1, docWords[index].length() - 1);       /**our rule*/
                            if (str.length() > 0) {
                                addTermToHashMap(str, doc.getOurDocID());
                            }
                        }
                    }
                    index++;
                } else {
                    index++;
                }
            }
        }
        /**STOP WORDS*/

        Iterator<String> iter;
        if (stem) {
            iter = stopWordsAfterStem.iterator();
        } else {
            iter = stopWords.iterator();
        }
        while (iter.hasNext()) {
            String strStop = iter.next();
            if (terms.containsKey(strStop) || terms.containsKey(strStop.toUpperCase())) {
                terms.remove(strStop);
            }
            if (termsOfSpecificDoc.containsKey(strStop) || termsOfSpecificDoc.containsKey(strStop.toUpperCase())) {
                termsOfSpecificDoc.remove(strStop);
            }
        }

        TreeMap<String, Integer> sortedMap = sortMapByValue(termsOfSpecificDoc);
        for (String key : sortedMap.keySet()) {
            currDoc.setMaxTermFreq(termsOfSpecificDoc.get(key));
            currDoc.setTermWithMaxFreq(key);
            break;
        }
        currDoc.setNumOfTerms(termsOfSpecificDoc.size());
        currDoc.setNumOfTotalWords(index);

//        try {
//            FileWriter docWriter = new FileWriter(docInfoFile,true);
//            docWriter.write(currDoc.getOurDocID()+"|"+currDoc.getLanguage()+"|"+currDoc.getNumOfTotalWords()+"|"+currDoc.getNumOfTerms()+"|"+currDoc.getTermWithMaxFreq()+"|"+currDoc.getMaxTermFreq() + System.lineSeparator());
//            docWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        sortedMap.clear();
        termsOfSpecificDoc.clear();


    }


    /**
     * adding term to hashmap of entities that contains all the entities in the corpus
     *
     * @param term  - term/entity to add
     * @param docId - from whch doc it is
     */
    private void addTermToCelebHashMap(String term, int docId) {


        if (term.charAt(term.length() - 1) == 44) {
            term = term.substring(0, term.length() - 1);
        }
        if (celebTerms.containsKey(term)) {
            celebTerms.get(term.toUpperCase()).addToPost(docId);
        } else {
            celebTerms.put(term.toUpperCase(), new TermDetails(docId));
        }
    }


    /**
     * adding term to regular hashmap that contains all the terms of the chunk of docs
     *
     * @param term  - term to add
     * @param docId - - from whch doc it is
     */
    private void addTermToHashMap(String term, int docId) {
        /**STEM */
        if (stem) {
            if (term.length() > 1) {
                stemmer.setCurrent(term);
                stemmer.stem();
                term = stemmer.getCurrent();
            }
        }
        /**lowerUpperLaw*/
        if (!Character.isLetter(term.charAt(term.length() - 1)) && !(Character.isDigit(term.charAt(term.length() - 1)))) {
            term = term.substring(0, term.length() - 1);
        }

        if (!termsOfSpecificDoc.containsKey(term.toLowerCase())) {
            termsOfSpecificDoc.put(term.toLowerCase(), 1);
        } else {
            int termOfSpecificDocFreq = termsOfSpecificDoc.get(term.toLowerCase());
            termsOfSpecificDoc.remove(term);
            termsOfSpecificDoc.put(term.toLowerCase(), termOfSpecificDocFreq + 1);
        }
        if ((!terms.containsKey((term.toUpperCase())) && (!terms.containsKey(term.toLowerCase())))) {
            if (Character.isUpperCase(term.charAt(0))) {
                terms.put(term.toUpperCase(), new TermDetails(docId));
            } else {
                terms.put(term.toLowerCase(), new TermDetails(docId));
            }
        } else if (Character.isUpperCase(term.charAt(0))) {
            if (terms.containsKey(term.toLowerCase())) {
                terms.get(term.toLowerCase()).addToPost(docId);
            } else {
                terms.get(term.toUpperCase()).addToPost(docId);
            }
        } else {
            if (terms.containsKey(term.toUpperCase())) {                             // if upper case is already in the dic
                TermDetails pos = terms.remove(term.toUpperCase());
                pos.addToPost(currDoc.getOurDocID());
                terms.put(term.toLowerCase(), pos);
            } else {
                terms.get(term.toLowerCase()).addToPost(docId);
            }
        }
    }


    /**
     * this func is created to identfy pattern of words and how to insert them to the terms
     *
     * @param docWords - all the words of doc
     */
    private void wordsRule(String[] docWords) {
        if (Character.isUpperCase(docWords[index].charAt(0)) && Character.isUpperCase(docWords[checkRange(docWords, 1)].charAt(0))) {                                                                      //yishut
            int incrementIndex = 1;
            String tmp = docWords[index];
            docWords[index] = docWords[index].replaceAll("[^a-zA-Z0-9-.\\/]", "");
            if (docWords.length <= index + incrementIndex) {
            } else {
                addTermToHashMap(docWords[index], currDoc.getOurDocID());
                while (Character.isUpperCase(docWords[incrementIndex + index].charAt(0))) {
                    docWords[index + incrementIndex] = docWords[index + incrementIndex].replaceAll("[^a-zA-Z0-9-.\\/]", "");
                    addTermToHashMap(docWords[index + incrementIndex], currDoc.getOurDocID());
                    tmp += " " + docWords[incrementIndex + index];
                    incrementIndex++;
                    if (docWords.length <= index + incrementIndex) {
                        break;
                    }
                }
            }
            addTermToCelebHashMap(tmp, currDoc.getOurDocID());
            index = index + incrementIndex;
        } else if (docWords[index].contains("-")) {
            addTermToHashMap(docWords[index], currDoc.getOurDocID());
            index++;
        } else if (docWords[index].equals("Between") || docWords[index].equals("between")) {
            addTermToHashMap(betweenFunc(docWords), currDoc.getOurDocID());
            index = index + 4;
        } else if (months.containsKey(docWords[index])) {
            if (dateConverter(docWords[index + 1], docWords[index]).equals(docWords[index])) {
                addTermToHashMap(dateConverter(docWords[index + 1], docWords[index]), currDoc.getOurDocID());
                index++;
            } else {
                addTermToHashMap(dateConverter(docWords[index + 1], docWords[index]), currDoc.getOurDocID());
                index = index + 2;
            }
        } else {
            /**STEM  !!! */
            if (stem) {
                if (docWords[index].length() > 1) {
                    stemmer.setCurrent(docWords[index]);
                    stemmer.stem();
                    docWords[index] = stemmer.getCurrent();
                }
            }
            if (docWords[index].length() > 1) {
                if (!Character.isLetter(docWords[index].charAt(docWords[index].length() - 1)) && !(Character.isDigit(docWords[index].charAt(docWords[index].length() - 1)))) {
                    docWords[index] = docWords[index].substring(0, docWords[index].length() - 1);
                }
            }
            if ((!terms.containsKey((docWords[index].toUpperCase())) && (!terms.containsKey(docWords[index].toLowerCase())))) {
                if (Character.isUpperCase(docWords[index].charAt(0))) {
                    addTermToHashMap(docWords[index].toUpperCase(), currDoc.getOurDocID());
                    index++;
                } else {
                    addTermToHashMap(docWords[index].toLowerCase(), currDoc.getOurDocID());
                    index++;
                }
            } else if (Character.isUpperCase(docWords[index].charAt(0))) {
                if (terms.containsKey(docWords[index].toLowerCase())) {
                    addTermToHashMap(docWords[index].toLowerCase(), currDoc.getOurDocID());
                } else {
                    addTermToHashMap(docWords[index].toUpperCase(), currDoc.getOurDocID());
                }
                index++;
            } else {
                if (terms.containsKey(docWords[index].toUpperCase())) {                             // if upper case is already in the dic
                    TermDetails pos = terms.remove(docWords[index].toUpperCase());
                    pos.addToPost(currDoc.getOurDocID());
                    terms.put(docWords[index].toLowerCase(), pos);   //// ////////// /////// /////// //////// ///////Steaming
                    index++;
                } else {
                    addTermToHashMap(docWords[index].toLowerCase(), currDoc.getOurDocID());
                    index++;
                }
            }
        }
    }


    /**
     * law of ranges
     *
     * @param docWords - all the words the the doc
     * @return the string that will be the term
     */
    private String betweenFunc(String[] docWords) {

        docWords[index] = docWords[index].replaceAll("[^a-zA-Z0-9-.\\/]", "");
        if (docWords.length <= index + 3) {
            if (!Character.isDigit(docWords[index].charAt(0))) {
                return docWords[index];
            }
            docWords[index + 3] = docWords[index + 3].replaceAll("[^a-zA-Z0-9-.\\/]", "");
            docWords[index + 1] = docWords[index + 1].replaceAll("[^a-zA-Z0-9-.\\/]", "");
            return docWords[index + 1] + "-" + docWords[index + 3];
        }
        return docWords[index];
    }


    /**
     * this func is created to identfy pattern of numbers and how to insert them to the terms
     *
     * @param docWords - all the words the the doc
     */
    private void numbersRule(String[] docWords) {

        if (docWords[index].contains("-")) {
            wordsRule(docWords);
            return;
        }
        if (docWords[checkRange(docWords, 1)].equals("GMT")) {
            if (docWords[index].length() < 4) {
                regularNumFormat(docWords);
                return;
            }
            Double hours = strToDub(docWords[index].substring(0, 2));
            Double minutes = strToDub(docWords[index].substring(2, 4));
            boolean pm = false;
            if (hours > 11) {
                pm = true;
            }
            String hourStr = "";
            if (pm) {
                hours = hours - 12;
                String str = hours.toString().substring(0, (hours.toString()).length() - 2);
                if (hours < 10) {
                    str = "0" + str;
                }
                String strMin = minutes.toString().substring(0, (minutes.toString()).length() - 2);
                if (minutes < 10) {
                    strMin = "0" + strMin;
                }
                hourStr = str + ":" + strMin + " PM";
            } else {
                String str = hours.toString().substring(0, (hours.toString()).length() - 2);
                if (hours < 10) {
                    str = "0" + str;
                }
                String strMin = minutes.toString().substring(0, (minutes.toString()).length() - 2);
                if (minutes < 10) {
                    strMin = "0" + strMin;
                }

                hourStr = str + ":" + strMin + " AM";
            }
            addTermToHashMap(hourStr, currDoc.getOurDocID());
            index = index + 2;
        } else if (percent.contains(docWords[checkRange(docWords, 1)]) || docWords[index].charAt(docWords[index].length() - 1) == 37) {             //Percent
            if (docWords[index].charAt(docWords[index].length() - 1) == 37) {
                addTermToHashMap(docWords[index], currDoc.getOurDocID());
                index++;
            } else {
                addTermToHashMap(docWords[index] + "%", currDoc.getOurDocID());
                index = index + 2;
            }
        }                                       // end of percent
        else if (months.containsKey(docWords[checkRange(docWords, 1)])) {                         // Dates
            addTermToHashMap(dateConverter(docWords[index], docWords[index + 1]), currDoc.getOurDocID());
            if ((dateConverter(docWords[index], docWords[index + 1]).equals(docWords[index + 1]))) {
                addTermToHashMap(dateConverter(docWords[index], docWords[index + 1]), currDoc.getOurDocID());
                index++;
            } else {
                addTermToHashMap(dateConverter(docWords[index + 1], docWords[index]), currDoc.getOurDocID());
                index = index + 2;
            }
        }//end of Dates    //prices:
        else if (prices.contains(docWords[checkRange(docWords, 3)]) || prices.contains(docWords[checkRange(docWords, 2)]) || prices.contains(docWords[checkRange(docWords, 1)]) || (isFraction(docWords[checkRange(docWords, 1)]) && indexPlusTwo(docWords)) || docWords[index].charAt(0) == 36) {          //prices
            addTermToHashMap(priceFormatConverter(docWords), currDoc.getOurDocID());
        } else if (Character.isLetter(docWords[index].charAt(docWords[index].length() - 1))) {
            addTermToHashMap(docWords[index], currDoc.getOurDocID());
            index++;
        } else if (distances.contains(docWords[checkRange(docWords, 1)])) {
            addTermToHashMap(distanceFormatConverter(docWords), currDoc.getOurDocID());
        } else {
            addTermToHashMap(regularNumFormat(docWords), currDoc.getOurDocID());
        }
    }


    /**
     * checking if two words after the current is inside the bounds
     *
     * @param docWords - all the words the the doc
     * @return - true if there is no bound over flow
     */
    private boolean indexPlusTwo(String[] docWords) {
        if (index + 2 < docWords.length) {
            if (prices.contains(docWords[index + 2])) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return the hashmap of entities
     */
    public HashMap<String, TermDetails> getCelebTerms() {
        return celebTerms;
    }


    /**
     * OUR RULE - converting all distance units to same pattern units
     *
     * @param docWords - all the words the the doc
     * @return - the string that will be the term
     */
    private String distanceFormatConverter(String[] docWords) {

        double num = strToDub(docWords[index]);
        if (docWords[index + 1].equals("meter") || docWords[index + 1].equals("Meter")) {
            index = index + 2;
            return clearDotsAndZeros(num + "") + " Meter";
        } else {
            num = num * 1000;
            index = index + 2;
            return clearDotsAndZeros(num + "") + " Meter";
        }
    }


    /**
     * Price rule - converting all prices units to same pattern units
     *
     * @param docWords - all the words the the doc
     * @return - the string that will be the term
     */
    private String priceFormatConverter(String[] docWords) {
        String price = docWords[index];
        if (docWords[index].charAt(0) == 36) {                                                          /// prices
            price = docWords[index].substring(1);
        }
        Double doubPrice = strToDub(price);
        if (doubPrice >= 1000000) {
            doubPrice = doubPrice / 1000000;
            String tmp = (clearDotsAndZeros(doubPrice.toString()) + " M Dollars");
            if (docWords[checkRange(docWords, 1)].equals("Dollars")) {
                index = index + 2;
            } else {
                index++;
            }
            return tmp;

        } else if (docWords[checkRange(docWords, 1)].equals("million") || docWords[checkRange(docWords, 1)].equals("m")) {
            String tmp = new String(clearDotsAndZeros(doubPrice.toString()) + " M Dollars");
            if (docWords[checkRange(docWords, 1)].equals("m")) {
                index = index + 3;
            } else if (index + 3 < docWords.length) {
                if (docWords[checkRange(docWords, 2)].equals("U.S.")) {
                    index = index + 4;
                } else {
                    index = index + 2;
                }
            } else {
                index = index + 2;
            }
            return tmp;

        } else if (docWords[checkRange(docWords, 1)].equals("billion") || docWords[checkRange(docWords, 1)].equals("bn")) {
            doubPrice = doubPrice * 1000;
            String tmp = new String(clearDotsAndZeros(doubPrice.toString()) + " M Dollars");
            if (docWords[checkRange(docWords, 1)].equals("bn")) {
                index = index + 3;
            } else if (index + 3 < docWords.length) {
                if (docWords[index + 2].equals("U.S.")) {
                    index = index + 4;
                } else {
                    index = index + 2;
                }
            } else {
                index = index + 2;
            }
            return tmp;
        } else if (docWords[checkRange(docWords, 1)].equals("trillion")) {
            doubPrice = doubPrice * 1000000;
            String tmp = new String(clearDotsAndZeros(doubPrice.toString()) + " M Dollars");
            index = index + 4;
            return tmp;
        } else {
            if (isFraction(docWords[checkRange(docWords, 1)])) {
                String tmp = new String(clearDotsAndZeros(doubPrice.toString()) + " " + docWords[index + 1] + " Dollars");
                index = index + 3;
                return tmp;

            } else if (docWords[checkRange(docWords, 1)].equals("Dollars")) {
                String tmp = new String(clearDotsAndZeros(doubPrice.toString()) + " Dollars");
                index = index + 2;
                return tmp;
            } else {
                String tmp = new String(clearDotsAndZeros(doubPrice.toString()) + " Dollars");
                index++;
                return tmp;
            }

        }                                                                                       ///// end of prices

    }


    /**
     * checking bounds for the sake of not over flow after them.
     *
     * @param words         - all the words the the doc
     * @param optionalIndex - check if the index is not bigger than the doc
     * @return - true if it is inside the bounds
     */
    private int checkRange(String[] words, int optionalIndex) {
        if ((index + optionalIndex) < words.length) {
            return (index + optionalIndex);
        }
        return index;
    }


    /**
     * @param str - optional term
     * @return - if the specific number is fraction
     */
    private boolean isFraction(String str) {
        if (str.contains("/") && ((str.charAt(0) > 47 && str.charAt(0) < 58))) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Date rule - converting all dates units to same pattern units
     *
     * @param numDay - the num in the date
     * @param month  - the month in the date
     * @return - the string that will be the term
     */
    private String dateConverter(String numDay, String month) {
        String monthStr = months.get(month);
        if (!Character.isDigit(numDay.charAt(0))) {
            return month;
        }
        numDay = numDay.replaceAll("[^a-zA-Z0-9-.\\/]", "");
        if (numDay.length() > 2) {
            return numDay + "-" + monthStr;
        } else {
            if (numDay.length() != 1) {
                return monthStr + "-" + numDay;
            } else {
                return monthStr + "-0" + numDay;
            }
        }
    }

    /**
     * Regular number rule - converting all numbers units to same pattern units
     *
     * @param docWords - all the words the the doc
     * @return - the string that will be the term
     */
    private String regularNumFormat(String[] docWords) {
        Double numValue = strToDub(docWords[index]);
        if (numValue < 1000) {
            if (isFraction(docWords[checkRange(docWords, 1)])) {
                if (isFraction(docWords[index])) {
                    String str = docWords[index];
                    index++;
                    return str;
                }
                String str = clearDotsAndZeros(numValue.toString()) + " " + docWords[index + 1];
                index = index + 2;
                return str;
            } else if (docWords[checkRange(docWords, 1)].equals("Thousand")) {
                String str = numValue.toString();
                str = clearDotsAndZeros(str);
                index = index + 2;
                return str + "K";
            } else if (docWords[checkRange(docWords, 1)].equals("Million")) {
                String str = numValue.toString();
                str = clearDotsAndZeros(str);
                index = index + 2;
                return str + "M";
            } else if (docWords[checkRange(docWords, 1)].equals("Billion")) {
                String str = numValue.toString();
                str = clearDotsAndZeros(str);
                index = index + 2;
                return str + "B";
            } else {
                index++;
                String str = numValue.toString();
                str = clearDotsAndZeros(str);
                return str;
            }
        } else if (numValue < 1000000) {
            index++;
            numValue = numValue / 1000;
            String str = numValue.toString();
            str = clearDotsAndZeros(str);
            return str + "K";
        } else if (numValue < 1000000000) {
            index++;
            numValue = numValue / 1000000;
            String str = numValue.toString();
            int dotIndex = str.indexOf(".");
            str = clearDotsAndZeros(str);
            return str + "M";
        } else {
            index++;
            numValue = numValue / 1000000000;
            String str = numValue.toString();
            str = clearDotsAndZeros(str);
            return str + "B";
        }
    }


    /**
     * this func is clearing the dots and zeros that exists in double units
     *
     * @param str - the optional term
     * @return - the string that will be the term
     */
    private String clearDotsAndZeros(String str) {

        int dotIndex = str.indexOf(".");
        if (str.length() > 5 && dotIndex + 4 < str.length()) {
            str = str.substring(0, dotIndex + 4);
            while (str.charAt(str.length() - 1) == '0') {
                str = str.substring(0, str.length() - 1);
            }
        }
        while (str.charAt(str.length() - 1) == '0') {
            str = str.substring(0, str.length() - 1);
        }
        if (str.charAt(str.length() - 1) == '.') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    /**
     * this func is converting the string into double for later manipulation
     *
     * @param str - the optional term
     * @return - the double that need to be manipulated
     */
    private double strToDub(String str) {
        if (str.contains(",") && str.contains(".")) {
            str = str.replaceAll(",", "");
            String[] twoVal = str.split("\\.");                                                           // num that include "." N ","
            try {
                Double val = Double.parseDouble(twoVal[0]);
                String second = "0." + twoVal[1];
                if (second.length() > 5) {
                    second = second.substring(0, 5);
                }
                Double val_2 = Double.parseDouble(second);
                return val + val_2;
            } catch (NumberFormatException e) {

            }
        } else if (str.contains(",")) {
            str = str.replaceAll(",", "");
            try {
                Double val = Double.parseDouble(str);
                return val;
            } catch (NumberFormatException e) {

            }
        } else if (str.contains(".")) {
            if (str.charAt(str.length() - 1) == '.') {
                return Double.parseDouble(str.substring(0, str.length() - 1));
            }
            String[] twoVal = str.split("\\.");
            try {
                Double val = Double.parseDouble(twoVal[0]);
                String second = "0." + twoVal[1];
                if (second.length() > 5) {
                    second = second.substring(0, 5);
                }
                Double val_2 = Double.parseDouble(second);
                return val + val_2;
            } catch (NumberFormatException e) {

            }
        }
        Double val = 0.0;
        try {
            val = Double.parseDouble(str);

        } catch (NumberFormatException e) {

        }
        return val;
    }

    /**
     * clearing the hashmap of specific chunk to be ready for the next
     */
    public void clearTerms() {
        terms.clear();
    }


    /**
     * this func is sorting the terms by alphabetic order
     *
     * @param map - the hashmap that need to be sorted
     * @return
     */
    public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map) {
        Comparator<String> comparator = new ValueComparator(map);

        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }

    /**
     * clearing the entities hashmap of specific chunk to be ready for the next
     */
    public File changeSizeCelebs(int celebPostingIndex, String postingPath) {
        Map<String, TermDetails> sortedCurrTerms = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                int cmp = s.compareToIgnoreCase(t1);
                if (cmp != 0) {
                    return cmp;
                }
                return s.compareTo(t1);
            }
        });
        sortedCurrTerms.putAll(celebTerms);
        File celebPostingFile = new File(postingPath + "\\celebPosting" + celebPostingIndex + ".txt");
        try {
            celebPostingFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(celebPostingFile);
            for (String key : sortedCurrTerms.keySet()) {
                writer.write(key + " |");
                writer.write(sortedCurrTerms.get(key).getTotalFreqInCorpus() + "| |" + sortedCurrTerms.get(key).getNumOfDocsFreq() + "| ");
                HashMap<Integer, Integer> id_Freq = sortedCurrTerms.get(key).getId_freq();
                for (Integer docID : sortedCurrTerms.get(key).getId_freq().keySet()) {
                    writer.write("(" + docID + " ," + id_Freq.get(docID) + ") ");
                }
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        celebTerms.clear();
        return celebPostingFile;
    }
}
class ValueComparator implements Comparator<String> {

    HashMap<String, Integer> map = new HashMap<String, Integer>();

    public ValueComparator(HashMap<String, Integer> map){
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


