package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RepartePDVRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.UsuarioService;

@Service
public class FixacaoReparteServiceImpl implements FixacaoReparteService {
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private FixacaoReparteRepository fixacaoReparteRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private CotaService cotaService;
	
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
	RepartePDVRepository repartePDVRepository;
	
	@Transactional
	@Override
	public void incluirFixacaoReparte(FixacaoReparte fixacaoReparte) {
		
		fixacaoReparteRepository.adicionar(fixacaoReparte);
	}

	@Transactional
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(
			FiltroConsultaFixacaoProdutoDTO filtroConsultaFixacaoProdutoDTO) {
		List<FixacaoReparteDTO> listaFixacaoReparteDTO = new ArrayList<FixacaoReparteDTO>();
		Produto produto = new Produto();
		String nomeProdutoBusca =  filtroConsultaFixacaoProdutoDTO.getNomeProduto();
		String codigoProdutoBusca = filtroConsultaFixacaoProdutoDTO.getCodigoProduto();
		boolean isCodigoEmpty =false; 
		boolean isNomeEmpty =false;
		
		if(nomeProdutoBusca != null){
			 produto = produtoRepository.obterProdutoPorNome(nomeProdutoBusca);
		}else{
			 produto = produtoRepository.obterProdutoPorCodigo(codigoProdutoBusca);
		}
		isNomeEmpty = (produto == null || produto.getNome() == null || produto.getNome() == "");
		isCodigoEmpty =  (produto==null || produto.getCodigo() == null || produto.getCodigo()=="");
		if(isNomeEmpty && isCodigoEmpty){
			return listaFixacaoReparteDTO;
		}else{
			
			listaFixacaoReparteDTO = fixacaoReparteRepository.obterFixacoesRepartePorProduto(filtroConsultaFixacaoProdutoDTO);
		
		}
		return listaFixacaoReparteDTO;
	}

	@Transactional
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorCota(
			FiltroConsultaFixacaoCotaDTO filtroConsultaFixacaoCotaDTO) {
		List<FixacaoReparteDTO> listaFixacaoReparteDTO = new ArrayList<FixacaoReparteDTO>();
		String codigoCotaBusca = filtroConsultaFixacaoCotaDTO.getCota();
		
		if(StringUtils.isNotEmpty(codigoCotaBusca)){
			listaFixacaoReparteDTO = fixacaoReparteRepository.obterFixacoesRepartePorCota(filtroConsultaFixacaoCotaDTO);
		}
		return listaFixacaoReparteDTO;
	}

	@Override
	@Transactional
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorProduto(
			FiltroConsultaFixacaoProdutoDTO filtroProduto) {
		List<FixacaoReparteDTO> resultado = null;
		if(filtroProduto != null && filtroProduto.getCodigoProduto()!=null){
			Produto produto = produtoRepository.obterProdutoPorCodigo(filtroProduto.getCodigoProduto());
			 resultado = estoqueProdutoCotaRepository.obterHistoricoEdicaoPorProduto(produto) ;
		}
		return resultado; 
	}
	
	@Override
	@Transactional
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorCota(
			FiltroConsultaFixacaoCotaDTO filtroCota) {
		Cota cota = cotaRepository.obterPorNumerDaCota(new Integer(filtroCota.getCota()));
		List<FixacaoReparteDTO> resultado = estoqueProdutoCotaRepository.obterHistoricoEdicaoPorCota(cota, filtroCota.getCodigoProduto()) ;
		return resultado; 
	}
	
	@Override
	@Transactional
	public FixacaoReparte adicionarFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte = getFixacaoRepartePorDTO(fixacaoReparteDTO);
		
		if(fixacaoReparte.getId() != null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Já existe fixação para esta cota[" +
					fixacaoReparteDTO.getCotaFixadaString() +
					"] e produto[" + fixacaoReparteDTO.getProdutoFixado() + "].");
		}
		
		fixacaoReparteRepository.adicionar(fixacaoReparte);
		
		fixarTudoNoPDVPrincipal(fixacaoReparte);
		
		return fixacaoReparte;
	}

	private void fixarTudoNoPDVPrincipal(FixacaoReparte fixacaoReparte) {
		List<PDV> pdvs = fixacaoReparte.getCotaFixada().getPdvs();
		
		if (pdvs != null && pdvs.size() > 0) {
			PDV pdv = pdvs.get(0);
			RepartePDV repartePDV = repartePDVRepository.obterRepartePorPdv(fixacaoReparte.getId(), fixacaoReparte.getProdutoFixado().getId(), pdv.getId());
			if(repartePDV == null) {
				repartePDV = new RepartePDV();
			}
			
			repartePDV.setFixacaoReparte(fixacaoReparte);
			repartePDV.setPdv(pdv);
			repartePDV.setReparte(fixacaoReparte.getQtdeExemplares().intValue());
			repartePDV.setProduto(fixacaoReparte.getProdutoFixado());
			
			repartePDVRepository.merge(repartePDV);
		}
	}

	@Override
	@Transactional
	public List<PdvDTO> obterListaPdvPorFixacao(Long id) {
		FixacaoReparte fixacaoReparte = this.obterFixacao(id);
		FiltroPdvDTO filtroPdvDTO = new FiltroPdvDTO();
		filtroPdvDTO.setIdCota(fixacaoReparte.getCotaFixada().getId());
		return pdvRepository.obterPDVsPorCota(filtroPdvDTO);
	}
	
	@Override
	@Transactional
	public void removerFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorId(fixacaoReparteDTO.getId());
		List<FixacaoRepartePdv> repartes = fixacaoRepartePdvRepository.obterFixacaoRepartePdvPorFixacaoReparte(fixacaoReparte);
		
		for (FixacaoRepartePdv fixacaoRepartePdv : repartes) {
			fixacaoRepartePdvRepository.remover(fixacaoRepartePdv);
		}
		fixacaoReparteRepository.remover(fixacaoReparte);
	}
	
	private FixacaoReparte getFixacaoRepartePorDTO(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte = new FixacaoReparte();
		Cota cota = cotaRepository.obterPorNumerDaCota(fixacaoReparteDTO.getCotaFixada().intValue());
		Produto produto = produtoRepository.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado().toString());
		
		validaStatusProduto(fixacaoReparteDTO, produto);
		
		fixacaoReparte.setProdutoFixado(produto);
		fixacaoReparte.setCotaFixada(cota);
		
		// obter o id da base
		
		fixacaoReparte = buscarFixacaoCadastrada(fixacaoReparte);
		if(fixacaoReparte == null){
			fixacaoReparte  = new FixacaoReparte();
		}

		Usuario usuario = usuarioService.getUsuarioLogado();
		fixacaoReparte.setUsuario(usuario);
		fixacaoReparte.setCotaFixada(cota);
		fixacaoReparte.setDataHora(new Date());
		fixacaoReparte.setProdutoFixado(produto);
		fixacaoReparte.setQtdeEdicoes(fixacaoReparteDTO.getQtdeEdicoes());
		fixacaoReparte.setQtdeExemplares(fixacaoReparteDTO.getQtdeExemplares());
		fixacaoReparte.setQtdeEdicoes(fixacaoReparteDTO.getQtdeEdicoes());
		fixacaoReparte.setEdicaoInicial(fixacaoReparteDTO.getEdicaoInicial()!=null? new Integer(fixacaoReparteDTO.getEdicaoInicial()) : null);
		fixacaoReparte.setEdicaoFinal(fixacaoReparteDTO.getEdicaoFinal() !=null? new Integer(fixacaoReparteDTO.getEdicaoFinal()):null);
		
		return fixacaoReparte;
	}

	private void validaStatusProduto(FixacaoReparteDTO fixacaoReparteDTO, Produto produto) {
		
		if (fixacaoReparteDTO.getEdicaoInicial() != null && fixacaoReparteDTO.getEdicaoFinal() != null) {
			List<ProdutoEdicao> listProdutoEdicao = produtoEdicaoRepository.listProdutoEdicaoPorCodProdutoNumEdicoes(produto.getCodigo(), fixacaoReparteDTO.getEdicaoInicial().longValue(), fixacaoReparteDTO.getEdicaoFinal().longValue());
			for (ProdutoEdicao produtoEdicao : listProdutoEdicao) {
				statusPermitido(new ArrayList<>(produtoEdicao.getLancamentos()).get(0).getStatus());
			}
		}
		
	}

	private void statusPermitido(StatusLancamento status) {
		switch (status) {
		
		case CONFIRMADO:
		case FECHADO:
			throw new ValidacaoException(TipoMensagem.WARNING,"Não permitido a fixação devido ao status das edições.");
			
		case BALANCEADO:
		case BALANCEADO_RECOLHIMENTO:
		case CANCELADO:
		case EM_BALANCEAMENTO:
		case EM_BALANCEAMENTO_RECOLHIMENTO:
		case ESTUDO_FECHADO:
		case EXCLUIDO_RECOLHIMENTO:
		case EXPEDIDO:
		case FURO:
		case PLANEJADO:
		case RECOLHIDO:
			
		default:
			break;
		}
	}
	
	@Override
	@Transactional
	public List<TipoClassificacaoProduto> obterClassificacoesProduto(){
		return tipoClassificacaoProdutoRepository.obterTodos();
	}
	
	@Override
	@Transactional
	public FixacaoReparte obterFixacao(Long id) {
		return fixacaoReparteRepository.buscarPorId(id); 
	}
	
	@Override
	@Transactional
	public FixacaoReparteDTO obterFixacaoDTO(Long id){
		FixacaoReparte fixacaoReparte = obterFixacao(id);
		return getFixacaoRepartePorDTO(fixacaoReparte);
		
	}
	
	private FixacaoReparteDTO getFixacaoRepartePorDTO(FixacaoReparte fixacaoReparte) {
		FixacaoReparteDTO fixacaoReparteDTO = new FixacaoReparteDTO();
		if(fixacaoReparte!=null){
			fixacaoReparteDTO.setQtdeEdicoes(fixacaoReparte.getQtdeEdicoes());
			fixacaoReparteDTO.setQtdeExemplares(fixacaoReparte.getQtdeExemplares());
			fixacaoReparteDTO.setId(fixacaoReparte.getId());
			if(fixacaoReparte.getEdicaoInicial()!=null){
				fixacaoReparteDTO.setEdicaoInicial(fixacaoReparte.getEdicaoInicial());
			}
			if(fixacaoReparte.getEdicaoFinal()!=null){
				fixacaoReparteDTO.setEdicaoFinal((fixacaoReparte.getEdicaoFinal()));
			}
			if(fixacaoReparte.getCotaFixada()!=null){
				fixacaoReparteDTO.setCotaFixada(fixacaoReparte.getCotaFixada().getNumeroCota());
				fixacaoReparteDTO.setNomeCota(fixacaoReparte.getCotaFixada().getPessoa().getNome());
			}
			if(fixacaoReparte.getProdutoFixado()!=null){
				fixacaoReparteDTO.setProdutoFixado((fixacaoReparte.getProdutoFixado().getCodigo()));
				fixacaoReparteDTO.setNomeProduto(fixacaoReparte.getProdutoFixado().getNome());
				if(fixacaoReparte.getProdutoFixado().getTipoClassificacaoProduto() !=null){
					fixacaoReparteDTO.setClassificacaoProduto(fixacaoReparte.getProdutoFixado().getTipoClassificacaoProduto().getDescricao());
				}
			}
			
		}
		return fixacaoReparteDTO;
	}

	@Override
	public FixacaoReparte buscarFixacaoCadastrada(FixacaoReparte fixacaoReparte) {
		return fixacaoReparteRepository.buscarPorProdutoCota(fixacaoReparte.getCotaFixada(), fixacaoReparte.getProdutoFixado()); 
		
	}

	public CotaDTO getCotaDTO(Cota cota){
		if(cota != null){	
			CotaDTO cotaDTO = new CotaDTO();
			cotaDTO.setNumeroCota(cota.getNumeroCota());
			cotaDTO.setNomePessoa(cota.getPessoa().getNome());
			return cotaDTO;
		}else{
			return null;
		}
	}

	@Transactional
	@Override
	public boolean isCotaPossuiVariosPdvs(Long idFixacao) {
		FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorId(idFixacao);
		if(fixacaoReparte.getCotaFixada() !=null){
			Long id = fixacaoReparte.getCotaFixada().getId();
			int size = pdvRepository.obterPdvPorCotaComEndereco(id).size();
			return size > 1;
		}else{
			return false;
		}
	}
	
	@Transactional
	@Override
	public void excluirFixacaoPorCota(Long idCota){
		
		Cota cota = cotaRepository.buscarCotaPorID(idCota);
		
		List<FixacaoReparte> fixacaoRepartes = fixacaoReparteRepository.buscarPorCota(cota);
		

		for(FixacaoReparte fr : fixacaoRepartes ){
			
			fixacaoRepartePdvRepository.removerFixacaoReparte(fr);
		}
		
		fixacaoReparteRepository.removerPorCota(cota);
	}

	@Transactional
	@Override
	public boolean isFixacaoExistente(FixacaoReparteDTO fixacaoReparteDTO) {
		return fixacaoReparteRepository.isFixacaoExistente(fixacaoReparteDTO);
	}

	@Transactional
	@Override
	public boolean isCotaValida(FixacaoReparteDTO fixacaoReparteDTO) {
		Cota cota = cotaRepository.obterPorNumerDaCota(fixacaoReparteDTO.getCotaFixada());
		return (cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO) 
				|| cota.getSituacaoCadastro().equals(SituacaoCadastro.SUSPENSO))
				&& cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.CONVENCIONAL);
	}

	@Override
	public boolean gerarCopiafixacao(CopiaMixFixacaoDTO copiaDTO) {

		
		switch (copiaDTO.getTipoCopia()) {
		case COTA:

			FiltroConsultaFixacaoCotaDTO fMixCota = new FiltroConsultaFixacaoCotaDTO();
			fMixCota.setCota(copiaDTO.getCotaNumeroOrigem().toString());
			
	
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaDTO.getCotaNumeroDestino());
			
			List<FixacaoReparteDTO> mixCotaOrigem = fixacaoReparteRepository.obterFixacoesRepartePorCota(fMixCota);
			if(mixCotaOrigem==null || mixCotaOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Fixação de Reparte não encontrada para cópia.");
			}
			for (FixacaoReparteDTO mixCotaDTO : mixCotaOrigem) {
				mixCotaDTO.setCotaFixadaId(cotaDestino.getId());
			}
					
			try {
//				this.mixCotaProdutoRepository.gerarCopiaMixCota(mixCotaOrigem,this.usuarioService.getUsuarioLogado());
				fixacaoReparteRepository.gerarCopiaPorCotaFixacaoReparte(mixCotaOrigem,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			break;
		case PRODUTO:
			
			FiltroConsultaFixacaoProdutoDTO fMixProduto = new FiltroConsultaFixacaoProdutoDTO();
			
			Produto produtoDestino = produtoService.obterProdutoPorCodigo(copiaDTO.getCodigoProdutoDestino());
			fMixProduto.setCodigoProduto(copiaDTO.getCodigoProdutoOrigem());
			
			List<FixacaoReparteDTO> mixProdutoOrigem = fixacaoReparteRepository.obterFixacoesRepartePorProduto(fMixProduto);
			
//			List<MixProdutoDTO> mixProdutoOrigem = pesquisarPorProduto(fMixProduto);
			if(mixProdutoOrigem==null || mixProdutoOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Fixação de Reparte não encontrada para cópia.");
			}
			
			for (FixacaoReparteDTO mixProdutoDTO : mixProdutoOrigem) {
				mixProdutoDTO.setProdutoFixadoId(produtoDestino.getId());
			}
			
			try {
				this.fixacaoReparteRepository.gerarCopiaPorProdutoFixacaoReparte(mixProdutoOrigem,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			break;

		default:
			break;
		}
		return true;
	
	}
	
}
