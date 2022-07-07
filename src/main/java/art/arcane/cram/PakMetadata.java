package art.arcane.cram;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Accessors(chain = true, fluent = true)
public class PakMetadata {
    private String namespace;
    @Singular
    private List<PakResourceMetadata> resources;
    @Singular
    private List<String> pakFiles;
    private long pakSize;
}
