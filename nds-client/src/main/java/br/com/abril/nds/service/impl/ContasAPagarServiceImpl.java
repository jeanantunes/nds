package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalFornecedorDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.repository.ContasAPagarRepository;
import br.com.abril.nds.service.ContasAPagarService;

@Service
public class ContasAPagarServiceImpl implements ContasAPagarService {
	
	@Autowired 
	private ContasAPagarRepository contasAPagarRepository;

	@Transactional
	@Override
	public List<ContasAPagarConsultaProdutoDTO> pesquisarProdutos(FiltroContasAPagarDTO filtro) {
		return contasAPagarRepository.pesquisarProdutos(filtro);
	}

	@Transactional
	@Override
	public ContasAPagarGridPrincipalProdutoDTO pesquisarPorProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		
		//MOCK
		ContasAPagarGridPrincipalProdutoDTO dto = new ContasAPagarGridPrincipalProdutoDTO();
        dto.setTotalDesconto(BigDecimal.TEN);
        dto.setTotalPagto(BigDecimal.TEN);
        dto.setValorLiquido(BigDecimal.TEN);
        
        ContasApagarConsultaPorProdutoDTO obj = new ContasApagarConsultaPorProdutoDTO();
        obj.setCodigo("1");
        obj.setDataFinal(new Date());
        obj.setDataLcto(new Date());
        obj.setDebitosCreditos(BigInteger.TEN);
        obj.setEdicao(1L);
        obj.setEncalhe(BigInteger.TEN);
        obj.setFaltasSobras(BigInteger.TEN);
        obj.setFornecedor("FC");
        obj.setProduto("Veja");
        obj.setProdutoEdicaoId(1000L);
        obj.setRctl(new Date());
        obj.setReparte(BigInteger.TEN);
        obj.setSaldoAPagar(BigDecimal.TEN);
        obj.setSuplementacao(BigInteger.TEN);
        obj.setTipo(true);
        obj.setVenda(BigInteger.TEN);
        
        dto.setGrid(new ArrayList<ContasApagarConsultaPorProdutoDTO>());
        dto.getGrid().add(obj);
        dto.getGrid().add(obj);
        dto.getGrid().add(obj);
        dto.getGrid().add(obj);
        dto.getGrid().add(obj);
        dto.getGrid().add(obj);
        
        dto.setTotalGrid(dto.getGrid().size());
        
        return dto;

	}

	@Transactional
	@Override
	public FlexiGridDTO<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public ContasAPagarGridPrincipalFornecedorDTO pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasAPagarConsignadoDTO> pesquisarDetalheConsignado(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public List<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		// TODO Auto-generated method stub
		return null;
	}
}
