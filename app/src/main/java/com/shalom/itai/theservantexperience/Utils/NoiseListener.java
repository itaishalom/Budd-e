package com.shalom.itai.theservantexperience.Utils;




import android.media.MediaRecorder;

import java.io.IOException;

public class NoiseListener {

    private MediaRecorder recorder = null;
    private boolean isListening = false;
    public NoiseListener(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");
    }

    public void start() {
        try {

            recorder.prepare();
            recorder.start();
            isListening = true;
            recorder.getMaxAmplitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
     catch (IllegalStateException e) {
        e.printStackTrace();
    }
    }


    public double stop() {
        double amplitudeDb =0;
        if(isListening) {
           double  amplitude = recorder.getMaxAmplitude();
             amplitudeDb = 20 * Math.log10((double) Math.abs(amplitude));
            recorder.stop();
        }
        return amplitudeDb;
    }
    public void dispose() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }
}