package br.edu.ifspsaocarlos.sdm.mensagem.model;

import java.util.Date;

/**
 * Created by Abner - Manutençao on 29/06/2015.
 */
public class Mensagem {
    String id;
    String origem;
    String destino;
    String assunto;
    String corpo;
    String origem_nome;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getOrigem_nome() { return origem_nome; }

    public void setOrigem_nome(String origem_nome) { this.origem_nome = origem_nome; }
}
