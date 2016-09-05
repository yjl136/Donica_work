package cn.donica.slcd.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * @author 梁明杰
 * @desc 音量调节工具类
 * @since 2015-10-23 上午11:26:54
 */
public class AudioUtil {

    /**
     * 获取最大音量
     *
     * @return
     */
    public static int getMaxAudio(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 获取当前音量
     */
    public static int getCurrentAudio(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 设置当前音量
     */
    public static void setCurrentAudio(Context context, int progress) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    }

    /**
     * 设置静音
     *
     * @param context
     * @param isMute  true:静音 false:不静音
     */
    public static void setAudioMute(Context context, boolean isMute) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, isMute);
    }
}