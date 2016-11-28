package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
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
	
	private String naturezaOperacao;

	private String formaPagamento;

	private String serie;

	private Long numeroNF;

	private String dataLancamentoDeAte;

	private Date dataSaida;

	private String horaSaida;

	private int tipoNF;

	private String ambiente;

	private String chave;
	
	private boolean quebraTotalizacaoUltimaPagina;
	
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
	
	private String nomeBox;
	
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
	
	private String periodoRecolhimento;
	
	private String destinatarioCNPJ;

	private String destinatarioNome;

	private String destinatarioInscricaoEstadual;

	private String destinatarioLogradouro;

	private String destinatarioNumero;

	private String destinatarioComplemento;

	private String destinatarioBairro;

	private String destinatarioMunicipio;

	private String destinatarioUF;

	private String destinatarioCEP;

	private String destinatarioTelefone;
	
	private Integer destinatarioCodigoBox;
	
	private String destinatarioNomeBox;
	
	private String destinatarioCodigoRota;
	
	private String destinatarioDescricaoRota;

	private String emissorCNPJ;

	private String emissorNome;

	private String emissorFantasia;

	private String emissorInscricaoEstadual;

	private String emissorInscricaoEstadualSubstituto;

	private String emissorInscricaoMunicipal;

	private String emissorLogradouro;

	private String emissorNumero;

	private String emissorBairro;

	private String emissorMunicipio;

	private String emissorUF;

	private String emissorCEP;

	private String emissorTelefone;
	
	private boolean itensComFuroLancamento;

	private DistribuidorDTO distribuidorDTO;
	
	private List<List<ProdutoEmissaoDTO>> paginasProduto;
	
	private List<List<CapaDTO>> paginasCapa;
	
	private List<ProdutoEmissaoDTO> produtos;
	
	private Long qtdGrupoCota;
	
	private BigDecimal totalEmissaoCE = new BigDecimal(0);
	
	public String getDestinatarioCNPJ() {
		return destinatarioCNPJ;
	}

	public void setDestinatarioCNPJ(String destinatarioCNPJ) {
		this.destinatarioCNPJ = destinatarioCNPJ;
	}

	public String getDestinatarioNome() {
		return destinatarioNome;
	}

	public void setDestinatarioNome(String destinatarioNome) {
		this.destinatarioNome = destinatarioNome;
	}

	public String getDestinatarioInscricaoEstadual() {
		return destinatarioInscricaoEstadual;
	}

	public void setDestinatarioInscricaoEstadual(
			String destinatarioInscricaoEstadual) {
		this.destinatarioInscricaoEstadual = destinatarioInscricaoEstadual;
	}

	public String getDestinatarioLogradouro() {
		return destinatarioLogradouro;
	}

	public void setDestinatarioLogradouro(String destinatarioLogradouro) {
		this.destinatarioLogradouro = destinatarioLogradouro;
	}

	public String getDestinatarioNumero() {
		return destinatarioNumero;
	}

	public void setDestinatarioNumero(String destinatarioNumero) {
		this.destinatarioNumero = destinatarioNumero;
	}

	public String getDestinatarioComplemento() {
		return destinatarioComplemento;
	}

	public void setDestinatarioComplemento(String destinatarioComplemento) {
		this.destinatarioComplemento = destinatarioComplemento;
	}

	public String getDestinatarioBairro() {
		return destinatarioBairro;
	}

	public void setDestinatarioBairro(String destinatarioBairro) {
		this.destinatarioBairro = destinatarioBairro;
	}

	public String getDestinatarioMunicipio() {
		return destinatarioMunicipio;
	}

	public void setDestinatarioMunicipio(String destinatarioMunicipio) {
		this.destinatarioMunicipio = destinatarioMunicipio;
	}

	public String getDestinatarioUF() {
		return destinatarioUF;
	}

	public void setDestinatarioUF(String destinatarioUF) {
		this.destinatarioUF = destinatarioUF;
	}

	public String getDestinatarioCEP() {
		return destinatarioCEP;
	}

	public void setDestinatarioCEP(String destinatarioCEP) {
		this.destinatarioCEP = destinatarioCEP;
	}

	public String getDestinatarioTelefone() {
		return destinatarioTelefone;
	}

	public void setDestinatarioTelefone(String destinatarioTelefone) {
		this.destinatarioTelefone = destinatarioTelefone;
	}

	public Integer getDestinatarioCodigoBox() {
		return destinatarioCodigoBox;
	}

	public void setDestinatarioCodigoBox(Integer destinatarioCodigoBox) {
		this.destinatarioCodigoBox = destinatarioCodigoBox;
	}

	public String getDestinatarioNomeBox() {
		return destinatarioNomeBox;
	}

	public void setDestinatarioNomeBox(String destinatarioNomeBox) {
		this.destinatarioNomeBox = destinatarioNomeBox;
	}

	public String getDestinatarioCodigoRota() {
		return destinatarioCodigoRota;
	}

	public void setDestinatarioCodigoRota(String destinatarioCodigoRota) {
		this.destinatarioCodigoRota = destinatarioCodigoRota;
	}

	public String getDestinatarioDescricaoRota() {
		return destinatarioDescricaoRota;
	}

	public void setDestinatarioDescricaoRota(String destinatarioDescricaoRota) {
		this.destinatarioDescricaoRota = destinatarioDescricaoRota;
	}
	public CotaEmissaoDTO() {
		
	}
	
	public String getNomeBox() {
		return nomeBox;
	}

	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

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
		this.cep = (cep!= null && !cep.isEmpty())? Util.adicionarMascaraCEP(cep) : cep;
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
		this.qtdeExemplares = (qtdeExemplares==null)?0:qtdeExemplares.intValue();
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
		this.vlrTotalCe = CurrencyUtil.formatarValor( (vlrTotalCe == null)?BigDecimal.ZERO:vlrTotalCe);
		this.totalEmissaoCE = totalEmissaoCE.add((vlrTotalCe == null)?BigDecimal.ZERO:vlrTotalCe);
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

	public List<List<ProdutoEmissaoDTO>> getPaginasProduto() {
		return paginasProduto;
	}

	public void setPaginasProduto(List<List<ProdutoEmissaoDTO>> paginasProduto) {
		this.paginasProduto = paginasProduto;
	}

	public List<ProdutoEmissaoDTO> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoEmissaoDTO> produtos) {
		this.produtos = produtos;
	}

	public List<List<CapaDTO>> getPaginasCapa() {
		return paginasCapa;
	}

	public void setPaginasCapa(List<List<CapaDTO>> paginasCapa) {
		this.paginasCapa = paginasCapa;
	}

	public boolean isQuebraTotalizacaoUltimaPagina() {
		return quebraTotalizacaoUltimaPagina;
	}

	public void setQuebraTotalizacaoUltimaPagina(
			boolean quebraTotalizacaoUltimaPagina) {
		this.quebraTotalizacaoUltimaPagina = quebraTotalizacaoUltimaPagina;
	}

	public String getPeriodoRecolhimento() {
		return periodoRecolhimento;
	}

	public void setPeriodoRecolhimento(String periodoRecolhimento) {
		this.periodoRecolhimento = periodoRecolhimento;
	}

	public String getEmissorCNPJ() {
		return emissorCNPJ;
	}

	public void setEmissorCNPJ(String emissorCNPJ) {
		this.emissorCNPJ = emissorCNPJ;
	}

	public String getEmissorNome() {
		return emissorNome;
	}

	public void setEmissorNome(String emissorNome) {
		this.emissorNome = emissorNome;
	}

	public String getEmissorFantasia() {
		return emissorFantasia;
	}

	public void setEmissorFantasia(String emissorFantasia) {
		this.emissorFantasia = emissorFantasia;
	}

	public String getEmissorInscricaoEstadual() {
		return emissorInscricaoEstadual;
	}

	public void setEmissorInscricaoEstadual(String emissorInscricaoEstadual) {
		this.emissorInscricaoEstadual = emissorInscricaoEstadual;
	}

	public String getEmissorInscricaoEstadualSubstituto() {
		return emissorInscricaoEstadualSubstituto;
	}

	public void setEmissorInscricaoEstadualSubstituto(
			String emissorInscricaoEstadualSubstituto) {
		this.emissorInscricaoEstadualSubstituto = emissorInscricaoEstadualSubstituto;
	}

	public String getEmissorInscricaoMunicipal() {
		return emissorInscricaoMunicipal;
	}

	public void setEmissorInscricaoMunicipal(String emissorInscricaoMunicipal) {
		this.emissorInscricaoMunicipal = emissorInscricaoMunicipal;
	}

	public String getEmissorLogradouro() {
		return emissorLogradouro;
	}

	public void setEmissorLogradouro(String emissorLogradouro) {
		this.emissorLogradouro = emissorLogradouro;
	}

	public String getEmissorNumero() {
		return emissorNumero;
	}

	public void setEmissorNumero(String emissorNumero) {
		this.emissorNumero = emissorNumero;
	}

	public String getEmissorBairro() {
		return emissorBairro;
	}

	public void setEmissorBairro(String emissorBairro) {
		this.emissorBairro = emissorBairro;
	}

	public String getEmissorMunicipio() {
		return emissorMunicipio;
	}

	public void setEmissorMunicipio(String emissorMunicipio) {
		this.emissorMunicipio = emissorMunicipio;
	}

	public String getEmissorUF() {
		return emissorUF;
	}

	public void setEmissorUF(String emissorUF) {
		this.emissorUF = emissorUF;
	}

	public String getEmissorCEP() {
		return emissorCEP;
	}

	public void setEmissorCEP(String emissorCEP) {
		this.emissorCEP = emissorCEP;
	}

	public String getEmissorTelefone() {
		return emissorTelefone;
	}

	public void setEmissorTelefone(String emissorTelefone) {
		this.emissorTelefone = emissorTelefone;
	}

	public String getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(String naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Long getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(Long numeroNF) {
		this.numeroNF = numeroNF;
	}

	public String getDataLancamentoDeAte() {
		return dataLancamentoDeAte;
	}

	public void setDataLancamentoDeAte(String dataLancamentoDeAte) {
		this.dataLancamentoDeAte = dataLancamentoDeAte;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public String getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}

	public int getTipoNF() {
		return tipoNF;
	}

	public void setTipoNF(int tipoNF) {
		this.tipoNF = tipoNF;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public boolean isItensComFuroLancamento() {
		return itensComFuroLancamento;
	}

	public void setItensComFuroLancamento(boolean itensComFuroLancamento) {
		this.itensComFuroLancamento = itensComFuroLancamento;
	}

	public DistribuidorDTO getDistribuidorDTO() {
		return distribuidorDTO;
	}

	public void setDistribuidorDTO(DistribuidorDTO distribuidorDTO) {
		this.distribuidorDTO = distribuidorDTO;
	}
	
    public Long getQtdGrupoCota() {
        return qtdGrupoCota;
    }

    public void setQtdGrupoCota(Long qtdGrupoCota) {
        this.qtdGrupoCota = qtdGrupoCota;
    }

	public BigDecimal getTotalEmissaoCE() {
		return totalEmissaoCE;
	}
}
