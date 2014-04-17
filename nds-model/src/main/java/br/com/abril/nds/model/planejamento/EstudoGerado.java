package br.com.abril.nds.model.planejamento;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.*;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESTUDO_GERADO")
public class EstudoGerado implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstudoGerado.class);

    @Id
	@Column(name = "ID")
	protected Long id;
	
	@Column(name = "QTDE_REPARTE", nullable = false)
	protected BigInteger qtdeReparte;

	@Column(name = "DATA_LANCAMENTO")
	@Temporal(TemporalType.DATE)
	protected Date dataLancamento;

	@ManyToOne
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	protected ProdutoEdicao produtoEdicao;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "estudo", fetch = FetchType.LAZY)
	protected Set<Lancamento> lancamentos = new HashSet<Lancamento>();
	
	@Column(name = "LANCAMENTO_ID")
	protected Long lancamentoID;
	
	@Cascade(value={CascadeType.SAVE_UPDATE, CascadeType.PERSIST, CascadeType.DELETE})
	@OneToMany(mappedBy = "estudo", fetch = FetchType.LAZY)
	protected Set<EstudoCotaGerado> estudoCotas = new HashSet<EstudoCotaGerado>();
	
	/** Status do Estudo. */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	protected StatusLancamento status;
	
	/** Data de cadastro do Estudo no sistema. */
	@Column(name = "DATA_CADASTRO", nullable = false)
	@Temporal(TemporalType.DATE)
	protected Date dataCadastro;
	
	/** Data de alteração do Estudo no sistema. */
	@Column(name = "DATA_ALTERACAO")
	@Temporal(TemporalType.DATE)
	protected Date dataAlteracao;

	@Column(name = "REPARTE_DISTRIBUIR")
	protected BigInteger reparteDistribuir;
	
	@Column(name = "SOBRA")
	protected BigInteger sobra;
	
	@Column(name = "DISTRIBUICAO_POR_MULTIPLOS")
	protected Integer distribuicaoPorMultiplos; //TODO no estudo usa boolean, verificar alteração
	
	@Column(name = "PACOTE_PADRAO")
	protected BigInteger pacotePadrao; //TODO BigDecimal
	
	@ManyToOne (optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_ID")
	protected Usuario usuario;

    @Column(name = "ESTUDO_ORIGEM_COPIA")
    protected Long idEstudoOrigemCopia; //Estudo usado para gerar copia proporcional
    
    @Column(name = "LIBERADO")
	private Boolean liberado;

    @Column(name = "DADOS_VENDA_MEDIA")
    @Type(type = "text")
    private String dadosVendaMedia;
    
    @Column(name = "REPARTE_MINIMO")
    private BigInteger reparteMinimo;

    public BigInteger getQtdeReparte() {
		return qtdeReparte;
	}
	
	public void setQtdeReparte(BigInteger qtdeReparte) {
		this.qtdeReparte = qtdeReparte;
	}
	
	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public Lancamento getLancamento() {
		return lancamentos.isEmpty() ? null : lancamentos.iterator().next();
	}
	
	/**
	 * @return the estudoCotas
	 */
	public Set<EstudoCotaGerado> getEstudoCotas() {
		return estudoCotas;
	}

	/**
	 * @param estudoCotas the estudoCotas to set
	 */
	public void setEstudoCotas(Set<EstudoCotaGerado> estudoCotas) {
		this.estudoCotas = estudoCotas;
	}

	public StatusLancamento getStatus() {
		return status;
	}

	public void setStatus(StatusLancamento status) {
		this.status = status;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Set<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(Set<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public BigInteger getReparteDistribuir() {
		return reparteDistribuir;
	}

	public void setReparteDistribuir(BigInteger reparteDistribuir) {
		this.reparteDistribuir = reparteDistribuir;
	}

	public Integer getDistribuicaoPorMultiplos() {
		return distribuicaoPorMultiplos;
	}

	public void setDistribuicaoPorMultiplos(Integer distribuicaoPorMultiplos) {
		this.distribuicaoPorMultiplos = distribuicaoPorMultiplos;
	}

	public BigInteger getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(BigInteger pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public Usuario getUsuarioId() {
		return usuario;
	}

	public void setUsuarioId(Usuario usuarioId) {
		this.usuario = usuarioId;
	}

	public Long getLancamentoID() {
		return lancamentoID;
	}

	public void setLancamentoID(Long lancamentoID) {
		this.lancamentoID = lancamentoID;
	}

	public BigInteger getSobra() {
		return sobra;
	}

	public void setSobra(BigInteger sobra) {
		this.sobra = sobra;
	}

    public Long getIdEstudoOrigemCopia() {
        return idEstudoOrigemCopia;
    }

    public void setIdEstudoOrigemCopia(Long idEstudoOrigemCopia) {
        this.idEstudoOrigemCopia = idEstudoOrigemCopia;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isLiberado() {
		return liberado;
	}

	public void setLiberado(Boolean liberado) {
		this.liberado = liberado;
	}

    public void setDadosVendaMedia(DistribuicaoVendaMediaDTO dadosVendaMedia) {
        if (dadosVendaMedia != null) {
            ObjectMapper mapper = new ObjectMapper();
            String valueAsString = null;
            try {
                valueAsString = mapper.writeValueAsString(dadosVendaMedia);
            } catch (IOException e) {
                LOGGER.info("Serialization error.", e);
            }
            this.dadosVendaMedia = valueAsString;
        }
    }

    public void setDadosVendaMedia(String dadosVendaMedia) {
        this.dadosVendaMedia = dadosVendaMedia;
    }

    public String getDadosVendaMedia() {
        return dadosVendaMedia;
    }

	public BigInteger getReparteMinimo() {
		return reparteMinimo;
	}

	public void setReparteMinimo(BigInteger reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getEstudoCotas() == null) ? 0 : this.getEstudoCotas().hashCode());
		result = prime * result + ((this.getIdEstudoOrigemCopia() == null) ? 0 : this.getIdEstudoOrigemCopia().hashCode());
		result = prime * result + ((this.getLancamentos() == null) ? 0 : this.getLancamentos().hashCode());
		result = prime * result + ((this.getProdutoEdicao() == null) ? 0 : this.getProdutoEdicao().hashCode());
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
		EstudoGerado other = (EstudoGerado) obj;
		if (this.getEstudoCotas() == null) {
			if (other.getEstudoCotas() != null)
				return false;
		} else if (!this.getEstudoCotas().equals(other.getEstudoCotas()))
			return false;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getIdEstudoOrigemCopia() == null) {
			if (other.getIdEstudoOrigemCopia() != null)
				return false;
		} else if (!this.getIdEstudoOrigemCopia().equals(other.getIdEstudoOrigemCopia()))
			return false;
		if (this.getLancamentos() == null) {
			if (other.getLancamentos() != null)
				return false;
		} else if (!this.getLancamentos().equals(other.getLancamentos()))
			return false;
		if (this.getProdutoEdicao() == null) {
			if (other.getProdutoEdicao() != null)
				return false;
		} else if (!this.getProdutoEdicao().equals(other.getProdutoEdicao()))
			return false;
		return true;
	}
}

