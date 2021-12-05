import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
            var dataCSV = CSVParser.ParseHappynesCountryCsv("HappynesCountry2016.csv");
            var dbInstance = DbHandler.getInstance();
            var dataDb = dbInstance.getCountryGenerosity();
            var minGenerosity = dbInstance.getCountryWithMinGenerosity();
            System.out.println("Самая жадная страна: " + minGenerosity.getFirst() + " с показателем щедрости: " + minGenerosity.getSecond());

            EventQueue.invokeLater(() -> {
                var ex = new CountryGenerosityChart(dataDb);
                ex.setVisible(true);
            });
    }
}

