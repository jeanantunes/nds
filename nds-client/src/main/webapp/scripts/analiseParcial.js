var analiseParcialController = $.extend(true, {

    path: contextPath,
    
    baseInicialAnalise: null, //para voltar ao estado original da analise

    linkNomeCota : '<a tabindex="-1" class="linkNomeCota" numeroCota="#numeroCota" >#nomeCota</a>',
    edicoesBase : [],
    edicoesBaseDadosEdicoes : [],
    detalheEdicoesBases : [],
    inputReparteSugerido: '<input #disabled reducaoReparte="#redReparte" nmCota="#nmCota" reparteInicial="#repEstudo" reparteAtual="#value" numeroCota="#numeroCota" ajustado="#ajustado" qtdPDV="#qtdPDV" quantidadeAjuste="#quantidadeAjuste" value="#value" idrowGrid="#idrow" percentualVenda="#percVenda"  class="reparteSugerido" />',
    tipoExibicao : 'NORMAL',
    
                                     
    totalizarEdicoesBasesDadosEdicoes : function(indice, reparte, venda){
    	
    	if (edicoesBaseDadosEdicoes[indice] != undefined){ 
    	
    	    edicoesBaseDadosEdicoes[indice].reparte = reparte;
    	
    	    edicoesBaseDadosEdicoes[indice].venda = venda;
    	}
    },                                
                                  
    
    exibirMsg: function(tipo, texto) {
        exibirMensagem(tipo, texto);
    },

    mostrarModalBaseVisualizacao:function(){
    	
    	$('#prodCadastradosGrid tbody').empty();
    	
    	var objEdicoesBase = {};
        objEdicoesBase.page = 1;
        objEdicoesBase.total = 1;
        objEdicoesBase.rows = [];
        var tipoClassificacao = $('#filtroClassificacao').html().replace('selected="selected"', '');

        for (var i = 0; i < analiseParcialController.edicoesBase.length; i++) {
            var edicao = analiseParcialController.edicoesBase[i];
            var optionClassificacao = 'value="id"'.replace('id',edicao.idTipoClassificacao);
            objEdicoesBase.rows.push({ cell: {
                codigo:         '<input class="inputCodigoEB" value="#">'.replace('#',edicao.codigoProduto),
                produto:        '<input class="inputProdutoEB" value="#">'.replace('#',edicao.nomeProduto),
                edicao:         '<input id="$" class="inputEdicaoEB" value="#">'.replace('$',edicao.produtoEdicaoId).replace('#',edicao.edicao),
                classificacao:  '<select class="selectClassEB">#options#</select>'.replace('#options#', tipoClassificacao.replace(optionClassificacao, optionClassificacao + ' selected')),
                acao:           '<img src="images/ico_editar.gif" alt="Alterar Edição" class="icoEditarEB">' +
                                '<img src="images/ico_excluir.gif" alt="Excluir Base" class="icoExcluirEB">' +
                                '<img src="images/ico_arrow_resize.png" alt="Mover Base" class="icoMoverEB">'
            } });
        }
        $("#prodCadastradosGrid").flexAddData(objEdicoesBase);

        $('#prodCadastradosGrid tbody').sortable({
            opacity: 0.8,
            placeholder: "sortable-placeholder",
            containment: $('#prodCadastradosGrid').closest('.flexigrid')
        });

        $("#dialog-mudar-base").dialog({
            escondeHeader: false,
            resizable : false,
            height : 470,
            width : 650,
            modal : true,
            buttons : {
                "Base Original" : function() {
                    $(this).dialog("close");
                    analiseParcialController.restauraBaseInicial();
                },
                "Confirmar" : function() {
                    var parameters = [];
                    var hasErros = false;

                    $('#prodCadastradosGrid tr').each(function (i) {

                        var $this = $(this);
                        var codigoProduto = $this.find('.inputCodigoEB').val();
                        var nomeProduto = $this.find('.inputProdutoEB').val();
                        var edicao = $this.find('.inputEdicaoEB').val();

                        if (codigoProduto === '' || nomeProduto === '' || edicao === '') {
                            analiseParcialController.exibirMsg('WARNING', ['É necessário informar todos os campos!']);
                            hasErros = true;
                            return false;
                        }

                        parameters.push({name: 'edicoesBase['+ i +'].ordemExibicao',    value: i});
                        parameters.push({name: 'edicoesBase['+ i +'].codigoProduto',    value: codigoProduto});
                        parameters.push({name: 'edicoesBase['+ i +'].nomeProduto',      value: nomeProduto});
                        parameters.push({name: 'edicoesBase['+ i +'].edicao',           value: edicao});
                        parameters.push({name: 'edicoesBase['+ i +'].produtoEdicaoId',  value: $this.find('.inputEdicaoEB').attr('id')});

                    });

                    if (!hasErros) {
                        
                    	analiseParcialController.baseInicialAnalise = null;
                    	
                    	parameters.push({name: 'id', value: $('#estudoId').val()},
                            {name: 'numeroEdicao', value: $('#numeroEdicao').val()},
                            {name: 'codigoProduto', value: $('#codigoProduto').val()},
                            {name: 'faixaDe', value: $('#faixaDe').val()},
                            {name: 'faixaAte', value: $('#faixaAte').val()});

                        $(this).dialog("close");
                        $("#baseEstudoGridParcial").flexOptions({params: parameters}).flexReload();
                    }
                },
                "Cancelar" : function() {
                    $('#prodCadastradosGrid tbody').sortable("cancel");
                    $(this).dialog("close");
                }
            }
        });
    },
    
    mudarBaseVisualizacao : function() {
    	analiseParcialController.verificarPermissaoAcesso(analiseParcialController.mostrarModalBaseVisualizacao); 
    },

    buscarNomeProduto : function(elemento) {
        if (elemento.value !== '') {
            $.ajax({
                url: analiseParcialController.path +'/produto/pesquisarPorCodigoProduto',
                data: [{name: 'codigoProduto', value: elemento.value}],
                type: 'POST',
                //global: false,
                success: function(result) {
                    if (result.mensagens) {
                        analiseParcialController.exibirMsg(result.mensagens);
                    } else {
                        var $tr = $(elemento).closest('tr');
                        $tr.find('.inputProdutoEB').val(result.result.nome);
                        $tr.find('.inputEdicaoEB').val('');
                    }
                }});
        } /*else {
            analiseParcialController.exibirMsg('WARNING', ['É necessário informar um código para o produto!']);
        }*/
    },

    buscarCodigoProduto : function(elemento) {
        if (elemento.value !== '') {
            $.ajax({
                url: analiseParcialController.path +'/produto/pesquisarPorNomeProduto',
                data: [{name: 'nomeProduto', value: elemento.value}],
                type: 'POST',
                //global: false,
                success: function(result) {
                    if (result.mensagens) {
                        analiseParcialController.exibirMsg(result.mensagens);
                    } else {
                        var $tr = $(elemento).closest('tr');
                        $tr.find('.inputCodigoEB').val(result.result.codigo);
                        $tr.find('.inputProdutoEB').val(result.result.nome);
                        $tr.find('.inputEdicaoEB').val('');
                    }
                }});
        } /*else {
            analiseParcialController.exibirMsg('WARNING', ['É necessário informar um nome para o produto!']);
        }*/
    },

    validarNumeroEdicao : function(elemento) {
        var codigoProduto = $(elemento).closest('tr').find('.inputCodigoEB');
        if (codigoProduto.val() !== '' && elemento.value !== '') {
            $.ajax({
                url:analiseParcialController.path + '/produto/validarNumeroEdicao',
                data:[{name: 'codigoProduto', value: codigoProduto.val()},
                     {name: 'numeroEdicao', value: elemento.value}],
                type: 'POST',
                global: false,
                success: function(result) {
                    if (result.mensagens) {
                        analiseParcialController.exibirMsg(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                        elemento.value = '';
                    } else {
                        $(elemento).attr('id', result.id);
                    }
                },
                error: function(result) {
                    if (result.mensagens) {
                        analiseParcialController.exibirMsg(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                        elemento.value = '';
                    }
                }});
        }
    },

    carregarDetalhesCota : function(numeroCota) {

        $.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/carregarDetalhesCota',
            [{name: 'numeroCota', value: numeroCota},
             {name: 'idClassifProdEdicao', value: $('#tipoClassificacaoProdutoId').val()},
             {name: 'codigoProduto', value: $('input[id="codigoProduto"]').val()}],
            function(result){

                var $dialog = $('#dialog-cotas-detalhes'),
                    $mix = $dialog.find('#dados-mix'),
                    $fixacao = $dialog.find('#dados-fixacao');
                $mix.hide();
                $fixacao.hide();
                
                var faturamentoFormatado = '';
                
                if(result.faturamento != undefined && result.faturamento != ""){
               	
                	var mediaFaturamento = ((result.faturamento)/3);
                	faturamentoFormatado = floatToPrice(mediaFaturamento);

                }
                
                $dialog.find('#numeroCotaD').text(result.numeroCota || '');
                $dialog.find('#nomeCotaD').text(result.nomePessoa || '');
                $dialog.find('#tipoCotaD').text(result.tipoCota || '');
                $dialog.find('#rankingCotaD').text(result.qtdeRankingSegmento || '');
                $dialog.find('#faturamentoCotaD').text(faturamentoFormatado != '' ? "R$ "+faturamentoFormatado : '');
                $dialog.find('#mesAnoCotaD').text("Últimos 3 meses.");

                if (result.mixDataAlteracao) {
                    $dialog.find('#mixRepMin').text(result.mixRepMin || '');
                    $dialog.find('#mixRepMax').text(result.mixRepMax || '');
                    $dialog.find('#mixUsuario').text(result.nomeUsuario || '');
                    $dialog.find('#mixDataAlteracao').text(result.mixDataAlteracao ? result.mixDataAlteracao.$ : '');
                    $mix.show();
                }

                if (result.fxDataAlteracao) {
                    $dialog.find('#fxEdicaoInicial').text(result.fxEdicaoInicial || '');
                    $dialog.find('#fxEdicaoFinal').text(result.fxEdicaoFinal || '');
                    $dialog.find('#fxEdicoesAtendidas').text(result.fxEdicoesAtendidas || '');
                    $dialog.find('#fxQuantidadeEdicoes').text(result.fxQuantidadeEdicoes || '');
                    $dialog.find('#fxQuantidadeExemplares').text(result.fxQuantidadeExemplares || '');
                    $dialog.find('#fxDataAlteracao').text(result.fxDataAlteracao ? result.fxDataAlteracao.$ : '');
                    $fixacao.show();
                }
        });


        $("#cotasDetalhesGrid").flexOptions({ params: [{name: 'numeroCota', value: numeroCota},
                                                       {name: 'estudoId', value: $('#estudoId').val()}] }).flexReload();

        $("#dialog-cotas-detalhes").dialog({
            escondeHeader: false,
            resizable : false,
            height : 560,
            width : 740,
            modal : true,
            buttons : {
                "Fechar" : function() {
                    $(this).dialog("close");
                }
            }
        });
    },

    somarTotais : function() {
        var totalJuramento = 0,
            totalUltimoReparte = 0,
            totalReparteSugerido = 0,
            totalReparteEstudoOrigem = 0,
            totais = [];

        for (var i=1; i<7; i++) {
            totais[i] = {};
            totais[i].reparte = 0;
            totais[i].venda = 0;
        }

        var totalCotas = $('#baseEstudoGridParcial tbody tr:visible').each(function(){
            var $tr = $(this);

            totalJuramento += $tr.find('td[abbr="juramento"] div').text() * 1;
            totalUltimoReparte += $tr.find('td[abbr="ultimoReparte"] div').text() * 1;
            totalReparteSugerido += $tr.find('td[abbr="reparteSugerido"] input').val() * 1;
            totalReparteEstudoOrigem += $tr.find('td[abbr="reparteEstudoOrigemCopia"] div').text() * 1;

            for (var i=1; i<7; i++) {
                totais[i].reparte += $tr.find('td[abbr="reparte' + i + '"] div').text() * 1;
                totais[i].venda += $tr.find('td[abbr="venda' + i + '"] div').text() * 1;
            }

        }).length;

        $('#total_juramento').text(totalJuramento);
        $('#total_ultimo_reparte').text(totalUltimoReparte);
        $('#total_reparte_sugerido').text(totalReparteSugerido);
        $('#total_reparte_origem').text(totalReparteEstudoOrigem);
        $('#total_de_cotas').text(totalCotas);

        for (var j = 1; j < 7; j++) {
        	
            $('#total_reparte'+ j).text(totais[j].reparte);
            
            $('#total_venda'+ j).text(totais[j].venda);
            
            analiseParcialController.totalizarEdicoesBasesDadosEdicoes(j-1, totais[j].reparte, totais[j].venda);
        }
    },
    
    alterarVisualizacaoGrid : function() {

        $('#baseEstudoGridParcial').closest('div.flexigrid').find('thead:visible tr:eq(0)').each(function () {
            var tr = $(this);
            var last = tr.find('th[colspan="2"]:last');
            tr.find('th[colspan="2"]').not(last).each(function(){
                $(this).detach().insertAfter(last);
            });
        });

        $('#baseEstudoGridParcial').closest('div.flexigrid').find('thead:visible tr:eq(1)').each(function() {
            var tr = $(this);
            var td1 = tr.find('[abbr="ultimoReparte"],[abbr="reparteSugerido"],[abbr="leg"],[abbr="juramento"],[abbr="reparteEstudoOrigemCopia"]');
            if (td1.index() > 7) {
                var td2 = tr.find('[abbr="npdv"]');
            } else {
                var td2 = tr.find('th:last');
            }
            td1.detach().insertAfter(td2);

            //reparte/venda
            var last = tr.find('[abbr^="venda"]:last');
            var tempReparte;
            tr.find('[abbr^="reparte"],[abbr^="venda"]').not(last).not(last.prev()).not('[abbr="reparteSugerido"],[abbr="reparteEstudoOrigemCopia"]').each(function(){
                var $this = $(this);
                if ($this.attr('abbr').indexOf('reparte') > -1) {
                    tempReparte = $this;
                    $this.detach().insertAfter(last);
                } else {
                    $this.detach().insertAfter(tempReparte);
                }
            });
        });

        $('#baseEstudoGridParcial tr').each(function() {
            var tr = $(this);
            var td1 = tr.find('[abbr="ultimoReparte"],[abbr="reparteSugerido"],[abbr="leg"],[abbr="juramento"],[abbr="reparteEstudoOrigemCopia"]');
            if (td1.index() > 7) {
                var td2 = tr.find('[abbr="npdv"]');
            } else {
                var td2 = tr.find('td:last');
            }
            td1.detach().insertAfter(td2);

            //reparte/venda
            var last = tr.find('[abbr^="venda"]:last');
            var tempReparte;
            tr.find('[abbr^="reparte"],[abbr^="venda"]').not(last).not(last.prev()).not('[abbr="reparteSugerido"],[abbr="reparteEstudoOrigemCopia"]').each(function(){
                var $this = $(this);
                if ($this.attr('abbr').indexOf('reparte') > -1) {
                    tempReparte = $this;
                    $this.detach().insertAfter(last);
                } else {
                    $this.detach().insertAfter(tempReparte);
                }
            });
        });

        $('.tableTotais tr').each(function() {
            var tr = $(this);
            var td1 = tr.find('#total_ultimo_reparte,#total_reparte_sugerido,#lbl_legenda,#total_juramento,#total_reparte_origem');
            if (td1.index() > 7) {
                var td2 = tr.find('#total_de_cotas');
            } else {
                var td2 = tr.find('td:last');
            }
            td1.detach().insertAfter(td2);

            //reparte/venda
            var last = tr.find('[id^="total_venda"]:last');
            var tempReparte;
            tr.find('[id^="total_reparte"],[id^="total_venda"]').not(last).not(last.prev()).not('#total_reparte_sugerido,#total_reparte_origem').each(function(){
                var $this = $(this);
                if ($this.attr('id').indexOf('reparte') > -1) {
                    tempReparte = $this;
                    $this.detach().insertAfter(last);
                } else {
                    $this.detach().insertAfter(tempReparte);
                }
            });
        });

        var colSpanEdicoesBase = $('#baseEstudoGridParcial').closest('div.flexigrid').find('thead:visible tr:eq(1)').find('th[abbr^="venda"]:first').index() - 1;
        $('#baseEstudoGridParcial').closest('div.flexigrid').find('thead:visible tr:eq(0) th[colspan!="2"]').attr('colspan', colSpanEdicoesBase);
    },

    atualizaEdicoesBaseHeader : function() {
        setTimeout(function(){
            var $header = $('table#baseEstudoGridParcial')
                .closest('div.flexigrid')
                .find('thead:visible');

            if ($header.find('tr').length > 1) {
                $header.find('tr').first().remove();
            }

            var colSpanEdicoesBase = $header.find('tr th[abbr^="venda"]:first').index() - 1;

            if (analiseParcialController.tipoExibicao === 'NORMAL') {
                $header.prepend($('<tr>').append($('<th colspan="' + colSpanEdicoesBase + '" style="border-bottom: 1px solid #DDDDDD;">')
                    .append('<div style="text-align: right;">Edições Base:</div>')));

                if (colSpanEdicoesBase === 7 || colSpanEdicoesBase === 8) {
                    for (var i=0; i<6; i++) {
                        var edicao = $.extend({}, {edicao:'-'}, analiseParcialController.edicoesBase[i]);
                        $header.find('tr').first().append($('<th colspan="2">').append($('<div style="text-align: center;">').append(edicao.edicao)));
                    }
                } else {
                    for (var i=5; i>=0; i--) {
                        var edicao = $.extend({}, {edicao:'-'}, analiseParcialController.edicoesBase[i]);
                        $header.find('tr').first().append($('<th colspan="2">').append($('<div style="text-align: center;">').append(edicao.edicao)));
                    }
                }

            } else {
                $header.prepend(
                    $('<tr>')
                        .append($('<th colspan="' + colSpanEdicoesBase + '" style="border-bottom: 1px solid #DDDDDD;">')
                            .append('<div style="text-align: right;">Edições Base:</div>'))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append($('#dataLancamentoParcial1').val())))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append($('#dataLancamentoParcial2').val())))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append($('#dataLancamentoParcial3').val())))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('Acumulado')))
                );
            }
        }, 0);
    },

    atualizaReparte : function(input, isAtualizarRepartePDV) {
    	
    	if (!$('#saldo_reparte').text() || $('#saldo_reparte').text() == ""){
    		$('#saldo_reparte').text(0);
    	}
    	
        var saldoReparte = $('#saldo_reparte');
        var input_reparte_element = $(input);
        var numeroCota = input_reparte_element.attr('numeroCota');
        var nomeCota = input_reparte_element.attr('nmCota');
        var qtdPdv = input_reparte_element.attr('qtdPDV');
        var reparteDigitado = ((input_reparte_element.val() == "") ? 0 : input_reparte_element.val());
        var reparteAtual = input_reparte_element.attr('reparteAtual');
        var idRowGrid = input_reparte_element.attr('idrowgrid');
        var reparteSubtraido = parseInt(reparteDigitado, 10) - parseInt(reparteAtual, 10);
        var legenda_element = input_reparte_element.closest('td').next().find('div');
        
        if (reparteAtual != reparteDigitado) {
            var legendaText = legenda_element.text();
            
            if (legendaText.indexOf('FX') > -1 || legendaText.indexOf('MX') > -1) {
            	
            	var codProd = $("#codigoProduto", analiseParcialController.workspace).text() || $("#codigoProduto", analiseParcialController.workspace).val();
            	var usedMix = $('#usedMinMaxMix', analiseParcialController.workspace).val();
            	
            	if((legendaText.indexOf('MX') > -1) && (usedMix == "false")){
            		analiseParcialController.atualizarReparteCota(input_reparte_element, numeroCota, reparteSubtraido, reparteDigitado, reparteAtual, saldoReparte, legenda_element, idRowGrid);
            		return;
            	}
            	
            	$.postJSON(
            		analiseParcialController.path + '/distribuicao/analise/parcial/verificarMaxMinCotaMix',
            		[{name:"numeroCota", value:numeroCota},
            		 {name:"codigoProduto", value:"" + codProd + ""},
            		 {name:"qtdDigitado", value:reparteDigitado},
            		 {name:"tipoClassificacaoProduto", value:$("#tipoClassificacaoProdutoId").val()}],
            		 function(result){
            			
            			isAtualizarRepartePDV = result[1];
            			
            			if (!result[0]){
            			
            				usuarioController.supervisor.verificarRoleSupervisao({
        		            	optionalDialogMessage: 'É necessario confirmar esta ação com senha.',
        		            	callbacks: {
        		    				usuarioSupervisorCallback: function() {
        		    					
        		    					if((qtdPdv > 1) && (isAtualizarRepartePDV)){
        		    						
        	    							analiseParcialController.defineRepartePorPDV(
        	    									numeroCota, nomeCota, reparteDigitado, legendaText);
        	    							
        	    							analiseParcialController.atualizarReparteCota(
        	    									input_reparte_element, numeroCota, reparteSubtraido, 
        	    									reparteDigitado, reparteAtual, saldoReparte, legenda_element);
        	    							
        		    	                }else{
        		    	                	analiseParcialController.atualizarReparteCota(
        		    	                			input_reparte_element, numeroCota, reparteSubtraido, 
        		    	                			reparteDigitado, reparteAtual, saldoReparte, legenda_element);
        		    	                }
        		    				},
        		    				usuarioNaoSupervisorCallback: function(){
        		    					analiseParcialController.resetReparteSugerido(input, numeroCota);
        			        		}
        		    			}
        		            });
            			} else {

            				analiseParcialController.atualizarReparteCota(
            						input_reparte_element, numeroCota, reparteSubtraido, reparteDigitado, 
            						reparteAtual, saldoReparte, legenda_element);
            			}
            		}
            	);
            }else{
            	analiseParcialController.atualizarReparteCota(input_reparte_element, numeroCota, reparteSubtraido, reparteDigitado, reparteAtual, saldoReparte, legenda_element, idRowGrid);
            }
        }
    },
    
    atualizarReparteCota : function(input_reparte_element, numeroCota, reparteSubtraido, reparteDigitado, reparteAtual, saldoReparte, legenda_element, idRowGrid){

    	var legendaCota = legenda_element.text();

    	var mixID 	  = legenda_element.find("span").html() === 'MX' ? legenda_element.find("span").attr("mixID") : '';
    	var fixacaoID = legenda_element.find("span").html() === 'FX' ? legenda_element.find("span").attr("fixacaoID") : '';
    	
    	var usedMix = $('#usedMinMaxMix', analiseParcialController.workspace).val();
    	
    	if(usedMix == "false"){
    		legendaCota = "";
    	}
    	var cota_legenda =  mixID ? mixID : fixacaoID;

		$.ajax({
            url: analiseParcialController.path +'/distribuicao/analise/parcial/mudarReparte',
            data: [{name:'numeroCota', value : numeroCota},
    		     {name:'estudoId', value : $('#estudoId').val()},
    		     {name:'variacaoDoReparte', value : reparteSubtraido},
    		     {name:'reparteDigitado', value : reparteDigitado},
    		     {name:'legendaCota', value : legendaCota},
    		     {name:'fixacaoMixID', value : cota_legenda}],
            type: 'post',
            dataType: 'json',
            async: false,
            cache: false,
            success: function(result) {
	    		
    			var saldoReparteAtualizado = parseInt(saldoReparte.text(), 10) - reparteSubtraido;
		          
		        saldoReparte.text(saldoReparteAtualizado);
		      	
		      	input_reparte_element.attr('reparteAtual', reparteDigitado);
		      	
		      	var reparteInicial = input_reparte_element.attr('reparteInicial');
		          
		        input_reparte_element.attr('reducaoReparte', analiseParcialController.calculaPercentualReducaoReparte(reparteInicial, reparteDigitado));
		          
				if (reparteDigitado === reparteInicial) {
					legenda_element.removeClass('asterisco');
				} else {
					legenda_element.addClass('asterisco');
				}
				
				$('#total_reparte_sugerido').text(
					$('#baseEstudoGridParcial tr td input:text').map(function() {
						return parseInt(this.value, 10);
					}).toArray().reduce(function(a, b) {
						return a + b; 
					})
				);
				
				if (typeof histogramaPosEstudoController != 'undefined') {
				
					histogramaPosEstudoController.change.refreshGrid = true;
					histogramaPosEstudoController.change.estudoId = $('#estudoId').val();
  	
				}
  
				$('#abrangencia').text(result).formatNumber({format:'#.00 %', locale:'br'});
    		},
    		error: function(result) {

    			analiseParcialController.exibirMsg(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
        		input_reparte_element.val(reparteAtual);
                $('#abrangencia').text('');
        		
			}
        });
    },
    
    resetReparteSugerido : function(input, numeroCota){
    	
    	var reparteInicial = $(input).attr('reparteInicial');
		
		$('#baseEstudoGridParcial td[abbr="reparteSugerido"] input[numerocota="' + numeroCota + '"]', analiseParcialController.workspace).val(reparteInicial).get(0);
		
    },

    calculaPercentualReducaoReparte: function (reparteEstudo, reparteSugerido) {
        var repEstudo = isNaN(reparteEstudo)?0:reparteEstudo;
        var repSugerido = isNaN(reparteSugerido)?0:reparteSugerido;
        var reducaoReparte = Math.abs(repEstudo - repSugerido);
        return Math.round((reducaoReparte / repEstudo) * 10000) / 100;
    },

    restauraBaseInicial : function() {
    	
    	$.postJSON(
    		analiseParcialController.path + '/distribuicao/analise/parcial/restaurarBaseInicial'
    	);
    	
        $('#baseEstudoGridParcial').flexAddData(analiseParcialController.baseInicialAnalise);
    },

    preProcessGrid : function(resultado) {

    	var disabled = $('#status_estudo').text()==='Liberado';
    	
        if (resultado.mensagens) {
            analiseParcialController.exibirMsg(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
            return;
        }

        if (resultado.rows.length === 0) {
            analiseParcialController.exibirMsg('WARNING', ['Não há resultados para esta consulta.']);
            return;
        }

        var $header = $('table#baseEstudoGridParcial')
            .closest('div.flexigrid')
            .find('thead:visible');

        if ($header.find('tr').length > 1) {
            $header.find('tr').first().remove();
        }

        if (null == analiseParcialController.baseInicialAnalise) {
            analiseParcialController.baseInicialAnalise = $.extend(true, {}, resultado);
        }
        analiseParcialController.edicoesBase = resultado.rows[0].cell.edicoesBase;

        var totalSaldoReparte = 0;
        
        // atualização dos valores da grid
        for (var i = 0; i < resultado.rows.length; i++) {
        	
        	var cell = resultado.rows[i].cell;
        	var numCota = cell.cota;
        	
        	var somaReparteCota = 0;
        	var somaVendasCota = 0;
        	var porcentagemVendaCota = 0;

        	for (var j = 0; j < 6; j++) {
                if (typeof cell.edicoesBase[j] === 'undefined' || typeof cell.edicoesBase[j].reparte === 'undefined') {
                    cell['reparte'+ (j + 1)] = '';
                    cell['venda'+ (j + 1)] = '';
                } else {
                    cell['reparte'+ (j + 1)] = cell.edicoesBase[j].reparte;
                    somaReparteCota += Number(cell.edicoesBase[j].reparte);
                    
                    if (cell.edicoesBase[j].venda){
                    	cell['venda'+ (j + 1)] = cell.edicoesBase[j].venda;
                    	somaVendasCota += Number(cell.edicoesBase[j].venda);
                    } else {
                    	cell['venda'+ (j + 1)] = '';
                    }
                    
                    if( cell['reparte'+ (j + 1)] === 0 ||  cell['reparte'+ (j + 1)]=== "0"){
                    	 cell['reparte'+ (j + 1)] = "";
                    }
                    
                    if(cell['venda'+ (j + 1)] === 0 || cell['venda'+ (j + 1)] === "0"){
                    	
                    	if(cell['reparte'+ (j + 1)] === 0 ||  cell['reparte'+ (j + 1)] == ""){
                    		cell['venda'+ (j + 1)] = "";
                    	}
                    }
                }
            }
        	
        	if(somaVendasCota > 0){
        		porcentagemVendaCota = ((somaVendasCota)/(somaReparteCota))*100;
        	} 
        	
        	
            var input = analiseParcialController.inputReparteSugerido.toString()
                            .replace(/#numeroCota/g, numCota)
                            .replace(/#value/g, cell.reparteSugerido)
                            .replace(/#repEstudo/g, cell.reparteEstudo)
                            .replace(/#disabled/g, disabled ? 'disabled':'')
                            .replace(/#ajustado/g, cell.ajustado)
                            .replace(/#quantidadeAjuste/g, cell.quantidadeAjuste)
                            .replace(/#redReparte/g, analiseParcialController.calculaPercentualReducaoReparte(cell.reparteEstudo, cell.reparteSugerido))
                            .replace(/#qtdPDV/g, cell.npdv)
                            .replace(/#idrow/g, i+1)
                            .replace(/#percVenda/g, porcentagemVendaCota)
                            .replace(/#nmCota/g, cell.nome);
            
            cell.reparteSugerido = input;
            
            cell.nome = analiseParcialController.linkNomeCota.replace('#nomeCota', cell.nome).replace('#numeroCota', cell.cota);

            if (typeof cell.classificacao === 'undefined') {
                cell.classificacao = '';
            }
            if (cell.cotaNova == true) {
                cell.cota += '<span class="asteriscoCotaNova"></span>';
            }
            if (cell.npdv > 1 && cell.contemRepartePorPDV == true) {
                cell.npdv = '<a tabindex="-1" class="editaRepartePorPDV" numeroCota="'+ numCota +'">'+ cell.npdv +'</a>';
            }
            if (cell.leg === 'S') {
                cell.leg = '';
            }
            if (cell.leg !== '') {
            	
            	var mixID = cell.mixID ? 'mixID="' + cell.mixID + '"': '';
            	var fixacaoID = cell.fixacaoID ? 'fixacaoID="' + cell.fixacaoID  + '"': '';
            	
                cell.leg = '<span class="legendas" id="leg_'+ numCota +'" title="'+ cell.descricaoLegenda +'"' + mixID + ' ' + fixacaoID + '>'+ cell.leg +'</span>';
            }
            if (cell.juramento == 0) {
                cell.juramento = '';
            }
            
            if (!cell.ultimoReparte || cell.ultimoReparte === 0 || cell.ultimoReparte === "0"){
            	cell.ultimoReparte = '';
            }
            
          
            totalSaldoReparte += parseInt(cell.quantidadeAjuste);
        }

        if(resultado.rows[0].cell.edicoesBase != undefined){
      	   
        	edicoesBaseDadosEdicoes = resultado.rows[0].cell.edicoesBase; 
        }    
        
        return resultado;
    },

    atualizaAbrangencia: function () {
        $.post(
            analiseParcialController.path + '/distribuicao/analise/parcial/percentualAbrangencia',
            [{name: 'estudoId', value: $('#estudoId').val()}],
            function (result) {
                if (result.mensagens) {
                    analiseParcialController.exibirMsg(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                    $('#abrangencia').text('');
                } else {
                    $('#abrangencia').text(result).formatNumber({format:'#.00 %', locale:'br'});
                }
            }
        );
    },

    onSuccessReloadGrid : function() {
        //limpa espaços da grid
        $('table#baseEstudoGridParcial tr td div').filter(function(){return $.trim($(this).html()) === '&nbsp;';}).text('');

        analiseParcialController.somarTotais();
        analiseParcialController.atualizaEdicoesBaseHeader();
        analiseParcialController.atualizaAbrangencia();

        //insere asterisco para marcações de reparteSugerido != reparteEstudo
        $('table#baseEstudoGridParcial tr td[abbr="reparteSugerido"] div input').each(function(){
            
        	var $this = $(this);
            
            if (($this.attr('reparteInicial') != $this.attr('reparteAtual'))||($this.attr('ajustado') == "true")) {
            	
                $this.closest('tr').find('td[abbr="leg"] div').addClass('asterisco');
            }
            
//            analiseParcialController.addEventoLegenda();
        });
       
    },

    modeloNormal : function (estudoOrigem) {
        var modelo = [
            {display: 'Cota',       name: 'cota',               width: 35, sortable: true, align: 'right'},
//            {display: 'Cota Nova',  name: 'cotaNova',           width: 1,  hide: true},
            {display: 'Class.',     name: 'classificacao',      width: 30, sortable: true, align: 'center'},
            {display: 'Nome',       name: 'nome',               width: 150,sortable: true, align: 'left'},
            {display: 'NPDV',       name: 'npdv',               width: 30, sortable: true, align: 'right'},
            {display: 'Últ. Rep.',  name: 'ultimoReparte',      width: 50, sortable: true, align: 'right'},
            {display: 'Rep. Sug.',  name: 'reparteSugerido',    width: 50, sortable: true, align: 'right'},
            {display: 'LEG',        name: 'leg',                width: 20, sortable: true, align: 'center'}];

        if (estudoOrigem) {
            modelo = modelo.concat([{display: 'Est. Orig.', name: 'reparteEstudoOrigemCopia', width: 50, sortable: true, align: 'right'}]);
        }
        modelo = modelo.concat([
//            {display: 'Desc Leg',   name: 'descricaoLegenda',   width: 1,  hide: true},
//          {display: 'Média. VDA',  name: 'mediaVenda',         width: 60, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte1',           width: 30, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda1',             width: 30, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte2',           width: 30, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda2',             width: 30, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte3',           width: 30, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda3',             width: 30, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte4',           width: 30, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda4',             width: 30, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte5',           width: 30, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda5',             width: 30, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte6',           width: 30, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda6',             width: 30, sortable: true, align: 'right'}]);

        return modelo;
    },
    
    modeloParcial : function() {
        return [
            {display: 'Cota',       name: 'cota',               width: 35, sortable: true, align: 'right'},
//            {display: 'Cota Nova',  name: 'cotaNova',           width: 40, sortable: true, hide: true},
            {display: 'Class.',     name: 'classificacao',      width: 30, sortable: true, align: 'center'},
            {display: 'Nome',       name: 'nome',               width: 150,sortable: true, align: 'left'},
            {display: 'NPDV',       name: 'npdv',               width: 30, sortable: true, align: 'right'},
            {display: 'Últ. Rep.',  name: 'ultimoReparte',      width: 50, sortable: true, align: 'right'},
            {display: 'Rep. Sug.',  name: 'reparteSugerido',    width: 50, sortable: true, align: 'right'},
            {display: 'LEG',        name: 'leg',                width: 20, sortable: true, align: 'center'},
//            {display: 'Desc Leg',   name: 'descricaoLegenda',   width: 1,  sortable: false, hide: true},
            {display: 'Juram.',     name: 'juramento',          width: 40, sortable: true, align: 'right'},
//          {display: 'Média. VDA',  name: 'mediaVenda',         width: 50, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte1',           width: 40, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda1',             width: 40, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte2',           width: 40, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda2',             width: 40, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte3',           width: 40, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda3',             width: 40, sortable: true, align: 'right'},
            {display: 'REP',        name: 'reparte4',           width: 40, sortable: true, align: 'right'},
            {display: 'VDA',        name: 'venda4',             width: 40, sortable: true, align: 'right'}];
    },

    sortGrid : function (sortname, sortorder) {
        var $table, settings, sortAtribute;
        $table = $(arguments.callee.caller.arguments).closest('.flexigrid').find('div.bDiv:first table');
        settings = {order: sortorder};
        sortAtribute = 'td[abbr="' + sortname + '"] div';

        if (sortname === 'reparteSugerido') {
            settings.useVal=true;
            sortAtribute += ' input';
        }

        $table.find('tr')
            .tsort(sortAtribute, settings)
            .removeClass('erow').filter(':odd').addClass('erow')
            .end().find('td').removeClass('sorted')
            .filter('[abbr="' + sortname + '"]').addClass('sorted');

        return false; //Impede que o flexgrid faça um request para repopular o grid.
    },

    salvarRepartePorPDV: function ($dialogReparte, reparteCota, numeroCota, legenda) {
        
    	var $repartesPDV = $dialogReparte.find('table.pdvCotaGrid_AP tr input.repartePDV');
        
        var totalRepartePDVs = $repartesPDV.map(function () {
            return parseInt(this.value, 10);
        }).toArray().reduce(function (a, b) {
                return a + b;
            });
        
        if (totalRepartePDVs != reparteCota) {
            var inputReparteSugeridoCota = $('#baseEstudoGridParcial td[abbr="reparteSugerido"] input[numerocota="' + numeroCota + '"]').val(totalRepartePDVs).get(0);
            analiseParcialController.atualizaReparte(inputReparteSugeridoCota, false);
        }
        
        var param = [];
        param.push({name: 'estudoId', value: $('#estudoId').val()});
        param.push({name: 'numeroCota', value: numeroCota});
        param.push({name: 'legenda', value: legenda});
        param.push({name: 'manterFixa', value: $('#AP_dialog-defineReparte').find('input[name="input2"]').is(':checked')});
        $repartesPDV.each(function (k) {
            param.push({name: 'reparteMap[' + k + '].id', value: $(this).closest('tr').find('td[abbr="id"] div').text()});
            param.push({name: 'reparteMap[' + k + '].reparte', value: this.value});
        });
        $.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/defineRepartePorPDV', param);
    },

    defineRepartePorPDV: function(numeroCota, nomeCota, reparteCota, legenda) {

        var $dialogReparte = $('#AP_dialog-defineReparte', analiseParcialController.workspace);
        $dialogReparte.find('span.numeroCota').text(numeroCota);
        $dialogReparte.find('span.nomeCota').text(nomeCota);
        $dialogReparte.find('span.reparteCota').text(reparteCota);

        $('.pdvCotaGrid_AP', analiseParcialController.workspace).flexOptions({params:[{name: 'numeroCota', value: numeroCota},
                                               {name: 'estudoId', value: $('#estudoId').val()}]}).flexReload();

        $dialogReparte.dialog({
            escondeHeader: false,
            resizable : false,
            height: 580,
            width: 660,
            modal : true,
            buttons : {
                "Confirmar" : function() {
                    if (legenda.indexOf('FX') > -1) {
                        $('<div>Confirma alteração de fixação por PDV para esta cota?</div>').dialog({
                            escondeHeader: false,
                            title: 'Confirmação',
                            buttons: {
                                "Confirmar": function() {
                                    $(this).dialog("close");
                                    $dialogReparte.dialog("close");
                                    analiseParcialController.salvarRepartePorPDV($dialogReparte, reparteCota, numeroCota, legenda);
                                },
                                "Cancelar": function() {
                                    $(this).dialog("close");
                                }
                            }
                        });
                    } else if (legenda.indexOf('MX') > -1) {
                        $('<div>Confirma alteração de mix por PDV para esta cota?</div>').dialog({
                            escondeHeader: false,
                            title: 'Confirmação',
                            buttons: {
                                "Confirmar": function() {
                                    $(this).dialog("close");
                                    $dialogReparte.dialog("close");
                                    analiseParcialController.salvarRepartePorPDV($dialogReparte, reparteCota, numeroCota, legenda);
                                },
                                "Cancelar": function() {
                                    $(this).dialog("close");
                                }
                            }
                        });
                    } else {
                        analiseParcialController.salvarRepartePorPDV($dialogReparte, reparteCota, numeroCota, legenda);
                        $(this).dialog("close");
                    }
                },
                "Cancelar" : function() {
                    $(this).dialog("close");
                }
            }
        });
    },

    init : function(_id, _faixaDe, _faixaAte, _tipoExibicao){
    	
    	$('#filtroOrdenarPor option:eq(1)').prop('selected', true).parent().change();

        $("#cotasQueNaoEntraramNoEstudo_cota").change(function(){
            var numeroCota = this.value;
            if (numeroCota != '') {
                $.ajax({
                    url: analiseParcialController.path + '/cadastro/cota/pesquisarPorNumero',
                    data: [{name: 'numeroCota', value: numeroCota}],
                    type: 'POST',
                    success: function(result) {
                        if (result.mensagens) {
                            analiseParcialController.exibirMsg(result.mensagens);
                        } else {
                            $('#cotasQueNaoEntraramNoEstudo_nome').val(result.result.nome);
                        }
                    }});
            }
        });

        $('#cotasQueNaoEntraramNoEstudo_nome').autocomplete({
            source: function(request, response) {
                $.ajax({
                    url: analiseParcialController.path + '/cadastro/cota/autoCompletarPorNome',
                    data: [{name: 'nomeCota', value: request.term}],
                    type: 'POST',
                    global: false,
                    success: function(result) {
                        response(result.result.slice(0,10)); // mostrar apenas ,10 resultados
                    }});
            },
            minLength: 3,
            select: function( event, ui ) {
                $("#cotasQueNaoEntraramNoEstudo_cota").val(ui.item.chave.numero);
//                event.preventDefault();
//                event.stopPropagation();
            }
        });

        $( "#inputNomeProduto" ).autocomplete({
            source: function(request, response) {
                $.ajax({
                    url: 'produto/autoCompletarPorNome',
                    data: [{name: 'nome', value: request.term}],
                    type: 'POST',
                    global: false,
                    success: function(result) {
                        response(result.result.slice(0,10)); // mostrar apenas ,10 resultados
                    }});
            },
            minLength: 3,
            select: function( event, ui ) {
                $('#inputCodigoProduto').val(ui.item.value);
            }
        });

        $("#dialog-edicoes-produtos").on('click', '.classPesquisar', function () {
            var parameters = $('#inputCodigoProduto, #inputNomeProduto, #inputNumeroEdicao, #filtroClassificacao').filter('input[value!=""], select[value!="-1"]').map(function () {
                return {name: this.name, value: this.value};
            });
            if (parameters) {
                $("#edicaoProdCadastradosGrid:visible").flexOptions({params: parameters}).flexReload();
            }
        });

        $('#dialog-mudar-base')
        .on('click', '.icoExcluirEB, .icoEditarEB', function () {
            var $this = $(this);
            if ($this.hasClass('icoExcluirEB')) {
                if ($this.closest('tbody').find('tr').length > 1) {
                    $this.closest('tr').remove();
                }
            }
            if ($this.hasClass('icoEditarEB')) {
                popup_edicoes_produto();
            }
        })
        .on('change', '.inputCodigoEB, .inputProdutoEB, .inputEdicaoEB', function () {
            var $this = $(this);

            if ($this.hasClass('inputCodigoEB')) {
                analiseParcialController.buscarNomeProduto(this);
            }
            if ($this.hasClass('inputProdutoEB')) {
                analiseParcialController.buscarCodigoProduto(this);
            }
            if ($this.hasClass('inputEdicaoEB')) {
                analiseParcialController.validarNumeroEdicao(this);
            }
        });

        $('#dialog-edicoes-produtos')
        .on({
                mouseenter: function (event) {
                    var $tr = $(this).closest('tr');
                    var edicao = $tr.find('td[abbr="numeroEdicao"] div').text();
                    var codPorduto = $tr.find('td[abbr="codigoProduto"] div').text();
                    var urlImg = "capa/getCapaEdicaoJson?codigoProduto=#cod#&numeroEdicao=#ed#"
                        .replace('#cod#', codPorduto)
                        .replace('#ed#', edicao);

                    var $divCapa = $('#previewImagemCapa');

                    $divCapa.find('img').attr('src', urlImg);

                    $divCapa.dialog({
                        escondeHeader: false,
                        title:'Capa',
                        resizable: false,
                        height:'auto',
                        width:'auto',
                        modal: false,
                        position: { my: "left", at: "right", of: $(event.target).parent() }
                    });
                },
                mouseleave: function () {
                    $('#previewImagemCapa').dialog("close");
                }
        }, '.icoCapaEB')
        .on('change', '.inputCodigoEB', function () {
            var $this = $(this);

            if ($this.hasClass('inputCodigoEB')) {
                analiseParcialController.buscarNomeProduto(this);
            }
        });

        $('#baseEstudoGridParcial')
        .on('focus', 'tr td input:text', function(event){
        	
        	unbindAjaxLoading();
        	
        }).on('blur', 'tr td input:text', function(event){
            
        	analiseParcialController.atualizaReparte(this, true);
        	
        	bindAjaxLoading();

        }).on('keyup', 'tr td input:text', function(event){
            if(event.which === 13) {//tab === 9
                $(event.currentTarget)
                    .closest('tr').next('tr')
                    .find('input:text').focus()
                    .select();
            }
        }).on('click', 'tr td input:text', function(event){
            $(event.currentTarget).select();
        }).on('click', 'a.editaRepartePorPDV', function(event){
            event.preventDefault();
            analiseParcialController.defineRepartePorPDV($(this).attr('numeroCota'),
                $(this).closest('tr').find('a.linkNomeCota').text(),
                $(this).closest('tr').find('input.reparteSugerido').val(),
                $(this).closest('tr').find('td[abbr="leg"] span.legendas').text());
        }).on('click', 'a.linkNomeCota', function(event) {
            event.preventDefault();
            analiseParcialController.carregarDetalhesCota($(this).attr('numeroCota'));
        });

        analiseParcialController.tipoExibicao = _tipoExibicao;

        var estudoOrigem = $('#estudoOrigem').val();
        var parameters = [];
        parameters.push({name: 'id', value: _id});
        parameters.push({name: 'faixaDe', value: _faixaDe});
        parameters.push({name: 'faixaAte', value: _faixaAte});
        parameters.push({name: 'codigoProduto', value: $('#codigoProduto').val()});
        parameters.push({name: 'numeroEdicao', value: $('#numeroEdicao').val()});
        parameters.push({name: 'estudoOrigem', value: estudoOrigem});
        parameters.push({name: 'dataLancamentoEdicao', value: $('#dataLancamentoEdicao').val()});
        parameters.push({name: 'numeroParcial', value: $('#numeroPeriodo').val()});
        
        if(typeof(histogramaPosEstudo_cotasRepMenorVenda)!="undefined"){
        	parameters.push({name: "numeroCotaStr", value: histogramaPosEstudo_cotasRepMenorVenda});
        }

        var modelo = _tipoExibicao == 'NORMAL' ? analiseParcialController.modeloNormal(estudoOrigem) : analiseParcialController.modeloParcial();
        $('#baseEstudoGridParcial').flexigrid({
            preProcess : analiseParcialController.preProcessGrid,
            url : analiseParcialController.path + '/distribuicao/analise/parcial/init',
            params: parameters,
            dataType : 'json',
            colModel : modelo,
            width: estudoOrigem?1035:980,
            height: 200,
            colMove: false,
            showToggleBtn: false,
            sortorder: 'desc',
            sortname: 'reparteSugerido',
            onChangeSort: analiseParcialController.sortGrid,
            onSuccess: analiseParcialController.onSuccessReloadGrid
        });


        $('#liberar').click(function(event){
            
        	setTimeout(function(){
        	
	        	if(analiseParcialController.verificacoesParaLiberarEstudo()) {                
	            	var id = $('#estudoId').val();
	            		$.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/verificacoesParaLiberarEstudo',
	            				[{name : 'estudoId', value : id}],
	            				
	            				function(result) {
	            					analiseParcialController.liberarEstudo();
			            		},
			            		function(result) {
			            			analiseParcialController.exibirMsg(result.tipoMensagem, result.listaMensagens);
			            		},
			            		null
	            		);
	            		
	            }
	            event.preventDefault();
	            
        	},1500);
        });
        
        $('#naoLiberar').click(function(event){
        	analiseParcialController.exibirMsg('WARNING', ['Já existe um estudo liberado para este lançamento.']);
            event.preventDefault();
        });

        $('#cotasNaoSelec', analiseParcialController.workspace).flexigrid({
            preProcess : analiseParcialController.preProcessGridNaoSelec,
            url: analiseParcialController.path + '/distribuicao/analise/parcial/cotasQueNaoEntraramNoEstudo/filtrar',
            dataType : 'json',
            colModel : [{display: 'Cota',         name: 'numeroCota',      width: 40,  sortable: true, align: 'left'},
                        {display: 'Nome',         name: 'nomeCota',        width: 160, sortable: true, align: 'left'},
                        {display: 'Sigla Motivo', name: 'siglaMotivo',     width: 10,  sortable: true, hide: true},
                        {display: 'Motivo',       name: 'motivo', 		   width: 160, sortable: true, align: 'left'},
                        {display: 'Qtde',         name: 'quantidade',      width: 60,  sortable: false, align: 'center'}],
            width : 490,
            height : 200,
            autoload: false
        });
        
        
//        $('.asterisco').mouseenter(function (event){
//        	$('.asterisco').tipsy({live: true, gravity: 'sw', title: function(){return 'Reparte Alterado';}});
//        	setTimeout($(".tipsy").hide(),3000);
//        });
        
    },
    
    verificacoesParaLiberarEstudo : function(){
    	
    	if ($('#status_estudo').text() == 'Liberado') {
            analiseParcialController.exibirMsg('WARNING', ['Estudo já está libearado.']);
            return false;
        }else if ($('#saldo_reparte').html() != 0) {
            analiseParcialController.exibirMsg('WARNING', ['Não é possível liberar estudo com saldo de reparte.']);
            return false;
    	}else{
    		if(analiseParcialController.verificarDivergenciaReparte()){
    			return true;
    		}else{
    			analiseParcialController.exibirMsg('WARNING', ['Não é possível liberar estudo com saldo de reparte.']);
    			return false;
    		}
    	}
    },
    
    verificarDivergenciaReparte : function(){
    	var reparteEstudo = $('#total_reparte_estudo_cabecalho').text();
    	var reparteDistribuido = $('#total_reparte_sugerido').text();
    	
    	if(reparteEstudo != undefined && reparteDistribuido != undefined){
    		if(reparteEstudo != reparteDistribuido){
    			var novoReparte = reparteEstudo - reparteDistribuido;
    			$('#saldo_reparte').text(novoReparte);
    			return false;
    		}else{
    			return true;
    		}
    	}else{
    		return true;
    	}
    },
    
    liberarEstudo : function(){
    	
    	$('<div>Liberar estudo?</div>').dialog({
			escondeHeader: false,
			title: 'Confirmação',
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					$.post(analiseParcialController.path +'/distribuicao/analise/parcial/liberar', analiseParcialController.getDadosLiberacao(), function(result) {
						
						if (result.mensagens) {
			        		
			        		analiseParcialController.exibirMsg(
			        			result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			        		
			        		return;
			        	}
						
						$('#status_estudo').text('Liberado');
						analiseParcialController.exibirMsg('SUCCESS', ['Estudo liberado com sucesso!']);
						
						if(typeof(matrizDistribuicao)=="object"){
							matrizDistribuicao.carregarGrid();
						}
						
						var disabled = $('#status_estudo').text()==='Liberado';    
						$('.reparteSugerido').attr('disabled','disabled');
					});
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			}
		});
    },
    
    getDadosLiberacao: function () {
       
    	var data = [];

        data.push({name: 'estudoId', value: $("#estudoId").val()});

        $("#baseEstudoGridParcial tr td input").each(function (key, element) {
            if (element.value > 0) {
                data.push({name: 'cotas[' + key + '].numeroCota', value: $(element).attr('numerocota')});
                data.push({name: 'cotas[' + key + '].reparte', value: element.value});
            }
        });

        return data;
    },
    
    preProcessGridNaoSelec : function(resultado) {
        $.each(resultado.rows, function(i, value) {
            if (typeof value.cell.nomeCota === 'undefined') {
                value.cell.nomeCota = '';
            }
            if (typeof value.cell.quantidade === 'undefined') {
                value.cell.quantidade = '';
            }

            var isReadOnly = (value.cell.siglaMotivo === 'CL' || value.cell.siglaMotivo === 'FN') ? 'readonly' : '';

            value.cell.quantidade = '<input type="text" motivo="' + value.cell.siglaMotivo + '" id="'+i+'" style="width: 50px;" value="'+
            value.cell.quantidade +'" onchange="analiseParcialController.validaMotivoCotaReparte(this);" ' + 
            ' situacaoCota="'+ value.cell.situacaoCota +'" ' + ' numeroCota="'+ value.cell.numeroCota +'" ' + isReadOnly + ' />';
        });
        
        if($("[id='dialog-cotas-estudos']", analiseParcialController.workspace).length > 1){
        	$("[id='dialog-cotas-estudos']:first", analiseParcialController.workspace).remove();		
        }
        
        $("[id='saldoReparteNaoSelec']").text($('#saldo_reparte').text());
        
        return resultado;
    },

    validaMotivoCotaReparte : function(input) {
        var $input = $(input);
        $input.data('valid', false);
        var situacaoCota;

        if ($input.val() !== '' && !isNaN($input.val()) && $input.val() > 0) {
        	
        	situacaoCota = $input.attr('situacaoCota');
        	
        	if(situacaoCota === 'SUSPENSO'){
        		
        		$("#dialog-confirmacao-cota-suspensa").dialog({
                    escondeHeader: false,
                    resizable : false,
                    modal : true,
                    buttons : {
                        "Confirmar" : function() {
                        	$(this).dialog("close");
                        },
                        "Cancelar" : function() {
                        	$input.val('');
                        	analiseParcialController.atualizaQuantidadeTotal($input);
                        	$(this).dialog("close");
                        	return;
                        }
                    },
                    open: function() {
                    	$(document.body).unbind("keydown");
                    	
                    	$("#dialog-confirmacao-cota-suspensa").keypress(function(e) {
                          if (e.keyCode == $.ui.keyCode.ENTER || e.keyCode == $.ui.keyCode.ESCAPE) {
                        	  e.preventDefault();
                        	  $(this).dialog("close");
                          }
                        });
                      },
                });
        	}
        	
        	var message = '';
        	
            switch ($input.attr('motivo')) {
                case 'SM': //Publicação não está no MIX da cota
                    message = 'Deseja incluir pulicação no mix?';
                    break;
                case 'GN': //Cota não recebe esse Segmento
                    message = 'Deseja incluir publicação na lista de publicações recebida pela cota?';
                    break;
                case 'SS': //Cota Suspensa
                    message = 'Cota suspensa, continuar mesmo assim?';
                    break;
                default:
                    analiseParcialController.atualizaQuantidadeTotal($input);
                	return;
            }

            usuarioController.supervisor.verificarRoleSupervisao({
            	optionalDialogMessage: message,
            	callbacks: {
    				usuarioSupervisorCallback: function() { 
    					analiseParcialController.atualizaQuantidadeTotal($input); 
    				}
    			}
            });
            
        } else if ($input.val() === '' || $input.val() == 0) {
        	analiseParcialController.exibirMsg('WARNING', ['Esse não é um reparte válido! Essa cota não será inserida na distribuição.']);
            analiseParcialController.atualizaQuantidadeTotal($input);
        } else {
            $input.val('');
        }
    },

    atualizaQuantidadeTotal : function($input) {
    	var $saldoReparteNaoSelec = $("#dialog-cotas-estudos").find("[id='saldoReparteNaoSelec']");
        var saldo = parseInt($saldoReparteNaoSelec.html(), 10);

        var valorDigitado = parseInt($input.val(), 10);
        valorDigitado = isNaN(valorDigitado)?0:valorDigitado;

        var valorAnterior = parseInt($input.data('valor-anterior'), 10);
        valorAnterior = isNaN(valorAnterior)?0:valorAnterior;

        var variacao = valorDigitado - valorAnterior;

        saldo = saldo - variacao;
        $saldoReparteNaoSelec.text(saldo);

        $input.data('valor-anterior', $input.val());
        $input.data('valid', true);
    },
    
    
    cotasQueNaoEntraramNoEstudo : function() {

		var cota = $("#cotasQueNaoEntraramNoEstudo_cota").val();
        var nome = $("#cotasQueNaoEntraramNoEstudo_nome").val();
        var motivo = $("#cotasQueNaoEntraramNoEstudo_motivo").val();
        var elemento = $("#cotasQueNaoEntraramNoEstudo_elementos").val();
        var estudo = $("#estudoId").val();
        var tipoSegmentoProduto = $("#tipoSegmentoProduto").val();

        $("#cotasNaoSelec").flexOptions({
            params:[{name: 'queryDTO.cota',     value: cota}, 
                    {name: 'queryDTO.nome',     value: nome}, 
                    {name: 'queryDTO.motivo',   value: motivo}, 
                    {name: 'queryDTO.elemento', value: elemento}, 
                    {name: 'queryDTO.estudo',   value: estudo},
                    {name: 'queryDTO.tipoSegmentoProduto',   value: tipoSegmentoProduto}]
        }).flexReload();
        
        setTimeout(function(){
    		if($('#status_estudo').text()==='Liberado'){
    			analiseParcialController.bloquearInputCotasQueNaoEntraramNoEstudo();
			}},2000);
    },
    
    verCapa : function() {
        $("#dialog-detalhes").dialog({
            escondeHeader: false,
            resizable: false,
            height:'auto',
            width:'auto',
            modal: false
        });
    },

    getCotasComReparte: function () {
        var data = [];
        var estudoId = $("#estudoId").val();

        data.push({name: 'estudoId', value: estudoId});

        $("#cotasNaoSelec tr td input").each(function (key, element) {
            if (element.value > 0) {
                data.push({name: 'cotas[' + key + '].numeroCota', value: $(element).attr('numerocota')});
                data.push({name: 'cotas[' + key + '].quantidade', value: element.value});
                data.push({name: 'cotas[' + key + '].motivo', value: $(element).attr('motivo')});
            }
        });

        return data;
    },
    
    postMudarReparteLote: function () {
        $.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/mudarReparteLote',
            analiseParcialController.getCotasComReparte(),
            function (result) {
        		
	        	if (result.mensagens) {
	        		
	        		analiseParcialController.exibirMsg(
	        			result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
	        		
	        		return;
	        	}
        	
                $('#baseEstudoGridParcial').flexReload();
                $('#saldo_reparte').text($('#saldoReparteNaoSelec').text());
                analiseParcialController.atualizaAbrangencia();
            });
    },

    verificarPermissaoAcesso:function(funcao){
		
		var	url = analiseParcialController.path + '/distribuicao/analise/parcial/validar';

		$.postJSON(url,null,function(result) {
			funcao();
		},null,true);
		
	},
    
    exibirCotasQueNaoEntraramNoEstudo : function() {
        
    	analiseParcialController.verificarPermissaoAcesso(analiseParcialController.exibirModalCotasQueNaoEntraramNoEstudo);
    },
    
    bloquearInputCotasQueNaoEntraramNoEstudo : function() {
	    var inputsCotasNaoSelecionadas = $("#cotasNaoSelec tr td input");
	
	    inputsCotasNaoSelecionadas.each(function () {
	        $(this).disable();
	    });
    },
    
    exibirModalCotasQueNaoEntraramNoEstudo:function(){
    	
    	 //limpa os campos ao abrir o pop-up
        $("#cotasQueNaoEntraramNoEstudo_cota").val('');
        $("#cotasQueNaoEntraramNoEstudo_nome").val('');
        $("#cotasQueNaoEntraramNoEstudo_motivo").val('');
        $("#cotasQueNaoEntraramNoEstudo_elementos").val('');

        analiseParcialController.cotasQueNaoEntraramNoEstudo();
        
        $("#dialog-cotas-estudos").dialog({
            escondeHeader: false,
            resizable : false,
            height : 530,
            width : 550,
            modal : true,
            buttons : {
                "Confirmar" : function() {
                    var $inputsPreenchidos = $("#cotasNaoSelec tr td input").filter(function(){return this.value > 0;});
                    var isValid = true;
                    $inputsPreenchidos.each(function () {
                        if ($(this).data('valid') === false) {
                            isValid = false;
                            return false;
                        }
                    });

                    if ($inputsPreenchidos.length > 0 && isValid) {
                    	
//                    	if ($inputsPreenchidos.filter('[motivo="SS"]').length > 0) {
//                    		
//                    	}
                    	
                        if ($inputsPreenchidos.filter('[motivo="SM"]').length > 0) {

                            var params = [];
                            var codigoProduto = $('#codigoProduto').text();
                            
                            if(codigoProduto == ""){
                            	codigoProduto = $('#codigoProduto').val();
                            }
                            
                            var classificacao = $('#tipoClassificacaoProdutoDescricao').val();
                            
                            if(classificacao == ""){
                            	classificacao = $('#tipoClassificacaoProdutoDescricao').text();
                            }

                            params.push({name: 'produtoId', value: codigoProduto});

                            $inputsPreenchidos.filter('[motivo="SM"]').each(function(key){
                                var $this = $(this);
                                params.push({name: 'listaNovosMixProduto[' + key + '].numeroCota', value: $this.attr('numerocota')});
                                params.push({name: 'listaNovosMixProduto[' + key + '].codigoProduto', value: codigoProduto});
                                params.push({name: 'listaNovosMixProduto[' + key + '].classificacaoProduto', value: classificacao});
                                params.push({name: 'listaNovosMixProduto[' + key + '].reparteMinimo', value: $this.val()});
                                params.push({name: 'listaNovosMixProduto[' + key + '].reparteMaximo', value: $this.val()});
                            });

                            $.postJSON(analiseParcialController.path + '/distribuicao/mixCotaProduto/adicionarMixProduto', params);
                        }
                        if ($inputsPreenchidos.filter('[motivo="GN"]').length > 0) {

                            var params = [];
                            params.push({name: 'filtro.produtoDto.codigoProduto', value: $('#codigoProduto').text()});
                            params.push({name: 'filtro.produtoDto.idClassificacaoProduto', value: $('#tipoClassificacaoProdutoId').val()});
                            params.push({name: 'filtro.excecaoSegmento', value: true});

                            $inputsPreenchidos.filter('[motivo="GN"]').each(function(key){
                                var $this = $(this);
                                params.push({name: 'listaNumeroCota[' + key + ']', value: $this.attr('numerocota')});
                            });

                            $.postJSON(analiseParcialController.path + '/distribuicao/excecaoSegmentoParciais/inserirCotaNaExcecao', params);
                        }
                        analiseParcialController.postMudarReparteLote();
                        $(this).dialog("close");
                    }else{
                    	analiseParcialController.exibirMsg('WARNING', ['Não há cotas com reparte válido!']);
                    }
                },
                "Cancelar" : function() {
                	$(this).dialog("close");
                }
            },
            open: function() {
            	$(document.body).unbind("keydown");
            	
            	$("#dialog-cotas-estudos").keypress(function(e) {
                  if (e.keyCode == $.ui.keyCode.ENTER) {
                	  e.preventDefault();
                	  
                	  var index = e.srcElement.attributes.id.value;
                	  
                	  index = parseInt(index)+1;

                	  if(index < $("#cotasNaoSelec tr td input",  analiseParcialController.workspace).size()){
                		  $("#"+index).focus();
                	  }else{
                		  $(".ui-dialog:visible").find("button").first().focus();
                	  }
                  }
                });
            	setTimeout(function(){
            		if($('#status_estudo').text()==='Liberado'){
            			analiseParcialController.bloquearInputCotasQueNaoEntraramNoEstudo();
        			}},2000);
              },
	        beforeClose: function() {
	        	$("#cotasNaoSelec tr td input",  analiseParcialController.workspace).filter(function(){return this.value > 0;}).remove();
	        	$(this, analiseParcialController.workspace).dialog("destroy");
	        },
        });
    },
    
    selecionarElementos : function(tipo, optionsId) {
        if (tipo !== '') {
            $.getJSON(analiseParcialController.path +"/componentes/elementos", [{name: "tipo", value: tipo}, {name: "estudo", value: $("#estudoId").val()}], function(data) {
                var options = $("#" + optionsId + ":visible");
                options.empty();
                options.append($("<option />").val('').text('Selecione...'));
                $.each(data, function() {
                    if (this.tipo === 'bairro') {
                        options.append($("<option />").val(this.tipo +"_"+ this.value).text(this.value));
                    } else {
                        options.append($("<option />").val(this.tipo +"_"+ this.id).text(this.value));
                    }
                });
            });
        } else {
            var options = $("#"+ optionsId);
            options.empty();
            options.append($("<option />").val('').text('Selecione...'));
        }
    },

    apresentarOpcoesOrdenarPor : function(opcao) {
        analiseParcialController.limparCamposDeAte();
        if (opcao === "") {
            $("#opcoesOrdenarPor").hide();
        } else {
            $(".label").hide();
            $("#label_"+ opcao).show();
            $("#labelAte_"+ opcao).show();
            $("#opcoesOrdenarPor").show();
        }
    },

    limparCamposDeAte: function () {
        $("#faixaDe")
        .add("#faixaAte")
        .add("#ordenarPorDe")
        .add("#ordenarPorAte")
        .val('');
    },
    
//    addEventoLegenda: function () {
//        $('.asterisco').mouseenter(function (event){
//        	$('.asterisco').tipsy({live: true, gravity: 'sw', title: function(){return 'Reparte Alterado';}});
//        	setTimeout(function(){$(".tipsy").hide()},2000);
//        });
//    },

    filtrarOrdenarPor : function(estudo) {
        
    	var valueFiltroOrdenarPor = $("#filtroOrdenarPor").val();
        var elemento = $("#elementos :selected").val();

        switch (valueFiltroOrdenarPor) {
            case 'numero_cota':
                analiseParcialController.filtrarCotas(estudo, valueFiltroOrdenarPor, elemento);
                break;
            case 'percentual_de_venda':
                analiseParcialController.filtrarOrdenarPercentualVenda(estudo, valueFiltroOrdenarPor, elemento);
                break;
            case 'reducao_de_reparte':
                analiseParcialController.filtrarOrdenarReducaoReparte(estudo, valueFiltroOrdenarPor, elemento);
                break;
            default:
            	analiseParcialController.filtroDefault(estudo, valueFiltroOrdenarPor, elemento);
        }
        if (event) {
            event.preventDefault();
        }
        return false;
    },
    
    filtrarOrdenarPorElemento : function(estudo) {
    	
    	$('#filtroOrdenarPor option:eq(0)').prop('selected', true).parent().change();
        
    	var valueFiltroOrdenarPor = $("#filtroOrdenarPor").val();
        var elemento = $("#elementos :selected").val();

    	analiseParcialController.filtroDefault(estudo, valueFiltroOrdenarPor, elemento);
    
    	if (event) {
            event.preventDefault();
        }
    	
    	$('#filtroOrdenarPor option:eq(1)').prop('selected', true).parent().change();
        
    	return false;
    },
    
    filtroDefault: function (estudo, valueFiltroOrdenarPor, elemento) {

        $("#baseEstudoGridParcial").flexOptions({
            params: [{name:'filterSortName', value: valueFiltroOrdenarPor},
                {name:'filterSortFrom', value: $("#ordenarPorDe").val()},
                {name:'filterSortTo',   value: $("#ordenarPorAte").val()},
                {name:'id',             value: estudo},
                {name:'elemento',       value: elemento}]
        }).flexReload();

    },

    filtrarCotas: function (estudo, valueFiltroOrdenarPor, elemento) {
        if ($("#ordenarPorDe").val() === '' && $("#ordenarPorAte").val() === '') {

        	$('#baseEstudoGridParcial tr').show().filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
            
        	analiseParcialController.filtroDefault(estudo, valueFiltroOrdenarPor, elemento);
        	
        } else {
            var de = $("#ordenarPorDe").val() * 1;
            var ate = $("#ordenarPorAte").val() * 1;

            var sortAtribute = 'td[abbr="cota"]';

            $('#baseEstudoGridParcial tr')
                .removeClass('erow')
                .each(function(){
                    var $tr = $(this);
                    var cota = $tr.find(sortAtribute).text() * 1;

                    $tr.hide();
                    if (de === 0 || ate === 0) {
                        if (de === cota || ate === cota) {
                            $tr.show();
                        }
                    } else {
                        if ((de < ate) && (de <= cota && cota <= ate)) {
                            $tr.show();
                        } else if (de >= cota && cota >= ate) {
                            $tr.show();
                        }
                    }

                })
                .filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
        }
        analiseParcialController.somarTotais();
    },

    filtrarTipoDistribuicaoCota: function (tipo) {
        $.post(contextPath + '/distribuicao/analise/parcial/tipoDistribuicaoCotaFiltro', {tipo:tipo.toUpperCase()},
            function(resp) {
                var sortAtribute = 'td[abbr="cota"]';

                $('#baseEstudoGridParcial tr')
                    .removeClass('erow')
                    .each(function(){
                        var $tr = $(this);
                        var cota = $tr.find(sortAtribute).text() * 1;

                        $tr.hide();
                        if (resp.indexOf(cota) != -1) {
                            $tr.show();
                        }
                    })
                    .filter(':odd').addClass('erow')
                    .end().find('td').removeClass('sorted');
                analiseParcialController.somarTotais();
            });
    },

    filtrarOrdenarPercentualVenda : function(estudo, valueFiltroOrdenarPor, elemento) {
        if ($("#ordenarPorDe").val() === '' || $("#ordenarPorAte").val() === '') {
            
        	$('#baseEstudoGridParcial tr').show().filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
            
        	analiseParcialController.filtroDefault(estudo, valueFiltroOrdenarPor, elemento);
            
        } else {
            var de = $("#ordenarPorDe").val() * 1;
            var ate = $("#ordenarPorAte").val() * 1;

            var settings = {order: de<ate?'asc':'desc', attr: 'percentualVenda'};
            var sortAtribute = 'td[abbr="cota"]';

            $('#baseEstudoGridParcial tr')
                .tsort(sortAtribute, settings)
                .removeClass('erow')
                .each(function(){
                    var $tr = $(this);
                    var perc = Math.round($('#baseEstudoGridParcial td[abbr="reparteSugerido"] input[numerocota="' + $tr.find('td[abbr="cota"]').text() + '"]').attr('percentualvenda'));
                    if (de < ate) {
                        if (de <= perc && perc <= ate) {
                            $tr.show();
                        } else {
                            $tr.hide();
                        }
                    } else {
                        if (de >= perc && perc >= ate) {
                            $tr.show();
                        } else {
                            $tr.hide();
                        }
                    }
                })
                .filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
        }
        analiseParcialController.somarTotais();
    },

    filtrarOrdenarReducaoReparte : function(estudo, valueFiltroOrdenarPor, elemento) {
        if ($("#ordenarPorDe").val() === '' || $("#ordenarPorAte").val() === '') {
            
        	$('#baseEstudoGridParcial tr').show().filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
            
            analiseParcialController.filtroDefault(estudo, valueFiltroOrdenarPor, elemento);
            
        } else {
            var de = $("#ordenarPorDe").val() * 1;
            var ate = $("#ordenarPorAte").val() * 1;

            var settings = {order: de<ate?'asc':'desc', attr: 'reducaoReparte'};
            var sortAtribute = 'td[abbr="reparteSugerido"] div input';

            $('#baseEstudoGridParcial tr')
                .tsort(sortAtribute, settings)
                .removeClass('erow')
                .each(function(){
                    var $tr = $(this);
                    var rr = $tr.find(sortAtribute).attr('reducaoReparte') * 1;
                    if (de < ate) {
                        if (de <= rr && rr <= ate) {
                            $tr.show();
                        } else {
                            $tr.hide();
                        }
                    } else {
                        if (de >= rr && rr >= ate) {
                            $tr.show();
                        } else {
                            $tr.hide();
                        }
                    }
                })
                .filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
        }
        analiseParcialController.somarTotais();
    },

    filtrarCotasNaoSelec : function(estudo) {
        analiseParcialController.cotasQueNaoEntraramNoEstudo();
        if (event) {
            event.preventDefault();
        }
        return false;
    },
    
    atualizaRepartePDV : function(){
    	
    	var $repartesPDV = $('#AP_dialog-defineReparte', analiseParcialController.workspace).find('table.pdvCotaGrid_AP tr input.repartePDV');
        
        var totalRepartePDVs = $repartesPDV.map(function () {
            return parseInt(this.value, 10);
        }).toArray().reduce(function (a, b) {
                return a + b;
            });
    	
    	$('#reparteCota', analiseParcialController.workspace).text(totalRepartePDVs);
    	
    },
    
    prepararDetalhesEdicoesBases : function(){
    	
    	$('.detalhesDados-analiseParcial').show();
    	
    	// tr codProduto
		$("#analiseParcialPopUpCodProduto", analiseParcialController.workspace).clear();
		$("#analiseParcialPopUpCodProduto", analiseParcialController.workspace)
		.append('<td class="class_linha_1"><strong>Código:</strong></td>');
    	
		// tr nomeProduto
		$("#analiseParcialPopUpNomeProduto", analiseParcialController.workspace).clear();
		$("#analiseParcialPopUpNomeProduto", analiseParcialController.workspace)
		.append('<td class="class_linha_1"><strong>Produto:</strong></td>');
		
		// tr numeroEdicao
		$('#analiseParcialPopUpNumeroEdicao', analiseParcialController.workspace).html('')
		.append('<td class="class_linha_1"><strong>Edição:</strong></td>');
		
		// tr dataLancamento
		$('#analiseParcialPopUpDatalancamento', analiseParcialController.workspace).html('')
		.append('<td width="136" class="class_linha_2"><strong>Data Lançamento:</strong></td>');
		
		// tr reparte
		$('#analiseParcialPopUpReparte', analiseParcialController.workspace).html('')
		.append('<td class="class_linha_1"><strong>Reparte:</strong></td>');
		
		// tr venda
		$('#analiseParcialPopUpVenda', analiseParcialController.workspace).html('')
		.append('<td class="class_linha_2"><strong>Venda:</strong></td>');
		
		for(var y = 0; y < analiseParcialController.detalheEdicoesBases.length; y++){
			detalheEdicao = analiseParcialController.detalheEdicoesBases[y];
			
			var reparte;
			var venda;
			
			var nomeProduto = detalheEdicao.nomeProduto != undefined ? detalheEdicao.nomeProduto : "";
			
			var novoNome = nomeProduto.split('.');
			
			var joinNome = '';
			
			if(novoNome.length > 1){
				for ( i=0; i < novoNome.length; i++ ) {
					joinNome += novoNome[i] + '. ';
				}
			}else{
				joinNome = novoNome;
			}
			
			if(detalheEdicao.reparte != undefined){
				reparte = detalheEdicao.reparte != 0 ? detalheEdicao.reparte : '';
			}
			
			if(detalheEdicao.venda != undefined){
				venda = detalheEdicao.venda != 0 ? detalheEdicao.venda : '';
			}
			
				$("#analiseParcialPopUpCodProduto", analiseParcialController.workspace).append(
				'<td class="class_linha_1">'+(detalheEdicao.codigoProduto != undefined ? detalheEdicao.codigoProduto : "")+'</td>');
		
				$("#analiseParcialPopUpNomeProduto", analiseParcialController.workspace).append(
						'<td class="class_linha_1">'+(joinNome)+'</td>');
				
				$("#analiseParcialPopUpNumeroEdicao", analiseParcialController.workspace).append(
						'<td class="class_linha_1">'+(detalheEdicao.edicao != undefined ? detalheEdicao.edicao : "")+'</td>');
				
				$("#analiseParcialPopUpDatalancamento", analiseParcialController.workspace).append(
						'<td width="130" align="center" class="class_linha_2">' + (detalheEdicao.dataLancamento != undefined ? detalheEdicao.dataLancamento : "") + '</td>');

	 			$("#analiseParcialPopUpReparte", analiseParcialController.workspace).append(
					'<td align="right" class="class_linha_1">' + (reparte) +'</td>');
                
                $("#analiseParcialPopUpVenda", analiseParcialController.workspace).append(
    					'<td align="right" class="class_linha_1">' + (venda) + '</td>');
		}
		
		// por estética de layout, insiro elementos td vazios
		for ( var it = 0; it < 6 - edicoesBaseDadosEdicoes.length; it++) {
			$("#analiseParcialPopUpCodProduto", analiseParcialController.workspace).append(
					'<td class="class_linha_1"></td>');
			
			$("#analiseParcialPopUpNomeProduto", analiseParcialController.workspace).append(
					'<td class="class_linha_1"></td>');
			
			$("#analiseParcialPopUpNumeroEdicao", analiseParcialController.workspace).append(
					'<td class="class_linha_1"></td>');
			
			$("#analiseParcialPopUpDatalancamento", analiseParcialController.workspace).append(
					'<td width="130" align="center" class="class_linha_2"></td>');
			
			$("#analiseParcialPopUpReparte", analiseParcialController.workspace).append(
					'<td align="right" class="class_linha_1"></td>');
			
			$("#analiseParcialPopUpVenda", analiseParcialController.workspace).append(
					'<td align="right" class="class_linha_1"></td>');
		}
					
	},
	
	montarDadosDetalhesEdicoesBases : function(){
		var parameters = [];
		
		for (var i = 0; i < analiseParcialController.edicoesBase.length; i++) {
			var row = analiseParcialController.edicoesBase[i];
 			
			
			parameters.push({name: 'edicoesBase['+ i +'].codigoProduto',   value: analiseParcialController.validarParametros(row.codigoProduto)});
            parameters.push({name: 'edicoesBase['+ i +'].nomeProduto',     value: analiseParcialController.validarParametros(row.nomeProduto)});
            parameters.push({name: 'edicoesBase['+ i +'].edicao',      	   value: analiseParcialController.validarParametros(row.edicao)});
            parameters.push({name: 'edicoesBase['+ i +'].dataLancamento',  value: analiseParcialController.validarParametros(row.dataLancamento)});
            parameters.push({name: 'edicoesBase['+ i +'].reparte',         value: 0});
            parameters.push({name: 'edicoesBase['+ i +'].venda',           value: 0});
            parameters.push({name: 'edicoesBase['+ i +'].ordemExibicao',   value: analiseParcialController.validarParametros(row.ordemExibicao)});
            parameters.push({name: 'edicoesBase['+ i +'].produtoEdicaoId', value: analiseParcialController.validarParametros(row.produtoEdicaoId)});
            parameters.push({name: 'edicoesBase['+ i +'].periodo',   	   value: row.periodo != undefined ? row.periodo : 'null'});
            
			
		}
		
		parameters.push({name: 'estudoId', value: $('#estudoId').val()});
		
		 $.post(analiseParcialController.path + '/distribuicao/analise/parcial/reparteTotalEVendaTotalPorEdicao',
		            parameters,
		             function (result) {
			 			analiseParcialController.detalheEdicoesBases = '';
			 			analiseParcialController.detalheEdicoesBases = result;
		 				analiseParcialController.prepararDetalhesEdicoesBases();
		            });
	},
	
	validarParametros : function(parametro){
		if(parametro == undefined || parametro == null){
			return '';
		}else{
			return parametro;
		}
		
	}
	
	
    
});

$(".cotasDetalhesGrid").flexigrid({
    url: analiseParcialController.path +'/distribuicao/analise/parcial/carregarDetalhesPdv',
    dataType : 'json',
    autoload: false,
    colModel : [{display: 'Código',   name: 'id',                     width: 40,  sortable: true, align: 'left'},
                {display: 'Tipo',     name: 'descricaoTipoPontoPDV',  width: 90,  sortable: true, align: 'left'},
                {display: '% Fat.',   name: 'porcentagemFaturamento', width: 30,  sortable: true, align: 'right'},
                {display: 'Princ.',   name: 'principal',              width: 30,  sortable: true, align: 'center'},
                {display: 'Endereço', name: 'endereco',               width: 420, sortable: true, align: 'left'}],
    width : 690,
    height : 200,
    preProcess: function(result) {

        var defaultCell = {
            id:'',
            descricaoTipoPontoPDV:'',
            porcentagemFaturamento:'',
            principal:'',
            endereco:''
        };

        for (var i=0; i<result.rows.length; i++) {
            var cell = result.rows[i].cell;
            result.rows[i].cell = $.extend({}, defaultCell, cell);

            if (cell.principal === true) {
                result.rows[i].cell.principal = 'S';
            } else {
                result.rows[i].cell.principal = 'N';
            }
        }

        return result;
    }
});

$(".pdvCotaGrid_AP", analiseParcialController.workspace).flexigrid({
    url: analiseParcialController.path +'/distribuicao/analise/parcial/carregarDetalhesPdv',
    dataType : 'json',
    autoload: false,
    colModel : [
        { display: 'Código',    name: 'id',         width: 50,  sortable: true, align: 'left' },
        { display: 'Nome PDV',  name: 'nomePDV',    width: 120, sortable: true, align: 'left' },
        { display: 'Endereço',  name: 'endereco',   width: 320, sortable: true, align: 'left' },
        { display: 'Reparte',   name: 'reparte',    width: 50,  sortable: true, align: 'center' } ],
    sortname : "codigo",
    sortorder : "asc",
    usepager : true,
    useRp : true,
    rp : 15,
    showTableToggleBtn : true,
    width : 600,
    height : 200,
    preProcess: function(result) {

        var defaultCell = {
            id:'',
            nomePdv:'',
            endereco:'',
            reparte:''
        };

        for (var i=0; i<result.rows.length; i++) {
            
        	var cell = result.rows[i].cell;
            result.rows[i].cell = $.extend({}, defaultCell, cell);
            
        	result.rows[i].cell.reparte = "<input class='repartePDV' value='"+(result.rows[i].cell.reparte || '0')+"' name='repartePDV' id='repartePDV"+i+"' onchange='analiseParcialController.atualizaRepartePDV();' >"
        }

        return result;
    }
});

$("#prodCadastradosGrid").flexigrid({
    colModel : [{display: 'Código',        name: 'codigo',        width: 80,  sortable: false, align: 'left'},
                {display: 'Produto',       name: 'produto',       width: 180, sortable: false, align: 'left'},
                {display: 'Edição',        name: 'edicao',        width: 80,  sortable: false, align: 'left'},
                {display: 'Classificação', name: 'classificacao', width: 120,  sortable: false, align: 'left'},
                {display: 'Açao',          name: 'acao',          width: 70,  sortable: false, align: 'center'}],
    dataType: 'json',
    striped: false,
    width : 595,
    height : 225
});

$("#edicaoProdCadastradosGrid").flexigrid({
    colModel: [ {display: 'Id Edição',     name: 'id',                      width: 1,   sortable: true, hide: true},
                {display: 'Id Class.',     name: 'idClassificacao',         width: 1,   sortable: true, hide: true},
                {display: 'Cod Produto',   name: 'codigoProduto',           width: 80,  sortable: true, align: 'left'},
                {display: 'Classificação', name: 'classificacao',           width: 95,   sortable: true, align: 'center'},
                {display: 'Edição',        name: 'numeroEdicao',            width: 45,  sortable: true, align: 'left'},
                {display: 'Data Lancto',   name: 'dataLancamentoFormatada', width: 100, sortable: true, align: 'center'},
                {display: 'Reparte',       name: 'reparte',                 width: 40,  sortable: true, align: 'right'},
                {display: 'Venda',         name: 'venda',                   width: 40,  sortable: true, align: 'right'},
                {display: 'Status',        name: 'status',                  width: 110, sortable: true, align: 'left'},
                {display: 'Capa',          name: 'capa',                    width: 30,  sortable: true, align: 'center'},
                {display: '',              name: 'sel',                     width: 30,  sortable: true, align: 'center'}],
    url: analiseParcialController.path +'/distribuicao/analise/parcial/pesquisarProdutoEdicao',
    dataType: 'json',
    disableSelect: true,
    autoload: false,
    preProcess: function(result) {

        var defaultCell = {
            id:'',
            codigoProduto:'',
            classificacao:'',
            numeroEdicao:'',
            dataLancamentoFormatada:'',
            reparte:'',
            venda:'',
            status:'',
            capa:'<img src="images/ico_detalhes.png" alt="Capa Revista" class="icoCapaEB">',
            sel:'<input type="checkbox" class="edicaoSelecao"/>'
        };

        for (var i=0; i<result.rows.length; i++) {
            var cell = result.rows[i].cell;
            result.rows[i].cell = $.extend({}, defaultCell, cell);
        }

        return result;
    },
    width : 700,
    height : 200
});

function popup_edicoes_produto() {
    $('#edicaoProdCadastradosGrid tbody').remove();
    $("#dialog-edicoes-produtos input").val('');
    
    $("#dialog-edicoes-produtos").dialog({
        escondeHeader: false,
        resizable : false,
        height : 420,
        width : 750,
        modal : true,
        buttons : {
            "Confirmar" : function() {
                var $tbody = $('#dialog-mudar-base #prodCadastradosGrid tbody');
                var $edicaoSelecao = $("#edicaoProdCadastradosGrid input.edicaoSelecao:checked");
                var tipoClassificacao = $('#filtroClassificacao').html().replace('selected="selected"', '');
                var optionClassificacao = 'value="id"';
                if (($tbody.find('tr').length + $edicaoSelecao.length) > 6) {
                    analiseParcialController.exibirMsg('WARNING', ['Não é possivel selecionar mais do que 6 edições no total!']);
                } else {
                    var tbodyAppend = '';
                    var modelRow = '<tr>' +
                        '<td align="left"><div style="text-align: left; width: 80px;"><input class="inputCodigoEB" value="#codigoProduto#"></div></td>' +
                        '<td align="left"><div style="text-align: left; width: 180px;"><input class="inputProdutoEB" value="#nomeProduto#"></div></td>' +
                        '<td align="left"><div style="text-align: left; width: 80px;"><input id="#idEdicao#" class="inputEdicaoEB" value="#numeroEdicao#"></div></td>' +
                        '<td align="left"><div style="text-align: left; width: 120px;"><select class="selectClassEB">#options#</select></div></td>' +
                        '<td align="center"><div style="text-align: center; width: 70px;">' +
                            '<img src="images/ico_editar.gif" alt="Alterar Edição" class="icoEditarEB">' +
                            '<img src="images/ico_excluir.gif" alt="Excluir Base" class="icoExcluirEB">' +
                            '<img src="images/ico_arrow_resize.png" alt="Mover Base" class="icoMoverEB">' +
                        '</div></td></tr>';
                    modelRow = modelRow.replace(/#nomeProduto#/, $('#inputNomeProduto').val());
                    $edicaoSelecao.each(function(key, value){
                        var $thisTR = $(this).closest('tr');
                        var idClassificacao = optionClassificacao.replace('id', $thisTR.find('td[abbr="idClassificacao"] div').text());
                        tbodyAppend += modelRow.replace(/#codigoProduto#/, $thisTR.find('td[abbr="codigoProduto"] div').text())
                                               .replace(/#idEdicao#/, $thisTR.find('td[abbr="id"] div').text())
                                               .replace(/#numeroEdicao#/, $thisTR.find('td[abbr="numeroEdicao"] div').text())
                                               .replace(/#options#/, tipoClassificacao.replace(idClassificacao, idClassificacao + ' selected'));
                    });
                    $tbody.append(tbodyAppend);
                    $(this).dialog("close");
                }
            },
            "Cancelar" : function() {
                $(this).dialog("close");
            }
        },
        form: $("#form-edicoes-produtos", this.workspace)
    });
};

//@ sourceURL=analiseParcial.js
