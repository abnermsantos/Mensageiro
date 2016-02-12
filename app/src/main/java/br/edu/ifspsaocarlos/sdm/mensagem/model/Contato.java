package br.edu.ifspsaocarlos.sdm.mensagem.model;

/**
 * Created by Abner - Manutençao on 25/06/2015.
 */
public class Contato {
    private String id;
    private String nomeCompleto;
    private String apelido;

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getApelido() { return apelido; }

    public void setApelido(String apelido) { this.apelido = apelido; }
}
