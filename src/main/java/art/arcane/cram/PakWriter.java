package art.arcane.cram;

import art.arcane.nbtson.NBTSon;
import art.arcane.nbtson.io.NBTUtil;
import art.arcane.nbtson.io.NamedTag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PakWriter {
    private final List<PakResourceInput> resources;
    private final File folder;
    private final PakOutputStream output;
    private final String name;
    private final long pakSize;

    public PakWriter(File folder, String name, long pakSize) throws IOException {
        folder.mkdirs();
        this.name = name;
        this.folder = folder;
        this.pakSize = pakSize;
        output = new PakOutputStream(folder, name, pakSize);
        resources = new ArrayList<>();
    }

    public PakWriter(File folder, String name) throws IOException {
        this(folder, name, 1024 * 1024);
    }

    public void write() throws IOException {
        PakMetadata.PakMetadataBuilder meta = PakMetadata.builder().namespace(name).pakSize(pakSize);
        long totalWritten = 0;

        for(PakResourceInput i : resources) {
            long written = i.write(output);
            meta.resource(PakResourceMetadata.builder()
                .key(i.getKey().toString())
                .type(i.getType().getCanonicalName())
                .start(totalWritten)
                .length(written)
                .build());
            totalWritten += written;
        }

        output.close();

        for(File i : output.getWrittenFiles()) {
            meta.pakFile(i.getName());
        }

        NBTUtil.write(new NamedTag("Package " + name, NBTSon.toNBT(meta.build())), new File(folder, name + ".dat"), true);
    }

    public PakWriter resource(PakKey key, PakResource resource) throws IOException {
        return resource(PakResourceInput.object(key, resource));
    }

    public PakWriter resource(PakResourceInput input) {
        resources.add(input);
        return this;
    }
}
