package org.partiql.jdbc;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class S3Test {
    private Connection connection;
    final private Logger logger = Logger.getLogger("org.partiql.jdbc");

    @Before
    public void setup() throws SQLException {
        // enforce the JDBC driver to be registered before we do anything
        try {
            Class.forName("org.partiql.jdbc.PartiQLDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:partiql");
    }

    @Test
    public void testS3() throws Exception {
        PartiQLStatement statement = (PartiQLStatement) connection.createStatement();
        String query = "SELECT doc.name, doc.address \n" +
                "FROM s3_link('partiql-test', 'env.ion') AS doc WHERE doc.age < 30";
        ResultSet results = statement.executeQuery(query);

        assertEquals("person_2", results.getString("name"));
        results.next();
        assertEquals("person_3", results.getString("name"));
    }
}
