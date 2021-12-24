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

    // Используем шаблон одиночка, чтобы не плодить множество
    // экземпляров класса DbHandler
    private static DbHandler instance = null;
    // Объект, в котором будет храниться соединение с БД
    private Connection connection;

    public static DbHandler getInstance() throws SQLException, URISyntaxException {
        if (instance == null)
            instance = new DbHandler();
        return instance;
    }

    private DbHandler() throws SQLException, URISyntaxException {
        URI resource = ClassLoader.getSystemResource("database.sqlite3").toURI();
        // Выполняем подключение к базе данных
        this.connection = DriverManager.getConnection("jdbc:sqlite::resource:database.sqlite3");
        System.out.println("База Подключена!");
    }

    public void CreateTable() {
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

    public Map<String, Float> getCountryGenerosity() throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            Map<String, Float> dataset = new HashMap<>();
            ResultSet dataFromDb = statement.executeQuery("SELECT country, generosity FROM happynes_country");
            while (dataFromDb.next())
                dataset.put(dataFromDb.getString("country"), dataFromDb.getFloat("generosity"));
            return dataset;
        }
    }

    public Tuple<String, Float> getCountryWithMinGenerosity() throws SQLException {
        try (Statement statement = this.connection.createStatement()) {
            Map<String, Float> dataset = new HashMap<>();
            ResultSet dataFromDb = statement.executeQuery("SELECT country, generosity FROM happynes_country WHERE generosity = (" +
                    "SELECT MIN(generosity) FROM happynes_country WHERE region = 'Middle East and Northern Africa' OR region = 'Central and Eastern Europe')");
            return new Tuple<>(dataFromDb.getString("country"), dataFromDb.getFloat("generosity"));
        }
    }

    public String getMiddleCountry() throws SQLException {
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
                            "WHERE Region = 'Western Europe' or Region = 'Sub-Saharan Africa'");
            return middledata.getString("Country");
        }
    }

    public void addCountryList(List<CountryHappynes> country) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
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
