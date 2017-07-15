package com.techmali.smartteam.domain.services;

import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techmali.smartteam.utils.CryptoManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static final String EXTRA_PUSH_DATA = "push_data";

    private SharedPreferences prefManager = null;

//    PushNotificationData mPushData;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        prefManager = CryptoManager.getInstance(this).getPrefs();

        /*if (remoteMessage.getData() != null) {
            mPushData = new PushNotificationData(remoteMessage.getData().get(PARAMS.TAG_TITLE),
                    remoteMessage.getData().get(PARAMS.TAG_MESSAGE),
                    remoteMessage.getData().get(PARAMS.TAG_CREATED),
                    remoteMessage.getData().get(PARAMS.TAG_BOOKING_CODE),
                    remoteMessage.getData().get(PARAMS.TAG_TICKET_NUMBER),
                    (Utils.isEmptyString(remoteMessage.getData().get(PARAMS.TAG_ACTIVITY_TYPE_ID)) ? -1 : Integer.valueOf(remoteMessage.getData().get(PARAMS.TAG_ACTIVITY_TYPE_ID))));

            sendNotification();
        }*/
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
//    private void sendNotification() {
//
//        String title = getApplicationContext().getString(R.string.app_name);
//        Intent intent;
//        // In any of below case open Booking Detail screen
//        if (mPushData.getActivity_type_id() == Constants.ACTIVITY_TYPE_PROFESSIONAL_ASSIGNED
//                || mPushData.getActivity_type_id() == Constants.ACTIVITY_TYPE_BOOKING_COMPLETED
//                || mPushData.getActivity_type_id() == Constants.ACTIVITY_TYPE_CANCELLED_BY_STAFF
//                || mPushData.getActivity_type_id() == Constants.ACTIVITY_TYPE_CANCELLED_BY_ADMIN
//                || mPushData.getActivity_type_id() == Constants.ACTIVITY_TYPE_ACCEPT_BY_STAFF
//                || mPushData.getActivity_type_id() == Constants.ACTIVITY_TYPE_BOOKING_COMPLETED_BY_ADMIN) {
//
//            intent = new Intent(this, ViewBookingStatusActivity.class);
//            intent.putExtra(BookingDetailsActivity.EXTRA_BOOKING_ID, mPushData.getBooking_code());
//            intent.putExtra(ViewBookingStatusActivity.EXTRA_IS_FROM_PUSH, true);
//            intent.putExtra(ViewBookingStatusActivity.EXTRA_TICKET_NUMBER, mPushData.getTicket_number());
//        } else {
//            intent = new Intent(getApplicationContext(), NotificationsListActivity.class);
//            intent.putExtra(ViewBookingStatusActivity.EXTRA_IS_FROM_PUSH, true);
//        }
//
//        Intent intentPush = new Intent(Constants.ACTION_GCM_PUSH);
//        intentPush.putExtra(EXTRA_PUSH_DATA, mPushData);
//        sendOrderedBroadcast(intentPush, null);
//
//        PendingIntent contentIntent;
//
//        contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Set Notification builder.
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
//                .setContentTitle(title)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(mPushData.getMessage())).setContentText(mPushData.getMessage());
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBuilder.setSmallIcon(R.drawable.ic_transperent); // transperent
//        } else {
//            mBuilder.setSmallIcon(R.drawable.ic_launcher);
//        }
//        mBuilder.setContentIntent(contentIntent);
//        mBuilder.setAutoCancel(true);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        mBuilder.setSound(alarmSound);
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//        mBuilder.setVibrate(new long[]{1000, 1000});
//
//        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(111, mBuilder.build());
//
//        int mBadge = Integer.valueOf(prefManager.getString(PARAMS.BADGE_COUNT, "0"));
//        mBadge++;
//        if (mBadge > 0) {
//            prefManager.edit().putString(PARAMS.BADGE_COUNT, mBadge + "").apply();
//            ShortcutBadger.applyCount(getApplicationContext(), mBadge); //for 1.1.4
//        } else {
//            prefManager.edit().putString(PARAMS.BADGE_COUNT, mBadge + "").apply();
//            ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4
//        }
//    }

}
