package br.com.abril.nds.enumerators;

public enum DataReferencia {

    DEZEMBRO_20("--12-20"),	//inicio entrada veraneio
    FEVEREIRO_28("--02-28"),//fim entrada veraneio
    MARCO_01("--03-01"),	//inicio saida veraneio
    DEZEMBRO_19("--12-19");	//fim saida veraneio
    
    private String data;

    private DataReferencia(String data) {
    	this.data = data;
    }

    public String getData() {
        return data;
    }
    
}