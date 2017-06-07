package com.pacreau.seb.kronos.service;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Kronos
 * com.pacreau.seb.kronos.service
 *
 * @author spacreau
 * @since 18/05/2017
 */

public class RingtoneService {

	private static final float VOLUME = 0.80f;

	private MediaPlayer ringtonePlayer;

	private static RingtoneService instance;

	private RingtoneService() {
		ringtonePlayer = new MediaPlayer();

	}

	public static RingtoneService getInstance() {
		if (instance == null) {
			instance = new RingtoneService();
		}

		return instance;
	}


	public void prepare(Activity p_oActivity) {
		p_oActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		ringtonePlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		try {
			Uri ringtoneAlert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			ringtonePlayer.setDataSource(p_oActivity, ringtoneAlert);
		} catch (IOException e) {
			Log.d(getClass().getSimpleName(), " java.io.IOException setDataSource ", e);
		}

		final AudioManager audioRingtoneManager = (AudioManager) p_oActivity.getSystemService(Context.AUDIO_SERVICE);

		if (audioRingtoneManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
			//ringtonePlayer.setAudioStreamType(AudioManager.STREAM_RING);
			ringtonePlayer.setVolume(VOLUME, VOLUME);
			ringtonePlayer.prepareAsync();
		}
	}

	public void startRingtone(boolean p_iEnBoucle) {
		ringtonePlayer.setLooping(p_iEnBoucle);
		ringtonePlayer.start();
	}

	public void stopRingtone() {
		ringtonePlayer.stop();
		ringtonePlayer.prepareAsync();
	}

	public void releaseRingtone() {
		ringtonePlayer.release();
	}
}
