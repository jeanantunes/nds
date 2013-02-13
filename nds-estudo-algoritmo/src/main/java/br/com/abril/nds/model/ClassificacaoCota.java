package br.com.abril.nds.model;

public enum ClassificacaoCota {
	
	Ajuste("AJ"),
	ReparteFixado("FX"),
	RedutorAutomatico("RD"),
	BancaSoComEdicaoBaseAberta("PR"),
	MaximoMinimo("MM"),
	InclusaoManualCotas("IN"),
	EnglobaDesengloba("EG"),
	BancaSemClassificacaoDaPublicacao("CL"),
	BancaQueRecebemDeterminadoSegmento("GN"),
	BancaMixSemDeterminadaPublicacao("SM"),
	BancaSuspensa("SS"),
	BancaForaDaRegiaoDistribuicao("FR"),
	BancaComReparteZeroMinimoZeroCotaAntiga("SH"),
	BancaComTotalVendaZeraMinimoZeroCotaAntiga("VZ"),
	CotaMix("MX"),
	BancaEstudoComplementar("CP"),
	BonificacaoParaCotas("TR"),
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
