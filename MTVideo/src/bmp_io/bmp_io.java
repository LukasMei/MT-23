package bmp_io;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

//import wav_io.WavFileException;

public final class bmp_io {

	public static void main(String[] args) throws IOException {

		String inFilename = null;
		String outFilename = null;

		int hight = 0;
		int width = 0;
		int bits = 0;

		// Klasse zum Lesen und Schreiben der Farbwerte eines Pixels
		PixelColor pc = null;
		BmpImage bmp = null;

		if (args.length < 1) {
			System.out.println("At least one filename specified  (" + args.length + ")");
			System.exit(0);
		}

		// ****************************************************
		// Implementierung bei einem Eingabeparamter

		inFilename = args[0];

		try {
			InputStream in = new FileInputStream(inFilename);
			bmp = BmpReader.read_bmp(in);

			// Implementierung

			//printRGB(bmp);

			//bufferWith(outFilename, bmp);
			//bufferHeight(outFilename, bmp);
			//reduceBits(bmp,6);
			doSomething(bmp);


			// Zugriff auf Pixel mit bmp.image.getRgbPixel(x, y);
			// Setzen eines Pixels mit bmp.image.setRgbPixel(x, y, pc);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (args.length == 1)
			System.exit(0);

		// ***************************************************
		// Implementierung bei Ein- und Ausgabeparameter (speichern in eine Datei (2.
		// Argument))

		outFilename = args[1];
		OutputStream out = new FileOutputStream(outFilename);

		// Implementierung

		// Speicherung
		try {
			BmpWriter.write_bmp(out, bmp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	public static void printRGB(BmpImage bmp) {		
		for (int i = 0; i < bmp.image.getWidth(); i++) {
			for (int j = 0; j < bmp.image.getHeight(); j++) {
				PixelColor pOut = bmp.image.getRgbPixel(i, j);
				System.out.printf("R: %-3d", pOut.r);
				System.out.printf("G: %-3d", pOut.g);
				System.out.printf("B: %-3d", pOut.b);
				System.out.println();
			}
		}
	}

	public static void bufferWith(String outFilename, BmpImage bmp) {
		try {
			FileOutputStream fos = new FileOutputStream(outFilename + "_width_out.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos, "US-ascii");
			BufferedWriter bw = new BufferedWriter(osw);

			for (int i = 0; i < bmp.image.getWidth(); i++) {
				PixelColor pOut = bmp.image.getRgbPixel(i, 0);
				String printRGB = "X/Y: " + i + "/0 " + "RGB: " + "(" + String.valueOf(pOut.r)
							+ "/" + String.valueOf(pOut.g) + "/" + String.valueOf(pOut.b) + ")";
				bw.write(printRGB);
				bw.newLine();

			}

			bw.close();
			osw.close();
			fos.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void bufferHeight(String outFilename, BmpImage bmp) {
		try {
			FileOutputStream fos = new FileOutputStream(outFilename + "_height_out.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos, "US-ascii");
			BufferedWriter bw = new BufferedWriter(osw);

			for (int j = 0; j < bmp.image.getHeight(); j++) {
				PixelColor pOut = bmp.image.getRgbPixel(0, j);
				String printRGB = "X/Y: " + j + "/0 " + "RGB: " + "(" + String.valueOf(pOut.r)
							+ "/" + String.valueOf(pOut.g) + "/" + String.valueOf(pOut.b) + ")";
				bw.write(printRGB);
				bw.newLine();
			}

			bw.close();
			osw.close();
			fos.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void reduceBits(BmpImage bmp,int rBits){

		double doubleExp = Math.pow(2, rBits);
		int intExp = (int) doubleExp;

		for(int i = 0; i < bmp.image.getWidth(); i++){
			for(int j = 0; j < bmp.image.getHeight(); j++){

				PixelColor pixel = bmp.image.getRgbPixel(i, j);

				pixel.r = (int) (pixel.r / intExp * doubleExp);
				pixel.g = (int) (pixel.g / intExp * doubleExp);
				pixel.b = (int) (pixel.b / intExp * doubleExp);
				
				bmp.image.setRgbPixel(i, j, pixel);		
				
			}
		}
	}

	public static void doSomething (BmpImage bmp){

		double[][] matrix = {{0.299,0.587,0.114 },
							 {-0.169,-0.331,0.5},
							 {0.5,-0.419,-0.081}};

		BmpImage yImage = new BmpImage();		
		RgbImage rgb = new RgbImage(bmp.image.getWidth(),
									bmp.image.getHeight(),
									bmp.image.getBitsPerPixel());




		for(int width = 0; width < bmp.image.getWidth(); width++){
			for(int height = 0; height < bmp.image.getHeight(); height++ ){

				PixelColor pixel = bmp.image.getRgbPixel(width, height);

				int yValue = (int)(((matrix[0][0] * pixel.r) + 0) + 
							((matrix[0][1] * pixel.g) + 128) + 
							((matrix[0][2] * pixel.b) + 128));
				int Cb = (int)(((matrix[1][0] * pixel.r) + 0) + 
							((matrix[1][1] * pixel.g) + 128) + 
							((matrix[1][2] * pixel.b) + 128));
				int Cr = (int)(((matrix[2][0] * pixel.r) + 0) + 
							((matrix[2][1] * pixel.g) + 128) + 
							((matrix[2][2] * pixel.b) + 128));

				PixelColor yPixel = new PixelColor(yValue,yValue,yValue);
				yImage.image.setRgbPixel(width,height,yPixel);
												
				
			}
		}

		
	}

	
}
