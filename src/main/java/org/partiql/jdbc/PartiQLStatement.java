package org.partiql.jdbc;

import org.partiql.lang.CompilerPipeline;
import org.partiql.lang.eval.EvaluationSession;
import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.eval.Expression;
import org.partiql.lang.util.ConfigurableExprValueFormatter;

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

    protected PartiQLStatement(PartiQLConnection connection, PartiQLGlobalBindings bindings) {
        this.connection = connection;
        this.pipeline = CompilerPipeline.standard(connection.ion);
        this.bindings = bindings;
        this.session = EvaluationSession.builder().globals(bindings.asExprValue().getBindings()).build();
    }

    @Override
    public ResultSet executeQuery(String s) throws SQLException {
        return null;
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public boolean execute(String s) throws SQLException {
        Expression e = pipeline.compile(s);
        ExprValue val = e.eval(session);
        logger.info(ConfigurableExprValueFormatter.getPretty().format(val));
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }
}
