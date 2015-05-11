package com.nanlagger.schooljournal.app.utils;

import android.util.Log;

public class MLogger {
	
	
	public static final void console(Class<?> cls, String message){
		Log.v(cls.getSimpleName(), message);
	}
	public static final void console(Class<?> cls, String message, Throwable ex){
		Log.e(cls.getSimpleName(), message, ex);
	}
	public static final void console(Class<?> cls,  Throwable ex){
		Log.e(cls.getSimpleName(), ex != null ? ex.getMessage() : "No message", ex);
	}

}
