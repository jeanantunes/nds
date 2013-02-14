package br.com.abril.nds.model;

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
	BancaComReparteZeroMinimoZeroCotaAntiga("SH"),
	/**
	 * VZ
	 */
	BancaComTotalVendaZeraMinimoZeroCotaAntiga("VZ"),
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
	SemClassificacao("ZZ");
	// TODO: verificar qual será a primeira classificação que a cota receberá
	// enquanto isso não é definido, utilizaremos a classificação 'ZZ' - Sem Classificação
	
	private String codigo;
	
	ClassificacaoCota(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
}
