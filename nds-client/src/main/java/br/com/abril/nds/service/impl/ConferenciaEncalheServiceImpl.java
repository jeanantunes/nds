package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.service.ConferenciaEncalheService;

@Service
public class ConferenciaEncalheServiceImpl implements ConferenciaEncalheService {
	
	//TODO
	private List<ConferenciaEncalheDTO> obterListaConferenciaEncalheMockada() {
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalhe = new ArrayList<ConferenciaEncalheDTO>();
		
		int contador = 0;
		
		ConferenciaEncalheDTO conferencia = null;
		
		while(contador++ < 10) {
			
			conferencia = new ConferenciaEncalheDTO();
			
			conferencia.setCodigo(""+contador);
			conferencia.setCodigoDeBarras(""+contador);
			conferencia.setCodigoSM(1L+contador);
			conferencia.setDesconto(BigDecimal.ONE);
			conferencia.setDia(1);
			conferencia.setJuramentada(true);
			conferencia.setNomeProduto("PRODUTONOME_"+contador);
			conferencia.setNumeroEdicao(1L+contador);
			conferencia.setPrecoCapa(BigDecimal.ONE);
			conferencia.setQtdExemplar(BigDecimal.TEN);
			conferencia.setValorTotal(BigDecimal.ONE);
			
			listaConferenciaEncalhe.add(conferencia);
			
		}
		
		return listaConferenciaEncalhe;
		
	}
	
	//TODO
	private List<DebitoCreditoCotaDTO> obterListaDebitoCreditoCotaMockada() {
		
		String[] tipoOperacao = {"DEBITO", "CREDITO"};
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = new ArrayList<DebitoCreditoCotaDTO>();
		
		int contador = 0;
		
		DebitoCreditoCotaDTO debitoCreditoCota = null;
		
		while(contador++ < 10) {
			
			debitoCreditoCota = new DebitoCreditoCotaDTO();
			
			debitoCreditoCota.setTipoLancamento(tipoOperacao[(contador%2)]);
			debitoCreditoCota.setValor(new BigDecimal(contador));
			debitoCreditoCota.setDataLancamento(new Date());
			debitoCreditoCota.setDataVencimento(new Date());
			debitoCreditoCota.setNumeroCota(123);
	
			listaDebitoCreditoCota.add(debitoCreditoCota);
			
		}
		
		return listaDebitoCreditoCota;
		
	}
	
	public List<Box> obterListaBoxEncalhe() {
		//TODO
		return null;
	}
	
	public String obterBoxPadraoUsuario(Long idUsuario) {
		//TODO
		return null;
	}

   /*
	* Verifica se o encalhe para esta cota ja foi conferido,
	* caso positivo retorna true.
	* 
	* Lancara uma exception caso: 
	* 	Não haja chamada de encalhe prevista (or) 
	*   Não possa reabrir conferencia devido a data de operacao. 
	*/
	public boolean verificarCotaProcessada(Integer numeroCota) {
		//TODO
		return false;
	}

   /*
	* Verifica cota emite NFe.  
	* Caso positivo retorna true
	*/
	public boolean verificarCotaEmiteNFe() {
		//TODO
		return false;
	}

	/**
	 * 
	 */
	public void inserirDadosNotaFiscalCota() {
		
	}
	

   /**
	* Obtem os dados sumarizados de encalhe da cota, e se esta estiver
	* com sua conferencia reaberta retorna tambem a lista do que ja foi
	* conferido.
	*/
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota() {
		
		//TODO
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = new InfoConferenciaEncalheCota();
		
		BigDecimal encalhe = new BigDecimal(25.4D);
		BigDecimal reparte = new BigDecimal(25.4D);
		BigDecimal totalDebitoCreditoCota = new BigDecimal(90.2D);
		BigDecimal valorPagar = new BigDecimal(50.2D);
		BigDecimal valorVendaDia = new BigDecimal(89.5D);
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalhe = obterListaConferenciaEncalheMockada();
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = obterListaDebitoCreditoCotaMockada();
		
		infoConfereciaEncalheCota.setEncalhe(encalhe);
		
		infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalhe);
		infoConfereciaEncalheCota.setListaDebitoCreditoCota(listaDebitoCreditoCota);
		
		infoConfereciaEncalheCota.setReparte(reparte);
		infoConfereciaEncalheCota.setTotalDebitoCreditoCota(totalDebitoCreditoCota);
		infoConfereciaEncalheCota.setValorPagar(valorPagar);
		infoConfereciaEncalheCota.setValorVendaDia(valorVendaDia);
		
		return infoConfereciaEncalheCota;
		
	}
	
	
	public ProdutoEdicao pesquisarProdutoEdicaoPorCodigoDeBarras(String codigoDeBarras) {
		//TODO
		return null;
	}
	
	public ProdutoEdicao pesquisarProdutoEdicaoPorSM() {
		//TODO
		return null;
	}
	
	/*
	 * Traz uma lista de codigoProduto - nomeProduto -  numeroEdicao
	 */
	public Object obterListaDadosProdutoEdicao(String codigoOuNome) {
		//TODO
		return null;
	}
	
   /*
	* Cada produto que é adicionado na conferencia de encalhe dever ser                 
	* verificado se existe uma chamada de encalhe para o mesmo.                         
	*                                                                                
	* Retorna dados do produto caso o mesmo esteja na chamada de encalhe em andamento.  
	*/
	public void verificarProdutoExistenciaChamadaEncalhe(Long idProdutoEdicao) {
		
	}
	
	
	public void salvarDadosConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
	}
	
	// (caso valor nota esteja diferente do encalhe requisitar correcao)
	public void finalizarConferenciaEncalhe(List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
	}
	
	private void gerarDiferencas() {
		//TODO
	}
	
	private void gerarDivida() {
		
	}
	
	private void gerarDividaChamadaEncalheAntecipada() {
		
	}
	
   /*
	* Apos finalizar conferencia de encalhe sera verificado        
	* quais documentos serao gerados e se os mesmos serao impressos
	* ou enviados por email.                                       
	*/
	private void gerarDocumentosConferenciaEncalhe() {
		//TODO
	}
	
	private void gerarSlip() {
		//TODO
	}
	
	private void gerarBoleto() {
		//TODO
	}
	
	private void gerarRecibo() {
		//TODO
	}
	
}
