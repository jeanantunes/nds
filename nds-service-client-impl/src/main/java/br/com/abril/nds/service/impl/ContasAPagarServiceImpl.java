package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ContasAPagarConsultaProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarGridPrincipalFornecedorVO;
import br.com.abril.nds.client.vo.ContasApagarConsultaPorDistribuidorVO;
import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarDistribDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasAPagarTotalDistribDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.ContasAPagarService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class ContasAPagarServiceImpl implements ContasAPagarService {
	
	@Autowired 
	private ContasAPagarRepository contasAPagarRepository;
	
	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	@Transactional
	@Override
	public List<ContasAPagarConsultaProdutoVO> pesquisarProdutos(final FiltroContasAPagarDTO filtro) {
		return contasAPagarRepository.obterProdutos(filtro);
	}

	@Transactional
	@Override
	public ContasAPagarGridPrincipalProdutoDTO pesquisarPorProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		this.validarFiltro(filtro);
		
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setSortColumn(sortname);
		paginacao.setSortOrder(sortorder);
		paginacao.setPaginaAtual(page);
		paginacao.setQtdResultadosPorPagina(rp);
		
		filtro.setPaginacaoVO(paginacao);
		
        ContasAPagarGridPrincipalProdutoDTO retorno = this.contasAPagarRepository.pesquisarTotaisPorProduto(filtro);

		retorno.setGrid(this.contasAPagarRepository.pesquisarPorProduto(filtro));
		retorno.setTotalGrid(this.contasAPagarRepository.pesquisarCountPorProduto(filtro).intValue());
		
		return retorno;
	}

	@Transactional
	@Override
	public FlexiGridDTO<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro) {
		
		this.validarFiltroPesquisaParcial(filtro);
		
		List<ContasAPagarParcialDTO> lista = this.contasAPagarRepository.pesquisarParcial(filtro);
		
		for (ContasAPagarParcialDTO dto : lista){
			
			List<Long> nfes = 
					this.notaFiscalRepository.obterNumerosNFePorLancamento(dto.getIdLancamento());
			
			StringBuilder textoNfe = new StringBuilder();
			for (Long nfe : nfes){
				
				if (textoNfe.length() != 0){
					
					textoNfe.append(" / ");
				}
				
				textoNfe.append(nfe);
			}
			
			dto.setNfe(textoNfe.toString());
		}
		
		FlexiGridDTO<ContasAPagarParcialDTO> dto = new FlexiGridDTO<ContasAPagarParcialDTO>();
		dto.setGrid(lista);
		
		Long count = this.contasAPagarRepository.countPesquisarParcial(filtro);
		dto.setTotalGrid(count == null ? 0 : count.intValue());
		
		return dto;
	}

	private void validarFiltroPesquisaParcial(FiltroContasAPagarDTO filtro) {
		
		if (filtro == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro de pesquisa inválido.");
		}
		
		if (filtro.getDataDetalhe() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida.");
		}
		
		if (filtro.getProduto() == null || filtro.getProduto().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Código produto inválido.");
		}
	}

	@Transactional
	@Override
	public ContasAPagarGridPrincipalFornecedorVO pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro) {
		
		this.validarFiltro(filtro);
		
		final ContasAPagarGridPrincipalFornecedorVO retorno = new ContasAPagarGridPrincipalFornecedorVO();
		
		retorno.setGrid(this.contasAPagarRepository.pesquisarPorDistribuidor(filtro));
		retorno.setTotalGrid(this.contasAPagarRepository.pesquisarPorDistribuidorCount(filtro));
		
		BigDecimal totalBruto = this.contasAPagarRepository.buscarTotalPesquisarPorDistribuidor(filtro, false);
		
		if (totalBruto == null){
			
			totalBruto = BigDecimal.ZERO;
		}
				
		retorno.setTotalBruto(
		        CurrencyUtil.formatarValor(
		                CurrencyUtil.arredondarValorParaDuasCasas(totalBruto)));
		
		BigDecimal totalDesconto = this.contasAPagarRepository.buscarTotalPesquisarPorDistribuidor(filtro, true);
		
		if (totalDesconto == null){
			
			totalDesconto = BigDecimal.ZERO;
		}
				
		retorno.setTotalDesconto(
		        CurrencyUtil.formatarValor(
		                CurrencyUtil.arredondarValorParaDuasCasas(totalDesconto)));

		List<ContasApagarConsultaPorDistribuidorVO> listaCPagar = retorno.getGrid();
		
		BigDecimal saldo = BigDecimal.ZERO;
		
		for (ContasApagarConsultaPorDistribuidorVO item : listaCPagar){
			
			saldo = saldo.add(CurrencyUtil.converterValor(item.getSaldo()));
		}

		retorno.setSaldo(CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(saldo)));

		return retorno;
	}

    private void validarFiltro(FiltroContasAPagarDTO filtro) {
		
		if (filtro == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro de pesquisa inválido.");
		}
		
		if (filtro.getDataDe() == null || filtro.getDataAte() == null){
		    
		    throw new ValidacaoException(TipoMensagem.WARNING, "Período é obrigatório.");
		}
	}

	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> pesquisarDetalheConsignado(FiltroContasAPagarDTO filtro) {
		
		List<ContasAPagarConsignadoDTO> listaConsignados = 
				this.contasAPagarRepository.pesquisarDetalheConsignado(filtro);
		
		ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> resultadoDTO = 
				new ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO>();
		
		resultadoDTO.setGrid(listaConsignados);
		
		Map<String, BigDecimal> mapFornecedorTotal = sumarizarTotalPorFornecedor(listaConsignados);
		
		resultadoDTO.setTotalDistrib(getListDTO(mapFornecedorTotal));
		
		return resultadoDTO;
	}

	/**
	 * Sumariza o valor total com desconto por fornecedor
	 * 
	 * @param listaConsignados
	 * @return
	 */
	private Map<String, BigDecimal> sumarizarTotalPorFornecedor(
			List<ContasAPagarConsignadoDTO> listaConsignados) {
		
		Map<String, BigDecimal> mapFornecedorTotal = new HashMap<String, BigDecimal>();
		
		BigDecimal valor = BigDecimal.ZERO;
		
		for (ContasAPagarConsignadoDTO consignado : listaConsignados){
			
		    if (mapFornecedorTotal.containsKey(consignado.getFornecedor())){
		        
		        if (valor != null){
		            
		            valor = valor.add(consignado.getValorComDesconto() == null ? BigDecimal.ZERO : consignado.getValorComDesconto());
		        }
		    } else {
		        
		        valor = consignado.getValorComDesconto() == null ? BigDecimal.ZERO : consignado.getValorComDesconto();
		    }
		    
			mapFornecedorTotal.put(consignado.getFornecedor(), valor);
		}
		
		return mapFornecedorTotal;
	}

	/**
	 * Transforma o map de Fornecedor/ValorTotalComDesconto em uma lista de ContasAPagarDistribDTO
	 * que está sendo utilizado para exibir as informações 
	 * 
	 * @param mapFornecedorTotal
	 * @return
	 */
	private List<ContasAPagarDistribDTO> getListDTO(Map<String, BigDecimal> mapFornecedorTotal) {
		
		List<ContasAPagarDistribDTO> contas = new ArrayList<ContasAPagarDistribDTO>();
		
		for(Map.Entry<String, BigDecimal> fornecedorTotal : mapFornecedorTotal.entrySet()) {
			
			contas.add(
					new ContasAPagarDistribDTO(fornecedorTotal.getKey(), 
											   fornecedorTotal.getValue()));
		}
		
		return contas;
	}
	
	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro) {
		
		List<ContasAPagarEncalheDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheEncalhe(filtro);
		
		ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> dto = 
				new ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO>();
		
		dto.setGrid(lista);
		
		Map<String, ContasAPagarDistribDTO> contas = new HashMap<String, ContasAPagarDistribDTO>();
		
		for (ContasAPagarEncalheDTO encalhe : lista){
			
		    final ContasAPagarDistribDTO capdto = contas.get(encalhe.getFornecedor());
		    
		    if (capdto == null){
		        
		        contas.put(
                        encalhe.getFornecedor(), 
                        new ContasAPagarDistribDTO(encalhe.getFornecedor(), encalhe.getValor()));
		    } else {
		        
		        capdto.setTotal(capdto.getTotal().add(encalhe.getValor()));
		    }
		}
		
		dto.setTotalDistrib(contas.values());
		
		return dto;
	}

	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro) {
		
		final List<ContasAPagarFaltasSobrasDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheFaltasSobras(filtro);
		
		final ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> dto = 
				new ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO>();
		
		dto.setGrid(lista);
		
		final Map<String, ContasAPagarDistribDTO> contas = new HashMap<String, ContasAPagarDistribDTO>();
		
		for (ContasAPagarFaltasSobrasDTO faltasSobras : lista){
			
		    final ContasAPagarDistribDTO cont = contas.get(faltasSobras.getFornecedor());
		    
		    if (cont == null){
		        
		        contas.put(
		                faltasSobras.getFornecedor(), 
		                new ContasAPagarDistribDTO(
		                        faltasSobras.getFornecedor(), faltasSobras.getValor()));
		    } else {
		        
		        cont.setTotal(cont.getTotal().add(faltasSobras.getValor()));
		    }
		}
		
		dto.setTotalDistrib(contas.values());
		
		return dto;
	}
}
