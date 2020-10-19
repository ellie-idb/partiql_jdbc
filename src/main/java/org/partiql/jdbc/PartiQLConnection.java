package org.partiql.jdbc;

import com.amazon.ion.IonSystem;
import com.amazon.ion.system.IonSystemBuilder;
import org.partiql.lang.CompilerPipeline;
import org.partiql.lang.eval.EvaluationSession;
import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.eval.ExprValueFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.logging.Logger;


public class PartiQLConnection extends AbstractConnection {
    final private Logger logger = Logger.getLogger("org.partiql.jdbc");
    final protected IonSystem ion;
    final private ExprValueFactory valueFactory;
    final private CompilerPipeline pipeline;
    final private PartiQLGlobalBindings bindings;

    /***
     *  Create a new PartiQL connection given an initial environment file to work on.
     *
     * @param path The file to load for initial ingest (as long as it is valid Ion data)
     */
    protected PartiQLConnection(String path) throws SQLException {
        this.ion = IonSystemBuilder.standard().build();
        this.pipeline = CompilerPipeline.standard(ion);
        this.valueFactory = ExprValueFactory.standard(ion);
        String actualPath = path.substring(path.lastIndexOf(":") + 1);
        try {
            String envFile = Files.readString(Path.of(actualPath));
            ExprValue environment = pipeline.compile(envFile).eval(EvaluationSession.standard());
            this.bindings = new PartiQLGlobalBindings(this.valueFactory).addBinding(environment.getBindings());
        } catch (IOException e) {
            throw new SQLException("Could not load environment file.");
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new PartiQLStatement(this, this.bindings);
    }

    @Override
    public PreparedStatement prepareStatement(String s) throws SQLException {
        throw new SQLFeatureNotSupportedException("Connection.prepareStatement(String) is not supported yet.");
    }

    @Override
    public void close() throws SQLException {
        throw new SQLFeatureNotSupportedException("Connection.close() is not supported yet.");
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setReadOnly(boolean b) throws SQLException {
        throw new SQLFeatureNotSupportedException("Connection.setReadOnly(boolean) is not supported yet.");
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return true;
    }
}
