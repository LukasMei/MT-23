package bmp_io;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

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

			bufferHistogramm(outFilename, bmp);

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

	public static void reduceBits(BmpImage bmp, int rBits) {

		double doubleExp = Math.pow(2, rBits);
		int intExp = (int) doubleExp;

		for (int i = 0; i < bmp.image.getWidth(); i++) {
			for (int j = 0; j < bmp.image.getHeight(); j++) {

				PixelColor pixel = bmp.image.getRgbPixel(i, j);

				pixel.r = (int) (pixel.r / intExp * doubleExp);
				pixel.g = (int) (pixel.g / intExp * doubleExp);
				pixel.b = (int) (pixel.b / intExp * doubleExp);

				bmp.image.setRgbPixel(i, j, pixel);

			}
		}
	}

	public static void convertRGBtoYCbCr(BmpImage bmp) {

		double[][] matrix = { { 0.299, 0.587, 0.114 },
				{ -0.169, -0.331, 0.5 },
				{ 0.5, -0.419, -0.081 } };

		for (int width = 0; width < bmp.image.getWidth(); width++) {
			for (int height = 0; height < bmp.image.getHeight(); height++) {

				PixelColor pixel = bmp.image.getRgbPixel(width, height);

				int yValue = (int) (((matrix[0][0] * pixel.r) + 0) +
						((matrix[0][1] * pixel.g) + 0) +
						((matrix[0][2] * pixel.b) + 0));

				int cb_r = (int) ((matrix[1][0] * pixel.r) + 128);
				int cb_g = (int) ((matrix[1][1] * pixel.g) + 128);
				int cb_b = (int) ((matrix[1][2] * pixel.b) + 128);

				int cr_r = (int) ((matrix[2][0] * pixel.r) + 128);
				int cr_g = (int) ((matrix[2][1] * pixel.g) + 128);
				int cr_b = (int) ((matrix[2][2] * pixel.b) + 128);

				PixelColor yPixel = new PixelColor(cb_r, cb_g, cb_b);
				bmp.image.setRgbPixel(width, height, yPixel);

			}
		}

	}

	public static void convertYCbCrtoRGB(BmpImage bmp) {

		double[][] matrixRGB = { { 0.299, 0.587, 0.114 },
				{ -0.169, -0.331, 0.5 },
				{ 0.5, -0.419, -0.081 } };

		double[][] matrixYCrCb = { { 1.00, 0.00, 1.403 },
				{ 1.00, -0.344, -0.714 },
				{ 1.00, 1.773, 0.00 } };

		for (int width = 0; width < bmp.image.getWidth(); width++) {
			for (int height = 0; height < bmp.image.getHeight(); height++) {

				PixelColor pixel = bmp.image.getRgbPixel(width, height);

				int yValue = (int) (((matrixRGB[0][0] * pixel.r) + 0) +
						((matrixRGB[0][1] * pixel.g) + 0) +
						((matrixRGB[0][2] * pixel.b) + 0));

				int cb = (int) ((matrixRGB[1][0] * pixel.r) +
						(matrixRGB[1][1] * pixel.g) +
						(matrixRGB[1][2] * pixel.b)) + 128;

				int cr = (int) ((matrixRGB[2][0] * pixel.r) +
						(matrixRGB[2][1] * pixel.g) +
						(matrixRGB[2][2] * pixel.b)) + 128;

				int r = (int) ((matrixYCrCb[0][0] * yValue) +
						(matrixYCrCb[0][1] * (cb - 128)) +
						(matrixYCrCb[0][2]) * (cr - 128));

				int g = (int) ((matrixYCrCb[1][0] * yValue) +
						(matrixYCrCb[1][1] * (cb - 128)) +
						(matrixYCrCb[1][2]) * (cr - 128));

				int b = (int) ((matrixYCrCb[2][0] * yValue) +
						(matrixYCrCb[2][1] * (cb - 128)) +
						(matrixYCrCb[2][2]) * (cr - 128));

				PixelColor yPixel = new PixelColor(r, g, b);
				bmp.image.setRgbPixel(width, height, yPixel);

			}
		}

	}

	public static void bufferHistogramm(String outFilename, BmpImage image) {
		String histogrammFilename = outFilename + "_histogram.txt";

		try {
			FileOutputStream fos = new FileOutputStream(histogrammFilename);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);		

			for (int width = 0; width < image.image.getWidth(); width++) {
				for (int height = 0; height < image.image.getHeight(); height++) {

					PixelColor pixel = image.image.getRgbPixel(width, height);

					double yColor = (0.299 * pixel.r) + (0.587 * pixel.g) + (0.114 * pixel.b);									
					
					bw.write(String.valueOf((int) yColor));
					bw.newLine();
				}
			}

			
				
			bw.close();
			osw.close();
			fos.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
