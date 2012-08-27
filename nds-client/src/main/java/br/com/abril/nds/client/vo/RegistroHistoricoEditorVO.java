package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de históricos de editor.
 * @author InfoA2
 */
@Exportable
public class RegistroHistoricoEditorVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7475719189534371260L;

	private String nomeEditor;
	
	@Export(label = "Código", exhibitionOrder = 1)
	private String codigoProduto;

	@Export(label = "Produto", exhibitionOrder = 2)
	private String nomeProduto;

	@Export(label = "EdicaoProduto", exhibitionOrder = 3)
	private Long edicaoProduto;

	private BigDecimal reparte;

	private BigInteger vendaExemplares;

	private BigDecimal porcentagemVenda;

	@Export(label = "Reparte", exhibitionOrder = 4)
	private String reparteFormatado;
	
	@Export(label = "Venda Exs.", exhibitionOrder = 5)
	private String vendaExemplaresFormatado;
	
	@Export(label = "% Venda", exhibitionOrder = 6)
	private String porcentagemVendaFormatado;
		
	public RegistroHistoricoEditorVO(String nomeEditor, String codigoProduto, String nomeProduto,
			Long edicaoProduto, BigDecimal reparte, BigInteger vendaExemplares) {
		this.nomeEditor=nomeEditor;
		this.codigoProduto=codigoProduto;
		this.nomeProduto=nomeProduto;
		this.edicaoProduto=edicaoProduto;
		this.reparte=reparte;
		this.vendaExemplares=vendaExemplares;
		this.formatarCampos();
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

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public BigDecimal getPorcentagemVenda() {
		return porcentagemVenda;
	}

	public void setPorcentagemVenda(BigDecimal porcentagemVenda) {
		this.porcentagemVendaFormatado = CurrencyUtil.formatarValor(porcentagemVenda);
		this.porcentagemVenda = porcentagemVenda;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public String getReparteFormatado() {
		return reparteFormatado;
	}

	public void setReparteFormatado(String reparteFormatado) {
		this.reparteFormatado = reparteFormatado;
	}

	public String getVendaExemplaresFormatado() {
		return vendaExemplaresFormatado;
	}

	public void setVendaExemplaresFormatado(String vendaExemplaresFormatado) {
		this.vendaExemplaresFormatado = vendaExemplaresFormatado;
	}

	public String getPorcentagemVendaFormatado() {
		return porcentagemVendaFormatado;
	}

	public void setPorcentagemVendaFormatado(String porcentagemVendaFormatado) {
		this.porcentagemVendaFormatado = porcentagemVendaFormatado;
	}

	private void formatarCampos() {
		this.reparteFormatado = CurrencyUtil.formatarValorTruncado(reparte);
		this.vendaExemplaresFormatado = CurrencyUtil.formatarValorTruncado(vendaExemplares);
		this.porcentagemVendaFormatado = CurrencyUtil.formatarValor(porcentagemVenda);
	}

}
