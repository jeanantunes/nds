var OperacaoDiferenciadaController = $.extend(true, {
		
	grupoSelecionado : null,
	
	grupos : [],
	
	init : function() {
		$( "#includeHistory", this.workspace ).button().click(function(event){
			var includeHistory = $(this).attr('checked') == 'checked';
			
			$(".gruposGrid", OperacaoDiferenciadaController.workspace).flexOptions({
				params : [{
					name : "includeHistory",
					value : includeHistory
				}]
			});
			$(".gruposGrid", OperacaoDiferenciadaController.workspace).flexReload();
			
			$(".gruposGrid", OperacaoDiferenciadaController.workspace).flexToggleCol(3,includeHistory);
			
			var label = (includeHistory)?'Ocultar Hist&oacute;ricos':'Exibir Hist&oacute;ricos';
			
			$(this ).button( "option", "label", label );
		});
		OperacaoDiferenciadaController.inicializarGrids();
	},
	
	processaRetornoPesquisa : function(result) {
		
		OperacaoDiferenciadaController.grupos = [];
		
		$.each(result.rows, function(index,row){
			OperacaoDiferenciadaController.grupos.push(row.cell);
			
			if(!row.cell.dataFimVigencia){
				row.cell.dataFimVigencia = ' - ';
				OperacaoDiferenciadaController.gerarAcao(index,row);
			}else{
				row.cell.acao = '';
			}
			
		} );
				
		return result;
	},
	
	gerarAcao : function(index,row) {
				
		row.cell.acao = 
			'<a isEdicao="true" href="javascript:;" onclick="OperacaoDiferenciadaController.editarGrupo(' + index + ');" style="margin-right:10px;">' +
				'<img src="' + contextPath + '/images/ico_editar.gif" border="0" alt="Editar" hspace="5" />' +
			'</a>' +
			'<a isEdicao="true" href="javascript:;" onclick="OperacaoDiferenciadaController.dialogExcluirGrupo(' + index + ');">' +
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
			$('#radioTipoCota', OperacaoDiferenciadaController.workspace).prop('checked', true);
			$('#comboTipoCota', OperacaoDiferenciadaController.workspace).val(OperacaoDiferenciadaController.grupoSelecionado.tipoCota);
			OperacaoDiferenciadaController.carregarTipoCota();
			
		} else if (OperacaoDiferenciadaController.grupoSelecionado.tipoGrupo == 'MUNICIPIO') {
			$('#radioMunicipios', OperacaoDiferenciadaController.workspace).prop('checked', true);
			OperacaoDiferenciadaController.carregarMunicipios();
		}
		OperacaoDiferenciadaController.dialogDetalhesGrupo();
	},
	
	salvarGrupo : function() {
		
		if( this.grupoSelecionado ) {
			$('#nomeDiferenca', OperacaoDiferenciadaController.workspace).val(this.grupoSelecionado.nome);
			$('#diaSemana', OperacaoDiferenciadaController.workspace).val(this.grupoSelecionado.diasSemana);
		}
		
		OperacaoDiferenciadaController.dialogConfirmarGrupo();
	},
	
	limparSelecoes : function(callback) {
		
	$("#selecionarTodosID", OperacaoDiferenciadaController.workspace).attr('checked', false);	
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/limparSelecoes',
				null,
				callback);		
	},
	
	carregarMunicipios : function() {
				
		$('.selecionarMunicipio', OperacaoDiferenciadaController.workspace).show();
		$('.selecionarCotas', OperacaoDiferenciadaController.workspace).hide();
		
		$('[name=selecao]', OperacaoDiferenciadaController.workspace).attr('name','selecaoInativo');

		var data = [];
		
		if(OperacaoDiferenciadaController.grupoSelecionado) {
			data.push({name:'idGrupo',		value: OperacaoDiferenciadaController.grupoSelecionado.idGrupo });
		}
		
		$(".selMunicipiosGrid", OperacaoDiferenciadaController.workspace).flexOptions({ params:data });		
		$(".selMunicipiosGrid", OperacaoDiferenciadaController.workspace).flexReload();
	},
	
	selecionarPorTipoCota : function() {
		$('#comboTipoCota', OperacaoDiferenciadaController.workspace).val("");
		$('#comboTipoCota', OperacaoDiferenciadaController.workspace).show();
		$('.selecionarMunicipio', OperacaoDiferenciadaController.workspace).hide();
		$('#selectCota', OperacaoDiferenciadaController.workspace).hide();
	},
	
	selecionarPorMunicipio : function() {
		this.limparSelecoes(OperacaoDiferenciadaController.carregarMunicipios);
	},
	
	carregarTipoCota : function() {
				
		$('.selecionarCotas', OperacaoDiferenciadaController.workspace).show();
		$('.selecionarMunicipio', OperacaoDiferenciadaController.workspace).hide();
		
		$('[name=selecao]').attr('name','selecaoInativo');
		
		var data = [];	
		
		if(OperacaoDiferenciadaController.grupoSelecionado) {
			data.push({name:'idGrupo',		value: OperacaoDiferenciadaController.grupoSelecionado.idGrupo });
		}
		
		data.push({name:'tipoCota',		value: $('#comboTipoCota', OperacaoDiferenciadaController.workspace).val() });
		
		$(".selCotasGrid", OperacaoDiferenciadaController.workspace).flexOptions({ params:data, newp:1 });		
		$(".selCotasGrid", OperacaoDiferenciadaController.workspace).flexReload();
	},
	
	novoGrupo : function() {
		
		this.grupoSelecionado = null;	
		
		$('#comboTipoCota', OperacaoDiferenciadaController.workspace).hide();
		$('#radioTipoCota', OperacaoDiferenciadaController.workspace).prop('checked', false);
		$('#radioMunicipios', OperacaoDiferenciadaController.workspace).prop('checked', false);
		$('#comboTipoCota', OperacaoDiferenciadaController.workspace).val('');
		
		$('.selecionarCotas', OperacaoDiferenciadaController.workspace).hide();
		$('.selecionarMunicipio', OperacaoDiferenciadaController.workspace).hide();
		
		this.dialogDetalhesGrupo();
	},
		
	excluirGrupo : function(index) {
		
		var data = [];		
		data.push({name:'idGrupo',		value: this.grupos[index].idGrupo });

		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/excluirGrupo',
				data,
				function(result){										
					$(".gruposGrid", OperacaoDiferenciadaController.workspace).flexReload();
					exibirMensagem('SUCCESS', ['Grupo excluido com sucesso.']);										
				});		
		
		$( "#dialog-confirm-grupo" , OperacaoDiferenciadaController.workspace).dialog( "close" );
		
	},
	
	processaMunicipios : function(result) {
		
		$.each(result.rows, function(index, row) {
			
			var municipio = row.cell.municipio;
			
			if(!municipio || municipio.length < 1){
				municipio = " null ";
			}
			
			if (!row.cell.municipio && "" != row.cell.municipio){
				row.cell.municipio = 'Cota sem endereço';
			}
			
			row.cell.selecionado="<input type='checkbox' name='selecao' " +
			"id='municipio["+municipio+"]' " +
			(row.cell.selecionado == true ? " checked='checked' " : "") +
			" onclick='OperacaoDiferenciadaController.adicionarSelecaoMunicipio( \""+municipio+"\" ,this);' />";
		});
				
		return result;
	},
	
	adicionarSelecaoMunicipio : function(id, check) {
		
		if(!check.checked) {
			$("#selecionarTodosID", OperacaoDiferenciadaController.workspace).attr("checked",false);
		}
		
		var dados = [];		
		dados.push({ name : 'municipio', value: id});		
		dados.push({ name : 'selecionado', value: check.checked });
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/selecionarMunicipio',dados);		
	},
	
	adicionarSelecaoCota : function(id, check) {
		
		if(!check.checked) {
			$("#selecionarTodosID", OperacaoDiferenciadaController.workspace).attr("checked",false);
		}
		
		var dados = [];		
		dados.push({ name : 'idCota', value: id});		
		dados.push({ name : 'selecionado', value: check.checked });
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/selecionarCota', 
				dados);		
	},
	
	selecionarTodosMunicipios : function(elementoCheck) {
		
		var dados = [];
		
		$.each( $('[name=selecao]', OperacaoDiferenciadaController.workspace), function(index, item) { 
		    $(item).attr('checked', elementoCheck.checked);
		    dados.push({ name : 'selecionados[' + index + ']', value: $(item).attr('id') });
		});
		
		dados.push({ name : 'selecionado', value: elementoCheck.checked });
		
		
		$.postJSON(contextPath + '/administracao/parametrosDistribuidor/selecionarTodosMunicipios', dados);
		
	},
	
	selecionarTodasCotas : function(elementoCheck) {
		
		var dados = [];
		
		$.each( $('[name=selecao]', OperacaoDiferenciadaController.workspace), function(index, item) { 
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
				
		var elemento = $("#" + campo, OperacaoDiferenciadaController.workspace);
		
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
		
		var elemento = $("#" + campo, OperacaoDiferenciadaController.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	confirmarOperacao : function() {
		var data = [];
		
		if(OperacaoDiferenciadaController.grupoSelecionado)
			data.push({name: "idGrupo", value: OperacaoDiferenciadaController.grupoSelecionado.idGrupo});
		
		$.each($("#diaSemana", OperacaoDiferenciadaController.workspace ).val(), 
			function(index, val) {							
				data.push({name: "diasSemana", value: val});
			}
		);
		
		var tipoOperacaoDiferenciada;
		
		var radioTipoCota = $('#radioTipoCota', OperacaoDiferenciadaController.workspace).prop('checked');
		
		if (radioTipoCota) {
			
			tipoOperacaoDiferenciada = "TIPO_COTA";
			
		} else {
			
			tipoOperacaoDiferenciada = "MUNICIPIO";
		}
		
		data.push({name: "tipoOperacaoDiferenciada", value: tipoOperacaoDiferenciada});
		data.push({name: "nome", value: $("#nomeDiferenca", OperacaoDiferenciadaController.workspace ).val()});
		
		$.postJSON(contextPath + "/administracao/parametrosDistribuidor/cadastrarOperacaoDiferenciada", data,
			function(result){
				
			$(".gruposGrid", OperacaoDiferenciadaController.workspace).flexReload();
			$( "#dialog-novo-grupo", OperacaoDiferenciadaController.workspace ).dialog( "close" );
			$( "#dialog-salvar", OperacaoDiferenciadaController.workspace ).dialog( "close" );

			exibirMensagem('SUCCESS', ['Grupo salvo com sucesso.']);	
			}, null, true
		);
	},
	
	dialogConfirmarGrupo : function() {
		
		$( "#dialog-salvar", OperacaoDiferenciadaController.workspace  ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Confirmar": function() {
					OperacaoDiferenciadaController.confirmarOperacao();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				
				$("#nomeDiferenca", this.workspace).val("");
				$("#diaSemana", this.workspace).val("");
			},
			form: $("#dialog-confirm-grupo", OperacaoDiferenciadaController.workspace).parents("form")
		});
	},
	
	dialogDetalhesGrupo : function() {
		
		$( "#dialog-novo-grupo", OperacaoDiferenciadaController.workspace ).dialog({
			resizable: false,
			height:470,
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
			form: $("#dialog-novo-grupo", OperacaoDiferenciadaController.workspace).parents("form")
		});

	},

	dialogExcluirGrupo : function(index) {
		
		$( "#dialog-confirm-grupo", OperacaoDiferenciadaController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					OperacaoDiferenciadaController.excluirGrupo(index);				
				},
				"Cancelar": function() {
					$( this, OperacaoDiferenciadaController.workspace ).dialog( "close" );
				}
			},
			form: $("#dialog-confirm-grupo", OperacaoDiferenciadaController.workspace).parents("form")
		});
	},
	
	inicializarGrids : function() {
		$(".selMunicipiosGrid", OperacaoDiferenciadaController.workspace).flexigrid({
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
				sortable : false,
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
	
	
	$(".selCotasGrid", OperacaoDiferenciadaController.workspace).flexigrid({
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
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'selecionado',
				width : 20,
				sortable : false,
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
	

		$(".gruposGrid", OperacaoDiferenciadaController.workspace).flexigrid({
			autoload : true,
			url : contextPath + '/administracao/parametrosDistribuidor/obterGrupos',
			dataType : 'json',
			preProcess: OperacaoDiferenciadaController.processaRetornoPesquisa,
			onSuccess:function(){bloquearItensEdicao(OperacaoDiferenciadaController.workspace);	},
			colModel : [ {
				display : 'Nome',
				name : 'nome',
				width : 460,
				sortable : true,
				align : 'left'
			},{
				display : 'Recolhimento',
				name : 'recolhimento',
				width : 180,
				sortable : false,
				align : 'LEFT'
			},{
				display : 'Ativo Apartir de',
				name : 'dataInicioVigencia',
				width : 80,
				sortable : false,
				align : 'center'
			},{
				display : 'Inativo Apartir de',
				name : 'dataFimVigencia',
				width : 90,
				sortable : false,
				align : 'center',
				hide: true
			},{
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			width : 940,
			height : 150,
			sortname : "nome",
			sortorder : "asc"
		});
	}
		
}, BaseController);

$(function() {
	OperacaoDiferenciadaController.init();
});
//@ sourceURL=operacaoDiferenciada.js