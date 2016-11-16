package newschoolsearch7;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.JFileChooser;

public class NewSchoolSearch7 {

   
    public static void main(String[] args)throws IOException {
        
        
        FileInputStream schoolStream = new FileInputStream("C:/Users/emarcha/Desktop/SchoolSearch/NewSearch2/schoolListTest.txt");
        BufferedReader sr = new BufferedReader(new InputStreamReader(schoolStream));

        ArrayList<String> schoolList = new ArrayList<String>();

        String schools;
        //stores list of schools onto Arraylist
        while((schools = sr.readLine()) != null){
                schoolList.add(schools);
        }
        
        //passes each school name to the search method
        for(int i = 1; i !=0; i--){
            for(String elem : schoolList){
                SchoolSearch(elem, i);
            }
        }
        
        sr.close();      
    }
    
    private static void SchoolSearch(String school, int pass)throws IOException{
        String runs;
        
        if(pass == 3){
            runs = "FirstScan";
        }else if(pass == 2){
            runs = "SecondScan";
        }else{
            runs = "FinalScan";
        }
        
        String assoc = school;
        System.out.println("\tSearching in " + assoc);
        
        //Open txt file containing list of URLs
        FileInputStream fstream = new FileInputStream("C:/Users/emarcha/Desktop/SchoolSearch/NewSearch2/schools/"+ assoc + "schoolList.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        
        //Opens txt file containing the list of terms to search
        FileInputStream termStream = new FileInputStream("C:/Users/emarcha/Desktop/SchoolSearch/NewSearch2/termList.txt");
        BufferedReader tr = new BufferedReader(new InputStreamReader(termStream));
        
        //creates a txt file that list all of the school urls that contain a cms term
        PrintWriter foundFile = new PrintWriter("C:/Users/emarcha/Desktop/SchoolSearch/NewSearch2/results/"+ assoc + "/Found/"+ assoc +"ContainsAcms" + runs + ".txt");
        //creates a txt file that list all of the schools that do not contain a cms term
        PrintWriter notFoundFile = new PrintWriter("C:/Users/emarcha/Desktop/SchoolSearch/NewSearch2/results/" + assoc + "/NotFound/"+ runs + "DoesNotContainCMS" + pass + ".txt");
        
        //creates the arraylist that will contain the terms
        ArrayList<String> termList = new ArrayList<String>();
        //Stores the cms terms into the arraylist 
        String cms; 
        while((cms = tr.readLine()) != null){
            termList.add(cms);
        }
        
        String htmlstring;
        String urlstring;
        
        //reads the URLs off the school list 
        while((urlstring = br.readLine()) != null){
            //passes each URL to the getUrlContents method to read source code and store it into a string
            htmlstring = getUrlContents("http://"+urlstring);
            int check = 0;
            for(String elem : termList){
                //When a term is found then it will write the URL and the term found on the txt file
                if(regexSearch(elem,htmlstring)){
                    foundFile.println(urlstring + " contains \t" + elem);
                    System.out.println("Found " + elem + " in " + urlstring);
                    check++;
                }else if(containsSearch(elem,htmlstring)){
                    System.out.println("\tChecked using contains since matcher failed");
                    foundFile.println(urlstring + " contains \t" + elem);
                    check++; 
                }
            }
            //If no term if found then it will write the URL onto txt file
            if(check ==0){
                    notFoundFile.println(urlstring);
                    System.out.println("Nothing found at: " + urlstring);
                    System.out.println("\tChecked Twice!");
                }
        }
        
        System.out.println("\tFinished searching in " + assoc + "\n");;
        foundFile.close();
        notFoundFile.close();
        br.close();
        tr.close();
        
        
    }
    
    //searches source code using matcher method
    private static boolean regexSearch(String term,String sourceCode){
        Pattern pattern = Pattern.compile(term.toLowerCase());
        Matcher matcher = pattern.matcher(sourceCode.toLowerCase());
        return matcher.find();
    }
    
    //searches source code using contains method
    private static boolean containsSearch(String term, String sourceCode){
        
        return(sourceCode.toLowerCase().contains(term.toLowerCase()));
        
    }
    
    //method that reads a URL and converts the source code into a string for searching
    private static String getUrlContents(String theUrl)
  {
    StringBuilder content = new StringBuilder();

    // many of these calls can throw exceptions, so i've just
    // wrapped them all in one try/catch statement.
    try
    {
      // create a url object
      URL url = new URL(theUrl);

      // create a urlconnection object
      URLConnection urlConnection = url.openConnection();
      //Setting User-agent to look like normal browser
      urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");


      // wrap the urlconnection in a bufferedreader
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

      String line;

      // read from the urlconnection via the bufferedreader
      while ((line = bufferedReader.readLine()) != null)
      {
        content.append(line + "\n");
      }
      bufferedReader.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return content.toString();
  }
    
}


