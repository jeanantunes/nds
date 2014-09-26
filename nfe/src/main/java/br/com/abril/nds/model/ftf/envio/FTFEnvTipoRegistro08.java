package br.com.abril.nds.model.ftf.envio;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro08 extends FTFBaseDTO implements FTFCommons {
	
	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "8";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codigoEstabelecimentoEmissor;
	
	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEmpresaEmissora;

	@FTFfield(tamanho = 11, tipo = "char", ordem = 4)
	private String codLocal;

	@FTFfield(tamanho = 2, tipo = "char", ordem = 5)
	private String tipoPedido;
	
	@FTFfield(tamanho = 8, tipo = "char", ordem = 6)
	private String numeroDocOrigem;

	@FTFfield(tamanho = 60, tipo = "char", ordem = 7)
	private String nomeDoCliente;
	
	@FTFfield(tamanho=14, tipo="char", ordem=8)
	private String cpfOuCnpj;
	
	@FTFfield(tamanho=60, tipo="char", ordem=9)
	private String endereco;
	
	@FTFfield(tamanho=10, tipo="char", ordem=10)
	private String numeroEndereco;
	
	@FTFfield(tamanho=60, tipo="char", ordem=11)
	private String complementoEndereco;
	
	@FTFfield(tamanho=60, tipo="char", ordem=12)
	private String bairro;
	
	@FTFfield(tamanho=30, tipo="char", ordem=13)
	private String cidade;
	
	@FTFfield(tamanho=7, tipo="numeric", ordem=14)
	private String codigoIBGE;
	
	@FTFfield(tamanho=2, tipo="char", ordem=15)
	private String siglaEstado;
	
	@FTFfield(tamanho=8, tipo="char", ordem=16)
	private String cep;
	
	@FTFfield(tamanho=2, tipo="char", ordem=17)
	private String codigoPais;
	
	@FTFfield(tamanho=30, tipo="char", ordem=18)
	private String nomePais;
	
	@FTFfield(tamanho=6, tipo="numeric", ordem=19)
	private String codigoCGL;
	
	@FTFfield(tamanho=4, tipo="char", ordem=20)
	private String ddi;
	
	@FTFfield(tamanho=4, tipo="char", ordem=21)
	private String ddd;
	
	@FTFfield(tamanho=8, tipo="char", ordem=22)
	private String telefone;
	
	@FTFfield(tamanho=10, tipo="char", ordem=23)
	private String complementoTelefone;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodigoEstabelecimentoEmissor() {
		return codigoEstabelecimentoEmissor;
	}

	public void setCodigoEstabelecimentoEmissor(String codigoEstabelecimentoEmissor) {
		this.codigoEstabelecimentoEmissor = codigoEstabelecimentoEmissor;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		cpfOuCnpj = cpfOuCnpj != null ? StringUtils.leftPad(cpfOuCnpj, 14, ' ') : StringUtils.leftPad(cpfOuCnpj, 14, ' ');
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumeroEndereco() {
		return numeroEndereco;
	}

	public void setNumeroEndereco(String numeroEndereco) {
		this.numeroEndereco = numeroEndereco;
	}

	public String getComplementoEndereco() {
		return complementoEndereco;
	}

	public void setComplementoEndereco(String complementoEndereco) {
		this.complementoEndereco = complementoEndereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	public String getSiglaEstado() {
		return siglaEstado;
	}

	public void setSiglaEstado(String siglaEstado) {
		this.siglaEstado = siglaEstado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getNomePais() {
		return nomePais;
	}

	public void setNomePais(String nomePais) {
		this.nomePais = nomePais;
	}

	public String getCodigoCGL() {
		return codigoCGL;
	}

	public void setCodigoCGL(String codigoCGL) {
		this.codigoCGL = codigoCGL;
	}

	public String getDdi() {
		return ddi;
	}

	public void setDdi(String ddi) {
		this.ddi = ddi;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getComplementoTelefone() {
		return complementoTelefone;
	}

	public void setComplementoTelefone(String complementoTelefone) {
		this.complementoTelefone = complementoTelefone;
	}
	
	
	public String getCnpjEmpresaEmissora() {
		return cnpjEmpresaEmissora;
	}

	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora) {
		this.cnpjEmpresaEmissora = cnpjEmpresaEmissora != null ? cnpjEmpresaEmissora.replaceAll("\\D", "") : null;
	}
	
	@Override
	public void setCodLocal(String codLocal) {
		this.codLocal = codLocal != null ? StringUtils.leftPad(codLocal, 11, '0') : StringUtils.leftPad("", 11, '0');
	}

	@Override
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido != null ? StringUtils.leftPad(tipoPedido, 2, '0') : StringUtils.leftPad(tipoPedido, ' ', '0');
	}

	@Override
	public void setNumeroDocOrigem(String numeroDocOrigem) {
		this.numeroDocOrigem = numeroDocOrigem;
	}

	public String getCodLocal() {
		return codLocal;
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public String getNumeroDocOrigem() {
		return numeroDocOrigem;
	}

	public String getNomeDoCliente() {
		return nomeDoCliente;
	}

	public void setNomeDoCliente(String nomeDoCliente) {
		this.nomeDoCliente = nomeDoCliente;
	}
}
