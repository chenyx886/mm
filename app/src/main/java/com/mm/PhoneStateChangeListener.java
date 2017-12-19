/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mm;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Company：苗苗
 * Class Describe： 手机状态监听器
 * Create Person：Chenyx
 * Create Time：2016/10/13 11:30
 * Update Person：
 * Update Time：
 * Update Remark：
 */
public class PhoneStateChangeListener extends PhoneStateListener {

	private static final String LOGTAG = PhoneStateChangeListener.class.getName();

	private UploadLocationService locationService;

	public PhoneStateChangeListener(UploadLocationService locationService) {
		this.locationService = locationService;
	}

	@Override
	public void onDataConnectionStateChanged(int state) {
		super.onDataConnectionStateChanged(state);
		Log.d(LOGTAG, "onDataConnectionStateChanged()...");
		Log.d(LOGTAG, "Data Connection State = " + getState(state));
		if (state == TelephonyManager.DATA_CONNECTED) {

			// 位置上传Service
			if (MApplication.getInstance().uploadIsClosing()) {
				MApplication.getInstance().startService(UploadLocationService.createIntent(MApplication.getInstance().getApplicationContext()));
			}
		}
	}

	private String getState(int state) {
		switch (state) {
		case 0: // '\0'
			return "DATA_DISCONNECTED";
		case 1: // '\001'
			return "DATA_CONNECTING";
		case 2: // '\002'
			return "DATA_CONNECTED";
		case 3: // '\003'
			return "DATA_SUSPENDED";
		}
		return "DATA_<UNKNOWN>";
	}

}
