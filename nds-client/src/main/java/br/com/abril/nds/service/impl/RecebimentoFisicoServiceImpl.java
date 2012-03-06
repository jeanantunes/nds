package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ItemNotaRecebimentoFisicoDTO;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ItemNotaFiscalRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.service.NotaFiscalService;
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
	 public void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
				ItemRecebimentoFisico itemRecebimentoFisico){
		
	}
	
	@Transactional
	private void inserirItemNotaRecebimentoFisico(ItemNotaRecebimentoFisicoDTO itemNotaDTO){
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		NotaFiscalFornecedor notaFiscal = new NotaFiscalFornecedor();		
		ItemNotaFiscal itemNota = new ItemNotaFiscal();		
					
		if(itemNotaDTO != null){			
			
			inserirLancamento(itemNotaDTO,produtoEdicao);
					
			notaFiscal.setId(itemNotaDTO.getIdNotaFiscal());			
			
			itemNota.setQtde(itemNotaDTO.getRepartePrevisto());
			itemNota.setProdutoEdicao(produtoEdicao);		
			itemNota.setDataLancamento(new Date(System.currentTimeMillis()));
			itemNota.setQtde(itemNotaDTO.getRepartePrevisto());
			itemNota.setProdutoEdicao(produtoEdicao);
			itemNota.setNotaFiscal(notaFiscal);
			ItemNotaFiscal itemNotaSalvo = itemNotaFiscalRepository.merge(itemNota);
			
			inserirItemRecebimentoFisico(itemNotaSalvo);
			
			
		}else{
			throw new IllegalArgumentException("O Item da Nota deve ser especificado.");
		}		
	}
	
	
	private void inserirItemRecebimentoFisico(ItemNotaFiscal itemNotaSalvo) {
		
		ItemRecebimentoFisico itemRecebimento = new ItemRecebimentoFisico();
		itemRecebimento.setItemNotaFiscal(itemNotaSalvo);
				
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
	private void inserirLancamento(ItemNotaRecebimentoFisicoDTO itemNotaDTO, ProdutoEdicao prod) {
		Lancamento lancamento = new Lancamento();
		
		lancamento.setDataLancamentoDistribuidor(itemNotaDTO.getDataLancamento());
		lancamento.setDataRecolhimentoDistribuidor(itemNotaDTO.getDataRecolhimento());
		lancamento.setDataCriacao(new Date(System.currentTimeMillis()));
		lancamento.setDataLancamentoPrevista(itemNotaDTO.getDataLancamento());
		lancamento.setDataRecolhimentoPrevista(itemNotaDTO.getDataRecolhimento());
		lancamento.setTipoLancamento(itemNotaDTO.getTipoLancamento());		
		lancamento.setReparte(itemNotaDTO.getRepartePrevisto());		
		lancamento.setStatus(StatusLancamento.RECEBIDO);
		lancamento.setDataStatus(new Date(System.currentTimeMillis()));
		
		
		prod.setId(itemNotaDTO.getIdProdutoEdicao());
		
		lancamento.setProdutoEdicao(prod);
		
		lancamentoRepository.adicionar(lancamento);
		
	}
	
	@Transactional
	public void excluirItemNotaRecebimentoFisico(ItemNotaFiscal itemNota){
		itemNotaFiscalRepository.remover(itemNota);
	}
	
	public void inserirDadosRecebimentoFisico(NotaFiscal notaFiscal, List<ItemNotaRecebimentoFisicoDTO> listaItensNota){
		
		
		
		
		//nota Fiscal já existe
		if(notaFiscal.getId()!= null){
			
			for(ItemNotaRecebimentoFisicoDTO itemNotaDTO : listaItensNota){
				inserirItemNotaRecebimentoFisico(itemNotaDTO);
				
				if(itemNotaDTO.getIdRecebimentoFisico() != null){
					
										
					//TODO inserir o item Recebimento
					
					
				}else{
					//TODO inserir o Recebimento fisico
					//TODO inserir o item Recebimento
				}
				
			}
		//nota fiscal nao existe	
		}else{
			NotaFiscal notaFsicalDB =notaFiscalRepository.merge(notaFiscal);
			for(ItemNotaRecebimentoFisicoDTO itemNotaDTO : listaItensNota){
				itemNotaDTO.setIdNotaFiscal(notaFsicalDB.getId());
				inserirItemNotaRecebimentoFisico(itemNotaDTO);	
				
				if(itemNotaDTO.getIdRecebimentoFisico() != null){
					
					//TODO inserir o item Recebimento
					
					
				}else{
					//TODO inserir o Recebimento fisico
					//TODO inserir o item Recebimento
				}
			}
			
			
		}
		
		
	}	
	
}
