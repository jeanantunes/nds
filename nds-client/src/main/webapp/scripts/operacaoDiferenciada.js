function OperacaoDiferenciada() {
	
	var T = this;
	
	this.grupos = []; 
		
	this.processaRetornoPesquisa = function(result) {
		
		$.each(result.rows, function(index,row){
			T.grupos.push(row.cell);
			T.gerarAcao(index,row);
		} );
				
		return result;
	},
	
	this.gerarAcao = function(index,row) {
				
		row.cell.acao = 
			'<a href="javascript:;" onclick="dialogEditarGrupo(' + index + ');">' +
			'<img src="' + contextPath + '/images/ico_editar.gif" border="0" alt="Editar" hspace="5" />' +
			'</a>' +
			
			'<a href="javascript:;" onclick="dialogExcluirGrupo(' + index + ');">' +
			'<img src="' + contextPath + '/images/ico_excluir.gif" border="0" alt="Excluir" />' +
			'</a>';
	},
	
	this.editarGrupo = function(index) {
		alert('editar ' + index);
	},
		
	this.excluirGrupo = function(index) {
		
		var data = [];		
		data.push({name:'idGrupo',		value: T.grupos[index].idGrupo });

		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/excluirGrupo',
				data,
				function(result){										
					$(".gruposGrid").flexReload();
					exibirMensagem('SUCCESS', ['Grupo excluido com sucesso.']);										
				});		
		
		$( "#dialog-confirm-grupo" ).dialog( "close" );
		
	},
	
	this.incluirGrupo = function() {
		alert('incluir ' + index);
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
		
	};
	
}
