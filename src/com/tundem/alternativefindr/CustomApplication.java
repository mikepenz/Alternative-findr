package com.tundem.alternativefindr;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

//mailTo = "mikepenz@gmail.com",
/*@ReportsCrashes(formKey = "", formUri = "http://www.tundem.com/android/gameframework/error_sendmail.php", mode = ReportingInteractionMode.NOTIFICATION, customReportContent = {
		ReportField.PACKAGE_NAME, ReportField.APP_VERSION_NAME, ReportField.USER_EMAIL, ReportField.USER_COMMENT,
		ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE,
		ReportField.LOGCAT }, resNotifTickerText = R.string.crash_notif_ticker_text, resNotifTitle = R.string.crash_notif_title, resNotifText = R.string.crash_notif_text, resDialogText = R.string.crash_dialog_text, resDialogEmailPrompt = R.string.crash_dialog_email_prompt, resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, resDialogOkToast = R.string.crash_dialog_ok_toast)
*/
@ReportsCrashes(formKey = "", // will not be used
mailTo = "error@tundem.com", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_dialog_text)
public class CustomApplication extends Application {
	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		super.onCreate();
	}
}