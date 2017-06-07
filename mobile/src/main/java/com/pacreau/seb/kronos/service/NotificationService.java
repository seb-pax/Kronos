package com.pacreau.seb.kronos.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import com.pacreau.seb.kronos.R;

/**
 * Kronos
 * com.pacreau.seb.kronos
 *
 * @author spacreau
 * @since 03/03/2017
 */

public class NotificationService {

	private static final String NOTIF_ID_PREF_KEY = "NOTIF_ID_PREF_KEY";

	private static NotificationService instance;

	private NotificationService() {
	}

	public static NotificationService getInstance() {
		if (instance == null) {
			instance = new NotificationService();
		}

		return  instance;
	}

	public static void sendLocalNotification(Context p_oContext, String p_sTitle, String p_sDetail, String p_sComplement) {

		String title = p_sTitle;
		String content = p_sComplement;
		// récup du son dans les prefs
		Uri uriNotificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(p_oContext);
		notifBuilder
				.setDefaults(Notification.DEFAULT_ALL)
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(R.mipmap.ic_launcher)
				.setTicker(title)
				.setContentTitle(title)
				.setContentText(content);
		notifBuilder.setSound(uriNotificationSound);

		// Mise en forme multiligne
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle(title);
		inboxStyle.addLine(content).addLine(p_sDetail);
		notifBuilder.setStyle(inboxStyle);

		NotificationManager notificationManager = (NotificationManager) p_oContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(computeNotificationId(p_oContext), notifBuilder.build());
	}

	private static int computeNotificationId(Context p_oContext) {
		int iNewId = 1 + PreferenceManager.getDefaultSharedPreferences(p_oContext).getInt(NOTIF_ID_PREF_KEY, 0);
		PreferenceManager.getDefaultSharedPreferences(p_oContext).edit().putInt(NOTIF_ID_PREF_KEY, iNewId).commit();
		return iNewId;
	}
}
