function OperacaoDiferenciada() {
	
	var T = this;
	
	this.grupoSelecionado = null;
	
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
			'<a href="javascript:;" onclick="OD.editarGrupo(' + index + ');">' +
			'<img src="' + contextPath + '/images/ico_editar.gif" border="0" alt="Editar" hspace="5" />' +
			'</a>' +
			
			'<a href="javascript:;" onclick="dialogExcluirGrupo(' + index + ');">' +
			'<img src="' + contextPath + '/images/ico_excluir.gif" border="0" alt="Excluir" />' +
			'</a>';
	},
	
	this.editarGrupo = function(index) {
		
		T.grupoSelecionado = T.grupos[index];
		
		if(T.grupoSelecionado.tipoGrupo == 'TIPO_COTA') {
			$('#radioTipoCota').prop('checked', true);
			$('#comboTipoCota').val(T.grupoSelecionado.tipoCota);
			T.carregarTipoCota();
			
		} else if (T.grupoSelecionado.tipoGrupo == 'MUNICIPIO') {
			$('#radioMunicipios').prop('checked', true);
			T.carregarMunicipios();
		}
		
		dialogDetalhesGrupo();
	},
	
	this.carregarMunicipios = function() {
				
		$('.selecionarMunicipio').show();
		$('.selecionarCotas').hide();

		var data = [];
		
		if(T.grupoSelecionado) {
			data.push({name:'idGrupo',		value: T.grupoSelecionado.idGrupo });
		}
		
		$(".selMunicipiosGrid").flexOptions({ params:data });		
		$(".selMunicipiosGrid").flexReload();
	},
	
	this.selecionarPorTipoCota = function() {
		$('#comboTipoCota').val("");
		T.carregarTipoCota();
	},
	
	this.carregarTipoCota = function() {
		
		$('.selecionarCotas').show();
		$('.selecionarMunicipio').hide();
		
		var data = [];	
		
		if(T.grupoSelecionado) {
			data.push({name:'idGrupo',		value: T.grupoSelecionado.idGrupo });
			data.push({name:'tipoCota',		value: T.grupoSelecionado.tipoCota });
		} else {
			data.push({name:'tipoCota',		value: $('#comboTipoCota').val() });
		}
		
		$(".selCotasGrid").flexOptions({ params:data });		
		$(".selCotasGrid").flexReload();
	},
	
	this.novoGrupo = function() {
		
		T.grupoSelecionado = null;
		
		$('#comboTipoCota').hide();
		$('#radioTipoCota').prop('checked', false);
		$('#radioMunicipios').prop('checked', false);
		$('#comboTipoCota').val('');
		
		dialogDetalhesGrupo();
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
	
	this.confirmarGrupo = function() {

		var data = [];
		
		data.push({name: "nomeDiferenca", value: $("#nomeDiferenca").val()});
		
		$.each($("#diaSemana").val(), 
			function(index, val) {
				
				data.push({name: "diasSemana", value: val});
			}
		);
		
		$.postJSON(contextPath + "/administracao/parametrosDistribuidor/cadastrarOperacaoDiferenciada", data,
			function(result){
				
				$( this ).dialog( "close" );
			}, null, true
		);
	},
	
	this.processaMunicipios = function(result) {
		
		$.each(result.rows, function(index, row) {
			row.cell.selecionado='<input name="input52" type="checkbox" ' +
			(row.cell.selecionado == 'true' ? 'checked="checked"' : '') + 
			'>';
		});
				
		return result;
	},
	
	this.processaCotas = function(result) {
		
		$.each(result.rows, function(index, row) {
			row.cell.selecionado='<input name="input52" type="checkbox" ' +
			(row.cell.selecionado == true ? 'checked="checked"' : '') + 
			'>';
		});
				
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
		
	};
	
}
