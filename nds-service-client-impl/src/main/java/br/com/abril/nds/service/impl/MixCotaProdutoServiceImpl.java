package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RepartePDVRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.UsuarioService;
@Service
public class MixCotaProdutoServiceImpl implements MixCotaProdutoService {
	@Autowired
	private MixCotaProdutoRepository mixCotaProdutoRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CotaRepository cotaRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProdutoRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private FixacaoRepartePdvRepository fixacaoRepartePdvRepository;
	
	@Autowired
	private RepartePDVRepository repartePDVRepository;
	

	@Transactional
	@Override
	public List<MixCotaDTO> pesquisarPorCota(FiltroConsultaMixPorCotaDTO
			filtroConsultaMixCotaDTO) {
		return mixCotaProdutoRepository.pesquisarPorCota(filtroConsultaMixCotaDTO);
	}

	@Transactional
	@Override
	public void removerMixCotaProduto(
			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.buscarPorId(filtroConsultaMixCotaDTO.getId());
		mixCotaProdutoRepository.remover(mixCotaProduto);
		
	}

	@Transactional
	@Override
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO) {
		return mixCotaProdutoRepository.pesquisarPorProduto(filtroConsultaMixProdutoDTO);
	}

	@Transactional
	@Override
	public List<RepartePDVDTO> obterRepartePdv(
			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
		MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.buscarPorId(filtroConsultaMixCotaDTO.getId());
		return null;
//				repartePDVRepository.obterRepartePdvPorCota(mixCotaProduto.getCota().getId());
	}
	
	
	

}


