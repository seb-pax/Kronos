package com.pacreau.seb.kronos;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

/**
 * Kronos
 * com.pacreau.seb.kronos
 *
 * @author spacreau
 * @since 07/03/2017
 */
public class CustomApplication extends Application {

	private FirebaseUser user = null;

	@Override
	public void onCreate() {
		super.onCreate();
		//AlertDao.getInstance().setPreferences(getApplicationContext().getSharedPreferences(AlertDao.KRONOS_ALERT_DAO,  Context.MODE_PRIVATE));
	}

	public FirebaseUser getUser() {
		return user;
	}

	public void setUser(FirebaseUser p_user) {
		this.user = p_user;
	}
}
