package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CFOPRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.ItemNotaFiscalRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class RecebimentoFisicoServiceImpl implements RecebimentoFisicoService {

	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
	
	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	@Autowired
	private ItemNotaFiscalRepository itemNotaFiscalRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
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
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	
		
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
			
			if(qtdRepartePrevisto != null && precoCapa != null) {
				
				valorTotal = qtdRepartePrevisto.multiply(precoCapa);
			
			}
			
			itemRecebimento.setValorTotal(valorTotal);
			
		}
		
		return listaItemRecebimentoFisico;
		
	}
	
	private void validarExisteNotaConfirmada(Long idNotaFiscal){
		
		if(idNotaFiscal == null){
			return;
		}
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(idNotaFiscal);
		
		if(recebimentoFisico != null){

			if(StatusConfirmacao.CONFIRMADO.equals(recebimentoFisico.getStatusConfirmacao())){
				throw new ValidacaoException(TipoMensagem.WARNING, "O Recebimento ja foi confirmado, não é possível alterar os dados do mesmo.");
			}
		}
	}
	
	/**
	 * Insere os dados do recebimento físico.
	 */
	@Transactional
	public void inserirDadosRecebimentoFisico(Usuario usuarioLogado, NotaFiscal notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual){
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Nota fiscal não existente.");
		}
		
		validarExisteNotaConfirmada(notaFiscal.getId());
		
		if(listaItensNota == null || listaItensNota.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem itens relativos a esta nota fiscal");
		}
		
		if(notaFiscal.getId() != null) {
			
			RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
			
			if(recebimentoFisico != null){

				verificarExclusao(notaFiscal.getId(), listaItensNota);

			}
				
			atualizarDadosNotaFiscalExistente(recebimentoFisico, usuarioLogado, notaFiscal, listaItensNota, dataAtual);
			
		} else {
			
			notaFiscal.setDataExpedicao(dataAtual);
			
			inserirDadosNovaNotaFiscal(usuarioLogado, notaFiscal, listaItensNota, dataAtual);			
		}		
	}
	
	/**
	 * Confirmação de RecebimentoFisico
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param listaItensNota
	 * @param dataAtual
	 */
	@Transactional
	public void confirmarRecebimentoFisico(Usuario usuarioLogado, NotaFiscal notaFiscal, List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual){
		
		verificarValorDaNota(listaItensNota,notaFiscal.getValorBruto());
		
		inserirDadosRecebimentoFisico(usuarioLogado, notaFiscal, listaItensNota, dataAtual);
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisico = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(notaFiscal.getId());

		if(listaItemRecebimentoFisico == null || listaItemRecebimentoFisico.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem itens para confirmação de recebimento físico nesta nota fiscal");
		}
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItemRecebimentoFisico) {

			inserirMovimentoEstoque(usuarioLogado, recebimentoFisicoDTO);
			
			inserirLancamento(recebimentoFisicoDTO, dataAtual, usuarioLogado);
			
		}
		
		alterarRecebimentoFisicoParaConfirmado(usuarioLogado, notaFiscal, dataAtual);	
		
	}
	/**
	 * Método que compara o valor bruto da nota com a soma dos valores dos itens
	 * @param listaItensNota
	 * @param valorBruto
	 */
	private void verificarValorDaNota(List<RecebimentoFisicoDTO> listaItensNota, BigDecimal valorBruto) {
		
		BigDecimal somaValorDosItens = new BigDecimal(0.0);
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota) {
			somaValorDosItens = somaValorDosItens.add(recebimentoFisicoDTO.getValorTotal());
		}
		
		if(valorBruto == null){
			valorBruto = new BigDecimal(0.0);
		}
		
		Double parseSoma = somaValorDosItens.doubleValue();
		Double parseValorBruto = valorBruto.doubleValue();
		
		if(!parseSoma.equals(parseValorBruto)){
			throw new ValidacaoException(TipoMensagem.ERROR,"Valor Bruto da Nota não corresponde a soma dos valores Totais do Item da Nota");
		}
	}

	@Override
	@Transactional
	public List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao) {
		
		return tipoNotaFiscalRepository.obterTiposNotasFiscais(tipoOperacao);
		
	}
	
	/**
	 * Exclui Itens da Nota e RecebimentoFisico
	 */	
	private void excluirItem(RecebimentoFisicoDTO recebimentoFisicoDTO){
		
		if(	recebimentoFisicoDTO != null && 
			recebimentoFisicoDTO.getIdItemRecebimentoFisico() != null && 
			recebimentoFisicoDTO.getIdItemNota() != null ){
			
			if (recebimentoFisicoDTO.getOrigemItemNota() != null) {

				if (recebimentoFisicoDTO.getOrigemItemNota().equals(Origem.MANUAL)) {
					
					excluirItemRecebimentoFisico(recebimentoFisicoDTO);

					excluirItemNotaFiscal(recebimentoFisicoDTO.getIdItemNota());

				} else {
					throw new ValidacaoException(TipoMensagem.ERROR,
							"Item Nota Fiscal Interface não pode ser excluida");
				}
			}
		
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Item Nota Fiscal não existente");
		}
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
	
	
	private void verificarExclusao(Long idNotaFiscal, List<RecebimentoFisicoDTO> listaItensNotaAtual){
		
		List<RecebimentoFisicoDTO> listaExclusao = new ArrayList<RecebimentoFisicoDTO>();
		
		List<RecebimentoFisicoDTO> listaItemRecebimentoFisicoBD = recebimentoFisicoRepository.obterListaItemRecebimentoFisico(idNotaFiscal);
		
		boolean indItemEncontrado = false;
		
		for(RecebimentoFisicoDTO recebimentoBD : listaItemRecebimentoFisicoBD){
			
			indItemEncontrado = false;
			
			for(RecebimentoFisicoDTO recebimentoAtual : listaItensNotaAtual){
				
				if(recebimentoBD.getIdItemRecebimentoFisico().equals(recebimentoAtual.getIdItemRecebimentoFisico())){	
					indItemEncontrado = true;
					break;
				}
			}
			
			if(!indItemEncontrado) {
				listaExclusao.add(recebimentoBD);			
			}
		}
		
		for(RecebimentoFisicoDTO recebimentoDTO : listaExclusao){
			
			if(Origem.INTERFACE.equals(recebimentoDTO.getOrigemItemNota())){
				continue;
			}
			
			excluirItem(recebimentoDTO);
		}
		
	}
	
	/**
	 * Atualiza os dados de uma nota fiscal existente.
	 * 
	 * @param recebimentoFisico
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param listaItensNota
	 */
	private void atualizarDadosNotaFiscalExistente(RecebimentoFisico recebimentoFisico, Usuario usuarioLogado, NotaFiscal notaFiscal,  List<RecebimentoFisicoDTO> listaItensNota, Date dataAtual) {
		
		if(recebimentoFisico == null){
			recebimentoFisico = inserirRecebimentoFisico(usuarioLogado, notaFiscal, dataAtual);
		}	
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota){
			
			ItemNotaFiscal itemNota = null;
			
			if(recebimentoFisicoDTO.getIdItemNota() == null) {
				itemNota = incluirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO, dataAtual);
			} else {
				itemNota = new ItemNotaFiscal();
				itemNota.setId(recebimentoFisicoDTO.getIdItemNota());
			}
			
			if(recebimentoFisicoDTO.getIdItemRecebimentoFisico()!=null) {
				
				if(Origem.INTERFACE.equals(recebimentoFisicoDTO.getOrigemItemNota())) {
					continue;
				}
				
				atualizarItemRecebimentoFisico(recebimentoFisicoDTO);
				
			} else {
				
				inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNota, recebimentoFisico);
				
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
			
			ItemNotaFiscal itemNotaFiscal = incluirItemNotaFiscal(usuarioLogado, notaFiscal, recebimentoFisicoDTO, dataAtual);
			
			inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNotaFiscal, recebimentoFisico);
						
		}
		
	}
	
	
	/**
	 * Insere um itemNotaFiscal.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param recebimentoDTO
	 * @param dataAtual
	 * 
	 * @return ItemNotaFiscal
	 */
	private ItemNotaFiscal incluirItemNotaFiscal(Usuario usuarioLogado, NotaFiscal notaFiscal, RecebimentoFisicoDTO recebimentoDTO, Date dataAtual){
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoDTO.getIdProdutoEdicao());
		
		ItemNotaFiscal itemNota = new ItemNotaFiscal();		
		
		itemNota.setQtde(recebimentoDTO.getRepartePrevisto());			
		itemNota.setDataLancamento(recebimentoDTO.getDataLancamento());
		itemNota.setDataRecolhimento(recebimentoDTO.getDataRecolhimento());
		itemNota.setTipoLancamento(recebimentoDTO.getTipoLancamento());
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
	 * Caso não exista um registro um registro de lançamento, este será criado e amarrado ao itemRecebimentoFisico.
	 * Caso o registro de lançamento exista, sera feito apenas a amarração deste com o itemRecebimentoFisico.
	 * 
	 * @param recebimentoFisicoDTO
	 * @param dataAtual
	 * @param usuarioLogado
	 */
	private void inserirLancamento(RecebimentoFisicoDTO recebimentoFisicoDTO, Date dataAtual, Usuario usuarioLogado) {
		
		if(Origem.INTERFACE.equals(recebimentoFisicoDTO.getOrigemItemNota())) {
			return;
		}
		//TODO : Por hora estamos usando somente a Data Lancamento como unica. Verificar se a do Distribuidor também será
		Lancamento lancamento = lancamentoRepository.obterLancamentoPorItensRecebimentoFisico(recebimentoFisicoDTO.getDataLancamento(), null, recebimentoFisicoDTO.getIdProdutoEdicao());
		
		if(lancamento != null) {
			
			if(lancamento.getRecebimentos() == null) {
				lancamento.setRecebimentos(new HashSet<ItemRecebimentoFisico>());
			}
			
			ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
			
			itemRecebimentoFisico.setId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
			
			lancamento.getRecebimentos().add(itemRecebimentoFisico);
			
			lancamentoRepository.alterar(lancamento);
		
		} else {

			lancamento = new Lancamento();
			
			lancamento.setDataLancamentoDistribuidor(recebimentoFisicoDTO.getDataLancamento());
			lancamento.setDataRecolhimentoDistribuidor(recebimentoFisicoDTO.getDataRecolhimento());

			lancamento.setDataLancamentoPrevista(recebimentoFisicoDTO.getDataLancamento());
			lancamento.setDataRecolhimentoPrevista(recebimentoFisicoDTO.getDataRecolhimento());
			
			lancamento.setTipoLancamento(recebimentoFisicoDTO.getTipoLancamento());		

			lancamento.setDataCriacao(dataAtual);
		
			lancamento.setReparte(recebimentoFisicoDTO.getRepartePrevisto());		
			
			lancamento.setStatus(StatusLancamento.RECEBIDO);
			
			lancamento.setDataStatus(dataAtual);
			
			ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
			itemRecebimentoFisico.setId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
			
			lancamento.getRecebimentos().add(itemRecebimentoFisico);
			
			ProdutoEdicao produtoEdicao = new ProdutoEdicao();
			produtoEdicao.setId(recebimentoFisicoDTO.getIdProdutoEdicao());
			
			lancamento.setProdutoEdicao(produtoEdicao);
			
			lancamentoRepository.adicionar(lancamento);
			
		}

		
		inserirHistoricoLancamento(lancamento, dataAtual, usuarioLogado);
		
		
	}
	
	/**
	 * Insere novo registro de historico de lancamento.
	 * 
	 * @param lancamento
	 * @param dataAtual
	 * @param usuarioLogado
	 */
	private void inserirHistoricoLancamento(Lancamento lancamento, Date dataAtual, Usuario usuarioLogado) {
		
		HistoricoLancamento historicoLancamento = new HistoricoLancamento();
		
		historicoLancamento.setData(dataAtual);
		historicoLancamento.setLancamento(lancamento);
		historicoLancamento.setResponsavel(usuarioLogado);
		historicoLancamento.setStatus(lancamento.getStatus());
		
		historicoLancamentoRepository.adicionar(historicoLancamento);
		
		
	}
	
	
	/**
	 * Faz a inserção da falta ou sobra referente a um itemRecebimentoFisico e atualiza o registro
	 * de itemRecebimentoFisico com o lançamento da diferença.
	 * 
	 * @param usuarioLogado
	 * @param recebimentoFisicoDTO
	 */
	private void lancarFaltaOUSobra(
			Usuario usuarioLogado,
			RecebimentoFisicoDTO recebimentoFisicoDTO) {
		
		//TODO: Chamar componente DIOGENES SERVICE
		
		/*BigDecimal calculoQdeDiferenca = recebimentoFisicoDTO.getQtdFisico().subtract(recebimentoFisicoDTO.getRepartePrevisto()) ;

		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(recebimentoFisicoDTO.getIdProdutoEdicao());					
		
		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		itemRecebimentoFisico.setId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
		
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
		itemRecebimentoFisicoRepository.alterar(itemRecebimentoFisico);*/
		
	}
	
	/**
	 * Verifica se existe divergência entre a qtdFisica e repartePrevisto.
	 * 
	 * @param repartePrevisto
	 * @param qtdFisico
	 * 
	 * @return boolean
	 */
	private boolean verificarDiferencaExistente(BigDecimal repartePrevisto, BigDecimal qtdFisico) {
		
		BigDecimal calculoQdeDiferenca = qtdFisico.subtract(repartePrevisto); 
		
		if(calculoQdeDiferenca.compareTo(new BigDecimal(0)) < 0) {
			
			return true;
			
		} else if(calculoQdeDiferenca.compareTo(new BigDecimal(0)) > 0) {						

			return true;
		}
		
		return false;
		
	}
	
	/**
	 * Atualiza a qtdFisicao de um itemRecebimento
	 *  
	 * @param recebimentoDTO
	 */
	private void atualizarItemRecebimentoFisico(RecebimentoFisicoDTO recebimentoDTO) {
		
		ItemRecebimentoFisico itemRecebimento = itemRecebimentoFisicoRepository.buscarPorId(recebimentoDTO.getIdItemRecebimentoFisico());
		
		itemRecebimento.setQtdeFisico(recebimentoDTO.getQtdFisico());
		
		itemRecebimentoFisicoRepository.alterar(itemRecebimento);		
	}
	
	
	/**
	 * Faz a inserção de um itemRecebimento.
	 *  
	 * @param recebimentoDTO
	 * @param itemNotaFiscal
	 * @param recebimentoFisico
	 */
	private void inserirItemRecebimentoFisico(
			RecebimentoFisicoDTO recebimentoDTO, 
			ItemNotaFiscal itemNotaFiscal, 
			RecebimentoFisico recebimentoFisico ) {
		
		ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
		
		itemRecebimento.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimento.setQtdeFisico(recebimentoDTO.getQtdFisico());
		itemRecebimento.setRecebimentoFisico(recebimentoFisico);
		
		itemRecebimentoFisicoRepository.adicionar(itemRecebimento);		
	}

		
	/**
	 * Atualização de Data e Satatus em Recebimento Fisico.
	 * 
	 * @param usuarioLogado
	 * @param notaFiscal
	 * @param dataAtual
	 */
	private void alterarRecebimentoFisicoParaConfirmado(Usuario usuarioLogado, NotaFiscal notaFiscal, Date dataAtual) {
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
		
		recebimentoFisico.setDataConfirmacao(dataAtual);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
		recebimentoFisico.setConferente(usuarioLogado);
		
		recebimentoFisicoRepository.alterar(recebimentoFisico);
		
	}
	
	
	/**
	 * Exclui Item da Nota  
	 * @param idItemNota
	 */
	private void excluirItemNotaFiscal(Long idItemNota) {
		
		ItemNotaFiscal itemNotaFiscal = itemNotaFiscalRepository.buscarPorId(idItemNota);
		
		itemNotaFiscalRepository.remover(itemNotaFiscal);		
	}
	
	/**
	 * Exclui Item Recebimento Fisico
	 * @param idItemRecebimentoFisico
	 */
	private void excluirItemRecebimentoFisico(RecebimentoFisicoDTO recebimentoFisicoDTO) {
		ItemRecebimentoFisico itemRecebimento = itemRecebimentoFisicoRepository.buscarPorId(recebimentoFisicoDTO.getIdItemRecebimentoFisico());
		if(itemRecebimento != null){
			itemRecebimentoFisicoRepository.remover(itemRecebimento);		
		}
	}
	
	/**
	 * Inserir movimento estoque e estoque de produto
	 */
	private void inserirMovimentoEstoque(
			Usuario usuarioLogado,
			RecebimentoFisicoDTO recebimentoFisicoDTO) {
	
		
		boolean indDiferenca = verificarDiferencaExistente(recebimentoFisicoDTO.getRepartePrevisto(), recebimentoFisicoDTO.getQtdFisico());
		
		if(indDiferenca) {
			
			//lancarFaltaOUSobra(usuarioLogado, recebimentoFisicoDTO);
			
		} else {
			
			TipoMovimentoEstoque tipoMovimento = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					TipoOperacao.ENTRADA, GrupoMovimentoEstoque.RECEBIMENTO_FISICO);
			
			movimentoEstoqueService.gerarMovimentoEstoque(
					recebimentoFisicoDTO.getDataLancamento(), 
					recebimentoFisicoDTO.getIdProdutoEdicao(), 
					usuarioLogado.getId(), 
					recebimentoFisicoDTO.getRepartePrevisto(),
					tipoMovimento);
			
		}
		
		
	}
	
}
