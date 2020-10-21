package org.partiql.jdbc.functions;

import com.amazon.ion.IonStruct;
import com.amazon.ion.IonValue;
import org.partiql.lang.eval.ExprFunction;
import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.eval.ExprValueFactory;

import java.util.List;

// Translated from the CLI example from Kotlin back to Java ...
public abstract class PartiQLBaseFunction implements ExprFunction {
    protected ExprValueFactory factory;

    protected PartiQLBaseFunction(ExprValueFactory factory) {
        this.factory = factory;
    }

    protected IonStruct optionsStruct(int requiredArity, List<? extends ExprValue> args) {
        return optionsStruct(requiredArity, args, requiredArity);
    }

    protected IonStruct optionsStruct(int requiredArity, List<? extends ExprValue> args, int optionsIndex) {
        if (args.size() == requiredArity) {
            return this.factory.getIon().newEmptyStruct();
        }
        else if (args.size() == requiredArity + 1) {
            return extractOptVal(args, optionsIndex);
        }

        throw new IllegalArgumentException("Bad number of arguments: " + args.size());
    }

    protected IonStruct extractOptVal(List<? extends ExprValue> args, int optionsIndex) {
        IonValue val = args.get(optionsIndex).getIonValue();
        if (val instanceof IonStruct) {
            return (IonStruct) val;
        }

        throw new IllegalArgumentException("Invalid option: " + val.toString());
    }

}
