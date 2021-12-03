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
            var a = ParseHappynesCountryCsv("HappynesCountry2016.csv");
            for (var i = 0; i < a.size(); i++)
                System.out.println(a.get(i).getCountry());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static List<CountryHappynes> ParseHappynesCountryCsv(String fileName) throws Exception {
        URI resource = ClassLoader.getSystemResource(fileName).toURI();
        //Загружаем строки из файла
        List<CountryHappynes> countyList = new ArrayList<>();
        List<String> fileLines = Files.readAllLines(Paths.get(resource));
        String[] headers = fileLines.remove(0).split(",");
        for (String fileLine : fileLines) {
//            Map<String, String> countryDict = new HashMap<>();
            String[] values = fileLine.split(",");
            Map<String, String> a = IntStream.range(0, headers.length).boxed().collect(Collectors.toMap(i -> headers[i], i -> values[i]));
//            for (int i = 0; i < headers.length; i++)
//                countryDict.put(headers[i], values[i]);
            countyList.add(new CountryHappynes(a));
        }
        return countyList;
    }
}

