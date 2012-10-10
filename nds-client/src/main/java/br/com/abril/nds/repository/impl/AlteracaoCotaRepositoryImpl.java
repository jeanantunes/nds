package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.AlteracaoCotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 */
@Repository
public class AlteracaoCotaRepositoryImpl extends AbstractRepositoryModel<Cota, Long> implements AlteracaoCotaRepository {

	/**
	 * Construtor padrão.
	 */
	public AlteracaoCotaRepositoryImpl() {
		super(Cota.class);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO){
		
		StringBuilder hql = new StringBuilder();
		boolean addedAnd = false;
		
		hql.append(" select new ").append(ConsultaAlteracaoCotaDTO.class.getCanonicalName());
		hql.append(" (cota.id, cota.numeroCota, pessoa.nome, pessoapj.razaoSocial, descontosProdutoEdicao.tipoDesconto, financeiro.fatorVencimento, financeiro.valorMininoCobranca, tipoEntrega.descricaoTipoEntrega, box.nome) ");
		hql.append(" from Cota cota ");
		hql.append(" JOIN cota.fornecedores fornecedor ");
		hql.append(" JOIN fornecedor.juridica as pessoapj ");
		hql.append(" JOIN cota.descontosProdutoEdicao descontosProdutoEdicao ");
		hql.append(" JOIN descontosProdutoEdicao.fornecedor fornecedorDescontosProdutoEdicao ");
		hql.append(" JOIN cota.parametroDistribuicao parametroDistribuicao ");
        hql.append(" JOIN parametroDistribuicao.tipoEntrega tipoEntrega ");
		hql.append(" JOIN cota.pessoa pessoa ");
		hql.append(" JOIN cota.enderecos enderecoCota 	");
		hql.append(" JOIN enderecoCota.endereco endereco 	");
        hql.append(" JOIN cota.box box ");
        hql.append(" JOIN cota.titularesCota titularesCota ");
        
//        hql.append(" JOIN titularesCota.numeroCota numeroCota ");
        
        hql.append(" JOIN titularesCota.financeiro financeiro ");
		hql.append(" where ");

//		hql.append(" titularesCota.numeroCota = cota.numeroCota ");
		
		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			hql.append("and cota.numeroCota = :numeroCota ");
			addedAnd = true;
		}
		
		if (filtroAlteracaoCotaDTO.getNomeCota() != null && !filtroAlteracaoCotaDTO.getNomeCota().isEmpty()&& !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			if(addedAnd)
				hql.append(" and ");	
			
			hql.append("  (upper(pessoa.nome) like upper(:nomeCota) OR upper(pessoa.razaoSocial) like  upper(:nomeCota ) )");
			addedAnd = true;
		}
		
		if (filtroAlteracaoCotaDTO.getIdBairro() != null && filtroAlteracaoCotaDTO.getIdBairro()>0) {
			if(addedAnd)
				hql.append(" and ");	
			
			hql.append(" endereco.codigoBairro = :idBairro ");
			addedAnd = true;
		}
		
		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null && !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty()&& !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			if(addedAnd)
				hql.append(" and ");	
			
			hql.append("  upper(endereco.cidade) like :idMunicipio ");
			addedAnd = true;
		}
		
		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null && filtroAlteracaoCotaDTO.getIdFornecedor() > 0) {
			if(addedAnd)
				hql.append(" and ");	
			
			hql.append(" fornecedor.id = :idFornecedor ");
			addedAnd = true;
		}
		
		if (filtroAlteracaoCotaDTO.getIdTpDesconto() != null && !filtroAlteracaoCotaDTO.getIdTpDesconto().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getIdTpDesconto())) {
			if(addedAnd)
				hql.append(" and ");	
			
			hql.append(" descontosProdutoEdicao.tipoDesconto = :tipoDesconto ");
			addedAnd = true;
		}
		
		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null && !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty() && filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			if(addedAnd)
				hql.append(" and ");	
			
			hql.append(" financeiro.id = :idVrMinimo ");
			addedAnd = true;
		}
		
		
		
		
		if (filtroAlteracaoCotaDTO.getPaginacao().getSortOrder() != null && filtroAlteracaoCotaDTO.getPaginacao().getSortOrder() != null) {
			hql.append(" order by ").append(filtroAlteracaoCotaDTO.getPaginacao().getSortOrder()).append(" ").append(filtroAlteracaoCotaDTO.getPaginacao().getOrdenacao().getOrdenacao());
		}
		
		Query query = super.getSession().createQuery(hql.toString());

		if (filtroAlteracaoCotaDTO.getNumeroCota() != null && filtroAlteracaoCotaDTO.getNumeroCota()>0) {
			query.setParameter("numeroCota", filtroAlteracaoCotaDTO.getNumeroCota());
		}
		
		if (filtroAlteracaoCotaDTO.getNomeCota() != null && !filtroAlteracaoCotaDTO.getNomeCota().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getNomeCota())) {
			query.setParameter("nomeCota", filtroAlteracaoCotaDTO.getNomeCota().toUpperCase() +"%" );
		}
		
		if (filtroAlteracaoCotaDTO.getIdBairro() != null && filtroAlteracaoCotaDTO.getIdBairro()>0) {
			query.setParameter("idBairro", filtroAlteracaoCotaDTO.getIdBairro());
		}
		
		if (filtroAlteracaoCotaDTO.getIdMunicipio() != null && !filtroAlteracaoCotaDTO.getIdMunicipio().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getIdMunicipio())) {
			query.setParameter("idMunicipio", filtroAlteracaoCotaDTO.getIdMunicipio().toUpperCase() +"%" );
		}
		
		if (filtroAlteracaoCotaDTO.getIdFornecedor() != null && filtroAlteracaoCotaDTO.getIdFornecedor() >0) {
			query.setParameter("idFornecedor", filtroAlteracaoCotaDTO.getIdFornecedor());
		}
		
		if (filtroAlteracaoCotaDTO.getIdTpDesconto() != null && !filtroAlteracaoCotaDTO.getIdTpDesconto().isEmpty() && !"-1".equals(filtroAlteracaoCotaDTO.getIdTpDesconto())) {
			query.setParameter("tipoDesconto", filtroAlteracaoCotaDTO.getIdTpDesconto());
		}
		
		if (filtroAlteracaoCotaDTO.getIdVrMinimo() != null && !filtroAlteracaoCotaDTO.getIdVrMinimo().toString().isEmpty() && filtroAlteracaoCotaDTO.getIdVrMinimo().doubleValue() > 0) {
			query.setParameter("idVrMinimo", filtroAlteracaoCotaDTO.getIdVrMinimo());
		}
		
		return query.list();
	}

	
}