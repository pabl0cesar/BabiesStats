/**
 * Prints out statistics about historic data of babies births and names.
 *
 * @author pabl0cesar
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File; 

public class BabiesStats {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoyNames = 0;
        int totalGirlNames = 0;
        int totalNames = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            totalNames+=1;
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                totalBoyNames+=1;
            }
            else {
                totalGirls += numBorn;
                totalGirlNames+=1;
            }
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("female girls = " + totalGirls);
        System.out.println("male boys = " + totalBoys);
        System.out.println("total names = " + totalNames);
        System.out.println("total boy names = " + totalBoyNames);
        System.out.println("total girl names = " + totalGirlNames);
        
    }
    
    public int rankName(int year, String name, String gender){
        int totalGender = 0;
        int rank=0;
        Boolean found = false;
        FileResource fr = new FileResource("data/yob"+year+".csv");
        
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (!found && rec.get(1).equals(gender)) {
                rank+=1;
                if(rec.get(0).equals(name)) found = true;
            }
        }
        if(found){return rank;}
        return -1;
    }
    
    public String getName(int year, int rank, String gender){
        int totalGender = 0;
        int count=0;
        FileResource fr = new FileResource("data/yob"+year+".csv");
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                totalGender+=1;}
            }
            
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender) && count < totalGender) {
                count+=1;
                if(count == rank) return rec.get(0);
            }
        }
        return "";
    }
    
    public String whatIsNameInYear(int year, int newYear, String name, String gender){
        String newName = getName(newYear,rankName(year,name,gender),gender);
        String heshe = "she"; if(gender.equals("M")) heshe="he";
        String tag = name+" born in "+year+" would be "+newName+" if "+heshe+" was born in "+newYear;
        if(!newName.isEmpty()){return tag;}
        return "";
    }
    
    public int yearOfHighestRank(String name, String gender){
        DirectoryResource dr = new DirectoryResource();
          
        int highestRank = Integer.MAX_VALUE;
        int yearHighestRank = 0;
        for(File f: dr.selectedFiles()){
            int year = Integer.parseInt(f.getName().replace("yob","").replace(".csv",""));
            int thisYearRank = rankName(year,name,gender);
            if (thisYearRank != -1 && thisYearRank < highestRank) {
                highestRank=thisYearRank;
                yearHighestRank=year;
            };
        }
        if(yearHighestRank > 0) return yearHighestRank;
        return -1;
    }
    
    public double getAvgRank(String name, String gender){
        int totalFiles = 0;
        int actFile = 0;
        DirectoryResource dr = new DirectoryResource();
          
        double totalRank = 0.0;
        for(File f: dr.selectedFiles()){
            totalFiles+=1;
            int year = Integer.parseInt(f.getName().replace("yob","").replace(".csv",""));            
            int thisYearRank = rankName(year,name,gender);
            
            System.out.println("ThisYearRank: "+thisYearRank);
            if (thisYearRank != -1){totalRank += thisYearRank;}
        }
        
        if(totalRank>0){return totalRank/totalFiles;}
        return -1;
    }
    
    public double getTotalBirthsRankedHigher(String name, String gender, int year){
        FileResource fr = new FileResource("data/yob"+year+".csv");
        int startRank = rankName(year,name,gender);
        System.out.println(startRank);
        double totalBirths = 0;
        int count = 0;
        
        for(CSVRecord record: fr.getCSVParser(false)){
            if(record.get(1).equals(gender) && count < startRank){
                    count+=1;
                    totalBirths+= Double.parseDouble(record.get(2));
            }
        }
        
        if(totalBirths!=0){return totalBirths;}
        return -1;
    }

    public void testTotalBirths () {
        // FileResource fr = new FileResource("data/yob1905.csv");
        // totalBirths(fr);
        // System.out.println(rankName(1971,"Frank","M"));
        // System.out.println(getName(1982,450,"M"));
        // System.out.println(whatIsNameInYear(1974,2014,"Owen","M"));
        // System.out.println("Mason yearOfHighestRank: "+yearOfHighestRank("Mich","M"));
        // System.out.println("Average Rank Of Pablo: "+getAvgRank("Robert","M"));
        System.out.println("Total births ranked higher than: "+getTotalBirthsRankedHigher("Drew","M",1990));
    }
}
