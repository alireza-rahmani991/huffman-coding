import java.io.IOException;
import java.io.OutputStream;


public class BitOutputStream {
    private OutputStream out;
    private int currentByte;
    private int numBitsFilled;

    public BitOutputStream(OutputStream out){
        this.out = out;
        currentByte = 0;
        numBitsFilled = 0;
    }

    public void writeSingleBit(int bit) throws IOException{
        if(bit != 0 && bit != 1){
            System.out.println("invalid bit");
            return;
        }

        currentByte = (currentByte << 1 ) | bit;
        numBitsFilled++;

        if(numBitsFilled == 8){
            out.write(currentByte);
            numBitsFilled = 0;
            currentByte = 0;
        }
    }

    public void writeMultipleBits(String bits) throws IOException{
        for(char bit : bits.toCharArray()){
            if(bit == '1'){
                writeSingleBit(1);
            }
            else{
                writeSingleBit(0);
            }
        }
    }

    public void flush() throws IOException{
        if(numBitsFilled > 0){
            currentByte <<= (8 - numBitsFilled);
            out.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
        out.flush();
    }

    public void close() throws IOException{
        flush();
        out.close();
    }
}
