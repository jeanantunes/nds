package br.com.abril.nds.model.movimentacao;

import java.math.BigDecimal;
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
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "MOVIMENTO_COTA")
@SequenceGenerator(name="MOVIMENTO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class MovimentoCota {
	
	@Id
	@GeneratedValue(generator = "MOVIMENTO_COTA_SEQ")
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_MOVIMENTO", nullable = false)
	private Date dataMovimento;
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	@Column(name = "QTDE", nullable = false)
	private BigDecimal qtde;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_MOVIMENTO_COTA", nullable = false)
	private TipoMovimentoCota tipoMovimentoCota;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataMovimento() {
		return dataMovimento;
	}
	
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public BigDecimal getQtde() {
		return qtde;
	}
	
	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}
	
	public TipoMovimentoCota getTipoMovimentoCota() {
		return tipoMovimentoCota;
	}
	
	public void setTipoMovimentoCota(TipoMovimentoCota tipoMovimentoCota) {
		this.tipoMovimentoCota = tipoMovimentoCota;
	}

}
