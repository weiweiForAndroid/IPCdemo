package com.test.ipcdemo;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;


public class NLog {

	public static final String TAG = "Noodle";
	public static final boolean SHOWLOG = true;

	public static void log(Object obj) {
		String msg;
		if (obj instanceof Throwable) {
			StringWriter sw = new StringWriter();
			((Throwable) obj).printStackTrace(new PrintWriter(sw));
			msg = sw.toString();
		} else {
			msg = String.valueOf(obj);
		}
		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();

		msg = callclassname + " -> " + callmethodname + ": " + msg;
		_d(TAG, msg);
	}

	/**
	 * 命名为log4format是为了区分函数<BR>
	 * log(String tag, String msg)<BR>
	 * 
	 * LogUtils.log4format()<BR>
	 * <P>
	 * Author : mingli
	 * </P>
	 * <P>
	 * Date : 2013-4-26
	 * </P>
	 * 
	 * @param format
	 * @param args
	 */
	public static void log4format(String format, Object... args) {
		String msg = String.format(format, args);
		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();

		msg = callclassname + " -> " + callmethodname + ": " + msg;

		_d(TAG, msg);
	}

	/**
	 * LogUtils.log()<BR>
	 * <P>
	 * Author : 荣承壮
	 * </P>
	 * <P>
	 * Date : 2012-11-5
	 * </P>
	 * 
	 * @param msg
	 * 
	 */
	public static void log(String msg) {
		if (msg == null || "".equals(msg)) {
			return;
		}
		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();

		msg = callclassname + " -> " + callmethodname + ": " + msg;

		_d(TAG, msg);
	}

	public static void r(String msg) {
		Log.e("mafg", msg);
	}

	/**
	 * LogUtils.log()<BR>
	 * <P>
	 * Author : 荣承壮
	 * </P>
	 * <P>
	 * Date : 2012-11-6
	 * </P>
	 * 
	 * @param tag
	 * @param msg
	 *
	 * 
	 */
	public static void log(String tag, String msg) {
		if (msg == null || "".equals(msg)) {
			return;
		}
		if (tag == null || "".equals(tag)) {
			log(msg);
			return;
		}
		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();

		msg = callclassname + " -> " + callmethodname + ": " + msg;

		_d(tag, msg);
	}

	/**
	 * LogUtils.error()<BR>
	 * <P>
	 * Author : 荣承壮
	 * </P>
	 * <P>
	 * Date : 2012-11-6
	 * </P>
	 * 
	 * @param msg
	 */

	public static void error(String msg) {
		if (!SHOWLOG){
			return;
		}
		if (msg == null || "".equals(msg)) {
			return;
		}
		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();

		msg = callclassname + " -> " + callmethodname + ": " + msg;
		_e(TAG, msg);
	}

	/**
	 * LogUtils.error()<BR>
	 * <P>
	 * Author : 荣承壮
	 * </P>
	 * <P>
	 * Date : 2012-11-6
	 * </P>
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void error(String tag, String msg) {
		if (msg == null || "".equals(msg)) {
			return;
		}
		if (tag == null || "".equals(tag)) {
			error(msg);
			return;
		}
		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();
		msg = callclassname + " -> " + callmethodname + ": " + msg;
		_e(tag, msg);
	}

	/**
	 * 重要log日志输出。 1.debug 模式时与<{@link #log(String)}<BR>
	 * 2.release 模式时，可以用工程模式来打开log输出<BR>
	 * 
	 * LFLog.log4important()<BR>
	 * <P>
	 * Author : mingli
	 * </P>
	 * <P>
	 * Date : 2013-8-9
	 * </P>
	 * 
	 * @param msg
	 */
	public static void log4important(String msg) {
		if (msg == null || "".equals(msg)) {
			return;
		}

		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();
		msg = callclassname + " -> " + callmethodname + ": " + msg;

		_important(TAG, msg);
	}

	public static void log4important(Object obj) {
		String msg;
		if (obj instanceof Throwable) {
			StringWriter sw = new StringWriter();
			((Throwable) obj).printStackTrace(new PrintWriter(sw));
			msg = sw.toString();
		} else {
			msg = String.valueOf(obj);
		}
		String callclassname = new Exception().getStackTrace()[1]
				.getClassName();
		String callmethodname = new Exception().getStackTrace()[1]
				.getMethodName();

		msg = callclassname + " -> " + callmethodname + ": " + msg;
		_important(TAG, msg);
	}


	private static void _d(String tag, String msg) {
		if (SHOWLOG) {
			Log.d(tag, msg);
		}
	}

	private static void _e(String tag, String msg) {
		if (SHOWLOG) {
			Log.e(tag, msg);
		}
	}

	/**
	 * 打印重要log，在release模式也会打印log LFLog._important()<BR>
	 * <P>
	 * Author : mingli
	 * </P>
	 * <P>
	 * Date : 2013-8-22
	 * </P>
	 * 
	 * @param tag
	 * @param msg
	 */
	private static void _important(String tag, String msg) {
		if (SHOWLOG) {
			Log.d(tag, msg);
		}
	}
}