package br.com.abril.nds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class QueryController {

	@Autowired
	private Result result;
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Get
	@Path("/query")
	public void index() {
		result.forwardTo("/WEB-INF/jsp/query.jsp");
	}

	@Post
	@Path("/query")
	public void post(String query, boolean update) {

		if (update) {
			result.include("result", execUpdate(query));
		} else {
			result.include("result", execQuery(query));
		}
		
		result.include("query", query);
		result.include("update", update);
		
		result.forwardTo("/WEB-INF/jsp/query.jsp");
	}

	private String execUpdate(String query) {
		return jdbcTemplate.getJdbcOperations().update(query) + " row(s) affected.";
	}

	private String execQuery(String query) {
		SqlRowSet rs = jdbcTemplate.getJdbcOperations().queryForRowSet(query);
		StringBuilder header = new StringBuilder();
		StringBuilder body = new StringBuilder();
		header.append("<table>");
		header.append("<tr>");
		while (rs.next()) {
			String[] columnNames = rs.getMetaData().getColumnNames();
			body.append("<tr>");
			for (String string : columnNames) {
				if (rs.isFirst()) {
					header.append("<td>");
					header.append(string);
					header.append("</td>");
				}
				body.append("<td>");
				body.append(rs.getObject(string));
				body.append("</td>");
			}
			body.append("</tr>");
		}
		header.append("</tr>");
		header.append(body);
		header.append("</table>");
		
		return header.toString();
	}
}
