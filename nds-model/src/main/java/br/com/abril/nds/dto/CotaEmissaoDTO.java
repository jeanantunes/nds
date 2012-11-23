package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
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
	
	private Long idChamEncCota;
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
	private Integer box;
	private String codigoRota;
	private String nomeRota;
	private String codigoRoteiro;
	private String nomeRoteiro;
	
	private String numDocumento;
	

	private String vlrReparte;	
	private String vlrComDesconto;	
	private String vlrReparteLiquido;	
	private String vlrEncalhe;	
	private String vlrTotalLiquido;

	
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
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarDataPTBR(dataRecolhimento);
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
	public void setQtdeExemplares(BigInteger qtdeExemplares) {
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

	/**
	 * @return the box
	 */
	public Integer getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	public void setBox(Integer box) {
		this.box = box;
	}


	/**
	 * @return the codigoRota
	 */
	public String getCodigoRota() {
		return codigoRota;
	}

	/**
	 * @param codigoRota the codigoRota to set
	 */
	public void setCodigoRota(String codigoRota) {
		this.codigoRota = codigoRota;
	}

	/**
	 * @return the nomeRota
	 */
	public String getNomeRota() {
		return nomeRota;
	}

	/**
	 * @param nomeRota the nomeRota to set
	 */
	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}

	/**
	 * @return the idChamEncCota
	 */
	public Long getIdChamEncCota() {
		return idChamEncCota;
	}

	/**
	 * @param idChamEncCota the idChamEncCota to set
	 */
	public void setIdChamEncCota(Long idChamEncCota) {
		this.idChamEncCota = idChamEncCota;
	}

	/**
	 * @return the vlrReparte
	 */
	public String getVlrReparte() {
		return vlrReparte;
	}

	/**
	 * @param vlrReparte the vlrReparte to set
	 */
	public void setVlrReparte(String vlrReparte) {
		this.vlrReparte = vlrReparte;
	}

	/**
	 * @return the vlrComDesconto
	 */
	public String getVlrComDesconto() {
		return vlrComDesconto;
	}

	/**
	 * @param vlrComDesconto the vlrComDesconto to set
	 */
	public void setVlrComDesconto(String vlrComDesconto) {
		this.vlrComDesconto = vlrComDesconto;
	}

	/**
	 * @return the vlrReparteLiquido
	 */
	public String getVlrReparteLiquido() {
		return vlrReparteLiquido;
	}

	/**
	 * @param vlrReparteLiquido the vlrReparteLiquido to set
	 */
	public void setVlrReparteLiquido(String vlrReparteLiquido) {
		this.vlrReparteLiquido = vlrReparteLiquido;
	}

	/**
	 * @return the vlrEncalhe
	 */
	public String getVlrEncalhe() {
		return vlrEncalhe;
	}

	/**
	 * @param vlrEncalhe the vlrEncalhe to set
	 */
	public void setVlrEncalhe(String vlrEncalhe) {
		this.vlrEncalhe = vlrEncalhe;
	}

	/**
	 * @return the vlrTotalLiquido
	 */
	public String getVlrTotalLiquido() {
		return vlrTotalLiquido;
	}

	/**
	 * @param vlrTotalLiquido the vlrTotalLiquido to set
	 */
	public void setVlrTotalLiquido(String vlrTotalLiquido) {
		this.vlrTotalLiquido = vlrTotalLiquido;
	}

	public String getCodigoRoteiro() {
		return codigoRoteiro;
	}

	public void setCodigoRoteiro(String codigoRoteiro) {
		this.codigoRoteiro = codigoRoteiro;
	}

	public String getNomeRoteiro() {
		return nomeRoteiro;
	}

	public void setNomeRoteiro(String nomeRoteiro) {
		this.nomeRoteiro = nomeRoteiro;
	}
	
	
}
