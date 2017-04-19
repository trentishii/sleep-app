package com.example.trent.sleepapp;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.trent.sleepapp.WavFile.*;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

public class Recorder {
    private static final String ANDROID = "Android";
    private static final String DATA = "data";
    private static final String FILES = "files";
    public static final String PREFNAME = "userPrefs";

    public Context context;
    private AudioRecord recorder;
    private int SAMPLINGRATE = 16000; //16000 samples/sec
    private int NUMCHANNELS = 1;
    private int PCM_16BIT_ENCODED = 16;
    private boolean isRecording;
    private Thread recordingThread = null;
    private short[] recShortBuffer;
    //private short[] recLongBuffer;;
    private final int recShortBufferSize = 128;	//Number of frames being read from recorder and written to audio file each time
    private int allIndex = 0;

    private Intent analyzeNewAudioIntent;

    private File SleepAudioFilesDir;
    private String audioFile;
    private WavFile newWavFile;

    /////CHOOSE DESIRED AUDIO FILE LENGTH (IN NUMBER OF SAMPLES)/////
    private int audioFileLength = SAMPLINGRATE * 30; //Create audio files of 30sec length
    //private int audioFileLength = SAMPLINGRATE * 60; //Create audio files of 1min length
    //private int audioFileLength = SAMPLINGRATE * 60 * 5; //Create audio files of 5min length
    //private int audioFileLength = SAMPLINGRATE * 60 * 10; //Create audio files of 10min length
    //private int audioFileLength = SAMPLINGRATE * 60 * 30; //Create audio files of 30min length

    public Recorder(Context context) {
        this.context = context;
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        SleepAudioFilesDir = new File(new File(new File(new File(
                externalPath, ANDROID), DATA), packageName), FILES);
    }

    public void recordNewAudio(){
        recShortBuffer = new short[recShortBufferSize];

        int bufferSize = AudioRecord.getMinBufferSize(SAMPLINGRATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLINGRATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        isRecording = true;
        recorder.startRecording();

        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioToFile();
            }
        }, "Recording Thread");
        recordingThread.start();
    }

    public void writeAudioToFile(){
        try {


            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault());
            String startTimeStamp = sdf.format(new Date());
            audioFile = "Audio-" + startTimeStamp + ".wav";

            //Create wav file of set length (with data header size) of chosen "audioFileLength"
            newWavFile = WavFile.newWavFile(new File(SleepAudioFilesDir, audioFile), NUMCHANNELS, audioFileLength,
                    PCM_16BIT_ENCODED, SAMPLINGRATE);
            System.out.println("Recording for new audio file: " + audioFile);

            int numShortsRead = 0;
            int numShortsReadIndex = 0;
            allIndex = 0;

            while(isRecording == true){
                //Read 128 shorts from recorder at a time
                numShortsRead = recorder.read(recShortBuffer, 0, recShortBufferSize);

                if(numShortsRead != recShortBufferSize)System.err.print("Num of shorts read: "+numShortsRead);

                if(numShortsRead == recShortBufferSize){
                    double[] arr = new double[recShortBuffer.length];
                    for (int j =0;j < recShortBuffer.length;j++) {
                        arr[j] = (double)recShortBuffer[j];
                    }
                    newWavFile.writeFrames(arr, numShortsRead);

                    allIndex += numShortsRead;
                    numShortsReadIndex += numShortsRead;

                    //At every audioFileLength, analyze completed audio file and create new audio file
                    if(numShortsReadIndex == audioFileLength){

                        newWavFile.close();
                        System.out.println("Done recording for audio file: " + audioFile);


                        //Create wav file of set length (with data header size) of chosen "audioFileLength"
                        sdf = new SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault());
                        startTimeStamp = sdf.format(new Date());
                        audioFile = "Audio-" + startTimeStamp + ".wav";
                        newWavFile = WavFile.newWavFile(new File(SleepAudioFilesDir, audioFile), NUMCHANNELS, audioFileLength,
                                PCM_16BIT_ENCODED, SAMPLINGRATE);
                        System.out.println("Recording for new audio file: " + audioFile);
                        allIndex = 0;
                        numShortsRead = 0;
                        numShortsReadIndex = 0;
                    }
                }

                //else{
                //	Log.w("Speech_Sanitation", "Number of shorts read from recorder not recShortBufferSize (128)");
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(){
        if(recorder != null){
            try {
                isRecording = false;
                recorder.stop();
                recorder.release();

                int numSec = allIndex / SAMPLINGRATE;
                int numMin = numSec / 60;
                System.out.println("allIndex: "+allIndex+" | numSec: "+numSec+" | numMin: "+numMin);
                newWavFile.close();

                //Modify header because file is of length different than preset length
                File fileToModify  = new File(SleepAudioFilesDir, audioFile);
                fixHeader(fileToModify);

                //Check to make sure can open newly created wav file ok
                //WavFile wf = WavFile.openWavFile(fileToModify);

                System.out.println("Created new audio file: " + audioFile);

                allIndex = 0;
                recorder = null;
                recordingThread = null;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void fixHeader(File fileToModify){

        try{
            //Change header of created wav file where recording was prematurely stopped
            // (from preset size to actual size of newly created file)

            RandomAccessFile file = new RandomAccessFile(fileToModify, "rw");
            FileChannel fc = file.getChannel();

            //Modify "Size of file" in header
            //Size of file = (Number of frames (shorts) X 2) + 36 (Num of remaining header bytes)
            //36 = num bytes of remaining header. 44 total header bytes and size of file is 2nd field
            int newFileSize = (allIndex * 2) + 36;
            byte[] temp = ByteBuffer.allocate(4).putInt(newFileSize).array();
            int n = 3;
            byte[] newFileSizeBytes = new byte[4];
            //Reverse bytes being written in header
            for(int i = 0; i < 4; i++){newFileSizeBytes[i] = temp[n]; n--;}
            ByteBuffer out = ByteBuffer.wrap(newFileSizeBytes);
            fc.position(4);
            while(out.hasRemaining()){
                fc.write(out);
            }

            //Modify "Size of data" field in header
            //Size of data = Number of frames (shorts) X 2
            newFileSize = (allIndex * 2);
            temp = ByteBuffer.allocate(4).putInt(newFileSize).array();
            n = 3;
            newFileSizeBytes = new byte[4];
            //Reverse bytes being written in header
            for(int i = 0; i < 4; i++){newFileSizeBytes[i] = temp[n]; n--;}
            out = ByteBuffer.wrap(newFileSizeBytes);
            fc.position(40);
            while(out.hasRemaining()){
                fc.write(out);
            }

            file.close();
            fc.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

