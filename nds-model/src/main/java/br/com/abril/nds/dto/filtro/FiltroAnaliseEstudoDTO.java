package br.com.abril.nds.dto.filtro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.abril.nds.util.Constantes;


public class FiltroAnaliseEstudoDTO extends FiltroDTO {

	private static final long serialVersionUID = -7460175679601254408L;

	private Long numEstudo;
	private Long numeroEdicao;
	private Long idTipoClassificacaoProduto;
	private String codigoProduto;
	private String nome;
	private Date dataLancamento;
	
	private Long idProduto;
	
	private OrdemColuna ordemColuna;
	
	public enum OrdemColuna{
		
		ESTUDO("numeroEstudo"),
		CODIGO("codigoProduto"),
		PRODUTO("nomeProduto"),
		EDICAO("numeroEdicaoProduto"),
		CLASSIFICACAO("descicaoTpClassifProd"),
		DT_NASCIMENTO("dataLancamento"),
		PERIODO("codPeriodoProd"),		
		STATUS("statusEstudo");
		
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
	
	public Long getNumEstudo() {
		return numEstudo;
	}

	public void setNumEstudo(Long numEstudo) {
		this.numEstudo = numEstudo;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Long getIdTipoClassificacaoProduto() {
		return idTipoClassificacaoProduto;
	}

	public void setIdTipoClassificacaoProduto(Long idTipoClassificacaoProduto) {
		this.idTipoClassificacaoProduto = idTipoClassificacaoProduto;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) throws ParseException {
		
		if (dataLancamento != null){
			this.dataLancamento = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(dataLancamento);
		}else{
			this.dataLancamento = null;
		}
		
	}

	public OrdemColuna getOrdemColuna() {
		return ordemColuna;
	}

	public void setOrdemColuna(OrdemColuna ordemColuna) {
		this.ordemColuna = ordemColuna;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
}
