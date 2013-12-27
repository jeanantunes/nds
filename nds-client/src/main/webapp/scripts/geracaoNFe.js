var geracaoNFeController = $.extend({
	
	/**
	 * path de geração de nfe
	 */
	path : contextPath + '/expedicao/geracaoNFe/',
	
	/**
	 * objeto utilizado para encapsular os dados do filtro de pesquisa
	 */
	filtroPesquisa : {
		tipoNotaFiscal:null,
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
		
		$("#geracaoNfe-filtro-selectFornecedores").multiselect({
			selectedList : 6
		});
		
		$("#geracaoNfe-filtro-selectFornecedores").multiselect("checkAll");
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
			}
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
		
		var params = this.getParams();
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
		params['fileType'] = fileType;
		
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
		
		$("#geracaoNfe-gridNFe").flexigrid({
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
			$("#geracaoNfe-pesquisa", geracaoNFeController.workspace).hide();
		
		} else {
			
			for(var index in data.rows) {
				
				if(data.rows[index].cell["notaImpressa"]) {
					data.rows[index].cell["notaImpressa"] = '<a href="javascript:;" ><img src="' + contextPath + '/images/ico_check.gif" border="0" />';
		
				}else {
					data.rows[index].cell["notaImpressa"] = "";
				}
			
				if(data.rows[index].cell["situacaoCadastro"] == 'SUSPENSO') {
					data.rows[index].cell["situacaoCadastro"] = '<a href="javascript:;" ><img src="' + contextPath + '/images/ico_suspenso.gif" border="0" />';
				} else if(data.rows[index].cell["situacaoCadastro"] == 'INATIVO') {
				
					data.rows[index].cell["situacaoCadastro"] = '<a href="javascript:;" ><img src="' + contextPath + '/images/ico_inativo.gif" border="0" />';
				
				} else {
					data.rows[index].cell["situacaoCadastro"] = "";
				}
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
		align : 'center',
	}, {
		display : 'Suspensa',
		name : 'situacaoCadastro',
		width : 100,
		sortable : true,
		align : 'center',
	}],
	
	/**
	 * Realiza pesquisa de acordo com os dado do filtro 
	 * e popula a grid de pesquisa:"geracaoNfe-pesquisa"
	 */
	pesquisar:function() {
		
		var params = [];
		
		params.push({name:"filtro.idTipoNotaFiscal" , value: $("#geracaoNfe-filtro-tipoNotaFiscal").val()});
		params.push({name:"filtro.dataInicial" , value: $("#geracaoNfe-filtro-movimentoDe").val()});
		params.push({name:"filtro.dataFinal" , value: $("#geracaoNfe-filtro-movimentoAte").val()});
		params.push({name:"filtro.intervaloBoxInicial" , value: $("#geracaoNfe-filtro-inputIntervaloBoxDe").val()});
		params.push({name:"filtro.intervaloBoxFinal" , value: $("#geracaoNfe-filtro-inputIntervaloBoxAte").val()});
		params.push({name:"filtro.intervalorCotaInicial" , value: $("#geracaoNfe-filtro-inputIntervaloCotaDe").val()});
		params.push({name:"filtro.intervalorCotaFinal" , value: $("#geracaoNfe-filtro-inputIntervaloCotaAte").val()});
		params.push({name:"filtro.dataEmissao" , value: $("#geracaoNfe-filtro-dataEmissao").val()});
		params.push({name:"filtro.idTipoNotaFiscal" , value: $("#geracaoNfe-filtro-tipoNotaFiscal").val()});
		params.push({name:"filtro.idRoteiro" , value: $("#geracaoNfe-filtro-listRoteiro").val()});
		params.push({name:"filtro.idRota" , value: $("#geracaoNfe-filtro-listRota").val()});
		
		if ($('#geracaoNfe-filtro-selectFornecedores').val()) {
			$.each($("#geracaoNfe-filtro-selectFornecedores").val(), function(index, v) {
				params.push({name : "filtro.listIdFornecedor[]", value : v});
			});
		}
		
		var grid = $("#geracaoNfe-gridNFe");
		
		var uri = "busca.json";
		
		grid.flexOptions({
			"url" : this.path + uri,
			params : params,
			newp : 1
		});
		
		grid.flexReload();
		
		$(".grids").show();
	
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
	
	parametros : function() {
		var params = {
			"intervaloBoxDe" : $("#geracaoNfe-filtro-inputIntervaloBoxDe", this.workspace).val(),
			"intervaloBoxAte" : $("#geracaoNfe-filtro-inputIntervaloBoxAte", this.workspace).val(),
			"intervaloCotaDe" : $("#geracaoNfe-filtro-inputIntervaloCotaDe", this.workspace).val(),
			"intervaloCotaAte" : $("#geracaoNfe-filtro-inputIntervaloCotaAte", this.workspace).val(),
			"intervaloDateMovimentoDe" : $("#geracaoNfe-filtro-movimentoDe", this.workspace).val(),
			"intervaloDateMovimentoAte" : $("#geracaoNfe-filtro-movimentoAte", this.workspace).val(),
			"tipoNotaFiscal" : $("#geracaoNfe-filtro-selectTipoNotaFiscal", this.workspace).val()
		};
		var listaFornecedores = $("#geracaoNfe-filtro-selectFornecedores", this.workspace).val();
		if (listaFornecedores) {
			params = serializeArrayToPost('listIdFornecedor', listaFornecedores, params);
		}
		return params;
	},
	
	
	/**
	 * Gerar Nfes para resultados da pesquisa e para cotas ausentes selecionadas
	 */
	btnGerarOnClick : function() {
		this.mapCotasSuspensas = new Object();
		
		$('#geracaoNfe-checkboxCheckAllCotasSuspensas', this.workspace).attr('checked', false);
		var _this = this;
		var params = this.getParams();
		$.postJSON(this.path + 'hasCotasSuspensas.json', params, function(data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			if (data.cotasSuspensas) {
				_this.$dialogCotasSuspensas.dialog("open");
				_this.gridReaload(_this.$gridCotasSuspensas,
						'buscaCotasSuspensas.json');
			} else {
				_this.gerar();
			}

		});
	},
	
	/**
	 * funcao responsavel por carregar o combos por Roteiro
	 */
	changeRoteiro : function(){
    	
        var boxDe = $("#geracaoNotaEnvio-filtro-boxDe").val();
    	
    	var boxAte = $("#geracaoNotaEnvio-filtro-boxAte").val();
    	
    	var idRota = $("#geracaoNotaEnvio-filtro-selectRota").val();
    	
    	var idRoteiro = $("#geracaoNotaEnvio-filtro-selectRoteiro").val();
     	
     	var params = [{
				            name : "idRoteiro",
				            value : idRoteiro	
						  }];
     	
     	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRoteiro', params, 
			function(result) {
    		
    		    var listaRota = result[0];
    		 
    		    var listaBox = result[1];
    		    
    		    var listaRoteiro = result[2];
 		    
    		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-selectRota", geracaoNotaEnvioController.workspace), listaRota ,idRota);  
    		    
    		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-boxDe", geracaoNotaEnvioController.workspace), listaBox ,boxDe);
     		    
    		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-boxAte", geracaoNotaEnvioController.workspace), listaBox ,boxAte);
    		    
    		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-selectRoteiro", geracaoNotaEnvioController.workspace), listaRoteiro ,idRoteiro); 
    	    }    
		);
	},
	
	
}, BaseController);
//@ sourceURL=geracaoNFe.js