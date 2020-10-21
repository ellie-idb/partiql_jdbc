package org.partiql.jdbc.functions;

import com.amazon.ion.IonDatagram;
import com.amazon.ion.IonReader;
import com.amazon.ion.IonStruct;
import com.amazon.ion.system.IonReaderBuilder;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.partiql.jdbc.functions.PartiQLBaseFunction;
import org.partiql.jdbc.PartiQLDataModel;
import org.partiql.lang.eval.Environment;
import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.eval.ExprValueFactory;
import org.partiql.lang.util.IonValueExtensionsKt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public class PartiQLFileReaderFunction extends PartiQLBaseFunction {
    public PartiQLFileReaderFunction(ExprValueFactory factory) {
        super(factory);
    }

    @NotNull
    @Override
    public String getName() {
        return "read_file";
    }

    @NotNull
    @Override
    public ExprValue call(@NotNull Environment environment, @NotNull List<? extends ExprValue> args) {
        IonStruct options = optionsStruct(1, args);
        String fileName = PartiQLDataModel.getString(args.get(0));

        FileInputStream stream;
        try {
            stream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find file: " + fileName);
        }

        IonReader ionReader = IonReaderBuilder.standard().build(stream);
        IonDatagram values = factory.getIon().getLoader().load(ionReader);
        return factory.newFromIonValue(values);
    }
}
