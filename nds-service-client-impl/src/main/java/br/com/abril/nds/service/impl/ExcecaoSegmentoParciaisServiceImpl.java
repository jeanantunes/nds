package br.com.abril.nds.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaQueNaoRecebeExcecaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeExcecaoDTO;
import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.model.distribuicao.ExcecaoProdutoCota;
import br.com.abril.nds.repository.ExcecaoSegmentoParciaisRepository;
import br.com.abril.nds.service.ExcecaoSegmentoParciaisService;
import br.com.abril.nds.service.ProdutoService;

@Service
public class ExcecaoSegmentoParciaisServiceImpl implements ExcecaoSegmentoParciaisService {

	@Autowired
	private ExcecaoSegmentoParciaisRepository excecaoSegmentoParciaisRepository; 
	
	@Autowired
	private ProdutoService produtoService;
	
	@Transactional(readOnly = true)
	@Override
	public List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {

		return excecaoSegmentoParciaisRepository.obterProdutosRecebidosPelaCota(filtro);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {
		checkCodigoProduto(filtro);
		return excecaoSegmentoParciaisRepository.obterProdutosNaoRecebidosPelaCota(filtro);
	}

	@Transactional
	@Override
	public void inserirListaExcecao(List<ExcecaoProdutoCota> listaExcessaoProdutoCota) {
		for (ExcecaoProdutoCota excessaoProdutoCota : listaExcessaoProdutoCota) {
			
			String icd = null;
			if(excessaoProdutoCota.getProduto()!=null ){
				if(excessaoProdutoCota.getProduto().getCodigo().length()==8){
					icd = this.produtoService.obterProdutoPorCodigo(excessaoProdutoCota.getProduto().getCodigo()).getCodigoICD();
				}else{
					icd=excessaoProdutoCota.getProduto().getCodigo();
				}
				
			}else if(StringUtils.isNotEmpty(excessaoProdutoCota.getCodigoICD())){
				if(excessaoProdutoCota.getCodigoICD().length()==8){
					icd = this.produtoService.obterProdutoPorCodigo(excessaoProdutoCota.getCodigoICD()).getCodigoICD();
				}else{
					icd=excessaoProdutoCota.getCodigoICD();
				}
			}
			
			
			excessaoProdutoCota.setCodigoICD(icd);
			
			excecaoSegmentoParciaisRepository.adicionar(excessaoProdutoCota);
		
			}
			
		}

	@Transactional
	@Override
	public void excluirExcecaoProduto(Long id) {
		excecaoSegmentoParciaisRepository.removerPorId(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaQueRecebeExcecaoDTO> obterCotasQueRecebemExcecaoPorProduto(	FiltroExcecaoSegmentoParciaisDTO filtro) {
		checkCodigoProduto(filtro);
		return excecaoSegmentoParciaisRepository.obterCotasQueRecebemExcecaoPorProduto(filtro);
	}

	@Transactional(readOnly = true)
	@Override
	public List<CotaQueNaoRecebeExcecaoDTO> obterCotasQueNaoRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {
		checkCodigoProduto(filtro);
		return excecaoSegmentoParciaisRepository.obterCotasQueNaoRecebemExcecaoPorProduto(filtro);
	}

	private void checkCodigoProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {
		if(filtro.getProdutoDto().getCodigoProduto().length()==8){
			String icd = this.produtoService.obterProdutoPorCodigo(filtro.getProdutoDto().getCodigoProduto()).getCodigoICD();
			filtro.getProdutoDto().setCodigoProduto(icd);
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<CotaQueNaoRecebeExcecaoDTO> autoCompletarPorNomeCotaQueNaoRecebeExcecao(FiltroExcecaoSegmentoParciaisDTO filtro) {
		return excecaoSegmentoParciaisRepository.autoCompletarPorNomeCotaQueNaoRecebeExcecao(filtro);
	}
}
