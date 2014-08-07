package br.com.abril.nds.enumerators;

public enum DataReferencia {

    DEZEMBRO_20("--12-20"),	//inicio entrada veraneio
    FEVEREIRO_15("--02-15"),	//fim entrada veraneio
    FEVEREIRO_16("--02-16"),	//inicio saida veraneio
    FEVEREIRO_29("--02-29"),	//inicio saida veraneio
    DEZEMBRO_19("--12-19");	//fim saida veraneio
    
    private String data;

    private DataReferencia(String data) {
	this.data = data;
    }

    public String getData() {
        return data;
    }
    
}
