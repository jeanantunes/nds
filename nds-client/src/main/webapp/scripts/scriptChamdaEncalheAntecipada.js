var chamdaEncalheAnteipadaController = $.extend(true, {
		
		totalCota:0,
		totalExemplares:0,
		tipoPesquisaSelecionado:"",
		tipoPesquisaGridCota:"PES_COTA",
		groupNameCheckGridCota:"groupGridCota",
		groupNameCheck:"checkgroup",
		nameGridPesquisaCota:"ceAntecipadaCotaGrid",
		nameGrid:"ceAntecipadaGrid",
		pesquisaCota:null,
		flagRecolhimento:false,
		dataRecolhimentoPrevista:null,
		
		
		gridSelectionHelper: {
			isCheckedAll: false,
			totalChecked: 0,
			totalExemplares: 0,
			checkedItems: [],
			uncheckedItems: [],
			
			changeCheckboxState: function(checkbox, index) {
				
				if (checkbox.checked) {
					this.checkItem(index);
				} else {
					this.uncheckItem(index);
				}
				
				chamdaEncalheAnteipadaController.atribuirValorQntCotas();
				chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
			},
			
			checkItem: function(index) {

				var item = chamdaEncalheAnteipadaController.parseGridLine(index);
				
				this.checkedItems.push(item);

				this.totalChecked++;
				this.totalExemplares += eval(item.exemplares);
			},
			
			uncheckItem: function(index) {
				
				var item = chamdaEncalheAnteipadaController.parseGridLine(index);
				chamdaEncalheAnteipadaController.gridSelectionHelper.checkedItems.splice(item, 1);
				this.uncheckedItems.push(item);
				this.totalChecked--;
				this.totalExemplares -= eval(item.exemplares);
			},
			
			keepChecked: function(item) {
				
				if(this.isCheckedAll) {
					
					var unchecked = !this.uncheckedItems.filter(function(el) {
						
						return el.numeroCota 	=== item.numeroCota
						&& el.codigoBox 	=== item.codigoBox
						&& el.idLancamento  === item.idLancamento
						&& el.exemplares === item.qntExemplares;
						
					}).length;
					
					return (this.isCheckedAll && unchecked);
				} else {
					
					var checked = this.checkedItems.filter(function(el) {
						
						return el.numeroCota 	=== item.numeroCota
							&& el.codigoBox 	=== item.codigoBox
							&& el.idLancamento  === item.idLancamento
							&& el.exemplares === item.qntExemplares;

						
					}).length;
					
					return (!this.isCheckedAll && checked);
				}
			},
			
			removeItemFromUncheckedArray: function(item) {

				this.uncheckedItems = this.uncheckedItems.filter(function(el) {
					
					return el.numeroCota 	!== item.numeroCota 
						&& el.codigoBox 	!== item.codigoBox
						&& el.idLancamento  !== item.idLancamento
						&& el.exemplares  !== item.qntExemplares
					    && el.nomeCota  !== item.nomeCota
					    && el.codigoChamadaAntecipada  !== item.codigoChamadaAntecipada
					    && el.id  !== item.id;
					
				});
			},
			
			getTotalCotas: function() {
				
				return this.totalChecked ? this.totalChecked : chamdaEncalheAnteipadaController.totalCota;
			},
			
			getTotalExemplares: function() {
				
				return this.totalExemplares ? this.totalExemplares : chamdaEncalheAnteipadaController.totalExemplares;
			},
			
			resetData: function() {
				
				this.totalChecked= 0;
				this.totalExemplares= 0;
				this.isCheckedAll= false;
				chamdaEncalheAnteipadaController.isCheckedAll=false;
				chamdaEncalheAnteipadaController.gridSelectionHelper.isCheckedAll = false;
				this.uncheckedItems= [];
				$('#sel',chamdaEncalheAnteipadaController.workspace).attr("checked", false);
				$('#checkRecolhimentoFinal',chamdaEncalheAnteipadaController.workspace).attr("checked", false);
				$('#checkRecolhimentoFinal').attr('disabled', 'disabled');
				this.checkedItems = [];
				checkedItems = new Array();
				uncheckedItems= new Array();
				$("#idTotalCotas",chamdaEncalheAnteipadaController.workspace).val(0);
				$("#idTotalExemplares",chamdaEncalheAnteipadaController.workspace).val(0);
				chamdaEncalheAnteipadaController.totalExemplares = 0;
				chamdaEncalheAnteipadaController.totalCota = 0;
				
			},

		},
			 
		params:function(){
			
			var formData = [ 
				             {name:"codigoProduto",value:$("#codigoProduto",chamdaEncalheAnteipadaController.workspace).val()},
				             {name:"numeroEdicao",value:$("#edicao",chamdaEncalheAnteipadaController.workspace).val()},
				             {name:"box",value:$("#box",chamdaEncalheAnteipadaController.workspace).val()},
				             {name:"fornecedor",value:$("#fornecedor",chamdaEncalheAnteipadaController.workspace).val()},
				             {name:"rota",value:$("#rota",chamdaEncalheAnteipadaController.workspace).val()},
				             {name:"roteiro",value:$("#roteiro",chamdaEncalheAnteipadaController.workspace).val()},
				             {name:"programacaoRealizada",value:chamdaEncalheAnteipadaController.getProgramacaoRealizada},
				             {name:"recolhimentoFinal",value:chamdaEncalheAnteipadaController.getRecolhimentoFinal},
				             {name:"municipio",value:$("#municipio",chamdaEncalheAnteipadaController.workspace).val()},
				             {name:"tipoPontoPDV",value:$("#tipoPontoPDV",chamdaEncalheAnteipadaController.workspace).val()}
				            ];
			return formData;
		},
		
		parseGridLine: function(lineIndex) {
			
			var gridLine = $("#gridAntecipada tr[id^='row']")[lineIndex];
			
			if (!gridLine) {
				
				return;
			}
			
			var numeroCota = $(gridLine).find("input[name='numCota']").val();
			var codigoBox = $(gridLine).find("input[name='codBox']").val();
			var idLancamento = $(gridLine).find("input[name='idLancamento']").val();
			var exemplares = $(gridLine).find("input[name^='qntExemplares']").val();
			var nomeCota = $(gridLine).find("input[name^='nomeCota']").val();
			var codigoChamadaAntecipada = $(gridLine).find("input[name^='codigoChamadaAntecipada']").val();
			var id = $(gridLine).find("input[name^='id']").val();
			
			return {
				numeroCota: numeroCota,
				codigoBox: codigoBox,
				idLancamento: idLancamento, 
				exemplares: exemplares
			};
		},

		getRecolhimentoFinal:function(){
			return ($("#checkRecolhimentoFinal",chamdaEncalheAnteipadaController.workspace).attr("checked") == "checked");
		},
		
		getProgramacaoRealizada:function(){
			return ($("#checkCE",chamdaEncalheAnteipadaController.workspace).attr("checked") == "checked");
		},
		
		getHiddenProduto: function (){
			return $("#codProdutoHidden",chamdaEncalheAnteipadaController.workspace).val();
		},
		
		getHiddenNumeroEdicao: function (){
		   	return $("#numeroEdicaoHidden",chamdaEncalheAnteipadaController.workspace).val();
		},
		
		getHiddenFornecedor: function (){
			return $("#codFornecedorHidden",chamdaEncalheAnteipadaController.workspace).val();
		},
		
		setHiddenProduto: function (){
			 $("#codProdutoHidden",chamdaEncalheAnteipadaController.workspace).val($("#codigoProduto",chamdaEncalheAnteipadaController.workspace).val());
		},
		
		setHiddenNumeroEdicao: function (){
		   	$("#numeroEdicaoHidden",chamdaEncalheAnteipadaController.workspace).val($("#edicao",chamdaEncalheAnteipadaController.workspace).val());
		},
		
		setHiddenFornecedor: function (){
			$("#codFornecedorHidden",chamdaEncalheAnteipadaController.workspace).val($("#fornecedor",chamdaEncalheAnteipadaController.workspace).val());
		},
		
		setHiddenMunicipio: function (){
		   	$("#codMunicipioHidden",chamdaEncalheAnteipadaController.workspace).val($("#municipio",chamdaEncalheAnteipadaController.workspace).val());
		},
		
		setHiddenTipoPontoPDV: function (){
			$("#codTipoPontoPdvHidden",chamdaEncalheAnteipadaController.workspace).val($("#tipoPontoPDV",chamdaEncalheAnteipadaController.workspace).val());
		},
		
		getHiddenMunicipio: function (){
		   	return $("#codMunicipioHidden",chamdaEncalheAnteipadaController.workspace).val();
		},
		
		getHiddenTipoPontoPDV: function (){
			return $("#codTipoPontoPdvHidden",chamdaEncalheAnteipadaController.workspace).val();
		},
		
		getCodigoProdutoPesquisa: function (){
			return  {"codigoProduto": $("#codigoProduto",chamdaEncalheAnteipadaController.workspace).val()};
		},
			
		pesquisar: function(){
			
			if($("#box",chamdaEncalheAnteipadaController.workspace).val() == -1){
				
				chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
			} else{
				chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
			}
				
		},
		
		montarGridPesquisaCotas: function (){
			
			$("#ceAntecipadaCotaGrid",chamdaEncalheAnteipadaController.workspace).flexOptions({
				url: contextPath + "/devolucao/chamadaEncalheAntecipada/montarPesquisaCotas",
				params: chamdaEncalheAnteipadaController.params(),
				newp: 1,
				onSuccess:function(){
					chamdaEncalheAnteipadaController.formatarCampos();
					chamdaEncalheAnteipadaController.sumarizarCotasSelecionadas(chamdaEncalheAnteipadaController.nameGrid);
					chamdaEncalheAnteipadaController.processarRenderizacaoDeBotoesCE();		
					bloquearItensEdicao(chamdaEncalheAnteipadaController.workspace);
				}
			});
			
			$("#ceAntecipadaCotaGrid",chamdaEncalheAnteipadaController.workspace).flexReload();
		},
		
		pesquisarCotasPorProdutoComCe:function(){
			
			$("#ceAntecipadaGrid",chamdaEncalheAnteipadaController.workspace).flexOptions({
				url: contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisar",
				params: chamdaEncalheAnteipadaController.params(),
				newp: 1,
				onSuccess:function(){
					chamdaEncalheAnteipadaController.sumarizarCotasSelecionadas(chamdaEncalheAnteipadaController.nameGrid);
					chamdaEncalheAnteipadaController.processarRenderizacaoDeBotoesCE();
					bloquearItensEdicao(chamdaEncalheAnteipadaController.workspace);
				}
			});
			
			$("#ceAntecipadaGrid", chamdaEncalheAnteipadaController.workspace).flexReload();
		},
		
		pesquisarCotasPorProduto:function(){
			
			chamdaEncalheAnteipadaController.gridSelectionHelper.resetData();
			
			$("#ceAntecipadaGrid",chamdaEncalheAnteipadaController.workspace).flexOptions({
				url: contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisar",
				params: chamdaEncalheAnteipadaController.params(),
				newp: 1,
				onSuccess:function(){
					chamdaEncalheAnteipadaController.sumarizarCotasSelecionadas(chamdaEncalheAnteipadaController.nameGrid);
					chamdaEncalheAnteipadaController.processarRenderizacaoDeBotoesCE();
					bloquearItensEdicao(chamdaEncalheAnteipadaController.workspace);
				}
			});
			
			$("#ceAntecipadaGrid", chamdaEncalheAnteipadaController.workspace).flexReload();
		},
		
		gravar: function (){
				
			var params = {'codigoProduto':chamdaEncalheAnteipadaController.getHiddenProduto(),
				'numeroEdicao':chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
				'dataRecolhimento':$("#dataAntecipacao",chamdaEncalheAnteipadaController.workspace).val(),
				'dataProgramada':$("#dataProgramada").val(),
				'gravarTodos':$("#sel", chamdaEncalheAnteipadaController.workspace).is(':checked'),
				'programacaoRealizada':$("#sel", chamdaEncalheAnteipadaController.workspace).is(':checked'),
				'recolhimentoFinal':chamdaEncalheAnteipadaController.getRecolhimentoFinal()
			};
			
			if(chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado){
				
				params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGridAux(chamdaEncalheAnteipadaController.nameGridPesquisaCota), params);
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/gravarCotasPesquisa",params, 
						 function (result){
					 
							 chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
							 chamdaEncalheAnteipadaController.zerarTotais();
							 
							 $("#dialog-novo",chamdaEncalheAnteipadaController.workspace).dialog("close");
							 
							 chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							 chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
						},
						chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);	
				 
			} else {
				
				var checkTodos = params['gravarTodos'];
				
				if(checkTodos == "undefined" || !checkTodos ){
					//params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGrid), params);
					params = serializeArrayToPost('listaChamadaEncalheAntecipada',  chamdaEncalheAnteipadaController.obterParametrosGridAux(chamdaEncalheAnteipadaController.gridSelectionHelper.checkedItems), params);
				} 
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/gravarCotas", params, 
					function (result){
						
						$("#dialog-novo",chamdaEncalheAnteipadaController.workspace).dialog("close");
				
						$("#checkCE",chamdaEncalheAnteipadaController.workspace).attr("checked", true);
						
						if (checkTodos) {
							
							$("#gridsCEAntecipada", chamdaEncalheAnteipadaController.workspace).hide();
							
							chamdaEncalheAnteipadaController.zerarTotais();
							 
							chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							
							chamdaEncalheAnteipadaController.isCheckedAll=false;
							chamdaEncalheAnteipadaController.gridSelectionHelper.isCheckedAll = false;
							$("#ceAntecipadaGrid",chamdaEncalheAnteipadaController.workspace).flexReload();
							
							$("#ceAntecipadaGrid",chamdaEncalheAnteipadaController.workspace).show();
							
						} else {
							
							chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
							
						}
						
						if(flagRecolhimento){								
							
							$("#dataProgramada", chamdaEncalheAnteipadaController.workspace).val($("#dataAntecipacao").val());
							
						}
						
						chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
						
						$("#dataAntecipacao", chamdaEncalheAnteipadaController.workspace).val("");
						
				}, null,true);
			}
			
		},
		
		exibirMensagemSucesso: function (result){
			
			var tipoMensagem = result.tipoMensagem;
			var listaMensagens = result.listaMensagens;
			if (tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}
		},
		
		validarItensSelecionados:function(group){
			
			return verifyAtLeastOneChecked(group); 
		},
		
		tratarErroPesquisaCota: function (jsonData){
			
			if (!jsonData || !jsonData.mensagens) {

				return;
			}
			
			var dadosValidacao = jsonData.mensagens.dados;
			
			if(dadosValidacao){
				
				exibirMensagem(
						jsonData.mensagens.tipoMensagem, 
						jsonData.mensagens.listaMensagens
				);
				
				$("#dialog-novo",chamdaEncalheAnteipadaController.workspace ).dialog("close");
			}
			
			var linhasDaGrid = $('#'+chamdaEncalheAnteipadaController.nameGridPesquisaCota+' tr',chamdaEncalheAnteipadaController.workspace);

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);

				if (dadosValidacao 
						&& ($.inArray(index, dadosValidacao) > -1)) {

					linha.removeClass('erow').addClass('linhaComErro');
					
				} else {

					linha.removeClass('linhaComErro');					
				}
			});
		},
		
		limparLinha: function(index){
			
			var linhasDaGrid = $('#'+chamdaEncalheAnteipadaController.nameGridPesquisaCota+' tr',chamdaEncalheAnteipadaController.workspace)[index];
		
			var valor = $(linhasDaGrid);
			
			valor.removeClass('linhaComErro');	
		},
		
		checkAll: function(input){
			
			chamdaEncalheAnteipadaController.gridSelectionHelper.isCheckedAll = input.checked; 
			
			if (chamdaEncalheAnteipadaController.tipoPesquisaSelecionado == chamdaEncalheAnteipadaController.tipoPesquisaGridCota){
				
				checkAll(input,chamdaEncalheAnteipadaController.groupNameCheckGridCota);
				
				if(input.checked == false){
					
					chamdaEncalheAnteipadaController.zerarTotais();
					
					$('#checkRecolhimentoFinal',chamdaEncalheAnteipadaController.workspace).attr("checked", false);
					$('#checkRecolhimentoFinal').attr('disabled', 'disabled');
					
				} else {
				
					$('#checkRecolhimentoFinal').removeAttr('disabled');
					
					chamdaEncalheAnteipadaController.totalExemplares = $("input[id^='qntExemplares']", chamdaEncalheAnteipadaController.workspace).sum();
					
					var checked = $("input[name=" + chamdaEncalheAnteipadaController.groupNameCheckGridCota + "]:checked").length;	
					
					chamdaEncalheAnteipadaController.totalCota = eval(checked);
					
					chamdaEncalheAnteipadaController.gridSelectionHelper.totalChecked = chamdaEncalheAnteipadaController.totalCota; 
				
					chamdaEncalheAnteipadaController.atribuirValorQntCotas();
					
					chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
				}
			} else{
				
				checkAll(input, chamdaEncalheAnteipadaController.groupNameCheck);
				
				if(input.checked == false) {
					
					chamdaEncalheAnteipadaController.zerarTotais();
					
					$('#checkRecolhimentoFinal',chamdaEncalheAnteipadaController.workspace).attr("checked", false);
					$('#checkRecolhimentoFinal').attr('disabled', 'disabled');
				} else {
					
					$('#checkRecolhimentoFinal').removeAttr('disabled');
					
					$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/obterTotalCotaExemplar", null, function (result){
						
						chamdaEncalheAnteipadaController.totalCota = result.qtdeTotalCotas;
						
						chamdaEncalheAnteipadaController.totalExemplares = result.qntTotalExemplares;
						
						chamdaEncalheAnteipadaController.gridSelectionHelper.totalExemplares = result.qntTotalExemplares;
						
						chamdaEncalheAnteipadaController.gridSelectionHelper.totalChecked = result.qtdeTotalCotas; 
						
						chamdaEncalheAnteipadaController.atribuirValorQntCotas();
					
						chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
						
						$("#ceAntecipadaGrid", chamdaEncalheAnteipadaController.workspace).flexReload();
					});	
				}
			}
		},
		
		zerarTotais: function (){
			
			chamdaEncalheAnteipadaController.totalCota = 0;
			
			chamdaEncalheAnteipadaController.totalExemplares = 0;	
			
			chamdaEncalheAnteipadaController.gridSelectionHelper.totalChecked = 0;
			
			chamdaEncalheAnteipadaController.gridSelectionHelper.totalExemplares = 0;
			
			chamdaEncalheAnteipadaController.atribuirValorQntCotas();
			
			chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
		},
		
		isItensSelecionados: function (){
			var groupName = (chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado)
								? chamdaEncalheAnteipadaController.groupNameCheckGridCota
									: chamdaEncalheAnteipadaController.groupNameCheck;

			if(!verifyAtLeastOneChecked(groupName) && chamdaEncalheAnteipadaController.gridSelectionHelper.getTotalCotas() < 1) {
				var mensagens = new Array('Selecione um item para realizar a operação!') ;
				exibirMensagem('WARNING',mensagens);
				return false;
			}
			return true;
		},
	
		exibirDialogData:function(tipoOperacao){
			
			if(!chamdaEncalheAnteipadaController.isItensSelecionados()){
				return;
			}
		
			$("#dialog-novo",chamdaEncalheAnteipadaController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: [
				         {id:"btn_confirma_programacao_ce",text:"Confirmar",
			        	   click: function() {
			        		   if(tipoOperacao =="Novo"){
									
									chamdaEncalheAnteipadaController.gravar();
									
								} else {
									
									chamdaEncalheAnteipadaController.reprogramarAntecipacaoEncalhe();	
								}
			        		  
			        		   	flagRecolhimento = $("#checkRecolhimentoFinal").is(':checked');
							  
			        		   	// $("#checkRecolhimentoFinal", chamdaEncalheAnteipadaController.workspace).attr("checked", false);
			        		  
			        	  	}
				         },
			        	{id:"btn_cancelar_antecipacao_ce",text:"Cancelar",
				         click:function(){
				        	 $( this ).dialog( "close" );
					        	 if(flagRecolhimento){								
					 				
					 				$("#checkRecolhimentoFinal", chamdaEncalheAnteipadaController.workspace).attr("checked", true);
					 				
					 			}
					        	$("#dataAntecipacao", chamdaEncalheAnteipadaController.workspace).val("");
			        		}  
			        	}  
				],
				form: $("#dialog-novo", chamdaEncalheAnteipadaController.workspace).parents("form")
			});
		},
			
		pesquisarProdutosSuccessCallBack:function() {
			
			chamdaEncalheAnteipadaController.pesquisarBox(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			chamdaEncalheAnteipadaController.pesquisarFornecedor(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			
			$("#dataProgramada",chamdaEncalheAnteipadaController.workspace).val("");
		},
		
		pesquisarProdutosErrorCallBack: function() {
			
			chamdaEncalheAnteipadaController.pesquisarBox(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			chamdaEncalheAnteipadaController.pesquisarFornecedor(chamdaEncalheAnteipadaController.getCodigoProdutoPesquisa());
			
			// $("#dataProgramada",chamdaEncalheAnteipadaController.workspace).val("");
		},
		
		montarComboFornecedores:function(result) {
			var comboFornecedores =  montarComboBox(result, true);
			
			$("#fornecedor",chamdaEncalheAnteipadaController.workspace).html(comboFornecedores);
		},
		
		montarComboBoxs:function(result) {
			
			var comboBoxes = "<option selected='selected'  value='-1'>Selecione</option>"; 
			comboBoxes = comboBoxes + "<option value=''>Todos</option>";
			comboBoxes = comboBoxes + montarComboBox(result, false);
			
			$("#box",chamdaEncalheAnteipadaController.workspace).html(comboBoxes);
		}, 
		
		pesquisarFornecedor:function(data) {
		
			$.postJSON(contextPath + "/produto/pesquisarFornecedorProduto",
					   data, chamdaEncalheAnteipadaController.montarComboFornecedores);
			
		},
		
		pesquisarBox:function(data){
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarBox",
					   data, chamdaEncalheAnteipadaController.montarComboBoxs);
		},
		
		validarEdicaoSuccessCallBack : function(){
			
			 var data = [{name:"codigoProduto",value:$("#codigoProduto",chamdaEncalheAnteipadaController.workspace).val()},
             			 {name:"numeroEdicao",value:$("#edicao",chamdaEncalheAnteipadaController.workspace).val()},
						];
			
			 $.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada",
					   data, function(result){
				 if(result.result!= ""){
					 $("#dataProgramada",chamdaEncalheAnteipadaController.workspace).val(result);	 
				 }
			 });
		},
		
		validarEdicaoErrorCallBack: function(){
			 // $("#dataProgramada",chamdaEncalheAnteipadaController.workspace).val("");
			
			if(flagRecolhimento){								
				
				$("#dataProgramada", chamdaEncalheAnteipadaController.workspace).val($("#dataAntecipacao").val());
				
			}
		},
		
		executarPreProcessamentoCota: function (resultado){
			
			chamdaEncalheAnteipadaController.limparGridCota();
			
			chamdaEncalheAnteipadaController.dataRecolhimentoPrevista = resultado.dataRecolhimentoPrevista; 
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens,""
				);
				
				$("#gridsCEAntecipada", chamdaEncalheAnteipadaController.workspace).hide();

				return resultado.tableModel;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.tableModel.rows, function(index, row) {
					
				var parametroCheckbox = '\'#qntExemplar' + index + '\', this';
				
				var keepChecked = chamdaEncalheAnteipadaController.gridSelectionHelper.keepChecked({
					numeroCota: "" + row.cell.numeroCota,
					codigoBox: "" + row.cell.codBox,
					idLancamento: "" + row.cell.idLancamento,
					qntExemplares: "" + row.cell.qntExemplares,
					nomeCota: "" + row.cell.nomeCota,
					codigoChamadaEncalhe: "" + row.cell.codigoChamadaEncalhe,
					id: "" + index
				}) ? 'checked="checked"' : '';

				var inputCheck = '<input ' + keepChecked + ' isEdicao="true" type="checkbox"  id="ch'+index+'" name="'+chamdaEncalheAnteipadaController.groupNameCheck
									+'" onclick="chamdaEncalheAnteipadaController.gridSelectionHelper.changeCheckboxState(this, ' + index + ')" />';
				
				var inputQuantidadeExemplares = 
					'<input type="hidden" id="qntExemplar' + index + '" name="qntExemplares" value="'+ row.cell.qntExemplares +'"/>';
						
				var inputNomeCota= 
						'<input type="hidden" id="nomeCota' + index + '" name="descricaoCota" value="'+ row.cell.numeroCota+'"/>';
						
				var inputNumeroCota= 
					'<input type="hidden" id="numCota' + index + '" name="numCota" value="'+ row.cell.numeroCota+'"/>';
						
				var inputHiddenCodigoChamadaEncalhe = 	
					'<input type="hidden" id="codigoChamadaAntecipada' + index + '" name="codigoChamadaAntecipada" value="'+ row.cell.codigoChamadaEncalhe+'"/>';
				
				var inputHiddenIdLancamento= 	
						'<input type="hidden" id="idLancamento' + index + '" name="idLancamento" value="'+ row.cell.idLancamento+'"/>';
				
				var inputHiddenCodBox= 	
						'<input type="hidden" id="codBox' + index + '" name="codBox" value="'+ row.cell.codBox+'"/>';
						
				row.cell.numeroCota = row.cell.numeroCota + inputNumeroCota 
									+ inputNomeCota  + inputHiddenCodigoChamadaEncalhe 
									+ inputHiddenIdLancamento + inputHiddenCodBox;

				row.cell.qntExemplares = row.cell.qntExemplares + inputQuantidadeExemplares;
				row.cell.sel = inputCheck;
			});
			
			chamdaEncalheAnteipadaController.setHiddenFornecedor();
			chamdaEncalheAnteipadaController.setHiddenNumeroEdicao();
			chamdaEncalheAnteipadaController.setHiddenProduto();
			chamdaEncalheAnteipadaController.setHiddenMunicipio();
			chamdaEncalheAnteipadaController.setHiddenTipoPontoPDV();
			
			if(resultado.recolhimentoFinal){
				$('#checkRecolhimentoFinal').attr('disabled', 'disabled');
				$("#checkRecolhimentoFinal", chamdaEncalheAnteipadaController.workspace).attr("checked", true);
			}
			
			$("#gridsCEAntecipada", chamdaEncalheAnteipadaController.workspace).show();
			$("#gridAntecipada", chamdaEncalheAnteipadaController.workspace).show();
			$("#gridPesquisaCota", chamdaEncalheAnteipadaController.workspace).hide();
			
			chamdaEncalheAnteipadaController.tipoPesquisaSelecionado = "";
			
			return resultado.tableModel;
		},
		
		pesquisarCotaErrorCallBack: function (idInputCkeck,idInputExemplares,idInputCota,indexLinha){
			
			var exemplares = eval( ($(idInputExemplares,chamdaEncalheAnteipadaController.workspace).val()=="")?0:$(idInputExemplares,chamdaEncalheAnteipadaController.workspace).val());
			
			var check  = $(idInputCkeck,chamdaEncalheAnteipadaController.workspace).attr('checked');
			
			chamdaEncalheAnteipadaController.limparLinha(indexLinha);
			
			if(exemplares > 0 &&  check == "checked"){
			
				chamdaEncalheAnteipadaController.totalCota -= 1;
				chamdaEncalheAnteipadaController.totalExemplares -= exemplares;
				
				chamdaEncalheAnteipadaController.atribuirValorQntCotas();
				chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
			}
			
			$(idInputCkeck,chamdaEncalheAnteipadaController.workspace).attr("checked",false);
			$(idInputCkeck,chamdaEncalheAnteipadaController.workspace).attr("disabled","disabled");
			$(idInputExemplares,chamdaEncalheAnteipadaController.workspace).val("");
			
		},
		
		pesquisarCotaSuccessCallBack: function (idInputCkeck,idInputExemplares, idInputCota,indexLinha){
			
			$(idInputCkeck, chamdaEncalheAnteipadaController.workspace).removeAttr("disabled"); 
			
			chamdaEncalheAnteipadaController.limparLinha(indexLinha);
			
			var param = [{name:"numeroCota", value:$(idInputCota,chamdaEncalheAnteipadaController.workspace).val()},
			             {name:"codigoProduto", value:chamdaEncalheAnteipadaController.getHiddenProduto()},
			             {name:"numeroEdicao", value:chamdaEncalheAnteipadaController.getHiddenNumeroEdicao()},
			             {name:"fornecedor", value:chamdaEncalheAnteipadaController.getHiddenFornecedor()},
			             {name:"programacaoRealizada", value:chamdaEncalheAnteipadaController.getProgramacaoRealizada()},
			             {name:"municipio", value:chamdaEncalheAnteipadaController.getHiddenMunicipio()},
			             {name:"tipoPontoPDV", value:chamdaEncalheAnteipadaController.getHiddenTipoPontoPDV()}
			             ];
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/obterQuantidadeExemplares",
					param, 
					function (result){
				
						$(idInputExemplares,chamdaEncalheAnteipadaController.workspace).val(result.quantidade);
						$("#codigoChamadaAntecipada" + indexLinha,chamdaEncalheAnteipadaController.workspace).val(result.idChamadaEncalhe);
						
						var check  = document.getElementById("sel",chamdaEncalheAnteipadaController.workspace).checked; 
						
						if(check){
							
							chamdaEncalheAnteipadaController.calcularTotalCota(idInputExemplares, $(idInputCkeck,chamdaEncalheAnteipadaController.workspace));
							$(idInputCkeck,chamdaEncalheAnteipadaController.workspace).attr("checked",true);
							$("#sel",chamdaEncalheAnteipadaController.workspace).attr("checked",true);
						}
					}, 
					function (result){
						
						$(idInputCkeck,chamdaEncalheAnteipadaController.workspace).attr("checked",false);
						$(idInputCkeck,chamdaEncalheAnteipadaController.workspace).attr("disabled","disabled");
						$(idInputExemplares,chamdaEncalheAnteipadaController.workspace).val("");

						chamdaEncalheAnteipadaController.limparLinhaCEAntecipadaCotaGrid(indexLinha);
					});	
		},

		limparLinhaCEAntecipadaCotaGrid: function(indexLinha) {

			$("#numCota" + indexLinha).val("");
			$("#descricaoCota" + indexLinha).val("");
			$("#qntExemplares" + indexLinha).val("");
			$("#chCota" + indexLinha).attr("checked", false);
		},
		
		executarPreProcessamentoGridCota:function(resultado){
			
			chamdaEncalheAnteipadaController.limparGridCota();
			
			chamdaEncalheAnteipadaController.dataRecolhimentoPrevista = resultado.dataRecolhimentoPrevista;
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$("#gridsCEAntecipada",chamdaEncalheAnteipadaController.workspace).hide();

				return resultado;
			}
			
			// Monta as colunas com os inputs do grid
			$.each(resultado.rows, function(index, row) {
				
				var parametroCheckbox = '\'#qntExemplar' + index + '\', this';
				
				var keepChecked = chamdaEncalheAnteipadaController.gridSelectionHelper.keepChecked({
					numeroCota: "" + row.cell.numeroCota,
					codigoBox: "" + row.cell.codBox,
					idLancamento: "" + row.cell.idLancamento,
					qntExemplares: "" + row.cell.qntExemplares,
					nomeCota: "" + row.cell.nomeCota,
					codigoChamadaEncalhe: "" + row.cell.codigoChamdaEncalhe,
					id: "" + index
				}) ? 'checked="checked"' : '';
				
				var idCheck = "chCota"+index;
				
				var paramIdCheck = '\'#chCota' + index +'\','+'\'#qntExemplares' + index + '\','+'\'#numCota' + index + '\','+index;
				
				var hiddenId = '<input type="hidden" name="id" value="' + index + '" />';

				var parametroPesquisaCota = '\'#numCota' + index + '\', \'#descricaoCota'+ index + '\', false, function(){chamdaEncalheAnteipadaController.pesquisarCotaSuccessCallBack('+paramIdCheck+')}, function(){ chamdaEncalheAnteipadaController.pesquisarCotaErrorCallBack('+paramIdCheck+')}';
				
				var inputCodigoCota = 
					'<input isEdicao="true" type="text" id="numCota' + index + '" name="numCota" style="width:80px; float:left; margin-right:10px;" maxlenght="255" onchange="chamdaEncalheAnteipadaController.pesquisaCota.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');" />';

				var parametroAutoCompleteCota = '\'#descricaoCota' + index + '\', true';

				var inputDescricaoCota = 
					'<input isEdicao="true" type="text" id="descricaoCota' + index + '" name="descricaoCota" style="width:580px;" maxlenght="255" onkeyup="chamdaEncalheAnteipadaController.pesquisaCota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onblur="chamdaEncalheAnteipadaController.pesquisaCota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" />';

				var inputQuantidadeExemplares = 
					'<input isEdicao="true" type="text" id="qntExemplares' + index + '" name="qntExemplares" disabled="disabled" style="width:80px; text-align: center"   />';
				
				var parametroCheckbox = '\'#qntExemplares' + index + '\', this';

				var inputCheck = '<input isEdicao="true" disabled="disabled" type="checkbox" id="'+idCheck+'" name="'+chamdaEncalheAnteipadaController.groupNameCheckGridCota+'" onclick="chamdaEncalheAnteipadaController.calcularTotalCota( '+parametroCheckbox +')" />';	
				
				var inputHiddenCodigoChamadaEncalhe = 	
					'<input type="hidden" id="codigoChamadaAntecipada' + index + '" name="codigoChamadaAntecipada" value="'+ row.cell.codigoChamdaEncalhe+'"/>';
					
				var inputHiddenIdLancamento= 	
						'<input type="hidden" id="idLancamento' + index + '" name="idLancamento" value="'+ row.cell.idLancamento+'"/>';
						
				
				row.cell.cota = inputCodigoCota + hiddenId + inputHiddenCodigoChamadaEncalhe +inputHiddenIdLancamento;
				row.cell.nome = inputDescricaoCota;
				row.cell.qtdeExemplares = inputQuantidadeExemplares;	
				row.cell.sel = inputCheck;
			});
			
			chamdaEncalheAnteipadaController.setHiddenFornecedor();
			chamdaEncalheAnteipadaController.setHiddenNumeroEdicao();
			chamdaEncalheAnteipadaController.setHiddenProduto();
			chamdaEncalheAnteipadaController.setHiddenMunicipio();
			chamdaEncalheAnteipadaController.setHiddenTipoPontoPDV();
			
			$("#gridsCEAntecipada", chamdaEncalheAnteipadaController.workspace).show();
			$("#gridAntecipada", chamdaEncalheAnteipadaController.workspace).hide();
			$("#gridPesquisaCota", chamdaEncalheAnteipadaController.workspace).show();
			
			chamdaEncalheAnteipadaController.tipoPesquisaSelecionado = chamdaEncalheAnteipadaController.tipoPesquisaGridCota;
			
			$("input[name='numCota']",chamdaEncalheAnteipadaController.workspace).numeric();
			
			return resultado;
		},
		
		calcularTotalGridCota: function (idInputExemplares, inputCkeck){
			
			var check  = document.getElementById("sel",chamdaEncalheAnteipadaController.workspace).checked ; 
			
			if(check){

				var exemplares = eval( ($(idInputExemplares,chamdaEncalheAnteipadaController.workspace).val()=="")?0:$(idInputExemplares,chamdaEncalheAnteipadaController.workspace).val());
				var checked = $("input[name=" + chamdaEncalheAnteipadaController.groupNameCheck + "]:checked",chamdaEncalheAnteipadaController.workspace).length;
				
				chamdaEncalheAnteipadaController.totalExemplares = ($("input[id^='qntExemplar']",chamdaEncalheAnteipadaController.workspace).sum() - exemplares);	
				chamdaEncalheAnteipadaController.totalCota = eval(checked);
			
				chamdaEncalheAnteipadaController.atribuirValorQntCotas();
				chamdaEncalheAnteipadaController.atribuirValorQntExemplares();		

				chamdaEncalheAnteipadaController.desmarcarCheckTodos();				
				
			}else{
				chamdaEncalheAnteipadaController.calcularTotalCota(idInputExemplares, inputCkeck);
			}	
		},
		
		calcularTotalCota: function(idExemplares,input){
			
			var exemplares = eval( ($(idExemplares).val()=="")?0:$(idExemplares).val());
			
			if(input.checked == false){
				chamdaEncalheAnteipadaController.totalCota -= 1;
				chamdaEncalheAnteipadaController.totalExemplares -= exemplares;	
			}
			else{
				
				chamdaEncalheAnteipadaController.totalCota += 1;
				chamdaEncalheAnteipadaController.totalExemplares += exemplares;
			}
			
			chamdaEncalheAnteipadaController.atribuirValorQntCotas();
			chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
			
			chamdaEncalheAnteipadaController.desmarcarCheckTodos();	
		},
		
		atribuirValorQntExemplares: function (){
			$("#idTotalExemplares",chamdaEncalheAnteipadaController.workspace).val(chamdaEncalheAnteipadaController.gridSelectionHelper.getTotalExemplares());
		},
		
		atribuirValorQntCotas: function (){
			$("#idTotalCotas",chamdaEncalheAnteipadaController.workspace).val(chamdaEncalheAnteipadaController.gridSelectionHelper.getTotalCotas());
		},
		
		desmarcarCheckTodos: function (){
			$('#sel',chamdaEncalheAnteipadaController.workspace).attr("checked",false);
			checkedItems = new Array();
			uncheckedItems= new Array();
		},
		
		obterParametrosGridAux: function(checkedList){
			
			var listaChamadaEncalheAntecipada = new Array();
			
			$.each(checkedList, function(index, value) {
				
				var cotaSelecionada = {'codigoProduto':chamdaEncalheAnteipadaController.getHiddenProduto(),
						'numeroEdicao':chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
						'numeroCota':value.numeroCota,
						'nomeCota':value.nomeCota,
						'id':value.id,
						'qntExemplares':value.exemplares,
						'codigoChamadaAntecipada':value.codigoChamadaAntecipada,
						'idLancamento':value.idLancamento};
			
				listaChamadaEncalheAntecipada.push(cotaSelecionada);
				
			});
			
			return listaChamadaEncalheAntecipada;
		},
	
		obterParametrosGrid: function(grid){
			
			var listaChamadaEncalheAntecipada = new Array();
			
			var linhasDaGrid = $('#'+grid+' tr', chamdaEncalheAnteipadaController.workspace);
			
			var groupName = (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)
							? chamdaEncalheAnteipadaController.groupNameCheckGridCota
									:chamdaEncalheAnteipadaController.groupNameCheck;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCheck = linha.find("td")[(chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?3:4];
				var colunaCota = linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?0:1];
				var colunaNomeCota =  linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?1:2];
				var colunaQntExemplares =  linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?2:3];
				
				var id = $(colunaCota,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="id"]').val();
				
				var codigoCota = $(colunaCota,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="numCota"]').val();
				
				var nomeCota = $(colunaNomeCota,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="descricaoCota"]').val();
				
				var qntExemplares =  $(colunaQntExemplares,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="qntExemplares"]').val();
			
				var codigoChamadaEncalhe = $(colunaCota,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="codigoChamadaAntecipada"]').val();
				
				var idLancamento = $(colunaCota,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="idLancamento"]').val();
				
				var check  = $(colunaCheck,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="'+groupName+'"]');
				
				if (chamdaEncalheAnteipadaController.isAtributosVaziosOrSelecionados(codigoCota,check)){
					return true;
				}
				
				var cotaSelecionada = {'codigoProduto':chamdaEncalheAnteipadaController.getHiddenProduto(),
						'numeroEdicao':chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
						'numeroCota':codigoCota,
						'nomeCota':nomeCota,
						'id':id,
						'qntExemplares':qntExemplares,
						'codigoChamadaEncalhe':codigoChamadaEncalhe,
						'idLancamento':idLancamento};
			
				listaChamadaEncalheAntecipada.push(cotaSelecionada);
				
			});
			
			return listaChamadaEncalheAntecipada;
		},
		
		isAtributosVaziosOrSelecionados: function (codigoCota,inputCheck){
			
			var check  = inputCheck.attr('checked');
			
			return (typeof check == "undefined" || !$.trim(codigoCota));
		},
		
		limparGridPesquisaCota: function (){
			 $('#'+ chamdaEncalheAnteipadaController.nameGridPesquisaCota+' tr',chamdaEncalheAnteipadaController.workspace).remove();
		},
		
		limparGridCota: function (){
			$('#'+ chamdaEncalheAnteipadaController.nameGrid +' tr',chamdaEncalheAnteipadaController.workspace).remove();
		},
		
		formatarCampos: function (){
			
			$("input[name='numCota']",chamdaEncalheAnteipadaController.workspace).numeric();
			$("input[name='descricaoCota']",chamdaEncalheAnteipadaController.workspace).autocomplete({source: ""});
		},
		
		exportar:function(fileType) {
			
			var paramDataProgaramada = "&dataProgaramada=" + $("#dataProgramada",chamdaEncalheAnteipadaController.workspace).val();
			
			if (chamdaEncalheAnteipadaController.tipoPesquisaSelecionado == chamdaEncalheAnteipadaController.tipoPesquisaGridCota){
				
				if(!chamdaEncalheAnteipadaController.isItensSelecionados()){
					return;
				}
				
				var listaChamadaEncalheAntecipada = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGridPesquisaCota));
				
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/validarCotasPesquisa",
						 listaChamadaEncalheAntecipada, function (result){
					
					var paramProduto = "&codigoProduto="+chamdaEncalheAnteipadaController.getHiddenProduto();
					var paramEdicao = "&numeroEdicao=" +chamdaEncalheAnteipadaController.getHiddenNumeroEdicao();
					var paramFornecedor = "&fornecedor="+chamdaEncalheAnteipadaController.getHiddenFornecedor();
					var paramMunicipio = "&municipio="+chamdaEncalheAnteipadaController.getHiddenMunicipio();
					var paramTipoPontoPDV ="&tipoPontoPDV=" + chamdaEncalheAnteipadaController.getHiddenTipoPontoPDV();
					
					window.location = 
						contextPath + "/devolucao/chamadaEncalheAntecipada/exportarPesquisaCotas?fileType=" 
								+ fileType + paramDataProgaramada
								+paramProduto + paramEdicao + paramFornecedor + paramMunicipio + paramTipoPontoPDV;
				},
				chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);
			}
			else{
				
				window.location = 
					contextPath + "/devolucao/chamadaEncalheAntecipada/exportar?fileType=" + fileType + paramDataProgaramada;
			}
		},
		
		recarregarComboRotas:function(idRoteiro){
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/recarregarListaRotas",
					[{name:"roteiro",value:idRoteiro}], function(result){
				
				var comboRotas =  montarComboBox(result, true);
				
				$("#rota",chamdaEncalheAnteipadaController.workspace).html(comboRotas);	
			});
		},
		
		recarregarComboRoteiroRotas:function(idBox){
			
			$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/recarregarRoteiroRota",
					[{name:"idBox",value:idBox}], function(result){
				
				var comboRotas =  montarComboBoxCustomJson(result.rotas, true);
				var comboRoteiros = montarComboBoxCustomJson(result.roteiros, true);
				
				$("#rota",chamdaEncalheAnteipadaController.workspace).html(comboRotas);
				$("#roteiro",chamdaEncalheAnteipadaController.workspace).html(comboRoteiros);
			});
		},
		
		sumarizarCotasSelecionadas:function(grid){
			
			var linhasDaGrid = $('#'+grid+' tr',chamdaEncalheAnteipadaController.workspace);
			
			var groupName = (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)
							? chamdaEncalheAnteipadaController.groupNameCheckGridCota
									:chamdaEncalheAnteipadaController.groupNameCheck;
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCheck = linha.find("td")[(chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?3:4];
				var colunaQntExemplares =  linha.find("td")[ (chamdaEncalheAnteipadaController.nameGridPesquisaCota == grid)?2:3];
				
				var qntExemplares =  $(colunaQntExemplares,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="qntExemplares"]').val();
			
				var inputCheck  = $(colunaCheck,chamdaEncalheAnteipadaController.workspace).find("div").find('input[name="'+groupName+'"]');
				
				var check  = inputCheck.attr('checked');
				
				if (typeof check != "undefined"){
					chamdaEncalheAnteipadaController.totalCota += 1;
					chamdaEncalheAnteipadaController.totalExemplares += eval(qntExemplares);
				}
			});
			
			chamdaEncalheAnteipadaController.atribuirValorQntCotas();
			chamdaEncalheAnteipadaController.atribuirValorQntExemplares();
		},
		
		cancelarProgramacaoAntecipacaoEncalhe:function(){
			
			var params = {
					codigoProduto:chamdaEncalheAnteipadaController.getHiddenProduto(),
					numeroEdicao:chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
					'cancelarTodos':$("#sel", chamdaEncalheAnteipadaController.workspace).is(':checked'),
					'dataProgramada':chamdaEncalheAnteipadaController.dataRecolhimentoPrevista,
					'recolhimentoFinal':chamdaEncalheAnteipadaController.getRecolhimentoFinal()
				};

			if(chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado){				
				params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGridPesquisaCota), params);
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/cancelarChamdaEncalheCotasPesquisa",
						params,
						function (result){
					 
							 chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
							 chamdaEncalheAnteipadaController.zerarTotais();
							 
							 $("#dialog-novo",chamdaEncalheAnteipadaController.workspace ).dialog("close");
							 $("#dialog-cancelamentoCE",chamdaEncalheAnteipadaController.workspace ).dialog("close");
							 
							 chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							 chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);

							 $("#checkRecolhimentoFinal", chamdaEncalheAnteipadaController.workspace).attr("checked", false);
							 $("#checkCE", chamdaEncalheAnteipadaController.workspace).attr("checked", false);
						},
						chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);	
			} else {
				
				var checkTodos = params['cancelarTodos'];
				if(checkTodos == "undefined" || !checkTodos ){
					
					params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGrid), params);
				}
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/cancelarChamdaEncalheCotas",
						params, 
						function (result){
					
							if (checkTodos) {
								$("#gridsCEAntecipada",chamdaEncalheAnteipadaController.workspace).hide();
							} else {
								chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
							}
							
							chamdaEncalheAnteipadaController.zerarTotais();
						
							chamdaEncalheAnteipadaController.desmarcarCheckTodos();
							chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
							
							$("#dialog-cancelamentoCE",chamdaEncalheAnteipadaController.workspace ).dialog("close");

							$("#checkRecolhimentoFinal", chamdaEncalheAnteipadaController.workspace).attr("checked", false);
							$("#checkCE", chamdaEncalheAnteipadaController.workspace).attr("checked", false);
								
						}, null,true);
			}
			
			$("#dataProgramada").val(chamdaEncalheAnteipadaController.dataRecolhimentoPrevista);
			
		},
		
		reprogramarAntecipacaoEncalhe:function(){
			
			var params ={'codigoProduto':chamdaEncalheAnteipadaController.getHiddenProduto(),
					'numeroEdicao':chamdaEncalheAnteipadaController.getHiddenNumeroEdicao(),
					'dataRecolhimento':$("#dataAntecipacao",chamdaEncalheAnteipadaController.workspace).val(),
					'dataProgramada': $("#dataProgramada",chamdaEncalheAnteipadaController.workspace).val(),
					'gravarTodos':$("#sel", chamdaEncalheAnteipadaController.workspace).is(':checked'),
					'recolhimentoFinal':$("#checkRecolhimentoFinal", chamdaEncalheAnteipadaController.workspace).is(':checked')
				};
			
			if(chamdaEncalheAnteipadaController.tipoPesquisaGridCota == chamdaEncalheAnteipadaController.tipoPesquisaSelecionado){
				
				params = serializeArrayToPost('listaChamadaEncalheAntecipada',chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGridPesquisaCota),params);
								
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/reprogramarCotasPesquisa",
					params, 
					function (result){
				 
						 chamdaEncalheAnteipadaController.montarGridPesquisaCotas();
						 chamdaEncalheAnteipadaController.zerarTotais();
						 
						 $("#dialog-novo",chamdaEncalheAnteipadaController.workspace).dialog("close");
						 
						 chamdaEncalheAnteipadaController.desmarcarCheckTodos();
						 chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
					},
					
					chamdaEncalheAnteipadaController.tratarErroPesquisaCota,true);	
				
			} else {
				
				var checkTodos = params['gravarTodos'];
				if(checkTodos == "undefined" || !checkTodos ) {
					
					params = serializeArrayToPost('listaChamadaEncalheAntecipada', chamdaEncalheAnteipadaController.obterParametrosGrid(chamdaEncalheAnteipadaController.nameGrid), params);
				}
				
				$.postJSON(contextPath + "/devolucao/chamadaEncalheAntecipada/reprogramarCotas", params, 
				function (result){
					
					if (checkTodos) {
						
						$("#gridsCEAntecipada", chamdaEncalheAnteipadaController.workspace).hide();
						
					} else {
						
						chamdaEncalheAnteipadaController.pesquisarCotasPorProduto();
						
					}
					
					$("#dialog-novo",chamdaEncalheAnteipadaController.workspace).dialog("close");
					
					chamdaEncalheAnteipadaController.zerarTotais();
					
					chamdaEncalheAnteipadaController.desmarcarCheckTodos();

					chamdaEncalheAnteipadaController.exibirMensagemSucesso(result);
					
					
				}, null,true);
			}
		},
		
		processarRenderizacaoDeBotoesCE:function(){
			
			if (chamdaEncalheAnteipadaController.getProgramacaoRealizada() == true){
				$("#bt_cancelar_programacao",chamdaEncalheAnteipadaController.workspace).show();
				$("#bt_reprogramar_CE",chamdaEncalheAnteipadaController.workspace).show();
				$("#bt_confirmar_novo",chamdaEncalheAnteipadaController.workspace).hide();
			}
			else{
				$("#bt_cancelar_programacao",chamdaEncalheAnteipadaController.workspace).hide();
				$("#bt_reprogramar_CE",chamdaEncalheAnteipadaController.workspace).hide();
				$("#bt_confirmar_novo",chamdaEncalheAnteipadaController.workspace).show();
			}
		},
		
		exibirDialogCancelamentoCE:function(){
			
			if(!chamdaEncalheAnteipadaController.isItensSelecionados()){
				return;
			}
			
			$("#dialog-cancelamentoCE" ,chamdaEncalheAnteipadaController.workspace).dialog({
				resizable: false,
				height:'auto',
				width:360,
				modal: true,
				buttons: [
			         {id:"btn_confirma_cancelar_ce",text:"Confirmar",
		        	  click: function() {
		        		  chamdaEncalheAnteipadaController.cancelarProgramacaoAntecipacaoEncalhe();	
		        	  	}
			         },
		        	{id:"btn_cancelar_ce",text:"Cancelar",
			         click:function(){
		        			$( this ).dialog( "close" );
		        		}	  
		        	}  
				],
				form: $("#dialog-cancelamentoCE", chamdaEncalheAnteipadaController.workspace).parents("form")
			});
		},
		
		init : function(pesquisaCota) {
			
			chamdaEncalheAnteipadaController.pesquisaCota = pesquisaCota;
			
		    definirAcaoPesquisaTeclaEnter(chamdaEncalheAnteipadaController.workspace);	

			$("#dataAntecipacao",chamdaEncalheAnteipadaController.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('input[id^="data"]',chamdaEncalheAnteipadaController.workspace).mask("99/99/9999");
			
			$("#edicao",chamdaEncalheAnteipadaController.workspace).numeric();

			$("#produto",chamdaEncalheAnteipadaController.workspace).autocomplete({source: ""});
			
			$("#codigoProduto",chamdaEncalheAnteipadaController.workspace).focus();
			
			$("#ceAntecipadaGrid",chamdaEncalheAnteipadaController.workspace).flexigrid({
				preProcess:chamdaEncalheAnteipadaController.executarPreProcessamentoCota,
				dataType : 'json',
				colModel : [ {
					display : 'Box',
					name : 'box',
					width : 60,
					sortable : true,
					align : 'left'
				},{
					display : 'Cota',
					name : 'numeroCota',
					width : 130,
					sortable : true,
					align : 'center'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 530,
					sortable : true,
					align : 'left'
				}, {
					display : 'Qtde. Exemplares',
					name : 'qntExemplares',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : ' ',
					name : 'sel',
					width : 50,
					sortable : true,
					align : 'center'
				}],
				sortname : "box",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});

		$("#ceAntecipadaCotaGrid",chamdaEncalheAnteipadaController.workspace).flexigrid({
			preProcess:chamdaEncalheAnteipadaController.executarPreProcessamentoGridCota,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 590,
				sortable : false,
				align : 'left'
			}, {
				display : 'Qtde. Exemplares',
				name : 'qtdeExemplares',
				width : 130,
				sortable : false,
				align : 'center'
			}, {
				display : ' ',
				name : 'sel',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			disableSelect : true,
			width : 960,
			height : 180
		});
	},
		
}, BaseController);
//@ sourceURL=scriptChamadaAntecipada.js