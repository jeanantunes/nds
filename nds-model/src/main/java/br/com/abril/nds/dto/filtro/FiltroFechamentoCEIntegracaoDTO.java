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
	
	private Integer codigoDistribuidorFornecdor;
	
	private Long idItemChamadaEncalheFornecedor;
	
	private Long idChamadaEncalhe;
	
	public Long getIdChamadaEncalhe() {
		return idChamadaEncalhe;
	}

	public void setIdChamadaEncalhe(Long idChamadaEncalhe) {
		this.idChamadaEncalhe = idChamadaEncalhe;
	}

	private Long idChamadaEncalheFornecedor;
	
	private String semana;
	
	private String comboCeIntegracao;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoFechamentoCEIntegracao ordenacaoColuna;
	
	private Intervalo<Date> periodoRecolhimento;
	
	public enum ColunaOrdenacaoFechamentoCEIntegracao {

		SEQUENCIAL("sequencial"),
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		NUMERO_EDICAO("numeroEdicao"),
		DEVFORN("qtdeDevSemCE"),
		REPARTE("reparte"),
		VENDA("venda"),
		PRECO_CAPA("precoCapa"),
		VALOR_VENDA("valorVenda"),
		TIPO("tipoFormatado"),
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
	
	public Integer getCodigoDistribuidorFornecdor() {
		return codigoDistribuidorFornecdor;
	}

	public void setCodigoDistribuidorFornecdor(Integer codigoDistribuidorFornecdor) {
		this.codigoDistribuidorFornecdor = codigoDistribuidorFornecdor;
	}
	
	public String getComboCeIntegracao() {
		return comboCeIntegracao;
	}

	public void setComboCeIntegracao(String comboCeIntegracao) {
		this.comboCeIntegracao = comboCeIntegracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idFornecedor == null) ? 0 : idFornecedor.hashCode());
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
		if (idFornecedor == null) {
			if (other.idFornecedor != null)
				return false;
		} else if (!idFornecedor.equals(other.idFornecedor))
			return false;
		if (semana == null) {
			if (other.semana != null)
				return false;
		} else if (!semana.equals(other.semana))
			return false;
		return true;
	}
}