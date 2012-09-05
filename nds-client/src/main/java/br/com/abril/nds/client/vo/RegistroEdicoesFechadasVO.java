package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de edições fechadas.
 * @author InfoA2
 */
@Exportable
public class RegistroEdicoesFechadasVO implements Serializable {

	private static final long serialVersionUID = -2136529530131643372L;

	@Export(label = "Código", exhibitionOrder = 1)
	private String codigoProduto;

	@Export(label = "Produto", exhibitionOrder = 2)
	private String nomeProduto;

	@Export(label = "Edição", exhibitionOrder = 3)
	private Long edicaoProduto;

	@Export(label = "Fornecedor", exhibitionOrder = 4)
	private String nomeFornecedor;

	@Export(label = "Lançamento", exhibitionOrder = 5)
	private Date dataLancamento;

	@Export(label = "Recolhimento", exhibitionOrder = 6)
	private Date dataRecolhimento;

	private boolean parcial;

	@Export(label = "Saldo", exhibitionOrder = 8)
	private BigInteger saldo;

	public RegistroEdicoesFechadasVO(String codigoProduto, String nomeProduto,
			Long edicaoProduto, String nomeFornecedor, Date dataLancamento,
			boolean parcial, Date dataRecolhimento, BigInteger saldo) {
		this.codigoProduto=codigoProduto;
		this.nomeProduto=nomeProduto;
		this.edicaoProduto=edicaoProduto;
		this.nomeFornecedor=nomeFornecedor;
		this.dataLancamento=dataLancamento;
		this.parcial=parcial;
		this.dataRecolhimento=dataRecolhimento;
		this.saldo=saldo;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public boolean isParcial() {
		return parcial;
	}

	public void setParcial(boolean parcial) {
		this.parcial = parcial;
	}
	
	public BigInteger getSaldo() {
		return saldo;
	}

	public void setSaldo(BigInteger saldo) {
		this.saldo = saldo;
	}

	@Export(label = "Parcial", exhibitionOrder = 7)
	public String getParcialString() {
		return (this.parcial ? "S" : "N"); 
	}
	
}
