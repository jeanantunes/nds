package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroEdicoesFechadasDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -990334749020240008L;

	private Date dataDe;
	
	private Date dataAte;
	
	private Long codigoFornecedor;
	
	private PaginacaoVO paginacao;
	
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		CODIGO_PRODUTO("codigoProduto"),
		NOME_PRODUTO("nomeProduto"),
		EDICAO_PRODUTO("edicaoProduto"),
		NOME_FORNECEDOR("nomeFornecedor"),
		DATA_LANCAMENTO("dataLancamento"),
		DATA_RECOLHIMENTO("dataRecolhimento"),
		PARCIAL("parcial"),
		SALDO("saldo");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}
	
	public FiltroEdicoesFechadasDTO() {
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public OrdenacaoColunaConsulta getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaConsulta ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public Long getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(Long codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

}
