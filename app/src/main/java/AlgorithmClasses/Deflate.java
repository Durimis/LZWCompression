package AlgorithmClasses;

import java.io.*;
import java.util.zip.*;

public class Deflate{
    public Long startTime, endTime;
    public void compressFromFile(File input_file, FileOutputStream fout){

        try{
            startTime = System.nanoTime();
            Deflater defl =new Deflater();

            DeflaterOutputStream out = new DeflaterOutputStream(fout, defl);

            int size = (int) input_file.length();
            byte[] bytes = new byte[size];

            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(input_file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println("File is compressed!!");
            out.write(bytes);
            endTime = System.nanoTime();
            out.close();

        }catch(Exception e){System.out.println(e);}
        
    }


    public InputStream getUncompressedDataInputStream(InputStream compressed_input_stream) throws IOException {

        return new InflaterInputStream(compressed_input_stream);

    }

    public  void decompressFromStream(FileInputStream fin, FileOutputStream fout){

        try{
            InflaterInputStream in = new InflaterInputStream(fin);
            System.out.println("start");
            int i;
            while ((i = in.read()) != -1) {
                fout.write(i);
               // fout.flush();
            }
            System.out.println("File is Uncompressed!!");
            fin.close();
            fout.close();
            in.close();

        }catch(Exception e){System.out.println(e);}
        System.out.println("rest of the code");
    }

    public void compressData(byte[] data, OutputStream out)
            throws IOException {
        Deflater d = new Deflater();

        DeflaterOutputStream dout = new DeflaterOutputStream(out, d);

        dout.write(data);
        dout.close();
    }

    public void compressHauffmanFromFile(File input_file, FileOutputStream fout){

        try{
            startTime = System.nanoTime();
            Deflater defl =new Deflater();

            DeflaterOutputStream out = new DeflaterOutputStream(fout, defl);
            defl.setLevel(Deflater.HUFFMAN_ONLY);

            int size = (int) input_file.length();
            byte[] bytes = new byte[size];

            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(input_file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println("File is compressed!!");
            out.write(bytes);
            endTime = System.nanoTime();
            out.close();

        }catch(Exception e){System.out.println(e);}

    }


}  