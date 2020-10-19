package org.partiql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/* TODO: Implement */
public class PartiQLPreparedStatement extends AbstractPreparedStatement {
    protected PartiQLPreparedStatement(PartiQLConnection connection, String sql) {

    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return null;
    }
}
