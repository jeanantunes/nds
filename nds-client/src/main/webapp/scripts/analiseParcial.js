var analiseParcialController = $.extend(true, {

    path: contextPath,

    baseInicialAnalise: null, //para voltar ao estado original da analise

    linkNomeCota : '<a tabindex="-1" class="linkNomeCota" numeroCota="#numeroCota" >#nomeCota</a>',
    edicoesBase : [],
    inputReparteSugerido: '<input #disabled reducaoReparte="#redReparte" reparteInicial="#repEstudo" reparteAtual="#value" numeroCota="#numeroCota" ajustado="#ajustado" quantidadeAjuste="#quantidadeAjuste" value="#value" class="reparteSugerido" />',
    tipoExibicao : 'NORMAL',

    exibirMsg: function(tipo, texto) {
        exibirMensagem(tipo, texto);
    },

    mudarBaseVisualizacao : function() {
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
             {name: 'codigoProduto', value: $('input[id="codigoProduto"]').val()}],
            function(result){

                var $dialog = $('#dialog-cotas-detalhes'),
                    $mix = $dialog.find('#dados-mix'),
                    $fixacao = $dialog.find('#dados-fixacao');
                $mix.hide();
                $fixacao.hide();

                $dialog.find('#numeroCotaD').text(result.numeroCota || '');
                $dialog.find('#nomeCotaD').text(result.nomePessoa || '');
                $dialog.find('#tipoCotaD').text(result.tipoCota || '');
                $dialog.find('#rankingCotaD').text(result.qtdeRankingSegmento || '');
                $dialog.find('#faturamentoCotaD').text(result.faturamento || '');
                $dialog.find('#mesAnoCotaD').text(result.dataGeracaoRank ? result.dataGeracaoRank.$ : '');

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
        }
    },

    carregarEdicoesBaseEstudo : function(estudoId) {
        var edicoesJaCarregadas = false;
        for (var i = 0; i < 6; i++) {
            if (typeof analiseParcialController.edicoesBase[i] !== 'undefined') {
                edicoesJaCarregadas = true;
                break;
            }
        }
        if (!edicoesJaCarregadas) {
            $.postJSON(analiseParcialController.path +'/distribuicao/analise/parcial/carregarEdicoesBaseEstudo',
                    [{name: 'estudoId', value: estudoId}],
                    function(resultado) {
                        if (typeof resultado.edicoesBase !== 'undefined' && resultado.edicoesBase.length > 0) {
                            analiseParcialController.edicoesBase = resultado.edicoesBase;
                        }
                    });
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
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('3ª Parcial')))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('2ª Parcial')))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('1ª Parcial')))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('Acumulado')))
                );
            }
        }, 0);
    },

    atualizaReparte : function(input) {
    	
    	if (!$('#saldo_reparte').val() || $('#saldo_reparte').val() == ""){
    		
    		$('#saldo_reparte').val(0);
    	}
    	
        var $saldoreparte = $('#saldo_reparte');
        var saldoReparte = parseInt($saldoreparte.val());
        var $input_reparte = $(input);
        var numeroCota = $input_reparte.attr('numeroCota');
        var reparteDigitado = $input_reparte.val();
        var reparteAtual = $input_reparte.attr('reparteAtual');
        var reparteSubtraido = parseInt(reparteDigitado, 10) - parseInt(reparteAtual, 10);
        var $legenda = $input_reparte.closest('td').next().find('div');
        
        if (reparteAtual != reparteDigitado) {
            var legendaText = $legenda.text();
            if (legendaText.indexOf('FX') > -1 || legendaText.indexOf('MX') > -1) {
                var senha = prompt('É necessario confirmar esta ação com senha.');
                if (senha !== 'D68') {
                    return;
                }
            }
            
            var saldoReparteAtualizado = parseInt($saldoreparte.val(), 10) - reparteSubtraido;
            
            $saldoreparte.text(saldoReparteAtualizado);
            
            $saldoreparte.val(saldoReparteAtualizado);

            $.ajax({url: analiseParcialController.path +'/distribuicao/analise/parcial/mudarReparte',
                data: {'numeroCota': numeroCota, 'estudoId': $('#estudoId').val(), 'variacaoDoReparte': reparteSubtraido},
                success: function() {
                	analiseParcialController.atualizaAbrangencia();
                    $input_reparte.attr('reparteAtual', reparteDigitado);
                    var reparteInicial = $input_reparte.attr('reparteInicial');
                    $input_reparte.attr('reducaoReparte', analiseParcialController.calculaPercentualReducaoReparte(reparteInicial, reparteDigitado));
                    if (reparteDigitado === reparteInicial) {
                        $legenda.removeClass('asterisco');
                    } else {
                        $legenda.addClass('asterisco');
                    }

                    $('#total_reparte_sugerido')
                        .text(
                            $('#baseEstudoGridParcial tr td input:text').map(function(){
                                return parseInt(this.value, 10);
                            }).toArray().reduce(function(a,b){
                                    return a+b;
                                }));

                    if (typeof histogramaPosEstudoController != 'undefined') {
                        //tenta atualizar os valores da tela de histograma pré analise
                        try{
                            histogramaPosEstudoController.Grids.EstudosAnaliseGrid.reload({
                                params : [{ name : 'estudoId' , value : $('#estudoId').val()}]
                            });
                            //histogramaPosEstudoController.popularFieldsetResumoEstudo();
                        }catch(e){
                            exibirMensagem('WARNING', [e.message]);
                        }
                    }
                },
                error: function() {
                    analiseParcialController.exibirMsg('WARNING', ['Erro ao enviar novo reparte!']);
                    $input_reparte.val($input_reparte.attr('reparteAtual'));
                }
            });
        }
    },

    calculaPercentualReducaoReparte: function (reparteEstudo, reparteSugerido) {
        var repEstudo = isNaN(reparteEstudo)?0:reparteEstudo;
        var repSugerido = isNaN(reparteSugerido)?0:reparteSugerido;
        var reducaoReparte = Math.abs(repEstudo - repSugerido);
        return Math.round((reducaoReparte / repEstudo) * 10000) / 100;
    },

    restauraBaseInicial : function() {
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
            var input = analiseParcialController.inputReparteSugerido.toString()
                            .replace(/#numeroCota/g, numCota)
                            .replace(/#value/g, cell.reparteSugerido)
                            .replace(/#repEstudo/g, cell.reparteEstudo)
                            .replace(/#disabled/g, disabled ? 'disabled':'')
                            .replace(/#ajustado/g, cell.ajustado)
                            .replace(/#quantidadeAjuste/g, cell.quantidadeAjuste)
                            .replace(/#redReparte/g, analiseParcialController.calculaPercentualReducaoReparte(cell.reparteEstudo, cell.reparteSugerido));
            cell.reparteSugerido = input;
            
            cell.nome = analiseParcialController.linkNomeCota.replace('#nomeCota', cell.nome)
                            .replace('#numeroCota', cell.cota);

            if (typeof cell.classificacao === 'undefined') {
                cell.classificacao = '';
            }
            if (cell.cotaNova == true) {
                cell.cota += '<span class="asteriscoCotaNova"></span>';
            }
            if (cell.npdv > 1) {
                cell.npdv = '<a tabindex="-1" class="editaRepartePorPDV" numeroCota="'+ numCota +'">'+ cell.npdv +'</a>';
            }
            if (cell.leg === 'S') {
                cell.leg = '';
            }
            if (cell.leg !== '') {
                cell.leg = '<span class="legendas" id="leg_'+ numCota +'" title="'+ cell.descricaoLegenda +'">'+ cell.leg +'</span>';
            }
            if (cell.juramento == 0) {
                cell.juramento = '';
            }

            for (var j = 0; j < 6; j++) {
                if (typeof cell.edicoesBase[j] === 'undefined' || typeof cell.edicoesBase[j].reparte === 'undefined' || cell.edicoesBase[j].reparte == 0) {
                    cell['reparte'+ (j + 1)] = '';
                    cell['venda'+ (j + 1)] = '';
                } else {
                    cell['reparte'+ (j + 1)] = cell.edicoesBase[j].reparte;
                    cell['venda'+ (j + 1)] = cell.edicoesBase[j].venda || 0;
                }
            }   
            
            totalSaldoReparte += parseInt(cell.quantidadeAjuste);
        }
        
        $("#saldo_reparte").val(totalSaldoReparte);
        
        $("#saldo_reparte").text(totalSaldoReparte);
        
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

    carregaDetalhesEdicoesBase: function () {
        if (analiseParcialController.edicoesBase.length > 0) {
            var param = [];
            $.each(analiseParcialController.edicoesBase, function (key, value) {
                param.push({name: 'produtoEdicaoList['+key+'].idProdutoEdicao', value: value.produtoEdicaoId});
                param.push({name: 'produtoEdicaoList['+key+'].parcial', value: analiseParcialController.tipoExibicao == 'PARCIAL'});
                if (typeof value.ordemExibicao != 'undefined') {
                    param.push({name: 'produtoEdicaoList['+key+'].ordemExibicao', value: value.ordemExibicao});
                }
                if (typeof value.periodo != 'undefined') {
                    param.push({name: 'produtoEdicaoList['+key+'].numeroPeriodo', value: value.periodo});
                }
            });
            $.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/historicoEdicoesBase', param, function (result) {
                var $rows = $('#tabelaDetalheAnalise tr');
                var rowCodigoProduto = $rows.eq(0).find('td');
                var rowNomeProduto = $rows.eq(1).find('td');
                var rowNumeroEdicao = $rows.eq(2).find('td');
                var rowDataLancamento = $rows.eq(3).find('td');
                var rowReparte = $rows.eq(4).find('td');
                var rowVenda = $rows.eq(5).find('td');
                $.each(analiseParcialController.edicoesBase, function (key, value) {
                    rowCodigoProduto.eq(key+1).text(value.codigoProduto);
                    rowNomeProduto.eq(key+1).text(value.nomeProduto);
                    rowNumeroEdicao.eq(key+1).text(value.edicao + (result[key].parcial ? ' / Período: ' + result[key].numeroPeriodo : ''));
                    rowDataLancamento.eq(key+1).text(result[key].dataLancamentoFormatada);
                    rowReparte.eq(key+1).text(result[key].reparte*1 || 0);
                    rowVenda.eq(key+1).text(result[key].venda*1 || 0);
                });
            });
        }
    },

    onSuccessReloadGrid : function() {
        //limpa espaços da grid
        $('table#baseEstudoGridParcial tr td div').filter(function(){return $.trim($(this).html()) === '&nbsp;';}).text('');

        analiseParcialController.somarTotais();
        analiseParcialController.atualizaEdicoesBaseHeader();
        analiseParcialController.atualizaAbrangencia();
        analiseParcialController.carregaDetalhesEdicoesBase();

        //insere asterisco para marcações de reparteSugerido != reparteEstudo
        $('table#baseEstudoGridParcial tr td[abbr="reparteSugerido"] div input').each(function(){
            
        	var $this = $(this);
            
            if (($this.attr('reparteInicial') != $this.attr('reparteAtual'))||($this.attr('ajustado') == "true")) {
            	
                $this.closest('tr').find('td[abbr="leg"] div').addClass('asterisco');
            }
        });

        var totalAcumuladoParcialReparte = 0;
        var totalAcumuladoParcialVenda = 0;
        //carrega % de venda
        $('#baseEstudoGridParcial tr').each(function(){
            var $tr = $(this);
            var totalReparte = $tr.find('td[abbr^="reparte"]:not([abbr="reparteSugerido"]) div').map(function(){return $(this).text()*1}).toArray().reduce(function(a,b){return a+b;});
            var totalVenda = $tr.find('td[abbr^="venda"] div').map(function(){return $(this).text()*1}).toArray().reduce(function(a,b){return a+b;});
            var perc = Math.round((totalVenda / totalReparte) * 10000) / 100;
            perc = isNaN(perc)?0:perc;
            $tr.find('td[abbr="cota"]').attr('percentualVenda', perc);
            
            if (analiseParcialController.tipoExibicao == 'PARCIAL') {
                if (totalReparte != 0) {
                    $tr.find('td[abbr^="reparte4"] div').text(totalReparte);
                    $tr.find('td[abbr^="venda4"] div').text(totalVenda);
                }
                totalAcumuladoParcialReparte += totalReparte;
                totalAcumuladoParcialVenda += totalVenda;
            }
        });
        
        if (analiseParcialController.tipoExibicao == 'PARCIAL') {
            $('#total_reparte4').text(totalAcumuladoParcialReparte);
            $('#total_venda4').text(totalAcumuladoParcialVenda);
        }
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
        var $repartesPDV = $dialogReparte.find('table.pdvCotaGrid tr input.repartePDV');
        var totalRepartePDVs = $repartesPDV.map(function () {
            return parseInt(this.value, 10);
        }).toArray().reduce(function (a, b) {
                return a + b;
            });
        if (totalRepartePDVs != reparteCota) {
            var inputReparteSugeridoCota = $('#baseEstudoGridParcial td[abbr="reparteSugerido"] input[numerocota="' + numeroCota + '"]').val(totalRepartePDVs).get(0);
            analiseParcialController.atualizaReparte(inputReparteSugeridoCota);
        }
        var param = [];
        param.push({name: 'estudoId', value: $('#estudoId').val()});
        param.push({name: 'numeroCota', value: numeroCota});
        param.push({name: 'legenda', value: legenda});
        param.push({name: 'manterFixa', value: $('#dialog-defineReparte').find('input[name="input2"]').is(':checked')});
        $repartesPDV.each(function (k) {
            param.push({name: 'reparteMap[' + k + '].id', value: $(this).closest('tr').find('td[abbr="id"] div').text()});
            param.push({name: 'reparteMap[' + k + '].reparte', value: this.value});
        });
        $.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/defineRepartePorPDV', param);
    },

    defineRepartePorPDV: function(numeroCota, nomeCota, reparteCota, legenda) {

        var $dialogReparte = $('#dialog-defineReparte');
        $dialogReparte.find('span.numeroCota').text(numeroCota);
        $dialogReparte.find('span.nomeCota').text(nomeCota);
        $dialogReparte.find('span.reparteCota').text(reparteCota);

        $('.pdvCotaGrid').flexOptions({params:[{name: 'numeroCota', value: numeroCota},
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
        .on('blur', 'tr td input:text', function(event){
            analiseParcialController.atualizaReparte(this);
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

        analiseParcialController.carregarEdicoesBaseEstudo(_id);

        $('#liberar').click(function(event){
            if ($('#status_estudo').text() == 'Liberado') {
                analiseParcialController.exibirMsg('WARNING', ['Estudo já está libearado.']);
            } else if ($('#saldo_reparte').text() != 0) {
                analiseParcialController.exibirMsg('WARNING', ['Não é possível liberar estudo com saldo de reparte.']);
            } else {
                $('<div>Liberar estudo?</div>').dialog({
                    escondeHeader: false,
                    title: 'Confirmação',
                    buttons: {
                        "Confirmar": function() {
                            $(this).dialog("close");
                            $.post(analiseParcialController.path +'/distribuicao/analise/parcial/liberar', {'id': $('#estudoId').val()},function(){
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
            }
            event.preventDefault();
        });
        
        $('#naoLiberar').click(function(event){
        	analiseParcialController.exibirMsg('WARNING', ['Já existe um estudo liberado para este lançamento.']);
            event.preventDefault();
        });

        $('#cotasNaoSelec').flexigrid({
            preProcess : analiseParcialController.preProcessGridNaoSelec,
            url: analiseParcialController.path + '/distribuicao/analise/parcial/cotasQueNaoEntraramNoEstudo/filtrar',
            dataType : 'json',
            colModel : [{display: 'Cota',         name: 'numeroCota',      width: 40,  sortable: true, align: 'left'},
                        {display: 'Nome',         name: 'nomeCota',        width: 160, sortable: true, align: 'left'},
                        {display: 'Sigla Motivo', name: 'motivo',          width: 10,  sortable: true, hide: true},
                        {display: 'Motivo',       name: 'descricaoMotivo', width: 160, sortable: true, align: 'left'},
                        {display: 'Qtde',         name: 'quantidade',      width: 60,  sortable: true, align: 'center'}],
            width : 490,
            height : 200,
            autoload: false,
            sortorder:'desc',
            sortname:'cota'
        });

//        analiseParcialController.cotasQueNaoEntraramNoEstudo();
    },
    
    preProcessGridNaoSelec : function(resultado) {
        $.each(resultado.rows, function(i, value) {
            if (typeof value.cell.nomeCota === 'undefined') {
                value.cell.nomeCota = '';
            }
            if (typeof value.cell.quantidade === 'undefined') {
                value.cell.quantidade = '';
            }

            var isReadOnly = (value.cell.motivo === 'CL' || value.cell.motivo === 'FN') ? 'readonly' : '';

            value.cell.quantidade = '<input type="text" motivo="' + value.cell.motivo + '" style="width: 50px;" value="'+
            value.cell.quantidade +'" onchange="analiseParcialController.validaMotivoCotaReparte(this);" ' +
            ' numeroCota="'+ value.cell.numeroCota +'" ' + isReadOnly + ' />';
        });
        return resultado;
    },

    validaMotivoCotaReparte : function(input) {
        var $input = $(input);
        $input.data('valid', false);

        if ($input.val() !== '' && !isNaN($input.val())) {
            switch ($input.attr('motivo')) {
                case 'SM': //Publicação não está no MIX da cota
                    analiseParcialController.popupConfirmaSenha($input, 'Deseja incluir pulicação no mix?');
                    break;
                case 'GN': //Cota não recebe esse Segmento
                    analiseParcialController.popupConfirmaSenha($input, 'Deseja incluir publicaçao na lista de publicações recebida pela cota?');
                    break;
                case 'SS': //Cota Suspensa
                    analiseParcialController.popupConfirmaSenha($input, 'Cota suspensa, continuar mesmo assim?');
                    break;
                default:
                    analiseParcialController.atualizaQuantidadeTotal($input);
            }
        } else if ($input.val() === '') {
            analiseParcialController.atualizaQuantidadeTotal($input);
        } else {
            $input.val('');
}
    },

    popupConfirmaSenha : function($input, msg) {
        var $dialog = $('#dialog-confirmacao-senha');
        $dialog.find('#msg-confirma').text(msg);
        $dialog.dialog({
            escondeHeader: false,
            buttons: {
                "Confirmar": function() {
                    if($(this).find('input').val() == 'D68') { //FIXME - validar senha no server...
                        $(this).dialog("close");
                        analiseParcialController.atualizaQuantidadeTotal($input);
                    } else {
                        analiseParcialController.exibirMsg('WARNING', ['Senha invalida!']);
                    }
                },
                "Cancelar": function() {
                    $(this).dialog("close");
                    $input.val('');
                    $input.data('valid', false);
                }
            }
        });
    },

    atualizaQuantidadeTotal : function($input) {
        var $saldoReparteNaoSelec = $('#saldoReparteNaoSelec');
        var saldo = parseInt($saldoReparteNaoSelec.html(), 10);

        var valorDigitado = parseInt($input.val(), 10);
        valorDigitado = isNaN(valorDigitado)?0:valorDigitado;

        var valorAnterior = parseInt($input.data('valor-anterior'), 10);
        valorAnterior = isNaN(valorAnterior)?0:valorAnterior;

        var variacao = valorDigitado - valorAnterior;

        saldo = saldo - variacao;
        $saldoReparteNaoSelec.html(saldo);

        $input.data('valor-anterior', $input.val());
        $input.data('valid', true);
    },
    
    cotasQueNaoEntraramNoEstudo : function() {
        var filtrarCotasQueNaoEntraramNoEstudo = function() {
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
        };

        $('#dialog-cotas-estudos .classPesquisar').click(filtrarCotasQueNaoEntraramNoEstudo);

        filtrarCotasQueNaoEntraramNoEstudo();
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
            function () {
                $('#baseEstudoGridParcial').flexReload();
                $('#saldo_reparte').text($('#saldoReparteNaoSelec').text());
                analiseParcialController.atualizaAbrangencia();
            });
    },

    exibirCotasQueNaoEntraramNoEstudo : function() {
        $('#password').val('');
        $('#saldoReparteNaoSelec').html($('#saldo_reparte').html());

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
                    var $inputsPreenchidos = $("#cotasNaoSelec tr td input").filter(function(){return this.value > 0});

                    var isValid = true;
                    $inputsPreenchidos.each(function () {
                        if ($(this).data('valid') === false) {
                            isValid = false;
                            return false;
                        }
                    });

                    if ($inputsPreenchidos.length > 0 && isValid) {
                        if ($inputsPreenchidos.filter('[motivo="SM"]').length > 0) {

                            var params = [];
                            var codigoProduto = $('#codigoProduto').val();

                            params.push({name: 'produtoId', value: codigoProduto});

                            $inputsPreenchidos.filter('[motivo="SM"]').each(function(key){
                                var $this = $(this);
                                params.push({name: 'listaNovosMixProduto[' + key + '].numeroCota', value: $this.attr('numerocota')});
                                params.push({name: 'listaNovosMixProduto[' + key + '].codigoProduto', value: codigoProduto});
                                params.push({name: 'listaNovosMixProduto[' + key + '].reparteMinimo', value: $this.val()});
                                params.push({name: 'listaNovosMixProduto[' + key + '].reparteMaximo', value: $this.val()});
                            });

                            $.postJSON(analiseParcialController.path + '/distribuicao/mixCotaProduto/adicionarMixProduto', params);
                        }
                        if ($inputsPreenchidos.filter('[motivo="GN"]').length > 0) {

                            var params = [];
                            params.push({name: 'filtro.produtoDto.codigoProduto', value: $('#codigoProduto').val()});
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
                    }
                },
                "Cancelar" : function() {
                    $(this).dialog("close");
                }
            }
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

    filtrarOrdenarPor : function(estudo) {
        var valueFiltroOrdenarPor = $("#filtroOrdenarPor").val();
        switch (valueFiltroOrdenarPor) {
            case 'numero_cota':
                analiseParcialController.filtrarCotas();
                break;
            case 'percentual_de_venda':
                analiseParcialController.filtrarOrdenarPercentualVenda();
                break;
            case 'reducao_de_reparte':
                analiseParcialController.filtrarOrdenarReducaoReparte();
                break;
            default:
                var elemento = $("#elementos :selected").val();

                $("#baseEstudoGridParcial").flexOptions({
                    params: [{name:'filterSortName', value: valueFiltroOrdenarPor},
                        {name:'filterSortFrom', value: $("#ordenarPorDe").val()},
                        {name:'filterSortTo',   value: $("#ordenarPorAte").val()},
                        {name:'id',             value: estudo},
                        {name:'elemento',       value: elemento}]
                }).flexReload();
        }
        if (event) {
            event.preventDefault();
        }
        return false;
    },

    filtrarCotas: function () {
        if ($("#ordenarPorDe").val() === '' && $("#ordenarPorAte").val() === '') {
            $('#baseEstudoGridParcial tr').show().filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
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

    filtrarOrdenarPercentualVenda : function() {
        if ($("#ordenarPorDe").val() === '' || $("#ordenarPorAte").val() === '') {
            $('#baseEstudoGridParcial tr').show().filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
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
                    var perc = $tr.find(sortAtribute).attr('percentualVenda');
                    if (de < ate) {
                        if (de < perc && perc < ate) {
                            $tr.show();
                        } else {
                            $tr.hide();
                        }
                    } else {
                        if (de > perc && perc > ate) {
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

    filtrarOrdenarReducaoReparte : function() {
        if ($("#ordenarPorDe").val() === '' || $("#ordenarPorAte").val() === '') {
            $('#baseEstudoGridParcial tr').show().filter(':odd').addClass('erow')
                .end().find('td').removeClass('sorted');
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

$(".pdvCotaGrid").flexigrid({
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

            result.rows[i].cell.reparte = '<input class="repartePDV" value="#">'.replace(/#/, result.rows[i].cell.reparte);
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
        }
    });
};

//@ sourceURL=analiseParcial.js
