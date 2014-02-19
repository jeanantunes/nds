package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;

@Entity
@Table(name="DISTRIBUIDOR_NOTA_FISCAL_TIPOS")
public class DistribuidorTipoNotaFiscal implements Serializable {

	private static final long serialVersionUID = 4479735364063319531L;

	public enum DistribuidorGrupoNotaFiscal {
		
		NOTA_FISCAL_ENVIO_PARA_COTA,
		NOTA_FISCAL_DEVOLUCAO_PELA_COTA,
		NOTA_FISCAL_VENDA,
		NOTA_FISCAL_DEVOLUCAO_AO_FORNECEDOR,
		NOTA_FISCAL_SIMBOLICA_VENDA_FORNECEDOR
		
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Column(name="NOME_CAMPO_TELA")
	private String nomeCampoTela;
	
	@Enumerated(EnumType.STRING)
	@Column(name="GRUPO_NOTA_FISCAL")
	private DistribuidorGrupoNotaFiscal grupoNotaFiscal;
	
	@OneToOne
	@JoinColumn(name="NOTA_FISCAL_TIPO_EMISSAO_ID")
	private NotaFiscalTipoEmissao tipoEmissao;
	
	@OneToMany
	@JoinTable(
	            name="DISTRIBUIDOR_NOTA_FISCAL_TIPOS_TIPO_EMISSAO",
	            joinColumns={
	            		@JoinColumn(table="DISTRIBUIDOR_NOTA_FISCAL_TIPOS", name="NOTA_FISCAL_TIPO_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(table="DISTRIBUIDOR_NOTA_FISCAL_TIPO_EMISSAO",name="NOTA_FISCAL_TIPO_EMISSAO_ID", referencedColumnName="id"))
	@OrderBy("sequencia")
	private List<NotaFiscalTipoEmissao> tipoEmissaoDisponiveis;
	
	@OneToOne
	@JoinColumn(name="DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	@OneToMany
	@JoinTable(
	            name="DISTRIBUIDOR_TIPO_NOTA_NATUREZA_OPERACAO",
	            joinColumns={
	            		@JoinColumn(table="DISTRIBUIDOR_NOTA_FISCAL_TIPOS", name="TIPO_NOTA_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(table="NATUREZA_OPERACAO",name="NATUREZA_OPERACAO_ID", referencedColumnName="id"))
	private List<NaturezaOperacao> naturezaOperacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeCampoTela() {
		return nomeCampoTela;
	}

	public void setNomeCampoTela(String nomeCampoTela) {
		this.nomeCampoTela = nomeCampoTela;
	}

	public DistribuidorGrupoNotaFiscal getGrupoNotaFiscal() {
		return grupoNotaFiscal;
	}

	public void setGrupoNotaFiscal(DistribuidorGrupoNotaFiscal grupoNotaFiscal) {
		this.grupoNotaFiscal = grupoNotaFiscal;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public NotaFiscalTipoEmissao getTipoEmissao() {
		return tipoEmissao;
	}

	public void setTipoEmissao(NotaFiscalTipoEmissao tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}

	public List<NotaFiscalTipoEmissao> getTipoEmissaoDisponiveis() {
		return tipoEmissaoDisponiveis;
	}

	public void setTipoEmissaoDisponiveis(
			List<NotaFiscalTipoEmissao> tipoEmissaoDisponiveis) {
		this.tipoEmissaoDisponiveis = tipoEmissaoDisponiveis;
	}

	public List<NaturezaOperacao> getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(List<NaturezaOperacao> naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

}