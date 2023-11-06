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

			bufferWith(outFilename, bmp);
			bufferHeight(outFilename, bmp);

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
}
