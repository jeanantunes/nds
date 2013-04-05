package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.MixCotaProdutoRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RepartePDVRepository;
import br.com.abril.nds.service.RepartePdvService;

@Service
public class RepartePdvServiceImpl implements RepartePdvService{
	@Autowired
	RepartePDVRepository repartePDVRepository;
	
	@Autowired
	FixacaoReparteRepository fixacaoReparteRepository;
	
	@Autowired
	MixCotaProdutoRepository mixCotaProdutoRepository;
	
	@Autowired
	CotaRepository  cotaRepository;
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	PdvRepository pdvRepository;

	@Transactional
	@Override
	public RepartePDV obterRepartePorPdv(Long idFixacao, Long idProduto, Long idPdv) {
		return repartePDVRepository.obterRepartePorPdv(idFixacao, idProduto, idPdv);
	}
	
	@Override
	@Transactional
	public RepartePDV obterRepartePorPdvMix(Long idMix, Long idProduto,
			Long idPdv) {
		return repartePDVRepository.obterRepartePdvMix(idMix, idProduto, idPdv);
	}

	

	@Override
	@Transactional
	public void salvarRepartesPDVMix(List<RepartePDVDTO> listaRepartes, String codProduto, String codCota, Long idMix) {
		int soma = 0;
		Cota cota=  cotaRepository.obterPorNumerDaCota(new Integer(codCota));
		Produto produto= produtoRepository.obterProdutoPorCodigo(codProduto);
		MixCotaProduto mixCotaProduto = mixCotaProdutoRepository.buscarPorId(idMix);
		PDV pdv = null;
		
		for (RepartePDVDTO repartePDVDTO : listaRepartes) {
			if(repartePDVDTO.getCodigoPdv() !=null){
				pdv= pdvRepository.buscarPorId(repartePDVDTO.getCodigoPdv());
			}
			RepartePDV repartePDV =  repartePDVRepository.obterRepartePdvMix(idMix, produto.getId(), pdv.getId());
			if(repartePDV == null){
				repartePDV = new RepartePDV();
			}	
			repartePDV.setMixCotaProduto(mixCotaProduto);
			repartePDV.setPdv(pdv);
			repartePDV.setReparte(repartePDVDTO.getReparte().intValue());
			repartePDV.setProduto(produto);
			
			soma += repartePDV.getReparte();
			repartePDVRepository.merge(repartePDV);
		}
		mixCotaProduto.setReparteMaximo(new Long(soma));
		mixCotaProdutoRepository.alterar(mixCotaProduto);
		
	}

	@Override
	@Transactional
	public void salvarRepartesPDV(List<RepartePDVDTO> listaRepartes, String codProduto, String codCota, Long idFixacao, boolean manterFixa) {
		int soma = 0;
		Cota cota=  cotaRepository.obterPorNumerDaCota(new Integer(codCota));
		Produto produto= produtoRepository.obterProdutoPorCodigo(codProduto);
		FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorId(idFixacao);
		fixacaoReparte.setManterFixa(manterFixa);
		PDV pdv = null;
		
		for (RepartePDVDTO repartePDVDTO : listaRepartes) {
			if(repartePDVDTO.getCodigoPdv() !=null){
				pdv= pdvRepository.buscarPorId(repartePDVDTO.getCodigoPdv());
			}
			RepartePDV repartePDV =  repartePDVRepository.obterRepartePorPdv(idFixacao, produto.getId(), pdv.getId());
			if(repartePDV == null){
				repartePDV = new RepartePDV();
			}	
			repartePDV.setFixacaoReparte(fixacaoReparte);
			repartePDV.setPdv(pdv);
			repartePDV.setReparte(repartePDVDTO.getReparte().intValue());
			repartePDV.setProduto(produto);
			
			soma += repartePDV.getReparte();
			repartePDVRepository.merge(repartePDV);
		}
		fixacaoReparte.setQtdeExemplares(soma);
		fixacaoReparteRepository.alterar(fixacaoReparte);
		
	}

	
	
}
