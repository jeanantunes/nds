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

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
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
		
		Integer diaInicioSemana = this.distribuidorService.inicioSemana().getCodigoDiaSemana();
		
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
}
