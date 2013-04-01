package br.com.abril.nds.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.service.EstudoService;

/**
 * Classe de implementação de serviços referentes a entidade {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 * 
 */
@Service
public class EstudoServiceImpl implements EstudoService {

    @Autowired
    private EstudoRepository estudoRepository;

    @Autowired
    private EstudoCotaRepository estudoCotaRepository;

    @Transactional(readOnly = true)
    public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {

	return this.estudoRepository.obterEstudoDoLancamentoPorDataProdutoEdicao(dataReferencia, idProdutoEdicao);
    }

    @Transactional(readOnly = true)
    @Override
    public Estudo obterEstudo(Long id) {
	return this.estudoRepository.buscarPorId(id);
    }

    @Override
    @Transactional
    public void excluirEstudosAnoPassado() {

	Calendar c = Calendar.getInstance();

	c.set(Calendar.HOUR_OF_DAY, 0);
	c.set(Calendar.MINUTE, 0);
	c.set(Calendar.SECOND, 0);
	c.set(Calendar.DAY_OF_MONTH, 1);
	c.set(Calendar.MONTH, Calendar.JANUARY);
	c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);

	Date dataStart = c.getTime();

	c.set(Calendar.HOUR_OF_DAY, 23);
	c.set(Calendar.MINUTE, 59);
	c.set(Calendar.SECOND, 59);
	c.set(Calendar.MONTH, Calendar.DECEMBER);
	c.set(Calendar.DAY_OF_MONTH, 31);

	Date dataEnd = c.getTime();

	System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(dataStart));
	System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(dataEnd));

	List<Estudo> listEstudos = estudoRepository.obterEstudosPorIntervaloData(dataStart, dataEnd);

	for (Estudo estudo : listEstudos) {

	    try {

		estudoRepository.remover(estudo);
	    } catch (Exception e) {

		System.out.println("Erro ao excluir estudo:" + estudo.getId());
		e.printStackTrace();
	    }

	}
    }

    @Override
    @Transactional
    public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId) {
	return estudoRepository.obterResumoEstudo(estudoId);
    }

    @Override
    @Transactional
    public void excluirEstudo(long id) {
	this.estudoRepository.removerPorId(id);
    }

    @Transactional(readOnly = true)
    public Estudo obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO) {

	return this.estudoRepository.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudoVO);
    }

    @Transactional(readOnly = true)
    public Long obterMaxId() {
	return this.estudoRepository.obterMaxId();
    }

    @Transactional
    public List<Long> salvar(List<Estudo> listEstudo) {

	List<Long> listIdEstudoAdicionado = null;

	if (listEstudo != null && !listEstudo.isEmpty()) {

	    listIdEstudoAdicionado = new ArrayList<Long>();

	    int iEstudo = 0;
	    while (iEstudo < listEstudo.size()) {

		Estudo estudo = listEstudo.get(iEstudo);

		Hibernate.initialize(estudo.getEstudoCotas());

		Iterator<EstudoCota> itEstudoCota = estudo.getEstudoCotas().iterator();

		Set<EstudoCota> setEstudoCota = new HashSet<EstudoCota>();

		while (itEstudoCota.hasNext()) {

		    EstudoCota estudoCota = itEstudoCota.next().clone();
		    estudoCota.setId(null);
		    estudoCota.setEstudo(estudo);

		    setEstudoCota.add(estudoCota);
		}

		estudo.setEstudoCotas(setEstudoCota);

		Long idEstudo = this.estudoRepository.adicionar(estudo);

		listIdEstudoAdicionado.add(idEstudo);

		iEstudo++;
	    }
	}

	return listIdEstudoAdicionado;
    }

}
