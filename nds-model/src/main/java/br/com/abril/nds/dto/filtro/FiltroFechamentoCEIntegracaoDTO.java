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
		ENCALHE("encalhe"),
		DIFERENCA("diferenca"),
		ESTOQUE("estoque");

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idChamadaEncalheFornecedor == null) ? 0
						: idChamadaEncalheFornecedor.hashCode());
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
		result = prime
				* result
				+ ((idItemChamadaEncalheFornecedor == null) ? 0
						: idItemChamadaEncalheFornecedor.hashCode());
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime
				* result
				+ ((periodoRecolhimento == null) ? 0 : periodoRecolhimento
						.hashCode());
		result = prime * result + ((semana == null) ? 0 : semana.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroFechamentoCEIntegracaoDTO other = (FiltroFechamentoCEIntegracaoDTO) obj;
		if (idChamadaEncalheFornecedor == null) {
			if (other.idChamadaEncalheFornecedor != null)
				return false;
		} else if (!idChamadaEncalheFornecedor
				.equals(other.idChamadaEncalheFornecedor))
			return false;
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (idItemChamadaEncalheFornecedor == null) {
			if (other.idItemChamadaEncalheFornecedor != null)
				return false;
		} else if (!idItemChamadaEncalheFornecedor
				.equals(other.idItemChamadaEncalheFornecedor))
			return false;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (periodoRecolhimento == null) {
			if (other.periodoRecolhimento != null)
				return false;
		} else if (!periodoRecolhimento.equals(other.periodoRecolhimento))
			return false;
		if (semana == null) {
			if (other.semana != null)
				return false;
		} else if (!semana.equals(other.semana))
			return false;
		return true;
	}
	
	

}
