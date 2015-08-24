package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AbastecimentoBoxCotaDTO;
import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.BoxRotasDTO;
import br.com.abril.nds.dto.BoxRoterioRotaDTO;
import br.com.abril.nds.dto.EntregadorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MapaCotaDTO;
import br.com.abril.nds.dto.MapaProdutoCotasDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaCotaDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaRotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.MapaAbastecimentoService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.ProdutoEdicaoVO;

@Service
public class MapaAbastecimentoServiceImpl implements MapaAbastecimentoService{

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Override
	@Transactional
	public List<AbastecimentoDTO> obterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro) {
		
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		return movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);
	}
	
	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterDadosAbastecimentoBoxVersusCota(FiltroMapaAbastecimentoDTO filtro) {
		
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		return movimentoEstoqueCotaRepository.obterDadosAbastecimentoBoxVersusCota(filtro);
	}
	
	@Override
	@Transactional
	public Long countObterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro) {
		return movimentoEstoqueCotaRepository.countObterDadosAbastecimento(filtro);
	}
	
	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterDetlhesDadosAbastecimento(Long idBox, FiltroMapaAbastecimentoDTO filtro) {
		return movimentoEstoqueCotaRepository.obterDetlhesDadosAbastecimento(idBox,filtro);
	}
	
	
	
	/**
	* Gera Mapa para Impressão por box
	*/
	@Transactional
	public Map<String, ProdutoMapaDTO> obterMapaDeImpressaoPorBox(
		FiltroMapaAbastecimentoDTO filtro) {
		
		Set<Integer> boxes = new LinkedHashSet<Integer>();
		
		Map<String, ProdutoMapaDTO> produtoMapa = new LinkedHashMap<String, ProdutoMapaDTO>();
		
		List<ProdutoAbastecimentoDTO> produtosPorBox = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);
		
		for(ProdutoAbastecimentoDTO produtoPorBox : produtosPorBox) {
		    boxes.add(produtoPorBox.getCodigoBox());
		}
		
		for(ProdutoAbastecimentoDTO produtoPorBox : produtosPorBox) {
			
			String keyProduto = produtoPorBox.getCodigoProduto() + " - " + produtoPorBox.getNumeroEdicao();
			
			if(!produtoMapa.containsKey(keyProduto)){ 
				
				produtoMapa.put(keyProduto, new ProdutoMapaDTO(
				produtoPorBox.getCodigoProduto(),
				produtoPorBox.getNomeProduto(),
				produtoPorBox.getNumeroEdicao(),				
				produtoPorBox.getCodigoBarra(),
				produtoPorBox.getPrecoCapa(),
				0,
				new TreeMap<Integer, Integer>()));
				
				for (Integer bx : boxes){
				    produtoMapa.get(keyProduto).getBoxQtde().put(bx, 0);
				}
			}

			Integer qtdeBox = produtoMapa.get(keyProduto).getBoxQtde().get(produtoPorBox.getCodigoBox());
			
			produtoMapa.get(keyProduto).getBoxQtde().put(produtoPorBox.getCodigoBox(), qtdeBox + produtoPorBox.getReparte());
			
			Integer novaQtdeTotal = produtoPorBox.getReparte() + produtoMapa.get(keyProduto).getTotalReparte();
			produtoMapa.get(keyProduto).setTotalReparte(novaQtdeTotal);
		}
		
		this.preencheBoxNaoUtilizado(boxes, produtoMapa);
		
		return produtoMapa;
	}
	
	/**
	* Adiciona Boxes não existentes aos HashMaps de box de ProdutoMapaDTO
	*
	* @param boxes
	* @param produtos
	*/
	private void preencheBoxNaoUtilizado(Set<Integer> boxes, Map<String, ProdutoMapaDTO> produtos) {
		for(Integer keyBox : boxes) {
	
			for(Entry<String, ProdutoMapaDTO> entry : produtos.entrySet()) {
	
				if(!entry.getValue().getBoxQtde().containsKey(keyBox)){
					entry.getValue().getBoxQtde().put(keyBox, 0);
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Map<Integer, Map<ProdutoEdicaoVO, Map<String, Integer>>> obterMapaDeImpressaoPorBoxQuebraPorCota(
	        FiltroMapaAbastecimentoDTO filtro) {
	    
	    List<ProdutoAbastecimentoDTO> produtosPorBox = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);
	    
	    Map<Integer, Map<ProdutoEdicaoVO, Map<String, Integer>>> ret = new LinkedHashMap<Integer, Map<ProdutoEdicaoVO, Map<String, Integer>>>();
	    
	    for (ProdutoAbastecimentoDTO dto : produtosPorBox){
            
            //box -> produto
            if (!ret.containsKey(dto.getCodigoBox())){
                
                Map<ProdutoEdicaoVO, Map<String,Integer>> produto =
                        new LinkedHashMap<ProdutoEdicaoVO, Map<String,Integer>>();
                
                ret.put(dto.getCodigoBox(), produto);
            }
            
            ProdutoEdicaoVO p = new ProdutoEdicaoVO(
                    dto.getIdProdutoEdicao(),
                    dto.getNomeProduto(),
                    dto.getCodigoBarra(),
                    dto.getNumeroEdicao(),
                    dto.getPrecoCapa());
            
            //produto -> cota
            if (!ret.get(dto.getCodigoBox()).containsKey(p)){
                
                Map<String, Integer> cota = new LinkedHashMap<String, Integer>();
                
                ret.get(dto.getCodigoBox()).put(p, cota);
            }
            
            //cota -> qtdProd
            if (!ret.get(dto.getCodigoBox()).get(p).containsKey(dto.getCodigoCota())){
                
                ret.get(dto.getCodigoBox()).get(p).put(dto.getCodigoCota().toString(), 0);
            }
            
            //adiciona qtd produto
            int qtd = dto.getReparte() + 
                    ret.get(dto.getCodigoBox()).get(p).get(dto.getCodigoCota().toString());
            
            ret.get(dto.getCodigoBox()).get(p).put(dto.getCodigoCota().toString(), qtd);
        }
	    
	    return ret;
	}
	
	@Transactional
	public Map<String, Map<String, ProdutoMapaRotaDTO>> obterMapaDeImpressaoPorBoxRota(
			FiltroMapaAbastecimentoDTO filtro) {
	
		Map<String, Map<String, ProdutoMapaRotaDTO>> boxes = new LinkedHashMap<String, Map<String, ProdutoMapaRotaDTO>> ();
	
		List<ProdutoAbastecimentoDTO> boxProdutoRota = this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoBoxRota(filtro);
	
		for(ProdutoAbastecimentoDTO item : boxProdutoRota ) {
	
			if(item.getCodigoBox() == null) {
				return null;
			}
	
			String keyBox = item.getCodigoBox() + " - " + item.getNomeBox();
	
			if(!boxes.containsKey(keyBox)){
				boxes.put(keyBox, new LinkedHashMap<String, ProdutoMapaRotaDTO>());
			}
	
			Map<String, ProdutoMapaRotaDTO> box = boxes.get(keyBox);
	
			if(!box.containsKey(item.getCodigoProduto())){
				
				box.put(item.getCodigoProduto(),
						new ProdutoMapaRotaDTO(item.getCodigoProduto(),
								item.getNomeProduto(),
								item.getNumeroEdicao(),
								item.getCodigoBarra(),
								item.getPrecoCapa(),
								0,
								new LinkedHashMap<String, Integer>()));
			}
	
			ProdutoMapaRotaDTO produto = box.get(item.getCodigoProduto());
	
	
			if(!produto.getRotasQtde().containsKey(item.getCodigoRota())){
				
				produto.getRotasQtde().put(item.getCodigoRota(),0);
			}
	
			Integer qtdeAtual = produto.getRotasQtde().get(item.getCodigoRota());
	
			produto.getRotasQtde().put(item.getCodigoRota(), qtdeAtual + item.getReparte());
	
			produto.setTotalReparte(produto.getTotalReparte() + item.getReparte());
		}
		
		
		int maiorQtdRotas = 0;
		for(Entry<String, Map<String, ProdutoMapaRotaDTO>> entry : boxes.entrySet()) {
			
			for (Entry<String, ProdutoMapaRotaDTO> ent : entry.getValue().entrySet()){
				
				int size = ent.getValue().getRotasQtde().size();
				
				if (size > maiorQtdRotas){
					maiorQtdRotas = size;
				}
			}
		}
	
		for(Entry<String, Map<String, ProdutoMapaRotaDTO>> entry : boxes.entrySet()) {
	
			preencheRotasNaoUtilizadas(maiorQtdRotas, entry.getValue());	
		}
	
		return boxes;
	}
	
	private void preencheRotasNaoUtilizadas(int qtd,
			Map<String, ProdutoMapaRotaDTO> produtos) {
	
		
		for(Entry<String, ProdutoMapaRotaDTO> entry : produtos.entrySet()) {	
			for (int index = entry.getValue().getRotasQtde().size() ; index < qtd ; index++){
				entry.getValue().getRotasQtde().put("|" + index, 0);
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Map<Integer, Map<String, Map<String, Map<ProdutoEdicaoVO, Map<String, Integer>>>>> obterMapaDeImpressaoPorBoxRotaQuebraCota(FiltroMapaAbastecimentoDTO filtro){
	    
	    Map<Integer, Map<String, Map<String, Map<ProdutoEdicaoVO, Map<String, Integer>>>>> ret = 
	    		new LinkedHashMap<Integer, Map<String, Map<String, Map<ProdutoEdicaoVO, Map<String, Integer>>>>>();
	    
	    List<ProdutoAbastecimentoDTO> boxProdutoRota = this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoBoxRota(filtro);
	    
	    for (ProdutoAbastecimentoDTO dto : boxProdutoRota){
	        
	        //box -> roteiro
	        if (!ret.containsKey(dto.getCodigoBox())){
	            
	            Map<String, Map<String, Map<ProdutoEdicaoVO, Map<String,Integer>>>> roteiro =
	                    new LinkedHashMap<String, Map<String, Map<ProdutoEdicaoVO, Map<String,Integer>>>>();
	            
	            ret.put(dto.getCodigoBox(), roteiro);
	        }
	        
	        //roteiro -> rota
	        if (!ret.get(dto.getCodigoBox()).containsKey(dto.getDescRoteiro())){
	            
	            Map<String, Map<ProdutoEdicaoVO, Map<String, Integer>>> rota =
                        new LinkedHashMap<String, Map<ProdutoEdicaoVO, Map<String, Integer>>>();
	            
	            ret.get(dto.getCodigoBox()).put(dto.getDescRoteiro(), rota);
	        }
	        
	        //rota -> produto
	        if (!ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).containsKey(dto.getCodigoRota())){
	            
	            Map<ProdutoEdicaoVO, Map<String, Integer>> produto =
	                    new LinkedHashMap<ProdutoEdicaoVO, Map<String, Integer>>();
	            
	            ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).put(dto.getCodigoRota(), produto);
	        }
	        
	        ProdutoEdicaoVO p = new ProdutoEdicaoVO(
	                dto.getIdProdutoEdicao(),
	                dto.getNomeProduto(),
	                dto.getCodigoBarra(),
	                dto.getNumeroEdicao(),
	                dto.getPrecoCapa());
	        
	        //produto -> cota
	        if (!ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).get(dto.getCodigoRota()).containsKey(p)){
	            
	            Map<String, Integer> cota = new LinkedHashMap<String, Integer>();
	            
	            ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).get(dto.getCodigoRota()).put(p, cota);
	        }
	        
	        //cota -> qtdProd
	        if (!ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).get(dto.getCodigoRota()).get(p).containsKey(dto.getCodigoCota())){
                
                ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).get(dto.getCodigoRota()).get(p).put(dto.getCodigoCota().toString(), 0);
            }
	        
	        //adiciona qtd produto
	        int qtd = dto.getReparte() + ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).get(dto.getCodigoRota()).get(p).get(dto.getCodigoCota().toString());
	        
	        ret.get(dto.getCodigoBox()).get(dto.getDescRoteiro()).get(dto.getCodigoRota()).get(p).put(dto.getCodigoCota().toString(), qtd);
	    }
	    
	    return ret;
	}
	
	@Override
	@Transactional
	public List<BoxRoterioRotaDTO> obterMapaDeImpressaoPorBoxRotaQuebraCota2(FiltroMapaAbastecimentoDTO filtro) {
				
		List<BoxRoterioRotaDTO> roteirizacao = new ArrayList<>();
		
		List<ProdutoAbastecimentoDTO> boxProdutoRota = this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoBoxRota(filtro);
		
		for (ProdutoAbastecimentoDTO dto : boxProdutoRota) {
			
			BoxRoterioRotaDTO rot = new BoxRoterioRotaDTO();
			BoxRoterioRotaDTO rotAux = new BoxRoterioRotaDTO();
			rotAux.setCodigoBox(dto.getCodigoBox());
			rotAux.setCodigoRota(dto.getCodigoRota());
			rotAux.setNomeRota(dto.getDescRota());
			rotAux.setRoteiro(dto.getDescRoteiro());
			
			if(!roteirizacao.contains(rotAux)) {
				rotAux.setProdutos(new ArrayList<ProdutoEdicaoAbastecimentoDTO>());
				roteirizacao.add(rotAux);
				int ndx = roteirizacao.indexOf(rotAux);
				rot = roteirizacao.get(ndx);
			} else {
				int ndx = roteirizacao.indexOf(rotAux);
				rot = roteirizacao.get(ndx);
			}
			
			ProdutoEdicaoAbastecimentoDTO produto = new ProdutoEdicaoAbastecimentoDTO();
			ProdutoEdicaoAbastecimentoDTO produtoAux = new ProdutoEdicaoAbastecimentoDTO();
			produtoAux.setCodigoProduto(dto.getCodigoProduto());
			produtoAux.setCodigoDeBarras(dto.getCodigoBarra());
			produtoAux.setNomeProduto(dto.getNomeProduto());
			produtoAux.setNumeroEdicao(dto.getNumeroEdicao());
			produtoAux.setPrecoCapa(dto.getPrecoCapa());
			
			if(!rot.getProdutos().contains(produtoAux)) {
				produtoAux.setQtdeExms(dto.getReparte());
				produtoAux.setItensCotas(new ArrayList<ItemDTO<Integer, Integer>>());
				rot.getProdutos().add(produtoAux);
				int ndx = rot.getProdutos().indexOf(produtoAux);
				produto = rot.getProdutos().get(ndx);
			} else {
				int ndx = rot.getProdutos().indexOf(produtoAux);
				produto = rot.getProdutos().get(ndx);
				produto.setQtdeExms(produto.getQtdeExms() + dto.getReparte());
			}
			
			ItemDTO<Integer, Integer> reparteCota = new ItemDTO<Integer, Integer>(dto.getCodigoCota(), dto.getReparte());
			produto.getItensCotas().add(reparteCota);
			
			
			/*
			 
			Map<String, List<ProdutoEdicaoAbastecimentoDTO>> mapProduto = new HashMap<String, List<ProdutoEdicaoAbastecimentoDTO>>();
		
			Map<String, Set<ItemDTO<Integer, Integer>>> mapaCotasProduto = new HashMap<String, Set<ItemDTO<Integer, Integer>>>();

			 
			if(mapaCotasProduto.get(dto.getCodigoProduto() +"-"+ dto.getNumeroEdicao()) == null){
				mapaCotasProduto.put(dto.getCodigoProduto() +"-"+ dto.getNumeroEdicao(), new HashSet<ItemDTO<Integer, Integer>>());
			}
			
			ItemDTO<Integer, Integer> reparteCota = new ItemDTO<Integer, Integer>(dto.getCodigoCota(), dto.getReparte());
			mapaCotasProduto.get(dto.getCodigoProduto() +"-"+ dto.getNumeroEdicao()).add(reparteCota);
			
			if(mapProduto.get(dto.getCodigoBox() +"-"+ dto.getCodigoRota()) == null){
				mapProduto.put(dto.getCodigoProduto() +"-"+ dto.getNumeroEdicao(), new ArrayList<ProdutoEdicaoAbastecimentoDTO>());
			}
			
			if(!mapProduto.get(dto.getCodigoBox() +"-"+ dto.getCodigoRota()).contains(produtoAux)) {
				produtoAux.setQtdeExms(dto.getReparte());
				mapProduto.get(dto.getCodigoBox() +"-"+ dto.getCodigoRota()).add(produtoAux);
			} else {
				int ndx = mapProduto.get(dto.getCodigoBox() +"-"+ dto.getCodigoRota()).indexOf(produtoAux);
				mapProduto.get(dto.getCodigoBox() +"-"+ dto.getCodigoRota()).get(ndx).setQtdeExms(
						mapProduto.get(dto.getCodigoBox() +"-"+ dto.getCodigoRota()).get(ndx).getQtdeExms() + dto.getReparte());
			}
			*/
			
	    }
		
		return roteirizacao;
	}
	
	@Override
	@Transactional
	public ProdutoEdicaoMapaDTO obterMapaDeImpressaoPorProdutoEdicao(
			FiltroMapaAbastecimentoDTO filtro) {
		
		List<ProdutoAbastecimentoDTO> produtosBoxRota = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoEdicao(filtro);
		
		ProdutoEdicaoMapaDTO peMapaDTO = new ProdutoEdicaoMapaDTO(
				produtosBoxRota.get(0).getCodigoProduto(),
				produtosBoxRota.get(0).getNomeProduto(),
				produtosBoxRota.get(0).getNumeroEdicao().longValue(),
				produtosBoxRota.get(0).getCodigoBarra(),
				produtosBoxRota.get(0).getPrecoCapa(),
				new LinkedHashMap<String, BoxRotasDTO>(),
				produtosBoxRota.get(0).getMaterialPromocional());
		
		if(produtosBoxRota.size() == 0) {	
			return null;
		}
		
		Set<String> rotas = new HashSet<String>();
		
		for(ProdutoAbastecimentoDTO item : produtosBoxRota) {
	
		    final String labelBox = item.getCodigoBox() + " - " + item.getNomeBox();
		    
			if(!peMapaDTO.getBoxes().containsKey(labelBox)){
				
			    peMapaDTO.getBoxes().put(labelBox, new BoxRotasDTO(0, new LinkedHashMap<String, Integer>()));
			}
	
			BoxRotasDTO box = peMapaDTO.getBoxes().get(labelBox);
			
			if(!box.getRotasQtde().containsKey(item.getCodigoRota())){
				
				box.getRotasQtde().put(item.getCodigoRota(), 0);
			}
	
			Integer qtdeRotaAtual = box.getRotasQtde().get(item.getCodigoRota());
			box.getRotasQtde().put(item.getCodigoRota(), qtdeRotaAtual + item.getReparte());
	
			Integer qtdeTotalAtual = box.getQtdeTotal();
			box.setQtdeTotal(qtdeTotalAtual + item.getReparte());
			
			rotas.add(item.getCodigoRota());
		}
		
		int maiorQtd = 0;
		for (Entry<String, BoxRotasDTO> entry : peMapaDTO.getBoxes().entrySet()){
			
			int qtd = entry.getValue().getRotasQtde().size();
			
			if (qtd > maiorQtd){
				maiorQtd = qtd;
			}
		}
		
		preencheRotasNaoUtilizadasPE(maiorQtd, peMapaDTO.getBoxes());	
	
		return peMapaDTO;
	}
	
	private void preencheRotasNaoUtilizadasPE(int qtd,
			HashMap<String, BoxRotasDTO> boxes) {
		
		for(Entry<String, BoxRotasDTO> entry : boxes.entrySet()) {	
			for (int index = entry.getValue().getRotasQtde().size() ; index < qtd ; index++){
				entry.getValue().getRotasQtde().put("|" + index, 0);
			}
		}
	}
	
	@Override
	@Transactional
	public MapaCotaDTO obterMapaDeImpressaoPorCota(FiltroMapaAbastecimentoDTO filtro) {
		
		List<ProdutoAbastecimentoDTO> produtosCota = this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);
		
		Map<Long,ProdutoMapaCotaDTO> produtoMapa = new LinkedHashMap<Long, ProdutoMapaCotaDTO>();
		
		MapaCotaDTO mapaCota = new MapaCotaDTO();
		
		mapaCota.setNumeroCota(filtro.getCodigoCota() != null ? filtro.getCodigoCota() : null);
	
		mapaCota.setNomeCota(filtro.getNomeCota() != null ? filtro.getNomeCota() : "");
	
		for (ProdutoAbastecimentoDTO item : produtosCota) {
	
			if (!produtoMapa.containsKey(item.getIdProdutoEdicao())) {
	
				ProdutoMapaCotaDTO produtoMapaCotaDTO =
						new ProdutoMapaCotaDTO(
								item.getNomeProduto(), item.getNumeroEdicao(), item.getCodigoBarra(), 
								item.getSequenciaMatriz(), item.getPrecoCapa(), 0, item.getMaterialPromocional());
	
	
				//colocar a lista de cotas
	
				produtoMapa.put(item.getIdProdutoEdicao(), produtoMapaCotaDTO);
	
				Integer qtdeAtual = produtoMapa.get(item.getIdProdutoEdicao()).getTotal();
	
				produtoMapa.get(item.getIdProdutoEdicao()).setTotal(qtdeAtual + item.getReparte());
				
			} else {
				
				ProdutoMapaCotaDTO produtoMapaCotaDTO = produtoMapa.get(item.getIdProdutoEdicao());
				if(produtoMapaCotaDTO != null) {
					
					//produtoMapaCotaDTO.setTotal(produtoMapaCotaDTO.getTotal() + item.getReparte());
					
				} else {
				
					produtoMapaCotaDTO = new ProdutoMapaCotaDTO(
								item.getNomeProduto(), item.getNumeroEdicao(), item.getCodigoBarra(), 
								item.getSequenciaMatriz(), item.getPrecoCapa(), 0, item.getMaterialPromocional());
				}
	
	
				//colocar a lista de cotas
	
				produtoMapa.put(item.getIdProdutoEdicao(), produtoMapaCotaDTO);
	
				Integer qtdeAtual = produtoMapa.get(item.getIdProdutoEdicao()).getTotal();
	
				produtoMapa.get(item.getIdProdutoEdicao()).setTotal(qtdeAtual + item.getReparte());
			}
		}
	
		Map<Long, ProdutoMapaCotaDTO> mapaProdutosOrdenados =
				new LinkedHashMap<Long, ProdutoMapaCotaDTO>();
	
		mapaProdutosOrdenados.putAll(produtoMapa);
	
		mapaCota.setProdutos(mapaProdutosOrdenados);
	
		return mapaCota;
	}
	
	@Override
	@Transactional
	public List<MapaProdutoCotasDTO> obterMapaDeImpressaoPorProdutoQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro) {
	
		List<ProdutoAbastecimentoDTO> produtosBoxCota = movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);
	
		if(produtosBoxCota.size() == 0)
			return null;
	
		
		Map<String, MapaProdutoCotasDTO> mapProdutosCotas = new HashMap<String, MapaProdutoCotasDTO>();
		for(ProdutoAbastecimentoDTO item : produtosBoxCota) {
		
			if(mapProdutosCotas.get(item.getCodigoProduto() +'-'+ item.getNumeroEdicao()) == null) {
			
				MapaProdutoCotasDTO pcMapaDTO = new MapaProdutoCotasDTO(
						item.getCodigoProduto(),
						item.getNomeProduto(),
						item.getNumeroEdicao().longValue(),
						item.getCodigoBarra(),
						item.getPrecoCapa(),
						new LinkedHashMap<Integer, Integer>(),
						new LinkedHashMap<String, Integer>());
				
				mapProdutosCotas.put(item.getCodigoProduto() +'-'+ item.getNumeroEdicao(), pcMapaDTO);
				
			}
			
		}
	
		for(Entry<String, MapaProdutoCotasDTO> entry : mapProdutosCotas.entrySet()) {
		
			for(ProdutoAbastecimentoDTO item : produtosBoxCota) {
				
				if(!entry.getValue().getCotasQtdes().containsKey(item.getCodigoCota())){
					entry.getValue().getCotasQtdes().put(item.getCodigoCota(), 0);
				}
		
				Integer qtdeAtual = entry.getValue().getCotasQtdes().get(item.getCodigoCota());
				
				if ((item.getCodigoProduto() +'-'+ item.getNumeroEdicao()).equals(entry.getValue().getCodigoProduto() +'-'+ item.getNumeroEdicao()) 
						&& item.getReparte() != null) {
					entry.getValue().getCotasQtdes().put(item.getCodigoCota(), qtdeAtual + item.getReparte());
				}
				
				this.montarMapaReparteBox(entry.getValue(), item);
			}
			
		}
		
		List<MapaProdutoCotasDTO> listProdutosCotas = new ArrayList<MapaProdutoCotasDTO>();
		listProdutosCotas.addAll(mapProdutosCotas.values());
	
		return listProdutosCotas;
	}

	private void montarMapaReparteBox(MapaProdutoCotasDTO pcMapaDTO, ProdutoAbastecimentoDTO item) {
		
		Integer codigoBox = item.getCodigoBox();
		String nomeBox = item.getNomeBox();
		
		if (codigoBox == null || nomeBox == null) {
		
			nomeBox = "Sem box definido";
			
		} else {
			
			nomeBox = codigoBox + " - " + nomeBox;
		}
		
		if((pcMapaDTO.getCodigoProduto() +'-'+ pcMapaDTO.getNumeroEdicao()).equals((item.getCodigoProduto() +'-'+ item.getNumeroEdicao()))) {
			
			Integer reparteBox = pcMapaDTO.getBoxQtdes().get(nomeBox);
			
			if (reparteBox == null) {
				reparteBox = 0;
			}
			
			if (item.getReparte() != null){
				reparteBox += item.getReparte();
			}
			
			pcMapaDTO.getBoxQtdes().put(nomeBox, reparteBox);
			
		}
		
	}
	
	@Override
	@Transactional
	public Map<EntregadorDTO, Map<Long, MapaProdutoCotasDTO>> obterMapaDeImpressaoPorEntregador(final FiltroMapaAbastecimentoDTO filtro) {
	
		final List<ProdutoAbastecimentoDTO> produtosBoxRota = movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorEntregador(filtro);
	
		if(produtosBoxRota.size() == 0){
			return null;
		}
		
		final Map<EntregadorDTO, Map<Long, MapaProdutoCotasDTO>> quebraPorEnt = 
		        new LinkedHashMap<EntregadorDTO, Map<Long, MapaProdutoCotasDTO>>();
		
		MapaProdutoCotasDTO pcMapaDTO = null;
	
		for(ProdutoAbastecimentoDTO item : produtosBoxRota) {
		    
		    final EntregadorDTO entDto = new EntregadorDTO();
		    entDto.setCodigoBox(item.getCodigoBox());
		    entDto.setDescricaoRota(item.getDescRota());
		    entDto.setDescricaoRoteiro(item.getDescRoteiro());
		    entDto.setIdEntregador(item.getIdEntregador());
		    entDto.setNomeEntregador(item.getNomeEntregador());
		    
		    if (!quebraPorEnt.containsKey(entDto)){
		        
		        quebraPorEnt.put(entDto, new LinkedHashMap<Long, MapaProdutoCotasDTO>());
		    }
		    
		    final Map<Long, MapaProdutoCotasDTO> mapas = quebraPorEnt.get(entDto);
		    
			if(!mapas.containsKey(item.getIdProdutoEdicao())) {
	
				pcMapaDTO = new MapaProdutoCotasDTO(
						item.getCodigoProduto(),
						item.getNomeProduto(),
						item.getNumeroEdicao().longValue(),
						item.getCodigoBarra(),
						item.getPrecoCapa(),
						new LinkedHashMap<Integer, Integer>(),
						new LinkedHashMap<String, Integer>());
	
				mapas.put(item.getIdProdutoEdicao(), pcMapaDTO);
			}	
	
			if(!pcMapaDTO.getCotasQtdes().containsKey(item.getCodigoCota())){
				pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), 0);
			}
	
			final Integer qtdeAtual = pcMapaDTO.getCotasQtdes().get(item.getCodigoCota());
			
			if (item.getReparte() != null){
				pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), qtdeAtual + item.getReparte());
			}
	
		}
	
		return quebraPorEnt;
	}
	
	@Override
	@Transactional
	public Map<EntregadorDTO, Map<Long, MapaProdutoCotasDTO>> obterMapaDeImpressaoPorEntregadorQuebrandoPorCota(final FiltroMapaAbastecimentoDTO filtro) {
	
		final List<ProdutoAbastecimentoDTO> produtosBoxRota = movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorEntregador(filtro);
	
		if(produtosBoxRota.size() == 0){
			return null;
		}
		
		final Map<EntregadorDTO, Map<Long, MapaProdutoCotasDTO>> quebraPorEnt = new LinkedHashMap<EntregadorDTO, Map<Long, MapaProdutoCotasDTO>>();
		
		MapaProdutoCotasDTO pcMapaDTO = null;
	
		Integer valorAux = 0;
		
		for(ProdutoAbastecimentoDTO item : produtosBoxRota) {
		    
		    final EntregadorDTO entDto = new EntregadorDTO();
		    entDto.setCodigoBox(item.getCodigoBox());
		    entDto.setDescricaoRota(item.getDescRota());
		    entDto.setDescricaoRoteiro(item.getDescRoteiro());
		    entDto.setIdEntregador(item.getIdEntregador());
		    entDto.setNomeEntregador(item.getNomeEntregador());
		    
		    if (!quebraPorEnt.containsKey(entDto)){
		        
		        quebraPorEnt.put(entDto, new LinkedHashMap<Long, MapaProdutoCotasDTO>());
		    }
		    
		    final Map<Long, MapaProdutoCotasDTO> mapas = quebraPorEnt.get(entDto);
		    
			if(!mapas.containsKey(item.getIdProdutoEdicao())) {
	
				pcMapaDTO = new MapaProdutoCotasDTO(
						item.getCodigoProduto(),
						item.getNomeProduto(),
						item.getNumeroEdicao().longValue(),
						item.getCodigoBarra(),
						item.getPrecoCapa(),
						new LinkedHashMap<Integer, Integer>(),
						new LinkedHashMap<String, Integer>());
	
				mapas.put(item.getIdProdutoEdicao(), pcMapaDTO);
			}	
	
			if(!pcMapaDTO.getCotasQtdes().containsKey(item.getCodigoCota())){
				pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), 0);
			}
	
			final Integer qtdeAtual = pcMapaDTO.getCotasQtdes().get(item.getCodigoCota());
			
			if (item.getReparte() != null){
				pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), qtdeAtual + item.getReparte());
				
				valorAux = valorAux +  item.getReparte();
				
				pcMapaDTO.setQtdes(valorAux);
			}
		}
	
		return quebraPorEnt;
	}
	
	
	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBoxRota(FiltroMapaAbastecimentoDTO filtro) {
	
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		return this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBoxRota(filtro);
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional
	public Long countObterMapaAbastecimentoPorBoxRota(FiltroMapaAbastecimentoDTO filtro) {
	
		return this.movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorBoxRota(filtro);
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorCota(FiltroMapaAbastecimentoDTO filtro) {
	
		Intervalo<Integer> intervaloCotas = null;
		if (filtro.getCodigoCota() != null){
			intervaloCotas = new Intervalo<Integer>(filtro.getCodigoCota(), filtro.getCodigoCota());
		}
		
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(intervaloCotas, intervaloDataLancamento, null);
		

		//TODO: testar lista retorno (uma foi excluida)
		
		List<ProdutoAbastecimentoDTO> mapaCota = this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);
		
		return mapaCota;
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional
	public Long countObterMapaAbastecimentoPorCota(FiltroMapaAbastecimentoDTO filtro) {
	
		return this.movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorCota(filtro);
	}

	/**
	* {@inheritDoc}
	*/

	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorProdutoEdicao(FiltroMapaAbastecimentoDTO filtro) {
	
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		return this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoEdicao(filtro);
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional
	public Long countObterMapaAbastecimentoPorProdutoEdicao(FiltroMapaAbastecimentoDTO filtro) {
	
		return this.movimentoEstoqueCotaRepository.countObterMapaAbastecimentoPorProdutoEdicao(filtro);
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro) {
	
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		return this.movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional
	public Long countObterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro) {
	
		return this.movimentoEstoqueCotaRepository.countObterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);
	}
	
	@Override
	@Transactional
	public List<ProdutoAbastecimentoDTO> obterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro) {
	
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		return this.movimentoEstoqueCotaRepository.obterMapaDeAbastecimentoPorEntregador(filtro);
	}
	
	@Override
	@Transactional
	public Long countObterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro) {
		return movimentoEstoqueCotaRepository.countObterMapaDeAbastecimentoPorEntregador(filtro);
	}
	
	@Override
	@Transactional
	public List<AbastecimentoBoxCotaDTO> obterMapaDeImpressaoPorBoxVersusCotaQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro, final Map<String, Object> parameters) {

		List<AbastecimentoBoxCotaDTO> produtosBoxCota = movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorBoxVersusCotaQuebrandoPorCota(filtro);

		return produtosBoxCota;
	}
}