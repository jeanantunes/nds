function EmissaoCE(pathTela,obj) {
	
	var T = this;
	
	this.emissoes = []; 
	this.fornecedoresSelecionados = [];
	
	this.cliquePesquisar = function() {
				
		var data = [];
		
		data.push({name:'filtro.dtRecolhimentoDe',		value: T.get("dataDe")});
		data.push({name:'filtro.dtRecolhimentoAte',		value: T.get("dataAte")});
		data.push({name:'filtro.idBoxDe',				value: T.get("boxDe")});
		data.push({name:'filtro.idBoxAte',				value: T.get("boxAte")});
		data.push({name:'filtro.numCotaDe',				value: T.get("cotaDe")});
		data.push({name:'filtro.numCotaAte',			value: T.get("cotaAte")});
		data.push({name:'filtro.idRoteiro',				value: T.get("roteiro")});
		data.push({name:'filtro.idRota',				value: T.get("rota")});
		data.push({name:'filtro.capa',					value: T.get("capa")});
		data.push({name:'filtro.personalizada',			value: T.get("personalizada")});
		
		$.each(T.fornecedoresSelecionados, function(index, row) {
			data.push({name:'filtro.fornecedores[' + index + ']',	value: row.id});
		});
		
		$(".ceEmissaoGrid").flexOptions({			
			url : pathTela + "/emissaoCE/pesquisar",
			dataType : 'json',
			params:data,
			preProcess: T.processaRetornoPesquisa
		});
		
		$(".ceEmissaoGrid").flexReload();
		
	},
	
	this.processaRetornoPesquisa = function(result) {
				
		return result;
	},
	
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = $("#" + campo);
		
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
		
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	this.gerarFornecedoresSelecionados = function() {
		
		var cellFornecedores = '';
				
		$.each(T.fornecedoresSelecionados, function(index,fornecedor){
			
			cellFornecedores += '<div class="forncedoresSel">' +
								   '<label>'+ fornecedor.nome +'</label>' +
								   '<a href="javascript:;" onclick="ECE.removerFornecedor('+index+');">' +
								   		'<img src="'+ pathTela +'/images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" />' +
								   '</a>' +
							   '</div>';
		});
		
		$('#fornecedoresSelecionados').html(cellFornecedores);
		
	},
	
	this.getFornecedoresSelecionados = function() {
		
		$.each( $('#selectFornecedores').val(), function(index, row) {
			T.fornecedoresSelecionados.push({id: row.split('_')[0] , nome: row.split('_')[1]});
		});
	},
	
	this.removerFornecedor=function(index) {
		T.fornecedoresSelecionados.splice(index,1);
		T.gerarFornecedoresSelecionados();
	};
	
}
