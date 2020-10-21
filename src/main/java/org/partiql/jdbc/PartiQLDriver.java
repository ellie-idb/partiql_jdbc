package org.partiql.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class PartiQLDriver implements Driver {
    final private Logger logger = Logger.getLogger("org.partiql.jdbc");
    public static final String PREFIX = "jdbc:partiql";
    static {
        try {
            DriverManager.registerDriver(new PartiQLDriver());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize PartiQL Driver");
        }
    }

    private boolean validateURL(String url) {
        if (url == null) return false;
        if (url.toLowerCase().startsWith(PREFIX)) return true;
        return false;
    }

    /* TODO: Implement */
    public int getMajorVersion() {
        return 0;
    }

    /* TODO: Implement */
    public int getMinorVersion() {
        return 1;
    }

    public boolean jdbcCompliant() {
        return false;
    }

    /* TODO: Implement */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public boolean acceptsURL(String url) {
        return validateURL(url);
    }

    /* TODO: Implement */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return null;
    }

    /* TODO: Implement */
    public Connection connect(String url, Properties info) throws SQLException {
        return new PartiQLConnection(url);
    }

}

