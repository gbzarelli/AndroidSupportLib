package br.com.helpdev.supportlib.sistema.telefonia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Guilherme Biff Zarelli on 08/07/15.
 */
public class Telefone implements DialogInterface.OnClickListener {

    public static final String TAG = "grupocriar.eve.Telefone";

    private Activity atividade;
    private ArrayList<ObTelefone> obTelefones;
    private int indice;

    public Telefone(Activity atividade, ArrayList<ObTelefone> obTelefones) {
        this.atividade = atividade;
        this.obTelefones = obTelefones;
    }

    public void discar(int indice) {
        if (indice > obTelefones.size() - 1) {
            throw new ArrayIndexOutOfBoundsException("indice > que quantidade de telefones");
        }
        this.indice = indice;
        AlertDialog.Builder alerta = new AlertDialog.Builder(atividade);
        alerta.setCancelable(false);
        alerta.setTitle("Atenção");
        alerta.setMessage(obTelefones.get(indice).getMensagemParaDiscagem());
        alerta.setPositiveButton("Confirmar", this);
        alerta.setNegativeButton("Cancelar", this);

        alerta.show();
    }

    public void onClick(DialogInterface di, int i) {
        if (i == DialogInterface.BUTTON_POSITIVE) {
            atividade.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + obTelefones.get(indice).getTelefone())));
        } else {
            di.cancel();
        }
    }
}