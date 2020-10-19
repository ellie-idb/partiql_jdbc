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
import java.sql.Statement;
import java.util.logging.Logger;


public class PartiQLConnection extends AbstractConnection {
    final private Logger logger = Logger.getLogger("org.partiql.jdbc");
    final protected IonSystem ion;
    final private ExprValueFactory valueFactory;
    final private CompilerPipeline pipeline;
    final private PartiQLGlobalBindings bindings;
    /***
     *
     * @param path
     */
    protected PartiQLConnection(String path) {
        ion = IonSystemBuilder.standard().build();
        pipeline = CompilerPipeline.standard(ion);
        valueFactory = ExprValueFactory.standard(ion);
        String actualPath = path.substring(path.lastIndexOf(":") + 1);
        String envFile;
        try {
            envFile = Files.readString(Path.of(actualPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not load environment");
        }
        ExprValue environment = pipeline.compile(envFile).eval(EvaluationSession.standard());
        bindings = new PartiQLGlobalBindings(valueFactory).addBinding(environment.getBindings());
    }

    @Override
    public Statement createStatement() throws SQLException {
        logger.info("Connection:createStatement()");
        return new PartiQLStatement(this, bindings);
    }

    @Override
    public PreparedStatement prepareStatement(String s) throws SQLException {
        logger.info("Connection:prepareStatement()");
        return new PartiQLPreparedStatement(this, s);
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setReadOnly(boolean b) throws SQLException {
        throw new UnsupportedOperationException("Method not supported: Connection.setReadOnly(boolean)");
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return true;
    }

    @Override
    public Statement createStatement(int i, int i1) throws SQLException {
        return new PartiQLStatement(this, bindings);
    }

    @Override
    public void setSchema(String s) throws SQLException {

    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }
}
