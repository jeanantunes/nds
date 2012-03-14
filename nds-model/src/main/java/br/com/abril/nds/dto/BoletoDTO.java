package br.com.abril.nds.dto;
import java.math.BigDecimal;
import java.util.Date;

public class BoletoDTO {

	private String cedenteNome;
	private String cedenteDocumento;
	
	private String sacadoNome;
	private String sacadoDocumento;
	
	private String sacadorAvalistaNome;
	private String sacadorAvalistaDocumento;
	
	private String enderecoSacadoUf;
	private String enderecoSacadoLocalidade;
	private String enderecoSacadoCep;
	private String enderecoSacadoBairro;
	private String enderecoSacadoLogradouro;
	private String enderecoSacadoNumero;
	
	private String enderecoSacadorAvalistaUf;
	private String enderecoSacadorAvalistaLocalidade;
	private String enderecoSacadorAvalistaCep;
	private String enderecoSacadorAvalistaBairro;
	private String enderecoSacadorAvalistaLogradouro;
	private String enderecoSacadorAvalistaNumero;
	
	private Integer contaNumero;
	private Integer contaCarteira;
	private Integer contaAgencia;
	
	private String tituloNumeroDoDocumento;
	private String tituloNossoNumero;
	private String tituloDigitoDoNossoNumero;
	private BigDecimal tituloValor;
	private Date tituloDataDoDocumento;
	private Date tituloDataDoVencimento;
	
	private String tituloTipoDeDocumento;
	private String tituloAceite;
    
    private BigDecimal tituloDesconto;
    private BigDecimal tituloDeducao;
    private BigDecimal tituloMora;
    private BigDecimal tituloAcrecimo;
    private BigDecimal tituloValorCobrado;

    private String boletoLocalPagamento;
    private String boletoInstrucaoAoSacado;
    private String boletoInstrucao1;
    private String boletoInstrucao2;
    private String boletoInstrucao3;
    private String boletoInstrucao4;
    private String boletoInstrucao5;
    private String boletoInstrucao6;
    private String boletoInstrucao7;
    private String boletoInstrucao8;
    
    public BoletoDTO(){
    	
    }
	
	
	public String getCedenteNome() {
		return cedenteNome;
	}
	
	
	public void setCedenteNome(String cedenteNome) {
		this.cedenteNome = cedenteNome;
	}
	
	
	public String getCedenteDocumento() {
		return cedenteDocumento;
	}
	
	
	public void setCedenteDocumento(String cedenteDocumento) {
		this.cedenteDocumento = cedenteDocumento;
	}
	
	
	public String getSacadoNome() {
		return sacadoNome;
	}
	
	
	public void setSacadoNome(String sacadoNome) {
		this.sacadoNome = sacadoNome;
	}
	
	
	public String getSacadoDocumento() {
		return sacadoDocumento;
	}
	
	
	public void setSacadoDocumento(String sacadoDocumento) {
		this.sacadoDocumento = sacadoDocumento;
	}
	
	
	public String getSacadorAvalistaNome() {
		return sacadorAvalistaNome;
	}
	
	
	public void setSacadorAvalistaNome(String sacadorAvalistaNome) {
		this.sacadorAvalistaNome = sacadorAvalistaNome;
	}
	
	
	public String getSacadorAvalistaDocumento() {
		return sacadorAvalistaDocumento;
	}
	
	
	public void setSacadorAvalistaDocumento(String sacadorAvalistaDocumento) {
		this.sacadorAvalistaDocumento = sacadorAvalistaDocumento;
	}
	
	
	public String getEnderecoSacadoUf() {
		return enderecoSacadoUf;
	}
	
	
	public void setEnderecoSacadoUf(String enderecoSacadoUf) {
		this.enderecoSacadoUf = enderecoSacadoUf;
	}
	
	
	public String getEnderecoSacadoLocalidade() {
		return enderecoSacadoLocalidade;
	}
	
	
	public void setEnderecoSacadoLocalidade(String enderecoSacadoLocalidade) {
		this.enderecoSacadoLocalidade = enderecoSacadoLocalidade;
	}
	
	
	public String getEnderecoSacadoCep() {
		return enderecoSacadoCep;
	}
	
	
	public void setEnderecoSacadoCep(String enderecoSacadoCep) {
		this.enderecoSacadoCep = enderecoSacadoCep;
	}
	public String getEnderecoSacadoBairro() {
		return enderecoSacadoBairro;
	}
	
	
	public void setEnderecoSacadoBairro(String enderecoSacadoBairro) {
		this.enderecoSacadoBairro = enderecoSacadoBairro;
	}
	
	
	public String getEnderecoSacadoLogradouro() {
		return enderecoSacadoLogradouro;
	}
	
	
	public void setEnderecoSacadoLogradouro(String enderecoSacadoLogradouro) {
		this.enderecoSacadoLogradouro = enderecoSacadoLogradouro;
	}
	
	
	public String getEnderecoSacadoNumero() {
		return enderecoSacadoNumero;
	}
	
	
	public void setEnderecoSacadoNumero(String enderecoSacadoNumero) {
		this.enderecoSacadoNumero = enderecoSacadoNumero;
	}
	
	
	public String getEnderecoSacadorAvalistaUf() {
		return enderecoSacadorAvalistaUf;
	}
	
	
	public void setEnderecoSacadorAvalistaUf(String enderecoSacadorAvalistaUf) {
		this.enderecoSacadorAvalistaUf = enderecoSacadorAvalistaUf;
	}
	
	
	public String getEnderecoSacadorAvalistaLocalidade() {
		return enderecoSacadorAvalistaLocalidade;
	}
	
	
	public void setEnderecoSacadorAvalistaLocalidade(
			String enderecoSacadorAvalistaLocalidade) {
		this.enderecoSacadorAvalistaLocalidade = enderecoSacadorAvalistaLocalidade;
	}
	
	
	public String getEnderecoSacadorAvalistaCep() {
		return enderecoSacadorAvalistaCep;
	}
	
	
	public void setEnderecoSacadorAvalistaCep(String enderecoSacadorAvalistaCep) {
		this.enderecoSacadorAvalistaCep = enderecoSacadorAvalistaCep;
	}
	
	
	public String getEnderecoSacadorAvalistaBairro() {
		return enderecoSacadorAvalistaBairro;
	}
	
	
	public void setEnderecoSacadorAvalistaBairro(
			String enderecoSacadorAvalistaBairro) {
		this.enderecoSacadorAvalistaBairro = enderecoSacadorAvalistaBairro;
	}
	
	
	public String getEnderecoSacadorAvalistaLogradouro() {
		return enderecoSacadorAvalistaLogradouro;
	}
	
	
	public void setEnderecoSacadorAvalistaLogradouro(
			String enderecoSacadorAvalistaLogradouro) {
		this.enderecoSacadorAvalistaLogradouro = enderecoSacadorAvalistaLogradouro;
	}
	
	
	public String getEnderecoSacadorAvalistaNumero() {
		return enderecoSacadorAvalistaNumero;
	}
	
	
	public void setEnderecoSacadorAvalistaNumero(
			String enderecoSacadorAvalistaNumero) {
		this.enderecoSacadorAvalistaNumero = enderecoSacadorAvalistaNumero;
	}
	
	
	public Integer getContaNumero() {
		return contaNumero;
	}
	
	
	public void setContaNumero(Integer contaNumero) {
		this.contaNumero = contaNumero;
	}
	
	
	public Integer getContaCarteira() {
		return contaCarteira;
	}
	
	
	public void setContaCarteira(Integer contaCarteira) {
		this.contaCarteira = contaCarteira;
	}
	
	
	public Integer getContaAgencia() {
		return contaAgencia;
	}
	
	
	public void setContaAgencia(Integer contaAgencia) {
		this.contaAgencia = contaAgencia;
	}
	
	
	public String getTituloNumeroDoDocumento() {
		return tituloNumeroDoDocumento;
	}
	
	
	public void setTituloNumeroDoDocumento(String tituloNumeroDoDocumento) {
		this.tituloNumeroDoDocumento = tituloNumeroDoDocumento;
	}
	
	
	public String getTituloNossoNumero() {
		return tituloNossoNumero;
	}
	
	
	public void setTituloNossoNumero(String tituloNossoNumero) {
		this.tituloNossoNumero = tituloNossoNumero;
	}
	
	
	public String getTituloDigitoDoNossoNumero() {
		return tituloDigitoDoNossoNumero;
	}
	
	
	public void setTituloDigitoDoNossoNumero(String tituloDigitoDoNossoNumero) {
		this.tituloDigitoDoNossoNumero = tituloDigitoDoNossoNumero;
	}
	
	
	public BigDecimal getTituloValor() {
		return tituloValor;
	}
	
	
	public void setTituloValor(BigDecimal tituloValor) {
		this.tituloValor = tituloValor;
	}
	
	
	public Date getTituloDataDoDocumento() {
		return tituloDataDoDocumento;
	}
	
	
	public void setTituloDataDoDocumento(Date tituloDataDoDocumento) {
		this.tituloDataDoDocumento = tituloDataDoDocumento;
	}
	
	
	public Date getTituloDataDoVencimento() {
		return tituloDataDoVencimento;
	}
	
	
	public void setTituloDataDoVencimento(Date tituloDataDoVencimento) {
		this.tituloDataDoVencimento = tituloDataDoVencimento;
	}
	
	
	public String getTituloTipoDeDocumento() {
		return tituloTipoDeDocumento;
	}
	
	
	public void setTituloTipoDeDocumento(String tituloTipoDeDocumento) {
		this.tituloTipoDeDocumento = tituloTipoDeDocumento;
	}
	
	
	public String getTituloAceite() {
		return tituloAceite;
	}
	
	
	public void setTituloAceite(String tituloAceite) {
		this.tituloAceite = tituloAceite;
	}
	
	
	public BigDecimal getTituloDesconto() {
		return tituloDesconto;
	}
	
	
	public void setTituloDesconto(BigDecimal tituloDesconto) {
		this.tituloDesconto = tituloDesconto;
	}
	
	
	public BigDecimal getTituloDeducao() {
		return tituloDeducao;
	}
	
	
	public void setTituloDeducao(BigDecimal tituloDeducao) {
		this.tituloDeducao = tituloDeducao;
	}
	
	
	public BigDecimal getTituloMora() {
		return tituloMora;
	}
	
	
	public void setTituloMora(BigDecimal tituloMora) {
		this.tituloMora = tituloMora;
	}
	
	
	public BigDecimal getTituloAcrecimo() {
		return tituloAcrecimo;
	}
	
	
	public void setTituloAcrecimo(BigDecimal tituloAcrecimo) {
		this.tituloAcrecimo = tituloAcrecimo;
	}
	
	
	public BigDecimal getTituloValorCobrado() {
		return tituloValorCobrado;
	}
	
	
	public void setTituloValorCobrado(BigDecimal tituloValorCobrado) {
		this.tituloValorCobrado = tituloValorCobrado;
	}
	
	
	public String getBoletoLocalPagamento() {
		return boletoLocalPagamento;
	}
	
	
	public void setBoletoLocalPagamento(String boletoLocalPagamento) {
		this.boletoLocalPagamento = boletoLocalPagamento;
	}
	
	
	public String getBoletoInstrucaoAoSacado() {
		return boletoInstrucaoAoSacado;
	}
	
	
	public void setBoletoInstrucaoAoSacado(String boletoInstrucaoAoSacado) {
		this.boletoInstrucaoAoSacado = boletoInstrucaoAoSacado;
	}
	
	
	public String getBoletoInstrucao1() {
		return boletoInstrucao1;
	}
	
	
	public void setBoletoInstrucao1(String boletoInstrucao1) {
		this.boletoInstrucao1 = boletoInstrucao1;
	}
	
	
	public String getBoletoInstrucao2() {
		return boletoInstrucao2;
	}
	
	
	public void setBoletoInstrucao2(String boletoInstrucao2) {
		this.boletoInstrucao2 = boletoInstrucao2;
	}
	
	
	public String getBoletoInstrucao3() {
		return boletoInstrucao3;
	}
	
	
	public void setBoletoInstrucao3(String boletoInstrucao3) {
		this.boletoInstrucao3 = boletoInstrucao3;
	}
	
	
	public String getBoletoInstrucao4() {
		return boletoInstrucao4;
	}
	
	
	public void setBoletoInstrucao4(String boletoInstrucao4) {
		this.boletoInstrucao4 = boletoInstrucao4;
	}
	
	
	public String getBoletoInstrucao5() {
		return boletoInstrucao5;
	}
	
	
	public void setBoletoInstrucao5(String boletoInstrucao5) {
		this.boletoInstrucao5 = boletoInstrucao5;
	}
	
	
	public String getBoletoInstrucao6() {
		return boletoInstrucao6;
	}
	
	
	public void setBoletoInstrucao6(String boletoInstrucao6) {
		this.boletoInstrucao6 = boletoInstrucao6;
	}
	
	
	public String getBoletoInstrucao7() {
		return boletoInstrucao7;
	}
	
	
	public void setBoletoInstrucao7(String boletoInstrucao7) {
		this.boletoInstrucao7 = boletoInstrucao7;
	}
	
	
	public String getBoletoInstrucao8() {
		return boletoInstrucao8;
	}
	
	
	public void setBoletoInstrucao8(String boletoInstrucao8) {
		this.boletoInstrucao8 = boletoInstrucao8;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((boletoInstrucao1 == null) ? 0 : boletoInstrucao1.hashCode());
		result = prime
				* result
				+ ((boletoInstrucao2 == null) ? 0 : boletoInstrucao2.hashCode());
		result = prime
				* result
				+ ((boletoInstrucao3 == null) ? 0 : boletoInstrucao3.hashCode());
		result = prime
				* result
				+ ((boletoInstrucao4 == null) ? 0 : boletoInstrucao4.hashCode());
		result = prime
				* result
				+ ((boletoInstrucao5 == null) ? 0 : boletoInstrucao5.hashCode());
		result = prime
				* result
				+ ((boletoInstrucao6 == null) ? 0 : boletoInstrucao6.hashCode());
		result = prime
				* result
				+ ((boletoInstrucao7 == null) ? 0 : boletoInstrucao7.hashCode());
		result = prime
				* result
				+ ((boletoInstrucao8 == null) ? 0 : boletoInstrucao8.hashCode());
		result = prime
				* result
				+ ((boletoInstrucaoAoSacado == null) ? 0
						: boletoInstrucaoAoSacado.hashCode());
		result = prime
				* result
				+ ((boletoLocalPagamento == null) ? 0 : boletoLocalPagamento
						.hashCode());
		result = prime
				* result
				+ ((cedenteDocumento == null) ? 0 : cedenteDocumento.hashCode());
		result = prime * result
				+ ((cedenteNome == null) ? 0 : cedenteNome.hashCode());
		result = prime * result
				+ ((contaAgencia == null) ? 0 : contaAgencia.hashCode());
		result = prime * result
				+ ((contaCarteira == null) ? 0 : contaCarteira.hashCode());
		result = prime * result
				+ ((contaNumero == null) ? 0 : contaNumero.hashCode());
		result = prime
				* result
				+ ((enderecoSacadoBairro == null) ? 0 : enderecoSacadoBairro
						.hashCode());
		result = prime
				* result
				+ ((enderecoSacadoCep == null) ? 0 : enderecoSacadoCep
						.hashCode());
		result = prime
				* result
				+ ((enderecoSacadoLocalidade == null) ? 0
						: enderecoSacadoLocalidade.hashCode());
		result = prime
				* result
				+ ((enderecoSacadoLogradouro == null) ? 0
						: enderecoSacadoLogradouro.hashCode());
		result = prime
				* result
				+ ((enderecoSacadoNumero == null) ? 0 : enderecoSacadoNumero
						.hashCode());
		result = prime
				* result
				+ ((enderecoSacadoUf == null) ? 0 : enderecoSacadoUf.hashCode());
		result = prime
				* result
				+ ((enderecoSacadorAvalistaBairro == null) ? 0
						: enderecoSacadorAvalistaBairro.hashCode());
		result = prime
				* result
				+ ((enderecoSacadorAvalistaCep == null) ? 0
						: enderecoSacadorAvalistaCep.hashCode());
		result = prime
				* result
				+ ((enderecoSacadorAvalistaLocalidade == null) ? 0
						: enderecoSacadorAvalistaLocalidade.hashCode());
		result = prime
				* result
				+ ((enderecoSacadorAvalistaLogradouro == null) ? 0
						: enderecoSacadorAvalistaLogradouro.hashCode());
		result = prime
				* result
				+ ((enderecoSacadorAvalistaNumero == null) ? 0
						: enderecoSacadorAvalistaNumero.hashCode());
		result = prime
				* result
				+ ((enderecoSacadorAvalistaUf == null) ? 0
						: enderecoSacadorAvalistaUf.hashCode());
		result = prime * result
				+ ((sacadoDocumento == null) ? 0 : sacadoDocumento.hashCode());
		result = prime * result
				+ ((sacadoNome == null) ? 0 : sacadoNome.hashCode());
		result = prime
				* result
				+ ((sacadorAvalistaDocumento == null) ? 0
						: sacadorAvalistaDocumento.hashCode());
		result = prime
				* result
				+ ((sacadorAvalistaNome == null) ? 0 : sacadorAvalistaNome
						.hashCode());
		result = prime * result
				+ ((tituloAceite == null) ? 0 : tituloAceite.hashCode());
		result = prime * result
				+ ((tituloAcrecimo == null) ? 0 : tituloAcrecimo.hashCode());
		result = prime
				* result
				+ ((tituloDataDoDocumento == null) ? 0 : tituloDataDoDocumento
						.hashCode());
		result = prime
				* result
				+ ((tituloDataDoVencimento == null) ? 0
						: tituloDataDoVencimento.hashCode());
		result = prime * result
				+ ((tituloDeducao == null) ? 0 : tituloDeducao.hashCode());
		result = prime * result
				+ ((tituloDesconto == null) ? 0 : tituloDesconto.hashCode());
		result = prime
				* result
				+ ((tituloDigitoDoNossoNumero == null) ? 0
						: tituloDigitoDoNossoNumero.hashCode());
		result = prime * result
				+ ((tituloMora == null) ? 0 : tituloMora.hashCode());
		result = prime
				* result
				+ ((tituloNossoNumero == null) ? 0 : tituloNossoNumero
						.hashCode());
		result = prime
				* result
				+ ((tituloNumeroDoDocumento == null) ? 0
						: tituloNumeroDoDocumento.hashCode());
		result = prime
				* result
				+ ((tituloTipoDeDocumento == null) ? 0 : tituloTipoDeDocumento
						.hashCode());
		result = prime * result
				+ ((tituloValor == null) ? 0 : tituloValor.hashCode());
		result = prime
				* result
				+ ((tituloValorCobrado == null) ? 0 : tituloValorCobrado
						.hashCode());
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoletoDTO other = (BoletoDTO) obj;
		if (boletoInstrucao1 == null) {
			if (other.boletoInstrucao1 != null)
				return false;
		} else if (!boletoInstrucao1.equals(other.boletoInstrucao1))
			return false;
		if (boletoInstrucao2 == null) {
			if (other.boletoInstrucao2 != null)
				return false;
		} else if (!boletoInstrucao2.equals(other.boletoInstrucao2))
			return false;
		if (boletoInstrucao3 == null) {
			if (other.boletoInstrucao3 != null)
				return false;
		} else if (!boletoInstrucao3.equals(other.boletoInstrucao3))
			return false;
		if (boletoInstrucao4 == null) {
			if (other.boletoInstrucao4 != null)
				return false;
		} else if (!boletoInstrucao4.equals(other.boletoInstrucao4))
			return false;
		if (boletoInstrucao5 == null) {
			if (other.boletoInstrucao5 != null)
				return false;
		} else if (!boletoInstrucao5.equals(other.boletoInstrucao5))
			return false;
		if (boletoInstrucao6 == null) {
			if (other.boletoInstrucao6 != null)
				return false;
		} else if (!boletoInstrucao6.equals(other.boletoInstrucao6))
			return false;
		if (boletoInstrucao7 == null) {
			if (other.boletoInstrucao7 != null)
				return false;
		} else if (!boletoInstrucao7.equals(other.boletoInstrucao7))
			return false;
		if (boletoInstrucao8 == null) {
			if (other.boletoInstrucao8 != null)
				return false;
		} else if (!boletoInstrucao8.equals(other.boletoInstrucao8))
			return false;
		if (boletoInstrucaoAoSacado == null) {
			if (other.boletoInstrucaoAoSacado != null)
				return false;
		} else if (!boletoInstrucaoAoSacado
				.equals(other.boletoInstrucaoAoSacado))
			return false;
		if (boletoLocalPagamento == null) {
			if (other.boletoLocalPagamento != null)
				return false;
		} else if (!boletoLocalPagamento.equals(other.boletoLocalPagamento))
			return false;
		if (cedenteDocumento == null) {
			if (other.cedenteDocumento != null)
				return false;
		} else if (!cedenteDocumento.equals(other.cedenteDocumento))
			return false;
		if (cedenteNome == null) {
			if (other.cedenteNome != null)
				return false;
		} else if (!cedenteNome.equals(other.cedenteNome))
			return false;
		if (contaAgencia == null) {
			if (other.contaAgencia != null)
				return false;
		} else if (!contaAgencia.equals(other.contaAgencia))
			return false;
		if (contaCarteira == null) {
			if (other.contaCarteira != null)
				return false;
		} else if (!contaCarteira.equals(other.contaCarteira))
			return false;
		if (contaNumero == null) {
			if (other.contaNumero != null)
				return false;
		} else if (!contaNumero.equals(other.contaNumero))
			return false;
		if (enderecoSacadoBairro == null) {
			if (other.enderecoSacadoBairro != null)
				return false;
		} else if (!enderecoSacadoBairro.equals(other.enderecoSacadoBairro))
			return false;
		if (enderecoSacadoCep == null) {
			if (other.enderecoSacadoCep != null)
				return false;
		} else if (!enderecoSacadoCep.equals(other.enderecoSacadoCep))
			return false;
		if (enderecoSacadoLocalidade == null) {
			if (other.enderecoSacadoLocalidade != null)
				return false;
		} else if (!enderecoSacadoLocalidade
				.equals(other.enderecoSacadoLocalidade))
			return false;
		if (enderecoSacadoLogradouro == null) {
			if (other.enderecoSacadoLogradouro != null)
				return false;
		} else if (!enderecoSacadoLogradouro
				.equals(other.enderecoSacadoLogradouro))
			return false;
		if (enderecoSacadoNumero == null) {
			if (other.enderecoSacadoNumero != null)
				return false;
		} else if (!enderecoSacadoNumero.equals(other.enderecoSacadoNumero))
			return false;
		if (enderecoSacadoUf == null) {
			if (other.enderecoSacadoUf != null)
				return false;
		} else if (!enderecoSacadoUf.equals(other.enderecoSacadoUf))
			return false;
		if (enderecoSacadorAvalistaBairro == null) {
			if (other.enderecoSacadorAvalistaBairro != null)
				return false;
		} else if (!enderecoSacadorAvalistaBairro
				.equals(other.enderecoSacadorAvalistaBairro))
			return false;
		if (enderecoSacadorAvalistaCep == null) {
			if (other.enderecoSacadorAvalistaCep != null)
				return false;
		} else if (!enderecoSacadorAvalistaCep
				.equals(other.enderecoSacadorAvalistaCep))
			return false;
		if (enderecoSacadorAvalistaLocalidade == null) {
			if (other.enderecoSacadorAvalistaLocalidade != null)
				return false;
		} else if (!enderecoSacadorAvalistaLocalidade
				.equals(other.enderecoSacadorAvalistaLocalidade))
			return false;
		if (enderecoSacadorAvalistaLogradouro == null) {
			if (other.enderecoSacadorAvalistaLogradouro != null)
				return false;
		} else if (!enderecoSacadorAvalistaLogradouro
				.equals(other.enderecoSacadorAvalistaLogradouro))
			return false;
		if (enderecoSacadorAvalistaNumero == null) {
			if (other.enderecoSacadorAvalistaNumero != null)
				return false;
		} else if (!enderecoSacadorAvalistaNumero
				.equals(other.enderecoSacadorAvalistaNumero))
			return false;
		if (enderecoSacadorAvalistaUf == null) {
			if (other.enderecoSacadorAvalistaUf != null)
				return false;
		} else if (!enderecoSacadorAvalistaUf
				.equals(other.enderecoSacadorAvalistaUf))
			return false;
		if (sacadoDocumento == null) {
			if (other.sacadoDocumento != null)
				return false;
		} else if (!sacadoDocumento.equals(other.sacadoDocumento))
			return false;
		if (sacadoNome == null) {
			if (other.sacadoNome != null)
				return false;
		} else if (!sacadoNome.equals(other.sacadoNome))
			return false;
		if (sacadorAvalistaDocumento == null) {
			if (other.sacadorAvalistaDocumento != null)
				return false;
		} else if (!sacadorAvalistaDocumento
				.equals(other.sacadorAvalistaDocumento))
			return false;
		if (sacadorAvalistaNome == null) {
			if (other.sacadorAvalistaNome != null)
				return false;
		} else if (!sacadorAvalistaNome.equals(other.sacadorAvalistaNome))
			return false;
		if (tituloAceite == null) {
			if (other.tituloAceite != null)
				return false;
		} else if (!tituloAceite.equals(other.tituloAceite))
			return false;
		if (tituloAcrecimo == null) {
			if (other.tituloAcrecimo != null)
				return false;
		} else if (!tituloAcrecimo.equals(other.tituloAcrecimo))
			return false;
		if (tituloDataDoDocumento == null) {
			if (other.tituloDataDoDocumento != null)
				return false;
		} else if (!tituloDataDoDocumento.equals(other.tituloDataDoDocumento))
			return false;
		if (tituloDataDoVencimento == null) {
			if (other.tituloDataDoVencimento != null)
				return false;
		} else if (!tituloDataDoVencimento.equals(other.tituloDataDoVencimento))
			return false;
		if (tituloDeducao == null) {
			if (other.tituloDeducao != null)
				return false;
		} else if (!tituloDeducao.equals(other.tituloDeducao))
			return false;
		if (tituloDesconto == null) {
			if (other.tituloDesconto != null)
				return false;
		} else if (!tituloDesconto.equals(other.tituloDesconto))
			return false;
		if (tituloDigitoDoNossoNumero == null) {
			if (other.tituloDigitoDoNossoNumero != null)
				return false;
		} else if (!tituloDigitoDoNossoNumero
				.equals(other.tituloDigitoDoNossoNumero))
			return false;
		if (tituloMora == null) {
			if (other.tituloMora != null)
				return false;
		} else if (!tituloMora.equals(other.tituloMora))
			return false;
		if (tituloNossoNumero == null) {
			if (other.tituloNossoNumero != null)
				return false;
		} else if (!tituloNossoNumero.equals(other.tituloNossoNumero))
			return false;
		if (tituloNumeroDoDocumento == null) {
			if (other.tituloNumeroDoDocumento != null)
				return false;
		} else if (!tituloNumeroDoDocumento
				.equals(other.tituloNumeroDoDocumento))
			return false;
		if (tituloTipoDeDocumento == null) {
			if (other.tituloTipoDeDocumento != null)
				return false;
		} else if (!tituloTipoDeDocumento.equals(other.tituloTipoDeDocumento))
			return false;
		if (tituloValor == null) {
			if (other.tituloValor != null)
				return false;
		} else if (!tituloValor.equals(other.tituloValor))
			return false;
		if (tituloValorCobrado == null) {
			if (other.tituloValorCobrado != null)
				return false;
		} else if (!tituloValorCobrado.equals(other.tituloValorCobrado))
			return false;
		return true;
	}
     
    
}
