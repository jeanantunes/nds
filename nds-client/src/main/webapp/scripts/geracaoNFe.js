var geracaoNFeController = $.extend({
	
	/**
	 * path de geração de nfe
	 */
	path : contextPath + '/expedicao/geracaoNFe/',
	
	geracaoLiberada : false,
	
	/**
	 * objeto utilizado para encapsular os dados do filtro de pesquisa
	 */
	filtroPesquisa : {
		naturezaOperacao:null,
		intervaloMovimentoDe:null, 
		intervaloMovimentoAte:null,
		dataEmissao:null,
		intervaloBoxDe:null, 
		intervaloBoxAte:null,
		intervaloCotaDe:null, 
		intervaloCotaAte:null,
		listaFornecedores:null,
		idRoteiro:null,
		idRota:null,
	},
	
	
	/**
	 * método executado ao carregar a tela
	 */
	init : function() {
		this.initInputs();
		this.initDialog();
		this.initButtons();
		this.initFlexiGrids();
		this.initFiltroDatas();
		
		$('.fieldFiltro :input, .fieldFiltro select', this.workspace).on('change', function() {
			geracaoNFeController.geracaoLiberada = false;
		});
		
		params = [];
		params.push({name: 'tipoEmitente', value: 'DISTRIBUIDOR'});
		params.push({name: 'tipoDestinatario', value: 'COTA'});
		
		$.postJSON(contextPath + '/administracao/naturezaOperacao/obterNaturezasOperacoesPorEmitenteDestinatario', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			
			$("#geracaoNfe-filtro-naturezaOperacao").empty();
			
			$('#geracaoNfe-filtro-naturezaOperacao').append($('<option>', { 
		        value: '-1',
		        text : 'Selecione...'
		    }));
			
			$.each(data.rows, function (i, row) {
			    $('#geracaoNfe-filtro-naturezaOperacao').append($('<option>', { 
			        value: row.cell.key,
			        text : row.cell.value
			    }));
			});
			
		});
		
	},
	
	initFiltroDatas : function(){
		
	    $.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor',
				null, 
				function(result) {
			
					$("#geracaoNfe-filtro-movimentoDe", this.workspace).val(result);
					
					$("#geracaoNfe-filtro-movimentoAte", this.workspace).val(result);
		        }
		); 
	},
	
	/**
	 * inicializa os inputs com mascaras e formatações e valores iniciais.
	 */
	initInputs : function() {
		
		$(".input-date").datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
		
		$(".input-date").mask("99/99/9999");
		$("#geracaoNfe-filtro-dataEmissao").val(formatDateToString(new Date()));
		
		$("#geracaoNfe-filtro-selectFornecedoresDestinatarios").multiselect({
			selectedList : 6,
		});
		$("#geracaoNfe-filtro-selectFornecedoresDestinatarios").multiselect("disable");
		
		$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect({
			multiple : false,
			selectedList : 1,
			minWidth : 212,
		});
		$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect("disable");
		
		$("#geracaoNfe-filtro-selectFornecedores").multiselect({
			selectedList : 6
		}).multiselect("checkAll");
		
	},
	
	/**
	 * inicializa os dialogs
	 */
	initDialog : function() {
		
		var _this = this;
		
		$("#geracaoNfe-dialog-cotasSuspensasConfirmar").dialog({
			autoOpen : false,
			resizable : false,
			width : 400,
			modal : true,
			buttons : {
				"Confirma	" : function() {
					_this.transferirSuplementar();
					$(this).dialog("close");
					
				},
				"Cancelar" : function (){
					$(this).dialog("close");
					$('#geracaoNfe-dialog-cotasSuspensas').dialog("open");
				}
			},
		});
		
		$("#geracaoNfe-dialog-cotasSuspensas").dialog({
			autoOpen : false,
			resizable : false,
			width : 400,
			modal : true,
			buttons : {
				"Confirma	" : function() {
					_this.transferirSuplementar();
					$(this).dialog("close");
					
				},
				"Cancelar" : function (){
					$(this).dialog("close");
					$('#geracaoNfe-dialog-cotasSuspensas').dialog("open");
				}
			}
		});
	},
	
	
	/**
	 * inicializa os botões com suas funções de click
	 */
	initButtons : function() {
		
		var _this = this;
		
		
		$("#geracaoNfe-btnPesquisar", this.workspace).click(function() {
			_this.pequisar();
		});

		$("#geracaoNfe-btnGerar", this.workspace).click(function() {
			_this.btnGerarOnClick();
		});

		$("#geracaoNfe-btnImprimirXLS", this.workspace).click(function() {
			_this.imprimir('XLS');
		});
		$("#geracaoNfe-btnImprimirPDF", this.workspace).click(function() {
			_this.imprimir('PDF');
		});

		$('#geracaoNfe-checkboxCheckAllCotasSuspensas', this.workspace).change(function() {
			$(".checkboxCheckCotasSuspensas", this.workspace).attr('checked',
					$(this).attr('checked') == 'checked');
		});	
		
	},
	
	/**
	 * Imprime o resultado da pesquisa no formato de arquivo parametrizado
	 * 
	 * @param fileType - tipo do arquivo para impressão
	 */
	imprimir : function(fileType) {
		
		var params = new Array();
		
		params.push({name:"filtro.idNaturezaOperacao" , value: $("#geracaoNfe-filtro-naturezaOperacao").val()});
		params.push({name:"filtro.dataInicial" , value: $("#geracaoNfe-filtro-movimentoDe").val()});
		params.push({name:"filtro.dataFinal" , value: $("#geracaoNfe-filtro-movimentoAte").val()});
		params.push({name:"filtro.intervaloBoxInicial" , value: $("#geracaoNfe-filtro-inputIntervaloBoxDe").val()});
		params.push({name:"filtro.intervaloBoxFinal" , value: $("#geracaoNfe-filtro-inputIntervaloBoxAte").val()});
		params.push({name:"filtro.intervalorCotaInicial" , value: $("#geracaoNfe-filtro-inputIntervaloCotaDe").val()});
		params.push({name:"filtro.intervalorCotaFinal" , value: $("#geracaoNfe-filtro-inputIntervaloCotaAte").val()});
		params.push({name:"filtro.dataEmissao" , value: $("#geracaoNfe-filtro-dataEmissao").val()});
		params.push({name:"filtro.idNaturezaOperacao" , value: $("#geracaoNfe-filtro-naturezaOperacao").val()});
		params.push({name:"filtro.idRoteiro" , value: $("#geracaoNfe-filtro-listRoteiro").val()});
		params.push({name:"filtro.idRota" , value: $("#geracaoNfe-filtro-listRota").val()});
		
		// Por limitacao do vRaptor, nao instancia dentro do filtro
		if($('input[name^="tipoDestinatario"]:checked').val() != 'FORNECEDOR') {
			
			if($(".emissaoRegimeEspecial").is(":visible")) {
				
				params.push({name:"notaFiscalTipoEmissaoRegimeEspecial", value: $("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").val()});
			}
		}
		
		if ($('#geracaoNfe-filtro-selectFornecedores').val()) {
			$.each($("#geracaoNfe-filtro-selectFornecedores").val(), function(index, v) {
				params.push({name : "filtro.listIdFornecedor[]", value : v});
			});
		}
		
		params.push({name:'fileType', value: fileType});
		
		$.fileDownload(this.path + 'exportar', {
			httpMethod : "POST",
			data : params
		});
	},
	
	
	processCotasSuspensas : function(tdDiv, pid) {
		var idCota = tdDiv.innerHTML;
		var _this = this;

		var checkBox = document.createElement("input");
		checkBox.type = "checkbox";

		checkBox.className = "checkboxCheckAllCotasSuspensas";

		checkBox.value = idCota;

		$(checkBox).change(function() {
			_this.mapCotasSuspensas[idCota + ''] = $(this).attr('checked') == 'checked';
		});

		$(tdDiv).empty();
		tdDiv.appendChild(checkBox);
		
	},
	
	initFlexiGrids : function() {
		var _this = this;
		
		$("#geracaoNfe-flexigrid-pesquisa").flexigrid({
			preProcess : _this.preProcessGridPesquisa,
			colModel : _this.colunasGridPesquisa,
			dataType : 'json',
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
			
		});
		
		$("#geracaoNfe-flexigrid-fornecedor-pesquisa").flexigrid({
			preProcess : _this.preProcessGridPesquisa,
			colModel : _this.colunasGridForncedorPesquisa,
			dataType : 'json',
			sortname : "idFornecedor",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
			
		});
		
		$("#geracaoNfe-gridCotasSuspensas").flexigrid({
			preProcess : _this.preProcessGridPesquisa,
			colModel : _this.colunasGridPesquisa,
			dataType : 'json',
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
			
		});
		
	},
	
	/**
	 * Metodo de pre-processamento dos dados inseridos na grid Pesquisa
	 * 
	 * @param data - dados inseridos na grid
	 * @returns dados normalizados para a grid
	 */
	preProcessGridPesquisa : function(data) {
		
		if (typeof data.mensagens == "object") {
		
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			$("#geracaoNfe-pesquisa", geracaoNFeController.workspace).empty();
		
		} else {
			
			for(var index in data.rows) {
				
				if(data.rows[index].cell["situacaoCadastro"] && data.rows[index].cell["situacaoCadastro"] == 'SUSPENSO') {
					data.rows[index].cell["situacaoCadastro"] = '<a href="javascript:;" ><img src="' + contextPath + '/images/ico_suspenso.gif" border="0" />';
					if(data.rows[index].cell["notaFiscalConsolidada"]) {
						data.rows[index].cell["numeroCota"] = '<span style="color: red;">'+ data.rows[index].cell["numeroCota"] +' *</span>';
					}
				} else if(data.rows[index].cell["situacaoCadastro"] && data.rows[index].cell["situacaoCadastro"] == 'INATIVO') {
				
					data.rows[index].cell["situacaoCadastro"] = '<a href="javascript:;" ><img src="' + contextPath + '/images/ico_inativo.gif" border="0" />';
				
				} else {
					if(data.rows[index].cell["notaFiscalConsolidada"]) {
						data.rows[index].cell["numeroCota"] = '<span style="color: red;">'+ data.rows[index].cell["numeroCota"] +' *</span>';
					}
					
					data.rows[index].cell["situacaoCadastro"] = "";
				}
				data.rows[index].cell["total"] = floatToPrice(data.rows[index].cell["total"]);
				data.rows[index].cell["totalDesconto"] = floatToPrice(data.rows[index].cell["totalDesconto"]);
				
			}
			return data;
		}
	},
	
	/**
	 * objeto utilizado para encapsular as colunas da grid de Pesquisas
	 */
	colunasGridPesquisa:[ {
		display : 'Cota',
		name : 'numeroCota',
		width : 50,
		sortable : true,
		align : 'left',
	}, {
		display : 'Nome',
		name : 'nomeCota',
		width : 385,
		sortable : true,
		align : 'left',
	}, {
		display : 'Total Exemplares',
		name : 'exemplares',
		width : 110,
		sortable : true,
		align : 'center',
	}, {
		display : 'Total R$',
		name : 'total',
		width : 120,
		sortable : true,
		align : 'right',
	},  {
		display : 'Total Desconto R$',
		name : 'totalDesconto',
		width : 100,
		sortable : true,
		align : 'right',
	}, {
		display : 'Suspensa',
		name : 'situacaoCadastro',
		width : 100,
		sortable : true,
		align : 'center',
	}],
	
	/**
	 * objeto utilizado para encapsular as colunas da grid de Pesquisas
	 */
	colunasGridForncedorPesquisa:[ {
		display : 'Fornecedor',
		name : 'numeroFornecedor',
		width : 100,
		sortable : true,
		align : 'left',
	}, {
		display : 'Nome',
		name : 'nomeFornecedor',
		width : 385,
		sortable : true,
		align : 'left',
	}, {
		display : 'Total Exemplares',
		name : 'exemplares',
		width : 110,
		sortable : true,
		align : 'center',
	}, {
		display : 'Total R$',
		name : 'total',
		width : 120,
		sortable : true,
		align : 'right',
	},  {
		display : 'Total Desconto R$',
		name : 'totalDesconto',
		width : 100,
		sortable : true,
		align : 'right',
	}],
	
	
	/**
	 * Realiza pesquisa de acordo com os dado do filtro 
	 * e popula a grid de pesquisa:"geracaoNfe-pesquisa"
	 */
	pesquisar : function() {
		
		geracaoNFeController.geracaoLiberada = true;
		
		if($("#geracaoNfe-filtro-naturezaOperacao").val() < 0) {
			exibirMensagem('WARNING', ['Selecione uma Natureza de Operação.']);
			return;
		}
		
		var params = [];
		
		params.push({name:"filtro.idNaturezaOperacao" , value: $("#geracaoNfe-filtro-naturezaOperacao").val()});
		params.push({name:"filtro.dataInicial" , value: $("#geracaoNfe-filtro-movimentoDe").val()});
		params.push({name:"filtro.dataFinal" , value: $("#geracaoNfe-filtro-movimentoAte").val()});
		params.push({name:"filtro.intervaloBoxInicial" , value: $("#geracaoNfe-filtro-inputIntervaloBoxDe").val()});
		params.push({name:"filtro.intervaloBoxFinal" , value: $("#geracaoNfe-filtro-inputIntervaloBoxAte").val()});
		params.push({name:"filtro.intervalorCotaInicial" , value: $("#geracaoNfe-filtro-inputIntervaloCotaDe").val()});
		params.push({name:"filtro.intervalorCotaFinal" , value: $("#geracaoNfe-filtro-inputIntervaloCotaAte").val()});
		params.push({name:"filtro.dataEmissao" , value: $("#geracaoNfe-filtro-dataEmissao").val()});
		params.push({name:"filtro.idRoteiro" , value: $("#geracaoNfe-filtro-selectRoteiro").val()});
		params.push({name:"filtro.idRota" , value: $("#geracaoNfe-filtro-selectRota").val()});
		
		// FIXME: Verificar o motivo do vRaptor nao instanciar
		// Por limitacao do vRaptor, nao instancia dentro do filtro
		if($('input[name^="tipoDestinatario"]:checked').val() != 'FORNECEDOR') {
			
			if($(".emissaoRegimeEspecial").is(":visible")) {
				
				params.push({name:"notaFiscalTipoEmissaoRegimeEspecial", value: $("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").val()});
			}
		} else {
			
			params.push({name:"filtro.emissaoPorEditor" , value: $("#geracaoNfe-filtro-emissaoPorEditor").is(':checked')});
			params.push({name:"filtro.emissaoPorDestinacaoEncalhe" , value: $("#geracaoNfe-filtro-emissaoPorDestinacaoEncalhe").is(':checked')});
		}
		
		if ($('#geracaoNfe-filtro-selectFornecedores').val()) {
			$.each($("#geracaoNfe-filtro-selectFornecedores").val(), function(index, v) {
				params.push({name : "filtro.listIdFornecedor[]", value : v});
			});
		}
		
		var mensagens = []; 
		if($("#geracaoNfe-filtro-movimentoDe").val() == '' || $("#geracaoNfe-filtro-movimentoAte").val() == '') {
			mensagens.push('A data de movimento não nula');
			exibirMensagem('WARNING', mensagens);
			return false;
		} else {
			var retorno = validarDatas($("#geracaoNfe-filtro-movimentoDe").val(), $("#geracaoNfe-filtro-movimentoAte").val());
			
			if(!retorno){			
				mensagens.push('A data de movimento final não pode ser maior que a data inicial');
				exibirMensagem('WARNING', mensagens);
				return false;
			}
		}
		
		var grid;
		
		if($('input[name=tipoDestinatario]:checked').val() == 'FORNECEDOR') {
			grid = $("#geracaoNfe-flexigrid-fornecedor-pesquisa");
		} else {
			grid = $("#geracaoNfe-flexigrid-pesquisa");
		}
		
		var uri = "pesquisar";
		
		grid.flexOptions({
			"url" : this.path + uri,
			params : params,
			newp : 1
		});
		
		grid.flexReload();
		
		if($('input[name=tipoDestinatario]:checked').val() == 'FORNECEDOR') {
			$(".grids").hide();
			$(".grids-forn").show();
		} else {
			$(".grids-forn").hide();
			$(".grids").show();
		}
	
	},
	
	gridReaload : function(grid, uri) {
		
		var params = [{
			name : "dataEmissao",
			value : this.filtroPesquisa.dataEmissao
		},{
			name : "idRoteiro",
			value : this.filtroPesquisa.idRoteiro	
		},{
			name : "idRota",
			value : this.filtroPesquisa.idRota	
		},{
			name : "intervaloBoxDe",
			value : this.filtroPesquisa.intervaloBoxDe	
		},{
			name : "intervaloBoxAte",
			value : this.filtroPesquisa.intervaloBoxAte	
		},{
			name : "intervaloCotaDe",
			value : this.filtroPesquisa.intervaloCotaDe	
		},{
			name : "intervaloCotaAte",
			value : this.filtroPesquisa.intervaloCotaAte	
		},{
			name : "intervaloMovimentoDe",
			value : this.filtroPesquisa.intervaloMovimentoDe	
		},{
			name : "intervaloMovimentoAte",
			value : this.filtroPesquisa.intervaloMovimentoAte	
		}];
		
		if (this.filtroPesquisa.listaFornecedores) {
			$.each(this.filtroPesquisa.listaFornecedores, function(index, value) {
				params.push({
					'name' : "listaIdFornecedores[]", 'value' : value
				});
			});
		}
	},
	
	/**
	 * Gerar Nfes para resultados da pesquisa e para cotas ausentes selecionadas
	 */
	btnGerarOnClick : function() {
		this.mapCotasSuspensas = new Object();
		
		$('#geracaoNfe-checkboxCheckAllCotasSuspensas', this.workspace).attr('checked', false);
		var _this = this;
		
		var params = [];
		
		params.push({name:"filtro.idNaturezaOperacao" , value: $("#geracaoNfe-filtro-naturezaOperacao").val()});
		params.push({name:"filtro.dataInicial" , value: $("#geracaoNfe-filtro-movimentoDe").val()});
		params.push({name:"filtro.dataFinal" , value: $("#geracaoNfe-filtro-movimentoAte").val()});
		params.push({name:"filtro.intervaloBoxInicial" , value: $("#geracaoNfe-filtro-inputIntervaloBoxDe").val()});
		params.push({name:"filtro.intervaloBoxFinal" , value: $("#geracaoNfe-filtro-inputIntervaloBoxAte").val()});
		params.push({name:"filtro.intervalorCotaInicial" , value: $("#geracaoNfe-filtro-inputIntervaloCotaDe").val()});
		params.push({name:"filtro.intervalorCotaFinal" , value: $("#geracaoNfe-filtro-inputIntervaloCotaAte").val()});
		params.push({name:"filtro.dataEmissao" , value: $("#geracaoNfe-filtro-dataEmissao").val()});
		params.push({name:"filtro.idNaturezaOperacao" , value: $("#geracaoNfe-filtro-naturezaOperacao").val()});
		params.push({name:"filtro.idRoteiro" , value: $("#geracaoNfe-filtro-listRoteiro").val()});
		params.push({name:"filtro.idRota" , value: $("#geracaoNfe-filtro-listRota").val()});
		
		if ($('#geracaoNfe-filtro-selectFornecedores').val()) {
			$.each($("#geracaoNfe-filtro-selectFornecedores").val(), function(index, v) {
				params.push({name : "filtro.listIdFornecedor[]", value : v});
			});
		}
		_this.gerar();
		
		// $.postJSON(this.path + 'hasCotasSuspensas.json', params, function(data) {
		// 	var tipoMensagem = data.tipoMensagem;
		// 	var listaMensagens = data.listaMensagens;

		// 	if (tipoMensagem && listaMensagens) {
		// exibirMensagemDialog(tipoMensagem, listaMensagens, "");
		// 	}
		// 	if (data.cotasSuspensas) {
		// 		_this.$dialogCotasSuspensas.dialog("open");
		// 		_this.gridReaload(_this.$gridCotasSuspensas,
		// 				'buscaCotasSuspensas.json');
		// 	} else {
		//}

		//});
	},
	
	gerar : function() {

		if(!geracaoNFeController.geracaoLiberada) {
			
			exibirMensagem("WARNING", ["Efetue a pesquisa para habilitar a geração!"]);
			return;
		}
		
		var params = new Array();
		
		params.push({name:"filtro.idNaturezaOperacao" , value: $("#geracaoNfe-filtro-naturezaOperacao").val()});
		params.push({name:"filtro.dataInicial" , value: $("#geracaoNfe-filtro-movimentoDe").val()});
		params.push({name:"filtro.dataFinal" , value: $("#geracaoNfe-filtro-movimentoAte").val()});
		params.push({name:"filtro.intervaloBoxInicial" , value: $("#geracaoNfe-filtro-inputIntervaloBoxDe").val()});
		params.push({name:"filtro.intervaloBoxFinal" , value: $("#geracaoNfe-filtro-inputIntervaloBoxAte").val()});
		params.push({name:"filtro.intervalorCotaInicial" , value: $("#geracaoNfe-filtro-inputIntervaloCotaDe").val()});
		params.push({name:"filtro.intervalorCotaFinal" , value: $("#geracaoNfe-filtro-inputIntervaloCotaAte").val()});
		params.push({name:"filtro.dataEmissao" , value: $("#geracaoNfe-filtro-dataEmissao").val()});
		params.push({name:"filtro.idNaturezaOperacao" , value: $("#geracaoNfe-filtro-naturezaOperacao").val()});
		params.push({name:"filtro.idRoteiro" , value: $("#geracaoNfe-filtro-listRoteiro").val()});
		params.push({name:"filtro.idRota" , value: $("#geracaoNfe-filtro-listRota").val()});
		
		if($('input[name^="tipoDestinatario"]:checked').val() != 'FORNECEDOR') {
			
			if($(".emissaoRegimeEspecial").is(":visible")) {
				params.push({name:"notaFiscalTipoEmissaoRegimeEspecial", value: $("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").val()});
			}
		}
		
		if ($('#geracaoNfe-filtro-selectFornecedores').val()) {
			$.each($("#geracaoNfe-filtro-selectFornecedores").val(), function(index, v) {
				params.push({name : "filtro.listIdFornecedor[]", value : v});
			});
		}
		var todas = $('#checkboxCheckAllCotasSuspensas', this.workspace).checked;
		
		var cotasSuspensas = $(".checkboxCheckCotasSuspensas", this.workspace);
		
		var listaCotasSuspensas = new Array;
		
		for (var index in cotasSuspensas) {
			if (cotasSuspensas[index].checked) {
				listaCotasSuspensas.push(cotasSuspensas[index].value);
			}
		}
		
		params = serializeArrayToPost('idCotasSuspensas', listaCotasSuspensas, params);
		params['dataEmissao'] = $("#datepickerEmissao", this.workspace).val();
		params['todasCotasSuspensa'] = todas;
		
		var _this = this;
		
		$.postJSON(this.path + 'gerarNotasFiscais', params, function(data) {
			
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			exibirMensagem("SUCCESS", ["Operação realizada com sucesso!"]);
			_this.gridReaload(_this.$gridNFe, 'pesquisar');
			
		});
	},
	
	/**
	 * funcao responsavel por carregar o combos por Roteiro
	 */
	changeRoteiro : function() {
    	
        var boxDe = $("#geracaoNfe-filtro-inputIntervaloBoxDe").val();
    	
    	var boxAte = $("#geracaoNfe-filtro-inputIntervaloBoxAte").val();
    	
    	var idRota = $("#geracaoNfe-filtro-selectRota").val();
    	
    	var idRoteiro = $("#geracaoNfe-filtro-selectRoteiro").val();
     	
     	var params = [{
				            name : "idRoteiro",
				            value : idRoteiro	
						  }];
     	
     	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRoteiro', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRoteiro = result[2];
 		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRota", geracaoNFeController.workspace), listaRota ,idRota);  
    		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-inputIntervaloBoxDe", geracaoNFeController.workspace), listaBox ,boxDe);
     		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-inputIntervaloBoxAte", geracaoNFeController.workspace), listaBox ,boxAte);
    		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRoteiro", geracaoNFeController.workspace), listaRoteiro ,idRoteiro); 
    	    }    
		);
	},
	
	verificarTipoDestinatario : function(element) {
		
		$('input[name^="tipoDestinatario"]:not(:checked)').removeAttr('checked');
		$(element).attr('checked', true);
		
		if(element.value != "FORNECEDOR") {
			$("#geracaoNfe-filtro-selectFornecedoresDestinatarios option:selected").removeAttr("selected");
			$("#geracaoNfe-filtro-selectFornecedoresDestinatarios").multiselect("disable");
		} else {
			$("#geracaoNfe-filtro-selectFornecedoresDestinatarios").multiselect("enable");
		}
		
		var emitente = '';
		if(element.value == 'COTA') {
			emitente = 'DISTRIBUIDOR';
		} else if(element.value == 'DISTRIBUIDOR') {
			emitente = 'COTA';
		} else if(element.value == 'FORNECEDOR') {
			emitente = 'DISTRIBUIDOR';
		}
		
		params = [];
		params.push({name: 'tipoEmitente', value: emitente});
		params.push({name: 'tipoDestinatario', value: element.value});
		
		$.postJSON(contextPath + '/administracao/naturezaOperacao/obterNaturezasOperacoesPorEmitenteDestinatario', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			
			$("#geracaoNfe-filtro-naturezaOperacao").empty();
			
			$('#geracaoNfe-filtro-naturezaOperacao').append($('<option>', { 
		        value: '-1',
		        text : 'Selecione...'
		    }));
			
			$.each(data.rows, function (i, row) {
			    $('#geracaoNfe-filtro-naturezaOperacao').append($('<option>', { 
			        value: row.cell.key,
			        text : row.cell.value
			    }));
			});
			
			if($('input[name^="tipoDestinatario"]:checked').val() != 'FORNECEDOR') {
				geracaoNFeController.verificarRegimeEspecialNaturezaOperacao($('#geracaoNfe-filtro-naturezaOperacao'));
				$(".emissaoEditorDestinacaoEncalhe").hide();
			} else {
				$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect("disable");
				$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect("hide");
				$(".emissaoRegimeEspecial").hide();
				$(".emissaoEditorDestinacaoEncalhe :input[type='checkbox']").attr('checked', true);
				$(".emissaoEditorDestinacaoEncalhe").show();
			}
			
		});
		
	},
	
	verificarRegimeEspecialNaturezaOperacao : function(el) {
		params = [];
		params.push({name: 'naturezaOperacaoId', value: el.value});
		
		$.postJSON(this.path + 'verificarRegimeEspecialNaturezaOperacao', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			
			if($('input[name^="tipoDestinatario"]:checked').val() != 'FORNECEDOR' 
					&& (data.tipoEmissaoRegimeEspecial == 'CONSOLIDA_EMISSAO_POR_DESTINATARIO'
						|| data.tipoEmissaoRegimeEspecial == 'CONSOLIDA_EMISSAO_A_JORNALEIROS_DIVERSOS')) {
				
				$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect("enable");
				$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect("show");
				$(".emissaoRegimeEspecial").show();
				
			} else {
				
				$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect("disable");
				$("#geracaoNfe-filtro-selectRegimeEspecialConsolidado").multiselect("hide");
				$(".emissaoRegimeEspecial").hide();
				
			}
		});
	},
	
	/**
	 * Recarregar combos por Box
	 */
    changeBox : function() {
		
    	var boxDe = $("#geracaoNfe-filtro-inputIntervaloBoxDe").val();
    	
    	var boxAte = $("#geracaoNfe-filtro-inputIntervaloBoxAte").val();
    	
    	var idRota = $("#geracaoNfe-filtro-selectRota").val();
    	
    	var idRoteiro = $("#geracaoNfe-filtro-selectRoteiro").val();
    	
    	var params = [{
			            name : "codigoBoxDe",
			            value : boxDe	
					  },{
						name : "codigoBoxAte",
						value : boxAte
					  }];
    	
    	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorBox', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		    
    		    var listaRoteiro = result[1];
    		    
    		    var listaBox = result[2];
    		
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRota", geracaoNFeController.workspace), listaRota ,idRota);
 		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRoteiro", geracaoNFeController.workspace), listaRoteiro ,idRoteiro); 
    		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-inputIntervaloBoxDe", geracaoNFeController.workspace), listaBox ,boxDe);
     		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-inputIntervaloBoxAte", geracaoNFeController.workspace), listaBox ,boxAte);
    	    }    
		);
	},
	
	/**
	 * Recarregar combos por Rota
	 */
    changeRota : function() {
    	
        var boxDe = $("#geracaoNfe-filtro-boxDe").val();
    	
    	var boxAte = $("#geracaoNfe-filtro-boxAte").val();
    	
    	var idRota = $("#geracaoNfe-filtro-selectRota").val();
    	
    	var idRoteiro = $("#geracaoNfe-filtro-selectRoteiro").val();
    	
    	var params = [{
			            name : "idRota",
			            value : idRota	
					  }];
	    
    	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRota', params, 
			function(result) {
    		
    		    var listaRoteiro = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRota = result[2];

    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-boxDe", geracaoNFeController.workspace), listaBox, boxDe);
 		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-boxAte", geracaoNFeController.workspace), listaBox, boxAte);
 		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRoteiro", geracaoNFeController.workspace), listaRoteiro, idRoteiro); 
    		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRota", geracaoNFeController.workspace), listaRota, idRota);
    	    }    
		);
	},
	
	/**
	 * Recarregar combos por Roteiro
	 */
    changeRoteiro : function() {
    	
        var boxDe = $("#geracaoNfe-filtro-inputIntervaloBoxDe").val();
    	
    	var boxAte = $("#geracaoNfe-filtro-inputIntervaloBoxAte").val();
    	
    	var idRota = $("#geracaoNfe-filtro-selectRota").val();
    	
    	var idRoteiro = $("#geracaoNfe-filtro-selectRoteiro").val();
     	
     	var params = [{
				            name : "idRoteiro",
				            value : idRoteiro	
						  }];
     	
     	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRoteiro', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRoteiro = result[2];
 		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRota", geracaoNFeController.workspace), listaRota, idRota);  
    		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-inputIntervaloBoxDe", geracaoNFeController.workspace), listaBox, boxDe);
     		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-inputIntervaloBoxAte", geracaoNFeController.workspace), listaBox, boxAte);
    		    
    		    geracaoNFeController.recarregarCombo($("#geracaoNfe-filtro-selectRoteiro", geracaoNFeController.workspace), listaRoteiro, idRoteiro); 
    	    }    
		);
	},
	
	/**
	 * Recarregar combo
	 */
	recarregarCombo : function (comboNameComponent, content, valSelected) {
		
		comboNameComponent.empty();

		comboNameComponent.append(new Option('Selecione...', '', true, true));
		
	    $.each(content, function(index, row) {
		    	
	    	comboNameComponent.append(new Option(row.value.$, row.key.$, true, true));
		});

	    if (valSelected) {
	    	
	        $(comboNameComponent).val(valSelected);
	    } else {
	    	
	        $(comboNameComponent).val('');
	    }
	},
	
}, BaseController);
//@ sourceURL=geracaoNFe.js