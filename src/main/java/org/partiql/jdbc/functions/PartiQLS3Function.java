package org.partiql.jdbc.functions;

import com.amazon.ion.IonDatagram;
import com.amazon.ion.IonReader;
import com.amazon.ion.IonStruct;
import com.amazon.ion.IonSystem;
import com.amazon.ion.system.IonReaderBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.jetbrains.annotations.NotNull;
import org.partiql.jdbc.PartiQLDataModel;
import org.partiql.lang.eval.Environment;
import org.partiql.lang.eval.ExprValue;
import org.partiql.lang.eval.ExprValueFactory;

import java.util.List;

public class PartiQLS3Function extends PartiQLBaseFunction {
    private AmazonS3 client;
    private IonSystem ion;

    public PartiQLS3Function(ExprValueFactory factory) {
        super(factory);
        this.ion = this.factory.getIon();
        this.client = AmazonS3ClientBuilder.standard()
                                           .build();
    }

    @NotNull
    @Override
    public String getName() {
        return "s3_link";
    }

    @NotNull
    @Override
    public ExprValue call(@NotNull Environment environment, @NotNull List<? extends ExprValue> args) {
        IonStruct opts = optionsStruct(2, args);
        String bucketName = PartiQLDataModel.getString(args.get(0));
        String key = PartiQLDataModel.getString(args.get(1));
        S3Object obj = this.client.getObject(bucketName, key);
        // TODO: investigate on why we have to reconstruct this to a datagram... shouldn't we be able to just pass the reader directly?
        S3ObjectInputStream inputStream = obj.getObjectContent();
        IonReader ionReader = IonReaderBuilder.standard().build(inputStream);
        IonDatagram values = ion.getLoader().load(ionReader);
        return this.factory.newFromIonValue(values);
    }
}
