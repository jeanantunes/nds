function Distribuicao(tela) {
	
	var D = this;
	
	this.getDados = function() {
	
		var data = [];
		
		data.push({name:'distribuicao.numCota',				value: D.get("numCota")});
		data.push({name:'distribuicao.qtdePDV',				value: D.get("qtdePDV")});
		data.push({name:'distribuicao.box',					value: D.get("box")});
		data.push({name:'distribuicao.assistComercial',		value: D.get("assistComercial")});
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
				
		return data;
	},
	
	this.setDados = function(dto) {
		
		D.set('numCota',dto.numCota);
		D.set('qtdePDV',dto.qtdePDV);
		D.set('box',dto.box);
		D.set('assistComercial',dto.assistComercial);
		D.set('tipoEntrega',dto.tipoEntrega);
		D.set('arrendatario',dto.arrendatario);
		D.set('observacao',dto.observacao);
		D.set('repPorPontoVenda',dto.repPorPontoVenda);
		D.set('solNumAtras',dto.solNumAtras);
		D.set('recebeRecolhe',dto.recebeRecolhe);
		D.set('neImpresso',dto.neImpresso);
		D.set('neEmail',dto.neEmail);
		D.set('ceImpresso',dto.ceImpresso);
		D.set('ceEmail',dto.ceEmail);
		D.set('slipImpresso',dto.slipImpresso);
		D.set('slipEmail',dto.slipEmail);
		
		if(dto.tiposEntrega)
			D.montarComboTipoEntrega(dto.tiposEntrega);
		
		D.$('qtdePDV').attr('disabled', dto.qtdeAutomatica?'disabled':'');
			
		
	},
	this.limparDados = function() {
		
		D.setDados(null);
	},
	
	this.carregarDadosDistribuicaoCota = function(idCota) {
		
		$.postJSON(contextPath + "/cadastro/cota/carregarDistribuicaoCota",
				"idCota=" + idCota ,
				function(result) {
					D.setDados(result);
				},
				null, true); 
	},
	
	
	this.montarComboTipoEntrega = function(itensDTO) {
		
		var combo =  montarComboBox(itensDTO, false);
		D.$("tipoEntrega").html(combo);
		D.$("tipoEntrega").sortOptions();
	},
	
	this.set = function(campo,value) {
		
		value = (value)? value : '';
		
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			elemento.attr('checked', (value) ? 'checked':'');
		} else {
			elemento.val(value);
		}
	},
	
	this.get = function(campo) {
		
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	this.$ = function(campo) {
		
		 return $("#" + tela + campo);
	};
	
	$(function() {
		D.$("numCota").numeric();
		D.$("qtdePDV").numeric();
	});
}
