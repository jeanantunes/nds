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
import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.model.distribuicao.ExcecaoProdutoCota;
import br.com.abril.nds.model.distribuicao.TipoExcecao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ExcecaoSegmentoParciaisRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ExcecaoSegmentoParciaisRepositoryImpl extends AbstractRepositoryModel<ExcecaoProdutoCota, Long> implements
	ExcecaoSegmentoParciaisRepository {

    public ExcecaoSegmentoParciaisRepositoryImpl() {
	super(ExcecaoProdutoCota.class);
    }

    @SuppressWarnings("unchecked")
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

	if (filtro.isExcecaoSegmento()) {
	    parameters.put("tipoExcecao", TipoExcecao.SEGMENTO);
	} else {
		parameters.put("tipoExcecao", TipoExcecao.PARCIAL);
	}

	
	
	if (filtroHasNumeroCota) {
	    hql.append(" and cota.numeroCota = :numeroCota ");
	    parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
	} else if (filtroHasNomePessoa) {
	    hql.append(" and coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa ");
	    parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
	}

	hql.append(" order by nomeProduto, codigoProduto");

	Query query = getSession().createQuery(hql.toString());

	setParameters(query, parameters);

	query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoRecebidoDTO.class));

	configurarPaginacao(filtro, query);

	return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" SELECT DISTINCT ");
    	hql.append(" 	produto.id as idProduto, ");
		hql.append(" 	produto.codigo as codigoProduto, ");
		hql.append(" 	produto.nome as nomeProduto, ");
		hql.append(" 	produto.tipoSegmentoProduto.descricao as nomeSegmento, ");
		hql.append(" 	coalesce(j.nomeFantasia, j.razaoSocial, '') as nomeFornecedor ");
		hql.append(" FROM Produto AS produto ");
		hql.append("  JOIN produto.fornecedores as f ");
		hql.append("  JOIN f.juridica as j ");
		hql.append(" WHERE produto.id not in ");
		hql.append(" 	(select distinct prod.id from ExcecaoProdutoCota e  ");
		hql.append(" 		JOIN e.produto prod ");
		hql.append(" 		JOIN e.cota cotaJoin ");
		
		hql.append(" 	where e.tipoExcecao = :tipoExcecao " );
		
		if (filtro.getCotaDto() != null) {
		hql.append(" 	and cotaJoin.numeroCota = :numCota ");
		}
		
		hql.append(" 	) ");
		
		if (filtro.getProdutoDto() != null) {		
			hql.append(" and produto.codigo = :codProduto ");
			}
		
		Query query = super.getSession().createQuery(hql.toString());
		
		if (filtro.isExcecaoSegmento()) {
			query.setParameter("tipoExcecao", TipoExcecao.SEGMENTO);
		} else {
			query.setParameter("tipoExcecao", TipoExcecao.PARCIAL);
		}
		if (filtro.getProdutoDto() != null) {	
		query.setParameter("codProduto", filtro.getProdutoDto().getCodigoProduto());
		}
		
		if (filtro.getCotaDto() != null) {
			query.setParameter("numCota", filtro.getCotaDto().getNumeroCota());
			}
		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoNaoRecebidoDTO.class));
		
		configurarPaginacao(filtro, query);
	
	return query.list();
    }

    @Override
    public List<CotaQueNaoRecebeExcecaoDTO> obterCotasQueNaoRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {

    	StringBuilder hql = new StringBuilder();
		
    	hql.append(" SELECT DISTINCT ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa, ");
		hql.append(" cota.situacaoCadastro as statusCota ");
		
		hql.append(" FROM Cota AS cota ");
		
		hql.append(" JOIN cota.pdvs as pdv ");
		hql.append(" JOIN cota.pessoa as pessoa ");
		
		hql.append(" WHERE cota.recebeRecolheParciais = 0 ");
		hql.append(" and cota.id not in ");
		hql.append(" (select cotaJoin.id from ExcecaoProdutoCota e  ");
		hql.append(" JOIN e.produto prod ");
		hql.append(" JOIN e.cota cotaJoin ");
		hql.append(" where e.tipoExcecao = :tipoExcecao " );
		hql.append(" and prod.codigo = :codProduto ");
		hql.append(" ) ");

		if (filtro.getCotaDto() != null) {		
		hql.append(" and cota.numeroCota = :numCota ");
		}
		
		hql.append(" order by numeroCota");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		if (filtro.isExcecaoSegmento()) {
			query.setParameter("tipoExcecao", TipoExcecao.SEGMENTO);
		} else {
			query.setParameter("tipoExcecao", TipoExcecao.PARCIAL);
		}
		query.setParameter("codProduto", filtro.getProdutoDto().getCodigoProduto());
		
		if (filtro.getCotaDto() != null) {
		query.setParameter("numCota", filtro.getCotaDto().getNumeroCota());
		}
		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoRecebeExcecaoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
    }

    @Override
    public List<CotaQueRecebeExcecaoDTO> obterCotasQueRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {
	Map<String, Object> parameters = new HashMap<String, Object>();

	StringBuilder hql = new StringBuilder();
	hql.append("select distinct ");
	hql.append("       e.id as idExcecaoProdutoCota, ");
	hql.append("       c.situacaoCadastro as statusCota, ");
	hql.append("       c.numeroCota as numeroCota, ");
	hql.append("       coalesce(pe.nomeFantasia, pe.razaoSocial, pe.nome,'') as nomePessoa, ");
	hql.append("       u.nome as nomeUsuario, ");
	hql.append("       e.dataAlteracao as dataAlteracao ");
	hql.append("  from ExcecaoProdutoCota as e ");
	hql.append("  join e.produto as p ");
	hql.append("  join e.usuario as u ");
	hql.append("  join e.cota as c ");
	hql.append("  join c.pessoa as pe ");
	hql.append(" where 1 = 1 ");
	hql.append("   and e.tipoExcecao = :tipoExcecao ");
	
	
	if (filtro.isExcecaoSegmento()) {
		parameters.put("tipoExcecao", TipoExcecao.SEGMENTO);
	} else {
		parameters.put("tipoExcecao", TipoExcecao.PARCIAL);
	}

	if (filtro.getProdutoDto().getCodigoProduto() != null && !filtro.getProdutoDto().getCodigoProduto().equals(0)) {
	    hql.append(" and p.codigo = :codigoProduto");
	    parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());
	} else if (filtro.getProdutoDto().getNomeProduto() != null && !filtro.getProdutoDto().getNomeProduto().isEmpty()) {
	    hql.append("and p.nome = :nomeProduto");
	    parameters.put("nomeProduto", filtro.getProdutoDto().getNomeProduto());
	}
	hql.append(" order by numeroCota, nomePessoa");

	Query query = getSession().createQuery(hql.toString());
	setParameters(query, parameters);
	query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueRecebeExcecaoDTO.class));
	configurarPaginacao(filtro, query);
	return query.list();
    }

    private void configurarPaginacao(FiltroDTO filtro, Query query) {

	PaginacaoVO paginacao = filtro.getPaginacao();

	if (paginacao == null)
	    return;

	if (paginacao.getQtdResultadosTotal().equals(0)) {
	    paginacao.setQtdResultadosTotal(query.list().size());
	}

	if (paginacao.getQtdResultadosPorPagina() != null) {
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

	@Override
	public List<CotaQueNaoRecebeExcecaoDTO> autoCompletarPorNomeCotaQueNaoRecebeExcecao(FiltroExcecaoSegmentoParciaisDTO filtro) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
			StringBuilder hql = new StringBuilder();
			hql.append("select distinct ");
			hql.append("       c.numeroCota as numeroCota, ");
			hql.append("       coalesce(pe.nomeFantasia, pe.razaoSocial, pe.nome,'') as nomePessoa, ");
			hql.append("       c.situacaoCadastro as statusCota ");
			hql.append("  from SegmentoNaoRecebido as s ");
			hql.append("  join s.cota as c ");
			hql.append("  join c.pessoa as pe ");
			hql.append("  join s.tipoSegmentoProduto as t ");
			hql.append("  join t.produtos as p ");
			hql.append(" where 1 = 1 ");
			if (!filtro.isExcecaoSegmento()) {
			    hql.append(" and c.parametroDistribuicao.recebeRecolheParciais = :recolheParciais ");
			    parameters.put("recolheParciais", false);
			}
			hql.append(" and c.id not in (select ex.cota.id ");
			hql.append("                    from ExcecaoProdutoCota as ex ");
			hql.append("                    join ex.produto as pr ");
			hql.append("                   where ");
			//garante que a excecao exibida e do tipo correspondente ao da pesquisa(segmento ou parcial)
			hql.append(" ex.tipoExcecao = :tipoExcecao ");
			
			if (filtro.isExcecaoSegmento()) {
				//na consulta por excecao segmento, nao deve ser exibido consulta parcial
			    parameters.put("tipoExcecao", TipoExcecao.SEGMENTO);
			} else {
				//na consulta por excecao parcial, nao deve ser exibido consulta segmento
				parameters.put("tipoExcecao", TipoExcecao.PARCIAL);
			}
			
			if (filtro.getProdutoDto().getCodigoProduto() != null && !filtro.getProdutoDto().getCodigoProduto().equals(0)) {
			    hql.append(" and  pr.codigo = :codigoProduto ) ");
			    hql.append(" and p.codigo = :codigoProduto ");
			    parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());
			} else if (filtro.getProdutoDto().getNomeProduto() != null && !filtro.getProdutoDto().getNomeProduto().isEmpty()) {
			    hql.append(" pr.nome = :nomeProduto ) ");
			    hql.append(" and p.nome = :nomeProduto ");
			    parameters.put("nomeProduto", filtro.getProdutoDto().getNomeProduto());
			}
			boolean consultaFiltrada = false;
			if (filtro.getCotaDto() != null) {
			    if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
				consultaFiltrada = true;
				hql.append(" and c.numeroCota = :numeroCota ");
				parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
			    } else if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
				consultaFiltrada = true;
				hql.append(" and coalesce(pe.nomeFantasia, pe.razaoSocial, pe.nome,'') = :nomePessoa ");
				parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
			    }
			}
			if (!consultaFiltrada) {
			    hql.append(" and 1 = 0 ");
			}
			hql.append(" order by numeroCota, nomePessoa ");
		
			Query query = getSession().createQuery(hql.toString());
			setParameters(query, parameters);
			query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoRecebeExcecaoDTO.class));
			return query.list();
	}
}
