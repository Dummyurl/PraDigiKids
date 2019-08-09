package com.pratham.pradigikids.util;

import android.media.MediaRecorder;
import android.util.Log;

public class Audio extends Thread {
    private boolean stopped = false;
    private final String recFilePath;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    /**
     * Give the thread high priority so that it's not canceled unexpectedly, and start it
     */

    public Audio(String recFilePath) {
        this.recFilePath = recFilePath;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
    }

    @Override
    public void run() {
        /*Log.i("Audio", "Running Audio Thread");
        AudioRecord recorder = null;
        AudioTrack track = null;
        short[][]   buffers  = new short[256][160];
        int ix = 0;
*/
        /*
         * Initialize buffer to hold continuously recorded audio data, start recording, and start
         * playback.
         */
        try {
            /*int N = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,N);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, N, AudioTrack.MODE_STREAM);
            recorder.startRecording();
            track.play();

            while(stopped)
            {
                Log.i("Map", "Writing new data to buffer");
                short[] buffer = buffers[ix++ % buffers.length];
                N = recorder.read(buffer,0,buffer.length);
                track.write(buffer, 0, buffer.length);
            }*/

            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setAudioEncodingBitRate(96000);
                mediaRecorder.setAudioSamplingRate(44100);
                mediaRecorder.setOutputFile(recFilePath);
                mediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaRecorder.start();
            isRecording = true;

        } catch (Throwable x) {
            Log.w("Audio", "Error reading voice audio", x);
        }
        /*
         * Frees the thread's resources after the loop completes so that it can be run again
         */
        /*finally
        {
            recorder.stop();
            recorder.release();
            track.stop();
            track.release();
        }*/
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */
    public void close() {
        //stopped = false;
        try {
            if (isRecording) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*else {
            mediaPlayer.release();
            mediaPlayer = null;}
*/
    }

}