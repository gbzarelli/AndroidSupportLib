package br.com.helpdev.supportlib.sistema.telefonia;

/**
 * Created by demantoide on 08/07/15.
 */
public class ObTelefone {
    public ObTelefone(String mensagemParaDiscagem, String telefone) {
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
