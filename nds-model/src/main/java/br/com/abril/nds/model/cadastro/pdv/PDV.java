package br.com.abril.nds.model.cadastro.pdv;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;

/**
 * Entidade que representa o PDV associado
 * a uma cota
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PDV")
@SequenceGenerator(name="PDV_SEQ", initialValue = 1, allocationSize = 1)
public class PDV implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5121689715572569495L;
	
	@Id
	@GeneratedValue(generator = "PDV_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date dataInclusao;
	
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	@JoinColumn(name = "COTA_ID")
	@Cascade(value = {org.hibernate.annotations.CascadeType.PERSIST,
					  org.hibernate.annotations.CascadeType.MERGE,
					  org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Cota cota;
	
	/**
	 * Status do PDV
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_PDV")
	private StatusPDV status;
	
	@OneToMany(mappedBy = "pdv", cascade=CascadeType.REMOVE)
	private Set<EnderecoPDV> enderecos = new HashSet<EnderecoPDV>();
	
	@OneToMany(mappedBy = "pdv",cascade=CascadeType.REMOVE)
	private Set<TelefonePDV> telefones = new HashSet<TelefonePDV>();
	
	/**
	 * Nome do PDV
	 */
	@Column(name = "NOME", nullable = false)
	private String nome;
	
	/**
	 * Nome do contato no PDV
	 */
	@Column(name = "CONTATO")
	private String contato;
	
	/**
	 * Site do PDV
	 */
	@Column(name = "SITE")
	private String site;
	
	/**
	 * Email do PDV
	 */
	@Column(name = "EMAIL")
	private String email;
	
	/**
	 * Ponto de referência do PDV
	 */
	@Column(name = "PONTO_REFERENCIA")
	private String pontoReferencia;
	
	/**
	 * Flag indicando se o pdv esta dentro de outro estabelecimento
	 */
	@Column(name = "DENTRO_OUTRO_ESTABELECIMENTO")
	private boolean dentroOutroEstabelecimento;
	
	/**
	 * Flag indicando se o pdv possui arrendatário
	 */
	@Column(name = "ARRENDATARIO")
	private boolean arrendatario;
	
	/**
	 * Tipo de estabelecimento ao qual o PDV está inserido
	 */
	@OneToOne
	@JoinColumn(name = "TIPO_ESTABELECIMENTO_PDV_ID" )
	private TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoPDV;
	
	/**
	 * Tamanho do PDV
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TAMANHO_PDV", nullable = true)
	private TamanhoPDV tamanhoPDV;
	
	/**
	 * Flag indicando se o PDV possui o Sistema IPV
	 */
	@Column(name = "POSSUI_SISTEMA_IPV")
	private boolean possuiSistemaIPV;
	
	/**
	 * Quantidade de funcionários do PDV
	 */
	@Column(name = "QTDE_FUNCIONARIOS")
	private int qtdeFuncionarios;
	
	/**
	 * Porcentagem que o PDV representa no total do faturamento
	 * da cota
	 */
	@Column(name = "PORCENTAGEM_FATURAMENTO")
	private BigDecimal porcentagemFaturamento;
	
	/**
	 * Licença municipal do PDV
	 */
	@Embedded
	private LicencaMunicipal licencaMunicipal;
	
	/**
	 * Períodos de funcionamento do PDV
	 */
	@OneToMany(mappedBy = "pdv", cascade ={CascadeType.REMOVE})
	private Set<PeriodoFuncionamentoPDV> periodos = new HashSet<PeriodoFuncionamentoPDV>();
	
	/**
	 * Características do PDV
	 */
	@Embedded
	private CaracteristicasPDV caracteristicas;
	
	/**
	 * Segmentação do PDV
	 */
	@Embedded
	private SegmentacaoPDV segmentacao;	
	
	/**
	 * Materiais promocionais do PDV
	 */
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name = "PDV_MATERIAL_PROMOCIONAL", joinColumns = {@JoinColumn(name = "PDV_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "MATERIAL_PROMOCIONAL_ID")})
	private Set<MaterialPromocional> materiais = new HashSet<MaterialPromocional>();
	  	
	@Column(name = "EXPOSITOR", nullable = true)  	
	private Boolean expositor;
		
	@Column(name = "TIPO_EXPOSITOR")
	private String tipoExpositor;

	@OneToOne(mappedBy = "pdv")
	@Cascade(org.hibernate.annotations.CascadeType.REMOVE)
	private GeradorFluxoPDV geradorFluxoPDV;
	
	@OneToMany(mappedBy = "pdv")
	private List<RotaPDV> rotas =  new ArrayList<RotaPDV>();

	@OneToMany(mappedBy="pdv")
	@Cascade(org.hibernate.annotations.CascadeType.REMOVE)
	private List<FixacaoRepartePdv> fixacaoRepartePdv;	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public Set<EnderecoPDV> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(Set<EnderecoPDV> enderecos) {
		this.enderecos = enderecos;
	}
	
	public StatusPDV getStatus() {
		return status;
	}
	
	public void setStatus(StatusPDV status) {
		this.status = status;
	}
	
	public Set<TelefonePDV> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(Set<TelefonePDV> telefones) {
		this.telefones = telefones;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPontoReferencia() {
		return pontoReferencia;
	}

	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia;
	}
	
	public boolean isDentroOutroEstabelecimento() {
		return dentroOutroEstabelecimento;
	}
	
	public void setDentroOutroEstabelecimento(boolean dentroOutroEstabelecimento) {
		this.dentroOutroEstabelecimento = dentroOutroEstabelecimento;
	}
	
	public TipoEstabelecimentoAssociacaoPDV getTipoEstabelecimentoPDV() {
		return tipoEstabelecimentoPDV;
	}
	
	public void setTipoEstabelecimentoPDV(
			TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoPDV) {
		this.tipoEstabelecimentoPDV = tipoEstabelecimentoPDV;
	}
	
	public TamanhoPDV getTamanhoPDV() {
		return tamanhoPDV;
	}
	
	public void setTamanhoPDV(TamanhoPDV tamanhoPDV) {
		this.tamanhoPDV = tamanhoPDV;
	}
	
	public boolean isPossuiSistemaIPV() {
		return possuiSistemaIPV;
	}
	
	public void setPossuiSistemaIPV(boolean possuiSistemaIPV) {
		this.possuiSistemaIPV = possuiSistemaIPV;
	}
	
	public int getQtdeFuncionarios() {
		return qtdeFuncionarios;
	}
	
	public void setQtdeFuncionarios(int qtdeFuncionarios) {
		this.qtdeFuncionarios = qtdeFuncionarios;
	}
	
	public BigDecimal getPorcentagemFaturamento() {
		return porcentagemFaturamento;
	}
	
	public void setPorcentagemFaturamento(BigDecimal porcentagemFaturamento) {
		this.porcentagemFaturamento = porcentagemFaturamento;
	}
	
	public LicencaMunicipal getLicencaMunicipal() {
		return licencaMunicipal;
	}
	
	public void setLicencaMunicipal(LicencaMunicipal licencaMunicipal) {
		this.licencaMunicipal = licencaMunicipal;
	}
	
	public Set<PeriodoFuncionamentoPDV> getPeriodos() {
		return periodos;
	}
	
	public void setPeriodos(Set<PeriodoFuncionamentoPDV> periodos) {
		this.periodos = periodos;
	}
	
	public CaracteristicasPDV getCaracteristicas() {
		return caracteristicas;
	}
	
	public void setCaracteristicas(CaracteristicasPDV caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
	public SegmentacaoPDV getSegmentacao() {
		return segmentacao;
	}
	
	public void setSegmentacao(SegmentacaoPDV segmentacao) {
		this.segmentacao = segmentacao;
	}
		
	public Set<MaterialPromocional> getMateriais() {
		return materiais;
	}
	
	public void setMateriais(Set<MaterialPromocional> materiais) {
		this.materiais = materiais;
	}

	/**
	 * @return the expositor
	 */
	public Boolean isExpositor() {
		return expositor;
	}

	/**
	 * @param expositor the expositor to set
	 */
	public void setExpositor(Boolean expositor) {
		this.expositor = expositor;
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
	 * @return the geradorFluxoPDV
	 */
	public GeradorFluxoPDV getGeradorFluxoPDV() {
		return geradorFluxoPDV;
	}

	/**
	 * @param geradorFluxoPDV the geradorFluxoPDV to set
	 */
	public void setGeradorFluxoPDV(GeradorFluxoPDV geradorFluxoPDV) {
		this.geradorFluxoPDV = geradorFluxoPDV;
	}

	/**
	 * @return the dataInclusao
	 */
	public Date getDataInclusao() {
		return dataInclusao;
	}

	/**
	 * @param dataInclusao the dataInclusao to set
	 */
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	
	/**
	 * @return the expositor
	 */
	public Boolean getExpositor() {
		return expositor;
	}
	
	/**
	 * @return the arrendatario
	 */
	public boolean isArrendatario() {
		return arrendatario;
	}

	/**
	 * @param arrendatario the arrendatario to set
	 */
	public void setArrendatario(boolean arrendatario) {
		this.arrendatario = arrendatario;
	}
	
	/**
     * @return the rotas
     */
    public List<RotaPDV> getRotas() {
        return rotas;
    }

    /**
     * @param rotas the rotas to set
     */
    public void setRotas(List<RotaPDV> rotas) {
        this.rotas = rotas;
    }

    /**
	 * @return the fixacaoRepartePdv
	 */
	public List<FixacaoRepartePdv> getFixacaoRepartePdv() {
		return fixacaoRepartePdv;
	}

	/**
	 * @param fixacaoRepartePdv the fixacaoRepartePdv to set
	 */
	public void setFixacaoRepartePdv(List<FixacaoRepartePdv> fixacaoRepartePdv) {
		this.fixacaoRepartePdv = fixacaoRepartePdv;
	}

	public EnderecoPDV getEnderecoEntrega(){
		for(EnderecoPDV item:this.getEnderecos()){
			if(item.getTipoEndereco()==TipoEndereco.LOCAL_ENTREGA){
				return item;
			}
		}
		return null;
	}
    
    public EnderecoPDV getEnderecoPrincipal(){
		for(EnderecoPDV item:this.getEnderecos()){
			if(item.isPrincipal()){
				return item;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getCota() == null) ? 0 : this.getCota().hashCode());
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
		PDV other = (PDV) obj;
		if (this.getCota() == null) {
			if (other.getCota() != null)
				return false;
		} else if (!this.getCota().equals(other.getCota()))
			return false;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PDV [id=" + id + ", dataInclusao=" + dataInclusao + ", cota="
				+ cota + ", status=" + status + ", enderecos=" + enderecos
				+ ", telefones=" + telefones + ", nome=" + nome + ", contato="
				+ contato + ", site=" + site + ", email=" + email
				+ ", pontoReferencia=" + pontoReferencia
				+ ", dentroOutroEstabelecimento=" + dentroOutroEstabelecimento
				+ ", arrendatario=" + arrendatario
				+ ", tipoEstabelecimentoPDV=" + tipoEstabelecimentoPDV
				+ ", tamanhoPDV=" + tamanhoPDV + ", possuiSistemaIPV="
				+ possuiSistemaIPV + ", qtdeFuncionarios=" + qtdeFuncionarios
				+ ", porcentagemFaturamento=" + porcentagemFaturamento
				+ ", licencaMunicipal=" + licencaMunicipal + ", periodos="
				+ periodos + ", caracteristicas=" + caracteristicas
				+ ", segmentacao=" + segmentacao + ", materiais=" + materiais
				+ ", expositor=" + expositor + ", tipoExpositor="
				+ tipoExpositor + ", geradorFluxoPDV=" + geradorFluxoPDV
				+ ", rotas=" + rotas + "]";
	}
}