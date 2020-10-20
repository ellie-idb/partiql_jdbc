package org.partiql.jdbc;

import com.amazon.ion.*;
import org.partiql.lang.eval.ExprValue;

import java.sql.SQLException;

public class PartiQLDataModel {
    /*
    TODO:
    private static <T> boolean ensureCoercible(ExprValue val, Class<T> c) throws SQLException {
        // this is ugly
        if (c.getTypeName() == "int" && val.getType().isNumber()) {
            return true;
        } else if (c.getTypeName() == "float" && val.getType().isNumber()) {
            return true;
        } else if (c.getTypeName() == "java.sql.Clob" && val.getType().isLob()) {
            return true;
        } else if (c.getTypeName() == "java.sql.Blob" && val.getType().isLob()) {
            return true;
        }
        logger.info(c.getTypeName());
        logger.info(val.getType().toString());
        return true;
    }
    */

    private static boolean matchesKeyAndType(IonStruct struct, String key, IonType type) {
        if (struct.containsKey(key)) {
            IonValue val = struct.get(key);
            if (val.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean getBool(ExprValue node, String name) throws SQLException {
        IonStruct struct = getStruct(node);
        if (matchesKeyAndType(struct, name, IonType.BOOL)) {
            return ((IonBool) struct.get(name)).booleanValue();
        }
        throw new SQLException("Value did not match IonBool?");
    }

    /**
     * Helper function to retrieve an integer from an ExprValue containing an IonStruct.
     *
     * @param node The abstract ExprValue node to extract the value from
     * @param name The name representing the key within the IonStruct.
     * @return An integer representing the value extracted from the ExprValue.
     * @throws SQLException
     */

    public static int getInt(ExprValue node, String name) throws SQLException {
        IonStruct struct = getStruct(node);
        if (matchesKeyAndType(struct, name, IonType.INT)) {
            return ((IonInt) struct.get(name)).intValue();
        }

        throw new SQLException("Value did not match IonType.INT");
    }

    /**
     * Helper function to retrieve an float from an ExprValue containing an IonStruct.
     *
     * @param node The abstract ExprValue node to extract the value from
     * @return An float representing the value extracted from the ExprValue.
     * @throws SQLException
     */
    public static float getFloat(ExprValue node) throws SQLException {
        if (node.getIonValue() instanceof IonFloat) {
            return ((IonFloat) node.getIonValue()).floatValue();
        } else {
            throw new SQLException("ionValue did not equal IonFloat?");
        }
    }

    public static String getString(ExprValue node, String name) throws SQLException {
        IonStruct struct = getStruct(node);
        if (matchesKeyAndType(struct, name, IonType.STRING)) {
            return ((IonString) struct.get(name)).stringValue();
        }

        throw new SQLException("ionValue did not equal IonString?");
    }

    public static IonStruct getStruct(ExprValue node) throws SQLException {
        if (node.getIonValue() instanceof IonStruct) {
            return ((IonStruct) node.getIonValue());
        }
        else {
            throw new SQLException("ionValue did not equal IonStruct?");
        }
    }

}
