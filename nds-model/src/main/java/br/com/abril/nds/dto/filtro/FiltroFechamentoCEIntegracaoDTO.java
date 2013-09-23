package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroFechamentoCEIntegracaoDTO implements Serializable {

	private static final long serialVersionUID = 4133061212097872582L;
	
	private Long idFornecedor;
	
	private Long idItemChamadaEncalheFornecedor;
	
	private Long idChamadaEncalheFornecedor;
	
	private String semana;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoFechamentoCEIntegracao ordenacaoColuna;
	
	private Intervalo<Date> periodoRecolhimento;
	
	public enum ColunaOrdenacaoFechamentoCEIntegracao {

		SEQUENCIAL("sequencial"),
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		REPARTE("reparte"),
		VENDA("venda"),
		PRECO_CAPA("precoCapa"),
		VALOR_VENDA("valorVenda"),
		TIPO("tipo"),
		ENCALHE("encalhe");

		private String nomeColuna;
		
		private ColunaOrdenacaoFechamentoCEIntegracao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoFechamentoCEIntegracao getPorDescricao(String descricao) {
			for(ColunaOrdenacaoFechamentoCEIntegracao coluna: ColunaOrdenacaoFechamentoCEIntegracao.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public Long getIdItemChamadaEncalheFornecedor() {
		return idItemChamadaEncalheFornecedor;
	}

	public void setIdItemChamadaEncalheFornecedor(Long idItemChamadaEncalheFornecedor) {
		this.idItemChamadaEncalheFornecedor = idItemChamadaEncalheFornecedor;
	}

	public Long getIdChamadaEncalheFornecedor() {
		return idChamadaEncalheFornecedor;
	}

	public void setIdChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor) {
		this.idChamadaEncalheFornecedor = idChamadaEncalheFornecedor;
	}

	public String getSemana() {
		return semana;
	}

	public void setSemana(String semana) {
		this.semana = semana;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoFechamentoCEIntegracao getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(
			ColunaOrdenacaoFechamentoCEIntegracao ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Intervalo<Date> getPeriodoRecolhimento() {
		return periodoRecolhimento;
	}

	public void setPeriodoRecolhimento(Intervalo<Date> periodoRecolhimento) {
		this.periodoRecolhimento = periodoRecolhimento;
	}
	
	public Integer getAnoReferente(){
		
		return SemanaUtil.getAno(semana);
	}
	
	public Integer getNumeroSemana(){
	
		return SemanaUtil.getSemana(semana);
	}

}
