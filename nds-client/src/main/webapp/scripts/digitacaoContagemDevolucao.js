var digitacaoContagemDevolucaoController = $.extend(true, {
		
		init : function(userProfileOperador) {
			
			this.hashInserirEdicoesFechadas = {};
			/**
			 * Renderiza componente de Data(período) da tela
			 */
			$('#digitacaoContagemDevolucao-dataDe', digitacaoContagemDevolucaoController.workspace).datepicker({

				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('#digitacaoContagemDevolucao-dataDe', digitacaoContagemDevolucaoController.workspace).mask("99/99/9999");
			
			$('#digitacaoContagemDevolucao-dataAte', digitacaoContagemDevolucaoController.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('#digitacaoContagemDevolucao-dataAte', digitacaoContagemDevolucaoController.workspace).mask("99/99/9999");

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

			$(".areaBts", digitacaoContagemDevolucaoController.workspace).hide();
			
			$("#digitacaoContagemDevolucao-dataDe", digitacaoContagemDevolucaoController.workspace).focus();
			
			this.montaGridEdicoesFechadas();
			
			this.replicar = {
				all:false,
				list:[]
			};
			
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
					
					$(".areaBts", digitacaoContagemDevolucaoController.workspace).show();
					
					bloquearItensEdicao(digitacaoContagemDevolucaoController.workspace);
					
					$(".edicaoFechada").parents("tr").css("background", "#ffeeee");
					
					$('input[id^="valorExemplarNota"]', digitacaoContagemDevolucaoController.workspace).numeric();
					
					digitacaoContagemDevolucaoController.replicarValores();
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
				
				$(".areaBts", digitacaoContagemDevolucaoController.workspace).hide();

				return resultado.tableModel;
			}

			var page = resultado.tableModel.page;
			
			// Monta as colunas com os inputs do grid
			for(var index in resultado.tableModel.rows)	{
				
				var row = resultado.tableModel.rows[index];

				var id = page+index;
				
				var idInput = "valorExemplarNota" + id ;
				
				var classEdicaoFechada = row.cell.isEdicaoFechada ? "edicaoFechada" : "";
				
				var inputExemplarNota = '<input isEdicao="true" id="'+idInput+'" name="qtdNota" class="input-exemplar-nota '+classEdicaoFechada+
					' " type="text" style="width:80px; text-align: center;"  maxlength="17" value="'+row.cell.qtdNota+'"/>';
				
				var corDif = '';
				
				if (!digitacaoContagemDevolucaoController.isRoleOperador()) {
					
					inputExemplarNota = '<input isEdicao="true" id="'+idInput+'" name="qtdNota" maxlength="17" class="input-exemplar-nota '
						+classEdicaoFechada+' " type="text" style="width:80px; text-align: center;"  value="'+row.cell.qtdNota+
						'" onchange="digitacaoContagemDevolucaoController.limparCheck(\''+id+'\')"/>';

					var checked = '';
					
					var inputCheckReplicarValor = '<input isEdicao="true" type="checkbox" id="ch'+id+
						'" class="chBoxReplicar" name="checkgroup" ' + checked + ' data-id="'+id
						+'" onclick="digitacaoContagemDevolucaoController.replicarValor(\''+id+'\')"/>';
					
					row.cell.replicarQtde = inputCheckReplicarValor;
					
					row.cell.qtdDevolucao = '<span id="qtdDevolucao_'+id+'">'+ row.cell.qtdDevolucao +'</span>';
					
					//Altera cor do valor da quantidade, caso seja um valo negativo
					if (row.cell.diferenca < 0){
						corDif = 'color:red';
					}
				}
				
				row.cell.diferenca = "<span id='difernca"+id+"' style='"+ corDif +"'>"+row.cell.diferenca+"</span>";
				
				//Inputs Hidden da grid
				var inputDataRecolhimentoDistribuidor= '<input name="idDataRecolhimentoDist" type="hidden" value="' + row.cell.dataRecolhimentoDistribuidor + '" />';
				var inputValorExemplarNotaAux = '<input type="hidden" id="valorExemplarNotaAux'+ id +'" value="'+ row.cell.qtdNota +'" />';
				
				//Insere input text para o campo exemplar nota
				row.cell.qtdNota =  inputDataRecolhimentoDistribuidor + inputExemplarNota + inputValorExemplarNotaAux;
				
				if( !row.cell.precoVenda ){
					row.cell.precoVenda ='0,00';
				};
				
			}
			
			$("#totalGeral", digitacaoContagemDevolucaoController.workspace).html(resultado.valorTotal);
			
			$("#grids", digitacaoContagemDevolucaoController.workspace).show();
			
			$(".areaBts", digitacaoContagemDevolucaoController.workspace).show();
			
			return resultado.tableModel;
		},
		
		
		
		replicarValor:function(id) {
			
			if ($("#ch" + id, digitacaoContagemDevolucaoController.workspace).is(":checked")){
				$("#valorExemplarNota" + id, digitacaoContagemDevolucaoController.workspace).val(
					$("#qtdDevolucao_" + id, digitacaoContagemDevolucaoController.workspace).text()
				);
				
				$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text("0");
			} else {
				
				$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text("");
				
				$("#sel", digitacaoContagemDevolucaoController.workspace).attr("checked",false);
				
				var valorAux = 
					$("#valorExemplarNotaAux" + id, digitacaoContagemDevolucaoController.workspace).val();
				
				if (valorAux || valorAux == "0"){
					$("#valorExemplarNota" + id, digitacaoContagemDevolucaoController.workspace).val(valorAux);
					
					$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text(
						parseInt($("#qtdDevolucao_" + id, digitacaoContagemDevolucaoController.workspace).text())-
						valorAux
					);
					
				} else {
					$("#valorExemplarNota" + id, digitacaoContagemDevolucaoController.workspace).val("");
					$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text("");
				}
			}
			
			if(this.replicar.list.indexOf(id) >= 0) {
				this.replicar.list.splice(this.replicar.list.indexOf(id), 1);
			}
			
			if(this.replicar.all ^ $("#ch" + id, this.workspace).is(":checked")) {
				this.replicar.list.push(id);
			}
		},

		/**
			Limpa os valores do checked. 
		**/
		limparCheck:function (id){
			
			$('#ch'+id, digitacaoContagemDevolucaoController.workspace).attr("checked",false);
			$('#sel', digitacaoContagemDevolucaoController.workspace).attr("checked",false);
			
			if ($("#valorExemplarNota" + id, digitacaoContagemDevolucaoController.workspace).val() != ""){
				$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text(
					parseInt($("#valorExemplarNota" + id, digitacaoContagemDevolucaoController.workspace).val()) -
					parseInt($("#qtdDevolucao_" + id, digitacaoContagemDevolucaoController.workspace).text())
				);
			} else {
				$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text("");
			}
		},
		
		replicarValores : function() {
			
			$(".chBoxReplicar", digitacaoContagemDevolucaoController.workspace).each( function(index, input) { 
				
				var id = input.dataset.id;
				var row = $(input); 
				var value = $("#qtdDevolucao_" + id).text();
				var qtdNota = $("#valorExemplarNota" + id);
				
				
				if(row.is(":checked")){ 
					qtdNota.val(value);
					$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text("0");
				} else {
					
					var valorAux = 
						$("#valorExemplarNotaAux" + id, digitacaoContagemDevolucaoController.workspace).val();
					
					if (valorAux || valorAux == "0"){
						qtdNota.val(valorAux);
						$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text(valorAux - value);
					} else {
						qtdNota.val("");
						$("#difernca" + id, digitacaoContagemDevolucaoController.workspace).text("");
					}
				}
			});
			
		},
		
				
		/**
			Verifica se todos os itens do grid estão selecionados. 
			Se itens  selecionados, limpa todos os valores de exemplar de nota.
			Se itens não selecionados, replica todos os valores para os campos exemplares de nota. 
		**/
		checkAllReplicarValor: function (todos, checkgroupName) {
			
			$(".chBoxReplicar").attr("checked", todos.checked);
			
			this.replicar.all = todos.checked;
			this.replicar.list = [];
			
			digitacaoContagemDevolucaoController.replicarValores();
		},
		
		/**
			Salva as informações de contagem de devolução informadas no grid.
		**/
		salvar:function(){
			
			var param = serializeArrayToPost('listaDigitacaoContagemDevolucao', 
					digitacaoContagemDevolucaoController.obterListaDigitacaoContagemDevolucao());
			
			param["replicarTodos"] = $("#sel", digitacaoContagemDevolucaoController.workspace).is(":checked");
			
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
		
		
		devolucaoFinal :function(){
			var param = serializeArrayToPost('listaDigitacaoContagemDevolucao', digitacaoContagemDevolucaoController.obterListaDigitacaoContagemDevolucao());	
			$.postJSON(
				contextPath + "/devolucao/digitacao/contagem/devolucaoFinal", 
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
 		Monta o componente de confiramção da ação de Confirmar contagem de devolução
	**/
		popupConfirmarDevolucaoFinal: function () {
	
			$("#dialog-confirmar-devolucao-final", digitacaoContagemDevolucaoController.workspace).dialog({
				resizable : false,
				height : 140,
				width : 320,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						digitacaoContagemDevolucaoController.devolucaoFinal();
						
						$(this).dialog("close");
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				form: $("#dialog-confirmar-devolucao-final", this.workspace).parents("form")
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
				var colunaExemplaresDevolucao = linha.find("td")[4];
				var colunaExemplarNota = linha.find("td")[indexColunaExemplarNota];
				var colunaTotalComDesconto = linha.find("td")[5];
				var colunaDiferenca = linha.find("td")[8];
				var colunaDataRecolhimento = linha.find("td")[7];
				
				var codigoProduto = $(colunaCodigoProduto, digitacaoContagemDevolucaoController.workspace).find("div").html();
				
				var numeroEdicao = $(colunaNumeroEdicao, digitacaoContagemDevolucaoController.workspace).find("div").html();

				var precoCapa = $(colunaPrecoCapa, digitacaoContagemDevolucaoController.workspace).find("div").html();

				var totalComDesconto = $(colunaTotalComDesconto, digitacaoContagemDevolucaoController.workspace).find("div").html();

				var qtdDevolucao = $(colunaExemplaresDevolucao, digitacaoContagemDevolucaoController.workspace).find("div").find('span').html();

				var qtdNota = $(colunaExemplarNota, digitacaoContagemDevolucaoController.workspace).find("div").find('input[name="qtdNota"]').val();
				
				var diferenca = parseInt(qtdDevolucao - qtdNota);
				
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
				
				if (!isNaN(diferenca)) {
					
					digitacaoContagemDevolucao.diferenca = diferenca;
				}

				listaDigitacaoContagemDevolucao.push(digitacaoContagemDevolucao);
			});

			return listaDigitacaoContagemDevolucao;
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