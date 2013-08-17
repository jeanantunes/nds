package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoPDV;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 27/05/13
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public interface EstudoPDVRepository extends Repository<EstudoPDV, Long> {
    EstudoPDV buscarPorEstudoCotaPDV(Estudo estudo, Cota cota, PDV pdv);
}
