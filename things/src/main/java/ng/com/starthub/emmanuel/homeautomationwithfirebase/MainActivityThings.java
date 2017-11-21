package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivityThings extends Activity {

    private static final String TAG = MainActivityThings.class.getSimpleName();

    private static final String DEVICE1_PIN = "GPIO_32";
    private static final String DEVICE2_PIN = "GPIO_33";
    private static final String DEVICE3_PIN = "GPIO_34";
    private static final String DEVICE4_PIN = "GPIO_37";
    private static final String PIR_MOTION_PIN = "GPIO_35";


    private PinSettings Device1;
    private PinSettings Device2;
    private PinSettings Device3;
    private PinSettings Device4;

    private PinSettings Monitor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_things);
        Log.d(TAG, "Oncreate... ");
        try {
            Log.d(TAG, "set output pin... ");
            Device1 = new PinSettings(DEVICE1_PIN , PinSettings.setState.OFF);
            Device2 = new PinSettings(DEVICE2_PIN , PinSettings.setState.OFF);
            Device3 = new PinSettings(DEVICE3_PIN , PinSettings.setState.OFF);
            Device4 = new PinSettings(DEVICE4_PIN , PinSettings.setState.OFF);

            Monitor1 = new PinSettings(PIR_MOTION_PIN, PinSettings.setState.IN);



        } catch (Exception e) {
            Log.e(TAG, "Error: Opening the pin " + e.getMessage(), e);
        }

        TextView textView1 = findViewById(R.id.device1_state);
        TextView textView2 = findViewById(R.id.device2_state);
        TextView textView3 = findViewById(R.id.device3_state);
        TextView textView4 = findViewById(R.id.device4_state);
        TextView textView5 = findViewById(R.id.monitor1);


        try {
            final FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();

            final DatabaseReference dbRef = fbDatabase.getReference("home_db");
            final DatabaseReference device1_Ref = dbRef.child("device1");
            final DatabaseReference device2_Ref = dbRef.child("device2");
            final DatabaseReference device3_Ref = dbRef.child("device3");
            final DatabaseReference device4_Ref = dbRef.child("device4");
            final DatabaseReference monitor_Ref = dbRef.child("monitor1");

            Log.d(TAG, "Listening to database... ");
            device1_Ref.addValueEventListener(new DeviceStatusEventListener(Device1, textView1));
            device2_Ref.addValueEventListener(new DeviceStatusEventListener(Device2, textView2));
            device3_Ref.addValueEventListener(new DeviceStatusEventListener(Device3, textView3));
            device4_Ref.addValueEventListener(new DeviceStatusEventListener(Device4, textView4));

            Monitor1.registerInput(monitor_Ref, textView5);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            Device1.close();
        } catch (Exception e) {
            Device1 = null;
        }

        try {
            Device2.close();
        } catch (Exception e) {
            Device2 = null;
        }

        try {
            Device3.close();
        } catch (Exception e) {
            Device3 = null;
        }

        try {
            Device4.close();
        } catch (Exception e) {
            Device4 = null;
        }

        if(Monitor1 != null) {
            try {
                Monitor1.unrgisterGpioCallback();
            } catch (Exception e) {
                Monitor1 = null;
            }

        }

    }
}
