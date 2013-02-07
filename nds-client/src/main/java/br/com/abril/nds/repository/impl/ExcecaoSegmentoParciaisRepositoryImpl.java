package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaQueNaoRecebeExcecaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeExcecaoDTO;
import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.model.distribuicao.ExcecaoProdutoCota;
import br.com.abril.nds.model.distribuicao.TipoExcecao;
import br.com.abril.nds.repository.ExcecaoSegmentoParciaisRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ExcecaoSegmentoParciaisRepositoryImpl extends AbstractRepositoryModel<ExcecaoProdutoCota, Long> implements ExcecaoSegmentoParciaisRepository {

	public ExcecaoSegmentoParciaisRepositoryImpl() {
		super(ExcecaoProdutoCota.class);
	}

	@Override
	public List<ProdutoRecebidoDTO> obterProdutosRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {
		
		boolean filtroHasNumeroCota = false;
		boolean filtroHasNomePessoa = false;
		
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
			filtroHasNumeroCota = true;
		}
		
		if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
			filtroHasNomePessoa = true;
		}
		
		hql.append(" SELECT ");
		hql.append(" excecaoProdutoCota.id as idExcecaoProdutoCota, "); // ID ExcessaoProdutoCota
		hql.append(" produto.codigo as codigoProduto, "); // CODIGO PRODUTO
		hql.append(" produto.nome as nomeProduto, "); // NOME PRODUTO
		hql.append(" usuario.nome as nomeUsuario, "); // NOME DO USUÁRIO
		hql.append(" excecaoProdutoCota.dataAlteracao as dataAlteracao "); // DATA ALTERAÇÃO
		
		hql.append(" FROM ExcecaoProdutoCota as excecaoProdutoCota ");
		hql.append(" INNER JOIN excecaoProdutoCota.produto as produto ");
		hql.append(" INNER JOIN excecaoProdutoCota.usuario as usuario ");
		hql.append(" INNER JOIN excecaoProdutoCota.cota as cota ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		
		// O filtro sempre terá OU nomeCota OU codigoCota
		hql.append(" WHERE ");
		
		hql.append(" excecaoProdutoCota.tipoExcecao = :tipoExcecao ");
		
		if(filtro.isExcecaoSegmento()){
			parameters.put("tipoExcecao", TipoExcecao.SEGMENTO);
		}else {
			parameters.put("tipoExcecao", TipoExcecao.PARCIAL);
		}
		
		if (filtroHasNumeroCota) {
			hql.append(" and cota.numeroCota = :numeroCota ");
			parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
		}else if (filtroHasNomePessoa) {
			hql.append(" and coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa ");
			parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoRecebidoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
	}
	
	@Override
	public List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {

		boolean filtroHasNumeroCota = false;
		boolean filtroHasNomePessoa = false;
		boolean filtroHasNomeProduto = false;
		boolean filtroHasCodigoProduto = false;
		
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
			filtroHasNumeroCota = true;
		}
		
		if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
			filtroHasNomePessoa = true;
		}
		
		if (filtro.getProdutoDto() != null) {
			if (filtro.getProdutoDto().getNomeProduto() != null && !filtro.getProdutoDto().getNomeProduto().isEmpty()) {
				filtroHasNomeProduto = true;
			}
			
			if (filtro.getProdutoDto().getCodigoProduto() != null && !filtro.getProdutoDto().getCodigoProduto().equals(0)) {
				filtroHasCodigoProduto = true;
			}
		}
		
		hql.append(" SELECT ");
		hql.append(" DISTINCT ");
		hql.append(" produto.id as idProduto, "); // ID PRODUTO
		hql.append(" produto.codigo as codigoProduto, "); // CODIGO PRODUTO
		hql.append(" produto.nome as nomeProduto, "); // NOME PRODUTO
		hql.append(" tipoSegmentoProduto.descricao as nomeSegmento, "); // NOME SEGMENTO
		hql.append(" coalesce(juridica.nomeFantasia, juridica.razaoSocial, '') as nomeFornecedor "); // NOME FORNECEDOR
		
		if (!filtro.isExcecaoSegmento()) {
			hql.append(" FROM Cota as cota ");
		}else{
			hql.append(" FROM SegmentoNaoRecebido as segmentoNaoRecebido ");
			hql.append(" INNER JOIN segmentoNaoRecebido.cota as cota ");
		}
		
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		hql.append(" INNER JOIN cota.estudoCotas as estudosCotas ");
		hql.append(" INNER JOIN estudosCotas.estudo as estudo ");
		hql.append(" INNER JOIN estudo.produtoEdicao as produtoEdicao ");
		hql.append(" INNER JOIN produtoEdicao.produto as produto ");
		hql.append(" INNER JOIN produto.tipoSegmentoProduto as tipoSegmentoProduto ");
		hql.append(" INNER JOIN produto.fornecedores as fornecedores ");
		hql.append(" INNER JOIN fornecedores.juridica as juridica ");
		
		// O filtro sempre terá OU nomeCota OU codigoCota
		hql.append(" WHERE ");
		
		if (!filtro.isExcecaoSegmento()) {
			hql.append(" cota.parametroDistribuicao.recebeRecolheParciais = :recolheParciais and ");
			parameters.put("recolheParciais", false);
		}
		
		if (filtroHasNomeProduto) {
			if (filtro.isAutoComplete()) {
				hql.append(" produto.nome like :nomeProduto and ");
				parameters.put("nomeProduto", filtro.getProdutoDto().getNomeProduto() + "%");
			}else {
				hql.append(" produto.nome = :nomeProduto and ");
				parameters.put("nomeProduto", filtro.getProdutoDto().getNomeProduto());
			}			
		}else if(filtroHasCodigoProduto) {
			hql.append(" produto.codigo = :codigoProduto and ");
			parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());
		}
		
		hql.append(" produto.id not in ( SELECT excecao.produto.id FROM ExcecaoProdutoCota as excecao ");
		hql.append(" INNER JOIN excecao.cota as cota ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		hql.append(" WHERE ");
		
		if (filtroHasNumeroCota) {
			hql.append(" cota.numeroCota = :numeroCota )");
			parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
		}else if (filtroHasNomePessoa) {
			hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa )");
			parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoNaoRecebidoDTO.class));
		
		return query.list();
	}
	
	@Override
	public List<CotaQueRecebeExcecaoDTO> obterCotasQueRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
				
		boolean filtroHasNomeProduto = false;
		boolean filtroHasCodigoProduto = false;
		
		if (filtro.getProdutoDto().getNomeProduto() != null && !filtro.getProdutoDto().getNomeProduto().isEmpty()) {
			filtroHasNomeProduto = true;
		}
		
		if (filtro.getProdutoDto().getCodigoProduto() != null && !filtro.getProdutoDto().getCodigoProduto().equals(0)) {
			filtroHasCodigoProduto = true;
		}
		
		hql.append(" SELECT ");
		hql.append(" excecaoProdutoCota.id as idExcecaoProdutoCota, "); // ID ExcessaoProdutoCota
		hql.append(" cota.situacaoCadastro as statusCota, "); // STATUS DA COTA
		hql.append(" cota.numeroCota as numeroCota, "); // NUMERO DA COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomePessoa, "); // NOME DA COTA
		hql.append(" usuario.nome as nomeUsuario, "); // NOME DO USUÁRIO
		hql.append(" excecaoProdutoCota.dataAlteracao as dataAlteracao "); // DATA ALTERAÇÃO
		
		hql.append(" FROM ExcecaoProdutoCota as excecaoProdutoCota ");
		hql.append(" INNER JOIN excecaoProdutoCota.produto as produto ");
		hql.append(" INNER JOIN excecaoProdutoCota.usuario as usuario ");
		hql.append(" INNER JOIN excecaoProdutoCota.cota as cota ");
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		
		// O filtro sempre terá OU nomeCota OU codigoCota
	hql.append(" WHERE ");
		
		hql.append(" excecaoProdutoCota.tipoExcecao = :tipoExcecao and");
		if(filtro.isExcecaoSegmento()){
			parameters.put("tipoExcecao", TipoExcecao.SEGMENTO);
		}else {
			parameters.put("tipoExcecao", TipoExcecao.PARCIAL);
		}
		
		if(filtroHasCodigoProduto) {
			hql.append(" produto.codigo = :codigoProduto");
			parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());
		} else if (filtroHasNomeProduto) {
			hql.append(" produto.nome = :nomeProduto");
			parameters.put("nomeProduto", filtro.getProdutoDto().getNomeProduto());
		} 
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueRecebeExcecaoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
	}

	@Override
	public List<CotaQueNaoRecebeExcecaoDTO> obterCotasQueNaoRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {
		
		boolean filtroHasNumeroCota = false;
		boolean filtroHasNomePessoa = false;
		boolean filtroHasNomeProduto = false;
		boolean filtroHasCodigoProduto = false;
		
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder hql = new StringBuilder();
		
		if (filtro.getProdutoDto().getNomeProduto() != null && !filtro.getProdutoDto().getNomeProduto().isEmpty()) {
			filtroHasNomeProduto = true;
		}
		
		if (filtro.getProdutoDto().getCodigoProduto() != null && !filtro.getProdutoDto().getCodigoProduto().equals(0)) {
			filtroHasCodigoProduto = true;
		}
		
		if (filtro.getCotaDto() != null) {
			if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
				filtroHasNumeroCota = true;
			}
			if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
				filtroHasNomePessoa = true;
			}
		}
		
		hql.append(" SELECT ");
		hql.append(" DISTINCT ");
		hql.append(" cota.numeroCota as numeroCota, "); // NUMERO DA COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') as nomePessoa, "); // NOME DA COTA
		hql.append(" cota.situacaoCadastro as statusCota "); // STATUS DA COTA
		
		if (!filtro.isExcecaoSegmento()) {
			hql.append(" FROM Cota as cota ");
		}else{
			hql.append(" FROM SegmentoNaoRecebido as segmentoNaoRecebido ");
			hql.append(" INNER JOIN segmentoNaoRecebido.cota as cota ");
		}
		
		hql.append(" INNER JOIN cota.pessoa as pessoa ");
		hql.append(" INNER JOIN cota.estudoCotas as estudosCotas ");
		hql.append(" INNER JOIN estudosCotas.estudo as estudo ");
		hql.append(" INNER JOIN estudo.produtoEdicao as produtoEdicao ");
		hql.append(" INNER JOIN produtoEdicao.produto as produto ");
		
		// O filtro sempre terá OU nomeProduto OU codigoProduto
		hql.append(" WHERE ");
		
		if (!filtro.isExcecaoSegmento()) {
			hql.append(" cota.parametroDistribuicao.recebeRecolheParciais = :recolheParciais and ");
			parameters.put("recolheParciais", false);
		}
		
		if (filtroHasNumeroCota) {
			hql.append(" cota.numeroCota = :numeroCota and ");
			parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
		}else if (filtroHasNomePessoa) {
			if (filtro.isAutoComplete()) {
				hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') like :nomePessoa and ");
				parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa() + "%");
			}else {
				hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa and ");
				parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
			}
		}
		
		hql.append(" cota.id not in ( SELECT excecao.cota.id FROM ExcecaoProdutoCota as excecao ");
		hql.append(" INNER JOIN excecao.produto as produto ");
		hql.append(" WHERE ");
		
		if (filtroHasCodigoProduto) {
			hql.append(" produto.codigo = :codigoProduto )");
			parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());
		}else if (filtroHasNomeProduto) {
			hql.append(" produto.nome = :nomeProduto )");
			parameters.put("nomeProduto", filtro.getProdutoDto().getNomeProduto());
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		setParameters(query, parameters);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoRecebeExcecaoDTO.class));
		
		return query.list();
	}
	
	private void configurarPaginacao(FiltroDTO filtro, Query query) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao == null) 
			return;
		
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
	
	private void setParameters(Query query, Map<String, Object> parameters) {
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
	}
}
