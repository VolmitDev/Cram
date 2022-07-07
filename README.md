# Cram
Pak files

[![Latest version of 'Cram' @ Cloudsmith](https://api-prd.cloudsmith.io/v1/badges/version/arcane/archive/maven/Cram/latest/a=noarch;xg=art.arcane/?render=true&show_latest=true)](https://cloudsmith.io/~arcane/repos/archive/packages/detail/maven/Cram/latest/a=noarch;xg=art.arcane/)

```groovy
maven { url "https://dl.cloudsmith.io/public/arcane/archive/maven/" }
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