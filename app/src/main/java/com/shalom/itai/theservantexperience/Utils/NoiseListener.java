package com.shalom.itai.theservantexperience.Utils;




import android.media.MediaRecorder;

import java.io.IOException;

public class NoiseListener {

    private MediaRecorder recorder = null;

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
            recorder.getMaxAmplitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public double stop() {
        int amplitude = recorder.getMaxAmplitude();
        double amplitudeDb = 20 * Math.log10((double)Math.abs(amplitude));
        recorder.stop();
        return amplitudeDb;
    }
    public void dispose() {
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }
}