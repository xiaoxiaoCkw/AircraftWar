package edu.hitsz.music;

import edu.hitsz.application.game.BaseGame;
import edu.hitsz.application.Main;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 音频播放线程
 *
 * @author zhangzewei
 */
public class MusicThread extends Thread {

    /**
     * 音频文件名
     */
    private final String filename;
    private AudioFormat audioFormat;
    private byte[] samples;
    /**
     * 循环播放标志
     */
    private boolean playBackFlag;

    private static final String BGM_FILE = "src/audio/bgm.wav";
    private static final String BGM_BOSS_FILE = "src/audio/bgm_boss.wav";

    public MusicThread(String filename) {
        //初始化filename
        this.filename = filename;
        reverseMusic();
    }

    public void reverseMusic() {
        try {
            //定义一个AudioInputStream用于接收输入的音频数据，使用AudioSystem来获取音频的音频输入流
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            //用AudioFormat来获取AudioInputStream的格式
            audioFormat = stream.getFormat();
            samples = getSamples(stream);
        } catch (UnsupportedAudioFileException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        playBackFlag = filename.equals(BGM_FILE) || filename.equals(BGM_BOSS_FILE);
    }

    public byte[] getSamples(AudioInputStream stream) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        DataInputStream dataInputStream = new DataInputStream(stream);
        try {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return samples;
    }

    public void play(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        //源数据行SourceDataLine是可以写入数据的数据行
        SourceDataLine dataLine = null;
        //获取受数据行支持的音频格式DataLine.info
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assert dataLine != null;
        dataLine.start();
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1) {
                //从音频流读取指定的最大数量的数据字节，并将其放入缓冲区中
                numBytesRead =
                        source.read(buffer, 0, buffer.length);
                //通过此源数据行将数据写入混频器
                if (numBytesRead != -1) {
                    dataLine.write(buffer, 0, numBytesRead);
                }

                if (BaseGame.isGameOver()) {
                    // 游戏结束停止播放所有音效
                    playBackFlag = false;
                    break;
                }
                if (filename.equals(BGM_BOSS_FILE) && BaseGame.isBossCrash()) {
                    // Boss敌机坠毁停止播放Boss敌机背景音乐
                    playBackFlag = false;
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        dataLine.drain();
        dataLine.close();

    }

    @Override
    public void run() {
        if (Main.audioON) {
            // 当游戏音效开关打开时播放音效
            do {
                InputStream stream = new ByteArrayInputStream(samples);
                play(stream);
            } while (playBackFlag);
        }
    }
}
