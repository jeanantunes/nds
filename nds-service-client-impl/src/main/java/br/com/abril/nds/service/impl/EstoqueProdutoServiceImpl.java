package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EstoqueProdutoFilaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.EstoqueProdutoFilaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.SemanaUtil;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.EstoqueProduto}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class EstoqueProdutoServiceImpl implements EstoqueProdutoService {

	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private EstoqueProdutoFilaRepository estoqueProdutoFilaRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRespository;
	
	
	@Transactional(readOnly = true)
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {
		
		return this.estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
	}

	@Override
	@Transactional(readOnly = true)
	public Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro) {
		
		if (filtro == null || filtro.getDataRecolhimento() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Campo data é obrigatório.");
		}
		
		return this.estoqueProdutoRespository.buscarEstoqueProdutoRecolhimentoCount(filtro);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(FiltroEstoqueProdutosRecolhimento filtro) {
		
		if (filtro == null || filtro.getDataRecolhimento() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Campo data é obrigatório.");
		}
		
		return this.estoqueProdutoRespository.buscarEstoqueProdutoRecolhimento(filtro);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<ItemDTO<Integer, Integer>> obterSemanasProdutosFechados() {
		
		//Comparator para ordenar os objetos do tipo ItemDTO
		Comparator<ItemDTO<Integer, Integer>> comp = new Comparator<ItemDTO<Integer, Integer>>() {

			@Override
			public int compare(ItemDTO<Integer, Integer> o1,
					ItemDTO<Integer, Integer> o2) {
				
				return o1.getKey().compareTo(o2.getKey());
			}
		};
		
		//Set necessário porque datas diferentes podem pertencer a semanas iguais do sistema
		Set<ItemDTO<Integer, Integer>> ret = new TreeSet<ItemDTO<Integer, Integer>>(comp);
		
		List<Date> datas = this.estoqueProdutoRespository.obterDatasRecProdutosFechados();
		
		if (datas == null || datas.isEmpty()){
			
			return ret;
		}
		
		Integer diaInicioSemana = this.distribuidorService.inicioSemanaRecolhimento().getCodigoDiaSemana();
		
		for (Date d : datas){
			
			int anoSemana = SemanaUtil.obterAnoNumeroSemana(d, diaInicioSemana);
			
			ret.add(new ItemDTO<Integer, Integer>(anoSemana, anoSemana));
		}
		
		return ret;
	}

	@Override
	@Transactional(readOnly=true)
	public BigInteger buscarQtdEstoquePorProduto(String codigoProduto, List<Long> numeroEdicao) {
		
		return this.estoqueProdutoRespository.buscarQtdEstoquePorProduto(
			StringUtils.leftPad(codigoProduto, 8, '0'), numeroEdicao);
	}
	
	@Transactional
	public void processarTransferenciaEntreEstoques(
			final Long idProdutoEdicao, 
			final TipoEstoque estoqueSaida, 
			final TipoEstoque estoqueEntrada,
			final Long idUsuario){
		
		EstoqueProduto estoqueProduto  = estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
		
		if(estoqueProduto == null){
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro para obter estoque produto!");
		}
		
		//Operacao de saida de estoque
		TipoMovimentoEstoque tipoMovimentoDe = 
				tipoMovimentoRepository.buscarTipoMovimentoEstoque(
						this.obterTipoMovimentoEstoqueTransferencia(estoqueSaida, OperacaoEstoque.SAIDA));
		
		//Operação entrada no estoque
		TipoMovimentoEstoque tipoMovimentoPara = 
				tipoMovimentoRepository.buscarTipoMovimentoEstoque(
						this.obterTipoMovimentoEstoqueTransferencia(estoqueEntrada, OperacaoEstoque.ENTRADA));
		
		BigInteger quantidadeTransferida = this.obterValorEstoqueProduto(estoqueSaida, estoqueProduto);
		
		if(quantidadeTransferida == null){
			quantidadeTransferida = BigInteger.ZERO;
		}
		
		movimentoEstoqueService.gerarMovimentoEstoque(idProdutoEdicao, idUsuario, quantidadeTransferida, tipoMovimentoDe);
		
		movimentoEstoqueService.gerarMovimentoEstoque(idProdutoEdicao, idUsuario, quantidadeTransferida, tipoMovimentoPara);
	}
	
	private GrupoMovimentoEstoque obterTipoMovimentoEstoqueTransferencia(
			final TipoEstoque tipoEstoque, 
			final OperacaoEstoque operacaoEstoque){
	
        if (tipoEstoque == null) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Erro para obter tipo de movimento estoque para transferencia!");
        }
        switch(tipoEstoque) {
        
	        case LANCAMENTO:
	            return isOperacaoEntrada(operacaoEstoque)
	                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO
	                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO;
	        case SUPLEMENTAR:
	            return isOperacaoEntrada(operacaoEstoque)
	                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR
	                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR;
	        case RECOLHIMENTO:
	            return isOperacaoEntrada(operacaoEstoque)
	                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO
	                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO;
	        case PRODUTOS_DANIFICADOS:
	            return isOperacaoEntrada(operacaoEstoque)
	                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS
	                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS;
	            
	        case DEVOLUCAO_FORNECEDOR:
	            return isOperacaoEntrada(operacaoEstoque)
	                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR
	                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR;
	            
	        case DEVOLUCAO_ENCALHE:
	            return isOperacaoEntrada(operacaoEstoque)
	                    ? GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_ENCALHE
	                            :GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE;
	            
	        case GANHO:
	            return GrupoMovimentoEstoque.GANHO_EM;
	            
	        case PERDA:
	            return GrupoMovimentoEstoque.PERDA_EM;
	        
	        default: return null;    
        }
    }
	
	private BigInteger obterValorEstoqueProduto(final TipoEstoque tipoEstoque, final EstoqueProduto estoqueProduto){
	
        if (tipoEstoque == null) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Erro para obter tipo de movimento estoque para transferencia!");
        }
        switch(tipoEstoque) {
        
	        case LANCAMENTO:
	            return estoqueProduto.getQtde();
	        
	        case SUPLEMENTAR:
	            return estoqueProduto.getQtdeSuplementar();
	        
	        case PRODUTOS_DANIFICADOS:
	            return estoqueProduto.getQtdeDanificado();
	            
	        case DEVOLUCAO_FORNECEDOR:
	            return estoqueProduto.getQtdeDevolucaoFornecedor();
	            
	        case DEVOLUCAO_ENCALHE:
	            return estoqueProduto.getQtdeDevolucaoEncalhe();
	            
	        case GANHO:
	            return estoqueProduto.getQtdeGanho();
	            
	        case PERDA:
	            return estoqueProduto.getQtdePerda();
	        
	        default: return null;    
        }
    }
	
	 private boolean isOperacaoEntrada(final OperacaoEstoque operacaoEstoque){
	     return (OperacaoEstoque.ENTRADA.equals(operacaoEstoque));
	 }
	 
	 @Transactional
	 @Override
	 public void saveOrUpdate(EstoqueProduto estoqueProduto){
	     this.estoqueProdutoRespository.saveOrUpdate(estoqueProduto);
	 }
	 
	 @Transactional
	 @Override
	 public EstoqueProduto obterEstoqueProdutoParaAtualizar(Long idProdutoEdicao) {
	     
	     return this.estoqueProdutoRespository.obterEstoqueProdutoParaAtualizar(idProdutoEdicao);
	 }
	 
	 @Override
	 @Transactional
	 public void atualizarEstoqueProdutoCota() {
	     
	     final List<EstoqueProdutoFilaDTO> epfs = this.estoqueProdutoFilaRepository.buscarTodosEstoqueProdutoFila();
	     
	     for (EstoqueProdutoFilaDTO epf : epfs) {
	         
	         boolean atualizaReg = true;
	         
	         if (OperacaoEstoque.SAIDA.equals(epf.getOperacaoEstoque())) {
	             
	             epf.setQtde(epf.getQtde().negate());
	         }
	         
	         EstoqueProduto ep = this.obterEstoqueProdutoParaAtualizar(epf.getIdProdutoEdicao());
	         
	         if (ep == null) {
	             
	             ep = new EstoqueProduto();
	             ep.setProdutoEdicao(produtoEdicaoRespository.buscarPorId(epf.getIdProdutoEdicao()));
	         }
	         
	         switch(epf.getTipoEstoque()) {
	             case DEVOLUCAO_ENCALHE:
	                 
	                 if (ep.getQtdeDevolucaoEncalhe() == null) {
	                     
	                     ep.setQtdeDevolucaoEncalhe(epf.getQtde());
	                 } else {
                        
                        ep.setQtdeDevolucaoEncalhe(ep.getQtdeDevolucaoEncalhe().add(epf.getQtde()));
	                 }
	                 
	             break;
	             case JURAMENTADO:
                    
	                 if (ep.getQtdeJuramentado() == null) {
	                     
	                     ep.setQtdeJuramentado(epf.getQtde());
	                 } else {
	                     
	                     ep.setQtdeJuramentado(ep.getQtdeJuramentado().add(epf.getQtde()));
	                 }
	                 
                 break;
                 case SUPLEMENTAR:
                     
                     if (ep.getQtdeSuplementar() == null) {
                         
                         ep.setQtdeSuplementar(epf.getQtde());
                     } else {
                         
                         ep.setQtdeSuplementar(ep.getQtdeSuplementar().add(epf.getQtde()));
                     }
                     
                 break;
                 default:
                     //os demais tipos de estoque não são alterados na conf. de encalhe,
                     //ao menos não até a data em que estas linhas foram escritas
                     atualizaReg = false;
                     break;
	         }
	         
	         if (atualizaReg) {
	             
	             this.saveOrUpdate(ep);
	         }
	         
	         this.estoqueProdutoFilaRepository.removerPorId(epf.getId());
	     }
	}
	 
}
