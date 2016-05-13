package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DISTRIBUIDOR_GRID_DISTRIBUICAO")
@SequenceGenerator(name = "DISTRIBUIDOR_GRID_DISTRIBUICAO_SEQ", initialValue = 1, allocationSize = 1)
public class DistribuidorGridDistribuicao implements Serializable {

	private static final long serialVersionUID = -912490741322143829L;

	@Id
	@GeneratedValue(generator = "DISTRIBUIDOR_GRID_DISTRIBUICAO_SEQ")
	@Column(name = "ID")
	private Long id;

	@OneToOne
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	@Column(name = "GERACAO_AUTOMATICA_ESTUDO")
	private boolean geracaoAutomaticaEstudo;
	
	@Column(name = "EXIBIR_INFORMACOES_REPARTE_COMPLEMENTAR")
	private boolean exibirInformacoesReparteComplementar;
	
	@Column(name = "VENDA_MEDIA_MAIS", nullable = false)
	private Integer vendaMediaMais;
	
	@Column(name = "PRACA_VERANEIO")
	private boolean pracaVeraneio;
	
	@Column(name = "COMPLEMENTAR_AUTOMATICO")
	private boolean complementarAutomatico;
	
	@Column(name = "PERCENTUAL_MAXIMO_FIXACAO")
	private Integer percentualMaximoFixacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public boolean isGeracaoAutomaticaEstudo() {
		return geracaoAutomaticaEstudo;
	}

	public void setGeracaoAutomaticaEstudo(boolean geracaoAutomaticaEstudo) {
		this.geracaoAutomaticaEstudo = geracaoAutomaticaEstudo;
	}

	public Integer getVendaMediaMais() {
		return vendaMediaMais;
	}

	public void setVendaMediaMais(Integer vendaMediaMais) {
		this.vendaMediaMais = vendaMediaMais;
	}

	public boolean isPracaVeraneio() {
		return pracaVeraneio;
	}

	public void setPracaVeraneio(boolean pracaVeraneio) {
		this.pracaVeraneio = pracaVeraneio;
	}

	public boolean isComplementarAutomatico() {
		return complementarAutomatico;
	}

	public void setComplementarAutomatico(boolean complementarAutomatico) {
		this.complementarAutomatico = complementarAutomatico;
	}

	public Integer getPercentualMaximoFixacao() {
		return percentualMaximoFixacao;
	}

	public void setPercentualMaximoFixacao(Integer percentualMaximoFixacao) {
		this.percentualMaximoFixacao = percentualMaximoFixacao;
	}

	public boolean isExibirInformacoesReparteComplementar() {
		return exibirInformacoesReparteComplementar;
	}

	public void setExibirInformacoesReparteComplementar(boolean exibirInformacoesReparteComplementar) {
		this.exibirInformacoesReparteComplementar = exibirInformacoesReparteComplementar;
	}
	
	
}
