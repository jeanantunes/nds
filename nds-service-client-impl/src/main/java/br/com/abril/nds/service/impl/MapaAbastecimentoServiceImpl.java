package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.MapaAbastecimentoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class MapaAbastecimentoServiceImpl implements MapaAbastecimentoService{

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Override
	@Transactional
	public List<AbastecimentoDTO> obterDadosAbastecimento(
			FiltroMapaAbastecimentoDTO filtro) {
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
		
		List<Integer> boxes =  new ArrayList<Integer>();
		
		
		CompararProdutoMapaDTO comparator = new CompararProdutoMapaDTO();
		TreeMap<String,ProdutoMapaDTO> produtoMapa = new TreeMap<String, ProdutoMapaDTO>();		
		comparator.setProdutoMapa(produtoMapa);
		
		List<ProdutoAbastecimentoDTO> produtosPorBox = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorBox(filtro);
		
		for(ProdutoAbastecimentoDTO produtoPorBox : produtosPorBox ) {
			
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
			
				Integer novaQtdeTotal =  produtoPorBox.getReparte() + produtoMapa.get(keyProduto).getTotalReparte();
				produtoMapa.get(keyProduto).setTotalReparte(novaQtdeTotal);
			
		}
		
		preencheBoxNaoUtilizado(boxes, produtoMapa);	
		
		TreeMap<String,ProdutoMapaDTO> produtoMapaOrdenada = new TreeMap<String, ProdutoMapaDTO>(comparator);	
		
		produtoMapaOrdenada.putAll(produtoMapa);
		
		return produtoMapaOrdenada;
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
		
		List<String> rotas =  new ArrayList<String>();
		
		HashMap<Integer, HashMap<String, ProdutoMapaRotaDTO>>  boxes = new HashMap<Integer, HashMap<String, ProdutoMapaRotaDTO>> ();
		
		List<ProdutoAbastecimentoDTO> boxProdutoRota = this.obterMapaAbastecimentoPorBoxRota(filtro);
		
		for(ProdutoAbastecimentoDTO item : boxProdutoRota ) {
			
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
			
			ProdutoMapaRotaDTO produto =  box.get(item.getCodigoProduto());
			
			
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


		List<String> rotas =  new ArrayList<String>();
		
		List<ProdutoAbastecimentoDTO> produtosBoxRota = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorProdutoEdicao(filtro);
		
		if(produtosBoxRota.size() == 0)
			return null;
		
		ProdutoEdicaoMapaDTO peMapaDTO = new ProdutoEdicaoMapaDTO(
				produtosBoxRota.get(0).getCodigoProduto(), 
				produtosBoxRota.get(0).getNomeProduto(), 
				produtosBoxRota.get(0).getNumeroEdicao().longValue(), 
				produtosBoxRota.get(0).getPrecoCapa(),  
				new HashMap<Integer, BoxRotasDTO>());
		
		for(ProdutoAbastecimentoDTO item : produtosBoxRota) {
			
			if(!peMapaDTO.getBoxes().containsKey(item.getCodigoBox()))
				peMapaDTO.getBoxes().put(item.getCodigoBox(), new BoxRotasDTO(0, new HashMap<String, Integer>()));
			
			BoxRotasDTO box = peMapaDTO.getBoxes().get(item.getCodigoBox());
			
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
		
		List<ProdutoAbastecimentoDTO> produtosCota = movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);
		
		Cota cota = cotaRepository.obterPorNumerDaCota(filtro.getCodigoCota());
		
		
		MapaCotaDTO mapaCota = new MapaCotaDTO(
					cota==null?null:cota.getNumeroCota(),
					cota==null?null:cota.getPessoa().getNome(),
					new HashMap<Long, ProdutoMapaCotaDTO>() );
		
		for(ProdutoAbastecimentoDTO item : produtosCota) {
			
			if(!mapaCota.getProdutos().containsKey(item.getIdProdutoEdicao()))
				mapaCota.getProdutos().put(
						item.getIdProdutoEdicao(), 
						new ProdutoMapaCotaDTO(
								item.getNomeProduto(), 
								item.getNumeroEdicao(), 
								item.getSequenciaMatriz(), 
								0));
			
			Integer qtdeAtual = mapaCota.getProdutos().get(item.getIdProdutoEdicao()).getTotal();
			mapaCota.getProdutos().get(item.getIdProdutoEdicao()).setTotal(qtdeAtual + item.getReparte());
			
		}
		
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
				new HashMap<Integer, Integer>());
		
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
	public HashMap<Long, MapaProdutoCotasDTO>  obterMapaDeImpressaoPorEntregador(
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

		return this.movimentoEstoqueCotaRepository.obterMapaAbastecimentoPorCota(filtro);
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
}
