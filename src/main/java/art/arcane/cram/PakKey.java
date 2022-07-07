package art.arcane.cram;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PakKey  {
    public static String DEFAULT_PAK_KEY = "minecraft";
    private final String namespace;
    private final String key;

    public PakKey(String namespacedkey)
    {
        this(namespacedkey.contains(":") ? namespacedkey.split("\\Q:\\E")[0] : DEFAULT_PAK_KEY,
                namespacedkey.contains(":") ? namespacedkey.split("\\Q:\\E")[1] : namespacedkey);
    }

    public String toString()
    {
        return namespace + ":" + key;
    }
}
