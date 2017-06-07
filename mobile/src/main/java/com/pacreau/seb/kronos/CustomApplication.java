package com.pacreau.seb.kronos;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

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
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
	}

	public FirebaseUser getUser() {
		return user;
	}

	public void setUser(FirebaseUser p_user) {
		this.user = p_user;
	}
}
