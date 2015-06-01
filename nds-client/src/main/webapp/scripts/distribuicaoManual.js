var distribuicaoManual = $.extend(true, {
	
	rowCount : 0,
	workspace : null,
	exibindoMensagem : false,
	hiddenIdCota : '<input type="hidden" id="idCotaGrid#index" value="#valor"/>',
	hiddenStatusCota : '<input type="hidden" id="statusCotaGrid#index" value="#valor"/>',
	inputNomeCota : '<div><input type="text" class="inputNomeCotaGrid" id="nomeCotaGrid#index" value="#valor" onblur="distribuicaoManual.checaSeRemoveu(\'#nomeCotaGrid#index\', #index)"></div>',
	inputPercEstoque : '<div class="textoGridCota" id="percEstoqueGrid#index" >#valor</div>',
	inputReparte : '<div><input type="text" class="inputGridCota" id="reparteGrid#index" name="reparteGrid" value="#valor" onkeypress="distribuicaoManual.keyupFunction(event, #index)" onchange="distribuicaoManual.calcularPercEstoque(#index)" class="inputGridCota" /></div>',
	inputNumeroCota : '<div><input type="text" class="inputGridCota" id="numeroCotaGrid#index" name="numeroCotaGrid" value="#valor" onchange="distribuicaoManual.pesquisarCota(\'#numeroCotaGrid#index\', #index)" /></div>',
	idLancamento : 0,
	reparteTotal : 0,
	isSolicitarSenhaReparte : true,
	isSolicitarSenhaCotaSuspensa : true,
	
	focusRight : function focusRight(input){
		
	    var position = $(input, distribuicaoManual.workspace).val().length;
	    
	    setTimeout(function(){
	    	
	        $(input, distribuicaoManual.workspace).focus().setCursorPosition(position);
	        
	        $(input, distribuicaoManual.workspace).select();
	        
	    },1);
	},
	
	obterMatrizSelecionada : function obterMatrizSelecionada(){
		var selecionado = {};
		
		$.each(matrizDistribuicao.lancamentos, function(index, lancamento){
			if(lancamento.selecionado) {
				selecionado = lancamento;
			}
		});
		
		return selecionado;
	},
	
	init : function() {
		var lancamentoSelecionado = distribuicaoManual.obterMatrizSelecionada();
		
		distribuicaoManual.workspace = $('.estudosManuaisGrid');
		this.configGrid();
		distribuicaoManual.workspace.find('tbody').append(distribuicaoManual.construirLinhaVazia());
		
		distribuicaoManual.idLancamento = lancamentoSelecionado.idLancamento;
		distribuicaoManual.reparteTotal = lancamentoSelecionado.lancto;
		this.atualizarTotalDistribuido(0);
		$('#distribuicao-manual-repGeral').html($('#distribuicao-manual-repDistribuir').text());
		
		distribuicaoManual.isSolicitarSenhaReparte = true;
		distribuicaoManual.isSolicitarSenhaCotaSuspensa = true;
		
		distribuicaoManual.focusRight("#numeroCotaGrid"+(distribuicaoManual.rowCount - 1));
		
		distribuicaoManual.verificarICD();
	},
	
	verificarICD : function(){
		$.postJSON(
	            pathTela + "/distribuicaoManual/verificarICD", 
	            [{name : "codProduto" , value : $('#distribuicao-manual-codigoProduto').text()}],
	            function(result) {
	            },
	            function(result){
	            	//distribuicaoManual.voltar();
	            	setTimeout(function() { 
		            	$(".ui-tabs-selected").find("span").click();
		    			$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
	    			}, 500);
	            }
	            );
	},
	
	voltar : function() {
		var idEstudo = $('#distribuicao-manual-estudo').text();
		
		if (!idEstudo) {
			distribuicaoManual.confirmar("#dialog-voltar", function() {
				$(".ui-tabs-selected").find("span").click();
				$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
				
			}, function() {
				setTimeout(function() { $('#numeroCotaGrid'+ (distribuicaoManual.rowCount - 1), distribuicaoManual.workspace).focus(); }, 100);
			});
		}else{
			$(".ui-tabs-selected").find("span").click();
			$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
		}
		
		matrizDistribuicao.pesquisar();
	},
	
	cancelar : function() {
		distribuicaoManual.confirmar("#dialog-cancelar-estudo", function() {
			$(".ui-tabs-selected").find("span").click();
			$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
			
		}, function() {
			setTimeout(function() { $('#numeroCotaGrid'+ (distribuicaoManual.rowCount - 1), distribuicaoManual.workspace).focus(); }, 100);
		});
	},
	
	confirmar : function(dialogId, callbackFunction, errorCallback, posCloseCallback) {
		$(dialogId).dialog({
		        escondeHeader: false,
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					callbackFunction();
					$(this).dialog("close");
				},
				"Cancelar": function() {
					errorCallback();
					$(this).dialog("close");
				}
			}
		});
	},
	
	atualizarTotalDistribuido : function(valor) {
		totalDistribuido = valor;
		$('#distribuicao-manual-totalDistribuido').html(valor);
	},

	somarReparteDistribuido : function(index) {
		var soma = 0;
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if (i != index) {
				if ($("#reparteGrid"+ i, distribuicaoManual.workspace).val()) {
					soma += parseInt($("#reparteGrid"+ i, distribuicaoManual.workspace).val());
				}
			}
		}
		return soma;
	},
	
	keyupFunction : function(event, index){
		
		if (event && event.keyCode == 13){
			$("#numeroCotaGrid"+(index+1)).focus();
			this.calcularPercEstoque(index);
		}
	},
	
	calcularPercEstoque : function(index) {
		
		$("#percEstoqueGrid"+ index, distribuicaoManual.workspace).html('0');
		
		var totalGeral = parseInt($("#reparteInicial").val());
		var repCota = parseInt($("#reparteGrid"+ index, distribuicaoManual.workspace).val());
		var totalDistribuido = distribuicaoManual.somarReparteDistribuido(index);
		
		if (((repCota / totalGeral) * 100).toFixed(2) >= 5) {
			$("#reparteGrid"+ index, distribuicaoManual.workspace).css("background-color", "#FFFF00");
			
			if(distribuicaoManual.isSolicitarSenhaReparte){

//				exibirMensagemDialog('WARNING', ['Reparte acima de 5%. Posteriormente poderá ser liberado mediante senha de autorização!'], '');
//				distribuicaoManual.isSolicitarSenhaReparte = false;

				var message = 'Reparte acima de 5%. Deseja incluir a cota? ';
				
				usuarioController.supervisor.verificarRoleSupervisao({
	            	optionalDialogMessage: message,
	            	callbacks: {
	    				usuarioSupervisorCallback: function() {

	    					distribuicaoManual.isSolicitarSenhaReparte = false;
	    					
	    					var a = $("input[id^='numeroCotaGrid']").length;
							
							setTimeout(function() { 
								$("input[id^='numeroCotaGrid']", distribuicaoManual.workspace)[a-1].focus();
							}, 500);
	    					
	    				},
						usuarioNaoSupervisorCallback: function(){
							distribuicaoManual.isSolicitarSenhaReparte = true;
							distribuicaoManual.limparLinha(index);
							$("#row"+ (index+1), distribuicaoManual.workspace).remove();
							$("#reparteGrid"+ index, distribuicaoManual.workspace).css("background-color", "#FFFFFF");
							$("#reparteGrid"+ index, distribuicaoManual.workspace).val('0');
							$("#percEstoqueGrid"+ index, distribuicaoManual.workspace).html('0');
							
							var a = $("input[id^='numeroCotaGrid']").length;
							
							setTimeout(function() { 
								$("input[id^='numeroCotaGrid']", distribuicaoManual.workspace)[a-1].focus();
							}, 500);
							
		        		}
	    			}
	            });
			}
			
		} else {
			$("#reparteGrid"+ index, distribuicaoManual.workspace).css("background-color", "#FFFFFF");
		}
		
		if (repCota <= totalGeral) {
		
			var repDistrib = totalGeral - totalDistribuido;
			
			if (repCota <= repDistrib) {
				$("#percEstoqueGrid"+ index, distribuicaoManual.workspace).html(((repCota / totalGeral) * 100).toFixed(2).replace('.', ','));
				totalDistribuido += repCota;
				$('#distribuicao-manual-totalDistribuido').html(totalDistribuido);
				repDistrib -= repCota;
				$('#distribuicao-manual-repDistribuir').html(repDistrib);
			} else {
				$("#reparteGrid"+ index, distribuicaoManual.workspace).val('0');
				exibirMensagemDialog('WARNING', ['Você não possui saldo suficiente para distribuir essa quantidade para a cota, reveja os valores.'], '');
				$("#reparteGrid"+ index, distribuicaoManual.workspace).focus();
			}
		
		} else {
			$("#reparteGrid"+ index, distribuicaoManual.workspace).val('0');
			exibirMensagemDialog('WARNING', ['O reparte da cota deve ser menor que o Total de Reparte a Distribuir.'], '');
			$("#reparteGrid"+ index, distribuicaoManual.workspace).focus();
		}
	},
	
	existeLinhaVazia : function() {
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if (!$('#numeroCotaGrid'+ i, distribuicaoManual.workspace).val()
					&& ($('#numeroCotaGrid'+ i, distribuicaoManual.workspace).val() != undefined)
					&& !$('#nomeCotaGrid'+ i, distribuicaoManual.workspace).val()
					&& ($('#nomeCotaGrid'+ i, distribuicaoManual.workspace).val() != undefined)) {
				return true;
			}
		}
		return false;
	},
	
	construirLinhaVazia : function() {
		
		if (!distribuicaoManual.existeLinhaVazia()) {
			
			var linhaVazia = '';
			
			if (!distribuicaoManual.workspace.find('tbody')[0]) {
				
				linhaVazia += '<tbody>';
			}
			
			linhaVazia += '<tr id="row'+ (distribuicaoManual.rowCount + 1) +'"><td align="left" abbr="numeroCota"><div style="text-align: left; width: 90px;">';
			linhaVazia += distribuicaoManual.inputNumeroCota.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '');
			
			linhaVazia += '</div></td><td align="left" abbr="nomeCota"><div style="text-align: left; width: 135px;">';
			linhaVazia += distribuicaoManual.inputNomeCota.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '');
			
			linhaVazia += '</div></td><td align="center" abbr="reparte"><div style="text-align: center; width: 65px;">';
			linhaVazia += distribuicaoManual.inputReparte.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '0');
			
			linhaVazia += '</div></td><td align="center" abbr="percEstoque"><div style="text-align: center; width: 80px;">';
			linhaVazia += distribuicaoManual.inputPercEstoque.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '0');
			
			linhaVazia += '</div></td></tr>';
			
			if (!distribuicaoManual.workspace.find('tbody')[0]) {
				
				linhaVazia += '</tbody>';
				
				distribuicaoManual.workspace.append(linhaVazia);
			} else {
				
				distribuicaoManual.workspace.find('tbody').append(linhaVazia);
			}
			
			this.configAutoComplete('#nomeCotaGrid'+ distribuicaoManual.rowCount, distribuicaoManual.rowCount);
				
			distribuicaoManual.focusRight("#reparteGrid"+distribuicaoManual.rowCount);

			distribuicaoManual.rowCount++;
		}
	},
	
	cotaJaExiste : function(numeroCota, index) {
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if (i != index) {
				var cota = $("#numeroCotaGrid"+ i, distribuicaoManual.workspace).val();
				if ((cota) && (cota == numeroCota)) {
					return true;
				}
			}
		}
		return false;
	},
	
	limparLinha : function(index) {
		$("#numeroCotaGrid"+ index, distribuicaoManual.workspace).val('');
		$("#nomeCotaGrid"+ index, distribuicaoManual.workspace).val('');
	},
	
	pesquisarCota : function(idObjetoCota, index) {
		
		var numeroCota = $(idObjetoCota).val().trim();
		if (!distribuicaoManual.cotaJaExiste(numeroCota, index)) {
			if(numeroCota.length == 0){
				$("#row"+ (index + 1), distribuicaoManual.workspace).remove();
				distribuicaoManual.conferirTotais();
	 			return;	
	 		}
	 		$.postJSON(contextPath + "/distribuicaoManual/consultarCotaPorNumero",
					{numeroCota : numeroCota},
					function(result){
						
						var callback = function() {
							$('#row'+ (index + 1), distribuicaoManual.workspace).append(
									distribuicaoManual.hiddenIdCota.replace(/#valor/g, result.idCota).replace(/#index/g, index));
							$('#row'+ (index + 1), distribuicaoManual.workspace).append(
									distribuicaoManual.hiddenStatusCota.replace(/#valor/g, result.status).replace(/#index/g, index));
							$('#nomeCotaGrid'+ index, distribuicaoManual.workspace).val(result.nomePessoa);
							$('#reparteGrid'+ index, distribuicaoManual.workspace).focus();
							distribuicaoManual.construirLinhaVazia();
							distribuicaoManual.exibindoMensagem = false;
							setTimeout(function() { $('#reparteGrid'+ index, distribuicaoManual.workspace).focus(); }, 100);
						};
						
						switch (result.status) {
							case 'SUSPENSO':
								if(distribuicaoManual.isSolicitarSenhaCotaSuspensa){

									var message = 'Cota SUSPENSA. Deseja incluir a cota? ';
									
									$('#row'+ (index + 1), distribuicaoManual.workspace).append(
											distribuicaoManual.hiddenIdCota.replace(/#valor/g, result.idCota).replace(/#index/g, index));
									$('#row'+ (index + 1), distribuicaoManual.workspace).append(
											distribuicaoManual.hiddenStatusCota.replace(/#valor/g, result.status).replace(/#index/g, index));
									$('#nomeCotaGrid'+ index, distribuicaoManual.workspace).val(result.nomePessoa);
									$('#reparteGrid'+ index, distribuicaoManual.workspace).focus();
									
									usuarioController.supervisor.verificarRoleSupervisao({
						            	optionalDialogMessage: message,
						            	callbacks: {
						    				usuarioSupervisorCallback: function() {
						    					
												distribuicaoManual.construirLinhaVazia();
												distribuicaoManual.exibindoMensagem = false;
												setTimeout(function() { $('#reparteGrid'+ index, distribuicaoManual.workspace).focus(); }, 100);
						    					
												exibirMensagemDialog('WARNING', ['A cota de número '+ numeroCota +' está com status SUSPENSO.'], '');
												
												distribuicaoManual.isSolicitarSenhaCotaSuspensa = false;
												
												var a = $("input[id^='numeroCotaGrid']").length;
												
												setTimeout(function() { 
													$("input[id^='numeroCotaGrid']", distribuicaoManual.workspace)[a-1].focus();
												}, 500);
												
						    				},
						    				usuarioNaoSupervisorCallback: function(){
												distribuicaoManual.isSolicitarSenhaCotaSuspensa = true;
												distribuicaoManual.limparLinha(index);
												distribuicaoManual.exibindoMensagem = false;
												
												var a = $("input[id^='numeroCotaGrid']").length;
												
												setTimeout(function() { 
													$("input[id^='numeroCotaGrid']", distribuicaoManual.workspace)[a-1].focus();
												}, 500);
												
							        		}
						    			}
						            });
									
								}else{
									
									exibirMensagemDialog('WARNING', ['A cota de número '+ numeroCota +' está com status SUSPENSO.'], '');
									
									$('#row'+ (index + 1), distribuicaoManual.workspace).append(
											distribuicaoManual.hiddenIdCota.replace(/#valor/g, result.idCota).replace(/#index/g, index));
									$('#row'+ (index + 1), distribuicaoManual.workspace).append(
											distribuicaoManual.hiddenStatusCota.replace(/#valor/g, result.status).replace(/#index/g, index));
									$('#nomeCotaGrid'+ index, distribuicaoManual.workspace).val(result.nomePessoa);
									$('#reparteGrid'+ index, distribuicaoManual.workspace).focus();
									distribuicaoManual.construirLinhaVazia();
									distribuicaoManual.exibindoMensagem = false;
									setTimeout(function() { $('#reparteGrid'+ index, distribuicaoManual.workspace).focus(); }, 100);
								}
								
							break;
							case 'INATIVO':
								distribuicaoManual.limparLinha(index);
								exibirMensagemDialog('WARNING', ['A cota de número '+ numeroCota +' está com status INATIVO.'], '');
							break;
							case 'PENDENTE':
								distribuicaoManual.limparLinha(index);
								exibirMensagemDialog('WARNING', ['A cota de número '+ numeroCota +' está com status PENDENTE.'], '');
							break;
							default:
								callback();
						}
	 				},
	 				function(result){
						//Verifica mensagens de erro do retorno da chamada ao controller.
						if (result.mensagens) {
							$('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val('');
							$('#numeroCotaGrid'+ index, distribuicaoManual.workspace).focus();
							exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "");
						}
					}, true, null
			);
		} else {
			$("#numeroCotaGrid"+ index, distribuicaoManual.workspace).val('');
			exibirMensagemDialog('WARNING', ['A cota de número '+ numeroCota +' já foi inserida anteriormente.'], '');
		}
	},
	
	configGrid : function () {
		distribuicaoManual.workspace.flexigrid({
			dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 90,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 135,
					sortable : true,
					align : 'left'
				}, {
					display : 'Reparte',
					name : 'reparte',
					width : 65,
					sortable : true,
					align : 'center'
				}, {
					display : '% do Estoque',
					name : 'percEstoque',
					width : 80,
					sortable : true,
					align : 'center'
				}],
				width : 447,
				height : 270
			});
	},

	checaSeRemoveu : function(idCaixaTexto, index) {
		if (!distribuicaoManual.exibindoMensagem) {
			if (($('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val()) &&
					(($(idCaixaTexto, distribuicaoManual.workspace).val() == null) ||
					($(idCaixaTexto, distribuicaoManual.workspace).val() == ''))) {
				$("#row"+ (index + 1), distribuicaoManual.workspace).remove();
				distribuicaoManual.conferirTotais();
			}
		}
	},
	
	conferirTotais : function() {
		var totalGeral = parseInt($("#reparteInicial").val());
		var totalDistribuido = distribuicaoManual.somarReparteDistribuido(-1);
		$('#distribuicao-manual-totalDistribuido').html(totalDistribuido);
		$('#distribuicao-manual-repDistribuir').html(totalGeral - totalDistribuido);
		
	},
	
	configAutoComplete : function(idCaixaTexto, index) {

		$(idCaixaTexto, distribuicaoManual.workspace).autocomplete({
			source : function(request, response) {
				$.ajax({
					url: contextPath + "/distribuicaoManual/consultarCotaPorNome",
					type: 'POST',
					dataType: "json",
					data: {nomeCota: request.term},
					success: function(result) {
						response(result.result);
					}});
			},
			select : function(event, ui) {
				
				var callback = function() {
					
					if (!distribuicaoManual.cotaJaExiste(ui.item.chave.numeroCota, index)) {
					
					$('#row'+ (index + 1), distribuicaoManual.workspace).append(
							distribuicaoManual.hiddenIdCota.replace(/#valor/g, ui.item.chave.idCota).replace(/#index/g, index));
					$('#row'+ (index + 1), distribuicaoManual.workspace).append(
							distribuicaoManual.hiddenStatusCota.replace(/#valor/g, ui.item.chave.status).replace(/#index/g, index));
					
					$('#nomeCotaGrid'+ index, distribuicaoManual.workspace).val(ui.item.chave.nomePessoa);
					
					$('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val(ui.item.chave.numeroCota);
					
					$('#reparteGrid'+ index, distribuicaoManual.workspace).focus();
					distribuicaoManual.construirLinhaVazia();
					distribuicaoManual.exibindoMensagem = false;
					setTimeout(function() { $('#reparteGrid'+ index, distribuicaoManual.workspace).focus(); }, 100);
					
					} else {
						$("#numeroCotaGrid"+ index, distribuicaoManual.workspace).val('');
						exibirMensagemDialog('WARNING', ['A cota de nome '+ ui.item.chave.nomePessoa +' já foi inserida anteriormente.'], '');
					}
					
				};
				
				//Validar
				
				switch (ui.item.chave.status) {
				
				case 'SUSPENSO':
					distribuicaoManual.exibindoMensagem = true;
					distribuicaoManual.confirmar('#dialog-status-suspenso', callback, function() {
						distribuicaoManual.limparLinha(index);
						distribuicaoManual.exibindoMensagem = false;
						setTimeout(function() { $('#numeroCotaGrid'+ index, distribuicaoManual.workspace).focus(); }, 100);
					});
				break;
				
				case 'INATIVO':
					distribuicaoManual.limparLinha(index);
					exibirMensagemDialog('WARNING', ['A cota de número '+ ui.item.chave.numeroCota +' está com status INATIVO.'], '');
				break;
				
				case 'PENDENTE':
					distribuicaoManual.limparLinha(index);
					exibirMensagemDialog('WARNING', ['A cota de número '+ ui.item.chave.numeroCota +' está com status PENDENTE.'], '');
				break;
				
				default:
					callback();
				}
				
			},
			focus: function( event, ui ) {
				$('#nomeCotaGrid'+ index, distribuicaoManual.workspace).val(ui.item.label);
			},
			minLength: 3,
			delay : 800,
		});
	},
	
	gerarEstudo : function() {
		var callbackFunction = function() {
			var data = [];
			data.push({name: 'estudoDTO.produtoEdicaoId', value: $('#idProdutoEdicao').val()});
			data.push({name: 'estudoDTO.reparteDistribuir', value: $('#reparteInicial').val()});
			data.push({name: 'estudoDTO.reparteDistribuido', value: $('#distribuicao-manual-totalDistribuido').text()});
			data.push({name: 'estudoDTO.dataLancamento', value: $('#distribuicao-manual-dataLancamento').html()});
			data.push({name: 'estudoDTO.lancamentoId', value: distribuicaoManual.idLancamento});
			data.push({name: 'estudoDTO.reparteTotal', value: distribuicaoManual.reparteTotal});
			
			for (var i = 0; i < distribuicaoManual.rowCount; i++) {
				if ($("#reparteGrid"+ i, distribuicaoManual.workspace).val() && ($("#reparteGrid"+ i, distribuicaoManual.workspace).val() > 0)) {
					data.push({name: "estudoCotasDTO["+ i +"].idCota", value: $("#idCotaGrid"+ i, distribuicaoManual.workspace).val()});
					data.push({name: "estudoCotasDTO["+ i +"].qtdeEfetiva", value: $("#reparteGrid"+ i, distribuicaoManual.workspace).val()});
				}
			}
			
			$.postJSON(contextPath +"/distribuicaoManual/gravarEstudo",
					data,
					function(result){
						$('#distribuicao-manual-estudo').html(result.long);
						distribuicaoManual.bloquearCampos();
						
						if(typeof(matrizDistribuicao)=="object"){
		            		matrizDistribuicao.carregarGrid();
						}
						
						exibirMensagemDialog("SUCCESS", ["Operação realizada com sucesso!"], "");
	 				},
	 				function(result){
						if (result.mensagens) {
							exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "");
						}
					}, true, null
			);
		};
		if (parseInt($('#distribuicao-manual-repDistribuir').html()) > 0) {
			distribuicaoManual.confirmar("#dialog-saldo", callbackFunction, function() {
				console.log(distribuicaoManual.rowCount);
				setTimeout(function() { $('#numeroCotaGrid'+ (distribuicaoManual.rowCount - 1), distribuicaoManual.workspace).focus(); }, 100);
			});
		} else {
			callbackFunction();
		}
	},
	
	bloquearCampos : function() {
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if ($("#reparteGrid"+ i, distribuicaoManual.workspace).val()) {
				$("#numeroCotaGrid"+ i, distribuicaoManual.workspace).attr("disabled", true).css("background-color", "#f0f0f0");
				$("#nomeCotaGrid"+ i, distribuicaoManual.workspace).attr("disabled", true).css("background-color", "#f0f0f0");
				$("#reparteGrid"+ i, distribuicaoManual.workspace).attr("disabled", true).css("background-color", "#f0f0f0");
			}
		}
		
		$('#distbManual_gerarEstudo').css("display", "none");
		
	},
	
	analisar : function() {
		//testa se registro selecionado possui estudo gerado
		if ($('#distribuicao-manual-estudo').html() == null || $('#distribuicao-manual-estudo').html() == "") {
			exibirMensagem("WARNING",["Gere o estudo antes de fazer a análise."]);
			return;
		} else {
			// Deve ir direto para EMS 2031
			matrizDistribuicao.redirectToTelaAnalise($('#distribuicao-manual-estudo').html());
		}
	},
	
	// ###-- Ajustes para SANTOS --##
	
	// Evento click do botao adicionar em lote		
	add_lote : function() {
		$("#modalUploadArquivo-DistbManual").dialog({
			resizable: false,
			height:300,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					var fileName = $("#excelFileDistbManual").val();
					var ext = fileName != ""? fileName.substr(fileName.lastIndexOf(".")+1).toLowerCase() : "";
				       
					if(ext!="xls" && ext!="xlsx"){
						exibirMensagem("WARNING", ["Somente arquivos com extensão .XLS ou .XLSX são permitidos."]);
					}else{
						distribuicaoManual.executarSubmitArquivo();
					}
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				},
			},
			form: $("#formUploadLoteDistbManual")
		});
	},
	
	executarSubmitArquivo:function(){
		
		$('#produtoEdicaoIdXLS').val($('#idProdutoEdicao').val());
		$('#reparteDistribuirXLS').val($('#reparteInicial').val());
		$('#reparteDistribuidoXLS').val($('#distribuicao-manual-totalDistribuido').text());
		$('#dataLancamentoXLS').val($('#distribuicao-manual-dataLancamento').html());
		$('#lancamentoIdXLS').val(distribuicaoManual.idLancamento);
		$('#reparteTotalXLS').val(distribuicaoManual.reparteTotal);
	  
		$("#formUploadLoteDistbManual").ajaxSubmit({
			
			success: function(responseText, statusText, xhr, $form)  { 

				var mensagens;
				
				if(responseText.mensagens == undefined && responseText.result == undefined){
					mensagens = responseText.long;
				}else{
					mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;
				}
				
	            var tipoMensagem = mensagens.tipoMensagem != undefined ? mensagens.tipoMensagem : 'SUCCESS';
		        var listaMensagens = mensagens.listaMensagens != undefined ? mensagens.listaMensagens : "Estudo gerado com sucesso";

		        if (tipoMensagem && listaMensagens) {
		        	
		        	if (tipoMensagem == 'SUCCESS') {

	        			exibirMensagem(tipoMensagem, [""+listaMensagens]); 
	        			
	        			$('#distribuicao-manual-estudo').html(responseText.long);
	        			$("#distbManual_gerarEstudo").hide();
	        			$("#distbManual_importacao").hide();
	        			$("#distbManual_cancelar").hide();
	        			$("#fieldDistribuicao-cotas").hide();
	        			
	        			if(typeof(matrizDistribuicao)=="object"){
	        				matrizDistribuicao.carregarGrid();
	        			}
	        				        			
	        		}else{
	        			exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialog-msg-upload');
	        		}
		        	
	                $("#modalUploadArquivo-DistbManual").dialog("close");
		        }
		   },
		   url:  contextPath +"/distribuicaoManual/uploadArquivoLoteDistbManual",				   
	       type: 'POST',
	       dataType: 'json',
 	   });
	}
	// ##-- --##
	
});

//@ sourceURL=distribuicaoManual.js