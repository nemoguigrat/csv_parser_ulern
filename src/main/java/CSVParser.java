import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CSVParser {
    public static List<CountryHappynes> ParseHappynesCountryCsv(String fileName) {
        try {
            URI resource = ClassLoader.getSystemResource(fileName).toURI();
            //Загружаем строки из файла
            List<CountryHappynes> countyList = new ArrayList<>();
            List<String> fileLines = Files.readAllLines(Paths.get(resource));
            String[] headers = fileLines.remove(0).split(",");
            for (String fileLine : fileLines) {
                String[] values = fileLine.split(",");
                Map<String, String> data = IntStream.range(0, headers.length).boxed().collect(Collectors.toMap(i -> headers[i], i -> values[i]));
                countyList.add(new CountryHappynes(data));
            }
            return countyList;
        } catch (Exception exception){
            exception.printStackTrace();
            System.out.println("Данные не были обработанны!");
            return new ArrayList<>();
        }

    }
}
