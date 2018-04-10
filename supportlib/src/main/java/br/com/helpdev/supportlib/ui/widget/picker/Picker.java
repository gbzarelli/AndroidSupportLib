package br.com.helpdev.supportlib.ui.widget.picker;


import android.support.v4.app.FragmentManager;

/**
 * Created by User on 08/03/2016.
 */
public abstract class Picker extends android.support.v4.app.DialogFragment {

    protected int request;
    protected CallbackPicker callback;

    public void setCallback(CallbackPicker callback) {
        this.callback = callback;
    }

    public void show(FragmentManager manager, String tag, CallbackPicker callback, int request) {
        super.show(manager, tag);
        this.callback = callback;
        this.request = request;
    }
}
