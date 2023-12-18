package wav_io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class wave_io {

	public static void main(String[] args) {

		int samples = 0;
		int validBits = 0;
		long sampleRate = 0;
		long numFrames = 0;
		int numChannels = 0;

		String inFilename = null;
		String outFilename = null;

		WavFile readWavFile = null;

		if (args.length < 1) {
			try {
				throw new WavFileException("At least one filename specified  (" + args.length + ")");
			} catch (WavFileException e1) {
				e1.printStackTrace();
			}
		}

		// ********************************************************
		// Implementierung bei einem Eingabeparameter

		inFilename = args[0];

		try {
			readWavFile = WavFile.read_wav(inFilename);

			// headerangaben
			numFrames = readWavFile.getNumFrames();

			// Anzahl der Kanaäle (mono/stereo)
			numChannels = readWavFile.getNumChannels();

			// Anzahl Abtastpunkte
			samples = (int) numFrames * numChannels;

			// Bitszahl
			validBits = readWavFile.getValidBits();

			// Abtastrate
			sampleRate = readWavFile.getSampleRate();

			

			// Implementierung
			// Zugriff auf die einzelne Samples mit readWavFile.sound[i]
			clipping(samples, readWavFile);

		} catch (IOException | WavFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (args.length == 1)
			System.exit(0);

		// ***********************************************************
		// Implementierung bei Ein-und Ausgabeparameter (Speichern der Ausgabedatei)

		outFilename = args[1];

		// Speicherung
		try {
			WavFile.write_wav(outFilename, numChannels, numFrames, validBits, sampleRate, readWavFile.sound);
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		}
	}

	public static void printSamples(int samples, WavFile readWavFile) {
		for (int i = 0; i < samples; i++) {
			System.out.println(readWavFile.sound[i]);
		}
	}

	public static void reduceBits(int samples, WavFile readWavFile) {
		int reducedBits = 12;

		for (int i = 0; i < samples; i++) {
			readWavFile.sound[i] /= Math.pow(2, reducedBits);
			readWavFile.sound[i] *= Math.pow(2, reducedBits);

		}

	}

	// Bit reduktion difference Aufgabe 2.6
	public static void bitReduceDifference(int samples, WavFile readWavFile, int validBits) {

		int reduceBits = 8;
		int[] originalValue = new int[samples];

		for (int i = 0; i < samples; i++) {
			originalValue[i] = readWavFile.sound[i];
			// reduce bit amount
			readWavFile.sound[i] /= Math.pow(2, reduceBits);
			// countermeasure for the lower of the amplitude
			readWavFile.sound[i] *= Math.pow(2, reduceBits);

			// reduce the quantized value by its original value
			readWavFile.sound[i] -= originalValue[i];

			// countermeasure for the lower of the amplitude
			readWavFile.sound[i] *= Math.pow(2, validBits - reduceBits - 1);

		}

	}

	// Aufgabe Downsampling
	public static void downSample(int samples, WavFile readWavFile, long sampleRate, long numFrames) {
		for (int i = 0; i < samples / 2; i++) {
			readWavFile.sound[i] = readWavFile.sound[2 * i];
		}
		sampleRate /= 2;
		numFrames /= 2;

	}

	// Implementierung
	// Aufgabe 2. a)
	public static void buffer(String outFilename, int samples, WavFile readWavFile) {

		try {
			FileOutputStream fos = new FileOutputStream(outFilename + "_out.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos, "US-ascii");
			BufferedWriter bw = new BufferedWriter(osw);

			for (int i = 0; i < samples; i++) {
				bw.write(String.valueOf(readWavFile.sound[i]));
				bw.newLine();
			}

			bw.close();
			osw.close();
			fos.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/*
	 * Aufgabe 6_1
	 */
	public static void clipping(int samples,WavFile readWavFile) {
		/*
		 * factor = 10^(db/20)
		 */
		double factor_6 = 1.995;
		double factor_9 = 2.818;
		double factor_12 = 3.981;
		

		for(int i = 0; i < samples; i++){		
			
			double ergebnis =  readWavFile.sound[i];
			ergebnis *= factor_6;

			readWavFile.sound[i] = (short) ergebnis;

			if(ergebnis < -32768){
				readWavFile.sound[i] = -32768;
			}
			if(ergebnis > 32767){
				readWavFile.sound[i] = 32767;
			}
		}

	}
}
