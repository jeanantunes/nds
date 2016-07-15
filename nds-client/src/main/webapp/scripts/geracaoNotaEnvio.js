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
			intervaloBoxAte:null,
			exibirNotasEnvio:null
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
		},
		
		initFiltroDatas : function(){
			
		    $.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor',
					null, 
					function(result) {
				
						$("#geracaoNotaEnvio-filtro-movimentoDe", this.workspace).val(result);
						
						$("#geracaoNotaEnvio-filtro-movimentoAte", this.workspace).val(result);
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
			$(".input-numeric").numeric();
			$("#geracaoNotaEnvio-filtro-dataEmissao").val(formatDateToString(new Date()));
			
			$("#geracaoNotaEnvio-filtro-selectFornecedores").multiselect({
				selectedList : 6
			});
			
			$("#geracaoNotaEnvio-filtro-selectFornecedores").multiselect("checkAll");
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
			
			$("#geracaoNotaEnvio-btnEnviarEmail").click(function(e){
				_this.enviarEmail();
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
					sortname : "roteirizacao",
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
			
			mensagens = this.validarDataMovimento(); 
			if(mensagens[0]['value']) {
				
				this.gridReaload(grid, uri);
				
			} else {
				
				exibirMensagem('WARNING', mensagens);
				
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
			},{
				name : "exibirNotasEnvio",
				value : this.filtroPesquisa.exibirNotasEnvio	
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
			
			_this.gerarNotaEnvio();
			
			/*
			$.postJSON(this.path + 'hasCotasAusentes', null, function(data) {
				
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens, "");
				}
				
				_this.gerarNotaEnvio();
				
			});
			*/
		},
		
		/**
		 * Envia emails para as cotas que foram filtradas
		 */
		enviarEmail : function(){
			
			this.confirmDialog = new ConfirmDialog('Confirmar Envio de email?', function() {
		    	
				$.postJSON(geracaoNotaEnvioController.path + 'enviarEmail'), function(data){
					var tipoMensagem = data.tipoMensagem;
					var listaMensagens = data.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagemDialog(tipoMensagem, listaMensagens, "");
					}
				};
		            
				return true;
			    }, function() {
			    	 
			 });
			 this.confirmDialog.open();
			
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
	
		getArquivoNotaEnvio : function(data) {
			 this.confirmDialog = new ConfirmDialog('Confirmar Impressao NE/NECA.?', function() {
			    	
				 var path = geracaoNotaEnvioController.path + 'getArquivoNotaEnvio';

		            $.fileDownload(path, {
		                httpMethod : "POST",
		                failCallback : function(result) {
							
							result = $.parseJSON($(result).text());
							
							if(result != undefined && result.mensagens) {
								
								result = result.mensagens;
								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								
								if (tipoMensagem && listaMensagens) {
									exibirMensagemDialog(tipoMensagem, listaMensagens, "");
								}
							}else{
								exibirMensagem("ERROR", ["Erro ao Imprimir NE/NECA! Verifique também os parametros do distribuidor para impressão de documentos"]);
							}
		                    
		                },
		                successCallback : function() {
		                    _this.pesquisar();
		                }
		            });
			        return true;
			    }, function() {
			    	 
			    });
			 this.confirmDialog.open();
			
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

        	$.postJSON(geracaoNotaEnvioController.path + 'gerarNotaEnvio', params, 
        			geracaoNotaEnvioController.getArquivoNotaEnvio 
			);
           
		},
		
		/**
		 * Visualizar Nota Envio
		 */
		visualizarNE:function(e) {
			
			var results = $("#geracaoNotaEnvio-flexigrid-pesquisa tr");
			
			if (results.length == 0) {
				
				exibirMensagem("WARNING", ["Funcionalidade permitida apenas para pesquisas com resultado."]);
				
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
			
			this.filtroPesquisa.exibirNotasEnvio = $("#geracaoNotaEnvio-filtro-exibirNotasEnvio").val();
			
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
				$("#geracaoNotaEnvio-pesquisa", geracaoNotaEnvioController.workspace).hide();
			
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
		 * Metodo de pre-processamento dos dados inseridos na grid Cotas Ausentes
		 * 
		 * @returns intervalo de datas validos (true) ou invalidos (false)
		 */
		validarDataMovimento : function() {
			
			messages = [];
			
			if($('#geracaoNotaEnvio-filtro-movimentoDe').val() == ''
				|| $('#geracaoNotaEnvio-filtro-movimentoAte').val() == '') {
				
				messages.push({name: 'isValid', value: false});
				messages.push('Os campos de Data de Movimento não podem estar vazios');
				
				return messages;
			}
			
			messages.push({name: 'isValid', value: true});
			return messages;
			
		},

		/**
		 * Recarregar combo
		 */
		recarregarCombo : function (comboNameComponent, content, valSelected){
			
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

		/**
		 * Recarregar combos por Box
		 */
        changeBox : function(){
			
        	var boxDe = $("#geracaoNotaEnvio-filtro-boxDe").val();
        	
        	var boxAte = $("#geracaoNotaEnvio-filtro-boxAte").val();
        	
        	var idRota = $("#geracaoNotaEnvio-filtro-selectRota").val();
        	
        	var idRoteiro = $("#geracaoNotaEnvio-filtro-selectRoteiro").val();
        	
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
        		
        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-selectRota", geracaoNotaEnvioController.workspace), listaRota ,idRota);
     		    
        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-selectRoteiro", geracaoNotaEnvioController.workspace), listaRoteiro ,idRoteiro); 
        		    
        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-boxDe", geracaoNotaEnvioController.workspace), listaBox ,boxDe);
         		    
        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-boxAte", geracaoNotaEnvioController.workspace), listaBox ,boxAte);
        	    }    
			);
		},
		
		/**
		 * Recarregar combos por Rota
		 */
        changeRota : function(){
        	
            var boxDe = $("#geracaoNotaEnvio-filtro-boxDe").val();
        	
        	var boxAte = $("#geracaoNotaEnvio-filtro-boxAte").val();
        	
        	var idRota = $("#geracaoNotaEnvio-filtro-selectRota").val();
        	
        	var idRoteiro = $("#geracaoNotaEnvio-filtro-selectRoteiro").val();
        	
        	var params = [{
				            name : "idRota",
				            value : idRota	
						  }];
		    
        	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRota', params, 
				function(result) {
        		
        		    var listaRoteiro = result[0];
        		 
        		    var listaBox = result[1];
        		    
        		    var listaRota = result[2];

        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-boxDe", geracaoNotaEnvioController.workspace), listaBox ,boxDe);
     		    
        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-boxAte", geracaoNotaEnvioController.workspace), listaBox ,boxAte);
     		    
        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-selectRoteiro", geracaoNotaEnvioController.workspace), listaRoteiro ,idRoteiro); 
        		    
        		    geracaoNotaEnvioController.recarregarCombo($("#geracaoNotaEnvio-filtro-selectRota", geracaoNotaEnvioController.workspace), listaRota ,idRota);
        	    }    
			);
		},
		
		/**
		 * Recarregar combos por Roteiro
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

		setarParametrosNotaEnvio : function () {
			var params = [
			   {name:"filtro.intervaloBoxDe",        value:$("#idProduto", produtoController.workspace).val()},
			   {name:"filtro.intervaloBoxAte",       value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.intervaloCotaDe",       value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.intervaloCotaAte",      value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.intervaloMovimentoDe",  value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.intervaloMovimentoAte", value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.dataEmissao",           value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.idRoteiro",             value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.idRota",                value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.exibirNotasEnvio",      value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
			   {name:"filtro.listaIdFornecedores",   value:$("#tipoDescontoManual", produtoController.workspace).val()}];
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
		},  {
			display : 'Status',
			name : 'notaImpressa',
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

},BaseController);
//@ sourceURL=geracaoNotaEnvio.js