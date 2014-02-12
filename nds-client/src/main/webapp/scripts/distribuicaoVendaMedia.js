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
		row.pesoInput = '<select name="select'+index+'" id="select'+index+'" onchange="distribuicaoVendaMedia.selecionarPesoProduto('+index+', this)" ><option value="1">1</option><option value="2">2</option><option value="3">3</option></select>';
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
		}else {
			row.percentualVenda = floatToPrice(row.percentualVenda);
		}
		if(row.percentualVenda == undefined){
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
		
		data.push({name:"filtro.codigo", value:codigo});
		data.push({name:"filtro.nome", value:produto});
		data.push({name:"filtro.edicao", value:edicao});
		data.push({name:"filtro.classificacao", value:classificacao});
		
		$("#edicaoProdCadastradosGrid").flexOptions({
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
										+ "/capa/getCapaEdicaoJson?/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
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
		if(T.produtoEdicaoBases != undefined && T.produtoEdicaoBases.length === 1){
			distribuicaoVendaMedia.alertaUmaEdicaoBase();
		}else{
			distribuicaoVendaMedia.gerar();
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
			}
		}
		
		if(T.bonificacaoSelecionados != undefined){
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
		
		data.push({name : "codigoProduto", value : $('#codigoProduto').text()});
		data.push({name : "numeroEdicao", value : $('#numeroEdicao').html()});
		data.push({name : "idLancamento", value : $('#idLancamento').val()});
		data.push({name : "dataLancamento", value: $('#dataLancamento').html()});
        //TODO adicionar numero periodo caso o idLancamento nao seja o suficiente
		
		$.postJSON(pathTela + "/distribuicaoVendaMedia/gerarEstudo", data, function(result) {
		    //usado para exibir as variaveis do estudo
            
			myWindow = window.open('', '_blank');
            myWindow.document.write(result.list[0]);
            myWindow.focus();
            
            var isLiberado = result.list[2];
            	
            $('#idEstudo').text(result.list[1]);
            $('#idStatusEstudo').text(isLiberado === true ? "Liberado" : "Gerado");
            
            	
            if(typeof(matrizDistribuicao)=="object"){
            		matrizDistribuicao.carregarGrid();
            }
            
            $("#codigoPesquisaBases").remove();
    		$("#produtoPesquisaBases").remove();
    		$("#produtoPesquisaBases").remove();
    		$("#edicaoProdCadastradosGrid").remove();
            
			exibirMensagemDialog("SUCCESS", ["Operação realizada com sucesso!"], "");
		});
	};
	
	this.cancelar = function(){
		
		$("#codigoPesquisaBases").remove();
		$("#produtoPesquisaBases").remove();
		$("#produtoPesquisaBases").remove();
		$("#edicaoProdCadastradosGrid").remove();
		
		$(".ui-tabs-selected").find("span").click();
		$("a[href='"+pathTela+"/matrizDistribuicao']").click();
	};
	
	this.redirectToTelaAnalise = function(){

        // Obter matriz de distribuição
        var matriz = [],
        	url = contextPath + "/distribuicao/analiseEstudo/obterMatrizDistribuicaoPorEstudo",
        	dadosResumo = {},
        	numeroEstudo = $('#idEstudo').text();
        
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
};

//@ sourceURL=distribuicaoVendaMedia.js
