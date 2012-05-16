package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class ConferenciaEncalheServiceImpl implements ConferenciaEncalheService {
	

	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Autowired 
	private DistribuidorService distribuidorService; 
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private ConferenciaEncalheRepository conferenciaEncalheRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterListaBoxEncalhe()
	 */
	@Transactional(readOnly = true)
	public List<Box> obterListaBoxEncalhe(Long idUsuario) {
	
		List<Box> listaBoxEncalhe = boxRepository.obterListaBox(TipoBox.RECOLHIMENTO);
		
		if (idUsuario == null){
			
			return listaBoxEncalhe;
		}
		
		String codigoBoxPadraoUsuario = this.obterBoxPadraoUsuario(idUsuario);
		
		if (codigoBoxPadraoUsuario == null){
			
			return listaBoxEncalhe;
		}
		
		List<Box> boxes = new ArrayList<Box>();
			
		for (Box box : listaBoxEncalhe){
			
			if (box.getCodigo().equals(codigoBoxPadraoUsuario)){
				
				boxes.add(box);
				listaBoxEncalhe.remove(box);
				break;
			}
		}
		
		boxes.addAll(listaBoxEncalhe);
		
		return boxes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterBoxPadraoUsuario(java.lang.Long)
	 */
	public String obterBoxPadraoUsuario(Long idUsuario) {
		
		/*List<Box> listaBoxEncalhe = boxRepository.obterBoxUsuario(idUsuario, TipoBox.RECOLHIMENTO);
		
		if(listaBoxEncalhe != null && !listaBoxEncalhe.isEmpty()) {
			
			Box boxRecolhimentoUsuario = listaBoxEncalhe.get(0);
			
			return boxRecolhimentoUsuario.getCodigo();
		}*/
		
		if (idUsuario == null){
			
			return null;
		}
		
		return this.boxRepository.obterCodigoBoxPadraoUsuario(idUsuario);
		
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConferenciaEncalheService#obterInfoConferenciaEncalheCota(java.lang.Integer)
	 */
	@Transactional(readOnly = true)
	public InfoConferenciaEncalheCota obterInfoConferenciaEncalheCota(Integer numeroCota) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		int qtdDiasEncalheAtrasadoAceitavel = distribuidor.getQtdDiasEncalheAtrasadoAceitavel();
		
		ControleConferenciaEncalheCota controleConferenciaEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCota(numeroCota, dataOperacao);
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = new InfoConferenciaEncalheCota();
		
		if(controleConferenciaEncalheCota!=null) {
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO = 
					conferenciaEncalheRepository.obterListaConferenciaEncalheDTO(
							controleConferenciaEncalheCota.getId(), 
							distribuidor.getId());
			
			infoConfereciaEncalheCota.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
			
			infoConfereciaEncalheCota.setEncalhe(null);//TODO: calcular este valor no front end...
			
			
		} else {
			
			infoConfereciaEncalheCota.setEncalhe(BigDecimal.ZERO);
			
		}
		
		List<Long> listaIdProdutoEdicao = 
				chamadaEncalheCotaRepository.obterListaIdProdutoEdicaoChamaEncalheCota(numeroCota, dataOperacao, false, false);
		BigDecimal reparte = estoqueProdutoCotaRepository.obterValorTotalReparteCota(numeroCota, listaIdProdutoEdicao, distribuidor.getId());
		
		BigDecimal totalDebitoCreditoCota = null;//TODO sumarizado no front end...
		BigDecimal valorPagar = null;//TODO sumarizado no front end...
		BigDecimal valorVendaDia = null;//TODO sumarizado no front end...
		
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
	
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroRecebimentoReparte);
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = 
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(
						numeroCota, 
						dataOperacao, 
						tiposMovimentoFinanceiroIgnorados);
		
		infoConfereciaEncalheCota.setListaDebitoCreditoCota(listaDebitoCreditoCota);
		
		infoConfereciaEncalheCota.setReparte(reparte);
		
		infoConfereciaEncalheCota.setTotalDebitoCreditoCota(totalDebitoCreditoCota);
		
		infoConfereciaEncalheCota.setValorPagar(valorPagar);
		
		infoConfereciaEncalheCota.setValorVendaDia(valorVendaDia);
		
		return infoConfereciaEncalheCota;
		
	}
	
	
	@Transactional(readOnly = true)
	public ProdutoEdicao pesquisarProdutoEdicaoPorId(Integer numeroCota, Long idProdutoEdicao) throws ChamadaEncalheCotaInexistenteException {
		
		if (numeroCota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (idProdutoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Prdoduto Edição é obrigatório.");
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		if (produtoEdicao != null) {
		
			this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, idProdutoEdicao);
			
			carregarValorDesconto(produtoEdicao, numeroCota);
			
		}
		
		return produtoEdicao;
	}

	private void carregarValorDesconto(ProdutoEdicao produtoEdicao, Integer numeroCota) {
		
		BigDecimal hundred = new BigDecimal(100.0);
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		BigDecimal fatorDesconto = produtoEdicaoRepository.obterFatorDesconto(produtoEdicao.getId(), numeroCota, distribuidor.getId());
		
		if( fatorDesconto!=null && produtoEdicao.getPrecoVenda()!=null ) {
			
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			
			BigDecimal precoComDesconto = precoVenda.subtract(fatorDesconto.divide(hundred).multiply(precoVenda));
			
			produtoEdicao.setDesconto(precoComDesconto);
			
		}
		
		
	}
	
	@Transactional(readOnly = true)
	public ProdutoEdicao pesquisarProdutoEdicaoPorCodigoDeBarras(Integer numeroCota, String codigoDeBarras) throws ChamadaEncalheCotaInexistenteException {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (codigoDeBarras == null || codigoDeBarras.trim().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Código de Barras é obrigatório.");
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorCodigoBarra(codigoDeBarras);
		
		if (produtoEdicao != null){
		
			this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, produtoEdicao.getId());
			
			carregarValorDesconto(produtoEdicao, numeroCota);
		}
		
		return produtoEdicao;
	}
	
	@Transactional(readOnly = true)
	public ProdutoEdicao pesquisarProdutoEdicaoPorSM(Integer numeroCota, Long sm) throws ChamadaEncalheCotaInexistenteException {
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório.");
		}
		
		if (sm == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "SM é obrigatório.");
		}
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorSM(sm);
		
		if (produtoEdicao != null){
		
			this.validarExistenciaChamadaEncalheParaCotaProdutoEdicao(numeroCota, produtoEdicao.getId());
			
			carregarValorDesconto(produtoEdicao, numeroCota);
		}
		
		return produtoEdicao;
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
