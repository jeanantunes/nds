package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.ExcecaoSegmentoParciais;
import br.com.abril.nds.repository.ExcecaoSegmentoParciaisRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ExcecaoSegmentoParciaisRepositoryImpl extends AbstractRepositoryModel<ExcecaoSegmentoParciais, Long> implements ExcecaoSegmentoParciaisRepository {

	private Map<String, Object> parameters; 
	
	public ExcecaoSegmentoParciaisRepositoryImpl() {
		super(ExcecaoSegmentoParciais.class);
	}

	@Override
	public List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCotaPorSegmento(FiltroExcecaoSegmentoParciaisDTO filtro) {

		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" DISTINCT ");
		hql.append(" produto.id as idProduto, ");
		hql.append(" produto.codigo as codigoProduto, ");
		hql.append(" produto.nome as nomeProduto, ");
		hql.append(" tipoSegmentoProduto.descricao as nomeSegmento, ");
		hql.append(" coalesce(juridica.nomeFantasia, juridica.razaoSocial, '') as nomeFornecedor "); // FORNECEDOR
		
		hql.append(" FROM SegmentoNaoRecebido as segmentoNaoRecebido ");
		hql.append(" INNER JOIN segmentoNaoRecebido.tipoSegmentoProduto as tipoSegmentoProduto ");
		hql.append(" INNER JOIN segmentoNaoRecebido.cota as cota ");
		hql.append(" INNER JOIN cota.estudoCotas as estudosCotas ");
		hql.append(" INNER JOIN estudosCotas.estudo as estudo ");
		hql.append(" INNER JOIN estudo.produtoEdicao as produtoEdicao ");
		hql.append(" INNER JOIN produtoEdicao.produto as produto ");
		hql.append(" INNER JOIN produto.fornecedores as fornecedores ");
		hql.append(" INNER JOIN fornecedores.juridica as juridica ");
		
		// nunca o filtro vira sem o nomeCota e codigoCota nulos
		hql.append(" WHERE ");
		
		if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
			hql.append(" cota.numeroCota = :numeroCota ");
			parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
		}else if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
			hql.append(" coalesce(juridica.nomeFantasia, juridica.razaoSocial, '') = :nomePessoa ");
			parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
		}
		
		if (filtro.getNomeProduto() != null && !filtro.getNomeProduto().isEmpty()) {
			if (filtro.isSelectFromAutoComplete()) {
				hql.append(" and produto.nome = :nomeProduto");
				parameters.put("nomeProduto", filtro.getNomeProduto());
			}else {
				hql.append(" and produto.nome like :nomeProduto");
				parameters.put("nomeProduto", filtro.getNomeProduto() + "%");
			}			
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoNaoRecebidoDTO.class));

		query.setMaxResults(20);
		
		//configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}
		
		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}
		
		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
		
		
	}
	
	private void setParameters(Query query) {
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
	}

}
