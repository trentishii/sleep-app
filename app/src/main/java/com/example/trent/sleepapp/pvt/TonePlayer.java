package com.example.trent.sleepapp.pvt;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Plays audio tones of a specified frequency. 
 * Based on http://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android
 *  
 * @author mjskay
 *
 */
public class TonePlayer {
	private final int duration = 1;		// duration in seconds
	private final int sampleRate = 8000;
	private final int numSamples;
	private final AudioTrack audioTrack;

	/**
	 * Construct a TonePlayer that plays a 1000Hz tone.
	 * @param frequency
	 */
	public TonePlayer() {
		this(1000);
	}
	
	/**
	 * Construct a TonePlayer that plays a tone of the given frequency (in Hz).
	 * @param frequency
	 */
	public TonePlayer(final double frequency) {
		numSamples = (int) Math.ceil(duration * sampleRate);
		final short[] generatedSound = new short[numSamples];

		//generate 16bit waveform
		for (int i = 0; i < numSamples; ++i) {
			final double sample = Math.sin((double)(frequency * 2 * Math.PI * i) / (double)(sampleRate));
			// scale to maximum amplitude and write to buffer
			generatedSound[i] = (short) ((sample * Short.MAX_VALUE));
		}

		// build an AudioTrack for this tone
		audioTrack = new AudioTrack(
				AudioManager.STREAM_MUSIC,
				sampleRate, 
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, 
				generatedSound.length,
				AudioTrack.MODE_STATIC
				);
		audioTrack.write(generatedSound, 0, generatedSound.length);
		
		//when played, loop until stopped
		audioTrack.setLoopPoints(0, numSamples / 2, -1);
	}

	/**
	 * Start playing this tone. Tone plays until {@link #stop()} is called.
	 */
	void play() {
		audioTrack.play();
	}
  
	/**
	 * Stop playing this tone.
	 */
	void stop() {
		try {
			audioTrack.pause();
		}
		catch (IllegalStateException e) {
			// ignore this --- typically happens if the AudioTrack hasn't been initialized yet,
			// in which case a failure to pause it is irrelevant if the expected behaviour
			// is to stop the tone (since it won't have started in the first place).
			e.printStackTrace();
		}
  }
  
  /**
   * Called to release underlying resources used to play this tone.
   */
  void release() {
  	audioTrack.release();
  }
}
