package br.com.abril.nds.model.distribuicao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "FIXACAO_REPARTE")
@SequenceGenerator(name="FIXREP_SEQ", initialValue = 1, allocationSize = 1)
public class FixacaoReparte {
	
	@Id
	@GeneratedValue(generator = "FIXREP_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ID_COTA")
	private Cota cotaFixada;
	
	@ManyToOne
	@JoinColumn(name = "ID_PRODUTO")
	private Produto produtoFixado;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;
	
	@Column(name="QTDE_EDICOES")
	private Integer qtdeEdicoes;
	
	@Column(name="QTDE_EXEMPLARES")
	private Integer qtdeExemplares;
	
	@Column(name="DATA_HORA")
	private Date dataHora;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cota getCotaFixada() {
		return cotaFixada;
	}

	public void setCotaFixada(Cota cotaFixada) {
		this.cotaFixada = cotaFixada;
	}

	public Produto getProdutoFixado() {
		return produtoFixado;
	}

	public void setProdutoFixado(Produto produtoFixado) {
		this.produtoFixado = produtoFixado;
	}


	public Integer getQtdeEdicoes() {
		return qtdeEdicoes;
	}

	public void setQtdeEdicoes(Integer qtdeEdicoes) {
		this.qtdeEdicoes = qtdeEdicoes;
	}

	public Integer getQtdeExemplares() {
		return qtdeExemplares;
	}

	public void setQtdeExemplares(Integer qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	

}

