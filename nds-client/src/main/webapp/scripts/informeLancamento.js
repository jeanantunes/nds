var informeLancamentoController = $.extend(true, {
	
	
	init : function() {
		
		$("#dataLancamentoBox_infoLanc", informeLancamentoController.workspace).mask("99/99/9999");
		
		$("#dataLancamentoBox_infoLanc", informeLancamentoController.workspace)
				.datepicker(
						{
							showOn : "button",
							buttonImage : contextPath
									+ "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
							buttonImageOnly : true
						});

		$("#btnPesquisar_infoLanc", informeLancamentoController.workspace).click(function() {
			informeLancamentoController.pesquisar();
		});
		
		$("#btnImprimir_infoLanc", informeLancamentoController.workspace).click(function() {
			
			var dataExport = [];
			dataExport.push({name:'fileType', value: 'PDF'});
			dataExport.push({name:'idFornecedor', value: $("#idFornecdorSelect_infoLanc", informeLancamentoController.workspace).val()});
			dataExport.push({name:'semanaLancamento', value: $("#semanaLancamentoBox_infoLanc", informeLancamentoController.workspace).val()});
			dataExport.push({name:'dataLancamento', value: $("#dataLancamentoBox_infoLanc", informeLancamentoController.workspace).val()});
			
			$.fileDownload(contextPath + "/lancamento/informeLancamento/exportar", {
	            httpMethod : "GET",
	            data : dataExport,
	            failCallback : function(arg) {
	                exibirMensagem("WARNING", ["Erro ao gerar o arquivo!"]);
	            }
			});
			
			
		});
		
		$("#btnExportar_infoLanc", informeLancamentoController.workspace).click(function() {
			
			var dataExport = [];
			dataExport.push({name:'fileType', value: 'XLS'});
			dataExport.push({name:'idFornecedor', value: $("#idFornecdorSelect_infoLanc", informeLancamentoController.workspace).val()});
			dataExport.push({name:'semanaLancamento', value: $("#semanaLancamentoBox_infoLanc", informeLancamentoController.workspace).val()});
			dataExport.push({name:'dataLancamento', value: $("#dataLancamentoBox_infoLanc", informeLancamentoController.workspace).val()});
			
			$.fileDownload(contextPath + "/lancamento/informeLancamento/exportar", {
	            httpMethod : "GET",
	            data : dataExport,
	            failCallback : function(arg) {
	                exibirMensagem("WARNING", ["Erro ao gerar o arquivo!"]);
	            }
			});
			
			
		});
		
		$('#dataLancamentoBox_infoLanc,', informeLancamentoController.workspace).change(function() {
			informeLancamentoController.carregarDiaSemana();
		});
		
		$("#sugerirSemana_infoLanc", informeLancamentoController.workspace).click(function(e){
			
			if($(this).is(":checked")){
				informeLancamentoController.carregarDiaSemana();
			}else{
				$("#semanaLancamentoBox_infoLanc", informeLancamentoController.workspace).val("");
			 }
		});
		
		
		$("#informeLancamentoGrid", informeLancamentoController.workspace).flexigrid({
			preProcess : informeLancamentoController.executarPreProcessInformeGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Seq',
				name : 'sequencia',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codigoProduto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {

				display : 'Produto',
				name : 'nomeProduto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {

				display : 'Edição',
				name : 'numeroEdicao',
				width : 43,
				sortable : true,
				align : 'center'
			}, {

				display : 'Chamada de Capa',
				name : 'chamadaCapa',
				width : 200,
				sortable : true,
				align : 'center'
			}, {

				display : 'Código Barras',
				name : 'codigoDeBarras',
				width : 180,
				sortable : true,
				align : 'left'
			}, {

				display : 'Preço de Capa',
				name : 'precoVendaFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			sortname : "sequencia",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});
	},
	
	executarPreProcessInformeGrid : function(resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			
			$(".fieldInformeLancamentoGrid", informeLancamentoController.workspace).hide();
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			if(row.cell.sequencia == undefined){
				row.cell.sequencia = '';
			}
			
			if(row.cell.chamadaCapa == undefined){
				row.cell.chamadaCapa = '';
			}
			
			if(row.cell.precoVendaFormatado == undefined){
				row.cell.precoVendaFormatado = '';
			}else{
				row.cell.precoVendaFormatado = 'R$ ' + row.cell.precoVendaFormatado;
			}
			
			var inputChamadaDeCapa = '<input value="'+row.cell.chamadaCapa+'" onchange="informeLancamentoController.atualizarChamadaCapa(this, '+row.cell.idProdutoEdicao+')" />';
			
			row.cell.chamadaCapa = inputChamadaDeCapa;
			
			var inputCodigoDeBarras = '<input value="'+row.cell.codigoDeBarras+'" onchange="informeLancamentoController.atualizarCodigoBarras(this, '+row.cell.idProdutoEdicao+')" />';
			
			row.cell.codigoDeBarras = inputCodigoDeBarras;
			
		});
		
		$(".fieldInformeLancamentoGrid", informeLancamentoController.workspace).show();
		
		return resultado;
		
	},
	
	pesquisar : function() {
		
		$(".fieldInformeLancamentoGrid").show();
		
		$("#informeLancamentoGrid", informeLancamentoController.workspace).flexOptions({
			url: contextPath + "/lancamento/informeLancamento/pesquisarInformeLancamento",
			dataType : 'json',
			params : informeLancamentoController.obterParametrosFiltro()
		});
		
		$("#informeLancamentoGrid", informeLancamentoController.workspace).flexReload();	
	},
	
	obterParametrosFiltro : function () {
		
		var data = [];
		
		data.push({name:'idFornecedor', value: $("#idFornecdorSelect_infoLanc", informeLancamentoController.workspace).val()});
		data.push({name:'semanaLancamento', value: $("#semanaLancamentoBox_infoLanc", informeLancamentoController.workspace).val()});
		data.push({name:'dataLancamento', value: $("#dataLancamentoBox_infoLanc", informeLancamentoController.workspace).val()});
		
		
		return data;
		
	},
	
	atualizarChamadaCapa : function (inputNovoValor, idPE){
		
		var novoValor = $(inputNovoValor).val();
		
		informeLancamentoController.atualizarRegistro(novoValor, idPE, false);
	},
	
	atualizarCodigoBarras : function (inputNovoValor, idPE){
		
		var novoValor = $(inputNovoValor).val();
		
		informeLancamentoController.atualizarRegistro(novoValor, idPE, true);
	},
	
	atualizarRegistro : function(valInput, idPE, isCodigoDeBarras) {
		
		$("#dialog-ConfirmarUpdate").dialog({
			resizable : false,
			height : 200,
			width : 400,
			modal : true,
			buttons : {
				"Confirmar" : function() {
//					var valInput = $(data).val();
					
					var data = [];
					
					data.push({name:'idNumeroEdicao', value: idPE});
					data.push({name:'novoValor', value: valInput});
					data.push({name:'isCodigoDeBarras', value: isCodigoDeBarras});
					
			    	$.postJSON(contextPath + "/lancamento/informeLancamento/atualizarProdutoEdicao",
							data,
							function(result) {
			    				//Success!!
			        		},
			        		function(result) {
			        			exibirMensagem(result.tipoMensagem, result.listaMensagens);
			        		},
			        		null
					);
					
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
		
	},

	carregarDiaSemana : function() {
	
		if($("#sugerirSemana_infoLanc:checked").size() < 1){
			return;
		}
	
		var _this = informeLancamentoController;
		
		var dataPesquisa = $("#dataLancamentoBox_infoLanc", _this.workspace).val();
	
		if (!dataPesquisa) {
	
			return;
		}
	
		var data = [{ name: 'data', value: $("#dataLancamentoBox_infoLanc", _this.workspace).val() }];
		
		$.getJSON(
			contextPath + "/cadastro/distribuidor/obterNumeroSemana", 
			data,
			function(result) {
	
				if (result) {
	
					$("#semanaLancamentoBox_infoLanc", _this.workspace).val(result.int);
				}
			});
	}
	
}, BaseController);
//@ sourceURL=informeLancamento.js