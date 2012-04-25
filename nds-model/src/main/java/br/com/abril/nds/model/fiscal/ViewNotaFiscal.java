package br.com.abril.nds.model.fiscal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "VIEW_NOTA_FISCAL")
public class ViewNotaFiscal implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ViewNotaFiscalPK pk = new ViewNotaFiscalPK();
	
	public static class ViewNotaFiscalPK implements Serializable{
		
		@Column(name = "NOTAS_DE")
		private String notasDe;
		
		@Column(name = "DATA_EMISSAO")
		private Date dataEmissao;
		
		@Column(name = "NUMERO")
		private String numero;
		
		@Column(name = "SERIE")
		private String serie;

		/**
		 * Obtém notasDe
		 *
		 * @return String
		 */
		public String getNotasDe() {
			return notasDe;
		}

		/**
		 * Atribuí notasDe
		 * @param notasDe 
		 */
		public void setNotasDe(String notasDe) {
			this.notasDe = notasDe;
		}

		/**
		 * Obtém dataEmissao
		 *
		 * @return Date
		 */
		public Date getDataEmissao() {
			return dataEmissao;
		}

		/**
		 * Atribuí dataEmissao
		 * @param dataEmissao 
		 */
		public void setDataEmissao(Date dataEmissao) {
			this.dataEmissao = dataEmissao;
		}

		/**
		 * Obtém numero
		 *
		 * @return String
		 */
		public String getNumero() {
			return numero;
		}

		/**
		 * Atribuí numero
		 * @param numero 
		 */
		public void setNumero(String numero) {
			this.numero = numero;
		}

		/**
		 * Obtém serie
		 *
		 * @return String
		 */
		public String getSerie() {
			return serie;
		}

		/**
		 * Atribuí serie
		 * @param serie 
		 */
		public void setSerie(String serie) {
			this.serie = serie;
		}
		
		
	}


	@Column(name = "PESSOA_ID")
	private Long idPessoa;

	@Column(name = "TIPO")
	private String tipo;
	
	@Column(name = "ORIGEM")
	private String origem;
	
	@Column(name = "USUARIO_ID")
	private Long idUsuario;
	
	@Column(name = "STATUS_NOTA_FISCAL")
	private String statusNotaFiscal;
	
	@Column(name = "COTA_ID")
	private Long idCota;
	
	@Column(name = "FORNECEDOR_ID")
	private Long idFornecedor;
	

	
	@Column(name = "DATA_EXPEDICAO")
	private Date dataExpedicao;
	

	
	@Column(name = "CHAVE_ACESSO")
	private String chaveAcesso;
	
	@Column(name = "VALOR_BRUTO")
	private BigDecimal valorBruto;
	
	@Column(name = "VALOR_LIQUIDO")
	private BigDecimal valorLiquido;
	
	@Column(name = "VALOR_DESCONTO")
	private BigDecimal valorDesconto;	
	
	@Column(name = "CFOP_ID")
	private Long idCfop;
	
	@Column(name = "PJ_ID")
	private Long idEmitente;
	
	@Column(name = "TIPO_NF_ID")
	private Long idTipoNotaFiscal;
	
	/**
	 * Status de emissão da nota fiscal, deve ser
	 * preenchido qdo a nota for emitida pelo distribuidor
	 */
	@Column(name = "STATUS_EMISSAO")
	private String statusEmissao;
	
	/**
	 * Flag indicando se a nota foi emitida pelo distribuidor ou
	 * recebida de terceiros
	 * true indica que a nota foi emitida pelo distribuidor
	 * false que a nota foi recebida de terceiros
	 */
	@Column(name = "EMITIDA")
	private boolean emitida;

	
	@Column(name = "TIPO_EMISSAO_NFE")
	private String tipoEmissaoNfe;
	
	/**
	 * Campo com a descrição do ocorrido na 
	 * integração da nfe.
	 */
	@Column(name="MOVIMENTO_INTEGRACAO")
	private String movimentoIntegracao;
	
	@Column(name="STATUS_EMISSAO_NFE") 
	private String statusEmissaoNfe;

	/**
	 * Obtém pk
	 *
	 * @return ViewNotaFiscalPK
	 */
	public ViewNotaFiscalPK getPk() {
		return pk;
	}

	/**
	 * Atribuí pk
	 * @param pk 
	 */
	public void setPk(ViewNotaFiscalPK pk) {
		this.pk = pk;
	}

	/**
	 * Obtém idPessoa
	 *
	 * @return Long
	 */
	public Long getIdPessoa() {
		return idPessoa;
	}

	/**
	 * Atribuí idPessoa
	 * @param idPessoa 
	 */
	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	/**
	 * Obtém tipo
	 *
	 * @return String
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Atribuí tipo
	 * @param tipo 
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Obtém origem
	 *
	 * @return String
	 */
	public String getOrigem() {
		return origem;
	}

	/**
	 * Atribuí origem
	 * @param origem 
	 */
	public void setOrigem(String origem) {
		this.origem = origem;
	}

	/**
	 * Obtém idUsuario
	 *
	 * @return Long
	 */
	public Long getIdUsuario() {
		return idUsuario;
	}

	/**
	 * Atribuí idUsuario
	 * @param idUsuario 
	 */
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * Obtém statusNotaFiscal
	 *
	 * @return String
	 */
	public String getStatusNotaFiscal() {
		return statusNotaFiscal;
	}

	/**
	 * Atribuí statusNotaFiscal
	 * @param statusNotaFiscal 
	 */
	public void setStatusNotaFiscal(String statusNotaFiscal) {
		this.statusNotaFiscal = statusNotaFiscal;
	}

	/**
	 * Obtém idCota
	 *
	 * @return Long
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * Atribuí idCota
	 * @param idCota 
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
	 * Obtém idFornecedor
	 *
	 * @return Long
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * Atribuí idFornecedor
	 * @param idFornecedor 
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * Obtém dataExpedicao
	 *
	 * @return Date
	 */
	public Date getDataExpedicao() {
		return dataExpedicao;
	}

	/**
	 * Atribuí dataExpedicao
	 * @param dataExpedicao 
	 */
	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	/**
	 * Obtém chaveAcesso
	 *
	 * @return String
	 */
	public String getChaveAcesso() {
		return chaveAcesso;
	}

	/**
	 * Atribuí chaveAcesso
	 * @param chaveAcesso 
	 */
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	/**
	 * Obtém valorBruto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	/**
	 * Atribuí valorBruto
	 * @param valorBruto 
	 */
	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	/**
	 * Obtém valorLiquido
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	/**
	 * Atribuí valorLiquido
	 * @param valorLiquido 
	 */
	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	/**
	 * Obtém valorDesconto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * Atribuí valorDesconto
	 * @param valorDesconto 
	 */
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	/**
	 * Obtém idCfop
	 *
	 * @return Long
	 */
	public Long getIdCfop() {
		return idCfop;
	}

	/**
	 * Atribuí idCfop
	 * @param idCfop 
	 */
	public void setIdCfop(Long idCfop) {
		this.idCfop = idCfop;
	}

	/**
	 * Obtém idEmitente
	 *
	 * @return Long
	 */
	public Long getIdEmitente() {
		return idEmitente;
	}

	/**
	 * Atribuí idEmitente
	 * @param idEmitente 
	 */
	public void setIdEmitente(Long idEmitente) {
		this.idEmitente = idEmitente;
	}

	/**
	 * Obtém idTipoNotaFiscal
	 *
	 * @return Long
	 */
	public Long getIdTipoNotaFiscal() {
		return idTipoNotaFiscal;
	}

	/**
	 * Atribuí idTipoNotaFiscal
	 * @param idTipoNotaFiscal 
	 */
	public void setIdTipoNotaFiscal(Long idTipoNotaFiscal) {
		this.idTipoNotaFiscal = idTipoNotaFiscal;
	}

	/**
	 * Obtém statusEmissao
	 *
	 * @return String
	 */
	public String getStatusEmissao() {
		return statusEmissao;
	}

	/**
	 * Atribuí statusEmissao
	 * @param statusEmissao 
	 */
	public void setStatusEmissao(String statusEmissao) {
		this.statusEmissao = statusEmissao;
	}

	/**
	 * Obtém emitida
	 *
	 * @return boolean
	 */
	public boolean isEmitida() {
		return emitida;
	}

	/**
	 * Atribuí emitida
	 * @param emitida 
	 */
	public void setEmitida(boolean emitida) {
		this.emitida = emitida;
	}

	/**
	 * Obtém tipoEmissaoNfe
	 *
	 * @return String
	 */
	public String getTipoEmissaoNfe() {
		return tipoEmissaoNfe;
	}

	/**
	 * Atribuí tipoEmissaoNfe
	 * @param tipoEmissaoNfe 
	 */
	public void setTipoEmissaoNfe(String tipoEmissaoNfe) {
		this.tipoEmissaoNfe = tipoEmissaoNfe;
	}

	/**
	 * Obtém movimentoIntegracao
	 *
	 * @return String
	 */
	public String getMovimentoIntegracao() {
		return movimentoIntegracao;
	}

	/**
	 * Atribuí movimentoIntegracao
	 * @param movimentoIntegracao 
	 */
	public void setMovimentoIntegracao(String movimentoIntegracao) {
		this.movimentoIntegracao = movimentoIntegracao;
	}

	/**
	 * Obtém statusEmissaoNfe
	 *
	 * @return String
	 */
	public String getStatusEmissaoNfe() {
		return statusEmissaoNfe;
	}

	/**
	 * Atribuí statusEmissaoNfe
	 * @param statusEmissaoNfe 
	 */
	public void setStatusEmissaoNfe(String statusEmissaoNfe) {
		this.statusEmissaoNfe = statusEmissaoNfe;
	}
	
	
}
