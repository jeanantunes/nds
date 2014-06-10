package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class EstudoComplementarDTO  implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private long idEstudo;
	 private long idEstudoComplementar;
	 private long idProduto;
	 private String nomeProduto;

	 private long idEdicao;
	 private String nomeClassificacao;
	 private long idPublicacao;
	 private long idPEB;
	 private String nomeFornecedor;
	 private String dataLncto;
	 private String dataRclto;
	 private BigInteger QtdeReparte;
	 private Long numeroEdicao;
	 private String codigoProduto;
	 private String tipoSegmentoProduto;
	 
	public long getIdEstudo() {
		return idEstudo;
	}
	public void setIdEstudo(long idEstudo) {
		this.idEstudo = idEstudo;
	}
	public long getIdEstudoComplementar() {
		return idEstudoComplementar;
	}
	public void setIdEstudoComplementar(long idEstudoComplementar) {
		this.idEstudoComplementar = idEstudoComplementar;
	}
	public long getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(long idProduto) {
		this.idProduto = idProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public long getIdEdicao() {
		return idEdicao;
	}
	public void setIdEdicao(long idEdicao) {
		this.idEdicao = idEdicao;
	}
	public String getNomeClassificacao() {
		return nomeClassificacao;
	}
	public void setNomeClassificacao(String nomeClassificacao) {
		this.nomeClassificacao = nomeClassificacao;
	}
	public long getIdPublicacao() {
		return idPublicacao;
	}
	public void setIdPublicacao(long idPublicacao) {
		this.idPublicacao = idPublicacao;
	}
	public long getIdPEB() {
		return idPEB;
	}
	public void setIdPEB(long idPEB) {
		this.idPEB = idPEB;
	}
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	public String getDataLncto() {
		return dataLncto;
	}
	public void setDataLncto(String dataLncto) {
		this.dataLncto = dataLncto;
	}
	public String getDataRclto() {
		return dataRclto;
	}
	public void setDataRclto(String dataRclto) {
		this.dataRclto = dataRclto;
	}
	public BigInteger getQtdeReparte() {
		return QtdeReparte;
	}
	public void setQtdeReparte(BigInteger getQtdeReparte) {
		this.QtdeReparte = getQtdeReparte;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getTipoSegmentoProduto() {
		return tipoSegmentoProduto;
	}
	public void setTipoSegmentoProduto(String tipoSegmentoProduto) {
		this.tipoSegmentoProduto = tipoSegmentoProduto;
	}
	 
}
