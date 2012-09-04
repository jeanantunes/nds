var consultaNFEEncalheTratamentoController = $.extend(true, {
	popup : function() {
		
		$( "#dialog-novo", consultaNFEEncalheTratamentoController.workspace ).dialog({
			resizable: 
				 false,
			height:'auto',
			width:280,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	popup_confirm : function() {
	
		$( "#dialog-confirm", consultaNFEEncalheTratamentoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:280,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	},
	
	popup_rejeitar : function() {
	
		$( "#dialog-rejeitar", consultaNFEEncalheTratamentoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:280,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	popup_dadosNotaFiscal : function(numeroNfe, dataEncalhe, chaveAcesso, serie, vlrNota, idNotaFiscalEntrada) {
		
		$('#numeroNotaFiscalPopUp', consultaNFEEncalheTratamentoController.workspace).text(numeroNfe);
		$('#dataNotaFiscalPopUp', consultaNFEEncalheTratamentoController.workspace).text(dataEncalhe);
		$('#chaveAcessoNotaFiscalPopUp', consultaNFEEncalheTratamentoController.workspace).text(chaveAcesso);
		$('#serieNotaFiscalPopUp', consultaNFEEncalheTratamentoController.workspace).text(serie);
		$('#valorNotaFiscalPopUp', consultaNFEEncalheTratamentoController.workspace).text(vlrNota);
		
		$(".pesqProdutosNotaGrid", consultaNFEEncalheTratamentoController.workspace).flexOptions({
			url: contextPath + "/nfe/consultaNFEEncalheTratamento/pesquisarItensPorNota",
			dataType : 'json',
			params: [{name:'filtro.codigoNota', value:idNotaFiscalEntrada}]
		});

		$(".pesqProdutosNotaGrid", consultaNFEEncalheTratamentoController.workspace).flexReload();
	
		$( "#dialog-dadosNotaFiscal", consultaNFEEncalheTratamentoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:860,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		
		
		});	
		      
	},
	
	popup_confirmar : function() {
	
		$( "#dialog-confirmar-cancelamento", consultaNFEEncalheTratamentoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:280,
			modal: true,
			buttons: {
				"Confirmar": function() {					
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	popup_nfe : function(numeroCota, nome){
		
		if(numeroCota != '0'){
			$('#cotaCadastroNota', consultaNFEEncalheTratamentoController.workspace).val(numeroCota);
			$('#nomeCotaCadastroNota', consultaNFEEncalheTratamentoController.workspace).val(nome);
			$('#cotaCadastroNota', consultaNFEEncalheTratamentoController.workspace).attr('disabled', 'disabled');
			$('#nomeCotaCadastroNota', consultaNFEEncalheTratamentoController.workspace).attr('disabled', 'disabled');
			}
			
		$( "#dialog-nfe", consultaNFEEncalheTratamentoController.workspace ).dialog({
			resizable: false,
			height:300,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					cadastrarNota();
					$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},

	cadastrarNota : function(){		

		$.postJSON(
				contextPath + '/nfe/consultaNFEEncalheTratamento/cadastrarNota',
				[
					{ name: "nota.numero", value: $('#numeroNotaCadastroNota', consultaNFEEncalheTratamentoController.workspace).val() },
					{ name: "nota.serie", value: $('#serieNotaCadastroNota', consultaNFEEncalheTratamentoController.workspace).val() },
					{ name: "nota.chaveAcesso", value: $('#chaveAcessoCadastroNota', consultaNFEEncalheTratamentoController.workspace).val() },
					{ name: "numeroCota", value: $('#cotaCadastroNota', consultaNFEEncalheTratamentoController.workspace).val() },
				],
				function(result) {
					alert(result);					
				},
				null,
				true
			);
	
	},
  
	callback : function() {
			setTimeout(function() {
				$( "#effect:visible", consultaNFEEncalheTratamentoController.workspace).removeAttr( "style" ).fadeOut();

		}, 1000 );
	},	
	
	init : function() {
		$( "#data", consultaNFEEncalheTratamentoController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$(".pesqProdutosNotaGrid", consultaNFEEncalheTratamentoController.workspace).flexigrid({
			preProcess: consultaNFEEncalheTratamentoController.executarPreProcessamento,
			dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Dia',
					name : 'dia',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Qtde. Info',
					name : 'qtdInformada',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Qtde. Recebida',
					name : 'qtdRecebida',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Capa R$',
					name : 'precoCapaFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço Desc R$',
					name : 'precoDescontoFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total R$',
					name : 'totalDoItemFormatado',
					width : 80,
					sortable : true,
					align : 'right'
				}],
				width : 810,
				height : 250
			});
		$(".notaRecebidaGrid", consultaNFEEncalheTratamentoController.workspace).flexigrid({
			preProcess: consultaNFEEncalheTratamentoController.executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 280,
					sortable : true,
					align : 'left'
				}, {
					display : 'NF-e',
					name : 'numeroNfe',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Série',
					name : 'serie',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Chave Acesso',
					name : 'chaveAcesso',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : '',
					name : 'acao',
					width : 25,
					sortable : true,
					align : 'center'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		
		
		$(".encalheNfeGrid", consultaNFEEncalheTratamentoController.workspace).flexigrid({
			preProcess: consultaNFEEncalheTratamentoController.executarPreProcessamento,
			dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Data Encalhe',
					name : 'dataEncalhe',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo de Nota',
					name : 'tipoNota',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Valor Nota R$',
					name : 'vlrNotaFormatado',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Valor Real R$',
					name : 'vlrRealFormatado',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Diferença',
					name : 'diferencaFormatado',
					width : 60,
					sortable : true,
					align : 'right'
				}, {
					display : 'Status',
					name : 'status',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : ' ',
					name : 'sel',
					width : 20,
					sortable : true,
					align : 'center'
				}],
				sortname : "cota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		
	},
	
	confirmar : function(){
		$(".dados", consultaNFEEncalheTratamentoController.workspace).show();
	},
	
	pesqEncalhe : function(){
		$(".dadosFiltro", consultaNFEEncalheTratamentoController.workspace).show();		
		var status = $('#situacaoNfe', consultaNFEEncalheTratamentoController.workspace).val();		
		if(status == 'RECEBIDA'){			
			pesquisarNotaRecebidas();		
		}else{			
			pesquisarNotasPendente();
		}
		
	},
	
	pesquisarNotaRecebidas : function(){
		
		$(".notaRecebidaGrid", consultaNFEEncalheTratamentoController.workspace).flexOptions({
			url: contextPath + "/nfe/consultaNFEEncalheTratamento/pesquisarNotasRecebidas",
			dataType : 'json',
			params: [
						{name:'filtro.codigoCota', value:$('#codigoCota', consultaNFEEncalheTratamentoController.workspace).val()},
						{name:'filtro.data', value:$('#data', consultaNFEEncalheTratamentoController.workspace).val()},
						{name:'filtro.statusNotaFiscalEntrada', value:$('#situacaoNfe', consultaNFEEncalheTratamentoController.workspace).val()}						
						]
		});

		$(".notaRecebidaGrid", consultaNFEEncalheTratamentoController.workspace).flexReload();
	},
	
	pesquisarNotasPendente : function(){
		
		$(".encalheNfeGrid", consultaNFEEncalheTratamentoController.workspace).flexOptions({
			url: contextPath + "/nfe/consultaNFEEncalheTratamento/pesquisarNotasPendentes",
			dataType : 'json',
			params: [
						{name:'filtro.codigoCota', value:$('#codigoCota', consultaNFEEncalheTratamentoController.workspace).val()},
						{name:'filtro.data', value:$('#data', consultaNFEEncalheTratamentoController.workspace).val()},
						{name:'filtro.statusNotaFiscalEntrada', value:$('#situacaoNfe', consultaNFEEncalheTratamentoController.workspace).val()}						
						]
		});

		$(".encalheNfeGrid", consultaNFEEncalheTratamentoController.workspace).flexReload();		
	},
	
	mostrar_nfes : function(){
		$(".nfes", consultaNFEEncalheTratamentoController.workspace).show();
	},

	executarPreProcessamento : function(resultado) {
			
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", consultaNFEEncalheTratamentoController.workspace).hide();

			return resultado;
		}
		var status = $('#situacaoNfe', consultaNFEEncalheTratamentoController.workspace).val();					
		if(status == 'RECEBIDA'){
			$.each(resultado.rows, function(index, row) {
				
				var linkAviso = '<a href="javascript:;" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_alert.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAviso;
			});
			
		}else{
			
			$.each(resultado.rows, function(index, row) {					
				
				var linkLancamento = '<a href="javascript:;"  onclick="popup_nfe(\''+row.cell.numeroCota+'\',\''+row.cell.nome+'\');" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_lancamento.png" hspace="5" border="0px" />' +
								   '</a>';
			   var linkCadastro = '<a href="javascript:;" onclick="consultaNFEEncalheTratamentoController.popup_dadosNotaFiscal('+row.cell.numeroNfe+','
					   																		+row.cell.dataEncalhe+','
					   																		+row.cell.chaveAcesso+','
					   																		+row.cell.serie+','
					   																		+row.cell.vlrNota+','
					   																		+row.cell.idNotaFiscalEntrada+');" style="cursor:pointer">' +
							   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_cadastros.png" hspace="5" border="0px" />' +
		                         '</a>';
               var checkBox = '<input type="checkbox" id="checkNota" name="checkNota" />';
				
				row.cell.acao = linkLancamento + linkCadastro;
				row.cell.sel = checkBox;
			});
		
		}
		
		$(".grids", consultaNFEEncalheTratamentoController.workspace).show();
		
		return resultado;
	},
	
	pesquisarCota : function() {
 		
		numeroCota = $("#codigoCota", consultaNFEEncalheTratamentoController.workspace).val();
 		
 		$.postJSON(
			contextPath + '/nfe/consultaNFEEncalheTratamento/buscarCotaPorNumero',
			{ "numeroCota": numeroCota },
			function(result) {

				$("#nomeCota", consultaNFEEncalheTratamentoController.workspace).html(result);
				
			},
			null,
			true
		);
 	}
	
}, BaseController);