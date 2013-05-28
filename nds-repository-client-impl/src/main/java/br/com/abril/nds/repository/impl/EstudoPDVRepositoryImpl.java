package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.planejamento.EstudoPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoPDVRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 27/05/13
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class EstudoPDVRepositoryImpl extends AbstractRepositoryModel<EstudoPDV, Long> implements EstudoPDVRepository {

    public EstudoPDVRepositoryImpl() {
        super(EstudoPDV.class);
    }
}
