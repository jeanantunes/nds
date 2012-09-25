var geracaoNotaEnvioController = $.extend({
		
		path : contextPath + '/expedicao/geracaoNotaEnvio/',
	
		/**
		 * objeto utilizado para encapsular os dados do filtro de pesquisa
		 */
		filtroPesquisa : {
			dataEmissao:null,
			listaFornecedores:null,
			idRoteiro:null,
			idRota:null,
			intervaloMovimentoDe:null, 
			intervaloMovimentoAte:null,
			intervaloCotaDe:null, 
			intervaloCotaAte:null,
			intervaloBoxDe:null, 
			intervaloBoxAte:null
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
			$(".input-numeric").numeric();
			$("#geracaoNotaEnvio-filtro-dataEmissao").val(formatDateToString(new Date()));
			
			$("#geracaoNotaEnvio-filtro-selectFornecedores").multiselect({
				selectedList : 6
			});
		},
		
		/**
		 * inicializa os botões com suas funções de click
		 */
		initButtons : function() {
			
			var _this = this;
			
			$("#geracaoNotaEnvio-btnPesquisar").click(function(e){
				$("#geracaoNotaEnvio-pesquisa").show();
				$("#geracaoNotaEnvio-fileExport").show();
				_this.pesquisar();
			});
			
			$("#geracaoNotaEnvio-btnImprimirNE").click(function(e){
				$("#geracaoNotaEnvio-cotasAusentes-checkAll").uncheck();
				_this.gerarImpressaoNE();
			});
			
			$("#geracaoNotaEnvio-btnImprimirXLS").click(function(e){
				_this.imprimir("XLS");
			});
			
			$("#geracaoNotaEnvio-cotasAusentes-checkAll").click(function(e){
				var check = $("#geracaoNotaEnvio-cotasAusentes-checkAll").is(":checked");
				$(".checkboxCheckCotasAusentes").attr("checked", check);
			});
			
			$("#geracaoNotaEnvio-btnVisualizarNE").click(function(e){
				return _this.visualizarNE(e);
			});	
			
		},
		
		/**
		 * inicializa os dialogs
		 */
		initDialog : function() {
			
			var _this = this;
			
			$("#geracaoNotaEnvio-dialog-suplementar").dialog({
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
						$('#geracaoNotaEnvio-dialog-cotasAusentes').dialog("open");
					}
				}
			});
			
			$('#geracaoNotaEnvio-dialog-cotasAusentes').dialog({
				autoOpen : false,
				resizable : false,
				width : 630,
				modal : true,
				buttons : {
					"Confirma a Geração" : function() {
						_this.gerarNotaEnvio();
						$(this).dialog("close");
						
					},
					"Cancelar o Envio" : function () {
						$(this).dialog("close");
						$("#geracaoNotaEnvio-dialog-suplementar").dialog("open");
					},
					"Fechar" : function() {
						$(this).dialog("close");
					}
				}
			});
		},
		
		/**
		 * inicializa as grids da tela
		 */
		initFlexiGrids : function() {
			
			var _this = this;
			
			$("#geracaoNotaEnvio-flexigrid-pesquisa").flexigrid({
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
			
			$("#geracaoNotaEnvio-flexigrid-pesquisa").flexOptions({
					onSuccess:_this.preProcessGridPesquisa
			});
			
			$("#geracaoNotaEnvio-flexigrid-cotasAusentes").flexigrid({
				preProcess : _this.preProcessGridCotasAusentes,
				colModel : _this.colunasGridCotasAusentes,
				dataType : 'json',
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 570,
				height : 180
			});
			
			$("#geracaoNotaEnvio-flexigrid-cotasAusentes").flexOptions({
				onSuccess:_this.preProcessGridCotasAusentes
			});
		},
		
		/**
		 * Realiza pesquisa de acordo com os dado do filtro 
		 * e popula a grid de pesquisa:"geracaoNotaEnvio-flexigrid-pesquisa"
		 */
		pesquisar:function() {
			
			this.dataBind();
			
			var grid = $("#geracaoNotaEnvio-flexigrid-pesquisa");
			
			var uri = "pesquisar";
			
			this.gridReaload(grid, uri);
		
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
						'name' : "listaIdFornecedores[]",
						'value' : value
					});
				});
			}
			
			grid.flexOptions({
				"url" : this.path + uri,
				params : params,
				newp : 1
			});
			
			grid.flexReload();
		},
		
		/**
		 * gera impressão da Nota de Envio
		 */
		gerarImpressaoNE : function() {
			
			var _this = this;
			
			var gridCotasAusentes = $("#geracaoNotaEnvio-flexigrid-cotasAusentes");
			
			$.postJSON(this.path + 'hasCotasAusentes', null, function(data) {
				
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens, "");
				}
				if (data.cotasAusentes) {
					$('#geracaoNotaEnvio-dialog-cotasAusentes').dialog("open");
					_this.gridReaload(gridCotasAusentes, 'buscarCotasAusentes');
				} else {
					_this.gerarNotaEnvio();
				}
				
			});
		},
		
		/**
		 * Imprime o resultado da pesquisa no formato de arquivo parametrizado
		 * 
		 * @param fileType - tipo do arquivo para impressão
		 */
		imprimir : function(fileType) {
			
			var params = {"fileType":fileType};
			
			$.fileDownload(this.path + 'exportar', {
				httpMethod : "POST",
				data : params
			});
		},
		
		/**
		 * Envia as cotas selecionadas do dialog para terem 
		 * seus repartes transferidos para o estoque suplementar 
		 */
		transferirSuplementar : function() {
			
			var cotasSelecionadas = [];
			
			var cotasAusentes = $(".checkboxCheckCotasAusentes");
			
			for (var index in cotasAusentes) {
				if (cotasAusentes[index].checked) {
					cotasSelecionadas.push(cotasAusentes[index].value);
				}
			}
			
			var params = serializeArrayToPost("listaIdCotas", cotasSelecionadas);
			
			$.postJSON(this.path + 'transferirSuplementar', params, 
					function(data) {
						var tipoMensagem = data.tipoMensagem;
						var listaMensagens = data.listaMensagens;
				
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens, "");
						} 
			});
			
		},
		
		/**
		 * Gera Notas de Envio para resultados da pesquisa e para cotas ausentes selecionadas
		 */
		gerarNotaEnvio : function() {
			
			var cotasSelecionadas = [];
			
			var cotasAusentes = $(".checkboxCheckCotasAusentes");
			
			for (var index in cotasAusentes) {
				if (cotasAusentes[index].checked) {
					cotasSelecionadas.push(cotasAusentes[index].value);
				}
			}
			
			var params = serializeArrayToPost("listaIdCotas", cotasSelecionadas);
			
			$.postJSON(this.path + 'gerarNotaEnvio', params, function(data) {
				
				if (!data) return;
				
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;
		
				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens, "");
				} 
			});
		},
		
		/**
		 * Visualizar Nota Envio
		 */
		visualizarNE:function(e) {
			var results = $("#geracaoNotaEnvio-flexigrid-pesquisa tr");
			
			if (results.length != 1) {
				exibirMensagem("WARNING", ["Funcionalidade permitida apenas para pesquisas com um resultado."]);
				
				return false;
			}
		},
		
		/**
		 * Obtém dados da tela e popula o objeto filtroPesquisa
		 */
		dataBind:function() {
			
			this.filtroPesquisa.listaFornecedores= $("#geracaoNotaEnvio-filtro-selectFornecedores").val();;
			this.filtroPesquisa.dataEmissao = $("#geracaoNotaEnvio-filtro-dataEmissao").val();
			this.filtroPesquisa.idRota = $("#geracaoNotaEnvio-filtro-selectRota").val();
			this.filtroPesquisa.idRoteiro = $("#geracaoNotaEnvio-filtro-selectRoteiro").val();
			
			this.filtroPesquisa.intervaloBoxDe = $("#geracaoNotaEnvio-filtro-boxDe").val();
			this.filtroPesquisa.intervaloBoxAte = $("#geracaoNotaEnvio-filtro-boxAte").val();
			
			this.filtroPesquisa.intervaloCotaDe = $("#geracaoNotaEnvio-filtro-cotaDe").val();
			this.filtroPesquisa.intervaloCotaAte = $("#geracaoNotaEnvio-filtro-cotaAte").val();
			
			this.filtroPesquisa.intervaloMovimentoDe = $("#geracaoNotaEnvio-filtro-movimentoDe").val();
			this.filtroPesquisa.intervaloMovimentoAte = $("#geracaoNotaEnvio-filtro-movimentoAte").val();
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
			
			} else {
				
				for(var index in data.rows) {
					
					if(data.rows[index].cell["notaImpressa"]) {
						data.rows[index].cell["notaImpressa"] = '<a href="javascript:;" ><img src="' + contextPath + '/images/ico_check.gif" border="0" />';
			
					}else {
						data.rows[index].cell["notaImpressa"] = "";
					}
				}
				return data;
			}
		},
		
		/**
		 * Metodo de pre-processamento dos dados inseridos na grid Cotas Ausentes
		 * 
		 * @param data - dados inseridos na grid
		 * @returns dados normalizados para a grid
		 */
		preProcessGridCotasAusentes : function(data) {
			if (typeof data.mensagens == "object") {
				exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			} else {
				
				for(var index in data.rows) {
					
					var idCota = data.rows[index].cell["idCota"];
					
					data.rows[index].cell["checked"] = 
						'<input type="checkbox" class="checkboxCheckCotasAusentes" value="'+idCota+'" /> ';
				}
				
				return data;
			}
		},
		
		/**
		 * objeto utilizado para encapsular as colunas da grid de Cotas Ausentes
		 */
		colunasGridCotasAusentes:[{
			display : 'Cota',
			name : 'numeroCota',
			width : 50,
			sortable : true,
			align : 'left',
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 280,
			sortable : true,
			align : 'left',
		}, {
			display : 'Total Exemplares',
			name : 'exemplares',
			width : 130,
			sortable : true,
			align : 'center',
		}, {
			display : '',
			name : 'checked',
			width : 40,
			sortable : true,
			align : 'center',
		}],
		
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
		}, {
			display : 'Total Desconto R$',
			name : 'totalDesconto',
			width : 120,
			sortable : true,
			align : 'right',
		}, {
			display : 'Status',
			name : 'notaImpressa',
			width : 80,
			sortable : true,
			align : 'center',
		}],

},BaseController);