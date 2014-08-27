package br.com.abril.nds.dto.filtro;

public class FiltroEdicaoBaseDistribuicaoVendaMedia extends FiltroDTO {

	private static final long serialVersionUID = -4420735338558281305L;
	
	private String codigo; 
	private String nome; 
	private Long edicao;
	private Long classificacao;
	private Long idLancamento;
	
	private boolean consolidado;
	
	public FiltroEdicaoBaseDistribuicaoVendaMedia(String codigo, String nome,
			Long edicao, Long classificacao, Long idLancamento) {
		this.codigo = codigo;
		this.nome = nome;
		this.edicao = edicao;
		this.classificacao = classificacao;
		this.idLancamento = idLancamento;
	}
	private OrdemColuna ordemColuna;
	
	public enum OrdemColuna{
		
		CODIGO("codigoProduto"),
		EDICAO("numeroEdicao"),
		PERIODO("periodo"),
		DATA_LANCAMENTO("dataLancamentoFormatada"),
		REPARTE("reparte"),
		VENDA("venda"),
		STATUS("status"),
		CLASSIFICACAO("classificacao");		
		
		private String descricao;
		
		private OrdemColuna(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		@Override
		public String toString() {
			
			return this.descricao;
		}
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public Long getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(String classificacao) {
		if(!classificacao.equalsIgnoreCase("selecione...") || !classificacao.equalsIgnoreCase("selecione")){
			this.classificacao = new Long(classificacao);
		}else{
			this.classificacao = null;
		}
	}
    public OrdemColuna getOrdemColuna() {
		return ordemColuna;
	}
	public void setOrdemColuna(OrdemColuna ordemColuna) {
		this.ordemColuna = ordemColuna;
	}
	public Long getIdLancamento() {
		return idLancamento;
	}
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}
    public boolean isConsolidado() {
        return consolidado;
    }
    public void setConsolidado(boolean consolidado) {
        this.consolidado = consolidado;
    }
}
