package br.com.abril.nds.dto;

import java.io.Serializable;

public class EfacilDTO implements Serializable{


    private static final long serialVersionUID = 1775344933445921109L;


    private String dados;
    private String privateKey;
    private String token;
    private String metodo;
    private String URL;


    public String getDados() {
        return dados;
    }

    public void setDados(String dados) {
        this.dados = dados;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
