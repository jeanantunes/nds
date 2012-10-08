var entradaNFETerceirosController = $.extend(true, {
	
	path : contextPath + "/nfe/entradaNFETerceiros/",
	
	init : function() {
		$( "#dataInicial", this.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#dataFinal", this.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		this.initEncalheNfeGrid();
		this.initNotaRecebidaGrid();
		this.initPesqProdutosNotaGrid();
	},
	
	

	cadastrarNota : function(){		

		$.postJSON(
				contextPath + '/nfe/entradaNFETerceiros/cadastrarNota',
				[
					{ name: "nota.numero", value: $('#numeroNotaCadastroNota', this.workspace).val() },
					{ name: "nota.serie", value: $('#serieNotaCadastroNota', this.workspace).val() },
					{ name: "nota.chaveAcesso", value: $('#chaveAcessoCadastroNota', this.workspace).val() },
					{ name: "numeroCota", value: $('#cotaCadastroNota', this.workspace).val() },
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
				$( "#effect:visible", this.workspace).removeAttr( "style" ).fadeOut();

		}, 1000 );
	},	

	
	confirmar : function(){
		$(".dados", this.workspace).show();
	},
	
	pesquisarEncalhe : function(){
		$(".dadosFiltro", this.workspace).show();		
		var status = $('#situacaoNfe', this.workspace).val();		
		
		if(status != ''){
			if(status == 'RECEBIDA'){			
				this.pesquisarNotaRecebidas();		
			}else{			
				this.pesquisarNotasPendente();
			}
		}else{
			$(".ui-state-highlight ui-corner-all").show();
			$(".ui-icon ui-icon-info").html("teste");
			$(".ui-icon ui-icon-info").show();
			
		}
		
	},
	
	pesquisarNotaRecebidas : function(){
		
		var params = $("#form-pesquisa-nfe").serialize();
		
		$(".notaRecebidaGrid", this.workspace).flexOptions({
			url: this.path + "pesquisarNotasRecebidas?" + params,
			newp : 1
		});

		$(".notaRecebidaGrid", this.workspace).flexReload();
	},
	
	pesquisarNotasPendente : function(){
		
		var params = $("#form-pesquisa-nfe").serialize();
		
		$(".encalheNfeGrid", this.workspace).flexOptions({
			url: this.path +"pesquisarNotasPendentes?" + params,
			newp: 1
		});

		$(".encalheNfeGrid", this.workspace).flexReload();		
	},
	
	mostrar_nfes : function(){
		$(".nfes", this.workspace).show();
	},

	executarPreProcessamento : function(resultado) {
			
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", this.workspace).hide();

			return resultado;
		}
		var status = $('#situacaoNfe', this.workspace).val();					
		if(status == 'RECEBIDA'){
			$.each(resultado.rows, function(index, row) {
				
				var linkAviso = '<a href="javascript:;" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/ico_alert.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAviso;
			});
			
		}else{
			
			$.each(resultado.rows, function(index, row) {					
				
				var linkLancamento = '<a href="javascript:;"  onclick="entradaNFETerceirosController.popup_nfe(\''+row.cell.numeroCota+'\',\''+row.cell.nome+'\');" style="cursor:pointer">' +
								   	 '<img title="Lançamentos da Edição" src="' + contextPath + '/images/bt_lancamento.png" hspace="5" border="0px" />' +
								   '</a>';
			   var linkCadastro = '<a href="javascript:;" onclick="entradaNFETerceirosController.popup_dadosNotaFiscal('+row.cell.numeroNfe+','
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
		
		$(".grids", this.workspace).show();
		
		return resultado;
	},
	
	//***OK***//
	pesquisarCota : function() {
 		
		numeroCota = $("#numeroCota", this.workspace).val();
 		
 		$.postJSON(
			contextPath + '/nfe/entradaNFETerceiros/buscarCotaPorNumero',
			{ "numeroCota": numeroCota },
			function(result) {
				
				$("#nomeCota", this.workspace).html(result);
				
			},
			null,
			true
		);
 	},
 	
 	//***OK***//
	pesquisarCotaCadastroNota : function() {
 		
		numeroCota = $("#cotaCadastroNota", this.workspace).val();
 		
 		$.postJSON(
			contextPath + '/nfe/entradaNFETerceiros/buscarCotaPorNumero',
			{ "numeroCota": numeroCota },
			function(result) {
				
				$("#nomeCotaCadastroNota", this.workspace).val(result);
				
			},
			null,
			true
		);
 	},
 	
 	popup : function() {
		
		$( "#dialog-novo", this.workspace ).dialog({
			resizable: 
				 false,
			height:'auto',
			width:200,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-novo", this.workspace).dialog("close")
		});
	},
	
	popup_confirm : function() {
	
		$( "#dialog-confirm", this.workspace ).dialog({
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
			},
			form: $("#dialog-confirm", this.workspace).dialog("close")
		});	
		      
	},
	
	popup_rejeitar : function() {
	
		$( "#dialog-rejeitar", this.workspace ).dialog({
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
			},
			form: $("#dialog-rejeitar", this.workspace).dialog("close")
		});
	},
	
	popup_dadosNotaFiscal : function(numeroNfe, dataEncalhe, chaveAcesso, serie, vlrNota, idNotaFiscalEntrada) {
		
		/*$('#numeroNotaFiscalPopUp', this.workspace).text(numeroNfe);
		$('#dataNotaFiscalPopUp', this.workspace).text(dataEncalhe);
		$('#chaveAcessoNotaFiscalPopUp', this.workspace).text(chaveAcesso);
		$('#serieNotaFiscalPopUp', this.workspace).text(serie);
		$('#valorNotaFiscalPopUp', this.workspace).text(vlrNota);
		
		$(".pesquisarProdutosNotaGrid", this.workspace).flexOptions({
			url: contextPath + "/nfe/entradaNFETerceiros/pesquisarItensPorNota",
			dataType : 'json',
			params: [{name:'filtro.codigoNota', value:idNotaFiscalEntrada}]
		});

		$(".pesquisarProdutosNotaGrid", this.workspace).flexReload();*/
	
		$( "#dialog-dadosNotaFiscal", this.workspace ).dialog({
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
			},
			form: $("#dialog-dadosNotaFiscal", this.workspace).parents("form")
		
		
		});	
		      
	},
	
	popup_confirmar : function() {
	
		$( "#dialog-confirmar-cancelamento", this.workspace ).dialog({
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
			},
			form: $("#dialog-confirmar-cancelamento", this.workspace).parents("form")
		});
	},
	
	popup_nfe : function(numeroCota, nome){
		
		if(numeroCota != '0'){
			$('#cotaCadastroNota', this.workspace).val(numeroCota);
			$('#nomeCotaCadastroNota', this.workspace).val(nome);			
		}else{
			$('#cotaCadastroNota', this.workspace).val('');
			$('#nomeCotaCadastroNota', this.workspace).val('');
		}
		$('#nomeCotaCadastroNota', this.workspace).attr('readonly', true);
		
		$( "#dialog-nfe", this.workspace ).dialog({
			resizable: false,
			height:280,
			width:350,
			modal: true,
			buttons: {
				"Confirmar": function() {
					this.cadastrarNota();
					$( this ).dialog( "close" );					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-nfe", this.workspace).parents("form")
		});
	},
	
	initEncalheNfeGrid :function(){
		$(".encalheNfeGrid", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamento,
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
				height : 'auto'
			});
	},
	
	initNotaRecebidaGrid : function() {
		$(".notaRecebidaGrid").flexigrid({
			preProcess: this.executarPreProcessamento,
			dataType : 'json',
				colModel : [ {
					display : 'Nº Nota',
					name : 'numNota',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Série',
					name : 'serie',
					width : 30,
					sortable : true,
					align : 'left'
				}, {
					display : 'Chave Acesso',
					name : 'chaveAcesso',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'Dt. Emissão',
					name : 'dtEmissao',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Tipo Nota',
					name : 'tipoNota',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor/Cota',
					name : 'fornecedorCota',
					width : 210,
					sortable : true,
					align : 'left'
				}, {
					display : 'Vlr. Nota R$',
					name : 'vlrNota',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 30,
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
	
	initPesqProdutosNotaGrid : function() {
		$(".pesqProdutosNotaGrid", this.workspace).flexigrid({
			preProcess: this.executarPreProcessamento,
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
	}
	
}, BaseController);