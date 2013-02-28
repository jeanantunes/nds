var fechamentoEncalheController = $.extend(true, {

	vDataEncalhe : '',
	vFornecedorId : '',
	vBoxId : '',
	isFechamento : false,

	init : function() {
		$("#datepickerDe", fechamentoEncalheController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#dtPostergada", fechamentoEncalheController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

		$(".cotasGrid", fechamentoEncalheController.workspace).flexigrid({
			preProcess: fechamentoEncalheController.preprocessamentoGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'colaboradorName',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'boxName',
				width : 37,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiroName',
				width : 85,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rotaName',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;ao',
				name : 'acao',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : ' ',
				name : 'check',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 240,
			singleSelect : true
		});
		$(".fechamentoGrid", fechamentoEncalheController.workspace).flexigrid({
			dataType : 'json',
			preProcess: fechamentoEncalheController.preprocessamentoGridFechamento,
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 110,
				sortable : true,
				align : 'left'
			},{
				display : 'Edi&ccedil;&atilde;o',
				name : 'edicao',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Pre&ccedil;o Capa R$',
				name : 'precoCapaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pre&ccedil;o Desc R$',
				name : 'precoCapaDescFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Exempl. Devolu&ccedil;&atilde;o',
				name : 'exemplaresDevolucaoFormatado',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'totalFormatado',
				width : 80,
				sortable : false,
				align : 'right'
			}, {
				display : 'F&iacute;sico',
				name : 'fisico',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferen&ccedil;a',
				name : 'diferenca',
				width : 50,
				sortable : false,
				align : 'right'
			},{
				display : 'Estoque',
				name : 'estoque',
				width : 60,
				sortable : false,
				align : 'right'
			},{
				display : 'Replicar Qtde.',
				name : 'replicar',
				width : 80,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto',
			singleSelect : true
		});
		
	},
	
	pesquisar : function(aplicaRegraMudancaTipo) {
		if (aplicaRegraMudancaTipo == null ){
			aplicaRegraMudancaTipo = false;
		}
		
		$('.divBotoesPrincipais', fechamentoEncalheController.workspace).hide();
		$('#bt_cotas_ausentes', fechamentoEncalheController.workspace).hide();
		
		$(".fechamentoGrid", fechamentoEncalheController.workspace).flexOptions({
			"url" : contextPath + '/devolucao/fechamentoEncalhe/pesquisar',
			params : [{
				name : "dataEncalhe",
				value : $('#datepickerDe', fechamentoEncalheController.workspace).val()
			}, {
				name : "fornecedorId",
				value : $('#selectFornecedor', fechamentoEncalheController.workspace).val()
			}, {
				name : "boxId",
				value : $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val()
			},
			{
				name : "aplicaRegraMudancaTipo",
				value : aplicaRegraMudancaTipo
			}],
			
			newp:1
		});
		
		$(".fechamentoGrid", fechamentoEncalheController.workspace).flexReload();
		$(".grids", fechamentoEncalheController.workspace).show();
		
		vDataEncalhe = $('#datepickerDe', fechamentoEncalheController.workspace).val();
		vFornecedorId = $('#selectFornecedor', fechamentoEncalheController.workspace).val();
		vBoxId = $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val();
	},
	
	preprocessamentoGridFechamento : function(resultado) {
		
		if (typeof resultado.mensagens == "object") {
            exibirMensagemDialog(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens, "");
            return;
        } 
		
		if (resultado.rows && resultado.rows.length > 0) {
			$('#bt_cotas_ausentes', fechamentoEncalheController.workspace).show();
			$('.divBotoesPrincipais', fechamentoEncalheController.workspace).show();
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var valorFisico = row.cell.fisico == null ? '' : row.cell.fisico;
			if ( ( row.cell.diferenca == "0" && valorFisico == '' ) ||  valorFisico == '' ) {
					row.cell.diferenca = "";
			}
			
			var fechado = row.cell.fechado == false ? '' : 'disabled="disabled"';
			row.cell.fisico = '<input type="text" onkeypress="fechamentoEncalheController.nextInputExemplares('+index+',window.event);" tabindex="'+index+'" style="width: 60px" id = "'+row.cell.produtoEdicao+'"  name="fisico" value="' + valorFisico + '" onchange="fechamentoEncalheController.onChangeFisico(this, ' + index + ')" ' + fechado + '/>';
		
			row.cell.replicar = '<input type="checkbox"  id="ch'+index+'" name="checkgroupFechamento" onclick="fechamentoEncalheController.replicar(' + index + ');"' + fechado+ '/>';
			
			if (fechado != '') {
				$('.divBotoesPrincipais', fechamentoEncalheController.workspace).hide();
			}
		});
		
		return resultado;
	},
	
	replicarTodos : function(replicar) {
	
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		for (i = 0; i<tabela.rows.length; i++) {
			if (replicar){
			
				fechamentoEncalheController.replicarItem(i);
			
			} else {
				
				fechamentoEncalheController.limparInputsFisico(i);
			}
		}
	},
	
	replicar:function(index){
		$("#sel",this.workspace).attr("checked",false);
		fechamentoEncalheController.replicarItem(index);
	},
	
	limparInputsFisico: function(index) {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var campo = tabela.rows[index].cells[7].firstChild.firstChild;
		
		if (!campo.disabled) {

			campo.val("");
		}
	},
	
	replicarItem : function(index) {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var valor = tabela.rows[index].cells[5].firstChild.innerHTML;
		var campo = tabela.rows[index].cells[7].firstChild.firstChild;
		var diferenca = tabela.rows[index].cells[8].firstChild;
		
		if(campo.disabled){
			return;
		}
		
		campo.value = valor;
		diferenca.innerHTML = "0";
	},
	
	checkAll:function(input){
			
		checkAll(input,"checkgroupFechamento");
		
		fechamentoEncalheController.replicarTodos(input.checked);
	},
	
	onChangeFisico : function(campo, index) {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var devolucao = parseInt(tabela.rows[index].cells[5].firstChild.innerHTML);
		var diferenca = tabela.rows[index].cells[8].firstChild;
		
		if (campo.value == "") {
			diferenca.innerHTML = "";
		} else {
			diferenca.innerHTML =campo.value - devolucao ;			
		}
	},
	
	gerarArrayFisico : function() {
		
		var tabela = $('.fechamentoGrid', fechamentoEncalheController.workspace).get(0);
		var fisico;
		var arr = new Array();
		
		for (i=0; i<tabela.rows.length; i++) {
			fisico = tabela.rows[i].cells[6].firstChild.firstChild.value;
			arr.push(fisico);
		}
		
		return arr;
	},
	
	salvar : function() {
			$.postJSON(
				contextPath + "/devolucao/fechamentoEncalhe/salvar",
				fechamentoEncalheController.populaParamentrosFechamentoEncalheInformados(),
				function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagem(tipoMensagem, listaMensagens);
					}
					fechamentoEncalheController.pesquisar();
					
				},
			  	null,
			   	false
			);
			
	},
	
	popup_encerrarEncalhe : function(isSomenteCotasSemAcao) {

		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		$(".cotasGrid", fechamentoEncalheController.workspace).flexOptions({
			url: contextPath + "/devolucao/fechamentoEncalhe/cotasAusentes",
			params: [{name:'dataEncalhe', value: dataEncalhe }, {name:'isSomenteCotasSemAcao', value: isSomenteCotasSemAcao}],
			newp: 1,
		});
		
		$(".cotasGrid", fechamentoEncalheController.workspace).flexReload();
	},
	
	verificarEncerrarOperacaoEncalhe : function() {

		var dataEncalhe = $('#datepickerDe', fechamentoEncalheController.workspace).val();

		var params = [
			{name:"dataEncalhe", value:dataEncalhe}, 
			{name:"operacao", value: "VERIFICACAO"}
		];
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe",
			params,
			function (result) {
			
				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					
					exibirMensagem(tipoMensagem, listaMensagens);
					
					return;
				}
				
				if (!result.isCotasAusentes) {
					
					fechamentoEncalheController.isFechamento = true;
					
					fechamentoEncalheController.popup_encerrarEncalhe(true);
					
				} else {
					
					if ($( "#dialog-encerrarEncalhe", fechamentoEncalheController.workspace).dialog("isOpen")) {

						$( "#dialog-encerrarEncalhe", fechamentoEncalheController.workspace).dialog("destroy");
					}

					fechamentoEncalheController.popup_encerrar();
				}			
			},
		  	null,
		   	false
		);
	},
	
	popup_encerrar : function() {
		
		$("#dataConfirma", fechamentoEncalheController.workspace).html($("#datepickerDe", fechamentoEncalheController.workspace).val());
		
		$( "#dialog-confirm", fechamentoEncalheController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {

					var params = [
					      {name: 'dataEncalhe', value: $('#datepickerDe', fechamentoEncalheController.workspace).val()}, 
					      {name: 'operacao', value: 'CONFIRMACAO'}
					];
					
					var _this = $(this);

					$.postJSON(
						contextPath + "/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe",
						params,
						function (result) {

							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								
								exibirMensagem(tipoMensagem, listaMensagens);
								
								_this.dialog("destroy");
								
								return;
								
							}

							if (!result.isCotasAusentes) {
								fechamentoEncalheController.isFechamento = true;
								fechamentoEncalheController.popup_encerrarEncalhe(true);
							} else {
								fechamentoEncalheController.popup_encerrar();
							}
							
							fechamentoEncalheController.pesquisar();
							
							_this.dialog("destroy");
						},
					  	null,
					   	false
					);
				},
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				$(".fechamentoGrid", fechamentoEncalheController.workspace).flexReload();
			},
			form: $("#dialog-confirm", this.workspace).parents("form")
		});
	},
	
	encerrarFechamento : function() {
		
		salvar();
		
		/* verificar cotas pendentes */
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/encerrarFechamento",
			{ 'dataEncalhe' : $('#datepickerDe', fechamentoEncalheController.workspace).val() },
			function (result) {
				
			},
		  	null,
		   	true
		);
	},

	confirmar : function(){
		$(".dados", fechamentoEncalheController.workspace).show();
	},
	
	checarTodasCotasGrid : function(checked) {
				
		if (checked) {
			$("input[type=checkbox][name='checkboxGridCotas']", fechamentoEncalheController.workspace).each(function() {
				if (this.disabled == false) {
					//fechamentoEncalheController.
					/*var elem = document.getElementById("textoCheckAllCotas");
					elem.innerHTML = "Desmarcar todos";*/
					var elem = $("#textoCheckAllCotas");
					elem.html("Desmarcar todos");
					$(this).attr('checked', true);
				}
			});
				
        } else {
			/*var elem = document.getElementById("textoCheckAllCotas");
			elem.innerHTML = "Marcar todos";*/
			var elem = $("#textoCheckAllCotas");
			elem.html("Marcar todos");

			$("input[type=checkbox][name='checkboxGridCotas']", fechamentoEncalheController.workspace).each(function(){
				$(this).attr('checked', false);
			});
		}
	},

	preprocessamentoGrid : function(resultado) {	
		
		if (resultado.mensagens) {
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			$(".cotasGrid", fechamentoEncalheController.workspace).hide();
			return resultado;
		}

		$( "#dialog-encerrarEncalhe", fechamentoEncalheController.workspace ).dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: {
				"Postergar": function() {
					fechamentoEncalheController.postergarCotas();
					
				},
				"Cobrar": function() {
					fechamentoEncalheController.veificarCobrancaGerada();
					
				},
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-encerrarEncalhe", this.workspace).parents("form")
		});
		
		//document.getElementById("checkTodasCotas").checked = false;
		$("#checkTodasCotas").attr("checked", false);
		fechamentoEncalheController.checarTodasCotasGrid(false);
		
		$.each(resultado.rows, function(index, row) {
			
			var checkBox = '<span></span>';
			
			if (row.cell.acao == null || row.cell.acao == '') { 
				checkBox = '<input type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" value="' + row.cell.idCota + '" />';	
			} else {
				checkBox = '<input type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" value="' + row.cell.idCota + '" disabled="disabled"/>';	
			}
			
		    row.cell.check = checkBox;
		});

		
		$(".cotasGrid", fechamentoEncalheController.workspace).show();
		
		return resultado;
	},

	obterCotasMarcadas : function() {
 
		var cotasAusentesSelecionadas = new Array();

		$("input[type=checkbox][name='checkboxGridCotas']:checked", fechamentoEncalheController.workspace).each(function(){
			cotasAusentesSelecionadas.push(parseInt($(this).val()));
		});

		return cotasAusentesSelecionadas;
	},
	
	postergarCotas : function() {
		
		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/dataSugestaoPostergarCota",
				{ 'dataEncalhe' : dataEncalhe},
				function (result) {
						
			        $("#dtPostergada", fechamentoEncalheController.workspace).val(result.resultado);
				}
		);
		
		var postergarTodas = $("#checkTodasCotas").attr("checked") == "checked";

		var cotasSelecionadas = postergarTodas ? [] : fechamentoEncalheController.obterCotasMarcadas();

		if (postergarTodas || cotasSelecionadas.length > 0) {
			
			$("#dialog-postergar", fechamentoEncalheController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					
					"Confirmar": function() {
						
						var dataPostergacao = $("#dtPostergada", fechamentoEncalheController.workspace).val();
						var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
						
						$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/postergarCotas",
									{ 'dataPostergacao' : dataPostergacao, 
									  'dataEncalhe' : dataEncalhe, 
									  'idsCotas' : cotasSelecionadas,
									  'postergarTodasCotas' : postergarTodas
									},
									function (result) {
	
										$("#dialog-postergar", fechamentoEncalheController.workspace).dialog("close");
										
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemEncerrarEncalhe');
										}

										$(".cotasGrid", fechamentoEncalheController.workspace).flexReload();
										
								        if (fechamentoEncalheController.isFechamento) {

								        	fechamentoEncalheController.isFechamento = false;
								        	
								        	fechamentoEncalheController.verificarEncerrarOperacaoEncalhe();
								        }
									},
								  	null,
								   	true,
								   	'dialogMensagemEncerrarEncalhe'
							);
					},
					
					"Cancelar": function() {
						
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					
					$("#dtPostergada", fechamentoEncalheController.workspace).val("");
					
					clearMessageDialogTimeout('dialogMensagemEncerrarEncalhe');
				},
				form: $("#dialog-postergar", this.workspace).parents("form")
			});
	
			carregarDataPostergacao();
			
		} else {
			
			var listaMensagens = new Array();
			
			listaMensagens.push('Selecione pelo menos uma cota para postergar!');
			exibirMensagemDialog('WARNING', listaMensagens, 'dialogMensagemEncerrarEncalhe');
		}
	},

	carregarDataPostergacao : function() {

		var dataPostergacao = $("#dtPostergada", fechamentoEncalheController.workspace).val();
		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/carregarDataPostergacao",
				{ 'dataEncalhe' : dataEncalhe, 'dataPostergacao' : dataPostergacao },
				function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemPostergarCotas');
					} else {
						$("#dtPostergada", fechamentoEncalheController.workspace).val(result);
					}
				},
			  	null,
			   	true,
			   	'dialogMensagemPostergarCotas'
		);

	},
	
	veificarCobrancaGerada: function(){
		
		var cobrarTodas  = $("#checkTodasCotas").attr("checked") == "checked";
		
		$.postJSON(contextPath + '/devolucao/fechamentoEncalhe/veificarCobrancaGerada',
				{
					'idsCotas' : fechamentoEncalheController.obterCotasMarcadas(),
					'cobrarTodasCotas': cobrarTodas
				},
		
			function(conteudo){
			
				if(conteudo && conteudo.tipoMensagem == 'WARNING') {
					
					$("#msgRegerarCobranca", fechamentoEncalheController.workspace).text(conteudo.listaMensagens[0]);
					
					$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog({
						resizable : false,
						height : 'auto',
						width : 680,
						modal : true,
						buttons : {
							"Confirmar" : function() {
								
								$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog("close");
								fechamentoEncalheController.cobrarCotas();
							},
							"Cancelar" : function(){
							
								$("#dialog-confirmar-regerar-cobranca", fechamentoEncalheController.workspace).dialog("close");
							}
						},
						form: $("#dialog-confirmar-regerar-cobranca", this.workspace).parents("form")
					});
					
				} else {
					
					fechamentoEncalheController.cobrarCotas();
				}
				
			}, null, true, "dialog-confirmar-regerar-cobranca"
		);
	},
	
	cobrarCotas : function() {

		var dataOperacao = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		var cobrarTodas  = $("#checkTodasCotas").attr("checked") == "checked";

		var idsCotas = cobrarTodas ? [] : fechamentoEncalheController.obterCotasMarcadas();

		$.postJSON(contextPath + "/devolucao/fechamentoEncalhe/cobrarCotas",
					{ 
						'dataOperacao' : dataOperacao, 
						'idsCotas' : idsCotas,
						'cobrarTodasCotas': cobrarTodas
					},
					function (result) {
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemEncerrarEncalhe');
						}

						$(".cotasGrid", fechamentoEncalheController.workspace).flexReload();
					},
				  	null,
				   	true
			);
	},

	gerarArquivoCotasAusentes : function(fileType) {

		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		window.location = 
			contextPath + 
			"/devolucao/fechamentoEncalhe/exportarArquivo?" + 
			"dataEncalhe=" + dataEncalhe + 
			"&sortname=" + $(".cotasGrid", fechamentoEncalheController.workspace).flexGetSortName() +
			"&sortorder=" + $(".cotasGrid", fechamentoEncalheController.workspace).getSortOrder() +
			"&rp=" + $(".cotasGrid", fechamentoEncalheController.workspace).flexGetRowsPerPage() +
			"&page=" + $(".cotasGrid", fechamentoEncalheController.workspace).flexGetPageNumber() +
			"&fileType=" + fileType;

		return false;
	},
	
	imprimirArquivo : function(fileType) {

		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/imprimirArquivo?"
			+ "dataEncalhe=" + vDataEncalhe
			+ "&fornecedorId="+ vFornecedorId
			+ "&boxId=" + vBoxId
			+ "&sortname=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetSortName()
			+ "&sortorder=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).getSortOrder()
			+ "&rp=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},

	verificarMensagemConsistenciaDados : function() {
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/verificarMensagemConsistenciaDados",
			{ 
			    'dataEncalhe' : $('#datepickerDe', fechamentoEncalheController.workspace).val(),
			    'fornecedorId' : $('#selectFornecedor', fechamentoEncalheController.workspace).val(),
				'boxId' :  $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val()
		    },
			function (result) {

				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					if (tipoMensagem == "ERROR"  ){
						exibirMensagem("WARNING", listaMensagens);
					//	fechamentoEncalheController.pesquisar(false);
					} else {
						$('#mensagemConsistenciaDados', fechamentoEncalheController.workspace).html(listaMensagens[0]);
						fechamentoEncalheController.popup_mensagem_consistencia_dados();
					}
				} else {
					fechamentoEncalheController.pesquisar(false);
				}

    		},
		  	null,
		   	false
		);
	},

	popup_mensagem_consistencia_dados : function() {
		$( "#dialog-mensagem-consistencia-dados", fechamentoEncalheController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					fechamentoEncalheController.pesquisar(true);
					$(this).dialog( "close" );
				},
				
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			},
			beforeClose: function() {
				//clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-mensagem-consistencia-dados", this.workspace).parents("form")
		});
	},
	
	 populaParamentrosFechamentoEncalheInformados : function(){
		 
		 var data = new Array();
		 
		 data.push({name:"dataEncalhe", value: $('#datepickerDe', fechamentoEncalheController.workspace).val()});
		 data.push({name:"fornecedorId", value: $('#selectFornecedor', fechamentoEncalheController.workspace).val()});
		 data.push({name:"boxId", value: $('#selectBoxEncalhe', fechamentoEncalheController.workspace).val()});

		 $("input[type=text][name='fisico']").each(function(index, value){
			
			 data.push({name: 'listaFechamento[' + index + '].produtoEdicao', value: $(value).attr('id')});
			 data.push({name: 'listaFechamento[' + index + '].fisico', value: $(value).val()});
		 });
		 
		return data;
	},

	 limpaGridPesquisa : function() {
		 
		 $(".fechamentoGrid", fechamentoEncalheController.workspace).clear();
		 $('#divFechamentoGrid', fechamentoEncalheController.workspace).css("display", "none");
	},
	
	salvarNoEncerrementoOperacao : function() {
		
		$.postJSON(
			contextPath + "/devolucao/fechamentoEncalhe/salvarNoEncerrementoOperacao",
			fechamentoEncalheController.populaParamentrosFechamentoEncalheInformados(),
			function (result) {
				
				var tipoMensagem = result.tipoMensagem;
				
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					
					exibirMensagem(tipoMensagem, listaMensagens);
				} else {
					
					fechamentoEncalheController.verificarEncerrarOperacaoEncalhe();
				}
			},
		  	null,
		   	false
		);
	},
	
	analiticoEncalhe : function() {
		$('#workspace').tabs('addTab', "Anal&iacute;tico Encalhe", "/nds-client/devolucao/fechamentoEncalhe/analitico");
	},
	
	nextInputExemplares : function(curIndex,evt) {
		
		if (evt.keyCode == 13) {
			var nextElement = $('[tabindex=' + (curIndex + 1) + ']');
			nextElement.select();
			nextElement.focus();
		}
	}
	
}, BaseController);
//@ sourceURL=fechamentoEncalhe.js
