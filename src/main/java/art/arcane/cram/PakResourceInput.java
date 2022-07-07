package art.arcane.cram;

import art.arcane.nbtson.NBTSon;
import art.arcane.nbtson.io.NBTSerializer;
import art.arcane.nbtson.io.NBTUtil;
import art.arcane.nbtson.io.NamedTag;
import art.arcane.nbtson.tag.CompoundTag;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.function.Supplier;

@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class PakResourceInput {
    private Class<? extends PakResource> type;
    private PakKey key;
    private Supplier<InputStream> reader;
    private long size;

    public long write(OutputStream out) throws IOException {
        InputStream in = reader.get();
        long w = in.transferTo(out);
        in.close();
        return w;
    }

    public static PakResourceInput object(PakKey key, PakResource resource, Gson gson) throws IOException {
        return compound(key, resource.getClass(), NBTSon.toNBT(resource, gson));
    }

    public static PakResourceInput object(PakKey key, PakResource resource) throws IOException {
        return compound(key, resource.getClass(), NBTSon.toNBT(resource));
    }

    public static PakResourceInput compound(PakKey key, Class<? extends PakResource> type, CompoundTag compound) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        new NBTSerializer(true).toStream(new NamedTag("", compound), boas);
        boas.close();
        byte[] bytes = boas.toByteArray();
        return PakResourceInput.builder()
                .size(bytes.length)
                .key(key)
                .type(type)
                .reader(() -> new ByteArrayInputStream(bytes))
                .build();
    }

    public static PakResourceInput file(PakKey key, Class<? extends PakResource> type, File f) {
        return PakResourceInput.builder()
            .size(f.length())
            .key(key)
            .type(type)
            .reader(() -> {
                try {
                    return new FileInputStream(f);
                } catch(FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            })
            .build();
    }
}
