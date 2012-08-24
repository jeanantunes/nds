var OperacaoDiferenciadaController = $.extend(true, {
		
	grupoSelecionado : null,
	
	grupos : [],
	
	init : function() {
		OperacaoDiferenciadaController.inicializarGrids();
	},
	
	processaRetornoPesquisa : function(result) {
		
		OperacaoDiferenciadaController.grupos = [];
		
		$.each(result.rows, function(index,row){
			OperacaoDiferenciadaController.grupos.push(row.cell);
			OperacaoDiferenciadaController.gerarAcao(index,row);
		} );
				
		return result;
	},
	
	gerarAcao : function(index,row) {
				
		row.cell.acao = 
			'<a href="javascript:;" onclick="OperacaoDiferenciadaController.editarGrupo(' + index + ');">' +
			'<img src="' + contextPath + '/images/ico_editar.gif" border="0" alt="Editar" hspace="5" />' +
			'</a>' +
			
			'<a href="javascript:;" onclick="OperacaoDiferenciadaController.dialogExcluirGrupo(' + index + ');">' +
			'<img src="' + contextPath + '/images/ico_excluir.gif" border="0" alt="Excluir" />' +
			'</a>';
	},
	
	editarGrupo : function(index) {
		
		this.grupoSelecionado = this.grupos[index];
		
		var data = [];	
		
		data.push({name:'idGrupo',	value: OperacaoDiferenciadaController.grupoSelecionado.idGrupo });
		data.push({name:'tipoGrupo',	value: OperacaoDiferenciadaController.grupoSelecionado.tipoGrupo });
				
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/carregarSelecoes', data, OperacaoDiferenciadaController.editarPorTipo);	
		
	},
	
	editarPorTipo : function(result) {
		
		if(OperacaoDiferenciadaController.grupoSelecionado.tipoGrupo == 'TIPO_COTA') {
			$('#radioTipoCota', this.workspace).prop('checked', true);
			$('#comboTipoCota', this.workspace).val(OperacaoDiferenciadaController.grupoSelecionado.tipoCota);
			OperacaoDiferenciadaController.carregarTipoCota();
			
		} else if (OperacaoDiferenciadaController.grupoSelecionado.tipoGrupo == 'MUNICIPIO') {
			$('#radioMunicipios', this.workspace).prop('checked', true);
			OperacaoDiferenciadaController.carregarMunicipios();
		}
		OperacaoDiferenciadaController.dialogDetalhesGrupo();
	},
	
	salvarGrupo : function() {
		
		if( this.grupoSelecionado ) {
			$('#nomeDiferenca').val(this.grupoSelecionado.nome);
			$('#diaSemana').val(this.grupoSelecionado.diasSemana);
		}
		
		OperacaoDiferenciadaController.dialogConfirmarGrupo();
	},
	
	limparSelecoes : function(callback) {
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/limparSelecoes',
				null,
				callback);		
	},
	
	carregarMunicipios : function() {
				
		$('.selecionarMunicipio', this.workspace).show();
		$('.selecionarCotas', this.workspace).hide();
		
		$('[name=selecao]').attr('name','selecaoInativo');

		var data = [];
		
		if(OperacaoDiferenciadaController.grupoSelecionado) {
			data.push({name:'idGrupo',		value: OperacaoDiferenciadaController.grupoSelecionado.idGrupo });
		}
		
		$(".selMunicipiosGrid", this.workspace).flexOptions({ params:data });		
		$(".selMunicipiosGrid", this.workspace).flexReload();
	},
	
	selecionarPorTipoCota : function() {
		$('#comboTipoCota', this.workspace).val("");
		$('#comboTipoCota', this.workspace).show();
		$('.selecionarMunicipio', this.workspace).hide();
		$('#selectCota', this.workspace).hide();
	},
	
	selecionarPorMunicipio : function() {
		this.limparSelecoes(OperacaoDiferenciadaController.carregarMunicipios);
	},
	
	carregarTipoCota : function() {
				
		$('.selecionarCotas', this.workspace).show();
		$('.selecionarMunicipio', this.workspace).hide();
		
		$('[name=selecao]').attr('name','selecaoInativo');
		
		var data = [];	
		
		if(OperacaoDiferenciadaController.grupoSelecionado) {
			data.push({name:'idGrupo',		value: OperacaoDiferenciadaController.grupoSelecionado.idGrupo });
		}
		
		data.push({name:'tipoCota',		value: $('#comboTipoCota', this.workspace).val() });
		
		$(".selCotasGrid", this.workspace).flexOptions({ params:data });		
		$(".selCotasGrid", this.workspace).flexReload();
	},
	
	novoGrupo : function() {
		
		this.grupoSelecionado = null;
		
		$('#comboTipoCota', this.workspace).hide();
		$('#radioTipoCota', this.workspace).prop('checked', false);
		$('#radioMunicipios', this.workspace).prop('checked', false);
		$('#comboTipoCota', this.workspace).val('');
		
		$('.selecionarCotas', this.workspace).hide();
		$('.selecionarMunicipio', this.workspace).hide();
		
		this.dialogDetalhesGrupo();
	},
		
	excluirGrupo : function(index) {
		
		var data = [];		
		data.push({name:'idGrupo',		value: this.grupos[index].idGrupo });

		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/excluirGrupo',
				data,
				function(result){										
					$(".gruposGrid", this.workspace).flexReload();
					exibirMensagem('SUCCESS', ['Grupo excluido com sucesso.']);										
				});		
		
		$( "#dialog-confirm-grupo" , this.workspace).dialog( "close" );
		
	},

	confirmarGrupo : function() {

		var data = [];
		
		data.push({name: "nomeDiferenca", value: $("#nomeDiferenca", this.workspace).val()});
		
		$.each($("#diaSemana", this.workspace).val(), 
			function(index, val) {
				
				data.push({name: "diasSemana", value: val});
			}
		);
		
		$.postJSON(contextPath + "/administracao/parametrosDistribuidor/cadastrarOperacaoDiferenciada", data,
			function(result){
				
				$( this, this.workspace ).dialog( "close" );
			}, null, true
		);
	},
	
	processaMunicipios : function(result) {
		
		$.each(result.rows, function(index, row) {
			row.cell.selecionado='<input type="checkbox" name="selecao" ' +
			'id="'+row.cell.id+'"' +
			(row.cell.selecionado == true ? 'checked="checked"' : '') +
			'onclick="OperacaoDiferenciadaController.adicionarSelecaoMunicipio('+row.cell.id+',this);"' +
			'>';
		});
				
		return result;
	},
	
	adicionarSelecaoMunicipio : function(id, check) {
		
		if(check.checked==false) {
			$("#selecionarTodosID").attr("checked",false);
		}
		
		var dados = [];		
		dados.push({ name : 'idMunicipio', value: id});		
		dados.push({ name : 'selecionado', value: check.checked });
		
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/selecionarMunicipio', 
				dados);		
	},
	
	adicionarSelecaoCota : function(id, check) {
		
		if(check.checked==false) {
			$("#selecionarTodosID").attr("checked",false);
		}
		
		var dados = [];		
		dados.push({ name : 'idCota', value: id});		
		dados.push({ name : 'selecionado', value: check.checked });
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/selecionarCota', 
				dados);		
	},
	
	selecionarTodosMunicipios : function(elementoCheck) {
		
		var dados = [];
		
		$.each( $('[name=selecao]'), function(index, item) { 
		    $(item).attr('checked', elementoCheck.checked);
		    dados.push({ name : 'selecionados[' + index + ']', value: $(item).attr('id') });
		});
		
		dados.push({ name : 'selecionado', value: elementoCheck.checked });
		
		debugger;
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/selecionarTodosMunicipios', dados);
		
	},
	
	selecionarTodasCotas : function(elementoCheck) {
		
		var dados = [];
		
		$.each( $('[name=selecao]'), function(index, item) { 
		    $(item).attr('checked', elementoCheck.checked);
		    dados.push({ name : 'selecionados[' + index + ']', value: $(item).attr('idcota') });
		});
		
		dados.push({ name : 'selecionado', value: elementoCheck.checked });
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/selecionarTodasCotas', dados);		
	},
	
	processaCotas : function(result) {
		
		$.each(result.rows, function(index, row) {
			row.cell.selecionado='<input name="selecao" type="checkbox" ' +
			'idcota="'+row.cell.idCota+'"' +
			(row.cell.selecionado == true ? 'checked="checked"' : '') +
			'onclick="OperacaoDiferenciadaController.adicionarSelecaoCota('+row.cell.idCota+',this);"' +
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
	set : function(campo,value) {
				
		var elemento = $("#" + campo, this.workspace);
		
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
	get : function(campo) {
		
		var elemento = $("#" + campo, this.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	dialogConfirmarGrupo : function() {
		
		$( "#dialog-salvar", OperacaoDiferenciadaController.workspace  ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Confirmar": function() {
					debugger;
					var data = [];
					
					data.push({name: "nome", value: $("#nomeDiferenca", this.workspace ).val()});
					
					if(OperacaoDiferenciadaController.grupoSelecionado)
						data.push({name: "idGrupo", value: OperacaoDiferenciadaController.grupoSelecionado.idGrupo});
					
					$.each($("#diaSemana", this.workspace ).val(), 
						function(index, val) {							
							data.push({name: "diasSemana", value: val});
						}
					);
										
					$.postJSON(contextPath + "/administracao/parametrosDistribuidor/cadastrarOperacaoDiferenciada", data,
						function(result){
							
						$(".gruposGrid", OperacaoDiferenciadaController.workspace).flexReload();
						$( "#dialog-novo-grupo", OperacaoDiferenciadaController.workspace ).dialog( "close" );
						$( "#dialog-salvar", OperacaoDiferenciadaController.workspace ).dialog( "close" );

						exibirMensagem('SUCCESS', ['Grupo salvo com sucesso.']);	
						}, null, true
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-confirm-grupo", this.workspace).parents("form")
		});
	},
	
	dialogDetalhesGrupo : function() {
		
		$( "#dialog-novo-grupo", this.workspace ).dialog({
			resizable: false,
			height:500,
			width:750,
			modal: true,
			buttons: {
				"Confirmar": function() {
					OperacaoDiferenciadaController.salvarGrupo();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-novo-grupo", this.workspace).parents("form")
		});

	},

	dialogExcluirGrupo : function(index) {
		
		$( "#dialog-confirm-grupo", this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					OperacaoDiferenciadaController.excluirGrupo(index);				
				},
				"Cancelar": function() {
					$( this, this.workspace ).dialog( "close" );
				}
			},
			form: $("#dialog-confirm-grupo", this.workspace).parents("form")
		});
	},
	
	inicializarGrids : function() {
		$(".selMunicipiosGrid", this.workspace).flexigrid({
			autoload : false,
			url : contextPath + '/administracao/parametrosDistribuidor/obterMunicipios',
			dataType : 'json',
			preProcess: OperacaoDiferenciadaController.processaMunicipios,
			colModel : [ {
				display : 'Municipio',
				name : 'municipio',
				width : 525,
				sortable : true,
				align : 'left'
			},{
				display : 'Qtde Cotas',
				name : 'qtde',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'selecionado',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "municipio",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			width : 700,
			height : 150
		});
	
	
	$(".selCotasGrid", this.workspace).flexigrid({
			autoload : false,
			url : contextPath + '/administracao/parametrosDistribuidor/obterCotas',
			dataType : 'json',
			preProcess: OperacaoDiferenciadaController.processaCotas,
			colModel : [ {
				display : 'Cota',
				name : 'numCota',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 300,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'selecionado',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "numCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			width : 700,
			height : 150
		});
	

		$(".gruposGrid", this.workspace).flexigrid({
			autoload : true,
			url : contextPath + '/administracao/parametrosDistribuidor/obterGrupos',
			dataType : 'json',
			preProcess: OperacaoDiferenciadaController.processaRetornoPesquisa,
			colModel : [ {
				display : 'Nome',
				name : 'nome',
				width : 500,
				sortable : true,
				align : 'left'
			},{
				display : 'Recolhimento',
				name : 'recolhimento',
				width : 180,
				sortable : true,
				align : 'LEFT'
			},{
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 800,
			height : 150
		});
	}
		
}, BaseController);

$(function() {
	OperacaoDiferenciadaController.init();
				
});
