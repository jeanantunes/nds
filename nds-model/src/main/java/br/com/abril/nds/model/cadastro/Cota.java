package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
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

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Cascade;

import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;

@Entity
@Table(name = "COTA")
@SequenceGenerator(name="COTA_SEQ", initialValue = 1, allocationSize = 1)
public class Cota implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4962347006238498224L;

	@Id
	@GeneratedValue(generator = "COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUMERO_COTA", nullable = false)
	private Integer numeroCota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	@Column(name = "SUGERE_SUSPENSAO", nullable = false)
	private boolean sugereSuspensao;
	
	@Column(name = "POSSUI_CONTRATO", nullable = false)
	private boolean possuiContrato;
	
	@OneToMany(mappedBy = "cota", cascade={CascadeType.REMOVE})
	private List<PDV> pdvs = new ArrayList<PDV>();

	@OneToMany(mappedBy = "cota")
	private List<ChamadaEncalheCota> chamadaEncalheCotas = new ArrayList<ChamadaEncalheCota>();

	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_CADASTRO", nullable = false)
	private SituacaoCadastro situacaoCadastro;
		
	@OneToMany(mappedBy = "cota")
	private Set<EnderecoCota> enderecos = new HashSet<EnderecoCota>();
	
	@OneToMany(mappedBy = "cota")
	private Set<TelefoneCota> telefones = new HashSet<TelefoneCota>();
	
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OneToOne(mappedBy = "cota")
	private ContratoCota contratoCota;
	
	@ManyToOne
	@JoinColumn(name = "BOX_ID")
	private Box box; 
	
	@Cascade(value = org.hibernate.annotations.CascadeType.PERSIST)
	@OneToMany(mappedBy = "cota")
	private List<HistoricoSituacaoCota> historicos = new ArrayList<HistoricoSituacaoCota>();
	
	@OneToMany(mappedBy = "cota")
	private Set<EstudoCota> estudoCotas = new HashSet<EstudoCota>();
	
	@OneToOne(mappedBy = "cota")
	private ParametroCobrancaCota parametroCobranca;
		
	@Embedded
	private ParametroDistribuicaoCota parametroDistribuicao;
	
	@ManyToOne(optional = true)
	@JoinColumn(name="ID_FIADOR")
	private Fiador fiador;
	
	@OneToMany(mappedBy = "cota")
	private Set<EstoqueProdutoCota> estoqueProdutoCotas = new HashSet<EstoqueProdutoCota>();

	@OneToMany(mappedBy = "cota", fetch=FetchType.LAZY)
	private Set<MovimentoEstoqueCota> movimentoEstoqueCotas = new HashSet<MovimentoEstoqueCota>();

	/**
	 * Data de início de atividade da cota
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "INICIO_ATIVIDADE", nullable = false)
	private Date inicioAtividade;
	
	/**
	 * Data de início do titular da cota
	 */
	@Temporal(TemporalType.DATE)
    @Column(name = "INICIO_TITULARIDADE", nullable = false)
	private Date inicioTitularidade;
	
	/**
	 * Fornecedores associados à Cota
	 */
	@ManyToMany
	@JoinTable(name = "COTA_FORNECEDOR", joinColumns = {@JoinColumn(name = "COTA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "FORNECEDOR_ID")})
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CLASSIFICACAO_ESPECTATIVA_FATURAMENTO")
	private ClassificacaoEspectativaFaturamento classificacaoEspectativaFaturamento;
	
	@Embedded
	private ParametrosCotaNotaFiscalEletronica parametrosCotaNotaFiscalEletronica;
	
	@OneToOne(mappedBy = "cota",cascade={CascadeType.REMOVE})
	private BaseReferenciaCota baseReferenciaCota;
	
	@OneToMany(mappedBy = "cota",cascade={CascadeType.REMOVE})
	private Set<SocioCota> sociosCota = new HashSet<SocioCota>();
	
	@OneToMany(mappedBy="cota", cascade={CascadeType.REMOVE})
	private Set<HistoricoNumeroCota> historicoNumeroCota;
	
	@OneToMany(mappedBy="cota", cascade={CascadeType.REMOVE})
	private Set<DescontoProdutoEdicao> descontosProdutoEdicao = new HashSet<DescontoProdutoEdicao>();

	@ManyToMany(mappedBy="cotas", targetEntity=GrupoCota.class)
	private Set<GrupoCota> grupos;
	
	/**
	 * Histórico de titulares da cota
	 */
	@OneToMany(mappedBy = "cota", cascade = {CascadeType.ALL})
	private Set<HistoricoTitularidadeCota> titularesCota =new HashSet<HistoricoTitularidadeCota>();
	
	/**
	 * Referente a garantias da cota.
	 */
	@OneToOne(mappedBy="cota", fetch=FetchType.LAZY)
	private CotaGarantia cotaGarantia;
		
	public Cota() {
        this.inicioAtividade = new Date();
        this.inicioTitularidade = new Date();
    }
	
	public Set<HistoricoNumeroCota> getHistoricoNumeroCota() {
		return historicoNumeroCota;
	}

	public void setHistoricoNumeroCota(Set<HistoricoNumeroCota> historicoNumeroCota) {
		this.historicoNumeroCota = historicoNumeroCota;
	}
	
	/**
	 * @return the descontosProdutoEdicao
	 */
	public Set<DescontoProdutoEdicao> getDescontosProdutoEdicao() {
		return descontosProdutoEdicao;
	}

	/**
	 * @param descontosProdutoEdicao the descontosProdutoEdicao to set
	 */
	public void setDescontosProdutoEdicao(
			Set<DescontoProdutoEdicao> descontosProdutoEdicao) {
		this.descontosProdutoEdicao = descontosProdutoEdicao;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public boolean isSugereSuspensao() {
		return sugereSuspensao;
	}
	
	public void setSugereSuspensao(boolean sugereSuspensao) {
		this.sugereSuspensao = sugereSuspensao;
	}
	
	public boolean isPossuiContrato() {
		return possuiContrato;
	}

	public void setPossuiContrato(boolean possuiContrato) {
		this.possuiContrato = possuiContrato;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<PDV> getPdvs() {
		return pdvs;
	}
	
	public void setPdvs(List<PDV> pdvs) {
		this.pdvs = pdvs;
	}
	
	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}
	
	public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
		
	public Set<EnderecoCota> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(Set<EnderecoCota> enderecos) {
		this.enderecos = enderecos;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}
	
	public ContratoCota getContratoCota() {
		return contratoCota;
	}
	
	public void setContratoCota(ContratoCota contratoCota) {
		this.contratoCota = contratoCota;
	}
	
	public List<HistoricoSituacaoCota> getHistoricos() {
		return historicos;
	}
	
	public void setHistoricos(List<HistoricoSituacaoCota> historicos) {
		this.historicos = historicos;
	}

	public Set<EstudoCota> getEstudoCotas() {
		return estudoCotas;
	}

	public void setEstudoCotas(Set<EstudoCota> estudoCotas) {
		this.estudoCotas = estudoCotas;
	}
	
	public ParametroCobrancaCota getParametroCobranca() {
		return parametroCobranca;
	}
	
	public void setParametroCobranca(ParametroCobrancaCota parametroCobranca) {
		this.parametroCobranca = parametroCobranca;
	}

	public Fiador getFiador() {
		return fiador;
	}

	public void setFiador(Fiador fiador) {
		this.fiador = fiador;
	}
	
	public Set<EstoqueProdutoCota> getEstoqueProdutoCotas() {
		return estoqueProdutoCotas;
	}

	public void setEstoqueProdutoCotas(Set<EstoqueProdutoCota> estoqueProdutoCotas) {
		this.estoqueProdutoCotas = estoqueProdutoCotas;
	}
	
	public Date getInicioAtividade() {
		return inicioAtividade;
	}
	
	public void setInicioAtividade(Date inicioAtividade) {
		this.inicioAtividade = inicioAtividade;
	}
	
    public Date getInicioTitularidade() {
        return inicioTitularidade;
    }

    public void setInicioTitularidade(Date inicioTitularidade) {
        this.inicioTitularidade = inicioTitularidade;
    }

    public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Set<TelefoneCota> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<TelefoneCota> telefones) {
		this.telefones = telefones;
	}
	
	/**
	 * @return the parametrosCotaNotaFiscalEletronica
	 */
	public ParametrosCotaNotaFiscalEletronica getParametrosCotaNotaFiscalEletronica() {
		return parametrosCotaNotaFiscalEletronica;
	}

	/**
	 * @param parametrosCotaNotaFiscalEletronica the parametrosCotaNotaFiscalEletronica to set
	 */
	public void setParametrosCotaNotaFiscalEletronica(
			ParametrosCotaNotaFiscalEletronica parametrosCotaNotaFiscalEletronica) {
		this.parametrosCotaNotaFiscalEletronica = parametrosCotaNotaFiscalEletronica;
	}

	/**
	 * @return the classificacaoEspectativaFaturamento
	 */
	public ClassificacaoEspectativaFaturamento getClassificacaoEspectativaFaturamento() {
		return classificacaoEspectativaFaturamento;
	}

	/**
	 * @param classificacaoEspectativaFaturamento the classificacaoEspectativaFaturamento to set
	 */
	public void setClassificacaoEspectativaFaturamento(
			ClassificacaoEspectativaFaturamento classificacaoEspectativaFaturamento) {
		this.classificacaoEspectativaFaturamento = classificacaoEspectativaFaturamento;
	}
	
	/**
	 * @return the baseReferenciaCota
	 */
	public BaseReferenciaCota getBaseReferenciaCota() {
		return baseReferenciaCota;
	}

	/**
	 * @param baseReferenciaCota the baseReferenciaCota to set
	 */
	public void setBaseReferenciaCota(BaseReferenciaCota baseReferenciaCota) {
		this.baseReferenciaCota = baseReferenciaCota;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cota other = (Cota) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the parametroDistribuicao
	 */
	public ParametroDistribuicaoCota getParametroDistribuicao() {
		return parametroDistribuicao;
	}

	/**
	 * @param parametroDistribuicao the parametroDistribuicao to set
	 */
	public void setParametroDistribuicao(ParametroDistribuicaoCota parametroDistribuicao) {
		this.parametroDistribuicao = parametroDistribuicao;
	}

	/**
	 * @return the sociosCota
	 */
	public Set<SocioCota> getSociosCota() {
		return sociosCota;
	}

	/**
	 * @param sociosCota the sociosCota to set
	 */
	public void setSociosCota(Set<SocioCota> sociosCota) {
		this.sociosCota = sociosCota;
	}

	public Set<MovimentoEstoqueCota> getMovimentoEstoqueCotas() {
		return movimentoEstoqueCotas;
	}

	public void setMovimentoEstoqueCotas(Set<MovimentoEstoqueCota> movimentoEstoqueCotas) {
		this.movimentoEstoqueCotas = movimentoEstoqueCotas;
	}

	/**
	 * @return the grupos
	 */
	public Set<GrupoCota> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos the grupos to set
	 */
	public void setGrupos(Set<GrupoCota> grupos) {
		this.grupos = grupos;
	}

    /**
     * @return the titularesCota
     */
    public Set<HistoricoTitularidadeCota> getTitularesCota() {
        return titularesCota;
    }

    /**
     * @param titularesCota the titularesCota to set
     */
    public void setTitularesCota(Set<HistoricoTitularidadeCota> titularesCota) {
        this.titularesCota = titularesCota;
    }
    
    /**
     * Adiciona um novo histórico de titularidade da cota
     * 
     * @param titularCota
     *            histórico de titularidade da cota para adição
     * @throws IllegalArgumentException
     *             caso o histórico de titularidade da cota seja nulo
     */
    public void addTitularCota(HistoricoTitularidadeCota titularCota) {
        Validate.notNull(titularCota, "Titular da Cota não deve ser nulo!");
        if (titularesCota == null) {
            titularesCota = new HashSet<HistoricoTitularidadeCota>();
        }
        titularesCota.add(titularCota);
    }
    
    /**
     * Verifica se o titular da cota é uma pessoa física
     * 
     * @return true se o titular é pessoa física, false caso contrário
     */
    public boolean isTitularPessoaFisica() {
        return pessoa instanceof PessoaFisica;
    }

	/**
	 * @return the cotaGarantia
	 */
	public CotaGarantia getCotaGarantia() {
		return cotaGarantia;
	}

	/**
	 * @param cotaGarantia the cotaGarantia to set
	 */
	public void setCotaGarantia(CotaGarantia cotaGarantia) {
		this.cotaGarantia = cotaGarantia;
	}
	
	public EnderecoCota getEnderecoPrincipal(){
		for(EnderecoCota item:this.getEnderecos()){
			if(item.isPrincipal()){
				return item;
			}
		}
		return null;
	}
}
