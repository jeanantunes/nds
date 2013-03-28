package br.com.abril.nds.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class EstudoDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.insertEstudo}")
	private String insertEstudo;

	@Value("#{query_estudo.insertEstudoCotas}")
	private String insertEstudoCotas;

	@Value("#{query_estudo.insertProdutoEdicao}")
	private String insertProdutoEdicao;

	@Value("#{query_estudo.insertProdutoEdicaoBase}")
	private String insertProdutoEdicaoBase;

	public void gravarEstudo(EstudoTransient estudo) {
		List<EstudoTransient> estudos = new ArrayList<>();
		estudos.add(estudo);
		Long estudoId = null;
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource paramSource = new BeanPropertySqlParameterSource(estudo);
			jdbcTemplate.update(insertEstudo, paramSource, keyHolder);
			estudoId = keyHolder.getKey().longValue();
			System.out.println(estudoId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (estudoId != null) {
			List<ProdutoEdicaoEstudo> listaProdutoEdicao = new ArrayList<>();
			for (CotaEstudo cota : estudo.getCotas()) {
				cota.setIdEstudo(estudoId);
				for (ProdutoEdicaoEstudo produto : cota.getEdicoesRecebidas()) {
					produto.setIdEstudo(estudoId);
					produto.setIdCota(cota.getId());
					listaProdutoEdicao.add(produto);
				}
			}
			gravarCotas(estudo.getCotas());

			for (ProdutoEdicaoEstudo prod : estudo.getEdicoesBase()) {
				prod.setIdEstudo(estudoId);
			}
			gravarProdutoEdicaoBase(estudo.getEdicoesBase());
			gravarProdutoEdicao(listaProdutoEdicao);
		}
	}

	public void gravarCotas(final List<CotaEstudo> cotas) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cotas.toArray());
		jdbcTemplate.batchUpdate(insertEstudoCotas, batch);
	}

	public void gravarProdutoEdicao(List<ProdutoEdicaoEstudo> produtosEdicao) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicao.toArray());
		jdbcTemplate.batchUpdate(insertProdutoEdicao, batch);
	}

	public void gravarProdutoEdicaoBase(List<ProdutoEdicaoEstudo> produtosEdicaoBase) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicaoBase.toArray());
		jdbcTemplate.batchUpdate(insertProdutoEdicaoBase, batch);
	}

	public void carregarPercentuaisProporcao(EstudoTransient estudo) {
		// TODO: implementar método para carregar percentuais de venda e pdv da tela de parâmetros do distribuidor (EMS 188)
	}
}
