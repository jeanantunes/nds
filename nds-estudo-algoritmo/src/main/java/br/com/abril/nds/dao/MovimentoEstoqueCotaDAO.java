package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class MovimentoEstoqueCotaDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.queryReparteJuramentadoAFaturar}")
	private String queryReparteJuramentadoAFaturar;

	public BigDecimal retornarReparteJuramentadoAFaturar(CotaEstudo cota, ProdutoEdicaoEstudo produtoEdicao) {
		BigDecimal valorJuramentado = BigDecimal.ZERO;

		Map<String, Object> params = new HashMap<>();
		params.put("TIPO_MOVIMENTO_ID", 32); // FIXME: corrigir select para buscar tipo movimento corretamente
		params.put("COTA_ID", cota.getId());
		params.put("PRODUTO_EDICAO_ID", produtoEdicao.getId());
		SqlRowSet rs = jdbcTemplate.queryForRowSet(queryReparteJuramentadoAFaturar, params);
		while (rs.next()) {
			valorJuramentado = rs.getBigDecimal(1);
		}
		return valorJuramentado;

	}
}