package br.com.abril.nds.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

public class FormaCobrancaDTO {
	
	Long idFormaCobranca;
	Long idCota;
	Long idParametroCobranca;
	
	//DADOS DO GRID
	String fornecedor;
	String concentracaoPagto = "";
	String tipoPagto;
	String detalhesTipoPagto = "";
	boolean parametroDistribuidor;
	
	//DADOS DO FORMULARIO
	TipoCobranca tipoCobranca;
	TipoFormaCobranca tipoFormaCobranca;
	Long idBanco;
	boolean recebeEmail;
	
	String numBanco;
	String nomeBanco;
	Long agencia;
	String agenciaDigito;
	Long conta;
	String contaDigito;
	
	Integer diaDoMes;
	Integer primeiroDiaQuinzenal;
	Integer segundoDiaQuinzenal;
	List<Integer> diasDoMes;
	
	boolean domingo;
	boolean segunda;
	boolean terca;
	boolean quarta;
	boolean quinta;
	boolean sexta;
	boolean sabado;
	
	List<Long> fornecedoresId;
	Set<Fornecedor> fornecedores;
	Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota;
	
	private String unificacao;
	
	public FormaCobrancaDTO(){
		
	}

	//CONTRUTOR PARA DADOS DA GRID
	public FormaCobrancaDTO(Long idFormaCobranca, 
			String fornecedor,
			String concentracaoPagto, 
			String tipoPagto, 
			String detalhesTipoPagto,
			boolean parametroDistribuidor,
			String descUnificacao) {
		super();
		this.idFormaCobranca = idFormaCobranca;
		this.fornecedor = fornecedor;
		this.concentracaoPagto = concentracaoPagto;
		this.tipoPagto = tipoPagto;
		this.detalhesTipoPagto = detalhesTipoPagto;
		this.parametroDistribuidor = parametroDistribuidor;
		this.unificacao = descUnificacao;
	}
	
	//CONTRUTOR PARA O FORMULARIO
	public FormaCobrancaDTO(long idCota, long idParametroCobranca, TipoCobranca tipoCobranca, 
			TipoFormaCobranca tipoFormaCobranca, Long idBanco,
			boolean recebeEmail, String numBanco, String nomeBanco,
			Long agencia, String agenciaDigito, Long conta,
			String contaDigito, Integer diaDoMes, Integer primeiroDiaQuinzenal, Integer segundoDiaQuinzenal, boolean domingo, boolean segunda,
			boolean terca, boolean quarta, boolean quinta, boolean sexta,
			boolean sabado) {
		super();
		this.idCota = idCota;
		this.idParametroCobranca = idParametroCobranca;
		this.tipoCobranca = tipoCobranca;
		this.tipoFormaCobranca = tipoFormaCobranca;
		this.idBanco = idBanco;
		this.recebeEmail = recebeEmail;
		this.numBanco = numBanco;
		this.nomeBanco = nomeBanco;
		this.agencia = agencia;
		this.agenciaDigito = agenciaDigito;
		this.conta = conta;
		this.contaDigito = contaDigito;
		this.diaDoMes = diaDoMes;
		this.primeiroDiaQuinzenal = primeiroDiaQuinzenal;
		this.segundoDiaQuinzenal = segundoDiaQuinzenal;
		this.domingo = domingo;
		this.segunda = segunda;
		this.terca = terca;
		this.quarta = quarta;
		this.quinta = quinta;
		this.sexta = sexta;
		this.sabado = sabado;
	}

	public Long getIdFormaCobranca() {
		return idFormaCobranca;
	}

	public void setIdFormaCobranca(Long idFormaCobranca) {
		this.idFormaCobranca = idFormaCobranca;
	}
	
	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdParametroCobranca() {
		return idParametroCobranca;
	}

	public void setIdParametroCobranca(Long idParametroCobranca) {
		this.idParametroCobranca = idParametroCobranca;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getConcentracaoPagto() {
		return concentracaoPagto;
	}

	public void setConcentracaoPagto(String concentracaoPagto) {
		this.concentracaoPagto = concentracaoPagto;
	}

	public String getTipoPagto() {
		return tipoPagto;
	}

	public void setTipoPagto(String tipoPagto) {
		this.tipoPagto = tipoPagto;
	}

	public String getDetalhesTipoPagto() {
		return detalhesTipoPagto;
	}

	public void setDetalhesTipoPagto(String detalhesTipoPagto) {
		this.detalhesTipoPagto = detalhesTipoPagto;
	}

	public boolean isParametroDistribuidor() {
		return parametroDistribuidor;
	}

	public void setParametroDistribuidor(boolean parametroDistribuidor) {
		this.parametroDistribuidor = parametroDistribuidor;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
		if (this.tipoCobranca != null) {
		    setTipoPagto(this.tipoCobranca.getDescTipoCobranca());
		}
	}

	public TipoFormaCobranca getTipoFormaCobranca() {
		return tipoFormaCobranca;
	}

	public void setTipoFormaCobranca(TipoFormaCobranca tipoFormaCobranca) {
		this.tipoFormaCobranca = tipoFormaCobranca;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public boolean isRecebeEmail() {
		return recebeEmail;
	}

	public void setRecebeEmail(boolean recebeEmail) {
		this.recebeEmail = recebeEmail;
	}

	public String getNumBanco() {
		return numBanco;
	}

	public void setNumBanco(String numBanco) {
		this.numBanco = numBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public Long getAgencia() {
		return agencia;
	}

	public void setAgencia(Long agencia) {
		this.agencia = agencia;
	}

	public String getAgenciaDigito() {
		return agenciaDigito;
	}

	public void setAgenciaDigito(String agenciaDigito) {
		this.agenciaDigito = agenciaDigito;
	}

	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
	}

	public String getContaDigito() {
		return contaDigito;
	}

	public void setContaDigito(String contaDigito) {
		this.contaDigito = contaDigito;
	}

	public Integer getDiaDoMes() {
		return diaDoMes;
	}

	public void setDiaDoMes(Integer diaDoMes) {
		this.diaDoMes = diaDoMes;
	}

	public Integer getPrimeiroDiaQuinzenal() {
		return primeiroDiaQuinzenal;
	}

	public void setPrimeiroDiaQuinzenal(Integer primeiroDiaQuinzenal) {
		this.primeiroDiaQuinzenal = primeiroDiaQuinzenal;
	}

	public Integer getSegundoDiaQuinzenal() {
		return segundoDiaQuinzenal;
	}

	public void setSegundoDiaQuinzenal(Integer segundoDiaQuinzenal) {
		this.segundoDiaQuinzenal = segundoDiaQuinzenal;
	}

	public boolean isDomingo() {
		return domingo;
	}

	public void setDomingo(boolean domingo) {
		this.domingo = domingo;
	}

	public boolean isSegunda() {
		return segunda;
	}

	public void setSegunda(boolean segunda) {
		this.segunda = segunda;
	}

	public boolean isTerca() {
		return terca;
	}

	public void setTerca(boolean terca) {
		this.terca = terca;
	}

	public boolean isQuarta() {
		return quarta;
	}

	public void setQuarta(boolean quarta) {
		this.quarta = quarta;
	}

	public boolean isQuinta() {
		return quinta;
	}

	public void setQuinta(boolean quinta) {
		this.quinta = quinta;
	}

	public boolean isSexta() {
		return sexta;
	}

	public void setSexta(boolean sexta) {
		this.sexta = sexta;
	}

	public boolean isSabado() {
		return sabado;
	}

	public void setSabado(boolean sabado) {
		this.sabado = sabado;
	}

	public List<Long> getFornecedoresId() {
		return fornecedoresId;
	}

	public void setFornecedoresId(List<Long> fornecedoresId) {
		this.fornecedoresId = fornecedoresId;
	}

    public void addIdFornecedor(Long id) {
       if (fornecedoresId == null) {
           fornecedoresId = new ArrayList<Long>();
       }
       fornecedoresId.add(id);
    }

	public Set<ConcentracaoCobrancaCota> getConcentracaoCobrancaCota() {
		return concentracaoCobrancaCota;
	}

	public void setConcentracaoCobrancaCota(
			Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota) {
		this.concentracaoCobrancaCota = concentracaoCobrancaCota;
	}

	public List<Integer> getDiasDoMes() {
		return diasDoMes;
	}

	public void setDiasDoMes(List<Integer> diasDoMes) {
		this.diasDoMes = diasDoMes;
	}

	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}
	
	public void addFornecedor(Fornecedor fornecedor) {
		
		if (this.fornecedores == null) {
			
			this.fornecedores = new HashSet<Fornecedor>();			
		}

		if (this.fornecedor == null) {
			
			this.fornecedor = "";
		
		} else if (!this.fornecedor.isEmpty()) {
			
			this.fornecedor += "/";
		}
		
		this.fornecedor += fornecedor.getJuridica().getNomeFantasia();

		this.fornecedores.add(fornecedor);
	}

	public String getUnificacao() {
		return unificacao;
	}

	public void setUnificacao(String unificacao) {
		this.unificacao = unificacao;
	}

}
