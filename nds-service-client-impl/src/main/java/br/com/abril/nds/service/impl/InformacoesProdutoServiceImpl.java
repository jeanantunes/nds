package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.dto.BonificacaoJsonDTO;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.EstudoProdutoEdicaoBaseRepository;
import br.com.abril.nds.repository.InformacoesProdutoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.ProdutoBaseSugeridaRepository;
import br.com.abril.nds.repository.RegiaoRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.ComponentesPDV;
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
	@Transactional(readOnly=true)
	public List<InfoProdutosItemRegiaoEspecificaDTO> buscarItemRegiao(Long idEstudo) {
		
		EstudoGerado estudoGerado = estudoGeradoRepository.buscarPorId(idEstudo);
		
		List<InfoProdutosItemRegiaoEspecificaDTO> itensRetorno = new ArrayList<>();
		
		if(estudoGerado!= null){
			
			List<BonificacaoDTO> bonificacoes = this.obterBonificacoes(estudoGerado.getDadosVendaMedia());
			
			if(bonificacoes!= null && !bonificacoes.isEmpty()){
				
				for(BonificacaoDTO item : bonificacoes){
					
					if(ComponentesPDV.REGIAO.equals(item.getComponente())){
						
						InfoProdutosItemRegiaoEspecificaDTO retorno = new InfoProdutosItemRegiaoEspecificaDTO();
						
						retorno.setBonificacao(new BigDecimal(Util.nvl(item.getBonificacao(),0D)));
						retorno.setQtdReparteMin(Util.nvl(item.getReparteMinimo(),0D).intValue());
						retorno.setNomeItemRegiao(this.obterNomeDaRegiao(item.getElemento()));
						
						itensRetorno.add(retorno);
					}
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
