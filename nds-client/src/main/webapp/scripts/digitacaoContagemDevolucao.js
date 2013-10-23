var digitacaoContagemDevolucaoController = $.extend(true, {

		init : function(userProfileOperador) {
			
			this.hashInserirEdicoesFechadas = {};
			/**
			 * Renderiza componente de Data(período) da tela
			 */
			$('input[id^="data"]', digitacaoContagemDevolucaoController.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('input[id^="data"]', digitacaoContagemDevolucaoController.workspace).mask("99/99/9999");

			var colunas = digitacaoContagemDevolucaoController.montarColunas();
			
			$("#contagemDevolucaoGrid", digitacaoContagemDevolucaoController.workspace).flexigrid({				
				dataType : 'json',
				preProcess:digitacaoContagemDevolucaoController.executarPreProcessamento,
				onSuccess:function(){
					bloquearItensEdicao(digitacaoContagemDevolucaoController.workspace);
					$('input[id^="valorExemplarNota"]', digitacaoContagemDevolucaoController.workspace).numeric();
				},
				colModel : colunas,
				sortname : "codigoProduto",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
			
			if (digitacaoContagemDevolucaoController.isRoleOperador(userProfileOperador)){
				
				//Oculta os campos que não serão visíveis pelo perfil de usuário Operador
				//$("#btnConfirmar", digitacaoContagemDevolucaoController.workspace).hide();
				//$("#bt_sellAll", digitacaoContagemDevolucaoController.workspace).hide();
			}
			
			$("#dataDe", digitacaoContagemDevolucaoController.workspace).focus();
			
			this.montaGridEdicoesFechadas();
			
		},
		
		/**
			Verifica se o role do usuário logado é Operador 
		**/
		isRoleOperador: function(userProfileOperador){
			
			var userProfile = userProfileOperador;
			
			return userProfile;	
		},
		
		/**
			Monta as colunas do grid em função do perfil de usuário logado no sistema 
		**/
		montarColunas: function (){
			
			if(digitacaoContagemDevolucaoController.isRoleOperador()){
				return digitacaoContagemDevolucaoController.montarColunasPerfilOperador();
			}
			
			return digitacaoContagemDevolucaoController.montarColunasPerfilAdmin();
		},
		
		/**
			Executa a pesquisa de contagens de devolução. 
		**/
		pesquisar: function (){
			
			var formData = $('#pesquisaContagemDevolucaoForm', digitacaoContagemDevolucaoController.workspace).serializeArray();
			
			$("#contagemDevolucaoGrid", digitacaoContagemDevolucaoController.workspace).flexOptions({
				url: contextPath + "/devolucao/digitacao/contagem/pesquisar",
				params: formData,
				onSuccess: function(){
					bloquearItensEdicao(digitacaoContagemDevolucaoController.workspace);
					$(".edicaoFechada").parents("tr").css("background", "#ffeeee");
					$('input[id^="valorExemplarNota"]', digitacaoContagemDevolucaoController.workspace).numeric();
				}
			});
			
			$("#contagemDevolucaoGrid", digitacaoContagemDevolucaoController.workspace).flexReload();
			
		},
		
		/**
			Executa o pré processamento das informações do grid. 
		**/
		executarPreProcessamento: function (resultado){
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$("#grids", digitacaoContagemDevolucaoController.workspace).hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.tableModel.rows, function(index, row) {
				
				var idInput = "valorExemplarNota" + index ;
				
				var classEdicaoFechada = row.cell.isEdicaoFechada ? "edicaoFechada" : "";
				
				var inputExemplarNota = '<input isEdicao="true" id="'+idInput+'" name="qtdNota" class="input-exemplar-nota '+classEdicaoFechada+' " type="text" style="width:80px; text-align: center;"  maxlength="17" value="'+row.cell.qtdNota+'"/>';
										
				if(!digitacaoContagemDevolucaoController.isRoleOperador()){
					
					inputExemplarNota = '<input isEdicao="true" id="'+idInput+'" name="qtdNota" maxlength="17" class="input-exemplar-nota '+classEdicaoFechada+' " type="text" style="width:80px; text-align: center;"  value="'+row.cell.qtdNota+'" onkeypress="digitacaoContagemDevolucaoController.limparCheck(\'ch'+index+'\')"/>';
					
					var inputCheckReplicarValor = '<input isEdicao="true" type="checkbox" id="ch'+index+'" class="chBoxReplicar" name="checkgroup"  "/>';
					
					//Altera cor do valor da quantidade, caso seja um valo negativo
					if(row.cell.diferenca < 0){
						row.cell.diferenca = "<span style='color:red'>"+row.cell.diferenca+"</span>";
					}
					
					row.cell.replicarQtde = inputCheckReplicarValor;
				}
				
				//Inputs Hidden da grid
				var inputDataRecolhimentoDistribuidor= '<input name="idDataRecolhimentoDist" type="hidden" value="' + row.cell.dataRecolhimentoDistribuidor + '" />';
				
				//Insere input text para o campo exemplar nota
				row.cell.qtdNota =  inputDataRecolhimentoDistribuidor + inputExemplarNota;
				
				if( !row.cell.precoVenda ){
					row.cell.precoVenda ='0,00';
				};
				
			});
			
			$("#totalGeral", digitacaoContagemDevolucaoController.workspace).html(resultado.valorTotal);
			
			$("#grids", digitacaoContagemDevolucaoController.workspace).show();
			
			digitacaoContagemDevolucaoController.limparCheck('sel');
				
			return resultado.tableModel;
		},

		/**
			Limpa os valores do checked. 
		**/
		limparCheck:function (id){
			
			$('#'+id, digitacaoContagemDevolucaoController.workspace).attr("checked",false);	
		},
		
		replicarValores : function() {
			
			$("#contagemDevolucaoGrid tr", digitacaoContagemDevolucaoController.workspace).each( function(index, input) { 
				
				var row = $(input); 
				var value = $(row.find("td")[4]).find("div").html();
				var qtdNota = row.find("input[name='qtdNota']");
				
				
				if(row.find(".chBoxReplicar").is(":checked")){ 
					qtdNota.val(value);
					//qtdNota.prop('disabled', true); 
				} else {
					qtdNota.val("");
					//qtdNota.prop('disabled', false); 
				}
				
			});
		},
		
				
		/**
			Verifica se todos os itens do grid estão selecionados. 
			Se itens  selecionados, limpa todos os valores de exemplar de nota.
			Se itens não selecionados, replica todos os valores para os campos exemplares de nota. 
		**/
		checkAllReplicarValor: function (todos, checkgroupName) {
			
			if(todos.checked == false) {
				digitacaoContagemDevolucaoController.limparValorAll();
			}		
			else {										
				$(".chBoxReplicar").attr("checked", true);
			}	
		},
		
		/**
			Salva as informações de contagem de devolução informadas no grid.
		**/
		salvar:function(){
			
			var param = serializeArrayToPost('listaDigitacaoContagemDevolucao', digitacaoContagemDevolucaoController.obterListaDigitacaoContagemDevolucao());		
			
			$.postJSON(
				contextPath + "/devolucao/digitacao/contagem/salvar", 
				param,
				function(result) {
					digitacaoContagemDevolucaoController.pesquisar();
				},
				digitacaoContagemDevolucaoController.tratarErro, false
			);
		},
		
		/**
 			Trata erro nas ações de Salvar e Confirmar contagem de devolução. 
		**/
		tratarErro: function (resultado){
			
			if (resultado.mensagens) {
				
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
			}
		},
		
		/**
	 		Efetua a chamada ao método Confirmação de contagem de devoluação do controller. 
		**/
		confirmarOperacao: function (){
			
			var param = serializeArrayToPost('listaDigitacaoContagemDevolucao', digitacaoContagemDevolucaoController.obterListaDigitacaoContagemDevolucao());	
			
			$.postJSON(
				contextPath + "/devolucao/digitacao/contagem/confirmar", 
				param,
				function(result) {
					digitacaoContagemDevolucaoController.pesquisar();
				},
				digitacaoContagemDevolucaoController.tratarErro, false
			);
		},
		
		
		geraNota :function(){
			var param = serializeArrayToPost('listaDigitacaoContagemDevolucao', digitacaoContagemDevolucaoController.obterListaDigitacaoContagemDevolucao());	
			$.postJSON(
				contextPath + "/devolucao/digitacao/contagem/geraNota", 
				param,
				function(result) {
					digitacaoContagemDevolucaoController.pesquisar();
				},
				digitacaoContagemDevolucaoController.tratarErro, false
			);
		},
		
		gerarChamadaEncalheFornecedor : function() {
			 $.fileDownload(contextPath + "/devolucao/digitacao/contagem/gerarChamadaEncalheFornecedor", {
	                httpMethod : "POST",
	                data : [],
	                failCallback : function(arg) {
	                    exibirMensagem("WARNING", ["Não há CE Devolução no periodo informado!"]);
	                }
	         });
		},
		
		/**
	 		Monta o componente de confiramção da ação de Confirmar contagem de devolução
		**/
		popupConfirmar: function () {

			$("#dialog-confirmar", digitacaoContagemDevolucaoController.workspace).dialog({
				resizable : false,
				height : 140,
				width : 320,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						digitacaoContagemDevolucaoController.confirmarOperacao();
						
						$(this).dialog("close");
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				form: $("#dialog-confirmar", this.workspace).parents("form")
			});
		},
		
		/**
		 	Monta a lista de parâmetros necessarios para as ações de Salvar e Confirmar contagem de devolução
		**/
		obterListaDigitacaoContagemDevolucao: function () {

			var linhasDaGrid = $("#contagemDevolucaoGrid tr", digitacaoContagemDevolucaoController.workspace);

			var listaDigitacaoContagemDevolucao = new Array();
			
			//Verifica o role do usuario para obter o indice da coluna com os valores do exemplar nota
			var indexColunaExemplarNota = (digitacaoContagemDevolucaoController.isRoleOperador())?5:7;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
			
				var colunaCodigoProduto = linha.find("td")[0];
				var colunaNumeroEdicao = linha.find("td")[2];
				var colunaPrecoCapa = linha.find("td")[3];
				var colunaExemplarNota = linha.find("td")[indexColunaExemplarNota];
				var colunaTotalComDesconto = linha.find("td")[5];
				var colunaDiferenca = linha.find("td")[8];
				var colunaDataRecolhimento = linha.find("td")[7];
				
				var codigoProduto = $(colunaCodigoProduto, digitacaoContagemDevolucaoController.workspace).find("div").html();
				
				var numeroEdicao = $(colunaNumeroEdicao, digitacaoContagemDevolucaoController.workspace).find("div").html();

				var precoCapa = $(colunaPrecoCapa, digitacaoContagemDevolucaoController.workspace).find("div").html();

				var totalComDesconto = $(colunaTotalComDesconto, digitacaoContagemDevolucaoController.workspace).find("div").html();

				var diferenca = $(colunaDiferenca, digitacaoContagemDevolucaoController.workspace).find("span").html();
				
				var qtdNota = $(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="qtdNota"]').val();
				
				var dataRecolhimentoDistribuidor = $(colunaDataRecolhimento, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="idDataRecolhimentoDist"]').val();
				
				if (!$.trim(qtdNota)) {

					return true;
				}

				var digitacaoContagemDevolucao = {codigoProduto:codigoProduto,
												  numeroEdicao:numeroEdicao,
												  precoVenda:precoCapa,
												  dataRecolhimentoDistribuidor:dataRecolhimentoDistribuidor,
												  qtdNota:qtdNota,
												  valorTotalComDesconto:totalComDesconto};
				
					if (diferenca) {
						
						digitacaoContagemDevolucao.diferenca =diferenca;
					}

				listaDigitacaoContagemDevolucao.push(digitacaoContagemDevolucao);
			});

			return listaDigitacaoContagemDevolucao;
		},
		
		/**
			Limpa todos os dados do grid que estiverem selecionados
		**/
		limparValorAll: function (){
			
			$(".chBoxReplicar").attr("checked", false);
			digitacaoContagemDevolucaoController.replicarValores();	
		
		},
		
		/**
		 * Monta as colunas do grid para usuário com perfil de Administrador ou perfil que não seja Operador.
		 */
		montarColunasPerfilAdmin : function(){
			
			var colModel = [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Exemplar Devolução',
				name : 'qtdDevolucao',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total c/ Desc.R$',
				name : 'valorTotalComDesconto',
				width : 100,
				sortable : false,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorTotal',
				width : 80,
				sortable : false,
				align : 'right'
			}, {
				display : 'Exemplar Nota',
				name : 'qtdNota',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 60,
				sortable : false,
				align : 'center'
			},{
				display : 'Replicar Qtde',
				name : 'replicarQtde',
				width : 70,
				sortable : false,
				align : 'center'
			}];
			
			return colModel;
		},

		/**
		 * Monta as colunas do grid para usuário com perfil de Operador
		 */
		montarColunasPerfilOperador : function() {
			
			var colModel = [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 400,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Exemplar Nota',
				name : 'qtdNota',
				width : 137,
				sortable : false,
				align : 'center'
			}];
			
			return colModel;
		},
		
		
		incluirProdutoDialog :function(){					
			this.hashInserirEdicoesFechadas = {};
			$("#dialogEdicoesFechadasSelAll", this.workspace).attr("checked",false);
			var _this =  this;
			$( "#dialogEdicoesFechadas", this.workspace ).dialog({
				resizable: false,
				height:500,
				width:945,
				modal: true,
				buttons: {
					"Confirmar": function() {
						if ($(dialogEdicoesFechadasSelAll, _this.workspace).is(":checked")) {
							
							_this.adicionarEdicoesFechadas(true, null);
							
						}else{
							//{codigoProduto:codigoProduto,edicaoProduto:edicaoProduto,parcial:parcial,idProdutoEdicao:idProdutoEdicao}
							var listInserirEdicoesFechadas = new Array();
							for ( var id in _this.hashInserirEdicoesFechadas) {
								listInserirEdicoesFechadas
										.push(_this.hashInserirEdicoesFechadas[id]);
							}							
							if(listInserirEdicoesFechadas.length > 0){
								_this.adicionarEdicoesFechadas(false, listInserirEdicoesFechadas);
							}
							
						}
						$( this ).dialog( "close" );
					},
					
					"Cancelar": function() {
						delete _this.hashInserirEdicoesFechadas;
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialogEdicoesFechadas", this.workspace).parents("form")
			});
			
			$(".consultaEdicoesFechadasGrid", this.workspace).flexOptions({
				"url" : contextPath + '/devolucao/digitacao/contagem/pesquisaEdicoesFechadas',
				
			}).flexReload();
		},
		
		adicionarEdicoesFechadas : function(checkAll, listInserirEdicoesFechadas) {
			
			var param = {"checkAll" : checkAll};
			
			if (listInserirEdicoesFechadas) {
				params = serializeArrayToPost("listaEdicoesFechadas",listInserirEdicoesFechadas,params);
			}
			
			$.postJSON(
				contextPath + "/devolucao/digitacao/contagem/adicionarEdicoesFechadas", 
				param,
				function(result) {
					digitacaoContagemDevolucaoController.pesquisar();
				},
				digitacaoContagemDevolucaoController.tratarErro, false
			);
			
		},
		
		edicoesFechadasCheckAll :function(checkbox){
			
			$('.consultaEdicoesFechadasGrid tr td', this.workspace).each( function(){ 
				$('input[type="checkbox"]', this).attr("checked", $(checkbox, this.workspace).is(":checked"));
			});
			
		},
		
		clickEdicoesFechada : function(idProdutoEdicao,codigoProduto,edicaoProduto, parcial, checkbox){
			if($(checkbox, this.workspace).is(":checked")){
				this.hashInserirEdicoesFechadas[idProdutoEdicao] = {codigoProduto:codigoProduto,edicaoProduto:edicaoProduto,parcial:parcial,idProdutoEdicao:idProdutoEdicao};
			}else{
				delete this.hashInserirEdicoesFechadas[idProdutoEdicao];
			}
			
			$("#dialogEdicoesFechadasSelAll", this.workspace).attr("checked",false);
		},
		
		montaGridEdicoesFechadas :function(){
			$(".consultaEdicoesFechadasGrid", this.workspace).flexigrid({
				onSuccess: function() {bloquearItensEdicao(digitacaoContagemDevolucaoController.workspace);},
				preProcess: function(data) {
					if( typeof data.mensagens == "object") {

						exibirMensagemDialog(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);

					} else {
						$.each(data.rows, function(index, value) {
							
							var onClick = 'digitacaoContagemDevolucaoController.clickEdicoesFechada('
								+value.cell.idProdutoEdicao+','+value.cell.codigoProduto+', ' 
								+value.cell.edicaoProduto+','+value.cell.parcial+',this )';
							
							var sel = '<input type="checkbox" name="checkbox" id="checkbox" onclick="'+onClick+'" />';
							value.cell.parcial = (value.cell.parcial)?"Sim":"Não";				
							
							value.cell.sel = sel;
						});

						return data;
					}
				},
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 140,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'edicaoProduto',
					width : 60,
					sortable : true,
					align : 'center'
				},{
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 180,
					sortable : true,
					align : 'left'
				},  {
					display : 'Lançamento',
					name : 'dataLancamento',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Recolhimento',
					name : 'dataRecolhimento',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Parcial',
					name : 'parcial',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Saldo',
					name : 'saldo',
					width : 50,
					sortable : true,
					align : 'right'
				}, {
					display : '',
					name : 'sel',
					width : 30,
					sortable : true,
					align : 'center'
				}],
				sortname : "nomeFornecedor",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 900,
				height : 280
			});

		}
		
}, BaseController);
//@ sourceURL=digitacaoContagemDevolucao.js