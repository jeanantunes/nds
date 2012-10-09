package br.com.abril.nds.export.cnab.cobranca;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class DetalheSegmentoP {

	// Controle
	private Long codigoBanco;
	private Long lote;
	private Long tipoRegistro;
	
	// Serviço
	private Long numeroRegistro;
	private String segmento;
	private String cnab1;
	private Long codigoMovimento;
	
	// CC
	private Long codigoAgencia;
	private String dvAgencia;
	private Long numeroConta;
	private String dvConta;
	private String dvAgenciaConta;
	
	private String nossoNumero; 
	
	// Característica Cobrança
	private Long codigoCarteira;
	private Long cadastramento;
	private String tipoDocumento;
	private Long emissaoBloqueto;
	private String distribuicaoBloqueto;
	
	private String numeroDocumento;
	private Long dataVencimento;
	private Long valorTitulo;
	private Long agenciaCobradora;
	private String dvAgenciaCobradora;
	private Long especieTitulo;
	private String aceite;
	private Long dataEmissaoTitulo;
	
	// Juros
	private Long codigoJurosMora;
	private Long dataJurosMora;
	private Long jurosMora;
	
	// Desconto
	private Long codigoDesconto;
	private Long dataDesconto;
	private Long desconto;
	
	private Long valorIOF;
	private Long valorAbatimento;
	private String identificacaoTituloEmpresa;
	private Long codigoProtesto;
	private Long prazoProtesto;
	private Long codigoBaixaDevolucao;
	private String prazoBaixaDevolucao;
	private Long codigoMoeda;
	private Long numeroContrato;
	private String usoLivre;
	
	/**
	 * @return the codigoBanco
	 */
	@Field(offset=1, length=3)
	public Long getCodigoBanco() {
		return codigoBanco;
	}
	/**
	 * @param codigoBanco the codigoBanco to set
	 */
	public void setCodigoBanco(Long codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	/**
	 * @return the lote
	 */
	@Field(offset=4, length=4)
	public Long getLote() {
		return lote;
	}
	/**
	 * @param lote the lote to set
	 */
	public void setLote(Long lote) {
		this.lote = lote;
	}
	/**
	 * @return the tipoRegistro
	 */
	@Field(offset=8, length=1)
	public Long getTipoRegistro() {
		return tipoRegistro;
	}
	/**
	 * @param tipoRegistro the tipoRegistro to set
	 */
	public void setTipoRegistro(Long tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	/**
	 * @return the numeroRegistro
	 */
	@Field(offset=9, length=5)
	public Long getNumeroRegistro() {
		return numeroRegistro;
	}
	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(Long numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}
	/**
	 * @return the segmento
	 */
	@Field(offset=14, length=1)
	public String getSegmento() {
		return segmento;
	}
	/**
	 * @param segmento the segmento to set
	 */
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}
	/**
	 * @return the cnab1
	 */
	@Field(offset=15, length=1)
	public String getCnab1() {
		return cnab1;
	}
	/**
	 * @param cnab1 the cnab1 to set
	 */
	public void setCnab1(String cnab1) {
		this.cnab1 = cnab1;
	}
	/**
	 * @return the codigoMovimento
	 */
	@Field(offset=16, length=2)
	public Long getCodigoMovimento() {
		return codigoMovimento;
	}
	/**
	 * @param codigoMovimento the codigoMovimento to set
	 */
	public void setCodigoMovimento(Long codigoMovimento) {
		this.codigoMovimento = codigoMovimento;
	}
	/**
	 * @return the codigoAgencia
	 */
	@Field(offset=15, length=5)
	public Long getCodigoAgencia() {
		return codigoAgencia;
	}
	/**
	 * @param codigoAgencia the codigoAgencia to set
	 */
	public void setCodigoAgencia(Long codigoAgencia) {
		this.codigoAgencia = codigoAgencia;
	}
	/**
	 * @return the dvAgencia
	 */
	@Field(offset=23, length=1)
	public String getDvAgencia() {
		return dvAgencia;
	}
	/**
	 * @param dvAgencia the dvAgencia to set
	 */
	public void setDvAgencia(String dvAgencia) {
		this.dvAgencia = dvAgencia;
	}
	/**
	 * @return the numeroConta
	 */
	@Field(offset=24, length=12)
	public Long getNumeroConta() {
		return numeroConta;
	}
	/**
	 * @param numeroConta the numeroConta to set
	 */
	public void setNumeroConta(Long numeroConta) {
		this.numeroConta = numeroConta;
	}
	/**
	 * @return the dvConta
	 */
	@Field(offset=36, length=1)
	public String getDvConta() {
		return dvConta;
	}
	/**
	 * @param dvConta the dvConta to set
	 */
	public void setDvConta(String dvConta) {
		this.dvConta = dvConta;
	}
	/**
	 * @return the dvAgenciaConta
	 */
	@Field(offset=37, length=1)
	public String getDvAgenciaConta() {
		return dvAgenciaConta;
	}
	/**
	 * @param dvAgenciaConta the dvAgenciaConta to set
	 */
	public void setDvAgenciaConta(String dvAgenciaConta) {
		this.dvAgenciaConta = dvAgenciaConta;
	}
	/**
	 * @return the nossoNumero
	 */
	@Field(offset=38, length=20)
	public String getNossoNumero() {
		return nossoNumero;
	}
	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	/**
	 * @return the codigoCarteira
	 */
	@Field(offset=58, length=1)
	public Long getCodigoCarteira() {
		return codigoCarteira;
	}
	/**
	 * @param codigoCarteira the codigoCarteira to set
	 */
	public void setCodigoCarteira(Long codigoCarteira) {
		this.codigoCarteira = codigoCarteira;
	}
	/**
	 * @return the cadastramento
	 */
	@Field(offset=59, length=1)
	public Long getCadastramento() {
		return cadastramento;
	}
	/**
	 * @param cadastramento the cadastramento to set
	 */
	public void setCadastramento(Long cadastramento) {
		this.cadastramento = cadastramento;
	}
	/**
	 * @return the tipoDocumento
	 */
	@Field(offset=60, length=1)
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	/**
	 * @return the emissaoBloqueto
	 */
	@Field(offset=61, length=1)
	public Long getEmissaoBloqueto() {
		return emissaoBloqueto;
	}
	/**
	 * @param emissaoBloqueto the emissaoBloqueto to set
	 */
	public void setEmissaoBloqueto(Long emissaoBloqueto) {
		this.emissaoBloqueto = emissaoBloqueto;
	}
	/**
	 * @return the distribuicaoBloqueto
	 */
	@Field(offset=62, length=1)
	public String getDistribuicaoBloqueto() {
		return distribuicaoBloqueto;
	}
	/**
	 * @param distribuicaoBloqueto the distribuicaoBloqueto to set
	 */
	public void setDistribuicaoBloqueto(String distribuicaoBloqueto) {
		this.distribuicaoBloqueto = distribuicaoBloqueto;
	}
	/**
	 * @return the numeroDocumento
	 */
	@Field(offset=63, length=15)
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	/**
	 * @return the dataVencimento
	 */
	@Field(offset=78, length=8)
	public Long getDataVencimento() {
		return dataVencimento;
	}
	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Long dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	/**
	 * @return the valorTitulo
	 */
	@Field(offset=86, length=13)
	public Long getValorTitulo() {
		return valorTitulo;
	}
	/**
	 * @param valorTitulo the valorTitulo to set
	 */
	public void setValorTitulo(Long valorTitulo) {
		this.valorTitulo = valorTitulo;
	}
	/**
	 * @return the agenciaCobradora
	 */
	@Field(offset=101, length=5)
	public Long getAgenciaCobradora() {
		return agenciaCobradora;
	}
	/**
	 * @param agenciaCobradora the agenciaCobradora to set
	 */
	public void setAgenciaCobradora(Long agenciaCobradora) {
		this.agenciaCobradora = agenciaCobradora;
	}
	/**
	 * @return the dvAgenciaCobradora
	 */
	@Field(offset=106, length=1)
	public String getDvAgenciaCobradora() {
		return dvAgenciaCobradora;
	}
	/**
	 * @param dvAgenciaCobradora the dvAgenciaCobradora to set
	 */
	public void setDvAgenciaCobradora(String dvAgenciaCobradora) {
		this.dvAgenciaCobradora = dvAgenciaCobradora;
	}
	/**
	 * @return the especieTitulo
	 */
	@Field(offset=107, length=2)
	public Long getEspecieTitulo() {
		return especieTitulo;
	}
	/**
	 * @param especieTitulo the especieTitulo to set
	 */
	public void setEspecieTitulo(Long especieTitulo) {
		this.especieTitulo = especieTitulo;
	}
	/**
	 * @return the aceite
	 */
	@Field(offset=109, length=1)
	public String getAceite() {
		return aceite;
	}
	/**
	 * @param aceite the aceite to set
	 */
	public void setAceite(String aceite) {
		this.aceite = aceite;
	}
	/**
	 * @return the dataEmissaoTitulo
	 */
	@Field(offset=110, length=8)
	public Long getDataEmissaoTitulo() {
		return dataEmissaoTitulo;
	}
	/**
	 * @param dataEmissaoTitulo the dataEmissaoTitulo to set
	 */
	public void setDataEmissaoTitulo(Long dataEmissaoTitulo) {
		this.dataEmissaoTitulo = dataEmissaoTitulo;
	}
	/**
	 * @return the codigoJurosMora
	 */
	@Field(offset=108, length=1)
	public Long getCodigoJurosMora() {
		return codigoJurosMora;
	}
	/**
	 * @param codigoJurosMora the codigoJurosMora to set
	 */
	public void setCodigoJurosMora(Long codigoJurosMora) {
		this.codigoJurosMora = codigoJurosMora;
	}
	/**
	 * @return the dataJurosMora
	 */
	@Field(offset=119, length=8)
	public Long getDataJurosMora() {
		return dataJurosMora;
	}
	/**
	 * @param dataJurosMora the dataJurosMora to set
	 */
	public void setDataJurosMora(Long dataJurosMora) {
		this.dataJurosMora = dataJurosMora;
	}
	/**
	 * @return the jurosMora
	 */
	@Field(offset=127, length=13)
	public Long getJurosMora() {
		return jurosMora;
	}
	/**
	 * @param jurosMora the jurosMora to set
	 */
	public void setJurosMora(Long jurosMora) {
		this.jurosMora = jurosMora;
	}
	/**
	 * @return the codigoDesconto
	 */
	@Field(offset=142, length=1)
	public Long getCodigoDesconto() {
		return codigoDesconto;
	}
	/**
	 * @param codigoDesconto the codigoDesconto to set
	 */
	public void setCodigoDesconto(Long codigoDesconto) {
		this.codigoDesconto = codigoDesconto;
	}
	/**
	 * @return the dataDesconto
	 */
	@Field(offset=143, length=8)
	public Long getDataDesconto() {
		return dataDesconto;
	}
	/**
	 * @param dataDesconto the dataDesconto to set
	 */
	public void setDataDesconto(Long dataDesconto) {
		this.dataDesconto = dataDesconto;
	}
	/**
	 * @return the desconto
	 */
	@Field(offset=151, length=13)
	public Long getDesconto() {
		return desconto;
	}
	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(Long desconto) {
		this.desconto = desconto;
	}
	/**
	 * @return the valorIOF
	 */
	@Field(offset=166, length=13)
	public Long getValorIOF() {
		return valorIOF;
	}
	/**
	 * @param valorIOF the valorIOF to set
	 */
	public void setValorIOF(Long valorIOF) {
		this.valorIOF = valorIOF;
	}
	/**
	 * @return the valorAbatimento
	 */
	@Field(offset=181, length=2)
	public Long getValorAbatimento() {
		return valorAbatimento;
	}
	/**
	 * @param valorAbatimento the valorAbatimento to set
	 */
	public void setValorAbatimento(Long valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}
	/**
	 * @return the identificacaoTituloEmpresa
	 */
	@Field(offset=196, length=25)
	public String getIdentificacaoTituloEmpresa() {
		return identificacaoTituloEmpresa;
	}
	/**
	 * @param identificacaoTituloEmpresa the identificacaoTituloEmpresa to set
	 */
	public void setIdentificacaoTituloEmpresa(String identificacaoTituloEmpresa) {
		this.identificacaoTituloEmpresa = identificacaoTituloEmpresa;
	}
	/**
	 * @return the codigoProtesto
	 */
	@Field(offset=221, length=1)
	public Long getCodigoProtesto() {
		return codigoProtesto;
	}
	/**
	 * @param codigoProtesto the codigoProtesto to set
	 */
	public void setCodigoProtesto(Long codigoProtesto) {
		this.codigoProtesto = codigoProtesto;
	}
	/**
	 * @return the prazoProtesto
	 */
	@Field(offset=222, length=2)
	public Long getPrazoProtesto() {
		return prazoProtesto;
	}
	/**
	 * @param prazoProtesto the prazoProtesto to set
	 */
	public void setPrazoProtesto(Long prazoProtesto) {
		this.prazoProtesto = prazoProtesto;
	}
	/**
	 * @return the codigoBaixaDevolucao
	 */
	@Field(offset=224, length=1)
	public Long getCodigoBaixaDevolucao() {
		return codigoBaixaDevolucao;
	}
	/**
	 * @param codigoBaixaDevolucao the codigoBaixaDevolucao to set
	 */
	public void setCodigoBaixaDevolucao(Long codigoBaixaDevolucao) {
		this.codigoBaixaDevolucao = codigoBaixaDevolucao;
	}
	/**
	 * @return the prazoBaixaDevolucao
	 */
	@Field(offset=225, length=1)
	public String getPrazoBaixaDevolucao() {
		return prazoBaixaDevolucao;
	}
	/**
	 * @param prazoBaixaDevolucao the prazoBaixaDevolucao to set
	 */
	public void setPrazoBaixaDevolucao(String prazoBaixaDevolucao) {
		this.prazoBaixaDevolucao = prazoBaixaDevolucao;
	}
	/**
	 * @return the codigoMoeda
	 */
	@Field(offset=228, length=2)
	public Long getCodigoMoeda() {
		return codigoMoeda;
	}
	/**
	 * @param codigoMoeda the codigoMoeda to set
	 */
	public void setCodigoMoeda(Long codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}
	/**
	 * @return the numeroContrato
	 */
	@Field(offset=230, length=10)
	public Long getNumeroContrato() {
		return numeroContrato;
	}
	/**
	 * @param numeroContrato the numeroContrato to set
	 */
	public void setNumeroContrato(Long numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	/**
	 * @return the usoLivre
	 */
	@Field(offset=240, length=1)
	public String getUsoLivre() {
		return usoLivre;
	}
	/**
	 * @param usoLivre the usoLivre to set
	 */
	public void setUsoLivre(String usoLivre) {
		this.usoLivre = usoLivre;
	}
	
}
