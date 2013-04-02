package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.UsuarioService;
@Service
public class FixacaoReparteServiceImpl implements FixacaoReparteService {
	@Autowired
	private FixacaoReparteRepository fixacaoReparteRepository;
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
		Cota cota = null;
		String nomeCotaBusca =  filtroConsultaFixacaoCotaDTO.getNomeCota();
		String codigoCotaBusca = filtroConsultaFixacaoCotaDTO.getCota();
		
		if(codigoCotaBusca != null && codigoCotaBusca !=""){
			listaFixacaoReparteDTO = fixacaoReparteRepository.obterFixacoesRepartePorCota(filtroConsultaFixacaoCotaDTO);
		}
		return listaFixacaoReparteDTO;
	}

	
	@Override
	@Transactional
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorProduto(
			FiltroConsultaFixacaoProdutoDTO filtroProduto) {
		Produto produto = produtoRepository.obterProdutoPorCodigo(filtroProduto.getCodigoProduto());
		List<FixacaoReparteDTO> resutado =estoqueProdutoCotaRepository.obterHistoricoEdicaoPorProduto(produto) ;
		return resutado; 
	}
	
	@Override
	@Transactional
	public List<FixacaoReparteDTO> obterHistoricoLancamentoPorCota(
			FiltroConsultaFixacaoCotaDTO filtroCota) {
		Cota cota = cotaRepository.obterPorNumerDaCota(new Integer(filtroCota.getCota()));
		List<FixacaoReparteDTO> resutado =estoqueProdutoCotaRepository.obterHistoricoEdicaoPorCota(cota) ;
		return resutado; 
	}
	
	@Override
	@Transactional
	public void adicionarFixacaoReparte(FixacaoReparteDTO fixacaoReparteDTO) {
		FixacaoReparte fixacaoReparte = getFixacaoRepartePorDTO(fixacaoReparteDTO);
		
		if(fixacaoReparte.getId() == null){
			fixacaoReparteRepository.adicionar(fixacaoReparte);
		}else{
			fixacaoReparteRepository.alterar(fixacaoReparte);
		}
	}

	@Override
	@Transactional
	public List<PdvDTO> obterListaPdvPorFixacao(Long id) {
		FixacaoReparte fixacaoReparte = this.obterFixacao(id);
		FiltroPdvDTO filtroPdvDTO = new FiltroPdvDTO();
		filtroPdvDTO.setIdCota(fixacaoReparte.getCotaFixada().getId());
		List <PdvDTO> listPDVs = pdvRepository.obterPdvPorCotaComEndereco(filtroPdvDTO.getIdCota());
		return listPDVs; 
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
	
	
	private FixacaoReparte getFixacaoRepartePorDTO(FixacaoReparteDTO fixacaoReparteDTO){
		FixacaoReparte fixacaoReparte = new FixacaoReparte();
		Cota cota = cotaRepository.obterPorNumerDaCota(fixacaoReparteDTO.getCotaFixada().intValue());
		Produto produto = produtoRepository.obterProdutoPorCodigo(fixacaoReparteDTO.getProdutoFixado().toString());
		
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
				fixacaoReparteDTO.setCotaFixadaString(fixacaoReparte.getCotaFixada().getNumeroCota().toString());
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
	
	

}
