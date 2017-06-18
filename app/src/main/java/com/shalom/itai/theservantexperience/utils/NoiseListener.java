package com.shalom.itai.theservantexperience.utils;




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
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
            recorder.start();
            isListening = true;
            recorder.getMaxAmplitude();
        }


    public double stop() {
        double amplitudeDb =0;
        if(isListening) {
            double  amplitude = recorder.getMaxAmplitude();
            amplitudeDb = 20 * Math.log10(Math.abs(amplitude));
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