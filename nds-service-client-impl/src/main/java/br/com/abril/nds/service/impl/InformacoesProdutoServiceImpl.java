package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.InformacoesAbrangenciaEMinimoProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.EstudoProdutoEdicaoBaseRepository;
import br.com.abril.nds.repository.InformacoesProdutoRepository;
import br.com.abril.nds.repository.ProdutoBaseSugeridaRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.InformacoesProdutoService;

@Service
public class InformacoesProdutoServiceImpl implements InformacoesProdutoService  {
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProduto;

	@Autowired
	private InformacoesProdutoRepository infoProdutosRepo;
	
	@Autowired	
	private EstudoProdutoEdicaoBaseRepository estudoProdEdicBaseRepo;
	
	@Autowired
	private ProdutoBaseSugeridaRepository baseSugRepo;
	
	
	@Override
	@Transactional
	public List<TipoClassificacaoProduto> buscarClassificacao() {
		return tipoClassificacaoProduto.buscarTodos();
	}

	@Override
	@Transactional
	public List<InformacoesProdutoDTO> buscarProduto(FiltroInformacoesProdutoDTO filtro) {
		return infoProdutosRepo.buscarProdutos(filtro);
	}

	@Override
	@Transactional
	public List<EdicaoBaseEstudoDTO> buscarBases(Long idEstudo) {
		return estudoProdEdicBaseRepo.obterEdicoesBase(idEstudo);
	}
	
	@Override
	@Transactional
	public List<ProdutoBaseSugeridaDTO> buscarBaseSugerida(Long idEstudo) {
		return baseSugRepo.obterBaseSugerida(idEstudo);
	}

	@Override
	@Transactional
	public InformacoesCaracteristicasProdDTO buscarCaracteristicas(String codProduto, Long numEdicao) {
		return infoProdutosRepo.buscarCaracteristicas(codProduto, numEdicao);
	}

	@Override
	public List<InfoProdutosItemRegiaoEspecificaDTO> buscarItemRegiao() {
		return infoProdutosRepo.buscarItensRegiao();
	}

	@Override
	public InformacoesAbrangenciaEMinimoProdDTO buscarAbrangenciaEMinimo(Long idEstudo) {
		return infoProdutosRepo.buscarAbrangenciaEMinimo(idEstudo);
	}

}
