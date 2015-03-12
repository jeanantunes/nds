package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.dto.BonificacaoJsonDTO;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.InfoProdutosBonificacaoDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AreaInfluenciaPDVRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.EstudoProdutoEdicaoBaseRepository;
import br.com.abril.nds.repository.InformacoesProdutoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoBaseSugeridaRepository;
import br.com.abril.nds.repository.RegiaoRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.repository.TipoGeradorFluxoPDVRepsitory;
import br.com.abril.nds.repository.TipoPontoPDVRepository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class InformacoesProdutoServiceImpl implements InformacoesProdutoService  {
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProduto;

	@Autowired
	private InformacoesProdutoRepository infoProdutosRepo;
	
	@Autowired	
	private EstudoProdutoEdicaoBaseRepository estudoProdEdicBaseRepo;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqRepo;
	
	@Autowired
	private ProdutoBaseSugeridaRepository baseSugRepo;
	
	@Autowired
	private EstudoGeradoRepository estudoGeradoRepository;
	
	@Autowired
	private RegiaoRepository regiaoRepository;
	
	@Autowired
    private TipoPontoPDVRepository tipoPontoPDVRepository;
	
	@Autowired
    private AreaInfluenciaPDVRepository areaInfluenciaPDVRepository;
	
	@Autowired
    private TipoGeradorFluxoPDVRepsitory tipoGeradorFluxoPDVRepsitory;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoCota;
	
	@Override
	@Transactional
	public List<TipoClassificacaoProduto> buscarClassificacao() {
		return tipoClassificacaoProduto.buscarTodos();
	}

	@Override
	@Transactional
	public List<InformacoesProdutoDTO> buscarProduto(FiltroInformacoesProdutoDTO filtro) {
		List<InformacoesProdutoDTO> listaInformacoesProduto = infoProdutosRepo.buscarProdutos(filtro); 
		
		for (InformacoesProdutoDTO informacoesProdutoDTO : listaInformacoesProduto) {
			
			if((informacoesProdutoDTO.getStatus() != null && informacoesProdutoDTO.getStatus().equals(StatusLancamento.FECHADO))
					&& (informacoesProdutoDTO.getVenda() == null || informacoesProdutoDTO.getVenda().compareTo(BigInteger.ZERO) <= 0)){
				informacoesProdutoDTO.setVenda(estoqueProdutoCota.obterVendaBaseadoNoEstoque(informacoesProdutoDTO.getIdProdutoEdicao()).toBigInteger());
			}
			
		}
		
		return listaInformacoesProduto;
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
	@Transactional(readOnly=true)
	public List<InfoProdutosBonificacaoDTO> buscarItemRegiao(Long idEstudo) {
		
		EstudoGerado estudoGerado = estudoGeradoRepository.buscarPorId(idEstudo);
		
		List<InfoProdutosBonificacaoDTO> itensRetorno = new ArrayList<>();
		
		if(estudoGerado!= null){
			
			List<BonificacaoDTO> bonificacoes = this.obterBonificacoes(estudoGerado.getDadosVendaMedia());
			
			if(bonificacoes!= null && !bonificacoes.isEmpty()){
				
				for(BonificacaoDTO item : bonificacoes){
					
                    InfoProdutosBonificacaoDTO retorno = new InfoProdutosBonificacaoDTO();
				    
                    retorno.setComponente(item.getComponente().getDescricao());
                    retorno.setBonificacao(new BigDecimal(Util.nvl(item.getBonificacao(),0D)));
                    retorno.setQtdReparteMin(Util.nvl(item.getReparteMinimo(),0D).intValue());
                    
				    switch (item.getComponente()) {
			        
				    case TIPO_PONTO_DE_VENDA:
				        
                        retorno.setNomeItem(this.obterNomeTipoPDV(item.getElemento()));
				        
			            break;
			            
			        case AREA_DE_INFLUENCIA:
			            
                        retorno.setNomeItem(this.obterNomeAreaInfluencia(item.getElemento()));
			            
			            break;

			        case BAIRRO:
			            
			            retorno.setNomeItem(item.getElemento());

			            break;
			            
			        case DISTRITO:

			            retorno.setNomeItem(item.getElemento());
			            
			            break;
			            
			        case GERADOR_DE_FLUXO:

			            retorno.setNomeItem(this.obterNomeTipoGeradoFluxo(item.getElemento()));

			            break;
			            
			        case COTAS_A_VISTA:

			            retorno.setNomeItem(this.obterDescricaoCotaAVista(item.getElemento()));
			            
			            break;
			            
			        case COTAS_NOVAS_RETIVADAS :

			            retorno.setNomeItem(this.obterDescricaoCotasNovaReativadas(item.getElemento()));
			            
			            break;
			            
			        case REGIAO:
			            
                        retorno.setNomeItem(this.obterNomeDaRegiao(item.getElemento()));
                        
			            break;
			            
			        default:
			            break;
			        }
				    
                    itensRetorno.add(retorno);
				}
			}
		}
		
		return itensRetorno;
	}

	private String  obterNomeDaRegiao(String item) {
		
		final Long idRegiao = Long.parseLong(item);
		
		Regiao regiao = regiaoRepository.buscarPorId(idRegiao);
		
		if(regiao!= null){
			return regiao.getNomeRegiao();
		}
		
		return null;
	}
	
	private String  obterNomeTipoPDV(String item) {
        
        final Long idTipoPDV = Long.parseLong(item);
        
        TipoPontoPDV tipoPontoPDV = tipoPontoPDVRepository.buscarPorId(idTipoPDV);
        
        if(tipoPontoPDV != null){
            return tipoPontoPDV.getDescricao();
        }
        
        return null;
    }
	
	private String  obterNomeAreaInfluencia(String item) {
        
        final Long idAreaInfluencia = Long.parseLong(item);
        
        AreaInfluenciaPDV areaInfluenciaPDV = areaInfluenciaPDVRepository.buscarPorId(idAreaInfluencia);
        
        if(areaInfluenciaPDV != null){
            return areaInfluenciaPDV.getDescricao();
        }
        
        return null;
    }
	
	private String  obterNomeTipoGeradoFluxo(String item) {
        
        final Long idTipoGeradoFluxo = Long.parseLong(item);
        
        TipoGeradorFluxoPDV tipoGeradorFluxoPDV = tipoGeradorFluxoPDVRepsitory.buscarPorId(idTipoGeradoFluxo);
        
        if(tipoGeradorFluxoPDV != null){
            return tipoGeradorFluxoPDV.getDescricao();
        }
        
        return null;
    }
	
	private String  obterDescricaoCotasNovaReativadas(String item) {
        
        final Integer idCotasNovasReativadas = Integer.parseInt(item);
        
        switch (idCotasNovasReativadas) {
        
        case 0:
            return "Não";
            
        case 1:
            return "Sim";
            
        default:
            return null;
        }
    }
	
	private String  obterDescricaoCotaAVista(String item) {
        
        return Util.getEnumByEnumName(TipoCota.values(), item).getDescTipoCota();
    }
	
	private List<BonificacaoDTO> obterBonificacoes(String jsonDadosVendaMedia){
		
		if (jsonDadosVendaMedia != null) {
	        
			ObjectMapper mapper = new ObjectMapper();
	        
			BonificacaoJsonDTO bonificacoes = null;
	        
	        try {
	        	
	        	bonificacoes = mapper.readValue(jsonDadosVendaMedia, BonificacaoJsonDTO.class);
	        	
	        	return bonificacoes.getBonificacoes();
	        	
	        } catch (IOException e) {
	            return null;
	        }
	    }
		 
		return null;
	}

	@Override
	@Transactional
	public InformacoesVendaEPerceDeVendaDTO buscarVendas(String codProduto, Long numEdicao) {
		return infoProdutosRepo.buscarVendas(codProduto, numEdicao);
	}
}
