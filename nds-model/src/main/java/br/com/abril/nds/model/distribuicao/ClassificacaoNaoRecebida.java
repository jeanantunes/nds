package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "CLASSIFICACAO_NAO_RECEBIDA")
@SequenceGenerator(name = "CLASSIFICACAO_NAO_RECEBIDA_SEQ", initialValue = 1, allocationSize = 1)
public class ClassificacaoNaoRecebida implements Serializable {

	private static final long serialVersionUID = -8285198462735567701L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "CLASSIFICACAO_NAO_RECEBIDA_SEQ")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_CLASSIFICACAO_PRODUTO_ID")
	private TipoClassificacaoProduto tipoClassificacaoProduto;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;

	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ALTERACAO")
	private Date dataAlteracao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoClassificacaoProduto getTipoClassificacaoProduto() {
		return tipoClassificacaoProduto;
	}

	public void setTipoClassificacaoProduto(
			TipoClassificacaoProduto tipoClassificacaoProduto) {
		this.tipoClassificacaoProduto = tipoClassificacaoProduto;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	
}
