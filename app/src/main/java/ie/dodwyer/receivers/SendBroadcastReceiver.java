package ie.dodwyer.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by User on 4/17/2017.
 */

public class SendBroadcastReceiver extends BroadcastReceiver {
        private static final String ACTION_SMS_SENT = "SMS_SENT";
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals(ACTION_SMS_SENT)) {

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Bundle b = intent.getExtras();
                    if (b != null) {
                        String contact = b.getString("contact");

                        //activity.app.dbManager.updateChallenge()
                        Toast.makeText(context, "The message has been sent to '"+contact+"'.",
                                Toast.LENGTH_SHORT).show();
                    }

                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "The message has not been sent due to 'generic failure' error. The challenge cannot be counted.", Toast.LENGTH_LONG)
                            .show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "The message has not been sent due to 'no service' error. The challenge cannot be counted.", Toast.LENGTH_LONG)
                            .show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "The message has not been sent due to 'null PDU' error. The challenge cannot be counted.", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "The message has not been sent due to 'radio off' error. The challenge cannot be counted.", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }

}


