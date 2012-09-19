var confirmaExpedicaoController = $.extend(true, {

	change : false,

	init : function() {
		definirAcaoPesquisaTeclaEnter();
		//Define foco inicial no campo Data Lançamento.
		$('#idDataLancamento', confirmaExpedicaoController.workspace).focus();

		$(".confirmaExpedicaoGrid", confirmaExpedicaoController.workspace).flexigrid($.extend({},{
			colModel : [ {
				display : 'Data Entrada',
				name : 'dataEntrada',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Classifica&ccedil;&atilde;o',
				name : 'classificacao',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Pre&ccedilo',
				name : 'preco',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pct. Padr&atilde;o',
				name : 'pctPadrao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Chamada',
				name : 'dataChamada',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 80,
				sortable : false,
				align : 'left'
			}, {
				display : 'Estudo',
				name : 'estudo',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : '',
				name : 'selecionado',
				width : 20,
				sortable : false,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		})); 	
		
		$(".grids", confirmaExpedicaoController.workspace).show();		

		$("#idDataLancamento", confirmaExpedicaoController.workspace)
		.datepicker(
				{
					showOn : "button",
					buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
					buttonImageOnly : true
				});
		$( "#idDataLancamento", confirmaExpedicaoController.workspace ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idDataLancamento", confirmaExpedicaoController.workspace).mask("99/99/9999");
		
	},
	
	gerarCheckbox : function(id,name,idLancamento,selecionado) {
		var html = "";
		html+= ' <input ';
		html+= ' id="'+id+'"';
		html+= ' name="'+name+'"';
		html+= ' type="checkbox"';
		html+= ' onclick="confirmaExpedicaoController.adicionarSelecao('+idLancamento+',this);"';
		html+= ' style="float: left;"';
		
		if(selecionado==true) {
			html+= ' checked="checked" ' ;	
		}
		
		html+= ' />';
		return html;
		
	},
	
	adicionarSelecao : function(id, check) {
		
		if(check.checked==false) {
			$("#selecionarTodosID", confirmaExpedicaoController.workspace).attr("checked",false);
		}
		
		$.postJSON(contextPath + "/confirmacaoExpedicao/selecionarLancamento", 
				"idLancamento="+id +"&selecionado="+check.checked, 
				retornoSemAcao);				
	},
	
	retornoSemAcao : function(data) {
		
	},
	
	selecionarTodos : function (elementoCheck) {
		
		//var selects =  document.getElementsByName("selecao");
		var selects =  $("[name='selecao']");

		confirmaExpedicaoController.confirmaExpedicaoController.
		$.postJSON(contextPath + "/confirmacaoExpedicao/selecionarTodos", 
				"selecionado="+elementoCheck.checked, 
				confirmaExpedicaoController.retornoSemAcao);	
		
		$.each(selects, function(index, row) {
			row.checked=elementoCheck.checked;
		});
		
	},
			
	processaRetornoPesquisa : function(data) {
				
		var grid = data[0];
		var mensagens = data[1];
		var status = data[2];
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
		
		if(!grid.rows) {
			return grid;
		}
		
		for(var i=0; i<grid.rows.length; i++) {			
			
			var cell = grid.rows[i].cell;
								
			if(cell.estudo) {
				cell.selecionado = confirmaExpedicaoController.gerarCheckbox('idCheck'+i,'selecao', cell.idLancamento,cell.selecionado);
			} else {
				cell.estudo="";
				cell.selecionado="";
			}
		}
		
		return grid;
	},
	
	cliquePesquisar : function() {
		
		$("#selecionarTodosID", confirmaExpedicaoController.workspace).attr("checked",false);
		confirmaExpedicaoController.change= !confirmaExpedicaoController.change;
		
		var dataLancamento = $('#idDataLancamento', confirmaExpedicaoController.workspace).attr('value');
		var idFornecedor = $('#idFornecedor', confirmaExpedicaoController.workspace).attr('value');
		var estudo= ($('#idEstudo', confirmaExpedicaoController.workspace).attr('checked')) == 'checked';
		
		$(".confirmaExpedicaoGrid", confirmaExpedicaoController.workspace).flexOptions({			
			url : contextPath + '/confirmacaoExpedicao/pesquisarExpedicoes',
			dataType : 'json',
			preProcess:confirmaExpedicaoController.processaRetornoPesquisa,
			params:[{name:'dtLancamento',value:dataLancamento},
			        {name:'idFornecedor',value:idFornecedor},
			        {name:'estudo',value:estudo},
			        {name:'ultimaPesquisa',value:new Date()}]		
		});
		
		$(".confirmaExpedicaoGrid", confirmaExpedicaoController.workspace).flexReload();
	},
	
	popupConfirmar : function() {

		$("#dialog-confirmar", confirmaExpedicaoController.workspace).dialog({
			resizable : false,
			height : 140,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$("#selecionarTodosID", confirmaExpedicaoController.workspace).attr("checked",false);
					$(".confirmaExpedicaoGrid", confirmaExpedicaoController.workspace).flexOptions({			
						url : contextPath + '/confirmacaoExpedicao/confirmarExpedicao',
						dataType : 'json',
						preProcess:confirmaExpedicaoController.processaRetornoPesquisa		
					});
					
					$(".confirmaExpedicaoGrid", confirmaExpedicaoController.workspace).flexReload();
										
					$(this).dialog("close");
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			form: $("#dialog-confirmar", this.workspace).parents("form")
		});
	}

}, BaseController);
