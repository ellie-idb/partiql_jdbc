package org.partiql.jdbc;

import org.partiql.lang.CompilerPipeline;
import org.partiql.lang.eval.EvaluationSession;
import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.eval.Expression;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PartiQLStatement extends AbstractStatement {
    final private Logger logger = Logger.getLogger("org.partiql.jdbc");
    private PartiQLConnection connection;
    private CompilerPipeline pipeline;
    private EvaluationSession session;
    private PartiQLGlobalBindings bindings;
    private PartiQLResultSet results;

    /**
     * Create a statement given a PartiQL connection,
     * and the global environment loaded.
     *
     * @param connection The PartiQL connection to use
     * @param bindings The global environment which every query will refer to
     */
    protected PartiQLStatement(PartiQLConnection connection, PartiQLGlobalBindings bindings) {
        this.connection = connection;
        this.pipeline = CompilerPipeline.standard(this.connection.ion);
        this.bindings = bindings;
        this.session = EvaluationSession.builder().globals(this.bindings.asExprValue().getBindings()).build();
    }

    /**
     * @param s SQL query to run
     * @return The ResultSet representing the BAG returned by the query
     * @throws SQLException
     */
    @Override
    public ResultSet executeQuery(String s) throws SQLException {
        Expression expr = this.pipeline.compile(s);
        ExprValue val = expr.eval(session);
        return new PartiQLResultSet(this, val);
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public boolean execute(String s) throws SQLException {
        this.results = (PartiQLResultSet) executeQuery(s);
        return true;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return results;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }
}
