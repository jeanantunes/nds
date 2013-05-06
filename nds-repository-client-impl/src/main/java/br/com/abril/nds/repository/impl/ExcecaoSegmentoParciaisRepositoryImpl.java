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
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ExcecaoSegmentoParciaisRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ExcecaoSegmentoParciaisRepositoryImpl extends AbstractRepositoryModel<ExcecaoProdutoCota, Long> implements
	ExcecaoSegmentoParciaisRepository {

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

    @Override
    public List<ProdutoNaoRecebidoDTO> obterProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro) {

	Map<String, Object> parameters = new HashMap<String, Object>();

	StringBuilder hql = new StringBuilder();
	hql.append("select distinct ");
	hql.append("       p.id as idProduto, ");
	hql.append("       p.codigo as codigoProduto, ");
	hql.append("       p.nome as nomeProduto, ");
	hql.append("       t.descricao as nomeSegmento, ");
	hql.append("       coalesce(j.nomeFantasia, j.razaoSocial, '') as nomeFornecedor ");
	hql.append("  from SegmentoNaoRecebido as s ");
	hql.append("  join s.tipoSegmentoProduto as t ");
	hql.append("  join t.produtos as p ");
	hql.append("  join p.fornecedores as f ");
	hql.append("  join f.juridica as j ");
	hql.append("  join s.cota as c ");
	hql.append("  join c.pessoa as pe ");
	hql.append(" where p.id not in (select ex.produto.id ");
	hql.append("                      from ExcecaoProdutoCota as ex ");
	hql.append("                      join ex.cota as co ");
	hql.append("                      join co.pessoa as pes ");
	hql.append("                     where "); // o parentese e fechado no if abaixo

	if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
	    hql.append(" co.numeroCota = :numeroCota ) ");
	    hql.append(" and c.numeroCota = :numeroCota ");
	    parameters.put("numeroCota", filtro.getCotaDto().getNumeroCota());
	} else if (filtro.getCotaDto().getNomePessoa() != null && !filtro.getCotaDto().getNomePessoa().isEmpty()) {
	    hql.append(" coalesce(pes.nomeFantasia, pes.razaoSocial, pes.nome,'') = :nomePessoa )");
	    hql.append(" and coalesce(pe.nomeFantasia, pe.razaoSocial, pe.nome,'') = :nomePessoa ");
	    parameters.put("nomePessoa", filtro.getCotaDto().getNomePessoa());
	}
	boolean consultaFiltrada = false;
	if (filtro.getProdutoDto() != null) {
	    if (filtro.getProdutoDto().getCodigoProduto() != null && !filtro.getProdutoDto().getCodigoProduto().equals(0)) {
		consultaFiltrada = true;
		hql.append(" and p.codigo = :codigoProduto ");
		parameters.put("codigoProduto", filtro.getProdutoDto().getCodigoProduto());
	    } else if (filtro.getProdutoDto().getNomeProduto() != null && !filtro.getProdutoDto().getNomeProduto().isEmpty()) {
		consultaFiltrada = true;
		hql.append(" and p.nome = :nomeProduto ");
		parameters.put("nomeProduto", filtro.getProdutoDto().getNomeProduto());
	    }
	}
	if (!consultaFiltrada) {
	    hql.append(" and 1 = 0 ");
	}
	hql.append(" order by c.numeroCota");

	Query query = getSession().createQuery(hql.toString());
	setParameters(query, parameters);
	query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoNaoRecebidoDTO.class));
	return query.list();
    }

    @Override
    public List<CotaQueNaoRecebeExcecaoDTO> obterCotasQueNaoRecebemExcecaoPorProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {

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

	if (filtro.getProdutoDto().getCodigoProduto() != null && !filtro.getProdutoDto().getCodigoProduto().equals(0)) {
	    hql.append(" pr.codigo = :codigoProduto ) ");
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
}
