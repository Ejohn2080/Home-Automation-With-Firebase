package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.util.Log;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Emmanuel on 06/11/2017.
 */

public class SwitchChangeEventListener implements ValueEventListener {

    private final static String TAG = SwitchChangeEventListener.class.getSimpleName();

    private  final Switch aSwitch;

    SwitchChangeEventListener(Switch aSwitch){
        this.aSwitch = aSwitch;
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Boolean status = (Boolean) dataSnapshot.getValue();
        Log.d(TAG, "DATABASE Reference status: " + status);
        aSwitch.setChecked(status == null ? false : status);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
