package br.com.abril.nds.dto.integracao.efacil;

import java.io.Serializable;

public class CotaConsignadaHeader implements Serializable{


    private static final long serialVersionUID = 2001996088564982182L;

    private String codigoCota;
    private String codigoDistribuidor;
    private CotaConsignadaCorpo itens;


    public String getCodigoCota() {
        return codigoCota;
    }

    public void setCodigoCota(String codigoCota) {
        this.codigoCota = codigoCota;
    }

    public String getCodigoDistribuidor() {
        return codigoDistribuidor;
    }

    public void setCodigoDistribuidor(String codigoDistribuidor) {
        this.codigoDistribuidor = codigoDistribuidor;
    }

    public CotaConsignadaCorpo getItens() {
        return itens;
    }

    public void setItens(CotaConsignadaCorpo itens) {
        this.itens = itens;
    }
}
