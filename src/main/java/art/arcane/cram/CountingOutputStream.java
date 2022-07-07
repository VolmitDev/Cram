package art.arcane.cram;

import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends OutputStream {
    private final OutputStream out;
    @Getter
    private int count;

    public CountingOutputStream(OutputStream out)
    {
        this.count = 0;
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        count++;
    }
}
