import java.awt.*;

public class Main {
    public static void main(String[] args) throws Exception {
            var a = CSVParser.ParseHappynesCountryCsv("HappynesCountry2016.csv");
            var b = DbHandler.getInstance();
            var c = b.getCountryGenerosity();

            EventQueue.invokeLater(() -> {
                var ex = new CountryGenerosityChart(c);
                ex.setVisible(true);
            });
    }
}

