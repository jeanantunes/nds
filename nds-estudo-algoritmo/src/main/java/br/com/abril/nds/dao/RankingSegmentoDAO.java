package br.com.abril.nds.dao;


//@Repository
public class RankingSegmentoDAO {

//	@Autowired
//	private NamedParameterJdbcTemplate jdbcTemplate;
//
//	@Value("#{query_estudo.queryCotasOrdenadasMaiorMenor}")
//	private String queryCotasOrdenadasMaiorMenor;

	/*
	 * final String SQL_RETURN_ULTIMO_ID_RANKING_GERADO = "SELECT max(id) from ranking_segmento_gerados";
	 * 
	 * public Long getUltimoIDRankingSegmentoGerado(){
	 * 
	 * Long d = null; try { StringBuffer sb = new StringBuffer(SQL_RETURN_ULTIMO_ID_RANKING_GERADO);
	 * 
	 * PreparedStatement psmt = Conexao.getConexao().prepareStatement( sb.toString()); ResultSet rs = psmt.executeQuery(); while
	 * (rs.next()) { d = rs.getLong(1); } } catch (Exception ex) { System.out.println("Ocorreu um erro ao tentar consultar");
	 * ex.printStackTrace(); }
	 * 
	 * return d; }
	 */

//    public List<Long> getCotasOrdenadasMaiorMenor(List<CotaEstudo> cotaList, ProdutoEdicaoEstudo produtoEdicaoBase) {
//		List<Long> listaCotas = new ArrayList<>();
//		for (CotaEstudo cota : cotaList) {
//			listaCotas.add(cota.getId());
//		}
//		Map<String, Object> params = new HashMap<>();
//		params.put("PRODUTO_EDICAO_ID", produtoEdicaoBase.getId());
//		params.put("IDS_COTA", listaCotas);
//		SqlRowSet rs = jdbcTemplate.queryForRowSet(queryCotasOrdenadasMaiorMenor, params);
//
//
//		List<Long> retorno = new ArrayList<>();
//		while (rs.next()) {
//			retorno.add(rs.getLong(1));
//		}
//		return retorno;
//	}
}
