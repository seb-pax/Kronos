package com.pacreau.seb.kronos.alert;

/**
 * Kronos
 * com.pacreau.seb.kronos.alert
 *
 * @author spacreau
 * @since 02/03/2017
 */
import com.pacreau.seb.kronos.BR;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A dummy item representing a piece of content.
 */
public class Alert extends BaseObservable implements Parcelable {

	@Bindable
	private String id = null;

	@Bindable
	private long totalDuration = -1;

	@Bindable
	private long maxCount  = 1;

	@Bindable
	private String info = "";

	public Alert() {

	}
	public Alert(long p_totalDuration, long p_maxCount, String p_info) {
		this.totalDuration = p_totalDuration;
		this.maxCount = p_maxCount;
		if (p_info == null && p_totalDuration > 0) {
			this.info = "Durée : " + AlertDao.formatLongInTime(p_totalDuration * 1000);
		} else {
			this.info = p_info;
		}

	}

	public Alert(Parcel parcel) {
		id = parcel.readString();
		totalDuration = parcel.readLong();
		maxCount = parcel.readLong();
		info = parcel.readString();
	}

	public String getId() {
		return id;
	}

	public void setId(String p_id) {
		this.id = p_id;
	}

	public long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(long p_totalDuration) {
		this.totalDuration = p_totalDuration;
		notifyPropertyChanged(BR.totalDuration);
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long p_maxCount) {
		this.maxCount = p_maxCount;
		notifyPropertyChanged(BR.maxCount);

	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String p_info) {
		this.info = p_info;
		notifyPropertyChanged(BR.info);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(id);
		parcel.writeLong(totalDuration);
		parcel.writeLong(maxCount);
		parcel.writeString(info);
	}

	public static final Parcelable.Creator<Alert> CREATOR = new Parcelable.Creator<Alert>() {
		public Alert createFromParcel(Parcel in) {
			return new Alert(in);
		}

		public Alert[] newArray(int size) {
			return new Alert[size];
		}
	};

	@Override
	public String toString() {
		return "id" + this.id + "totalDuration" + this.totalDuration + "maxCount" + this.maxCount
				+ "info" + this.info;
	}

	public VALIDATION_ALERT validate() {
		if ( totalDuration <= 0) {
			return VALIDATION_ALERT.TOTAL_DURATION_INVALID;
		}

		return  VALIDATION_ALERT.VALID;
	}

	public enum VALIDATION_ALERT {
		VALID,
		TOTAL_DURATION_INVALID,
		MAXCOUNT_INVALID,
		INFO_INVALID
	}
}