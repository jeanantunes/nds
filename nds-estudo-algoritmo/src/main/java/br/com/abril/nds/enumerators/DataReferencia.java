package br.com.abril.nds.enumerators;

public enum DataReferencia {

    DEZEMBRO_15("--12-15"),	//inicio entrada veraneio
    FEVEREIRO_16("--02-16"),//limite base veraneio
    FEVEREIRO_28("--02-28"),//fim entrada veraneio
    FEVEREIRO_29("--02-29"),//fim entrada veraneio
    MARCO_01("--03-01"),	//inicio saida veraneio
    DEZEMBRO_14("--12-14");	//fim saida veraneio
    
    private String data;

    private DataReferencia(String data) {
    	this.data = data;
    }

    public String getData() {
        return data;
    }
    
}