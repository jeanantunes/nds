package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NotaFiscalNdsRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;

@Repository
public class NotaFiscalNdsRepositoryImpl extends AbstractRepositoryModel<NotaFiscalNds, Long> implements NotaFiscalNdsRepository {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	public NotaFiscalNdsRepositoryImpl() {
		super(NotaFiscalNds.class);
	}	
	
}
