package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;

/**
 * Created by Emmanuel on 05/11/2017.
 */

public class PinSettings implements AutoCloseable {

    private static final String TAG = PinSettings.class.getSimpleName();

    private static final String OFF = "OFF";
    private static final String ON = "ON";

    private static final String INTRUDER = "Intruder detected";
    private static final String NO_INTRUDER = "No intruder";

    private DatabaseReference Ref;
    private TextView textView;

    private Gpio pin;

    //Set the pin direction input or output
    public enum setState {
        ON(Gpio.DIRECTION_OUT_INITIALLY_HIGH), OFF(Gpio.DIRECTION_OUT_INITIALLY_LOW), IN(Gpio.DIRECTION_IN);

        final int state;

        setState(int state) {
            this.state = state;
        }

        public int getState() {
            return this.state;
        }

    }


    public PinSettings(String pinName, setState set) {
        PeripheralManagerService service = new PeripheralManagerService();

        try {
            Gpio pin = service.openGpio(pinName);
            this.pin = pin;
            this.pin.setDirection(set.state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerInput(final DatabaseReference Ref, final TextView textView) {
        this.Ref = Ref;
        this.textView = textView;
        try {
            //Detect both when the button goes down and when it goes up.
            pin.setEdgeTriggerType(Gpio.EDGE_BOTH);
            //register a callback
            this.pin.registerGpioCallback(monitorCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GpioCallback monitorCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                Ref.setValue(gpio.getValue());
                boolean value = getState();
                //Show the message on textview
                if (value) {
                    textView.setText(INTRUDER);
                } else {
                    textView.setText(NO_INTRUDER);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    };


    public boolean getState() {
        boolean value = false;
        try {
            value = pin.getValue();
        } catch (IOException e) {
            Log.d(TAG, "Error in reading pin value" + e.getMessage(), e);
        }

        return value;
    }


    public void turnOnOff(boolean turn, TextView textView) {
        try {
            pin.setValue(turn);
            boolean value = getState();
            if (value) {
                textView.setText(ON);
            } else {
                textView.setText(OFF);
            }
        } catch (IOException e) {
            Log.d(TAG, "Error in setting pin value" + e.getMessage(), e);
        }
    }


    @Override
    public void close() throws Exception {
        if (pin != null) {
            try {
                pin.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pin = null;
            }

        }
    }

    //unregister a callback
    public void unrgisterGpioCallback() {
        if (pin != null) {
            pin.unregisterGpioCallback(monitorCallback);
            try {
                pin.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pin = null;
            }
        }
    }

}
