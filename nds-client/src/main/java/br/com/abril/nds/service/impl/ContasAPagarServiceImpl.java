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
		return null;
	}

	@Transactional
	@Override
	public ContasAPagarGridPrincipalProdutoDTO pesquisarPorProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
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
		
		if (filtro.getCe() == null && (filtro.getDataDe() == null || filtro.getDataAte() == null)){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Intervalo de datas inválido.");
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
	public ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> pesquisarDetalheConsignado(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> to = new ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO>();
		
		ContasAPagarConsignadoDTO dto = new ContasAPagarConsignadoDTO();
		dto.setCodigo("1");
		dto.setDiferenca(1);
		dto.setEdicao(1);
		dto.setFornecedor("FC");
		dto.setMotivo("motivo");
		dto.setNfe("nfe");
		dto.setPrecoCapa(BigDecimal.TEN);
		dto.setPrecoComDesconto(BigDecimal.TEN);
		dto.setProduto("Veja");
		dto.setReparteFinal(1);
		dto.setReparteSugerido(1);
		dto.setValor(BigDecimal.TEN);
		dto.setValorComDesconto(BigDecimal.TEN);
		
		to.setGrid(new ArrayList<ContasAPagarConsignadoDTO>());
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		
		ContasAPagarDistribDTO dtoDistr1 = new ContasAPagarDistribDTO();
		dtoDistr1.setNome("Distr1"); dtoDistr1.setTotal(BigDecimal.TEN);
		ContasAPagarDistribDTO dtoDistr2 = new ContasAPagarDistribDTO();
		dtoDistr2.setNome("Distr2"); dtoDistr2.setTotal(BigDecimal.TEN);
		ContasAPagarDistribDTO dtoDistr3 = new ContasAPagarDistribDTO();
		dtoDistr3.setNome("Distr3"); dtoDistr3.setTotal(BigDecimal.TEN);
		
		to.setTotalDistrib(new ArrayList<ContasAPagarDistribDTO>()); 
		to.getTotalDistrib().add(dtoDistr1);
		to.getTotalDistrib().add(dtoDistr2);
		to.getTotalDistrib().add(dtoDistr3);
		
		return to;
	}

	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}
}
