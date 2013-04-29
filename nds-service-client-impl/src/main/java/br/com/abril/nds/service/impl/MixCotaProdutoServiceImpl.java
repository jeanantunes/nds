package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CopiaMixFixacaoDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RepartePDVRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.MixCotaProdutoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.vo.ValidacaoVO;
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
	private CotaService cotaService;
	
	@Autowired
	private ProdutoService produtoService;
	
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
	

	@Override
	@Transactional(readOnly = true)
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
	
	@Override
	@Transactional(readOnly = true)
	public List<MixProdutoDTO> pesquisarPorProduto(
			FiltroConsultaMixPorProdutoDTO filtroConsultaMixProdutoDTO) {
		return mixCotaProdutoRepository.pesquisarPorProduto(filtroConsultaMixProdutoDTO);
	}

//	@Override
//	@Transactional(readOnly = true)
//	public List<RepartePDVDTO> obterRepartePdv(
//			FiltroConsultaMixPorCotaDTO filtroConsultaMixCotaDTO) {
//		MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.buscarPorId(filtroConsultaMixCotaDTO.getId());
//		return null;
////				repartePDVRepository.obterRepartePdvPorCota(mixCotaProduto.getCota().getId());
//	}
	
	
	@Override
	@Transactional(readOnly=true)
	public MixCotaProdutoDTO obterPorId(Long id){
		return getMixCotaProdutoDTO(mixCotaProdutoRepository.buscarPorId(id));
	}
	
	private MixCotaProdutoDTO getMixCotaProdutoDTO(MixCotaProduto mixCotaProduto) {
		MixCotaProdutoDTO mixCotaProdutoDTO = new MixCotaProdutoDTO();
		mixCotaProdutoDTO.setId(mixCotaProduto.getId());
		mixCotaProdutoDTO.setReparteMaximo(mixCotaProduto.getReparteMaximo());
		mixCotaProdutoDTO.setReparteMedio(mixCotaProduto.getReparteMedio());
		mixCotaProdutoDTO.setReparteMinimo(mixCotaProduto.getReparteMinimo());
		
		if(mixCotaProduto.getCota()!=null){
			mixCotaProdutoDTO.setCotaId(mixCotaProduto.getCota().getId());
			if((mixCotaProduto.getCota().getPessoa()!=null)){
				mixCotaProdutoDTO.setNomeCota(mixCotaProduto.getCota().getPessoa().getNome());
				mixCotaProdutoDTO.setNumeroCota(mixCotaProduto.getCota().getNumeroCota().toString());
			}
		}
		if(mixCotaProduto.getProduto() !=null){
			mixCotaProdutoDTO.setProdutoId(mixCotaProduto.getProduto().getId());
			mixCotaProdutoDTO.setNomeProduto(mixCotaProduto.getProduto().getNome());
			mixCotaProdutoDTO.setCodigoProduto(mixCotaProduto.getProduto().getCodigo());
			mixCotaProdutoDTO.setClassificacaoProduto(mixCotaProduto.getProduto().getTipoClassificacaoProduto().getDescricao());
		}
		if(mixCotaProduto.getRepartesPDV()!=null){
			for(RepartePDV repartePDV: mixCotaProduto.getRepartesPDV()){
				RepartePDVDTO repartePDVDTO = new RepartePDVDTO();
				repartePDVDTO.setReparte(repartePDV.getReparte());
			}
		}
		return mixCotaProdutoDTO;
	}

	@Override
	@Transactional(readOnly=true)
	public List<PdvDTO> obterListaPdvPorMix(Long id) {
		MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.buscarPorId(id);
		FiltroPdvDTO filtroPdvDTO = new FiltroPdvDTO();
		filtroPdvDTO.setIdCota(mixCotaProduto.getCota().getId());
		List <PdvDTO> listPDVs = pdvRepository.obterPdvPorCotaComEndereco(filtroPdvDTO.getIdCota());
		return listPDVs; 
	}

	@Override
	@Transactional
	public void excluirTodos() {
		mixCotaProdutoRepository.excluirTodos();
		
	}

	@Override
	@Transactional
	public void excluirMixPorCota(Long idCota){
		mixCotaProdutoRepository.removerPorIdCota(idCota);
	}
	
	
	@Override
	@Transactional
	public void adicionarListaMixPorCota(List<MixCotaProdutoDTO> listaMixCota , Integer cotaId) {
		
		Cota cota = cotaService.obterPorNumeroDaCota(cotaId);
		
		if (cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.CONVENCIONAL)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não é do tipo Alternativo: ["+cota.getNumeroCota()+":"+cota.getPessoa().getNome()+"]");
		}
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		List<String> produtosJaCadastrados = new ArrayList<String>(); 
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
				if (validaPreenchimentoMixPorCota(mixCotaProdutoDTO)) {
					
					Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
					MixCotaProduto mixCotaProduto = new MixCotaProduto();
					mixCotaProduto.setProduto(produto);
					mixCotaProduto.setCota(cota);
					mixCotaProduto.setDataHora(new Date());
					mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
					mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
					mixCotaProduto.setUsuario(usuario);
					if(mixCotaProduto.getProduto()!=null || mixCotaProduto.getProduto().getId() !=null){
						if(!mixCotaProdutoRepository.existeMixCotaProdutoCadastrado(mixCotaProduto.getProduto().getId(), mixCotaProduto.getCota().getId())){
							mixCotaProdutoRepository.adicionar(mixCotaProduto);
						}
						else {
							produtosJaCadastrados.add("Produto já Cadastrado: ["+produto.getCodigo()+":"+produto.getNome()+"]");
						}
					}
				}
		}
		
		if (!produtosJaCadastrados.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, produtosJaCadastrados));
		}
	}

	private boolean validaPreenchimentoMixPorCota(MixCotaProdutoDTO mixCotaProdutoDTO) {
		return StringUtils.isNotBlank(mixCotaProdutoDTO.getCodigoProduto()) 
				&& StringUtils.isNotBlank(mixCotaProdutoDTO.getNomeProduto()) 
				&& mixCotaProdutoDTO.getReparteMinimo() != null 
				&& mixCotaProdutoDTO.getReparteMaximo() != null;
	}
	
	
	@Override
	@Transactional
	public void adicionarListaMixPorProduto(List<MixCotaProdutoDTO> listaMixCota , String produtoId) {
		
		Produto produto = produtoService.obterProdutoPorCodigo(produtoId);
		Usuario usuario = usuarioService.getUsuarioLogado();
		
		List<String> cotasJaCadastradas = new ArrayList<String>(); 
		
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
			if (validaPreenchimentoMixPorProduto(mixCotaProdutoDTO)) {
				
				Cota cota = cotaService.obterPorNumeroDaCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
				if (cota.getTipoDistribuicaoCota().equals(TipoDistribuicaoCota.ALTERNATIVO)) {
					MixCotaProduto mixCotaProduto = new MixCotaProduto();
					mixCotaProduto.setUsuario(usuario);
					mixCotaProduto.setProduto(produto);
					mixCotaProduto.setCota(cota);
					mixCotaProduto.setDataHora(new Date());
					mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
					mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
					if(mixCotaProduto.getProduto()!=null || mixCotaProduto.getProduto().getId() != null) {
						if(!mixCotaProdutoRepository.existeMixCotaProdutoCadastrado(mixCotaProduto.getProduto().getId(), mixCotaProduto.getCota().getId())) {
							mixCotaProdutoRepository.adicionar(mixCotaProduto);
						}
						else {
							cotasJaCadastradas.add("Produto já Cadastrado: ["+cota.getNumeroCota()+":"+cota.getPessoa().getNome()+"]");
						}
					}
				}
				cotasJaCadastradas.add("Cota não é do tipo Alternativo: ["+cota.getNumeroCota()+":"+cota.getPessoa().getNome()+"]");
			}
		}
		if (!cotasJaCadastradas.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, cotasJaCadastradas));
		}
	}

	private boolean validaPreenchimentoMixPorProduto(MixCotaProdutoDTO mixCotaProdutoDTO) {
		return StringUtils.isNotBlank(mixCotaProdutoDTO.getNomeCota()) 
				&& StringUtils.isNotBlank(mixCotaProdutoDTO.getNumeroCota()) 
				&& mixCotaProdutoDTO.getReparteMinimo() != null 
				&& mixCotaProdutoDTO.getReparteMaximo() != null;
	}
	
	@Override
	@Transactional
	public void adicionarMixEmLote(List<MixCotaProdutoDTO> mixCotaProdutoDTOList) {
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		for (MixCotaProdutoDTO mixCotaProdutoDTO : mixCotaProdutoDTOList) {
			if (validaMixEmLote(mixCotaProdutoDTO)) {

				Cota cota = cotaService.obterPorNumeroDaCota(new Integer(mixCotaProdutoDTO.getNumeroCota()));
				Produto produto = produtoService.obterProdutoPorCodigo(mixCotaProdutoDTO.getCodigoProduto());
				
				MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.obterMixPorCotaProduto(cota.getId(), produto.getId());
				if (mixCotaProduto == null) {
					mixCotaProduto = new MixCotaProduto();
					mixCotaProduto.setProduto(produto);
					mixCotaProduto.setCota(cota);
				}
				
				mixCotaProduto.setDataHora(new Date());
				mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
				mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
				mixCotaProduto.setUsuario(usuario);
				
				if (mixCotaProduto.getProduto() != null || mixCotaProduto.getProduto().getId() != null) {
					mixCotaProdutoRepository.merge(mixCotaProduto);
				}
			}
		}
	}
	
	@Override
	@Transactional
	public boolean gerarCopiaMix(CopiaMixFixacaoDTO copiaMix){
		
		switch (copiaMix.getTipoCopia()) {
		case COTA:

			FiltroConsultaMixPorCotaDTO fMixCota = new FiltroConsultaMixPorCotaDTO();
			fMixCota.setCota(copiaMix.getCotaNumeroOrigem());
			
	
			Cota cotaDestino = cotaService.obterPorNumeroDaCota(copiaMix.getCotaNumeroDestino());
			
			List<MixCotaDTO> mixCotaOrigem = pesquisarPorCota(fMixCota);
			if(mixCotaOrigem==null || mixCotaOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum MIX encontrado para cópia.");
			}
			for (MixCotaDTO mixCotaDTO : mixCotaOrigem) {
				mixCotaDTO.setIdCota(new BigInteger(cotaDestino.getId().toString()));
			}
					
			try {
				this.mixCotaProdutoRepository.gerarCopiaMixCota(mixCotaOrigem,this.usuarioService.getUsuarioLogado());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			break;
		case PRODUTO:
			
			FiltroConsultaMixPorProdutoDTO fMixProduto = new FiltroConsultaMixPorProdutoDTO();
			
			Produto produtoDestino = produtoService.obterProdutoPorCodigo(copiaMix.getCodigoProdutoDestino());
			fMixProduto.setCodigoProduto(copiaMix.getCodigoProdutoOrigem());
			
			List<MixProdutoDTO> mixProdutoOrigem = pesquisarPorProduto(fMixProduto);
			if(mixProdutoOrigem==null || mixProdutoOrigem.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum MIX encontrado para cópia.");
			}
			
			for (MixProdutoDTO mixProdutoDTO : mixProdutoOrigem) {
				mixProdutoDTO.setIdProduto(new BigInteger(produtoDestino.getId().toString()));
			}
			
			try {
				this.mixCotaProdutoRepository.gerarCopiaMixProduto(mixProdutoOrigem,this.usuarioService.getUsuarioLogado());
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

	private boolean validaMixEmLote(MixCotaProdutoDTO mixCotaProdutoDTO) {
		return StringUtils.isNotBlank(mixCotaProdutoDTO.getCodigoProduto())
				&& StringUtils.isNotBlank(mixCotaProdutoDTO.getNumeroCota()) 
				&& mixCotaProdutoDTO.getReparteMinimo() != null
				&& mixCotaProdutoDTO.getReparteMaximo() != null;
	}
}
