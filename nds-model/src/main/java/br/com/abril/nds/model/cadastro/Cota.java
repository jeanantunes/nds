package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import br.com.abril.nds.model.planejamento.EstudoCota;

@Entity
@Table(name = "COTA")
@SequenceGenerator(name="COTA_SEQ", initialValue = 1, allocationSize = 1)
public class Cota {

	@Id
	@GeneratedValue(generator = "COTA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NUMERO_COTA", nullable = false, unique=true)
	private Integer numeroCota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PESSOA_ID")
	private Pessoa pessoa;
	
	@Column(name = "SUGERE_SUSPENSAO", nullable = false)
	private boolean sugereSuspensao;
	
	@OneToMany
	@JoinColumn(name = "COTA_ID")
	private List<PDV> pdvs = new ArrayList<PDV>();

	@Enumerated(EnumType.STRING)
	@Column(name = "SITUACAO_CADASTRO", nullable = false)
	private SituacaoCadastro situacaoCadastro;
	
	@Column(name  ="FATOR_DESCONTO")
	private BigDecimal fatorDesconto;
	
	@OneToMany(mappedBy = "cota")
	private Set<EnderecoCota> enderecos = new HashSet<EnderecoCota>();
	
	@Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@OneToOne(mappedBy = "cota")
	private ContratoCota contratoCota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BOX_ID")
	private Box box;
	
	@Cascade(value = org.hibernate.annotations.CascadeType.PERSIST)
	@OneToMany(mappedBy = "cota")
	private List<HistoricoSituacaoCota> historicos = new ArrayList<HistoricoSituacaoCota>();
	
	@OneToMany(mappedBy = "cota")
	private Set<EstudoCota> estudoCotas = new HashSet<EstudoCota>();
	
	@OneToOne(mappedBy = "cota")
	private ParametroCobrancaCota parametroCobranca;
	
	@OneToMany
	@JoinColumn( name="ID_COTA")
	private List<RotaRoteiroOperacao> rotaRoteiroOperacao;
	
	@ManyToOne
	@JoinColumn(name="ID_FIADOR")
	private Fiador fiador;
	
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
	
	public BigDecimal getFatorDesconto() {
		return fatorDesconto;
	}
	
	public void setFatorDesconto(BigDecimal fatorDesconto) {
		this.fatorDesconto = fatorDesconto;
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
	
	

	public List<RotaRoteiroOperacao> getRotaRoteiroOperacao() {
		return rotaRoteiroOperacao;
	}

	public void setRotaRoteiroOperacao(List<RotaRoteiroOperacao> rotaRoteiroOperacao) {
		this.rotaRoteiroOperacao = rotaRoteiroOperacao;
	}
	
	public Fiador getFiador() {
		return fiador;
	}

	public void setFiador(Fiador fiador) {
		this.fiador = fiador;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
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
}