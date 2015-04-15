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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO")
@SuppressWarnings("serial")
public class Estudo implements Serializable  {

    private static final Logger LOGGER = LoggerFactory.getLogger(Estudo.class);

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
	protected Set<EstudoCota> estudoCotas = new HashSet<EstudoCota>();
	
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

    @Column(name = "DADOS_VENDA_MEDIA")
    @Type(type = "text")
    protected String dadosVendaMedia;

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
	public Set<EstudoCota> getEstudoCotas() {
		return estudoCotas;
	}

	/**
	 * @param estudoCotas the estudoCotas to set
	 */
	public void setEstudoCotas(Set<EstudoCota> estudoCotas) {
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
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		Estudo other = (Estudo) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getEstudoCotas() == null) {
			if (other.getEstudoCotas() != null)
				return false;
		} else if (!this.getEstudoCotas().equals(other.getEstudoCotas()))
			return false;
		return true;
	}

}
