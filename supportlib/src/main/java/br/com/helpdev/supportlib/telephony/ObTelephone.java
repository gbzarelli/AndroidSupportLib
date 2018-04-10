package br.com.helpdev.supportlib.telephony;

/**
 * Created by Guilherme Biff Zarelli on 08/07/15.
 */
public class ObTelephone {
    public ObTelephone(String mensagemParaDiscagem, String telefone) {
        this.mensagemParaDiscagem = mensagemParaDiscagem;
        this.telefone = telefone;
    }

    private String mensagemParaDiscagem;
    private String telefone;

    public String getMensagemParaDiscagem() {
        return mensagemParaDiscagem;
    }

    public void setMensagemParaDiscagem(String mensagemParaDiscagem) {
        this.mensagemParaDiscagem = mensagemParaDiscagem;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
