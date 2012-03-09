package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.ItemNotaFiscalRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class RecebimentoFisicoServiceImpl implements RecebimentoFisicoService {

	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	
	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	private ItemNotaFiscalRepository itemNotaFiscalRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@Autowired
	private CFOPRepository cFOPRepository;
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	
	@Transactional
	public void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico){
		
	}
		
	/**
	* Obtem lista com dados de itemRecebimento relativos ao id de uma nota fiscal.
	* 
	* @param idNotaFiscal
	* 
	* @return List - RecebimentoFisicoDTO
	* 
	*/
	@Transactional
	public List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal) {
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisico = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(idNotaFiscal);
		
		if(listaItemRecebimentoFisico == null) {
			return null;
		}
		
		for(RecebimentoFisicoDTO itemRecebimento : listaItemRecebimentoFisico) {
			
			BigDecimal qtdRepartePrevisto = itemRecebimento.getRepartePrevisto();
			
			BigDecimal precoCapa = itemRecebimento.getPrecoCapa();
			
			BigDecimal valorTotal = new BigDecimal(0.0D);
			
			if(qtdRepartePrevisto != null || precoCapa != null) {
				
				valorTotal = qtdRepartePrevisto.multiply(precoCapa);
			
			}
			
			itemRecebimento.setValorTotal(valorTotal);
			
		}
		
		return listaItemRecebimentoFisico;
		
	}
	
	@Transactional
	public List<CFOP> obterListaCFOP() {
		return cFOPRepository.buscarTodos();
	}
	
	@Transactional
	public List<TipoNotaFiscal> obterListaTipoNotaFiscal(TipoOperacao tipoOperacao) {
		return tipoNotaFiscalRepository.obterTiposNotasFiscais();
	}
	
	/**
	 * Obtém a pessoa jurídica através do CNPJ.
	 * 
	 * @param cnpj
	 * @return PessoaJuridica
	 */
	private PessoaJuridica obterPessoaPorCNPJ(String cnpj) {
		return pessoaJuridicaRepository.buscarPorCnpj(cnpj);
	}
	
	/**
	 * Atualiza os dados de uma nota fiscal existente.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param listaItensNota
	 */
	private void atualizarDadosNotaFiscalExistente(Usuario usuarioLogado, NotaFiscal notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual) {
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
		
		if(recebimentoFisico == null){
			recebimentoFisico = inserirRecebimentoFisico(usuarioLogado, notaFiscal, dataAtual);
		}
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota){
			
			ItemNotaFiscal itemNota = null;
			
			if(recebimentoFisicoDTO.getIdItemNota() == null) {
				itemNota = incluirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO);
			} else {
				itemNota = new ItemNotaFiscal();
				itemNota.setId(recebimentoFisicoDTO.getIdItemNota());
			}
			
			inserirItemRecebimentoFisico(usuarioLogado, recebimentoFisicoDTO, itemNota, recebimentoFisico);

			if(recebimentoFisicoDTO.getIdItemRecebimentoFisico() == null) {
				inserirLancamento(recebimentoFisicoDTO, dataAtual);
			} 
			
		}
		
	}
	
	/**
	 * Insere os dados de uma nova nota fiscal, incluindo 
	 * informações de diferença e lançamento.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param listaItensNota
	 */
	private void inserirDadosNovaNotaFiscal(Usuario usuarioLogado, NotaFiscal notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual) {
		
		String cnpj = notaFiscal.getEmitente().getCnpj();
		
		PessoaJuridica emitente = obterPessoaPorCNPJ(cnpj);
		
		if(emitente == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "CNPJ não corresponde a Pessoa Jurídica cadastrada.");
		}
		
		notaFiscal.setStatusNotaFiscal(StatusNotaFiscal.RECEBIDA);
		
		notaFiscal.setOrigem(Origem.MANUAL);
		
		notaFiscal.setEmitente(emitente);
		
		notaFiscalRepository.adicionar(notaFiscal);
		
		RecebimentoFisico recebimentoFisico = inserirRecebimentoFisico(usuarioLogado, notaFiscal, dataAtual);	
		
		for( RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota ){
			
			ItemNotaFiscal itemNotaFiscal = incluirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO);
			
			inserirItemRecebimentoFisico(usuarioLogado, recebimentoFisicoDTO, itemNotaFiscal, recebimentoFisico);
			
			inserirLancamento(recebimentoFisicoDTO, dataAtual);
		}
		
	}
	
	/**
	 * Insere os dados do recebimento físico.
	 */
	@Transactional
	public void inserirDadosRecebimentoFisico(Usuario usuarioLogado, NotaFiscal notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual){
		
		if(notaFiscal == null || listaItensNota == null) {
			return;
		}
		
		if(notaFiscal.getId() != null) {
			
			atualizarDadosNotaFiscalExistente(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
			
		} else {
			
			notaFiscal.setDataExpedicao(dataAtual);
			
			inserirDadosNovaNotaFiscal(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
			
		}
		
	}
	
	
	
	/**
	 * Insere um itemNotaFiscal.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param recebimentoDTO
	 * 
	 * @return ItemNotaFiscal
	 */
	private ItemNotaFiscal incluirItemNotaFiscal(Usuario usuarioLogado, NotaFiscal notaFiscal, RecebimentoFisicoDTO recebimentoDTO){
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoDTO.getIdProdutoEdicao());
		
		ItemNotaFiscal itemNota = new ItemNotaFiscal();		
		
		itemNota.setQtde(recebimentoDTO.getRepartePrevisto());			
		itemNota.setDataLancamento(recebimentoDTO.getDataLancamento());
		itemNota.setQtde(recebimentoDTO.getRepartePrevisto());
		itemNota.setProdutoEdicao(produtoEdicao);
		itemNota.setUsuario(usuarioLogado);
		itemNota.setNotaFiscal(notaFiscal);
		itemNota.setOrigem(Origem.MANUAL);
		itemNotaFiscalRepository.adicionar(itemNota);
				
		return itemNota;		
	}
	
	/**
	 * Insere um recebimento físico.
	 * 
	 * @param userLogado
	 * @param notaFiscal
	 * @param dataAtual
	 * 
	 * @return RecebimentoFisico
	 */
	private RecebimentoFisico inserirRecebimentoFisico(Usuario userLogado, NotaFiscal notaFiscal, Date dataAtual){
		
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		
		recebimentoFisico.setDataRecebimento(dataAtual);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		recebimentoFisico.setNotaFiscal(notaFiscal);
		recebimentoFisico.setRecebedor(userLogado);
		
		recebimentoFisicoRepository.adicionar(recebimentoFisico);
		
		return recebimentoFisico;	
		
	}
	
	
	/**
	 * Cria um registro de lançamento ligado ao itemRecebimentoFisico.
	 * 
	 * @param recebimentoFisicoDTO
	 * @param dataAtual
	 */
	@Transactional
	private void inserirLancamento(RecebimentoFisicoDTO recebimentoFisicoDTO, Date dataAtual) {
		
		Lancamento lancamento = new Lancamento();
		
		lancamento.setDataLancamentoDistribuidor(recebimentoFisicoDTO.getDataLancamento());
		lancamento.setDataRecolhimentoDistribuidor(recebimentoFisicoDTO.getDataRecolhimento());
		lancamento.setDataCriacao(dataAtual);
		lancamento.setDataLancamentoPrevista(recebimentoFisicoDTO.getDataLancamento());
		lancamento.setDataLancamentoDistribuidor(dataAtual);
		lancamento.setDataRecolhimentoPrevista(recebimentoFisicoDTO.getDataRecolhimento());
		lancamento.setTipoLancamento(recebimentoFisicoDTO.getTipoLancamento());		
		lancamento.setReparte(recebimentoFisicoDTO.getRepartePrevisto());		
		lancamento.setStatus(StatusLancamento.RECEBIDO);
		lancamento.setDataStatus(dataAtual);
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoFisicoDTO.getIdProdutoEdicao());
		
		lancamento.setProdutoEdicao(produtoEdicao);
		
		lancamentoRepository.adicionar(lancamento);
		
	}
	
	
	/**
	 * Faz a inserção da falta ou sobra referente a um itemRecebimentoFisico e atualiza o registro
	 * de itemRecebimentoFisico com o lançamento da diferença.
	 * 
	 * @param usuarioLogado
	 * @param recebimentoFisicoDTO
	 * @param itemRecebimentoFisico
	 */
	private void lancarFaltaOUSobra(
			Usuario usuarioLogado,
			RecebimentoFisicoDTO recebimentoFisicoDTO,
			ItemRecebimentoFisico itemRecebimentoFisico) {
		
		BigDecimal calculoQdeDiferenca = recebimentoFisicoDTO.getQtdFisico().subtract(recebimentoFisicoDTO.getRepartePrevisto()) ;

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoFisicoDTO.getIdProdutoEdicao());					
		
		Diferenca diferenca = new Diferenca();
		
		diferenca.setQtde(calculoQdeDiferenca);
		diferenca.setItemRecebimentoFisico(itemRecebimentoFisico);
		diferenca.setResponsavel(usuarioLogado);
		diferenca.setProdutoEdicao(produtoEdicao);
		diferenca.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		
		if( calculoQdeDiferenca.compareTo(new BigDecimal(0)) < 0 ){
		
			diferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE);
			
			diferencaEstoqueRepository.adicionar(diferenca);						
			
		} else if(calculoQdeDiferenca.compareTo(new BigDecimal(0)) > 0){						
			
			diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_DE);
			
			diferencaEstoqueRepository.adicionar(diferenca);
			
		}
		
		//TODO: VERIFICAR SE A LINHA ABAIXO É NECESSARIA.
		itemRecebimentoFisicoRepository.alterar(itemRecebimentoFisico);
		
	}
	
	
	/**
	 * Faz a inserção de um itemRecebimento, apontando também 
	 * a falta ou sobra referente ao mesmo.
	 *  
	 * @param usuarioLogado
	 * @param recebimentoDTO
	 * @param itemNotaFiscal
	 * @param recebimentoFisico
	 */
	private void inserirItemRecebimentoFisico( 
			Usuario usuarioLogado, 
			RecebimentoFisicoDTO recebimentoDTO, 
			ItemNotaFiscal itemNotaFiscal, 
			RecebimentoFisico recebimentoFisico ) {
		
		ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
		
		itemRecebimento.setDiferenca(null);
		itemRecebimento.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimento.setQtdeFisico(recebimentoDTO.getQtdFisico());
		itemRecebimento.setRecebimentoFisico(recebimentoFisico);
		
		itemRecebimentoFisicoRepository.adicionar(itemRecebimento);
		
		lancarFaltaOUSobra(usuarioLogado, recebimentoDTO, itemRecebimento);
		
	}

	@Override
	@Transactional
	public List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao) {
		
		return tipoNotaFiscalRepository.obterTiposNotasFiscais(tipoOperacao);
		
	}
	
}
