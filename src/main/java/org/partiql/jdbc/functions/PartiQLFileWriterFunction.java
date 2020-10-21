package org.partiql.jdbc.functions;

import org.jetbrains.annotations.NotNull;
import org.partiql.lang.eval.Environment;
import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.eval.ExprValueFactory;

import java.util.List;

public class PartiQLFileWriterFunction extends PartiQLBaseFunction {
    public PartiQLFileWriterFunction(ExprValueFactory factory) {
        super(factory);
    }

    @NotNull
    @Override
    public String getName() {
        return "write_file";
    }

    @NotNull
    @Override
    public ExprValue call(@NotNull Environment environment, @NotNull List<? extends ExprValue> list) {
        return null;
    }
}
