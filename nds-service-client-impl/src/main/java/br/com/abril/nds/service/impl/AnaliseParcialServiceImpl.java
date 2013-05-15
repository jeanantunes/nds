package br.com.abril.nds.service.impl;

import java.util.*;

import br.com.abril.nds.model.estudo.ClassificacaoCota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliseEstudoDetalhesDTO;
import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.AnaliseParcialRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.service.AnaliseParcialService;

@Service
public class AnaliseParcialServiceImpl implements AnaliseParcialService {

    @Autowired
    private AnaliseParcialRepository analiseParcialRepository;

    @Autowired
    private EstudoRepository estudoRepository;

    private static final Logger log = LoggerFactory.getLogger(AnaliseParcialServiceImpl.class);
    private Map<String, String> mapClassificacaoCota;

    @Override
    @Transactional
    public EstudoCota buscarPorId(Long id) {
        EstudoCota estudo = new EstudoCota();
        estudo.setEstudo(estudoRepository.buscarPorId(id));
        return estudo;
    }

    @Override
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId) {
        return analiseParcialRepository.carregarEdicoesBaseEstudo(estudoId);
    }

    @Override
    public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {
        List<AnaliseParcialDTO> lista = analiseParcialRepository.buscaAnaliseParcialPorEstudo(queryDTO);
        if (queryDTO.getModoAnalise() != null && queryDTO.getModoAnalise().equalsIgnoreCase("PARCIAL")) {
            for (AnaliseParcialDTO item : lista) {
                List<EdicoesProdutosDTO> temp = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    temp.addAll(analiseParcialRepository.getEdicoesBaseParciais(new Long(item.getCota()), queryDTO.getNumeroEdicao(), queryDTO.getCodigoProduto(), new Long(i)));
                }
                item.setEdicoesBase(temp);
            }
        } else {
            if (queryDTO.getEdicoesBase() == null) {
                queryDTO.setEdicoesBase(analiseParcialRepository.carregarEdicoesBaseEstudo(new Long(queryDTO.getEstudoId())));
            }
            for (AnaliseParcialDTO item : lista) {
                List<Long> idsProdutoEdicao = new ArrayList<>();
                List<EdicoesProdutosDTO> edicoesComVenda = new ArrayList<>();
                for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                    idsProdutoEdicao.add(edicao.getProdutoEdicaoId());
                }
                item.setEdicoesBase(new LinkedList<EdicoesProdutosDTO>());
                edicoesComVenda.addAll(analiseParcialRepository.getEdicoesBase(new Long(item.getCota()), idsProdutoEdicao));
                for (EdicoesProdutosDTO edicao : queryDTO.getEdicoesBase()) {
                    for (EdicoesProdutosDTO ed : edicoesComVenda) {
                        if (ed.getProdutoEdicaoId().equals(edicao.getProdutoEdicaoId())) {
                            BeanUtils.copyProperties(edicao, ed, new String[] {"reparte", "venda"});
                            item.getEdicoesBase().add(ed);
                        }
                    }
                }
            }
        }
        return lista;
    }

    @Override
    public void atualizaReparte(Long estudoId, Long numeroCota, Long reparte) {
        analiseParcialRepository.atualizaReparteCota(estudoId, numeroCota, reparte);
        analiseParcialRepository.atualizaReparteEstudo(estudoId, reparte);
    }

    @Override
    public List<PdvDTO> carregarDetalhesCota(Long numeroCota) {
        return analiseParcialRepository.carregarDetalhesCota(numeroCota);
    }

    @Override
    public void liberar(Long id) {
        analiseParcialRepository.liberar(id);
    }

    @Override
    public List<AnaliseEstudoDetalhesDTO> buscarDetalhesAnalise(ProdutoEdicao produtoEdicao) {
        return analiseParcialRepository.buscarDetalhesAnalise(produtoEdicao);
    }

    @Override
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
        List<CotaQueNaoEntrouNoEstudoDTO> cotaQueNaoEntrouNoEstudoDTOList = analiseParcialRepository.buscarCotasQueNaoEntraramNoEstudo(queryDTO);
        for (CotaQueNaoEntrouNoEstudoDTO cotaQueNaoEntrouNoEstudoDTO : cotaQueNaoEntrouNoEstudoDTOList) {
            cotaQueNaoEntrouNoEstudoDTO.setMotivo(traduzMotivo(cotaQueNaoEntrouNoEstudoDTO.getMotivo()));
        }
        return cotaQueNaoEntrouNoEstudoDTOList;
    }

    private String traduzMotivo(String motivo) {
        if (mapClassificacaoCota == null) {
            populaMapClassificacaoCota();
        }
        return mapClassificacaoCota.get(motivo);
    }

    private void populaMapClassificacaoCota() {
        mapClassificacaoCota = new HashMap<>();
        for (ClassificacaoCota classificacaoCota : ClassificacaoCota.values()) {
            mapClassificacaoCota.put(classificacaoCota.getCodigo(), classificacaoCota.getTexto());
        }
    }
}
