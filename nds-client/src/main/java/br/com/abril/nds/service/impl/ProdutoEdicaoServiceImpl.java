package br.com.abril.nds.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ProdutoEdicaoService;

@Service
public class ProdutoEdicaoServiceImpl implements ProdutoEdicaoService {

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Override
	@Transactional
	public List<ProdutoEdicao> obterProdutoEdicaoPorNomeProduto(String nomeProduto) {
		return produtoEdicaoRepository.obterProdutoEdicaoPorNomeProduto(nomeProduto);
	}

	@Override
	@Transactional
	public FuroProdutoDTO obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
			String codigo, String nomeProduto, Long edicao, Date dataLancamento) {
		
		if (codigo == null || codigo.isEmpty()){
			throw new IllegalArgumentException("Código é obrigatório.");
		}
		
		if (edicao == null){
			throw new IllegalArgumentException("Edição é obrigatório.");
		}
		
		if (dataLancamento == null){
			throw new IllegalArgumentException("Data Lançamento é obrigatório.");
		}
		
		FuroProdutoDTO furoProdutoDTO = produtoEdicaoRepository.
				obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
						codigo, nomeProduto, edicao, dataLancamento);
		
		if (furoProdutoDTO != null){
			//buscar proxima data para lançamento
			
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).parse(furoProdutoDTO.getNovaData()));
			} catch (ParseException e) {
				return furoProdutoDTO;
			}
			
			List<Integer> listaDiasSemana = 
					this.distribuicaoFornecedorRepository.obterDiasSemanaDistribuicao(
							furoProdutoDTO.getCodigoProduto(), 
							furoProdutoDTO.getIdProdutoEdicao());
			
			if (listaDiasSemana != null && !listaDiasSemana.isEmpty()){
				int diaSemana = -1;
				for (Integer dia : listaDiasSemana){
					if (dia > calendar.get(Calendar.DAY_OF_WEEK)){
						diaSemana = dia;
						break;
					}
				}
				
				if (diaSemana == -1){
					diaSemana = listaDiasSemana.get(0);
				}
				
				while (calendar.get(Calendar.DAY_OF_WEEK) != diaSemana){
					calendar.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				furoProdutoDTO.setNovaData(new SimpleDateFormat(furoProdutoDTO.DATE_PATTERN_PT_BR).format(calendar.getTime()));
			}
		}
		
		return furoProdutoDTO;
	}
	
	@Override
	@Transactional
	public boolean validarNumeroEdicao(String codigoProduto, Long numeroEdicao) {

		if (codigoProduto == null || codigoProduto.isEmpty()){
			throw new IllegalArgumentException("Código é obrigatório.");
		}
		
		if (numeroEdicao == null){
			throw new IllegalArgumentException("Número edição é obrigatório.");
		}

		
		ProdutoEdicao produtoEdicao = 
			produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto,
																			 numeroEdicao);
		
		return (produtoEdicao != null) ? true : false;
	}
}