package br.com.abril.nds.model.estudo;


public enum ClassificacaoCota {
	
	/**
	 * AJ
	 */
	Ajuste("AJ"),
	/**
	 * FX
	 */
	ReparteFixado("FX"),
	/**
	 * RD
	 */
	RedutorAutomatico("RD"),
	/**
	 * PR
	 */
	BancaSoComEdicaoBaseAberta("PR"),
	/**
	 * MM
	 */
	MaximoMinimo("MM"),
	/**
	 * IN
	 */
	InclusaoManualCotas("IN"),
	/**
	 * EG
	 */
	EnglobaDesengloba("EG"),
	/**
	 * CL
	 */
	BancaSemClassificacaoDaPublicacao("CL"),
	/**
	 * GN
	 */
	BancaQueRecebemDeterminadoSegmento("GN"),
	/**
	 * S
	 */
	CotaNova("S"),
	/**
	 * SM
	 */
	BancaMixSemDeterminadaPublicacao("SM"),
	/**
	 * SS
	 */
	BancaSuspensa("SS"),
	/**
	 * FR
	 */
	BancaForaDaRegiaoDistribuicao("FR"),
	/**
	 * SH
	 */
	BancaSemHistorico("SH"),
	/**
	 * VZ
	 */
	BancaComVendaZero("VZ"),
	/**
	 * MX
	 */
	CotaMix("MX"),
	/**
	 * CP
	 */
	BancaEstudoComplementar("CP"),
	/**
	 * TR
	 */
	BonificacaoParaCotas("TR"),
	/**
	 * ZZ
	 */
	SemClassificacao("");
	
	private String codigo;
	
	ClassificacaoCota(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public boolean notIn(ClassificacaoCota ... lista) {
	    for (int i = 0; i < lista.length; i++) {
		if (this.equals(lista[i])) {
		    return false;
		}
	    }
	    return true;
	}
	
	public boolean in(ClassificacaoCota ... lista) {
	    for (int i = 0; i < lista.length; i++) {
		if (this.equals(lista[i])) {
		    return true;
		}
	    }
	    return false;
	}
}
