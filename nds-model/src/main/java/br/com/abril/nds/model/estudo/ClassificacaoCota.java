package br.com.abril.nds.model.estudo;


public enum ClassificacaoCota {
	
	/**
	 * AJ
	 */
	Ajuste("AJ", "Cota com ajuste de reparte"),
	/**
	 * FX
	 */
	ReparteFixado("FX", "Reparte Fixado"),
	/**
	 * RD
	 */
	RedutorAutomatico("RD", "Redutor Automático"),
	/**
	 * 
	 */
	CotaExcecaoSegmento("SN", "Cota exceçao de Segmento"),
	/**
	 * PR
	 */
	BancaSoComEdicaoBaseAberta("PR", "Cota que só tem edição base aberta"),
	/**
	 * MM
	 */
	MaximoMinimo("MM", "Mínimo/ Máximo"),
	/**
	 * IN
	 */
	InclusaoManualCotas("IN", "Inclusão manual de cota"),
	/**
	 * EG
	 */
	EnglobaDesengloba("EG", "Engloba/Desengloba"),
	/**
	 * CL
	 */
	BancaSemClassificacaoDaPublicacao("CL", "Cota não recebe essa Classificação"),
	/**
	 * GN
	 */
	CotaNaoRecebeEsseSegmento("GN", "Cota não recebe esse Segmento"),
	/**
	 * S
	 */
	CotaNova("S", "Cota Nova"),
	/**
	 * SM
	 */
	BancaMixSemDeterminadaPublicacao("SM", "Cota alternativa sem Mix cadastrado"),
	/**
	 * SS
	 */
	BancaSuspensa("SS", "Cota Suspensa"),
	/**
	 * FR
	 */
	BancaForaDaRegiaoDistribuicao("FR", "Cota fora da região que recebe a publicação"),
	/**
	 * SH
	 */
	BancaSemHistorico("SH", "Cota sem histórico"),
	/**
	 * VZ
	 */
	BancaComVendaZero("VZ", "Cota com venda zero"),
	/**
	 * MX
	 */
	CotaMix("MX", "Cota de Mix"),
	
	/**
	 * MX - Desconsiderando Min e Max
	 */
	CotaMixSemMinMax("MS", "Cota de Mix sem Min e Max"),
	
	/**
	 * CP
	 */
	BancaEstudoComplementar("CP", "Cota do Estudo Complementar"),
	/**
	 * TR
	 */
	BonificacaoParaCotas("TR", "Cota com bonificação"),
    /**
     * FN
     */
    CotaNaoRecebeDesseFornecedor("FN", "Cota não recebe o Fornecedor"),
	/**
	 * ZZ
	 */
	SemClassificacao("", "");
	
	private String codigo;
	private String texto;
	
	ClassificacaoCota(String codigo, String texto) {
		this.codigo = codigo;
		this.texto = texto;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getTexto() {
	    return texto;
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
