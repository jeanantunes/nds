package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MixProdutoDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaMixPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
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
	}
	
	
	@Override
	@Transactional
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
			List<RepartePDVDTO> repartesDTO =new ArrayList<RepartePDVDTO>();
			for(RepartePDV repartePDV: mixCotaProduto.getRepartesPDV()){
				RepartePDVDTO repartePDVDTO = new RepartePDVDTO();
				repartePDVDTO.setReparte(repartePDV.getReparte());
			}
		}
		return mixCotaProdutoDTO;
	}

	@Override
	@Transactional
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
		Usuario usuario = usuarioService.getUsuarioLogado();
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
				if(StringUtils.isEmpty(mixCotaProdutoDTO.getCodigoProduto()) || StringUtils.isEmpty(mixCotaProdutoDTO.getNomeProduto()) || mixCotaProdutoDTO.getReparteMinimo()==null || mixCotaProdutoDTO.getReparteMaximo()==null){
					continue;
				}else{
					
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
					}else{
						continue;
					}
				
				}
		}
	}
	
	@Override
	public void adicionarListaMixPorCota(List<MixCotaProdutoDTO> mixCotaProdutoDTOList) {
		
		Usuario usuario = usuarioService.getUsuarioLogado();
		for (MixCotaProdutoDTO mixCotaProdutoDTO : mixCotaProdutoDTOList) {
				if(StringUtils.isEmpty(mixCotaProdutoDTO.getCodigoProduto()) || 
						StringUtils.isEmpty(mixCotaProdutoDTO.getNumeroCota()) ||
						mixCotaProdutoDTO.getReparteMinimo()==null || mixCotaProdutoDTO.getReparteMaximo()==null){
					continue;
				}else{
					
					Cota cota = cotaService.obterPorNumeroDaCota(new Integer(mixCotaProdutoDTO.getNumeroCota()));
					
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
					}else{
						continue;
					}
				
				}
		}
	}
	
	@Override
	@Transactional
	public void adicionarListaMixPorProduto(List<MixCotaProdutoDTO> listaMixCota , String produtoId) {
		
		Produto produto = produtoService.obterProdutoPorCodigo(produtoId);
		Usuario usuario = usuarioService.getUsuarioLogado();
		for (MixCotaProdutoDTO mixCotaProdutoDTO : listaMixCota) {
			if(StringUtils.isEmpty(mixCotaProdutoDTO.getNomeCota()) || StringUtils.isEmpty(mixCotaProdutoDTO.getNumeroCota()) || mixCotaProdutoDTO.getReparteMinimo()==null || mixCotaProdutoDTO.getReparteMaximo()==null){
				continue;
			}else{
				Cota cota = cotaService.obterPorNumeroDaCota(Integer.parseInt(mixCotaProdutoDTO.getNumeroCota()));
				MixCotaProduto mixCotaProduto = new MixCotaProduto();
				mixCotaProduto.setUsuario(usuario);
				mixCotaProduto.setProduto(produto);
				mixCotaProduto.setCota(cota);
				mixCotaProduto.setDataHora(new Date());
				mixCotaProduto.setReparteMinimo(mixCotaProdutoDTO.getReparteMinimo());
				mixCotaProduto.setReparteMaximo(mixCotaProdutoDTO.getReparteMaximo());
				if(mixCotaProduto.getProduto()!=null || mixCotaProduto.getProduto().getId() !=null){
					if(!mixCotaProdutoRepository.existeMixCotaProdutoCadastrado(mixCotaProduto.getProduto().getId(), mixCotaProduto.getCota().getId())){
						mixCotaProdutoRepository.adicionar(mixCotaProduto);
					}
				}else{
					continue;
				}
			}
		}
		
	}


}

