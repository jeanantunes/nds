package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.repository.RomaneioRepository;

@Repository
public class RomaneioRepositoryImpl extends AbstractRepositoryModel<Box, Long> implements RomaneioRepository {

	public RomaneioRepositoryImpl() {
		super(Box.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RomaneioDTO> buscarRomaneios(FiltroRomaneioDTO filtro,String limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nome, ");		
		hql.append("telefone.numero as numeroTelefone, ");
		hql.append("cota.id as idCota ");
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		
		hql.append(getOrderBy(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosRomaneio(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				RomaneioDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar.equals("limitar")) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return  popularEndereco(query.list());
		
	}
	
	private List<RomaneioDTO> popularEndereco(List<RomaneioDTO> listaRomaneios){
		List<RomaneioDTO> listaAux = new ArrayList<RomaneioDTO>();
		for(RomaneioDTO romaneio:listaRomaneios){
			StringBuilder hql = new StringBuilder();
			
			hql.append("SELECT endereco.logradouro as logradouro, ");
			hql.append("endereco.bairro as bairro, ");		
			hql.append("endereco.cidade as cidade, ");
			hql.append("endereco.uf as uf ");
			

			hql.append(" from EnderecoCota endCota ");
			hql.append(" LEFT JOIN endCota.endereco as endereco ");
			
			hql.append( " WHERE endCota.cota.id =:idCota ");
			hql.append( " AND (endCota.tipoEndereco = 'LOCAL_ENTREGA' OR endCota.principal = 1) ");
			
			Query query =  getSession().createQuery(hql.toString());
			
			query.setParameter("idCota", romaneio.getIdCota());
			
			query.setMaxResults(1);
			
			query.setResultTransformer(new AliasToBeanResultTransformer(
					RomaneioDTO.class));
			
			RomaneioDTO dto =  (RomaneioDTO) query.uniqueResult();
			if(dto != null){
				romaneio.setLogradouro(dto.getLogradouro());
				romaneio.setBairro(dto.getBairro());
				romaneio.setCidade(dto.getCidade());
				romaneio.setUf(dto.getUf());
			}
			listaAux.add(romaneio);
		}
		return listaAux;
	}
	
	private String getSqlFromEWhereRomaneio(FiltroRomaneioDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from Cota cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN pessoa.telefones as telefone ");
		hql.append(" LEFT JOIN cota.box as box ");
		hql.append(" LEFT JOIN box.roteiros as roteiro ");
		hql.append(" LEFT JOIN roteiro.rotas as rota ");
		

		boolean usarAnd = false;
		
		if(filtro.getIdBox() != null && filtro.getIdBox() != -1 ) {
			hql.append( (usarAnd ? " and ":" where ") +" box.id = :idBox ");
			usarAnd = true;
		}
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() != -1){
			hql.append( (usarAnd ? " and ":" where ") + " roteiro.id = :idRoteiro ");
			usarAnd = true;
		}
		if(filtro.getIdRota() != null && filtro.getIdRota() != -1){
			hql.append( (usarAnd ? " and ":" where ") + " rota.id = :idRota ");
			usarAnd = true;
		}


		return hql.toString();
	}
	
	private String getOrderBy(FiltroRomaneioDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		boolean usarAsc = false;
		
		if(filtro.getIdRoteiro() == null && filtro.getIdRota() != null){
			hql.append(" order by rota.ordem asc ");
			usarAsc = true;
		}
		if(filtro.getIdRoteiro() != null && filtro.getIdRota() == null){
			hql.append(" order by roteiro.ordem asc ");
			usarAsc = true;
		}
		if(filtro.getIdRoteiro() != null && filtro.getIdRota() != null){
			hql.append(" order by roteiro.ordem asc, rota.ordem asc ");
			usarAsc = true;
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null && usarAsc) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private HashMap<String,Object> buscarParametrosRomaneio(FiltroRomaneioDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getIdBox() != null && filtro.getIdBox() != -1 ) { 
			param.put("idBox", filtro.getIdBox());
		}
		if(filtro.getIdRoteiro() != null && filtro.getIdRoteiro() != -1){
			param.put("idRoteiro", filtro.getIdRoteiro());
		}
		if(filtro.getIdRota() != null && filtro.getIdRota() != -1){
			param.put("idRota", filtro.getIdRota());
		}
		
		return param;
	}

	@Override
	public Integer buscarTotal(FiltroRomaneioDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cota) ");
		
		hql.append(getSqlFromEWhereRomaneio(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosRomaneio(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}	
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}
	
}
