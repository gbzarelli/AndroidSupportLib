package br.com.helpdev.supportlib.telephony;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.RequiresPermission;

import java.util.List;
import br.com.helpdev.supportlib.R;

/**
 * Created by Guilherme Biff Zarelli on 08/07/15.
 */
public class Telephone implements DialogInterface.OnClickListener {

    private List<ObTelephone> obTelephones;
    private int indice;
    private Activity activity;

    public Telephone(List<ObTelephone> obTelephones) {
        this.obTelephones = obTelephones;
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public AlertDialog callAlert(Activity activity, int indice) {
        return callAlert(activity, indice, this);
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public AlertDialog callAlert(Activity activity, int indice, DialogInterface.OnClickListener onClickPositiveButtom) {
        this.activity = activity;
        if (indice > obTelephones.size() - 1) {
            throw new ArrayIndexOutOfBoundsException("indice > que quantidade de telefones");
        }
        this.indice = indice;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(activity.getString(R.string.app_name));
        builder.setMessage(obTelephones.get(indice).getMensagemParaDiscagem());
        builder.setPositiveButton(R.string.bt_continuar, onClickPositiveButtom);
        builder.setNegativeButton(R.string.bt_cancelar, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

    @SuppressLint("MissingPermission")
    public void onClick(DialogInterface di, int i) {
        try {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                call(activity, obTelephones.get(indice).getTelefone());
            } else {
                di.cancel();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static void call(Activity activity, String phone) {
        activity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
    }
}