import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {
    private InputStream in;
    private int currentByte;
    private int numBitsRemaining;

    public BitInputStream(InputStream in) {
        this.in = in;
        this.numBitsRemaining = 0;
    }
    
    public int readBit() throws IOException {
        if (numBitsRemaining == 0) {
            currentByte = in.read();
            if (currentByte == -1) {
                return -1; 
            }
            numBitsRemaining = 8;
        }
        
        numBitsRemaining--;
        return (currentByte >> numBitsRemaining) & 1;
    }
    
    public void close() throws IOException {
        in.close();
    }
}
