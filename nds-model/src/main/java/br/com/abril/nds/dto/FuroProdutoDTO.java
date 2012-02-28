package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FuroProdutoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -580201558784688016L;
	
	private final String MASCARA_DATA = "dd/MM/yyyy";
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private BigDecimal quantidadeExemplares;
	
	private String novaData;
	
	private String pathImagem;
	
	private Long idLancamento;
	
	private Long idProdutoEdicao;
	
	public FuroProdutoDTO(String codigoProduto, String nomeProduto, Long edicao, BigDecimal quantidadeExemplares, 
			Date novaData, String pathImagem, Long idLancamento, Long idProdutoEdicao){
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.edicao = edicao;
		this.quantidadeExemplares = quantidadeExemplares;
		this.pathImagem = pathImagem + codigoProduto + edicao;
		this.idLancamento = idLancamento;
		this.idProdutoEdicao = idProdutoEdicao;
		
		if (novaData != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(novaData);
			calendar.add(Calendar.DATE, 1);
			this.novaData = new SimpleDateFormat(MASCARA_DATA).format(calendar.getTime());
		}
	}
	
	public FuroProdutoDTO(String codigoProduto){
		this(codigoProduto, null, null, null, null, null, null, null);
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

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public BigDecimal getQuantidadeExemplares() {
		return quantidadeExemplares;
	}

	public void setQuantidadeExemplares(BigDecimal quantidadeExemplares) {
		this.quantidadeExemplares = quantidadeExemplares;
	}

	public String getNovaData() {
		return novaData;
	}

	public void setNovaData(String novaData) {
		this.novaData = novaData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPathImagem() {
		return pathImagem;
	}

	public void setPathImagem(String pathImagem) {
		this.pathImagem = pathImagem;
	}

	public Long getIdLancamento() {
		return idLancamento;
	}

	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
}