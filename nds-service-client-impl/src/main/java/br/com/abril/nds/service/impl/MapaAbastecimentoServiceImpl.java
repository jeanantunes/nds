package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.BoxRotasDTO;
import br.com.abril.nds.dto.MapaCotaDTO;
import br.com.abril.nds.dto.MapaProdutoCotasDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaCotaDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaRotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaJuramentadoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.MapaAbastecimentoService;
import br.com.abril.nds.util.Intervalo;

@Service
public class MapaAbastecimentoServiceImpl implements MapaAbastecimentoService{

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired EstoqueProdutoCotaJuramentadoRepository cotaJuramentadoRepository;
	
	@Autowired
	private CotaService cotaService;
	
	@Override
	@Transactional
	public List<AbastecimentoDTO> obterDadosAbastecimento(
		FiltroMapaAbastecimentoDTO filtro) {
		
		Intervalo<Date> intervaloDataLancamento = new Intervalo<Date>(filtro.getDataDate(), filtro.getDataDate());
		
		this.cotaService.verificarCotasSemRoteirizacao(null, intervaloDataLancamento, null);
		
		return movimentoEstoqueCotaRepository.obterDadosAbastecimento(filtro);
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
	public TreeMap<String, ProdutoMapaDTO> obterMapaDeImpressaoPorBox(
		FiltroMapaAbastecimentoDTO filtro) {
		
		List<Integer> boxes = new ArrayList<Integer>();
		
		
		CompararProdutoMapaDTO comparator = new CompararProdutoMapaDTO();
		TreeMap<String,ProdutoMapaDTO> produtoMapa = new TreeMap<String, ProdutoMapaDTO>();	
		comparator.setProdutoMapa(produtoMapa);
		
		List<ProdutoAbastecimentoDTO> produtosPorBox = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);
		
		//variavel para verificacao se existem cotas sem roteirizaca
	
		for(ProdutoAbastecimentoDTO produtoPorBox : produtosPorBox ) {
			if(produtoPorBox.getCodigoBox() == null) {
				if(!produtoMapa.containsKey(produtoPorBox.getCodigoCota().toString() +" - "+ produtoPorBox.getNomeCota())) {
					produtoMapa.put(produtoPorBox.getCodigoCota().toString() +" - "+ produtoPorBox.getNomeCota(), null);
				}
			}
		}
	
		if(produtoMapa.isEmpty()) {
			for(ProdutoAbastecimentoDTO produtoPorBox : produtosPorBox) {
				String keyProduto = produtoPorBox.getCodigoProduto();
	
				if(!boxes.contains(produtoPorBox.getCodigoBox()))
					boxes.add(produtoPorBox.getCodigoBox());
	
				if(!produtoMapa.containsKey(keyProduto))
					produtoMapa.put(keyProduto, new ProdutoMapaDTO(
					produtoPorBox.getCodigoProduto(),
					produtoPorBox.getNomeProduto(),
					produtoPorBox.getNumeroEdicao(),
					produtoPorBox.getPrecoCapa(),
					0,
					new HashMap<Integer, Integer>()));
	
				if(!produtoMapa.get(keyProduto).getBoxQtde().containsKey(produtoPorBox.getCodigoBox()))
					produtoMapa.get(keyProduto).getBoxQtde().put(produtoPorBox.getCodigoBox(),0);
					
					Integer qtdeBox = produtoMapa.get(keyProduto).getBoxQtde().get(produtoPorBox.getCodigoBox());
					
					produtoMapa.get(keyProduto).getBoxQtde().put(produtoPorBox.getCodigoBox(), qtdeBox + produtoPorBox.getReparte());
					
					Integer novaQtdeTotal = produtoPorBox.getReparte() + produtoMapa.get(keyProduto).getTotalReparte();
					produtoMapa.get(keyProduto).setTotalReparte(novaQtdeTotal);
			}
			
			preencheBoxNaoUtilizado(boxes, produtoMapa);	
			
			TreeMap<String,ProdutoMapaDTO> produtoMapaOrdenada = new TreeMap<String, ProdutoMapaDTO>(comparator);	
			
			produtoMapaOrdenada.putAll(produtoMapa);
			
			return produtoMapaOrdenada;

		} else {
	
			return produtoMapa;
		}
	}
	
	/**
	* Adiciona Boxes não existentes aos HashMaps de box de ProdutoMapaDTO
	*
	* @param boxes
	* @param produtos
	*/
	private void preencheBoxNaoUtilizado(List<Integer> boxes, TreeMap<String, ProdutoMapaDTO> produtos) {
		for(Integer keyBox : boxes) {
	
			for(String keyProduto : produtos.keySet()) {
	
				if(!produtos.get(keyProduto).getBoxQtde().containsKey(keyBox))
					produtos.get(keyProduto).getBoxQtde().put(keyBox, 0);
				}
			}
		}
	
	@Transactional
	public HashMap<Integer, HashMap<String, ProdutoMapaRotaDTO>> obterMapaDeImpressaoPorBoxRota(
			FiltroMapaAbastecimentoDTO filtro) {
	
		List<String> rotas = new ArrayList<String>();
	
		HashMap<Integer, HashMap<String, ProdutoMapaRotaDTO>> boxes = new HashMap<Integer, HashMap<String, ProdutoMapaRotaDTO>> ();
	
		List<ProdutoAbastecimentoDTO> boxProdutoRota = this.obterMapaAbastecimentoPorBoxRota(filtro);
	
		for(ProdutoAbastecimentoDTO item : boxProdutoRota ) {
	
			if(item.getCodigoBox() == null) {
				return null;
			}
	
			Integer keyBox = item.getCodigoBox();
	
			if(!boxes.containsKey(keyBox))
				boxes.put(keyBox, new HashMap<String, ProdutoMapaRotaDTO>());
	
			HashMap<String, ProdutoMapaRotaDTO> box = boxes.get(keyBox);
	
			if(!box.containsKey(item.getCodigoProduto()))
				box.put(item.getCodigoProduto(),
						new ProdutoMapaRotaDTO(item.getCodigoProduto(),
								item.getNomeProduto(),
								item.getNumeroEdicao(),
								item.getPrecoCapa(),
								0,
								new HashMap<String, Integer>()));
	
			ProdutoMapaRotaDTO produto = box.get(item.getCodigoProduto());
	
	
			if(!produto.getRotasQtde().containsKey(item.getCodigoRota()))
				produto.getRotasQtde().put(item.getCodigoRota(),0);
	
			Integer qtdeAtual = produto.getRotasQtde().get(item.getCodigoRota());
	
			produto.getRotasQtde().put(item.getCodigoRota(), qtdeAtual + item.getReparte());
	
			produto.setTotalReparte(produto.getTotalReparte() + item.getReparte());
	
			if(!rotas.contains(item.getCodigoRota()))
				rotas.add(item.getCodigoRota());
	
	
		}
	
		for(Integer keyBox : boxes.keySet()) {
	
			preencheRotasNaoUtilizadas(rotas, boxes.get(keyBox));	
		}
	
		return boxes;
	}
	
	private void preencheRotasNaoUtilizadas(List<String> rotas,
			HashMap<String, ProdutoMapaRotaDTO> produtos) {
	
		for(String keyRota : rotas) {
	
			for(String keyProduto : produtos.keySet()) {	
	
				if(!produtos.get(keyProduto).getRotasQtde().containsKey(keyRota))
					produtos.get(keyProduto).getRotasQtde().put(keyRota, 0);
			}
		}
	
	}
	
	@Override
	@Transactional
	public ProdutoEdicaoMapaDTO obterMapaDeImpressaoPorProdutoEdicao(
			FiltroMapaAbastecimentoDTO filtro) {
	
	
		List<String> rotas = new ArrayList<String>();
	
		List<ProdutoAbastecimentoDTO> produtosBoxRota = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoEdicao(filtro);
		
		ProdutoEdicaoMapaDTO peMapaDTO = new ProdutoEdicaoMapaDTO(
				produtosBoxRota.get(0).getCodigoProduto(),
				produtosBoxRota.get(0).getNomeProduto(),
				produtosBoxRota.get(0).getNumeroEdicao().longValue(),
				produtosBoxRota.get(0).getPrecoCapa(),
				new HashMap<Integer, BoxRotasDTO>());
		
		
		List<ProdutoAbastecimentoDTO> cotas = this.cotaRepository.obterCotaPorProdutoEdicaoData(filtro);
		
		if(cotas != null && !cotas.isEmpty()) {
			peMapaDTO.setCotasSemRoteirizacao(new ArrayList<String>());
		}
		
		for(ProdutoAbastecimentoDTO cota : cotas) {
			String cotaString = cota.getCota().getNumeroCota() + " - " + cota.getCota().getPessoa().getNome();
			
			if(cota.getCota().getBox() == null) {
				if(!peMapaDTO.getCotasSemRoteirizacao().contains(cotaString)) {
					peMapaDTO.getCotasSemRoteirizacao().add(cotaString);
				}
				
			}
		}
		
		
		
		if(produtosBoxRota.size() == 0) {	
			return null;
		}
	
			
		for(ProdutoAbastecimentoDTO item : produtosBoxRota) {
	
			if(!peMapaDTO.getBoxes().containsKey(item.getCodigoBox()))
				peMapaDTO.getBoxes().put(item.getCodigoBox(), new BoxRotasDTO(0, new HashMap<String, Integer>()));
	
			BoxRotasDTO box = peMapaDTO.getBoxes().get(item.getCodigoBox());
	
//			box.setCotas(new ArrayList<Cota>());
//			box.getCotas().add(item.getCota());
	
	
			if(!rotas.contains(item.getCodigoRota()))
				rotas.add(item.getCodigoRota());	
	
			if(!box.getRotasQtde().containsKey(item.getCodigoRota()))
				box.getRotasQtde().put(item.getCodigoRota(), 0);
	
			Integer qtdeRotaAtual = box.getRotasQtde().get(item.getCodigoRota());
			box.getRotasQtde().put(item.getCodigoRota(), qtdeRotaAtual + item.getReparte());
	
			Integer qtdeTotalAtual = box.getQtdeTotal();
			box.setQtdeTotal(qtdeTotalAtual + item.getReparte());
		}
	
		preencheRotasNaoUtilizadasPE(rotas, peMapaDTO.getBoxes());	
	
		return peMapaDTO;
	}
	
	private void preencheRotasNaoUtilizadasPE(List<String> rotas,
			HashMap<Integer, BoxRotasDTO> boxes) {
	
		for(String keyRota : rotas) {
	
			for(Integer keyBox : boxes.keySet()) {	
	
				if(!boxes.get(keyBox).getRotasQtde().containsKey(keyRota))
					boxes.get(keyBox).getRotasQtde().put(keyRota, 0);
			}
		}
	
	}
	
	@Override
	@Transactional
	public MapaCotaDTO obterMapaDeImpressaoPorCota(FiltroMapaAbastecimentoDTO filtro) {
	
		List<ProdutoAbastecimentoDTO> produtosCota =
			this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);
	
		CompararProdutoMapaCotaDTO comparator = new CompararProdutoMapaCotaDTO();
	
		TreeMap<Long,ProdutoMapaCotaDTO> produtoMapa = new TreeMap<Long, ProdutoMapaCotaDTO>();
	
		comparator.setProdutoMapa(produtoMapa);
		
		MapaCotaDTO mapaCota = new MapaCotaDTO();
	
		mapaCota.setNumeroCota(filtro.getCodigoCota() != null ? filtro.getCodigoCota() : null);
	
		mapaCota.setNomeCota(filtro.getNomeCota() != null ? filtro.getNomeCota() : "");
	
		for (ProdutoAbastecimentoDTO item : produtosCota) {
	
			if (!produtoMapa.containsKey(item.getIdProdutoEdicao())) {
	
				BigInteger somaEstoqueJuramentado =
						this.cotaJuramentadoRepository.buscarSomaEstoqueJuramentadoPorProdutoData(
								item.getIdProdutoEdicao(), filtro.getDataDate());
	
				if (somaEstoqueJuramentado != null) {
	
					BigInteger newReparte = BigInteger.valueOf(item.getReparte());
	
					item.setReparte(newReparte.subtract(somaEstoqueJuramentado));
				}
	
				ProdutoMapaCotaDTO produtoMapaCotaDTO =
						new ProdutoMapaCotaDTO(
								item.getNomeProduto(), item.getNumeroEdicao(), item.getSequenciaMatriz(), 0);
	
	
				//colocar a lista de cotas
	
				produtoMapa.put(item.getIdProdutoEdicao(), produtoMapaCotaDTO);
	
				Integer qtdeAtual = produtoMapa.get(item.getIdProdutoEdicao()).getTotal();
	
				produtoMapa.get(item.getIdProdutoEdicao()).setTotal(qtdeAtual + item.getReparte());
			}
		}
	
		TreeMap<Long, ProdutoMapaCotaDTO> mapaProdutosOrdenados =
				new TreeMap<Long, ProdutoMapaCotaDTO>(comparator);
	
		mapaProdutosOrdenados.putAll(produtoMapa);
	
		mapaCota.setProdutos(mapaProdutosOrdenados);
	
		return mapaCota;
	}
	
	@Override
	@Transactional
	public MapaProdutoCotasDTO obterMapaDeImpressaoPorProdutoQuebrandoPorCota(
			FiltroMapaAbastecimentoDTO filtro) {
	
		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		filtro.getPaginacao().setPaginaAtual(null);
	
		List<ProdutoAbastecimentoDTO> produtosBoxRota = movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);
	
		if(produtosBoxRota.size() == 0)
			return null;
	
		MapaProdutoCotasDTO pcMapaDTO = new MapaProdutoCotasDTO(
				produtosBoxRota.get(0).getCodigoProduto(),
				produtosBoxRota.get(0).getNomeProduto(),
				produtosBoxRota.get(0).getNumeroEdicao().longValue(),
				produtosBoxRota.get(0).getPrecoCapa(),
				new TreeMap<Integer, Integer>());
	
		for(ProdutoAbastecimentoDTO item : produtosBoxRota) {
	
			if(!pcMapaDTO.getCotasQtdes().containsKey(item.getCodigoCota()))
				pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), 0);
	
			Integer qtdeAtual = pcMapaDTO.getCotasQtdes().get(item.getCodigoCota());
			pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), qtdeAtual + item.getReparte());
	
		}
	
		return pcMapaDTO;
	}
	
	@Override
	@Transactional
	public HashMap<Long, MapaProdutoCotasDTO> obterMapaDeImpressaoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro) {
	
		List<ProdutoAbastecimentoDTO> produtosBoxRota = movimentoEstoqueCotaRepository.obterMapaDeImpressaoPorEntregador(filtro);
	
		if(produtosBoxRota.size() == 0)
			return null;
	
		HashMap<Long, MapaProdutoCotasDTO> mapas = new HashMap<Long, MapaProdutoCotasDTO>();
	
		MapaProdutoCotasDTO pcMapaDTO = null;
	
		for(ProdutoAbastecimentoDTO item : produtosBoxRota) {
	
			if(!mapas.containsKey(item.getIdProdutoEdicao())) {
	
				pcMapaDTO = new MapaProdutoCotasDTO(
						item.getCodigoProduto(),
						item.getNomeProduto(),
						item.getNumeroEdicao().longValue(),
						item.getPrecoCapa(),
						new HashMap<Integer, Integer>());
	
				mapas.put(item.getIdProdutoEdicao(), pcMapaDTO);
			}	
	
			if(!pcMapaDTO.getCotasQtdes().containsKey(item.getCodigoCota()))
				pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), 0);
	
			Integer qtdeAtual = pcMapaDTO.getCotasQtdes().get(item.getCodigoCota());
			pcMapaDTO.getCotasQtdes().put(item.getCodigoCota(), qtdeAtual + item.getReparte());
	
		}
	
		return mapas;
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
		
	  List<ProdutoAbastecimentoDTO> mapaRetorno = new ArrayList<ProdutoAbastecimentoDTO>();
	
		List<ProdutoAbastecimentoDTO> mapaCota =
		this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);
		
		for (ProdutoAbastecimentoDTO produto : mapaCota) {
		
			BigInteger somaEstoqueJuramentado =
					this.cotaJuramentadoRepository.buscarSomaEstoqueJuramentadoPorProdutoData(
							produto.getIdProdutoEdicao(), filtro.getDataDate());
		
			if (somaEstoqueJuramentado != null) {
				
				BigInteger newReparte = BigInteger.valueOf(produto.getReparte().longValue());
		
				produto.setReparte(newReparte.subtract(somaEstoqueJuramentado));
			}
		
			mapaRetorno.add(produto);
		}
		
		return mapaRetorno;
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



    private static class CompararProdutoMapaDTO implements Comparator<String> {
        
     TreeMap<String,ProdutoMapaDTO> produtoMapa;

	     @Override
	     public int compare(String o1, String o2) {
	    	 ProdutoMapaDTO p1 = produtoMapa.get(o1);
	    	 ProdutoMapaDTO p2 = produtoMapa.get(o2);
		
		
	    	 int result = p1.getNomeProduto().compareTo(p2.getNomeProduto());
	    	 if (result == 0)
	    		 result = p1.getNumeroEdicao().compareTo(p2.getNumeroEdicao());
	    	 return result;
	     }
	
		/**
		* @param produtoMapa the produtoMapa to set
		*/
		public void setProdutoMapa(TreeMap<String, ProdutoMapaDTO> produtoMapa) {
			this.produtoMapa = produtoMapa;
			}
    }

    private static class CompararProdutoMapaCotaDTO implements Comparator<Long> {
    
     TreeMap<Long, ProdutoMapaCotaDTO> produtoMapa;
    
     @Override
     public int compare(Long o1, Long o2) {
     ProdutoMapaCotaDTO p1 = produtoMapa.get(o1);
     ProdutoMapaCotaDTO p2 = produtoMapa.get(o2);
    
    
     int result = p1.getNomeProduto().compareTo(p2.getNomeProduto());
     if (result == 0)
    	 result = p1.getNumeroEdicao().compareTo(p2.getNumeroEdicao());
     	return result;
     }
    
     /**
      * @param produtoMapa the produtoMapa to set
	*/
     public void setProdutoMapa(TreeMap<Long, ProdutoMapaCotaDTO> produtoMapa) {
    	 this.produtoMapa = produtoMapa;
     }
    }
}
