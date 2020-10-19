import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.partiql.jdbc.PartiQLStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SelectTest {
    private Connection connection;

    @Before
    public void setup() throws SQLException {
        // enforce the Driver to be registered/loaded before we do anything
        try {
            Class.forName("org.partiql.jdbc.PartiQLDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:partiql:src/test/resources/ion/tutorial-all-data.env");
    }

    @After
    public void close() throws SQLException {
        if (connection == null) {
            return;
        }
        connection.close();
    }

    @Test
    public void testQ1() throws Exception {
        PartiQLStatement statement = (PartiQLStatement) connection.createStatement();
        String query = "SELECT e.id, \n" +
                "       e.name AS employeeName, \n" +
                "       e.title AS title\n" +
                "FROM hr.employees e\n" +
                "WHERE e.title = 'Dev Mgr'\n";
        statement.execute(query);
    }
}