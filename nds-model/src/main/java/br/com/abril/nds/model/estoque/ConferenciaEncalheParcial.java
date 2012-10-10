package br.com.abril.nds.model.estoque;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;
/**
 * Entidade que abstrai informações "inputadas" na funcionalidade 
 * digitação contagem devolução.
 * 
 * @author Discover Technology
 */
@Entity
@Table(name = "CONFERENCIA_ENC_PARCIAL")
@SequenceGenerator(name="CONFERENCIA_ENC_PARCIAL_SEQ", initialValue = 1, allocationSize = 1)
public class ConferenciaEncalheParcial {

	@Id
	@GeneratedValue(generator = "CONFERENCIA_ENC_PARCIAL_SEQ")
	private Long id;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_MOVIMENTO", nullable = false)
	private Date dataMovimento;
	
	@Column(name = "DATA_CONF_ENC_PARCIAL", nullable = false)
	private Date dataConfEncalheParcial;
	
	@Column(name = "DATA_APROVACAO")
	private Date dataAprovacao;
	
	@Column(name="STATUS_APROVACAO", nullable=false)
	private StatusAprovacao statusAprovacao;
	
	@Column(name = "DIFERENCA_APURADA")
	private Boolean diferencaApurada = false;

	@Column(name = "NF_PARCIAL_GERADA")
	private Boolean nfParcialGerada = false;
	
	@Column(name = "QTDE", nullable = false)
	private BigInteger qtde;
	
	@ManyToOne(optional = true)
	private Usuario responsavel;
	
	@ManyToOne(optional = true)
	private ProdutoEdicao produtoEdicao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataConfEncalheParcial() {
		return dataConfEncalheParcial;
	}

	public void setDataConfEncalheParcial(Date dataConfEncalheParcial) {
		this.dataConfEncalheParcial = dataConfEncalheParcial;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public StatusAprovacao getStatusAprovacao() {
		return statusAprovacao;
	}

	public void setStatusAprovacao(StatusAprovacao statusAprovacao) {
		this.statusAprovacao = statusAprovacao;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	public Boolean getDiferencaApurada() {
		return diferencaApurada;
	}

	public void setDiferencaApurada(Boolean diferencaApurada) {
		this.diferencaApurada = diferencaApurada;
	}

	public Boolean getNfParcialGerada() {
		return nfParcialGerada;
	}

	public void setNfParcialGerada(Boolean nfParcialGerada) {
		this.nfParcialGerada = nfParcialGerada;
	}
	
}
