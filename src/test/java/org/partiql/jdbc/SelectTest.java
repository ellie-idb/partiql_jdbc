package org.partiql.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class SelectTest {
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
        connection = DriverManager.getConnection("jdbc:partiql:src/test/resources/ion/tutorial-all-data.env");
    }

    @After
    public void close() throws SQLException {
        if (connection == null) {
            return;
        }
        // connection.close();
    }

    /*
        These are all examples from the PartiQL CLI that I've brought in.
        Currently there's no checking to see whether or not the result is valid.. but that's coming soon (TM)
    */

    @Test
    public void testQ1() throws Exception {
        PartiQLStatement statement = (PartiQLStatement) connection.createStatement();
        String query = "SELECT e.id, \n" +
                "       e.name AS employeeName, \n" +
                "       e.title AS title\n" +
                "FROM hr.employees e\n" +
                "WHERE e.title = 'Dev Mgr'\n";
        ResultSet results = statement.executeQuery(query);
        assertEquals("Susan Smith", results.getString("employeeName"));
        assertEquals("Dev Mgr", results.getString("title"));
        assertEquals(4, results.getInt("id"));
    }

    @Test
    public void testQ2() throws Exception {
        PartiQLStatement statement = (PartiQLStatement) connection.createStatement();
        String query = "SELECT e.name AS employeeName, \n" +
                "       p.name AS projectName\n" +
                "FROM hr.employeesNest AS e, \n" +
                "     e.projects AS p\n" +
                "WHERE p.name LIKE '%security%'";
        ResultSet results = statement.executeQuery(query);

        assertEquals("Bob Smith", results.getString("employeeName"));
        assertEquals("AWS Redshift security", results.getString("projectName"));
        results.next();
        assertEquals("Bob Smith", results.getString("employeeName"));
        assertEquals("AWS Aurora security", results.getString("projectName"));
        results.next();
        assertEquals("Jane Smith", results.getString("employeeName"));
        assertEquals("AWS Redshift security", results.getString("projectName"));
        assertFalse(results.next());
        assertTrue(results.isLast());
    }

    @Test
    public void testQ3() throws Exception {
        PartiQLStatement statement = (PartiQLStatement) connection.createStatement();
        String query = "SELECT e.id AS id, \n" +
                "       e.name AS employeeName, \n" +
                "       e.title AS title, \n" +
                "       p.name AS projectName\n" +
                "FROM hr.employeesNest AS e LEFT JOIN e.projects AS p";
        // Q3 is broken (LEFT JOIN e.projects as P ON ???)
        // ResultSet results = statement.executeQuery(query);
        // logger.info(results.toString());
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