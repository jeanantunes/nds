package br.com.abril.nds.dto;

import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;

public class PdvDTO implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long idCota;
	
	private StatusPDV statusPDV;
	
	private String descricaoTipoPontoPDV;
	
	private Date dataInicio;
	
	private String nomePDV ;
	
	private String contato;
	
	private String endereco;
	
	private String telefone;
	
	private boolean principal;
	
	private String site;
	
	private String email;
	
	private String pontoReferencia;
	
	private boolean dentroOutroEstabelecimento;
	
	private boolean arrendatario;
	
	private TamanhoPDV tamanhoPDV;
	
	private int qtdeFuncionarios;
	
	private boolean sistemaIPV;
	
	private BigDecimal porcentagemFaturamento;
	
	private TipoLicencaMunicipalDTO tipoLicencaMunicipal = new TipoLicencaMunicipalDTO();

	private String numeroLicenca;
	
	private String nomeLicenca;
	
	private TipoEstabelecimentoAssociacaoPDVDTO tipoEstabelecimentoAssociacaoPDV = new TipoEstabelecimentoAssociacaoPDVDTO();
	
	private List<PeriodoFuncionamentoDTO> periodosFuncionamentoDTO;
	
	private List<EnderecoAssociacaoDTO> enderecosAdicionar;
		
	private List<TelefoneAssociacaoDTO> telefonesAdicionar;
	
	private Set<Long> telefonesRemover;
	
	private Set<Long> enderecosRemover;
	
    private CaracteristicaDTO caracteristicaDTO;
    
	private List<Long> geradorFluxoSecundario = new ArrayList<Long>();
    
    private Long geradorFluxoPrincipal;
    
    private List<Long> maps = new ArrayList<Long>();
    
    private TipoPontoPDVDTO tipoPontoPDV = new TipoPontoPDVDTO();
    
    private boolean expositor;
    
    private String tipoExpositor;
    
    private FileInputStream imagem;
    
    private String pathImagem;
    
    private String pathAplicacao;
    
    private boolean possuiImagem;
    
	/**
	 * @return the expositor
	 */
	public boolean isExpositor() {
		return expositor;
	}

	/**
	 * @param expositor the expositor to set
	 */
	public void setExpositor(boolean expositor) {
		this.expositor = expositor;
	}

	

	/**
	 * @return the enderecosAdicionar
	 */
	public List<EnderecoAssociacaoDTO> getEnderecosAdicionar() {
		return enderecosAdicionar;
	}

	/**
	 * @param enderecosAdicionar the enderecosAdicionar to set
	 */
	public void setEnderecosAdicionar(List<EnderecoAssociacaoDTO> enderecosAdicionar) {
		this.enderecosAdicionar = enderecosAdicionar;
	}

	/**
	 * @return the enderecosRemover
	 */
	public Set<Long> getEnderecosRemover() {
		return enderecosRemover;
	}

	/**
	 * @param enderecosRemover the enderecosRemover to set
	 */
	public void setEnderecosRemover(Set<Long> enderecosRemover) {
		this.enderecosRemover = enderecosRemover;
	}

	/**
	 * @return the tipoExpositor
	 */
	public String getTipoExpositor() {
		return tipoExpositor;
	}

	/**
	 * @param tipoExpositor the tipoExpositor to set
	 */
	public void setTipoExpositor(String tipoExpositor) {
		this.tipoExpositor = tipoExpositor;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the caracteristicaDTO
	 */
	public CaracteristicaDTO getCaracteristicaDTO() {
		return caracteristicaDTO;
	}

	/**
	 * @param caracteristicaDTO the caracteristicaDTO to set
	 */
	public void setCaracteristicaDTO(CaracteristicaDTO caracteristicaDTO) {
		this.caracteristicaDTO = caracteristicaDTO;
	}
	

	/**
	 * @return the statusPDV
	 */
	public StatusPDV getStatusPDV() {
		return statusPDV;
	}

	/**
	 * @param statusPDV the statusPDV to set
	 */
	public void setStatusPDV(StatusPDV statusPDV) {
		this.statusPDV = statusPDV;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the nomePDV
	 */
	public String getNomePDV() {
		return nomePDV;
	}

	/**
	 * @param nomePDV the nomePDV to set
	 */
	public void setNomePDV(String nomePDV) {
		this.nomePDV = nomePDV;
	}

	/**
	 * @return the contato
	 */
	public String getContato() {
		return contato;
	}

	/**
	 * @param contato the contato to set
	 */
	public void setContato(String contato) {
		this.contato = contato;
	}

	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the pontoReferencia
	 */
	public String getPontoReferencia() {
		return pontoReferencia;
	}

	/**
	 * @param pontoReferencia the pontoReferencia to set
	 */
	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia;
	}

	/**
	 * @return the dentroOutroEstabelecimento
	 */
	public boolean isDentroOutroEstabelecimento() {
		return dentroOutroEstabelecimento;
	}

	/**
	 * @param dentroOutroEstabelecimento the dentroOutroEstabelecimento to set
	 */
	public void setDentroOutroEstabelecimento(boolean dentroOutroEstabelecimento) {
		this.dentroOutroEstabelecimento = dentroOutroEstabelecimento;
	}

	
	/**
	 * @return the tamanhoPDV
	 */
	public TamanhoPDV getTamanhoPDV() {
		return tamanhoPDV;
	}

	/**
	 * @param tamanhoPDV the tamanhoPDV to set
	 */
	public void setTamanhoPDV(TamanhoPDV tamanhoPDV) {
		this.tamanhoPDV = tamanhoPDV;
	}

	/**
	 * @return the qtdeFuncionarios
	 */
	public int getQtdeFuncionarios() {
		return qtdeFuncionarios;
	}

	/**
	 * @param qtdeFuncionarios the qtdeFuncionarios to set
	 */
	public void setQtdeFuncionarios(int qtdeFuncionarios) {
		this.qtdeFuncionarios = qtdeFuncionarios;
	}

	/**
	 * @return the sistemaIPV
	 */
	public boolean isSistemaIPV() {
		return sistemaIPV;
	}

	/**
	 * @param sistemaIPV the sistemaIPV to set
	 */
	public void setSistemaIPV(boolean sistemaIPV) {
		this.sistemaIPV = sistemaIPV;
	}

	/**
	 * @return the porcentagemFaturamento
	 */
	public BigDecimal getPorcentagemFaturamento() {
		return porcentagemFaturamento;
	}

	/**
	 * @param porcentagemFaturamento the porcentagemFaturamento to set
	 */
	public void setPorcentagemFaturamento(BigDecimal porcentagemFaturamento) {
		this.porcentagemFaturamento = porcentagemFaturamento;
	}

	/**
	 * @return the tipoLicencaMunicipal
	 */
	public TipoLicencaMunicipalDTO getTipoLicencaMunicipal() {
		return tipoLicencaMunicipal;
	}

	/**
	 * @param tipoLicencaMunicipal the tipoLicencaMunicipal to set
	 */
	public void setTipoLicencaMunicipal(TipoLicencaMunicipalDTO tipoLicencaMunicipal) {
		this.tipoLicencaMunicipal = tipoLicencaMunicipal;
	}

	/**
	 * @return the numeroLicenca
	 */
	public String getNumeroLicenca() {
		return numeroLicenca;
	}

	/**
	 * @param numeroLicenca the numeroLicenca to set
	 */
	public void setNumeroLicenca(String numeroLicenca) {
		this.numeroLicenca = numeroLicenca;
	}

	/**
	 * @return the nomeLicenca
	 */
	public String getNomeLicenca() {
		return nomeLicenca;
	}

	/**
	 * @param nomeLicenca the nomeLicenca to set
	 */
	public void setNomeLicenca(String nomeLicenca) {
		this.nomeLicenca = nomeLicenca;
	}

	/**
	 * @return the tipoEstabelecimentoAssociacaoPDV
	 */
	public TipoEstabelecimentoAssociacaoPDVDTO getTipoEstabelecimentoAssociacaoPDV() {
		return tipoEstabelecimentoAssociacaoPDV;
	}

	/**
	 * @param tipoEstabelecimentoAssociacaoPDV the tipoEstabelecimentoAssociacaoPDV to set
	 */
	public void setTipoEstabelecimentoAssociacaoPDV(
			TipoEstabelecimentoAssociacaoPDVDTO tipoEstabelecimentoAssociacaoPDV) {
		this.tipoEstabelecimentoAssociacaoPDV = tipoEstabelecimentoAssociacaoPDV;
	}

	/**
	 * @return the maps
	 */
	public List<Long> getMaps() {
		return maps;
	}

	/**
	 * @param maps the maps to set
	 */
	public void setMaps(List<Long> maps) {
		this.maps = maps;
	}

	/**
	 * @return the geradorFluxoSecundario
	 */
	public List<Long> getGeradorFluxoSecundario() {
		return geradorFluxoSecundario;
	}

	/**
	 * @param geradorFluxoSecundario the geradorFluxoSecundario to set
	 */
	public void setGeradorFluxoSecundario(List<Long> geradorFluxoSecundario) {
		this.geradorFluxoSecundario = geradorFluxoSecundario;
	}

	/**
	 * @return the geradorFluxoPrincipal
	 */
	public Long getGeradorFluxoPrincipal() {
		return geradorFluxoPrincipal;
	}

	/**
	 * @param geradorFluxoPrincipal the geradorFluxoPrincipal to set
	 */
	public void setGeradorFluxoPrincipal(Long geradorFluxoPrincipal) {
		this.geradorFluxoPrincipal = geradorFluxoPrincipal;
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
	 * @return the telefone
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * @param telefone the telefone to set
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * @return the principal
	 */
	public boolean isPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	/**
	 * @return the tipoPontoPDV
	 */
	public TipoPontoPDVDTO getTipoPontoPDV() {
		return tipoPontoPDV;
	}

	/**
	 * @param tipoPontoPDV the tipoPontoPDV to set
	 */
	public void setTipoPontoPDV(TipoPontoPDVDTO tipoPontoPDV) {
		this.tipoPontoPDV = tipoPontoPDV;
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

	public List<PeriodoFuncionamentoDTO> getPeriodosFuncionamentoDTO() {
		return periodosFuncionamentoDTO;
	}

	public void setPeriodosFuncionamentoDTO(List<PeriodoFuncionamentoDTO> periodosFuncionamentoDTO) {
		this.periodosFuncionamentoDTO = periodosFuncionamentoDTO;
	}

	/**
	 * @return the descricaoTipoPontoPDV
	 */
	public String getDescricaoTipoPontoPDV() {
		return descricaoTipoPontoPDV;
	}

	/**
	 * @param descricaoTipoPontoPDV the descricaoTipoPontoPDV to set
	 */
	public void setDescricaoTipoPontoPDV(String descricaoTipoPontoPDV) {
		this.descricaoTipoPontoPDV = descricaoTipoPontoPDV;
	}

	/**
	 * @return the imagem
	 */
	public FileInputStream getImagem() {
		return imagem;
	}

	/**
	 * @param imagem the imagem to set
	 */
	public void setImagem(FileInputStream imagem) {
		this.imagem = imagem;
	}
	
	/**
	 * @return the telefonesAdicionar
	 */
	public List<TelefoneAssociacaoDTO> getTelefonesAdicionar() {
		return telefonesAdicionar;
	}

	/**
	 * @param telefonesAdicionar the telefonesAdicionar to set
	 */
	public void setTelefonesAdicionar(List<TelefoneAssociacaoDTO> telefonesAdicionar) {
		this.telefonesAdicionar = telefonesAdicionar;
	}

	/**
	 * @return the telefonesRemover
	 */
	public Set<Long> getTelefonesRemover() {
		return telefonesRemover;
	}

	/**
	 * @param listaTelefoneRemover the telefonesRemover to set
	 */
	public void setTelefonesRemover(Set<Long> listaTelefoneRemover) {
		this.telefonesRemover = listaTelefoneRemover;
	}

	public String getPathImagem() {
		return pathImagem;
	}

	public void setPathImagem(String pathImagem) {
		this.pathImagem = pathImagem;
	}

	public String getPathAplicacao() {
		return pathAplicacao;
	}

	public void setPathAplicacao(String pathAplicacao) {
		this.pathAplicacao = pathAplicacao;
	}

	public boolean isArrendatario() {
		return arrendatario;
	}

	public void setArrendatario(boolean arrendatario) {
		this.arrendatario = arrendatario;
	}
	
	/**
     * @return the possuiImagem
     */
    public boolean isPossuiImagem() {
        return possuiImagem;
    }

    /**
     * @param possuiImagem the possuiImagem to set
     */
    public void setPossuiImagem(boolean possuiImagem) {
        this.possuiImagem = possuiImagem;
    }

    public void addGeradorFluxoSecundario(Long geradorFluxoSecundario) {
	    if (this.geradorFluxoSecundario == null) {
	        this.geradorFluxoSecundario = new ArrayList<Long>();
	    }
	    this.geradorFluxoSecundario.add(geradorFluxoSecundario);
	}
	
	public void addMaterialPromocional(Long materialPromocional) {
	    if (this.maps == null) {
	        this.maps = new ArrayList<Long>();
	    }
	    this.maps.add(materialPromocional);
	}
	
}
