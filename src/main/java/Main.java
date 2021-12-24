import java.awt.*;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
            List<CountryHappynes> dataCSV = CSVParser.ParseHappynesCountryCsv("HappynesCountry2016.csv");
            DbHandler dbInstance = DbHandler.getInstance();
            Map<String, Float> dataDb = dbInstance.getCountryGenerosity();
            Tuple<String, Float> minGenerosity = dbInstance.getCountryWithMinGenerosity();
            String middleCountry = dbInstance.getMiddleCountry();
            System.out.println("Самая жадная страна: " + minGenerosity.getFirst() + " с показателем щедрости: " + minGenerosity.getSecond());
            System.out.println("Самая средняя страна: " + middleCountry);

            EventQueue.invokeLater(() -> {
                CountryGenerosityChart ex = new CountryGenerosityChart(dataDb);
                ex.setVisible(true);
            });
    }
}

