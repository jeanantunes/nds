package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.export.Exportable;

@Exportable
@Entity
@Table(name = "EXCECAO_PRODUTO_COTA")
@SequenceGenerator(name = "EXCECAO_PRODUTO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ExcecaoProdutoCota implements Serializable {

	private static final long serialVersionUID = 2098242224037769932L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "EXCECAO_PRODUTO_COTA_SEQ")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID", nullable = true)
	private Cota cota;

	@ManyToOne(optional = true)
	@JoinColumn(name = "PRODUTO_ID", nullable = true)
	private Produto produto;

	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	@ManyToOne(optional = true)
	@JoinColumn(name = "TIPO_CLASSIFICACAO_PRODUTO_ID")
	private TipoClassificacaoProduto tipoClassificacaoProduto;
	
	@Column(name = "CODIGO_ICD", nullable = false)
	private String codigoICD;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ALTERACAO")
	private Date dataAlteracao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_EXCECAO", nullable = false)
	private TipoExcecao tipoExcecao;

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

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
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

	public TipoExcecao getTipoExcecao() {
		return tipoExcecao;
	}

	public void setTipoExcecao(TipoExcecao tipoExcecao) {
		this.tipoExcecao = tipoExcecao;
	}

	public TipoClassificacaoProduto getTipoClassificacaoProduto() {
		return tipoClassificacaoProduto;
	}

	public void setTipoClassificacaoProduto(
			TipoClassificacaoProduto tipoClassificacaoProduto) {
		this.tipoClassificacaoProduto = tipoClassificacaoProduto;
	}

	public String getCodigoICD() {
		return codigoICD;
	}

	public void setCodigoICD(String codigoICD) {
		this.codigoICD = codigoICD;
	}

}
