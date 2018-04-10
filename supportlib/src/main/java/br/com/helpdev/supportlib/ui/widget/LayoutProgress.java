package br.com.helpdev.supportlib.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;

import br.com.helpdev.supportlib.R;


/**
 * <pre>
 * class MainActivity : Activity() {
 *
 * private var myLayoutProgress: LayoutProgress? = null
 *
 * override fun onCreate(savedInstanceState: Bundle?) {
 * super.onCreate(savedInstanceState)
 * setContentView(R.layout.activity_main)
 * myLayoutProgress = LayoutProgress(this, R.id.container)
 *
 * if (null == savedInstanceState) {
 * myLayoutProgress!!.showProgress("Aguarde....")
 * myLayoutProgress!!.showContentDelay(5_000)
 * }
 * }
 *
 * override fun onSaveInstanceState(outState: Bundle?) {
 * super.onSaveInstanceState(outState)
 * myLayoutProgress?.onSaveInstanceState(outState)
 * }
 *
 * override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
 * super.onRestoreInstanceState(savedInstanceState)
 * myLayoutProgress?.onRestoreInstanceState(savedInstanceState)
 * }
 * }
 * </pre>
 * Created by Guilherme Biff Zarelli on 19/01/15.
 */
public class LayoutProgress {

    private View viewProgress;
    private Button bt1, bt2;
    private TextView tv1;
    private ProgressBar progress;
    private View content;
    //*
    private volatile boolean interrupt;
    private volatile long delayToShowContent;

    public LayoutProgress(View fragmentView, int idContent) {
        this.viewProgress = fragmentView.findViewById(R.id.layout_progress);
        this.content = fragmentView.findViewById(idContent);
        load();
    }

    public LayoutProgress(Activity activity, int idContent) {
        this.viewProgress = activity.findViewById(R.id.layout_progress);
        this.content = activity.findViewById(idContent);
        load();
    }

    private void load() {
        bt1 = viewProgress.findViewById(R.id.progress_bt_1);
        bt2 = viewProgress.findViewById(R.id.progress_bt_2);
        tv1 = viewProgress.findViewById(R.id.progress_tv_status);
        progress = viewProgress.findViewById(R.id.progress_bar);
        interrupt = false;
        delayToShowContent = -1;
    }

    public void showProgress(String text) {
        content.setVisibility(View.GONE);
        bt1.setVisibility(View.GONE);
        bt2.setVisibility(View.GONE);
        viewProgress.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);

        setTextProgress(text);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("content", content.getVisibility() == View.VISIBLE);
        outState.putBoolean("progress", progress.getVisibility() == View.VISIBLE);

        outState.putBoolean("bt1", bt1.getVisibility() == View.VISIBLE);
        outState.putString("bt1_string", bt1.getText().toString());
        outState.putSerializable("bt1_tag", (Serializable) bt1.getTag());

        outState.putBoolean("bt2", bt2.getVisibility() == View.VISIBLE);
        outState.putString("bt2_string", bt2.getText().toString());
        outState.putSerializable("bt2_tag", (Serializable) bt2.getTag());

        outState.putString("tv1", tv1.getText().toString());

        interrupt = true;
        outState.putLong("delayToShowContent", delayToShowContent);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        onRestoreInstanceState(bundle, null, null);
    }

    public void onRestoreInstanceState(Bundle bundle, View.OnClickListener onClickBt1, View.OnClickListener onClickBt2) {
        if (bundle.getBoolean("content", false)) {
            showContent();
        } else {
            showProgress(bundle.getString("tv1"));
            enableButtons(bundle.getBoolean("bt1", false), bundle.getBoolean("bt2", false));
            enableProgress(bundle.getBoolean("progress", false));
        }
        setBt1(bundle.getString("bt1_string"), bundle.getSerializable("bt1_tag"), onClickBt1);
        setBt2(bundle.getString("bt2_string"), bundle.getSerializable("bt2_tag"), onClickBt2);
        interrupt = false;
        delayToShowContent = bundle.getLong("delayToShowContent", -1);
        if (delayToShowContent > -1) {
            showContentDelay(delayToShowContent);
        }
    }

    public void setTextProgress(String text) {
        tv1.setText(text);
    }


    public void showContentDelay(final long delay) {
        new Thread("showContentDelay") {
            @Override
            public void run() {
                try {
                    long sleep = 0;
                    do {
                        delayToShowContent = delay - sleep;
                        Thread.sleep(100);
                        sleep += 100;
                    } while (sleep < delay && !interrupt);
                    if (interrupt) {
                        return;
                    }
                    delayToShowContent = -1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showContent();
                    }
                });
            }
        }.start();
    }

    public void showContent() {
        viewProgress.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        enableButtons(false, false);
    }

    public void stopProgress(String text, boolean bt1Enable, boolean bt2Enable) {
        progress.setVisibility(View.GONE);
        enableButtons(bt1Enable, bt2Enable);
        setTextProgress(text);
    }

    public void enableProgress(boolean enable) {
        progress.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    public void enableButtons(boolean bt1Enable, boolean bt2Enable) {
        if (bt1Enable) {
            bt1.setVisibility(View.VISIBLE);
        } else {
            bt1.setVisibility(View.GONE);
        }
        if (bt2Enable) {
            bt2.setVisibility(View.VISIBLE);
        } else {
            bt2.setVisibility(View.GONE);
        }
    }

    public boolean isProgressVisible() {
        return viewProgress.getVisibility() == View.VISIBLE;
    }

    public void setBt1(Context context, int stringId, View.OnClickListener onClickListener) {
        setBt1(context, stringId, null, onClickListener);
    }

    public void setBt1(Context context, int stringId, Serializable tag, View.OnClickListener onClickListener) {
        this.setBt1(context.getString(stringId), tag, onClickListener);
    }

    /**
     * R.id.progress_bt_1
     *
     * @param text
     * @param onClickListener
     */
    public void setBt1(String text, Serializable tag, View.OnClickListener onClickListener) {
        bt1.setText(text);
        bt1.setTag(tag);
        bt1.setOnClickListener(onClickListener);
    }

    public void setBt2(Context context, int stringId, View.OnClickListener onClickListener) {
        setBt2(context, stringId, null, onClickListener);
    }

    public void setBt2(Context context, int stringId, Serializable tag, View.OnClickListener onClickListener) {
        this.setBt2(context.getString(stringId), tag, onClickListener);
    }

    /**
     * R.id.progress_bt_2
     *
     * @param text
     * @param onClickListener
     */
    public void setBt2(String text, Serializable tag, View.OnClickListener onClickListener) {
        bt2.setText(text);
        bt2.setTag(tag);
        bt2.setOnClickListener(onClickListener);
    }

    public void setBt1(String text, View.OnClickListener onClickListener) {
        setBt1(text, null, onClickListener);
    }

    public void setBt2(String text, View.OnClickListener onClickListener) {
        setBt2(text, null, onClickListener);
    }


    public boolean isBt1Visible() {
        return bt1.getVisibility() == View.VISIBLE;
    }

}

