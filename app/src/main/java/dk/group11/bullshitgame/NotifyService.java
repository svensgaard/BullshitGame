package dk.group11.bullshitgame;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotifyService extends IntentService {
    public static final int NOTIFICATION_ID = 5453;
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_COUNT = "Services.action.COUNT";

    private static final String EXTRA_TIME = "Services.extra.TIME";

    public NotifyService() {
        super("NotifyService");
    }

    /**
     * Starts this service to perform action Count with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionCount(Context context, int time) {
        Intent intent = new Intent(context, NotifyService.class);
        intent.setAction(ACTION_COUNT);
        intent.putExtra(EXTRA_TIME, time);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_COUNT.equals(action)) {
                handleActionCount(intent);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCount(Intent intent) {
        int time = intent.getIntExtra(EXTRA_TIME, 10);
        synchronized (this) {
            try {
                wait(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String text = "You have not played in " + time +" milliseconds!";
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.die1)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentText(text)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
