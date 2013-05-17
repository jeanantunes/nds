package br.com.abril.nds.model.estudo;


public enum ClassificacaoCota {
	
	/**
	 * AJ
	 */
	Ajuste("AJ", "Ajuste"),
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
	CotaNaoRecebeSegmento("SN", "Cota não recebe segmento"),
	/**
	 * PR
	 */
	BancaSoComEdicaoBaseAberta("PR", "Banca que só tem edição base aberta"),
	/**
	 * MM
	 */
	MaximoMinimo("MM", "Mínimo/ Máximo"),
	/**
	 * IN
	 */
	InclusaoManualCotas("IN", "Inclusão manual de cotas"),
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
	BancaQueRecebemDeterminadoSegmento("GN", "Cota não recebe esse Segmento"),
	/**
	 * S
	 */
	CotaNova("S", "Cota Nova"),
	/**
	 * SM
	 */
	BancaMixSemDeterminadaPublicacao("SM", "Publicação não está no MIX da cota"),
	/**
	 * SS
	 */
	BancaSuspensa("SS", "Cota Suspensa"),
	/**
	 * FR
	 */
	BancaForaDaRegiaoDistribuicao("FR", "Região não recebe publicação"),
	/**
	 * SH
	 */
	BancaSemHistorico("SH", "Não recebeu as edições-base"),
	/**
	 * VZ
	 */
	BancaComVendaZero("VZ", "Sem vendas nas edições-bases"),
	/**
	 * MX
	 */
	CotaMix("MX", "Cota de Mix"),
	/**
	 * CP
	 */
	BancaEstudoComplementar("CP", "Bancas de Estudo Complementar"),
	/**
	 * TR
	 */
	BonificacaoParaCotas("TR", "Bonificação para Cotas"),
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
