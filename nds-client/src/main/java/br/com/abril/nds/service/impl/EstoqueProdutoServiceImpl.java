package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.EstoqueProduto}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class EstoqueProdutoServiceImpl implements EstoqueProdutoService {

	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Transactional(readOnly = true)
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {
		
		return this.estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
	}

	@Override
	@Transactional
	public List<ProdutoEdicaoSuplementarDTO> obterProdutosEdicaoSuplementarDisponivel(
			Date data, Long idCota) {
		
		List<MovimentoEstoqueCota> movimentosEstoqueReparteCotaAusente = this.movimentoEstoqueCotaService.obterMovimentoCotaPorTipoMovimento(data, idCota, 
				GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
		
		List<ProdutoEdicaoSuplementarDTO> listaProdutosEdicaoSuplementarDTO = new ArrayList<ProdutoEdicaoSuplementarDTO>();
		
		for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueReparteCotaAusente) {
			
			ProdutoEdicao produtoEdicao = movimentoEstoqueCota.getProdutoEdicao(); 
			
			EstoqueProduto estoqueProduto = this.buscarEstoquePorProduto(produtoEdicao.getId());
			
			ProdutoEdicaoSuplementarDTO produtoEdicaoSuplementar = new ProdutoEdicaoSuplementarDTO();
			
			produtoEdicaoSuplementar.setIdProdutoEdicao(produtoEdicao.getId());
			produtoEdicaoSuplementar.setCodigoProdutoEdicao(produtoEdicao.getCodigo());
			produtoEdicaoSuplementar.setNomeProdutoEdicao(produtoEdicao.getNomeComercial());
			produtoEdicaoSuplementar.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
			produtoEdicaoSuplementar.setReparte(movimentoEstoqueCota.getQtde());
			produtoEdicaoSuplementar.setQuantidadeDisponivel(estoqueProduto.getQtde());
			
			listaProdutosEdicaoSuplementarDTO.add(produtoEdicaoSuplementar);
		}
		
		return listaProdutosEdicaoSuplementarDTO;
	}

}
