package art.arcane.cram;

import art.arcane.multiburst.BurstExecutor;
import art.arcane.multiburst.MultiBurst;
import art.arcane.nbtson.NBTSon;
import art.arcane.nbtson.io.NBTDeserializer;
import art.arcane.nbtson.tag.CompoundTag;
import lombok.Getter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PakFile {
    @Getter
    private final String name;
    private final File folder;
    @Getter
    private final PakMetadata metadata;
    private final Map<PakKey, PakResourceMetadata> keyedMetadata;

    public PakFile(File folder, String name) throws IOException {
        this.folder = folder;
        this.name = name;
        this.metadata = NBTSon.fromNBT((CompoundTag) new NBTDeserializer(true)
                .fromFile(new File(folder, name + ".dat")).getTag(), PakMetadata.class);
        keyedMetadata = new HashMap<>();

        for(PakResourceMetadata i : metadata.resources()) {
            keyedMetadata.put(new PakKey(i.key()), i);
        }
    }

    public Map<PakKey, PakResource> getAllResources() throws IOException {
        List<File> pakFiles = metadata.pakFiles().stream().map(i -> new File(folder, i)).toList();
        byte[] all = new byte[(int) pakFiles.stream().mapToLong(File::length).sum()];
        BurstExecutor burst = MultiBurst.burst.burst(pakFiles.size());
        int offset = 0;
        for(File i : pakFiles) {
            int off = offset;
            burst.queue(() -> {
                try {
                    FileInputStream fin = new FileInputStream(i);
                    fin.read(all, off, (int) i.length());
                    MultiBurst.burst.lazy(() -> {
                        try {
                            fin.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            offset += i.length();
        }

        burst.complete();
        burst = MultiBurst.burst.burst(metadata.resources().size());
        Map<PakKey, PakResource> resources = new ConcurrentHashMap<>();
        for(PakResourceMetadata i : metadata.resources()) {
            burst.queue(() -> {
                try {
                    resources.put(new PakKey(i.key()), (PakResource) NBTSon.fromNBT((CompoundTag) new NBTDeserializer(true)
                        .fromStream(new ByteArrayInputStream(all, (int) i.start(), (int) i.length())).getTag(), Class.forName(i.type())));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        }

        burst.complete();
        return resources;
    }
}
