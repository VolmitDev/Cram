# Cram
Pak files

![](https://img.shields.io/github/v/release/ArcaneArts/Cram?color=%236f24f0&display_name=tag&label=Cram&sort=semver&style=for-the-badge)

```groovy
maven { url "https://arcanearts.jfrog.io/artifactory/archives" }
```

```groovy
implementation 'art.arcane:Cram:<VERSION>'
```

## Usage

Given the object schema

```java
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
```

We can write this to a folder using pak files

```java
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
```

We can then easily read this in true parallel

```java
PakFile file = new PakFile(new File("paks"), "test");
file.getAllResources().forEach((k, v) -> System.out.println("Read " + k + " as " + NBTSon.toSNBT(v)));
```

Per stream reading with proper skipping is coming soon