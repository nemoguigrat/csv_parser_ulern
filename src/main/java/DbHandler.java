import org.sqlite.JDBC;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyPair;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbHandler {
    private static DbHandler instance = null;

    private Connection connection;

    public static DbHandler getInstance(){
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    private DbHandler(){
        try {
            URI resource = ClassLoader.getSystemResource("database.sqlite3").toURI();
            // Выполняем подключение к базе данных
            this.connection = DriverManager.getConnection("jdbc:sqlite::resource:database.sqlite3");
            System.out.println("База Подключена!");
        } catch (SQLException e) {
            System.out.println("Драйвер базы данных не был подключен!");
            e.printStackTrace();
        } catch (URISyntaxException e){
            System.out.println("Невозможно найти указанный файл!");
            e.printStackTrace();

        }
    }

    public void createTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS 'happynes_country' " +
                    "('country' VARCHAR(50) PRIMARY KEY, " +
                    "'region' VARCHAR(100)," +
                    "'rank' INTEGER," +
                    "'happynesScore' FLOAT," +
                    "'lowerConfidenceInterval' FLOAT," +
                    "'upperConfidenceInterval' FLOAT," +
                    "'economy' FLOAT," +
                    "'family' FLOAT," +
                    "'health' FLOAT," +
                    "'freedom' FLOAT," +
                    "'trust' FLOAT," +
                    "'generosity' FLOAT," +
                    "'dystopiaResidual' FLOAT);");
            System.out.println("Таблица создана или уже существует.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Float> getCountryGenerosity(){
        try (Statement statement = this.connection.createStatement()) {
            Map<String, Float> dataset = new HashMap<>();
            ResultSet dataFromDb = statement.executeQuery("SELECT country, generosity FROM happynes_country");
            while (dataFromDb.next())
                dataset.put(dataFromDb.getString("country"), dataFromDb.getFloat("generosity"));
            return dataset;
        } catch (SQLException e){
            System.out.println("Ошибка при выполнении запроса!");
            e.printStackTrace();
            return null;
        }
    }

    public Tuple<String, Float> getCountryWithMinGenerosity(){
        try (Statement statement = this.connection.createStatement()) {
            ResultSet dataFromDb = statement.executeQuery("SELECT country, generosity FROM happynes_country WHERE generosity = (" +
                    "SELECT MIN(generosity) FROM happynes_country WHERE region = 'Middle East and Northern Africa' OR region = 'Central and Eastern Europe')");
            return new Tuple<>(dataFromDb.getString("country"), dataFromDb.getFloat("generosity"));
        } catch (SQLException e){
            System.out.println("Невозможно получить данные о стране");
            e.printStackTrace();
            return null;
        }
    }

    public String getMiddleCountry(){
        try (Statement statement = this.connection.createStatement()) {
            ResultSet middledata = statement.executeQuery(
                    "SELECT MIN(ABS((SELECT AVG(economy) FROM happynes_country)-economy) + " +
                            "ABS((SELECT AVG(family) FROM happynes_country)-family) +" +
                            "ABS((SELECT AVG(health) FROM happynes_country)-health) +" +
                            "ABS((SELECT AVG(freedom) FROM happynes_country)-freedom) +" +
                            "ABS((SELECT AVG(generosity) FROM happynes_country)-generosity) +" +
                            "ABS((SELECT AVG(trust) FROM happynes_country)-trust) +" +
                            "ABS((SELECT AVG(dystopiaResidual) FROM happynes_country)-dystopiaResidual)) as Result, country " +
                            "FROM happynes_country " +
                            "WHERE Region = 'Southeastern Asia' or Region = 'Sub-Saharan Africa'");
            return middledata.getString("Country");
        } catch (SQLException e){
            System.out.println("Невозможно получить данные о стране");
            e.printStackTrace();
            return "";
        }
    }

    public void addCountryList(List<CountryHappynes> country) {
        if (country.size() < 1)
            return;
        String sqlQuery = "INSERT INTO happynes_country VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = this.connection.prepareStatement(sqlQuery)) {
            connection.setAutoCommit(false);
            for (CountryHappynes countryObj : country) {
                statement.setObject(1, countryObj.getCountry());
                statement.setObject(2, countryObj.getRegion());
                statement.setObject(3, countryObj.getRank());
                statement.setObject(4, countryObj.getHappynesScore());
                statement.setObject(5, countryObj.getLowerConfidenceInterval());
                statement.setObject(6, countryObj.getUpperConfidenceInterval());
                statement.setObject(7, countryObj.getEconomy());
                statement.setObject(8, countryObj.getFamily());
                statement.setObject(9, countryObj.getHealth());
                statement.setObject(10, countryObj.getFreedom());
                statement.setObject(11, countryObj.getTrust());
                statement.setObject(12, countryObj.getGenerosity());
                statement.setObject(13, countryObj.getDystopiaResidual());

                statement.addBatch();
            }
            statement.executeBatch();
            statement.clearBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
