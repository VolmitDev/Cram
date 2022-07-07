package art.arcane.cram.test;

import art.arcane.cram.PakResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AnObject implements PakResource
{
    @Builder.Default
    private int anInt = 4;
    @Builder.Default
    private String astring = "somestring";
}