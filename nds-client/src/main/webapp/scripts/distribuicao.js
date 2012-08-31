function Distribuicao(tela) {
	
	var D = this;
	
	/**
	 * Envia dados da tela para serem salvos no controller
	 * 
	 * @return false - define que a não sairá da tela, responsabilidade deixada para o método de callback
	 */
	this.salvar = function(callback) {
				
		$.postJSON(contextPath + "/cadastro/cota/salvarDistribuicaoCota",
				D.getDados(),
				callback,
				null, true);
		
		return false;		
	},
		
	/**
	 * Retorna todos os dados da tela no padrão utilizado pelo VRaptor
	 * @return Espelho de DistribuicaoDTO (br.com.abril.nds.dto) 
	 */
	this.getDados = function() {
	
		var data = [];
		
		data.push({name:'distribuicao.numCota',				value: D.get("numCota")});
		data.push({name:'distribuicao.qtdePDV',				value: D.get("qtdePDV")});
		data.push({name:'distribuicao.box',					value: D.get("box")});
		data.push({name:'distribuicao.assistComercial',		value: D.get("assistComercial")});
		data.push({name:'distribuicao.gerenteComercial',    value: D.get("gerenteComercial")});
		data.push({name:'distribuicao.tipoEntrega',			value: D.get("tipoEntrega")});
		data.push({name:'distribuicao.observacao',			value: D.get("observacao")});
		data.push({name:'distribuicao.arrendatario',		value: D.get("arrendatario")});
		data.push({name:'distribuicao.repPorPontoVenda',	value: D.get("repPorPontoVenda")});
		data.push({name:'distribuicao.solNumAtras',			value: D.get("solNumAtras")});
		data.push({name:'distribuicao.recebeRecolhe',		value: D.get("recebeRecolhe")});
		data.push({name:'distribuicao.neImpresso',			value: D.get("neImpresso")});
		data.push({name:'distribuicao.neEmail',				value: D.get("neEmail")});
		data.push({name:'distribuicao.ceImpresso',			value: D.get("ceImpresso")});
		data.push({name:'distribuicao.ceEmail',				value: D.get("ceEmail")});
		data.push({name:'distribuicao.slipImpresso',		value: D.get("slipImpresso")});
		data.push({name:'distribuicao.slipEmail',			value: D.get("slipEmail")});
		data.push({name:'distribuicao.boletoImpresso',		value: D.get("boletoImpresso")});
		data.push({name:'distribuicao.boletoEmail',			value: D.get("boletoEmail")});
		data.push({name:'distribuicao.boletoSlipImpresso',	value: D.get("boletoSlipImpresso")});
		data.push({name:'distribuicao.boletoSlipEmail',		value: D.get("boletoSlipEmail")});
		data.push({name:'distribuicao.reciboImpresso',		value: D.get("reciboImpresso")});
		data.push({name:'distribuicao.reciboEmail',			value: D.get("reciboEmail")});
				
		return data;
	},
	
	/**
	 * Preenche a tela
	 * 
	 * @param dto - objeto Espelho de DistribuicaoDTO esperado (br.com.abril.nds.dto) 
	 */
	this.setDados = function(dto) {
				
		if(dto.tiposEntrega)
			D.montarComboTipoEntrega(dto.tiposEntrega);
		
		D.set('numCota',			dto.numCota);
		D.set('qtdePDV',			dto.qtdePDV ? dto.qtdePDV.toString() : '' );
		D.set('box',				dto.box);
		D.set('assistComercial',	dto.assistComercial);
		D.set('gerenteComercial',	dto.gerenteComercial);
		D.set('tipoEntrega',		dto.tipoEntrega);
		D.set('arrendatario',		dto.arrendatario);
		D.set('observacao',			dto.observacao);
		D.set('repPorPontoVenda',	dto.repPorPontoVenda);
		D.set('solNumAtras',		dto.solNumAtras);
		D.set('recebeRecolhe',		dto.recebeRecolhe);
		D.set('neImpresso',			dto.neImpresso);
		D.set('neEmail',			dto.neEmail);
		D.set('ceImpresso',			dto.ceImpresso);
		D.set('ceEmail',			dto.ceEmail);
		D.set('slipImpresso',		dto.slipImpresso);
		D.set('slipEmail',			dto.slipEmail);
		D.set('boletoImpresso',		dto.boletoImpresso);
		D.set('boletoEmail',		dto.boletoEmail);
		D.set('boletoSlipImpresso',	dto.boletoSlipImpresso);
		D.set('boletoSlipEmail',	dto.boletoSlipEmail);
		D.set('reciboImpresso',		dto.reciboImpresso);
		D.set('reciboEmail',		dto.reciboEmail);		
		
		if(dto.qtdeAutomatica) {
			D.$('qtdePDV').attr('disabled','disabled');
		} else {
			D.$('qtdePDV').removeAttr('disabled');
		}			
		
	},
	
	/**
	 * Limpa campos da tela
	 */
	this.limparDados = function() {
		
		D.setDados(null);
	},
	
	/**
	 * Busca no banco os dados de Distribuição da Cota e preenche a tela
	 * @param idCota - Código da cota
	 */
	this.carregarDadosDistribuicaoCota = function(idCota) {
		
		$.postJSON(contextPath + "/cadastro/cota/carregarDistribuicaoCota",
				"idCota=" + idCota ,
				function(result) {
					D.setDados(result);
				},
				null, true); 
	},
	
	/**
	 * Gera combo de de Tipo de Entrega
	 * @param itensDTO - objeto espelho de ItemDTO (br.com.abril.nds.dto)
	 */
	this.montarComboTipoEntrega = function(itensDTO) {
		
		itensDTO.splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": ""}});
		
		var combo =  montarComboBox(itensDTO, false);
		
		D.$("tipoEntrega").html(combo);
		D.$("tipoEntrega").sortOptions();
	},
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	this.get = function(campo) {
		
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	/**
	 * Obtém elemento da tela
	 */
	this.$ = function(campo) {
		
		 return $("#" + tela + campo);
	},
	
	this.imprimeProcuracao = function(){
		
	    document.location.assign(contextPath + "/cadastro/cota/imprimeProcuracao?numeroCota="+D.get("numCota"));
	};
	
	$(function() {
		D.$("numCota").numeric();
		D.$("qtdePDV").numeric();
	});
}
