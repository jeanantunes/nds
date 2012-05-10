package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.util.DateUtil;

@Service
public class ConferenciaEncalheServiceImpl implements ConferenciaEncalheService {
	
	//TODO - Remover apos testes
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
	
	//TODO - Remover apos testes
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
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Autowired 
	private DistribuidorService distribuidorService; 
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterListaBoxEncalhe()
	 */
	public List<Box> obterListaBoxEncalhe() {
	
		List<Box> listaBoxEncalhe = boxRepository.obterListaBox(TipoBox.RECOLHIMENTO);
	
		return listaBoxEncalhe;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterBoxPadraoUsuario(java.lang.Long)
	 */
	public String obterBoxPadraoUsuario(Long idUsuario) {
		
		List<Box> listaBoxEncalhe = boxRepository.obterBoxUsuario(idUsuario, TipoBox.RECOLHIMENTO);
		
		if(listaBoxEncalhe != null && !listaBoxEncalhe.isEmpty()) {
			
			Box boxRecolhimentoUsuario = listaBoxEncalhe.get(0);
			
			return boxRecolhimentoUsuario.getCodigo();
		}
		
		return null;
		
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#verificarChamadaEncalheCota(java.lang.Integer)
	 */
	public void verificarChamadaEncalheCota(Integer numeroCota) throws ConferenciaEncalheExistenteException, ChamadaEncalheCotaInexistenteException {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		int qtdDiasEncalheAtrasadoAceitavel = distribuidor.getQtdDiasEncalheAtrasadoAceitavel();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		if(controleConferenciaEncalheCota!=null) {
			throw new ConferenciaEncalheExistenteException();
		}
		
		Date dataRecolhimentoReferencia = DateUtil.subtrairDias(dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
		
		boolean encalheConferido = false;
		
		boolean indPesquisaCEFutura = true;
		
		Long qtdeRegistroChamadaEncalhe = 
				chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(numeroCota, dataRecolhimentoReferencia, null, indPesquisaCEFutura, encalheConferido);
		
		if(qtdeRegistroChamadaEncalhe == 0L) {
			throw new ChamadaEncalheCotaInexistenteException();
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#validarExistenciaChamadaEncalheParaCotaProdutoEdicao(java.lang.Integer, java.lang.Long)
	 */
	public void validarExistenciaChamadaEncalheParaCotaProdutoEdicao(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException {
		
		boolean encalheConferido = false;
		
		boolean indPesquisaCEFutura = true;

		Distribuidor distribuidor = distribuidorService.obter();
		Date dataOperacao = distribuidor.getDataOperacao();
		int qtdDiasEncalheAtrasadoAceitavel = distribuidor.getQtdDiasEncalheAtrasadoAceitavel();
		Date dataRecolhimentoReferencia = DateUtil.subtrairDias(dataOperacao, qtdDiasEncalheAtrasadoAceitavel);
		
		Long qtdeRegistroChamadaEncalhe = 
				chamadaEncalheCotaRepository.
				obterQtdListaChamaEncalheCota(numeroCota, dataRecolhimentoReferencia, idProdutoEdicao, indPesquisaCEFutura, encalheConferido);
		
		
		if(qtdeRegistroChamadaEncalhe == 0) {
			throw new ChamadaEncalheCotaInexistenteException();
		}
		
		
	}


	
	public boolean verificarCotaEmiteNFe() {
		return false;
	}

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
