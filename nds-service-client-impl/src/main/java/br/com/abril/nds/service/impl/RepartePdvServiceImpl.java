package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;
import br.com.abril.nds.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.MixCotaProduto;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RepartePdvService;

@Service
public class RepartePdvServiceImpl implements RepartePdvService{

	@Autowired
	private RepartePDVRepository repartePDVRepository;

    @Autowired
    private FixacaoRepartePdvRepository fixacaoRepartePdvRepository;
	
	@Autowired
	private FixacaoReparteRepository fixacaoReparteRepository;
	
	@Autowired
	private MixCotaProdutoRepository mixCotaProdutoRepository;
	
	@Autowired
	private CotaRepository  cotaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private PdvRepository pdvRepository;

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
	public void salvarRepartesPDVMix(List<RepartePDVDTO> listaRepartes, String codProduto, Long idMix) {
		int soma = 0;
		Produto produto= this.produtoService.obterProdutoPorCodigo(codProduto);
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

		mixCotaProduto.setReparteMinimo(Long.valueOf(soma));
		mixCotaProduto.setReparteMaximo(Long.valueOf(soma));
        mixCotaProduto.setDataHora(new Date());
        mixCotaProdutoRepository.alterar(mixCotaProduto);
	}

	@Override
	@Transactional
	public void salvarRepartesPDV(List<RepartePDVDTO> listaRepartes, String codProduto, Long idFixacao, boolean manterFixa) {
		int soma = 0;
		FixacaoReparte fixacaoReparte = fixacaoReparteRepository.buscarPorId(idFixacao);
		fixacaoReparte.setManterFixa(manterFixa);
		PDV pdv = null;
		
		for (RepartePDVDTO repartePDVDTO : listaRepartes) {
			if(repartePDVDTO.getCodigoPdv() !=null) {
				pdv = pdvRepository.buscarPorId(repartePDVDTO.getCodigoPdv());
			}

            FixacaoRepartePdv fixacaoRepartePdv = fixacaoRepartePdvRepository.obterPorFixacaoReparteEPdv(fixacaoReparte, pdv);
            if(fixacaoRepartePdv == null) {
                fixacaoRepartePdv = new FixacaoRepartePdv();
			}
			
			fixacaoRepartePdv.setFixacaoReparte(fixacaoReparte);
			fixacaoRepartePdv.setPdv(pdv);
			fixacaoRepartePdv.setRepartePdv(repartePDVDTO.getReparte());

			soma += fixacaoRepartePdv.getRepartePdv();
			fixacaoRepartePdvRepository.merge(fixacaoRepartePdv);
		}
		fixacaoReparte.setDataHora(new Date());
		fixacaoReparte.setQtdeExemplares(soma);
		fixacaoReparteRepository.alterar(fixacaoReparte);
	}
}
