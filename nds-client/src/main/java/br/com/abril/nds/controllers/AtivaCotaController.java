package br.com.abril.nds.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AtivaCotaController {

	private static final String UPDATE_COTA = "update cota set situacao_cadastro = upper(:situacaoCadastro) where numero_cota = :numeroCota";

	@Autowired
	private Result result;
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Get("/ativaCota")
	public void index() {
		result.include("situacaoCadastro", SituacaoCadastro.values());
		result.forwardTo("/WEB-INF/jsp/ativaCota.jsp");
	}

	@Post("/ativaCota")
	public void post(Integer numeroCota, String situacaoCadastro) {
		
		String update = ativaCota(numeroCota, situacaoCadastro);
		
		result.include("situacaoCadastro", SituacaoCadastro.values());
		
		result.include("result", update);
		result.forwardTo("/WEB-INF/jsp/ativaCota.jsp");
	}
	
	private String ativaCota(Integer numeroCota, String situacaoCadastro) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("numeroCota", numeroCota);
		paramMap.put("situacaoCadastro", situacaoCadastro);
		
		if (jdbcTemplate.update(UPDATE_COTA, paramMap) > 0) {
			return "Cota[" + numeroCota + "] atualizada com exito. Situacao de Cadastro: " + situacaoCadastro;
		}
		return "Cota[" + numeroCota + "] não atualizada.";
	}
	
}
