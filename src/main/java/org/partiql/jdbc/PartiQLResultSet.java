package org.partiql.jdbc;

import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.util.ConfigurableExprValueFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Iterator;

public class PartiQLResultSet extends AbstractResultSet {
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
        this.iterator = this.root.iterator();
        this.current = this.iterator.next(); // Skip over the initial Bag element
    }

    @Override
    public String toString() {
        return ConfigurableExprValueFormatter.getPretty().format(this.root);
    }

    @Override
    public boolean next() throws SQLException {
        if (this.iterator.hasNext()) {
            this.current = this.iterator.next();
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
    public int getInt(String s) throws SQLException {
       return PartiQLDataModel.getIntFromStruct(this.current, s);
    }

    @Override
    public boolean getBoolean(String s) throws SQLException {
        return PartiQLDataModel.getBoolFromStruct(this.current, s);
    }

    @Override
    public Object getObject(String s) throws SQLException {
        return PartiQLDataModel.getStruct(this.current);
    }

    @Override
    public String getString(String s) throws SQLException {
        return PartiQLDataModel.getStringFromStruct(this.current, s);
    }

    @Override
    public Statement getStatement() throws SQLException {
        return this.statement;
    }

    @Override
    public boolean isLast() throws SQLException {
        return !this.iterator.hasNext();
    }

    @Override
    public boolean last() throws SQLException {
        throw new SQLFeatureNotSupportedException("Unimplemented");
    }
}
