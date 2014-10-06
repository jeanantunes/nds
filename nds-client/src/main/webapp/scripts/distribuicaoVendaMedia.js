function DistribuicaoVendaMedia(pathTela, workspace) {
	
	var url = pathTela;
	var T = this;
	this.workspace = workspace;

	this.carregarEventos = function() {
	    if (!isNaN($('#reparteTotal').text()) && !isNaN($('#reparteDistribuir').val())) {
                $('#sobra').text(($('#reparteTotal').text()*1) - ($('#reparteDistribuir').val()*1));
            }
	    
	    $('#reparteDistribuir').blur(function() {
	        if (!isNaN($('#reparteTotal').text()) && !isNaN($('#reparteDistribuir').val())) {
	            $('#sobra').text(($('#reparteTotal').text()*1) - ($('#reparteDistribuir').val()*1));
	        }
	    });
	};
	
	this.removerDuplicados = function eliminateDuplicates(arr) {
		  var i,
	      len=arr.length,
	      out=[],
	      obj={};
	 
	  for (i=0;i<len;i++) {
	    obj[arr[i]]=0;
	  }
	  for (i in obj) {
	    out.push(i);
	  }
	  return out;
	},

	this.confirmarProdutosEdicaoBasePopup = function(){
	    var data = [];
	    
	    var qtdSelecionados = 0;
	    
	    if (typeof T.produtoEdicaoPesquisaBases !== 'undefined') {
	        $.each(T.produtoEdicaoPesquisaBases, function(index, item){
	            if(item.selecionado){
	                data.push({name :"indexes", value: index});
	                qtdSelecionados++;
	            }
	        });
	        T.produtoEdicaoPesquisaBases = [];
	    }
	    
	    if (qtdSelecionados > 6) {
	    	 exibirMensagem("WARNING", ["Não pode ter mais do que 6 bases selecionadas."]);
	    	 return;
	    }

	    $.postJSON(
	            pathTela + "/distribuicaoVendaMedia/adicionarProdutoEdicaoABase", 
	            data,
	            function(result) {
	                T.produtoEdicaoBases = result;
	                T.preencherGridBases(result);
	            },
	            function(){
	                exibirMensagem("ERROR", ["Erro ao processar a pesquisa. Tente novamente mais tarde."]);
	            }
	    );
	    
	    $.postJSON(
	            pathTela + "/distribuicaoVendaMedia/existeBaseVeraneio", 
	            null,
	            function(result) {
	            	if(result && result.boolean != undefined && !result.boolean) {
	            		
	            		exibirMensagem("WARNING", ["Não foram encontradas bases de veraneio."]);
	            	}
	            },
	            function(){
	                exibirMensagem("ERROR", ["Erro ao processar a pesquisa. Tente novamente mais tarde."]);
	            }
	    );
	    
	};
	
	this.preencherGridBases = function(resultado){
		$.each(resultado, function(index,row){ T.processarLinhaBases(index, row);});
		
		$(".dadosBasesGrid").flexAddData({
			rows : toFlexiGridObject(resultado),
			page : 1,
			total : 1
		});
	};
	
	this.processarLinhaBases = function(index, row){
		row.pesoInput = '<select name="select'+index+'" id="select'+index+'" onchange="distribuicaoVendaMedia.selecionarPesoProduto('+index+', this)" ><option value="1" '+ (row.indicePeso && row.indicePeso == 1 ? "selected":"") +' >1</option><option value="2" '+ (row.indicePeso && row.indicePeso == 2 ? "selected":"") +' >2</option><option value="3" '+ (row.indicePeso && row.indicePeso == 3 ? "selected":"") +' >3</option></select>';
		row.peso = 1;
		row.select = '<input onclick="distribuicaoVendaMedia.selecionarProdutoBase(' + index + ', this)" type="checkbox" value=""/>';
		
		if(row.periodo == undefined){
			row.periodo = '';
		}
		if(row.status == undefined){
			row.status = '';
		}
		if(row.reparte == undefined){
			row.reparte = '';
		}
		if(row.venda == undefined){
			row.venda = '';
		}
		
		if ((row.reparte && row.venda) && (row.reparte != 0 && row.venda != 0)) {
			row.percentualVenda = floatToPrice(row.venda * 100 / row.reparte); 
		} else {
			
			row.percentualVenda = '';
		}
		
	};
	
	this.selecionarPesoProduto = function(index, select){
		T.produtoEdicaoBases[index].peso = select.value;
	};
	
	this.processarLinhaPesquisaBases = function(index, row){
        row.id = '<input type="hidden" id="produtoEdicaoId_'+ index +'" value="'+ row.id +'"/>';
		row.capa = '<a onmouseover="distribuicaoVendaMedia.popup_detalhes(\''+row.codigoProduto+'\', '+row.numeroEdicao+');" onmouseout="popup_detalhes_close();" href="javascript:;"><img src="'+ pathTela +'/images/ico_detalhes.png" border="0"/></a>';
		row.select = '<input onclick="distribuicaoVendaMedia.selecionarProdutoBasePopUp(' + index + ', this)" type="checkbox" value=""/>';
		if(row.periodo == undefined){
			row.periodo = '';
		}
		if(row.reparte == undefined){
			row.reparte = '';
		}
		if(row.venda == undefined){
			row.venda = '';
		}
		if(row.percentualVenda == undefined){
			row.percentualVenda = '';
		}else {
			row.percentualVenda = floatToPrice(row.percentualVenda);
		}
		if(row.statusSituacao == undefined){
			row.statusSituacao = '';
		}
	};
	
	this.selecionarProdutoBasePopUp = function(index, checkbox){
		T.produtoEdicaoPesquisaBases[index].selecionado = checkbox.checked;
	};
	
	this.selecionarProdutoBase = function(index, checkbox){
		T.produtoEdicaoBases[index].selecionado = checkbox.checked;
	};
	
	this.removerTodasEdicoesDeBase = function() {
	    $.postJSON(
	            pathTela + "/distribuicaoVendaMedia/removerTodasEdicoesDeBase", 
	            [],
	            function(result) {
	                T.produtoEdicaoBases = result;
	                T.preencherGridBases(result);
	                if(T.produtoEdicaoBases != undefined && T.produtoEdicaoBases.length === 0){
	                    $('#qtdeBancas').hide();
	                }
	            },
	            function(){
	                exibirMensagem("ERROR", ["Erro ao excluir itens da lista. Tente novamente mais tarde."]);
	            }
	    );
	};
	
	this.removerProdutoEdicaoDaBase = function(){
		var data = [];
		$.each(T.produtoEdicaoBases, function(index, item){
			if(item.selecionado){
				data.push({name :"indexes", value: index});
			}
		});
		
		$.postJSON(
				pathTela + "/distribuicaoVendaMedia/removerProdutoEdicaoDaBase", 
				data,
				function(result) {
					T.produtoEdicaoBases = result;
					T.preencherGridBases(result);
					if(T.produtoEdicaoBases != undefined && T.produtoEdicaoBases.length === 0){
						$('#qtdeBancas').hide();
					}
				},
				function(){
					exibirMensagem("ERROR", ["Erro ao excluir itens da lista. Tente novamente mais tarde."]);
				}
			);
	};

	this.preencherGridBasesPesquisa = function(resultado){
		$.each(resultado, function(index,row){ 
			T.processarLinhaPesquisaBases(index, row);
		});
		
		var rows = [];
		
		for ( var e in resultado) {
			if(!isNaN(parseInt(e))){
				var id = parseInt(e); 
				rows.push({"id" : ++id,	"cell" : resultado[e]});
			}
		}
		
		var dtoTratado = {
			rows : rows,
			page : 1,
			total : 1
		};
		
		return dtoTratado;
	};
	
	this.pesquisarBases = function(){

		var data = [];
		var codigo = $("#codigoPesquisaBases").val();
		var produto = $("#produtoPesquisaBases").val();
		var edicao = $("#edicaoPesquisaBases").val();
		var classificacao = $("#selectClassificacao").val();
		var idLancamento = $("#idLancamento").val();
		var modoAnalise = $("#modoAnalise").val();
		var idProdutoEdicao = $("#idProdutoEdicao").val();
		
		data.push({name:"filtro.codigo", value:codigo});
		data.push({name:"filtro.nome", value:produto});
		data.push({name:"filtro.edicao", value:edicao});
		data.push({name:"filtro.classificacao", value:classificacao});
		data.push({name:"filtro.idLancamento", value:idLancamento});
		data.push({name:"modoAnalise", value:modoAnalise});
		data.push({name:"idProdutoEdicao", value:idProdutoEdicao});
		
		$("#edicaoProdCadastradosGrid-1", this.workspace).flexOptions({
			url: url + "/distribuicaoVendaMedia/pesquisarProdutosEdicao",
			params: data,
			preProcess: function(result){
				T.produtoEdicaoPesquisaBases = result;
				var dtoTratado = T.preencherGridBasesPesquisa(result);
				return dtoTratado;
			},
			onError: function(){
				exibirMensagem("ERROR", ["Erro ao processar a pesquisa. Tente novamente mais tarde."]);
			}
		}).flexReload();
		
	};
	
	this.popup_detalhes = function(codigoProduto,numeroEdicao) {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		//histogramaVendasController.getCapaEdicao(codigo,edicao);
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false,
			escondeHeader: false,
			position: { my: "left", at: "right", of: event.target },
			open : function(event, ui) {
				
				$("#imagemCapaEdicao").one('load', function() {
						$("#imagemCapaEdicao").show();
						$("#loadingCapa").hide();
					}).each(function() {
					  if(this.complete) $(this).load();
					});
				
				var randomnumber=Math.floor(Math.random()*11);
				
				$("#imagemCapaEdicao")
						.attr("src",contextPath
										+ "/capa/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
										+ codigoProduto
										+ "&numeroEdicao="
										+ numeroEdicao);
			},
			close:function(event, ui){
				$("#imagemCapaEdicao").removeAttr("src").hide();
				$("#loadingCapa").show();
				
			}
		});
	};
	
	this.selectElementoRegiaoDistribuicao = function(select, elementoSelect, callback){
		var selectedItem = $("#" + select).val();
		
		var target = null;
		if(typeof elementoSelect == "string"){
			target = $("#" + elementoSelect, T._workspace);
		}else if(typeof elementoSelect == "object"){
			target = elementoSelect;
		}
		
		if(selectedItem != 'Selecione...'){
			carregarCombo(pathTela + "/distribuicao/historicoVenda/carregarElementos", 
				  {"componente":selectedItem},
				  target, null, null, callback);
		} else {
			$('#' + elementoSelect).html('');
			$('#' + elementoSelect).append("<option value='-1'>Selecione...</option>");
		   
		}
	};
	
	this.checkComponenteBonificacao = function(value, descricao, enumValue){
		if(T.componenteBonificacaoSelecionado != null){
			$("#componenteBonificacao" + T.componenteBonificacaoSelecionado.value).removeAttr("checked");
		}
		$("#componenteBonificacao" + value).attr("checked", "checked");
		T.componenteBonificacaoSelecionado = {descricao : descricao, value : value, enumValue : enumValue};
	};
	
	this.selectComponenteBonificacao = function(value, descricao, enumValue){
		
		if($("#componenteBonificacao"+value).is(":checked ")==false){
		
			T.checkComponenteBonificacao(value, descricao, enumValue);
			T.loadBonificacoesGrid(value);
		}else{
			T.unloadBonificacoesGrid();
			$("#componenteBonificacao" + value).removeAttr("checked");
		}
		
		
	};

	this.unloadBonificacoesGrid=function(){
		$("#bonificacoesGrid").flexAddData({
			rows : [],
			page : 1,
			total : 0
		});
		
	};
	
	this.loadBonificacoesGrid = function(value,descricao,enumValue){
		var selectedItem = value;
		$.postJSON(
				url + "/distribuicao/historicoVenda/carregarElementos", 
				{"componente":selectedItem},
				function(result) {
					T.elementosBonificacao = result;
					$.each(result, function(index,row){ T.processarLinhaElemento(index, row);});
					$("#bonificacoesGrid").flexAddData({
						rows : toFlexiGridObject(result),
						page : 1,
						total : 1
					});
				},
				
				function(){
					exibirMensagem("ERROR", ["Erro ao processar a pesquisa. Tente novamente mais tarde."]);
				}
		);
		
		if(descricao && enumValue){
			
			if(T.componenteBonificacaoSelecionado != null){
				$("#componenteBonificacao" + T.componenteBonificacaoSelecionado.value).removeAttr("checked");
			}
			
			T.componenteBonificacaoSelecionado = {descricao : descricao, value : value, enumValue : enumValue};
		}
		
		
	};
	
	this.selecionarElementoBonificacao = function(index, checkbox){
		var count = 0;
		T.elementosBonificacao[index].selecionado = checkbox.checked;
		T.elementosBonificacao[index].checkBox = checkbox;
		
		T.elementosBonificacao.forEach(function(element){
			if (element.selecionado) 
				count++;
		});
		
		if (count > 3) {
			exibirMensagemDialog("WARNING", ["Não é possível adicionar mais que 3 elementos"], "");
			checkbox.checked = false;
			
			T.elementosBonificacao[index].selecionado = checkbox.checked;
			T.elementosBonificacao[index].checkBox = checkbox;
		}
	};
	
	this.processarLinhaElemento = function(index, row){
		row.descricao = row.value.$;
		row.acao = '<input onclick="distribuicaoVendaMedia.selecionarElementoBonificacao(' + index + ', this)" type="checkbox" value="" class="bonificacaoElementoInput"/>';
	};
	
	this.alterarBonificacao = function(input, index){
		T.bonificacaoSelecionados[index].percBonificacao = input.value;
	};
	
	this.alterarReparteMinimo = function(input, index){
		T.bonificacaoSelecionados[index].reparteMinimo = input.value;
	};
	
	this.alterarTodasAsCotas = function(input, index){
		T.bonificacaoSelecionados[index].todasAsCotas = input.checked;
	};
	
	this.confirmarSelecaoBonificacao = function(){
	    if (typeof T.bonificacaoSelecionados === 'undefined'){
	        T.bonificacaoSelecionados = [];
	    }
	    var bonificacoes = [];
	    for (var i = 0; i < T.bonificacaoSelecionados.length; i++) {
	        bonificacoes.push(T.bonificacaoSelecionados[i]);
	    }

	    for (var i = 0; i < T.elementosBonificacao.length; i++){
	        var elemento = T.elementosBonificacao[i];
	        if (elemento.selecionado) {
	            if (!T.containsBonificacao(elemento)) {
	                var bonificacao = {
	                        index: bonificacoes.length,
	                        componenteDesc: T.componenteBonificacaoSelecionado.descricao,
	                        componente: T.componenteBonificacaoSelecionado,
	                        elementoDesc: elemento.descricao,
	                        elemento: elemento,
	                        percBonificacaoInput: '<input id="percBonificacao-'+ bonificacoes.length +'" style="width: 40px; text-align: right;" onchange="distribuicaoVendaMedia.alterarBonificacao(this, '+ bonificacoes.length +')"/>',
	                        percBonificacao: '',
	                        reparteMinimoInput: '<input id="reparteMinimo-'+ bonificacoes.length +'" style="width: 40px; text-align: right;" onchange="distribuicaoVendaMedia.alterarReparteMinimo(this, '+bonificacoes.length+')"/>',
	                        reparteMinimo: '',
	                        sel: '<input id="percBonificacao-'+ bonificacoes.length +'" type="checkbox" onchange="distribuicaoVendaMedia.alterarTodasAsCotas(this, '+ bonificacoes.length+')" />',
	                        todasAsCotas: false,
	                        acao: '<a onclick="popup_excluir_bonificacao('+ bonificacoes.length +');" href="javascript:;"><img src="images/ico_excluir.gif" border="0"/></a>',
	                };
	                bonificacoes.push(bonificacao);
	            }
	        }
	    }

	    var iguais = 0,
	    diferentes = 0,
	    componentesUsados = [];

	    bonificacoes.forEach(function(novaBonificacao){
	        componentesUsados.push(novaBonificacao.componente.enumValue);
	    });

	    for (var i = 0; i < bonificacoes.length; i++) {
	        iguais = 0;
	        bonificacoes.forEach(function(bonificacao) {
	            if (bonificacoes[i].componente.enumValue === bonificacao.componente.enumValue) {
	                iguais++;
	            }
	        });

	        if (iguais > 3) {
	            break;
	        }
	    }

	    componentesUsados = T.removerDuplicados(componentesUsados);
	    componentesUsados.forEach(function(componente1) {
	        diferentes = 0;
	        componentesUsados.forEach(function(componente2) {
	            if (componente1 !== componente2) {
	                diferentes++;
	            }
	        });
	    });

	    if (iguais > 3) {
	        exibirMensagemDialog("WARNING", ["Não foi possível inserir esse(s) iten(s). Um componente não pode ter mais que 3 elementos."], "");
	        return;
	    } else if ((diferentes + 1) > 3) {
	        exibirMensagemDialog("WARNING", ["Não é possível adicionar mais que 3 Componentes."], "");
	        return;
	    } else {
	        T.bonificacaoSelecionados = bonificacoes;
	    }

	    T.preencherObjetoBonificacoes();
	    $("#elemento1Grid").flexAddData({
	        rows : toFlexiGridObject(T.bonificacaoSelecionados),
	        page : 1,
	        total : 1
	    });
	    T.preencherTelaBonificacoes();
	    T.bonificacaoSelecionados.forEach(function(element) { 
	        if (element.elemento.checkBox.checked) {
	            element.elemento.checkBox.checked = false;
	            element.elemento.selecionado = false;
	        }
	    });
	};
	
	this.preencherObjetoBonificacoes = function() {
	    $('#elemento1Grid tr').each(function(i, value) {
	        var inp = $(this).closest("tr").find("input");
	        distribuicaoVendaMedia.bonificacaoSelecionados[i].percBonificacao = inp.eq(0).val();
	        distribuicaoVendaMedia.bonificacaoSelecionados[i].reparteMinimo = inp.eq(1).val();
	        distribuicaoVendaMedia.bonificacaoSelecionados[i].todasAsCotas = inp.eq(2).is(":checked");
	    });
	};

	this.preencherTelaBonificacoes = function() {
	    for (var i = 0; i < distribuicaoVendaMedia.bonificacaoSelecionados.length; i++) {
	        var linha = $('#elemento1Grid tr').eq(i);
	        
	        if (typeof distribuicaoVendaMedia.bonificacaoSelecionados[i] !== 'undefined') {
	            linha.find('input:eq(0)').val(distribuicaoVendaMedia.bonificacaoSelecionados[i].percBonificacao);
	            linha.find('input:eq(1)').val(distribuicaoVendaMedia.bonificacaoSelecionados[i].reparteMinimo);
	            linha.find('input:eq(2)').prop('checked', distribuicaoVendaMedia.bonificacaoSelecionados[i].todasAsCotas);
	        }
	    }
	};
	
	this.removerBonificacao = function(index){
	    if (T.bonificacaoSelecionados.length === 1) {
	        T.bonificacaoSelecionados.shift();
	    } else {
	        T.bonificacaoSelecionados.splice(index, 1);
	    }

	    $("#elemento1Grid").flexAddData({
	        rows : toFlexiGridObject(T.bonificacaoSelecionados),
	        page : 1,
	        total : 1
	    });
	    T.preencherTelaBonificacoes();
	};
	
	this.containsBonificacao = function(elemento){
		for(var i = 0; i < T.bonificacaoSelecionados.length; i++){
			var bonificacao = T.bonificacaoSelecionados[i];

			if(bonificacao.componente.value == T.componenteBonificacaoSelecionado.value
					&& bonificacao.elemento.key.$ == elemento.key.$){
				return true;
			}
		}
		return false;
	};
	
	this.errorCallBack = function(){
		
	};
	
	this.verificacoesGerar = function(){
		if($('#reparteDistribuir').val() <= 0){
			exibirMensagemDialog("WARNING", ["Não é possível gerar estudo com reparte menor que 01."], "");
		}else{
			if(($("#distribuicaoPorMultiplo").is(":checked")) && ((($('#reparteDistribuir').val()) % ($('#multiplo').val())) > 0 )){
				exibirMensagemDialog("WARNING", ["O reparte tem que ser multiplo de " + $('#multiplo').val() + "."], "");
			}else{
				if(T.produtoEdicaoBases != undefined && T.produtoEdicaoBases.length === 1){
					distribuicaoVendaMedia.alertaUmaEdicaoBase();
				}else{
					distribuicaoVendaMedia.gerar();
				}
			}
		}
	};
		
	this.alertaUmaEdicaoBase = function(){
		$( "#dialog-edicoesbase" ).dialog({
		    escondeHeader: false,
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					distribuicaoVendaMedia.gerar();
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	this.gerar = function(){
		var data = [];
		
		data.push({name : "distribuicaoVendaMedia.reparteDistribuir", value : $("#reparteDistribuir").val() });
		data.push({name : "distribuicaoVendaMedia.reparteMinimo", value : $("#reparteMinimo").val() });
		data.push({name : "distribuicaoVendaMedia.usarFixacao", value : $("#usarFixacao")[0].checked });
		data.push({name : "distribuicaoVendaMedia.distribuicaoPorMultiplo", value : $("#distribuicaoPorMultiplo")[0].checked });
		data.push({name : "distribuicaoVendaMedia.multiplo", value : $("#multiplo").val() });
		data.push({name : "distribuicaoVendaMedia.reparteTotal", value : $("#reparteTotal").text() });
		
		
		if(T.produtoEdicaoBases != undefined){
			for(var i = 0; i < T.produtoEdicaoBases.length; i++){
				var produtoEdicao = T.produtoEdicaoBases[i];
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].id", value : produtoEdicao.id});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].numeroEdicao", value : produtoEdicao.numeroEdicao});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].idProduto", value : produtoEdicao.idProduto});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].codigoProduto", value : produtoEdicao.codigoProduto});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].periodo", value : produtoEdicao.periodo});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].parcial", value : produtoEdicao.parcial});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].dataLancamento", value : produtoEdicao.dataLancamentoFormatada});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].peso", value : produtoEdicao.peso});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].status", value : produtoEdicao.status});
				data.push({name: "distribuicaoVendaMedia.bases["+i+"].parcialConsolidado", value : produtoEdicao.isParcialConsolidado});
				
			}
		}
		
		if(T.bonificacaoSelecionados != undefined) {
			
            var doNotStringify = {
                acao: true,
                percBonificacaoInput: true,
                reparteMinimoInput: true,
                sel: true,
                todasAsCotas: true,
                checkBox: true
            };

            data.push({name: "distribuicaoVendaMedia.bonificacoesVO", value: JSON.stringify(T.bonificacaoSelecionados, function(k,v){if(doNotStringify.hasOwnProperty(k))return undefined; return v;}) });

            for(var i = 0; i < T.bonificacaoSelecionados.length; i++){
				data.push({name: "distribuicaoVendaMedia.bonificacoes["+i+"].componente", value : T.bonificacaoSelecionados[i].componente.enumValue});
				data.push({name: "distribuicaoVendaMedia.bonificacoes["+i+"].elemento", value : T.bonificacaoSelecionados[i].elemento.key.$});
				data.push({name: "distribuicaoVendaMedia.bonificacoes["+i+"].bonificacao", value : T.bonificacaoSelecionados[i].percBonificacao});
				data.push({name: "distribuicaoVendaMedia.bonificacoes["+i+"].reparteMinimo", value : T.bonificacaoSelecionados[i].reparteMinimo});
				data.push({name: "distribuicaoVendaMedia.bonificacoes["+i+"].todasAsCotas", value : T.bonificacaoSelecionados[i].todasAsCotas});
			}
		}
		
		data.push({name : "distribuicaoVendaMedia.todasAsCotas", value : $("#RDtodasAsCotas")[0].checked });
		if($("#RDcomponente")[0].checked){
			
			var v = new Array();
			var validate = true;
			$("select[name=elementoRegiaoDistribuicao]").each(function(idx,sel){
				
				if($(sel).val()!=-1 && v.indexOf($(sel).val())==-1){
					v.push($(sel).val());
				}else if($(sel).val()!=-1 && v.indexOf($(sel).val())>-1){
					validate = false;
					return;
				}
				
				
			});
			
			if(validate==false){
				exibirMensagemDialog("WARNING", ["Favor selecionar diferentes elementos em:","Região Distribuição > Componentes"], "");
				return;
			}
			
			data.push({name : "distribuicaoVendaMedia.componente", value : $("#componenteRegiaoDistribuicao").val() });
			data.push({name : "distribuicaoVendaMedia.elemento", value : $("#elementoRegiaoDistribuicao").val() });
			data.push({name : "distribuicaoVendaMedia.elemento2", value : $("select[name=elementoRegiaoDistribuicao]:eq(1)").val() });
			data.push({name : "distribuicaoVendaMedia.elemento3", value : $("select[name=elementoRegiaoDistribuicao]:eq(2)").val() });
		}
		if($("#RDAbrangencia")[0].checked){
			data.push({name : "distribuicaoVendaMedia.abrangenciaCriterio", value : $("#RDabrangenciaCriterio").val() });
			data.push({name : "distribuicaoVendaMedia.abrangencia", value : $("#RDabrangencia").val() });
		}
		
		if($("#RDroteiroEntrega")[0].checked){
			data.push({name : "distribuicaoVendaMedia.roteiroEntregaId", value : $("#selRoteiro").val() });
		}
		data.push({name : "distribuicaoVendaMedia.complementarAutomatico", value : $("#complementarAutomatico")[0].checked });
		data.push({name : "distribuicaoVendaMedia.cotasAVista", value : $("#cotasAVista")[0].checked });
		if($("#RDExcecaoBancas")[0].checked){
			data.push({name : "distribuicaoVendaMedia.excecaoDeBancasComponente", value : $("#componenteInformacoesComplementares").val() });
			
			var excecao1 = $("#elementoInformacoesComplementares1").val(),
				excecao2 = $("#elementoInformacoesComplementares2").val(),
				excecao3 = $("#elementoInformacoesComplementares3").val();
			
			if (excecao1 === excecao2 || (excecao3 != -1 && excecao1 === excecao3) || (excecao2 != -1 && excecao2 === excecao3)) {
				exibirMensagemDialog("WARNING", ["Favor selecionar diferentes elementos."], "");
				return;
			}
			
			data.push({name : "distribuicaoVendaMedia.excecaoDeBancas[0]", value : excecao1 });
			data.push({name : "distribuicaoVendaMedia.excecaoDeBancas[1]", value : excecao2 });
			data.push({name : "distribuicaoVendaMedia.excecaoDeBancas[2]", value : excecao3 });
		}
		
		
		var codProduto = $('#codigoProduto',this.workspace).val();
		
		if(codProduto == ""){
			codProduto = $('#codigoProduto',this.workspace).text();
		}
		
		numEdicao = $('#numeroEdicao',this.workspace).val();
		
		if(numEdicao == ""){
			var numEdicao = $('#numeroEdicao', this.workspace).html();
		}
		
		data.push({name : "codigoProduto", value : codProduto});
		data.push({name : "numeroEdicao", value : numEdicao});
		data.push({name : "idLancamento", value : $('#idLancamento',this.workspace).val()});
		data.push({name : "dataLancamento", value: $('#dataLancamento',this.workspace).html()});
        //TODO adicionar numero periodo caso o idLancamento nao s

		$.postJSON(pathTela + "/distribuicaoVendaMedia/gerarEstudo", data, function(result) {
		    //usado para exibir as variaveis do estudo
            
			myWindow = window.open('', '_blank');
			
			if(myWindow && myWindow.document) {
				myWindow.document.write(result.list[0]);
				myWindow.focus();
			} else {
				exibirMensagem("WARNING", ["Ajuste as configurações de popup no browser."]);
			}
            
            var isLiberado = result.list[2];
            	
            $('#idEstudo').text(result.list[1]);
            $('#idStatusEstudo').text(isLiberado === true ? "Liberado" : "Gerado");
            
            	
            if(typeof(matrizDistribuicao)=="object"){
            		matrizDistribuicao.carregarGrid();
            }
            
            T.produtoEdicaoBases = [];
            T.bonificacaoSelecionados = [];

			exibirMensagemDialog("SUCCESS", ["Operação realizada com sucesso!"], "");
		
		});
	};
	
	this.cancelar = function(){

		$(".ui-tabs-selected").find("span[class*='ui-icon-close']").click();
		T.produtoEdicaoBases = [];
		selectTabTitle('Matriz Distribuição');
	};
	
	this.redirectToTelaAnalise = function(){

        // Obter matriz de distribuição
        var matriz = [],
        	url = contextPath + "/distribuicao/analiseEstudo/obterMatrizDistribuicaoPorEstudo",
        	dadosResumo = {},
        	numeroEstudo = $('#idEstudo', this.workspace).text();
        
        $.postJSON(url,
                [{name : "id" , value : numeroEstudo}],
                function(response){
	            // CALLBACK
	            // ONSUCESS
	            matriz.push({name: "selecionado.classificacao",  value: response.classificacao});
	            matriz.push({name: "selecionado.nomeProduto",    value: response.nomeProduto});
	            matriz.push({name: "selecionado.codigoProduto",  value: response.codigoProduto});
	            matriz.push({name: "selecionado.dataLcto",       value: response.dataLancto});
	            matriz.push({name: "selecionado.edicao",         value: response.numeroEdicao});
	            matriz.push({name: "selecionado.estudo",         value: response.idEstudo});
	            matriz.push({name: "selecionado.idLancamento",   value: response.idLancamento});
	            matriz.push({name: "selecionado.estudoLiberado", value: (response.liberado != "")});
	            
	            $('#workspace .ui-tabs-nav li a').each(function(k, v){ 
					if($(v).text() == 'Histograma Pré Análise') {
						console.log(k +' - '+ $(v).text());
						$("#workspace").tabs('option', 'selected', k); 
						$("#workspace").tabs('load', k); 
					} 
				});
	            
	            $('#workspace').tabs({load : function(event, ui) {
					
	            	histogramaPosEstudoController.dadosResumo = dadosResumo;
	            	histogramaPosEstudoController.matrizSelecionado = matriz;
	            	histogramaPosEstudoController.popularFieldsetHistogramaPreAnalise(matriz);

					$('#workspace').tabs({load : function(event, ui) {}});
				}});

                var parametros = '?codigoProduto='+ response.codigoProduto +'&edicao='+ response.numeroEdicao;
				$('#workspace').tabs('addTab', 'Histograma Pré Análise', contextPath + '/matrizDistribuicao/histogramaPosEstudo' + parametros);
        	}
        );
    };

    this.recuperarEstadoDaTela = function(vendaMediaDTO) {
        var componentePDV =  {
            AREA_DE_INFLUENCIA: 0, BAIRRO: 1, COTAS_A_VISTA: 2, COTAS_NOVAS_RETIVADAS: 3, DISTRITO: 4, GERADOR_DE_FLUXO: 5, REGIAO: 6, TIPO_PONTO_DE_VENDA: 7
        };
        var temDicionarioElemento = [0, 2, 3, 5, 6, 7];
        var elementoPDV = {
            //area de influencia
            "AREA COMERCIAL A/B": 1, "AREA COMERCIAL C/D": 2, "COMERCIAL": 3, "CONVENIENCIA VIAGEM": 4, "ESCRITORIO A/B": 5, "ESCRITORIO E FABRICA C/D": 6, "RESIDENCIAL A/B": 7, "RESIDENCIAL C/D": 8,
            //cota a vista
            "Consignado": "CONSIGNADO", "Cotas à Vista": "A_VISTA",
            //cotas novas / reativadas
            "Sim": 1, "Não": 0,
            //gerador de fluxo
            "Academia de Ginastica/Esportes": 46, "Aeroporto": 39, "Agencia de Turismo": 38, "Banco": 40, "Bazares": 49, "Biblioteca": 29, "Casas, Aptos e Condominios": 61, "Centro Comercial": 20, "Centro de Convenções": 36, "Clinica Veterinaria": 57, "Clube": 30, "Corredores de Transito": 15, "Cursinho": 4, "Curso de Informatica": 7, "Curso de Lingua": 6, "Empresa/Industria/Escritorio": 44, "Escola 1 e 2 Grau Particular": 2, "Escola 1 e 2 Grau Publica": 3, "Escola de Arte": 8, "Escola de Musica": 60, "Estação de Metro": 10, "Estadio": 28, "Faculdades/Universidades": 9, "Farmacias e Drogarias": 51, "Feira Livre": 23, "Ferroviaria": 11, "Hospital": 53, "Hotel": 37, "Igreja": 43, "Lab. Clinicos/Consultorios": 52, "Lanchonete e Bares": 17, "Livraria": 31, "Loja de Armarinhos": 50, "Loja de Convivencia": 22, "Loja de Decoração": 47, "Loja de Disco": 58, "Loja de Instrumentos Musicais": 59, "Loja de Material de Construção": 48, "Loja de Material Esportivo": 45, "Loja de Plantas/Floriculturas": 55, "Loja p/ Materias de Jardinagem": 54, "Lojas de Animais": 56, "Mercado Municipal": 24, "Motel": 35, "Padarias": 16, "Parques": 26, "Pontos de Onibus": 13, "Posto de Gasolina": 42, "Praia": 27, "Pre-Escola": 1, "Repartição Publica": 41, "Restaurantes": 18, "Rodoviaria": 12, "Shopping Center": 19, "Superior": 5, "Supermercado e Hipermercado": 21, "Teatro": 32, "Terminais de Onibus": 14, "Varejão/Sacola": 25,
            //regiao
            "abc": 19, "Adicionar em lote": 10, "Automatica": 3, "beaba": 17, "Carlao": 15, "Hortolandia 2": 11, "Merge": 2, "RegiaoTeste": 1, "Regressiva": 16, "Rodrigo Teste": 4, "Teste": 5, "Teste 3333": 9, "teste Bonificacao 24.02": 18, "Teste do Merge": 13, "Testess": 20,
            //tipo ponto de venda
            "BANCA": 1, "BAZAR/PAPELARIA": 13, "COFFEE-SHOP": 31, "ESCOLA DE ARTE": 39, "HOSPITAL": 26, "LIVRARIA": 27, "LOJA DE CONVENIENCIA": 23, "OUTROS": 4, "PADARIA/BAR": 19, "POSTO DE GASOLINA": 12, "QUIOSQUE": 3, "REVISTARIA": 2, "TABACARIA": 14
        };
        var reconstroiBonificacao = function(bonificacao) {
            bonificacao.percBonificacaoInput = '<input id="percBonificacao-'+ bonificacao.index +'" style="width: 40px; text-align: right;" onchange="distribuicaoVendaMedia.alterarBonificacao(this, '+ bonificacao.index +')"/>';
            bonificacao.reparteMinimoInput =  '<input id="reparteMinimo-'+ bonificacao.index +'" style="width: 40px; text-align: right;" onchange="distribuicaoVendaMedia.alterarReparteMinimo(this, '+bonificacao.index+')"/>';
            bonificacao.sel =  '<input id="percBonificacao-'+ bonificacao.index +'" type="checkbox" onchange="distribuicaoVendaMedia.alterarTodasAsCotas(this, '+ bonificacao.index+')" />';
            bonificacao.acao =  '<a onclick="popup_excluir_bonificacao('+ bonificacao.index +');" href="javascript:;"><img src="images/ico_excluir.gif" border="0"/></a>';

            return bonificacao;
        };

        window.tmp = vendaMediaDTO;

        if (vendaMediaDTO) {
            $("#reparteDistribuir").val(vendaMediaDTO.reparteDistribuir);
            $("#reparteMinimo").val(vendaMediaDTO.reparteMinimo);
            $("#usarFixacao").prop('checked', vendaMediaDTO.usarFixacao);

            if (vendaMediaDTO.distribuicaoPorMultiplo) {
                $("#distribuicaoPorMultiplo").click();
                $("#multiplo").val(vendaMediaDTO.multiplo);
            }

            if (vendaMediaDTO.bases.length != 0) {
                T.preencherGridBases($.extend(true, [], vendaMediaDTO.bases));
                $(document).one("ajaxStop", function() {
                    $('.dadosBasesGrid select').each(function(k){$(this).val(vendaMediaDTO.bases[k].peso)});
                });
            }

            if (vendaMediaDTO.bonificacoes.length != 0) {
                T.bonificacaoSelecionados = $.extend(true, [], vendaMediaDTO.bonificacoesVO);
                $(document).one("ajaxStop", function() {
                    $("#elemento1Grid").flexAddData({
                        rows : toFlexiGridObject(T.bonificacaoSelecionados.map(function(e){return reconstroiBonificacao(e);})),
                        page : 1,
                        total : 1
                    });
                    T.preencherTelaBonificacoes();
                });
            }

            if (vendaMediaDTO.todasAsCotas) {
                $("#RDtodasAsCotas").click();
            }

            if (vendaMediaDTO.componente) {
                $("#RDcomponente").click();
                $("#componenteRegiaoDistribuicao").val(componentePDV[vendaMediaDTO.componente]).change();
                $(document).one("ajaxStop", function() {
                    if (temDicionarioElemento.indexOf(componentePDV[vendaMediaDTO.componente]) !== -1) {
                        $("#elementoRegiaoDistribuicao").val(elementoPDV[vendaMediaDTO.elemento]);
                        $("select[name=elementoRegiaoDistribuicao]:eq(1)").val(elementoPDV[vendaMediaDTO.elemento2]);
                        $("select[name=elementoRegiaoDistribuicao]:eq(2)").val(elementoPDV[vendaMediaDTO.elemento3]);
                    } else {
                        $("#elementoRegiaoDistribuicao").val(vendaMediaDTO.elemento);
                        $("select[name=elementoRegiaoDistribuicao]:eq(1)").val(vendaMediaDTO.elemento2);
                        $("select[name=elementoRegiaoDistribuicao]:eq(2)").val(vendaMediaDTO.elemento3);
                    }
                });
            }

            if (vendaMediaDTO.abrangenciaCriterio) {
                $("#RDAbrangencia").click();
                $("#RDabrangenciaCriterio").val(vendaMediaDTO.abrangenciaCriterio);
                $("#RDabrangencia").val(vendaMediaDTO.abrangencia);
            }

            if (vendaMediaDTO.roteiroEntregaId) {
                $("#RDroteiroEntrega").click();
                $("#selRoteiro").val(vendaMediaDTO.roteiroEntregaId);
            }

            $("#complementarAutomatico").prop('checked', vendaMediaDTO.complementarAutomatico);
            $("#cotasAVista").prop('checked', vendaMediaDTO.cotasAVista);

            if (vendaMediaDTO.excecaoDeBancasComponente) {
                $("#RDExcecaoBancas").click();
                $("#componenteInformacoesComplementares").val(componentePDV[vendaMediaDTO.excecaoDeBancasComponente]).change();
                $(document).one("ajaxStop", function () {
                    if (temDicionarioElemento.indexOf(componentePDV[vendaMediaDTO.excecaoDeBancasComponente]) !== -1) {
                        $("#elementoInformacoesComplementares1").val(elementoPDV[vendaMediaDTO.excecaoDeBancas[0]]);
                        $("#elementoInformacoesComplementares2").val(elementoPDV[vendaMediaDTO.excecaoDeBancas[1]]);
                        $("#elementoInformacoesComplementares3").val(elementoPDV[vendaMediaDTO.excecaoDeBancas[2]]);
                    } else {
                        $("#elementoInformacoesComplementares1").val(vendaMediaDTO.excecaoDeBancas[0]);
                        $("#elementoInformacoesComplementares2").val(vendaMediaDTO.excecaoDeBancas[1]);
                        $("#elementoInformacoesComplementares3").val(vendaMediaDTO.excecaoDeBancas[2]);
                    }

                });
            }
        }
    };
};

//@ sourceURL=distribuicaoVendaMedia.js
