var cadastroTipoNotaController = $.extend(true, {

	buscar: function() {
		var operacao = $("#operacaoID", this.worspace).val();
		var tipoNota = $("#tipoNota", this.workspace).val();
		$(".tiposNotasGrid", this.worspace).flexOptions({
			url: contextPath + '/administracao/naturezaOperacao/pesquisar',
			params: [
		         {name:'operacao', value: operacao},
		         {name:'tipoNota', value: tipoNota}
		    ],
		    newp: 1,
		});
		$(".tiposNotasGrid").flexReload();
	},
	initGrid: function() {
		$(".tiposNotasGrid", this.workspace).flexigrid({
			preProcess: cadastroTipoNotaController.executarPreProcessamentoTiposNotasGridGrid, 
			dataType : 'json',
			colModel : [ {
				display : 'Operacao',
				name : 'tipoAtividade',
				width : 138,
				sortable : true,
				align : 'left'
			},{
				display : 'Descrição',
				name : 'nopDescricao',
				width : 390,
				sortable : true,
				align : 'left'
			},{
				display : 'CFOP Dentro UF',
				name : 'cfopEstado',
				width : 85,
				sortable : true,
				align : 'center'
			},{
				display : 'CFOP Fora UF',
				name : 'cfopOutrosEstados',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Nota',
				name : 'numeroNotaFiscal',
				width : 50,
				sortable : true,
				align : 'center'
			},{
				display : 'Serie',
				name : 'serieNotaFiscal',
				width : 50,
				sortable : true,
				align : 'center',
				edittype: 'text',
				editable: true
			},{
				display : 'Gravar',
				name : 'gravar',
				width : 40,
				sortable : false,
				align : 'center'
			}],
			sortname : "tipoAtividade",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 950,
			height : 'auto'
		});
	}, 
	bindButtons : function() {
		$("#btnPesquisar", this.workspace).click(function() {
			cadastroTipoNotaController.buscar();
			$(".grids", this.worspace).show();
		});
	},
	init : function(path) {
		this.contextPath = path;
		this.initGrid();
		this.bindButtons();
		definirAcaoPesquisaTeclaEnter(this.worspace);
	},
	executarPreProcessamentoTiposNotasGridGrid : function(resultado) {
		var operacao = $("#operacaoID", this.worspace).val();
		var tipoNota = $("#tipoNota", this.workspace).val();
		$.each(resultado.rows, function(index, row) {
			row.cell.numeroNotaFiscal="<input type=\"text\" name=\"numeroNotaFiscal\" id=\"numeroNotaFiscal"+row.cell.id+"\" class=\"campoDePesquisa\"  value=\""+row.cell.numeroNotaFiscal+"\"/></td>";
			row.cell.serieNotaFiscal="<input type=\"text\" name=\"serieNotaFiscal\" id=\"serieNotaFiscal"+row.cell.id+"\" class=\"campoDePesquisa\"  value=\""+row.cell.serieNotaFiscal+"\"/></td>";
			row.cell.gravar = "<a href='javascript:;' onclick='cadastroTipoNotaController.gravarNota(\"" + row.cell.id + "\", \"" + operacao + "\", \"" + tipoNota + "\")'><img border='0' style='margin-right:10px;' src= " + contextPath + "/images/ico_salvar.gif /></href>";
		});
		return resultado
	},
	
	gravarNota : function(id, operacao, tipoNota) {
			$( "#dialog-gravarNota" ).dialog({
				resizable: false,
				height:'auto',
				width:400,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						
						var data = [{name: 'serieNotaFiscal', value: $("#serieNotaFiscal"+id).val()},{name: 'numeroNotaFiscal', value: $("#numeroNotaFiscal"+id).val()}, {name:'id', value: id},
							{name:'rp', value:15},{name:'page', value:1},
							{name:'sortname', value:'tipoAtividade'},{name:'sortorder', value:'asc'},
							{name:'operacao', value:operacao},{name:'tipNota', value:tipoNota}];
						
						$.postJSON(contextPath + "/administracao/naturezaOperacao/gravarNota",
								   data);
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
			
		}
	
	
}, BaseController);
//@sourceURL=naturezaOperacao.js