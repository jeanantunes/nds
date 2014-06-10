var valeDescontoController = $.extend(true, {
	
	edicoesAssociacao: new Array(),
	
	init: function() {

		valeDescontoController.bindComponents();
	
		valeDescontoController.formatComponents();
		
		valeDescontoController.createGrids();
	},
	
	bindComponents: function() {
	
		$("#inputEdicaoCuponada", valeDescontoController.workspace).keyup(function() {
			valeDescontoController.autoCompletarProdutoEdicaoCuponado();
		});
		
		$("#inputEdicaoCuponada", valeDescontoController.workspace).keyup(function() {
			if(!$.trim(this.value).length) {
				$("#edicaoCuponada", valeDescontoController.workspace).val("");
			}
		});
		
		$("#codigoProdutoAssociacao", valeDescontoController.workspace).change(function() {
			pesquisaProduto.pesquisarPorCodigoProduto('#codigoProdutoAssociacao', '#nomeProdutoAssociacao', 
														null, false, 
														function(result) {
															$("#codigoProdutoAssociacao", valeDescontoController.workspace).val(result.codigo);															
														});
		});
		
		$("#codigoNovoValeDesconto", valeDescontoController.workspace).change(function() {
			valeDescontoController.sugerirProximaEdicao($(this).val());
		});
		
		/*$("#nomeProdutoAssociacao", valeDescontoController.workspace).keyup(function() {
			pesquisaProduto.autoCompletarPorNomeProduto("#nomeProdutoAssociacao");
			$("#nomeProdutoAssociacao", valeDescontoController.workspace).autocomplete("search", $(this).val());
		});*/
		
		$("#nomeValeDesconto", valeDescontoController.workspace).keyup(function() {
			valeDescontoController.autoCompletarValeDesconto();
		});
		
		$("#nomeValeDesconto", valeDescontoController.workspace).blur(function() {
			 if(!$.trim(this.value).length) {
				 $("#codigoValeDesconto", valeDescontoController.workspace).val("");
				 $("#numeroEdicao", valeDescontoController.workspace).val("");
			 }
		});

		$("#vincularRecolhimentoCuponado", valeDescontoController.workspace).change(function() {

			var desabilitar = $(this).attr("checked") == "checked";

			$("#infoRecolhimentoValeDesconto input[type='text']", valeDescontoController.workspace).attr("disabled", desabilitar);
		});
		
		valeDescontoController.bindInputEdicoesAssociacao();
	},
	
	bindInputEdicoesAssociacao: function() {
		
		$("input[name='edicao']", valeDescontoController.workspace).blur(function() {
			
			var codigoProduto = $("#codigoProdutoAssociacao", valeDescontoController.workspace).val();
			
			valeDescontoController.validarEdicaoProduto(codigoProduto, $(this).val(), $(this).attr("numeroEdicao"));
			
			valeDescontoController.gerarInputsEdicao($(this));
		});
	},
	
	formatComponents: function() {
	
		$("#inputEdicaoCuponada", valeDescontoController.workspace).autocomplete({source: []});
		$("#nomeValeDesconto", valeDescontoController.workspace).autocomplete({source: []});
		//$("#nomeProdutoAssociacao", valeDescontoController.workspace).autocomplete({source: []});
		
		$("#valorValeDesconto", valeDescontoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});

		$("#dataRecolhimentoPrevista", valeDescontoController.workspace).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#dataRecolhimentoReal", valeDescontoController.workspace).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});		
	},
	
	createGrids: function() {
	
		$(".valesDescontoGrid", valeDescontoController.workspace).flexigrid({
			dataType : 'json',
			preProcess: valeDescontoController.preProcessGridPesquisa,
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 540,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Situação',
				name : 'situacao',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			} ],
			usepager : true,
			useRp : true,
			sortname: 'id',
			sortorder: 'desc',
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto',
			disableSelect: true
		});		
		
		$(".publicacoesCuponadas", valeDescontoController.workspace).flexigrid({
			dataType : 'json',
			preProcess: valeDescontoController.preProcessGridEdicoesAssociadas,
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Publicação',
				name : 'nome',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Recolhimento',
				name : 'dataRecolhimento',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Situação',
				name : 'situacao',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			} ],
			useRp : true,
			showTableToggleBtn : true,
			width : 754,
			height : 'auto',
			disableSelect: true
		});
	},
	
	pesquisar: function() {
		
		var filtro = $("#filtroValeDesconto", valeDescontoController.workspace).serializeArray();
		
		$(".valesDescontoGrid", valeDescontoController.workspace).flexOptions({
			url: contextPath + '/cadastro/valeDesconto/pesquisar',
			params: filtro,
			newp: 1
		}).flexReload();
	},
	
	preProcessGridPesquisa: function(result) {

		$.each(result.rows, function(index, row) {
			
			var linkEditar = '<a href="javascript:;" onclick="valeDescontoController.editarValeDesconto(' + row.cell.id + ');" style="cursor:pointer">' +
     	  					 '<img title="Editar" src="' + contextPath +'/images/ico_editar.gif" style="margin-right:10px" border="0px" />' +
     	  					 '</a>';

			var linkExcluir = '<a href="javascript:;" onclick="valeDescontoController.removerValeDesconto(' + row.cell.id + ');" style="cursor:pointer">' +
		   	 				  '<img title="Excluir" src="' + contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' +
		   	 				  '</a>';

			row.cell.acao = linkEditar + linkExcluir;
		});
		
		$(".grids", valeDescontoController.workspace).show();
		
		return result;
	},
	
	preProcessGridEdicoesAssociadas: function(result) {
	
		$.each(result.rows, function(index, row) {
			
			var linkExcluir = '<a href="javascript:;" onclick="valeDescontoController.removerAssociacao(' + row.id + ');" style="cursor:pointer">' +
		   	 				  '<img title="Excluir" src="' + contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' +
		   	 				  '</a>';

			row.cell.acao = linkExcluir;
		});
		
		return result;
	},
	
	novoValeDesconto: function() {
		
		valeDescontoController.resetNovoValeDescontoForm();
		
		valeDescontoController.edicoesAssociacao = new Array();
	
		valeDescontoController.popupValeDesconto();
	},
	
	resetNovoValeDescontoForm: function() {
		
		document.formNovoValeDesconto.reset();

		$(".publicacoesCuponadas", valeDescontoController.workspace).clear();
		
		$("#vincularRecolhimentoCuponado", valeDescontoController.workspace).attr("checked", false);
		
		$("#infoRecolhimentoValeDesconto input[type='text']", valeDescontoController.workspace).attr("disabled", false);
	},
	
	popupValeDesconto: function() {
	
		$("#dialog-novo-vale-desconto", valeDescontoController.workspace).dialog({
			resizable: false,
			height:600,
			width:810,
			modal: true,
			title:"Vale Desconto",
			buttons: {
				"Confirmar": function() {

					valeDescontoController.salvar();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				
			},
			form: $("#dialog-novo-vale-desconto", valeDescontoController.workspace).parents("form")
		});
	},
	
	novaAssociacao: function() {
	
		valeDescontoController.resetNovaAssociacaoForm();
		
		$("#dialog-nova-associacao", valeDescontoController.workspace).dialog({
			resizable: false,
			height:300,
			width:340,
			modal: true,
			title:"Vale Desconto",
			buttons: {
				"Confirmar": function() {

					valeDescontoController.associarEdicoes();
					
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				
			},
			form: $("#dialog-nova-associacao", valeDescontoController.workspace).parents("form")
		});
	},
	
	resetNovaAssociacaoForm: function() {
		
		document.formNovaAssociacao.reset();

		$("#tabelaAssociacaoEdicoes [name='edicaoAuto']", valeDescontoController.workspace).remove();
		
		$("[id^='spanAlert']").html("");
		
		$($("input[name='edicao']")[2], valeDescontoController.workspace).attr("onblur", valeDescontoController.gerarInputsEdicao);
		$($("input[name='edicao']")[2], valeDescontoController.workspace).attr("isUltimaEdicao", true);
		$($("input[name='edicao']")[2], valeDescontoController.workspace).attr("numeroEdicao", 3);
	},
	
	editarValeDesconto: function(idValeDesconto) {
	
		$.getJSON(
			contextPath + '/cadastro/valeDesconto/obterPorId',
			{id: idValeDesconto},
			function(result) {

				valeDescontoController.novoValeDesconto();
				
				$.each(result.publicacoesCuponadas, function(index, value) {
				
					valeDescontoController.edicoesAssociacao.push({
						"id": value.id,
						"grid": {
							"id": value.id,
							"codigo": value.codigo,
							"nome": value.nome,
							"numeroEdicao": value.numeroEdicao,
							"dataRecolhimento": value.dataRecolhimento.$,
							"situacao": value.situacao
						}
					});				
				});
				
				valeDescontoController.associarEdicoes();
				
				$.each(result, function(name, val){
				    var $el = $('[name="valeDesconto.'+name+'"]'),
				        type = $el.attr('type');

				    switch(type){
				        case 'checkbox':
				            $el.attr('checked', val);
				            if (val) {
				            	$("#infoRecolhimentoValeDesconto input[type='text']", valeDescontoController.workspace).attr("disabled", true);
				            }
				            break;
				        default:
				            $el.val(val);
				    }
					
					if (name==='valor') {
						$el.val(floatToPrice(val));
					} else if (name === 'dataRecolhimentoPrevista' || name === 'dataRecolhimentoReal') {
						$el.val(val.$);
					}
				});
			}
		);
	},
	
	removerValeDesconto: function(idValeDesconto) {
		
		$("#dialog-excluir", valeDescontoController.workspace).dialog( {
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(
						contextPath + '/cadastro/valeDesconto/remover',
						{id: idValeDesconto},
						function(result) {
							
							$("#dialog-novo-vale-desconto", valeDescontoController.workspace).dialog("close");

							$("#dialog-excluir", valeDescontoController.workspace).dialog("close");
							
							$(".valesDescontoGrid", valeDescontoController.workspace).flexReload();
						}
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-excluir", valeDescontoController.workspace).parents("form")
		});		
	},
	
	removerAssociacao: function(idProdutoEdicao) {
		
		$.each(valeDescontoController.edicoesAssociacao, function(index, value) {
		
			if (value.id == idProdutoEdicao) {
			
				valeDescontoController.edicoesAssociacao.splice(index, 1);
			
				return false;
			}
		});

		valeDescontoController.associarEdicoes();
	},
	
	autoCompletarValeDesconto: function() {
		
		var nome = $("#nomeValeDesconto", valeDescontoController.workspace).val().trim();
		
		if (nome && nome.length > 0){
			$.getJSON(contextPath + '/cadastro/valeDesconto/autoCompleteValesDesconto', 
				{filtro:nome}, 
				function(result){
						
					$("#nomeValeDesconto", valeDescontoController.workspace).autocomplete({
						source: result,
						select: function(event, ui) {
							
							var json = $.parseJSON(ui.item.value);
							
							$("#codigoValeDesconto", valeDescontoController.workspace).val(json.codigo);
							$("#numeroEdicao", valeDescontoController.workspace).val(json.edicao);

							ui.item.value = json.nome;
						},
						delay : 0,
					});
					
					$("#nomeValeDesconto", valeDescontoController.workspace).autocomplete(
						"search", nome
					);
				}
			);
		}
	},
	
	autoCompletarProdutoEdicaoCuponado: function() {
		
		var codigoNomeProduto = $("#inputEdicaoCuponada", valeDescontoController.workspace).val().trim();
		
		if (codigoNomeProduto && codigoNomeProduto.length > 0){
			$.getJSON(contextPath + '/cadastro/valeDesconto/autoCompleteProdutosCuponados', 
				{filtro:codigoNomeProduto}, 
				function(result){
						
					$("#inputEdicaoCuponada", valeDescontoController.workspace).autocomplete({
						source: result,
						select: function(event, ui) {
							$("#edicaoCuponada").val(ui.item.chave.long);
						},
						delay : 0,
					});
					
					$("#inputEdicaoCuponada", valeDescontoController.workspace).autocomplete(
						"search", codigoNomeProduto
					);
				}
			);
		}
	},
	
	gerarInputsEdicao: function(comp) {
	
		if (!$.trim($(comp, valeDescontoController.workspace).val()).length) {
			
			return;
		}
		
		var isUltimaEdicao = $(comp, valeDescontoController.workspace).attr("isUltimaEdicao");
		
		if (isUltimaEdicao) {
			
			var numeroEdicao = $(comp, valeDescontoController.workspace).attr("numeroEdicao");
			
			var proximaEdicao = parseInt(numeroEdicao) + 1;
			
			var id = "edicao" + proximaEdicao;
			
			var newEdicao = " <tr name='edicaoAuto'> <td> "
						  + proximaEdicao + "º Edição: "
						  + " </td> <td style='width:120px'> "
						  + " <input id='" + id + "' type='text' onblur='valeDescontoController.gerarInputsEdicao(\"#" + id + "\")'"
						  + " isUltimaEdicao='true' numeroEdicao='" + proximaEdicao + "' style='width:120px' name='edicao' /> "
						  + " </td> <td> <span id='spanAlert" + proximaEdicao + "'></span> </td> </tr> ";
			
			$("#tabelaAssociacaoEdicoes tbody", valeDescontoController.workspace).append(newEdicao);
			
			$(comp, valeDescontoController.workspace).removeAttr("isUltimaEdicao");
			
			$("#" + id).focus();
			
			valeDescontoController.bindInputEdicoesAssociacao();
		}
	},
	
	obterPorCodigo: function() {
		
		var codigoValeDesconto = $("#codigoValeDesconto", valeDescontoController.workspace).val();
		
		if (codigoValeDesconto === "") {
			
			$("#nomeValeDesconto", valeDescontoController.workspace).val("");
			$("#numeroEdicao", valeDescontoController.workspace).val("");
			
			return;
		}

		$.getJSON(
			contextPath + '/cadastro/valeDesconto/obterPorCodigo',
			{codigo: codigoValeDesconto},
			function(result) {
				$("#nomeValeDesconto", valeDescontoController.workspace).val(result.nome);
				$("#numeroEdicao", valeDescontoController.workspace).val("");
			}
		);
	},
	
	sugerirProximaEdicao: function(codigoValeDesconto) {
		
		$.getJSON(
			contextPath + '/cadastro/valeDesconto/sugerirProximaEdicao',
			{codigo: codigoValeDesconto},
			function(result) {
				$("#idProduto", valeDescontoController.workspace).val(result.idProduto);
				$("#nomeNovoValeDesconto", valeDescontoController.workspace).val(result.nome);
				$("#edicaoNovoValeDesconto", valeDescontoController.workspace).val(result.numeroEdicao);
			}
		);
	},
	
	salvar: function() {
		
		var valeDesconto = $("#formNovoValeDesconto", valeDescontoController.workspace).serializeArray();
		
		$.each(valeDesconto, function(index, value) {
			if (value.name === "valeDesconto.valor") {
				valeDesconto[index].value = priceToFloat(value.value);
			}
		});
		
		$.each(valeDescontoController.edicoesAssociacao, function(index, value) {
			valeDesconto.push({name:"valeDesconto.publicacoesCuponadas[" + index + "].id", value: value.id});
		});
		
		$.postJSON(
			contextPath + '/cadastro/valeDesconto/salvar',
			valeDesconto,
			function(result) {
				
				$("#dialog-novo-vale-desconto", valeDescontoController.workspace).dialog("close");
				
				$(".valesDescontoGrid", valeDescontoController.workspace).flexReload();
			}
		);
	},
	
	validarEdicaoProduto: function(codigoProduto, numeroEdicao, inputEdicao) {
		
		if (!codigoProduto || !numeroEdicao) {

			return;
		}
		
		$.getJSON(
			contextPath + '/cadastro/valeDesconto/validarEdicao',
			{codigoProduto: codigoProduto, numeroEdicao: numeroEdicao},
			function(result) {
				
				var edicaoValida = result;
				
				if (edicaoValida) {
				
					$("#spanAlert" + inputEdicao, valeDescontoController.workspace).html("");
					
					var duplicado = false;

					$.each(valeDescontoController.edicoesAssociacao, function(index, value) {
		
						if (value.id == edicaoValida.id) {
							duplicado = true;
							return false;
						}
					});
					
					if (duplicado) {

						valeDescontoController.showAlertEdicao('Esta edição já foi selecionada', inputEdicao);
						
					} else {
					
						valeDescontoController.edicoesAssociacao.push({
							"id": edicaoValida.id,
							"grid": {
								"id": edicaoValida.id,
								"codigo": edicaoValida.codigo,
								"nome": edicaoValida.nome,
								"numeroEdicao": edicaoValida.numeroEdicao,
								"dataRecolhimento": edicaoValida.dataRecolhimento.$,
								"situacao": edicaoValida.situacao
							}
						});
					}
					
				} else {

					valeDescontoController.showAlertEdicao('Edição não existe para este produto', inputEdicao);
				}
			}
		);
	},
	
	showAlertEdicao: function(msg, inputEdicao) {
		
		var img = "<img src='" + contextPath + "/images/ico_alert.gif' title='" + msg + "' />";

		$("#spanAlert" + inputEdicao, valeDescontoController.workspace).html(img);
	},
	
	associarEdicoes: function() {
	
		var rows = new Array();
		
		$.each(valeDescontoController.edicoesAssociacao, function(index, value) {
			rows.push({"id": value.id, "cell" : value.grid});			
		});

		$(".publicacoesCuponadas", valeDescontoController.workspace).flexAddData({
			rows: rows,
			total: valeDescontoController.edicoesAssociacao.length,
			page: 1
		});
	}
	
}, BaseController);

$(document).ready(function() {
	valeDescontoController.init();
});

//@ sourceURL=valeDesconto.js