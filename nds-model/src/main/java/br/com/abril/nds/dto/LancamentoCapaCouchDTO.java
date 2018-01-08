package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class LancamentoCapaCouchDTO implements Serializable{

    private static final long serialVersionUID = 2673570412029969558L;

    private String _id;

    private String _rev;

    private List<LancamentoCapaDetalheCouchDTO> lancamentoCapaDetalheCouchDTO;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public List<LancamentoCapaDetalheCouchDTO> getLancamentoCapaDetalheCouchDTO() {
        return lancamentoCapaDetalheCouchDTO;
    }

    public void setLancamentoCapaDetalheCouchDTO(List<LancamentoCapaDetalheCouchDTO> lancamentoCapaDetalheCouchDTO) {
        this.lancamentoCapaDetalheCouchDTO = lancamentoCapaDetalheCouchDTO;
    }
}
