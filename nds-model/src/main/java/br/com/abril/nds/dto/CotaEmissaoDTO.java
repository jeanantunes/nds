package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaEmissaoDTO implements Serializable{

	private static final long serialVersionUID = 3527246197853581250L;

	private Long idCota;
	@Export(label="Cota")
	private Integer numCota;
	@Export(label="Nome")
	private String nomeCota;
	@Export(label="Qtde. Exemplares")
	private Integer qtdeExemplares;
	@Export(label="Valor Total CE R$")
	private String vlrTotalCe;
	
	private List<ProdutoEmissaoDTO> produtos;
	private String numeroNome;
	private String cnpj;
	private String endereco;
	private String cidade;
	private String uf;
	private String cep;
	private String inscricaoEstadual;
	private String dataRecolhimento;
	private String dataEmissao;
	private String boxRota;
	private String numDocumento;
	
	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}
	
	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}
	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}
	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}
	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}
	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}
	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}
	/**
	 * @return the inscricaoEstadual
	 */
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}
	/**
	 * @param inscricaoEstadual the inscricaoEstadual to set
	 */
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	/**
	 * @return the dataRecolhimento
	 */
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	/**
	 * @return the dataEmissao
	 */
	public String getDataEmissao() {
		return dataEmissao;
	}
	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	/**
	 * @return the boxRota
	 */
	public String getBoxRota() {
		return boxRota;
	}
	/**
	 * @param boxRota the boxRota to set
	 */
	public void setBoxRota(String boxRota) {
		this.boxRota = boxRota;
	}
		
	/**
	 * @return the numCota
	 */
	public Integer getNumCota() {
		return numCota;
	}
	/**
	 * @param numCota the numCota to set
	 */
	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}
	
	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}
	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	/**
	 * @return the qtdeExemplares
	 */
	public Integer getQtdeExemplares() {
		return qtdeExemplares;
	}
	/**
	 * @param qtdeExemplares the qtdeExemplares to set
	 */
	public void setQtdeExemplares(BigDecimal qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares.intValue();
	}
	/**
	 * @return the vlrTotalCe
	 */
	public String getVlrTotalCe() {
		return vlrTotalCe;
	}
	/**
	 * @param vlrTotalCe the vlrTotalCe to set
	 */
	public void setVlrTotalCe(BigDecimal vlrTotalCe) {
		this.vlrTotalCe = CurrencyUtil.formatarValor(vlrTotalCe);
	}
	/**
	 * @return the produtos
	 */
	public List<ProdutoEmissaoDTO> getProdutos() {
		return produtos;
	}
	/**
	 * @param produtos the produtos to set
	 */
	public void setProdutos(List<ProdutoEmissaoDTO> produtos) {
		this.produtos = produtos;
	}

	/**
	 * @return the numDocumento
	 */
	public String getNumDocumento() {
		return numDocumento;
	}

	/**
	 * @param numDocumento the numDocumento to set
	 */
	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
	 * @return the numeroNome
	 */
	public String getNumeroNome() {
		return numeroNome;
	}

	/**
	 * @param numeroNome the numeroNome to set
	 */
	public void setNumeroNome(String numeroNome) {
		this.numeroNome = numeroNome;
	}
	
	
}
