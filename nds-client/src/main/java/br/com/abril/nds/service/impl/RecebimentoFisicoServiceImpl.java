package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.ItemNotaFiscalRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.service.RecebimentoFisicoService;

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
	
	public void inserirDadosRecebimentoFisico(NotaFiscal notaFiscal, List<RecebimentoFisicoDTO> listaItensNota){
		
		//nota Fiscal ja existe
		if(notaFiscal.getId()!= null){
			
			for(RecebimentoFisicoDTO recebimentoDTO : listaItensNota){
				//inserir ItemNotaRecebimento
				ItemNotaFiscal itemNotaDB = inserirItemNotaRecebimentoFisico(notaFiscal, recebimentoDTO);
				
				RecebimentoFisico buscaRecebimentoFisicoDB= recebimentoFisicoRepository.obterRecebimentoFisicoPorNotaFiscal(notaFiscal.getId());
				// se existir recebimentoFisico
				if(buscaRecebimentoFisicoDB != null){
					//inserir itemRecebimento										
					inserirItemRecebimentoFisico(recebimentoDTO, itemNotaDB,buscaRecebimentoFisicoDB);					
					
				}else{
					inserirRecebimentoFisico(recebimentoDTO, itemNotaDB, notaFiscal);
					
					inserirItemRecebimentoFisico(recebimentoDTO);
				}
			}
			
		//nota fiscal nao existe	
		}else{
			NotaFiscal notaFiscalDB =notaFiscalRepository.merge(notaFiscal);
			for(RecebimentoFisicoDTO recebimentoFisicoDTO : listaItensNota){
				//inserir item Nota recebimento Fisico
				ItemNotaFiscal itemNotaDB= inserirItemNotaRecebimentoFisico(notaFiscalDB, recebimentoFisicoDTO);
				
				//inserir recebimento Fisico
				RecebimentoFisico recebimentoFisicoDB = inserirRecebimentoFisico(recebimentoFisicoDTO, itemNotaDB, notaFiscalDB);	
				
				//ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
				inserirItemRecebimentoFisico(recebimentoFisicoDTO, itemNotaDB,recebimentoFisicoDB);
								
			}
		}
		
	}
	
	
	
	@Transactional
	 public void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
				ItemRecebimentoFisico itemRecebimentoFisico){
		
	}
	
	@Transactional
	private ItemNotaFiscal inserirItemNotaRecebimentoFisico(NotaFiscal notaFsical, RecebimentoFisicoDTO recebimentoDTO){
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		NotaFiscalFornecedor notaFiscal = new NotaFiscalFornecedor();		
		ItemNotaFiscal itemNota = new ItemNotaFiscal();		
					
		if(recebimentoDTO != null){			
			
			inserirLancamento(recebimentoDTO,produtoEdicao);
					
			notaFiscal.setId(notaFiscal.getId());			
			
			itemNota.setQtde(recebimentoDTO.getRepartePrevisto());
			itemNota.setProdutoEdicao(produtoEdicao);		
			itemNota.setDataLancamento(new Date(System.currentTimeMillis()));
			itemNota.setQtde(recebimentoDTO.getRepartePrevisto());
			itemNota.setProdutoEdicao(produtoEdicao);
			itemNota.setNotaFiscal(notaFiscal);
			ItemNotaFiscal itemNotaSalvo = itemNotaFiscalRepository.merge(itemNota);
			
			return itemNotaSalvo;
			
		}else{
			throw new IllegalArgumentException("O Item da Nota deve ser especificado.");
		}		
	}
	
	private RecebimentoFisico inserirRecebimentoFisico(RecebimentoFisicoDTO recebimentoDTO, ItemNotaFiscal itemNotaDB, NotaFiscal notaFiscal){
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		recebimentoFisico.setDataRecebimento(new Date(System.currentTimeMillis()));
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		RecebimentoFisico recebimentoFisicoDB= recebimentoFisicoRepository.merge(recebimentoFisico);
		return recebimentoFisicoDB;	
		
	}
	
	private ItemRecebimentoFisico inserirItemRecebimentoFisico(RecebimentoFisicoDTO recebimentoDTO, ItemNotaFiscal itemNotaDB, RecebimentoFisico recebimentoFisicoDB) {
		
		ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
		
		itemRecebimento.setItemNotaFiscal(itemNotaDB);
		itemRecebimento.setItemNotaFiscal(itemNotaDB);
		itemRecebimento.setRecebimentoFisico(recebimentoFisicoDB);
		itemRecebimento.setItemNotaFiscal(itemNotaDB);
		
		itemRecebimentoFisicoRepository.merge(itemRecebimento);
		return itemRecebimento;
	}

	@Transactional
	public void alterarItemNotaRecebimentoFisico(RecebimentoFisicoDTO recebimentoFisicoDTO){
		if(recebimentoFisicoDTO != null){	
		
			ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
			
			itemNotaFiscal.setId(recebimentoFisicoDTO.getIdItemNota());
			
			ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
			itemRecebimento.setQtdeFisico(recebimentoFisicoDTO.getQtdFisico());
			
			itemNotaFiscal.setRecebimentoFisico(itemRecebimento);
			
			if(recebimentoFisicoDTO.getIdItemNota()!=null && recebimentoFisicoDTO.getQtdFisico()!=null
					&& recebimentoFisicoDTO.getOrigem().equals(Origem.MANUAL)){
				itemNotaFiscalRepository.alterar(itemNotaFiscal);
			}
		}else{
			throw new IllegalArgumentException("O Item da Nota deve ser especificado.");
		}
	}
	
	@Transactional
	private void inserirLancamento(RecebimentoFisicoDTO recebimentoFisicoDTO, ProdutoEdicao prod) {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setDataLancamentoDistribuidor(recebimentoFisicoDTO.getDataLancamento());
		lancamento.setDataRecolhimentoDistribuidor(recebimentoFisicoDTO.getDataRecolhimento());
		lancamento.setDataCriacao(new Date(System.currentTimeMillis()));
		lancamento.setDataLancamentoPrevista(recebimentoFisicoDTO.getDataLancamento());
		lancamento.setDataRecolhimentoPrevista(recebimentoFisicoDTO.getDataRecolhimento());
		lancamento.setTipoLancamento(recebimentoFisicoDTO.getTipoLancamento());		
		lancamento.setReparte(recebimentoFisicoDTO.getRepartePrevisto());		
		lancamento.setStatus(StatusLancamento.RECEBIDO);
		lancamento.setDataStatus(new Date(System.currentTimeMillis()));
		
		
		prod.setId(recebimentoFisicoDTO.getIdProdutoEdicao());
		
		lancamento.setProdutoEdicao(prod);
		
		lancamentoRepository.adicionar(lancamento);
		
	}
	
	@Transactional
	public void excluirItemNotaRecebimentoFisico(ItemNotaFiscal itemNota){
		itemNotaFiscalRepository.remover(itemNota);
	}
	
	
	private void inserirItemRecebimentoFisico(RecebimentoFisicoDTO recebimentoDTO){
		//inserir o item Recebimento
		ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
	
		//realizar calculo para saber se é falta ou sobra
		Diferenca diferenca = new Diferenca();
		BigDecimal calculoQdeDiferenca = recebimentoDTO.getQtdFisico().subtract(recebimentoDTO.getRepartePrevisto()) ;
		diferenca.setQtde(calculoQdeDiferenca);
		diferenca.setItemRecebimentoFisico(itemRecebimento);
		
		ProdutoEdicao pe = new ProdutoEdicao();
		pe.setId(recebimentoDTO.getIdProdutoEdicao());					
		diferenca.setProdutoEdicao(pe);
		
		diferenca.setStatusConfirmacao(StatusConfirmacao.PENDENTE);
		//Testando condicao para verificar se é falta ou sobra na diferenca
		if(calculoQdeDiferenca.compareTo(new BigDecimal(0)) < 0){
		
			diferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE);
			diferencaEstoqueRepository.adicionar(diferenca);						
			
		}else if(calculoQdeDiferenca.compareTo(new BigDecimal(0)) > 0){						
			diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_DE);
			diferencaEstoqueRepository.adicionar(diferenca);
			
		}
			
		//setando diferenca
		itemRecebimento.setDiferenca(diferenca);
		//setando item da nota
		ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
		itemNotaFiscal.setId(recebimentoDTO.getIdItemNota());
		itemRecebimento.setItemNotaFiscal(itemNotaFiscal);
		//adicionando item Recebimento Fisico
		itemRecebimentoFisicoRepository.adicionar(itemRecebimento);
	}
	
}
