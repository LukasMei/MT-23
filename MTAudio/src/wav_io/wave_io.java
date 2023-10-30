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
			try { throw new WavFileException("At least one filename specified  (" + args.length + ")"); }
			catch (WavFileException e1) { e1.printStackTrace(); }
		}
	
		
		// ********************************************************
		// Implementierung bei einem Eingabeparameter

		inFilename=args[0];
		
		try {
			readWavFile = WavFile.read_wav(inFilename);
			
			// headerangaben
			numFrames = readWavFile.getNumFrames(); 
			
			// Anzahl der Kanaäle (mono/stereo)
			numChannels = readWavFile.getNumChannels();
			
			// Anzahl Abtastpunkte
			samples = (int) numFrames*numChannels;
			
			// Bitszahl
			validBits = readWavFile.getValidBits();
			
			// Abtastrate 
			sampleRate = readWavFile.getSampleRate();
			
			/* for (int i = 0; i < samples; i++) {
				System.out.println(readWavFile.sound[i]);
			} */

			/* int reducedBits = 12;

			for(int i = 0; i < samples; i++){
				readWavFile.sound[i] /= Math.pow(2, reducedBits);

				readWavFile.sound[i] *= Math.pow(2, reducedBits);

			} */

			// Bit reduktion difference Aufgabe 2.6
        
			int reduceBits = 8;
			int [] originalValue = new int [samples];
			
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

			// Implementierung
			// Zugriff auf die einzelne Samples mit readWavFile.sound[i]

			// Aufgabe Downsampling

           /*  for (int i = 0; i < samples /2; i++) {
                readWavFile.sound[i] = readWavFile.sound[2*i];
            }
            sampleRate /= 2;
            numFrames /= 2; */
			
		} catch (IOException | WavFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (args.length == 1) 
			System.exit(0);
			
		
		// ***********************************************************
		// Implementierung bei Ein-und Ausgabeparameter (Speichern der Ausgabedatei)
		
		outFilename=args[1];
		
		
		// Implementierung
		// Aufgabe 2. a)
/* 
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

			
           
			
		} catch (IOException e1){
			e1.printStackTrace();
		} */

		 
		
		
		// Speicherung
		try {
			WavFile.write_wav(outFilename, numChannels, numFrames, validBits, sampleRate, readWavFile.sound);
		} catch (IOException | WavFileException e) {
			e.printStackTrace();
		}
	}
}
