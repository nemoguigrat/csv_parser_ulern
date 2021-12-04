import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) {
        try{
            var a = CSVParser.ParseHappynesCountryCsv("HappynesCountry2016.csv");
            var b = DbHandler.getInstance();
            b.CreateTable();
            b.addCountryList(a);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

