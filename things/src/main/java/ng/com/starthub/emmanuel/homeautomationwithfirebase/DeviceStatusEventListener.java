package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Emmanuel on 05/11/2017.
 */

public class DeviceStatusEventListener implements ValueEventListener {

    private static final String TAG = DeviceStatusEventListener.class.getSimpleName();



    private final PinSettings pin;
    private  final TextView textView;

    DeviceStatusEventListener(PinSettings pin, TextView textView){
        this.pin = pin;
        this.textView = textView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Boolean value = (Boolean) dataSnapshot.getValue();
        Log.d(TAG, "Database Value: " + value);
        pin.turnOnOff(value == null ? false : value, textView);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "Error: Failed to read value", databaseError.toException());

    }

}
