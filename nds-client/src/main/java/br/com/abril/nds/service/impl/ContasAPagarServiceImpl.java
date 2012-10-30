package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarDistribDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalFornecedorDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasAPagarTotalDistribDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.service.ContasAPagarService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class ContasAPagarServiceImpl implements ContasAPagarService {
	
	@Autowired 
	private ContasAPagarRepository contasAPagarRepository;
	
	@Autowired
	private RecolhimentoService recolhimentoService;

	@Transactional
	@Override
	public List<ContasAPagarConsultaProdutoDTO> pesquisarProdutos(FiltroContasAPagarDTO filtro) {
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
	public FlexiGridDTO<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public ContasAPagarGridPrincipalFornecedorDTO pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro) {
		
		this.validarFiltro(filtro);
		
		ContasAPagarGridPrincipalFornecedorDTO retorno = new ContasAPagarGridPrincipalFornecedorDTO();
		
		retorno.setGrid(this.contasAPagarRepository.pesquisarPorDistribuidor(filtro));
		retorno.setTotalGrid(this.contasAPagarRepository.pesquisarPorDistribuidorCount(filtro));
		
		BigDecimal totalBruto = this.contasAPagarRepository.buscarTotalPesquisarPorDistribuidor(filtro, false);
		
		if (totalBruto == null){
			
			totalBruto = BigDecimal.ZERO;
		}
				
		retorno.setTotalBruto(totalBruto);
		
		BigDecimal totalDesconto = this.contasAPagarRepository.buscarTotalPesquisarPorDistribuidor(filtro, true);
		
		if (totalDesconto == null){
			
			totalDesconto = BigDecimal.ZERO;
		}
				
		retorno.setTotalDesconto(totalDesconto);
		retorno.setSaldo(totalBruto.subtract(totalDesconto));
		
		return retorno;
	}

	private void validarFiltro(FiltroContasAPagarDTO filtro) {
		
		if (filtro == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro de pesquisa inválido.");
		}
		
		if (filtro.getDataDe() != null && filtro.getDataAte() != null){
		
			if (filtro.getDataDe().after(filtro.getDataAte())){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Intervalo de datas inválido.");
			}
		}
		
		if (filtro.getCe() != null){
			
			Intervalo<Date> intervaloCE = this.recolhimentoService.getPeriodoRecolhimento(null, filtro.getCe(), null);
			
			if (filtro.getDataDe() != null && filtro.getDataAte() != null){
			
				if (intervaloCE.getDe().after(filtro.getDataDe()) || intervaloCE.getAte().before(filtro.getDataAte())){
					
					throw new ValidacaoException(TipoMensagem.WARNING, "Intervalo informado fora da semana CE informada.");
				}
			} else {
				
				filtro.setDataDe(intervaloCE.getDe());
				filtro.setDataAte(intervaloCE.getAte());
			}
		}
	}

	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> pesquisarDetalheConsignado(FiltroContasAPagarDTO filtro) {
		
		List<ContasAPagarConsignadoDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheConsignado(filtro);
		
		ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> dto = 
				new ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO>();
		
		dto.setGrid(lista);
		
		List<ContasAPagarDistribDTO> contas = new ArrayList<ContasAPagarDistribDTO>();
		ContasAPagarDistribDTO conta = new ContasAPagarDistribDTO();
		String fornecedor = null;
		
		for (ContasAPagarConsignadoDTO consignado : lista){
			
			conta.setNome(consignado.getFornecedor());
			conta.setTotal(conta.getTotal().add(consignado.getPrecoCapa()));
			
			if (!consignado.getFornecedor().equals(fornecedor)){
				
				contas.add(conta);
				fornecedor = consignado.getFornecedor();
				conta = new ContasAPagarDistribDTO();
			}
		}
		
		contas.add(conta);
		
		dto.setTotalDistrib(contas);
		
		return dto;
	}

	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro) {
		
		List<ContasAPagarEncalheDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheEncalhe(filtro);
		
		ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> dto = 
				new ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO>();
		
		dto.setGrid(lista);
		
		List<ContasAPagarDistribDTO> contas = new ArrayList<ContasAPagarDistribDTO>();
		ContasAPagarDistribDTO conta = new ContasAPagarDistribDTO();
		String fornecedor = null;
		
		for (ContasAPagarEncalheDTO encalhe : lista){
			
			conta.setNome(encalhe.getFornecedor());
			conta.setTotal(conta.getTotal().add(encalhe.getPrecoCapa()));
			
			if (!encalhe.getFornecedor().equals(fornecedor)){
				
				contas.add(conta);
				fornecedor = encalhe.getFornecedor();
				conta = new ContasAPagarDistribDTO();
			}
		}
		
		contas.add(conta);
		
		dto.setTotalDistrib(contas);
		
		return dto;
	}

	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro) {
		
		List<ContasAPagarFaltasSobrasDTO> lista = 
				this.contasAPagarRepository.pesquisarDetalheFaltasSobras(filtro);
		
		ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> dto = 
				new ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO>();
		
		dto.setGrid(lista);
		
		List<ContasAPagarDistribDTO> contas = new ArrayList<ContasAPagarDistribDTO>();
		ContasAPagarDistribDTO conta = new ContasAPagarDistribDTO();
		String fornecedor = null;
		
		for (ContasAPagarFaltasSobrasDTO faltasSobras : lista){
			
			conta.setNome(faltasSobras.getFornecedor());
			conta.setTotal(conta.getTotal().add(faltasSobras.getPrecoCapa()));
			
			if (!faltasSobras.getFornecedor().equals(fornecedor)){
				
				contas.add(conta);
				fornecedor = faltasSobras.getFornecedor();
				conta = new ContasAPagarDistribDTO();
			}
		}
		
		contas.add(conta);
		
		dto.setTotalDistrib(contas);
		
		return dto;
	}
}
