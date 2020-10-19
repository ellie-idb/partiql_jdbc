package org.partiql.jdbc;

import org.partiql.lang.eval.ExprValue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Logger;

public class PartiQLResultSet extends AbstractResultSet {
    final private Logger logger = Logger.getLogger("org.partiql.jdbc");
    // Statement which generated this result set
    private PartiQLStatement statement;
    // The root value (maintained *just* in case)
    private ExprValue root;
    // Our current node that we hit through the iterator
    private ExprValue current;
    private Iterator<ExprValue> iterator;

    /**
     * Create a ResultSet given a statement and a root Expression Value returned from
     * evaluating a PartiQL query.
     *
     * @param statement The initial statement that generated this result set
     * @param value The root ExprValue from which we iterate
     */
    protected PartiQLResultSet(PartiQLStatement statement, ExprValue value) {
        this.statement = statement;
        this.root = value;
        this.iterator = root.iterator();
        this.current = value;
        logger.info(value.getType().name());
    }

    @Override
    public boolean next() throws SQLException {
        if (iterator.hasNext()) {
            this.current = iterator.next();
            logger.info(current.getType().name());
            return true;
        }
        return false;
    }

    @Override
    public int getType() throws SQLException {
        /* Iterator cannot go backwards, it can only go forwards */
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public Statement getStatement() throws SQLException {
        return statement;
    }

    @Override
    public boolean isLast() throws SQLException {
        return iterator.hasNext() == false;
    }
}
