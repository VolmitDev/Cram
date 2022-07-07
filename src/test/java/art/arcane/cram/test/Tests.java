package art.arcane.cram.test;

import art.arcane.cram.*;
import art.arcane.nbtson.NBTSon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class Tests {
    @Test public void testPacking() throws IOException {
        PakWriter writer = new PakWriter(new File("paks"), "test")
                .resource(new PakKey("test:anobject0"), AnObject.builder().build())
                .resource(new PakKey("test:anobject1"), AnObject.builder().build())
                .resource(new PakKey("test:anobject2"), AnObject.builder().build())
                .resource(new PakKey("test:anobject3"), AnObject.builder().build())
                .resource(new PakKey("test:anobject4"), AnObject.builder().build())
                .resource(new PakKey("test:anobject5"), AnObject.builder().build())
                .resource(new PakKey("test:anobject6"), AnObject.builder().build())
                .resource(new PakKey("test:anobject7"), AnObject.builder().build())
                .resource(new PakKey("test:anobject8"), AnObject.builder().build())
                .resource(new PakKey("test:anobject9"), AnObject.builder().build());
        writer.write();

        PakFile file = new PakFile(new File("paks"), "test");
        file.getAllResources().forEach((k, v) -> System.out.println("Read " + k + " as " + NBTSon.toSNBT(v)));
    }
}
