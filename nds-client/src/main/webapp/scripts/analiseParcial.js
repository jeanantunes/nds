var analiseParcialController = $.extend(true, {

    path: contextPath,
    detalheEdicao: '<td class="class_linha_1">#numeroEdicao</td>',
    detalheDataLancamento : '<td width="80" align="center" class="class_linha_2">#dataLancamento</td>',
    detalheReparte : '<td align="right" class="class_linha_1">#reparte</td>',
    detalheVenda : '<td align="right" class="class_linha_2">#venda</td>',

    detalheEdicaoVazia : '<td><a href="javascript:;" onclick="escondeDados();">' +
        '<img src="'+ contextPath +'/images/ico_excluir.gif" alt="Fechar" width="15" height="15" border="0" />' +
        '</a></td>',
    detalheDataLancamentoVazia : '<td width="19" align="center">&nbsp;</td>',
    detalheReparteVazia : '<td align="right">&nbsp;</td>',
    detalheVendaVazia : '<td align="right">&nbsp;</td>',
    linhaEdicaoBase : '<tr id="row#index">' +
        '<td><div style="width: 70px;"><input type="text" id="codigoProduto#index" value="#codigoProduto" class="inputBaseNumero" onchange="analiseParcialController.buscarNomeProduto(#index);" /></div></td>' +
        '<td><div style="width: 180px;"><input type="text" id="nomeProduto#index" value="#nomeProduto" class="inputBaseNome" onchange="analiseParcialController.buscarCodigoProduto(#index);" /></div></td>' +
        '<td><div style="width: 70px;"><input type="text" id="numeroEdicao#index" value="#numeroEdicao" class="inputBaseNumero" onchange="analiseParcialController.validarNumeroEdicao(#index);" /></div></td>' +
        '<td><div style="width: 60px;"><a href="javascript:;" onclick="analiseParcialController.subirEdicaoBase(#index);"><img src="'+ contextPath +'/images/seta_sobe#sobe_desab.gif"/></a>'+
        '&nbsp;<a href="javascript:;" onclick="analiseParcialController.descerEdicaoBase(#index);"><img src="'+ contextPath +'/images/seta_desce#desce_desab.gif"/></a></div></td></tr>',

    linkNomeCota : '<a href="javascript:;" onclick="analiseParcialController.carregarDetalhesCota(#numeroCota);">#nomeCota</a>',
    edicoesBase : [],
    inputReparteSugerido : '<input type="hidden" id="reparteSalvo_#numeroCota" value="#value"/>'+
        '<input id="reparteAtual_#numeroCota" value="#value" class="reparteSugerido" '+
//        'tabindex="#tab" '+
        'onblur="analiseParcialController.inputBlur(event, this);"/>',

    tipoExibicao : 'NORMAL',

    exibirMsg: function(tipo, texto) {
        exibirMensagem(tipo, texto);
    },

    mudarBaseVisualizacao : function() {
        $("#dialog-mudar-base").dialog({
            resizable : false,
            height : 470,
            width : 550,
            modal : true,
            buttons : {
                "Confirmar" : function() {
                    var parameters = [];
                    for (var i = 0; i < 6; i++) {
                        analiseParcialController.edicoesBase[i].codigoProduto = $('#codigoProduto'+ i).val();
                        analiseParcialController.edicoesBase[i].numeroEdicao = $('#numeroEdicao'+ i).val();
                        parameters.push({name: 'edicoesBase['+ i +'].edicao', value: $('#numeroEdicao'+ i).val()});
                        parameters.push({name: 'edicoesBase['+ i +'].codigoProduto', value: $('#codigoProduto'+ i).val()});
                    }
                    parameters.push({name: 'id', value: $('#estudoId').val()},
                            {name: 'numeroEdicao', value: $('#numeroEdicao').val()},
                            {name: 'codigoProduto', value: $('#codigoProduto').val()},
                            {name: 'faixaDe', value: $('#faixaDe').val()},
                            {name: 'faixaAte', value: $('#faixaAte').val()});
                    $(this).dialog("close");
                    $("#baseEstudoGridParcial").flexOptions({params: parameters}).flexReload();
                },
                "Cancelar" : function() {
                    $(this).dialog("close");
                }
            }
        });

        $("#prodCadastradosGrid").html('');
        for (var i = 0; i < analiseParcialController.edicoesBase.length; i++) {
            var row = analiseParcialController.linhaEdicaoBase.replace(/#index/g, i);
            row = row.replace(/#codigoProduto/g, analiseParcialController.edicoesBase[i].codigoProduto);
            row = row.replace(/#nomeProduto/g, analiseParcialController.edicoesBase[i].nomeProduto);
            row = row.replace(/#numeroEdicao/g, analiseParcialController.edicoesBase[i].numeroEdicao);
            row = row.replace(/#sobe_desab/g, i == 0 ? '_desab' : '');
            row = row.replace(/#desce_desab/g, i == (analiseParcialController.edicoesBase.length - 1) ? '_desab' : '');
            $("#prodCadastradosGrid").append(row);
        }
    },

    buscarNomeProduto : function(index) {
        if ($('#codigoProduto'+ index).val() !== '') {
            $.postJSON(analiseParcialController.path +'/produto/pesquisarPorCodigoProduto',
                    [{name: 'codigoProduto', value: $('#codigoProduto'+ index).val()}],
                    function(result) {
                    $('#nomeProduto'+ index).val(result.nome);
                    $('#numeroEdicao'+ index).val('');
                });
        } else {
            analiseParcialController.exibirMsg('WARNING', ['É necessário informar um código para o produto!']);
        }
    },

    buscarCodigoProduto : function(index) {
        if ($('#nomeProduto'+ index).val() !== '') {
            $.postJSON(analiseParcialController.path +'/produto/pesquisarPorNomeProduto',
                    [{name: 'nomeProduto', value: $('#nomeProduto'+ index).val()}],
                    function(result) {
                    $('#codigoProduto'+ index).val(result.nome);
                    $('#numeroEdicao'+ index).val('');
                });
        } else {
            analiseParcialController.exibirMsg('WARNING', ['É necessário informar um nome para o produto!']);
        }
    },

    validarNumeroEdicao : function(index) {
        if ($('#codigoProduto'+ index).val() !== '') {
            if ($('#numeroEdicao'+ index).val() !== '') {
                $.postJSON(analiseParcialController.path + '/produto/validarNumeroEdicao',
                        [{name: 'codigoProduto', value: $('#codigoProduto'+ index).val()},
                         {name: 'numeroEdicao', value: $('#numeroEdicao'+ index).val()}],
                         null,
                         function(result) {
                        if (result.mensagens.listaMensagens.length > 0) {
                            $('#numeroEdicao'+ index).val('');
                        }
                    });
            } else {
                analiseParcialController.exibirMsg('WARNING', ['É necessário informar um número de edição!']);
            }
        } else {
            analiseParcialController.exibirMsg('WARNING', ['É necessário informar um código para o produto!']);
        }
    },

    carregarDetalhesCota : function(numeroCota) {
        $("#dialog-cotas-detalhes").dialog({
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

        $("#cotasDetalhesGrid").flexOptions({ params: [{name: 'numeroCota', value: numeroCota}] }).flexReload();
    },

    inicializarDetalhes : function() {
        $.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/detalhes',
                [{name: "id", value: $('#produtoEdicaoId').val()}],
                function(result) {
                $.each(result.rows, function(i, el) {
                    $('#tabelaDetalheAnalise').find('#edicoes').append(analiseParcialController.detalheEdicao.replace('#numeroEdicao', el.cell.numeroEdicao));
                    $('#tabelaDetalheAnalise').find('#dataLancamentos').append(analiseParcialController.detalheDataLancamento.replace('#dataLancamento', el.cell.dataLancamentoFormatada));
                    $('#tabelaDetalheAnalise').find('#repartes').append(analiseParcialController.detalheReparte.replace('#reparte', el.cell.reparte));
                    $('#tabelaDetalheAnalise').find('#vendas').append(analiseParcialController.detalheVenda.replace('#venda', el.cell.venda));
                });
                $('#tabelaDetalheAnalise').find('#edicoes').append(analiseParcialController.detalheEdicaoVazia);
                $('#tabelaDetalheAnalise').find('#dataLancamentos').append(analiseParcialController.detalheDataLancamentoVazia);
                $('#tabelaDetalheAnalise').find('#repartes').append(analiseParcialController.detalheReparteVazia);
                $('#tabelaDetalheAnalise').find('#vendas').append(analiseParcialController.detalheVendaVazia);
            });
    },

    somarTotais : function(resultado) {
        var totalJuramento = 0,
//            totalMediaVenda = 0,
            totalUltimoReparte = 0,
            quantidade = 0,
            totalReparteSugerido = 0,
            totais = [];
        $(resultado.rows).each(function(i, row) {
            var venda = 0,
                quantidade = 0;
            var cell = row.cell;
            for (var j = 1; j < 7; j++) {
                if (typeof totais[j - 1] === 'undefined') {
                    totais[j - 1] = {};
                    totais[j - 1].reparte = 0;
                    totais[j - 1].venda = 0;
                }
                if (!isNaN(cell['reparte'+ j]) && !isNaN(cell['venda'+ j]) &&
                        (cell['reparte'+ j] !== '') && (cell['reparte'+ j] !== '')) {
                    totais[j - 1].reparte += parseInt(cell['reparte'+ j], 10);
                    totais[j - 1].venda += parseInt(cell['venda'+ j], 10);
                    
                    venda += parseInt(cell['venda'+ j], 10);
                    quantidade++;
                }
            }
//            if (venda > 0) {
//                cell.mediaVenda = (venda / quantidade).toFixed(0);
//            } else {
//                cell.mediaVenda = '';
//            }
            if ((typeof cell.juramento !== 'undefined') && (cell.juramento !== '')) {
                totalJuramento += parseInt(cell.juramento, 10);
            }
//            if ((typeof cell.mediaVenda !== 'undefined') && (cell.mediaVenda !== '')) {
//                totalMediaVenda += parseInt(cell.mediaVenda, 10);
//            }
            if ((typeof cell.ultimoReparte !== 'undefined') && (cell.ultimoReparte !== '')) {
                totalUltimoReparte += parseInt(cell.ultimoReparte, 10);
            }
            if (!isNaN($(cell.reparteSugerido).val())) {
                totalReparteSugerido += parseInt($(cell.reparteSugerido).val(), 10);
            }
            quantidade++;
        });
        $('#total_juramento').text(totalJuramento);
//        $('#total_media_venda').text(totalMediaVenda);
        $('#total_ultimo_reparte').text(totalUltimoReparte);
        $('#total_reparte_sugerido').text(totalReparteSugerido);
        $('#total_de_cotas').text(resultado.rows.length);
        $('#total_reparte_sugerido_cabecalho').text(totalReparteSugerido);

        if (resultado.rows.length > 0) {
            for (var j = 1; j < 7; j++) {
                $('#total_reparte'+ j).text(totais[j - 1].reparte);
                $('#total_venda'+ j).text(totais[j - 1].venda);
            }
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
                        for (var i = 0; i < resultado.edicoesBase.length; i++) {
                            // atualização dos números das edições
                            if (typeof resultado.edicoesBase[i] !== 'undefined') {
                                analiseParcialController.edicoesBase[i] = {};
                                analiseParcialController.edicoesBase[i].numeroEdicao  = resultado.edicoesBase[i].edicao;
                                analiseParcialController.edicoesBase[i].nomeProduto   = resultado.edicoesBase[i].nomeProduto;
                                analiseParcialController.edicoesBase[i].codigoProduto = resultado.edicoesBase[i].codigoProduto;
                            }
                        }

                    });
        }
    },

    atualizaEdicoesBaseHeader : function() {
        setTimeout(function(){
            var $header = $('table#baseEstudoGridParcial')
                .parents('div.flexigrid')
                .find('thead:visible');

            if ($header.find('tr').length > 1) {
                $header.find('tr').first().remove();
            }

            if (analiseParcialController.tipoExibicao === 'NORMAL') {
                $header.prepend($('<tr>').append($('<th colspan="7" style="border-bottom: 1px solid #DDDDDD;">')
                    .append('<div style="text-align: right;">Edições Base:</div>')));
                $.each(analiseParcialController.edicoesBase, function(key, value) {
                    if(value){
                        $header.find('tr').first().append($('<th colspan="2">').append($('<div style="text-align: center;">')
                            .append(value.numeroEdicao)))
                    }
                });
            } else {
                $header.prepend(
                    $('<tr>')
                        .append($('<th colspan="7" style="border-bottom: 1px solid #DDDDDD;">')
                            .append('<div style="text-align: right;">Edições Base:</div>'))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('3ª Parcial')))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('2ª Parcial')))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('1ª Parcial')))
                        .append($('<th colspan="2">').append($('<div style="text-align: center;">').append('Acumulado')))
                );
            }
        }, 0);
    },

    inputBlur : function(event, input) {
        var numeroCota = input.id.match(/\d+/)[0];
        var $input_reparte = $(input);
        var $reparte_salvo = $input_reparte.prev();
        var reparteSubtraido = parseInt(input.value, 10) - parseInt($reparte_salvo.val(), 10);
        var $legenda = $input_reparte.parents('td').next().find('div');
        if (reparteSubtraido != 0) {
            $('#saldo_reparte').text(parseInt($('#saldo_reparte').text(), 10) - reparteSubtraido);

            $.ajax({url: analiseParcialController.path +'/distribuicao/analise/parcial/mudarReparte',
                data: {'numeroCota': numeroCota, 'estudoId': $('#estudoId').val(), 'variacaoDoReparte': reparteSubtraido},
                success: function() {
                    $reparte_salvo.val($input_reparte.val());
                    $legenda.addClass('asterisco');
                },
                error: function() {
                    analiseParcialController.exibirMsg('WARNING', 'Erro ao enviar novo reparte!');
                    $('#baseEstudoGridParcial').flexReload();
                }
            });
        }
    },

    preProcessGrid : function(resultado) {
        var $header = $('table#baseEstudoGridParcial')
            .parents('div.flexigrid')
            .find('thead:visible');

        if ($header.find('tr').length > 1) {
            $header.find('tr').first().remove();
        }

        // atualização dos valores da grid
        for (var i = 0; i < resultado.rows.length; i++) {
            var cell = resultado.rows[i].cell;
            var repSug = cell.reparteSugerido,
                numCota = cell.cota;
            var input = analiseParcialController.inputReparteSugerido.toString().replace('#numeroCota', numCota);
            input = input.replace('#value', repSug).replace('#numeroCota', numCota).replace('#value', repSug);
//            input = input.replace('#tab', i+1);
            cell.reparteSugerido = input;
            
            cell.nome = analiseParcialController.linkNomeCota.
            replace('#nomeCota', cell.nome).replace('#numeroCota', cell.cota);

            if (typeof cell.classificacao === 'undefined') {
                cell.classificacao = '';
            }
            if (cell.cotaNova == true) {
                cell.cota += '<span class="asterisco"></span>';
            }
            if (cell.leg !== '') {
                cell.leg = '<span id="leg_'+ numCota +'">'+ cell.leg +'</span>';
            }

            for (var j = 0; j < 6; j++) {
                if (typeof cell.edicoesBase[j] !== 'undefined') {
                    if (cell.edicoesBase[j].reparte === 0) {
                        cell['reparte'+ (j + 1)] = '';
                        cell['venda'+ (j + 1)] = '';
                    }
                    cell['reparte'+ (j + 1)] = cell.edicoesBase[j].reparte;
                    cell['venda'+ (j + 1)] = cell.edicoesBase[j].venda;
                } else {
                    cell['reparte'+ (j + 1)] = '';
                    cell['venda'+ (j + 1)] = '';
                }
            }
        }
        analiseParcialController.somarTotais(resultado);
        return resultado;
    },

    onSuccessReloadGrid : function() {
        $('td[abbr^=venda]', $('#baseEstudoGridParcial')).each(function(i, el) {
            $(el).css({'color': 'red', 'font-weight': 'bold'});
        });
        $('td[abbr^=ultimoReparte]', $('#baseEstudoGridParcial')).each(function(i, el) {
            $(el).css({'font-weight': 'bold'});
        });
        $('td[abbr^=reparteSugerido]', $('#baseEstudoGridParcial')).each(function(i, el) {
            $(el).css({'font-weight': 'bold'});
        });

        analiseParcialController.atualizaEdicoesBaseHeader();
    },

    modeloNormal : function() {
        return [{display: 'Cota',   name: 'cota',          width: 33,  sortable: true, align: 'right'},
         {display: 'Class.', name: 'classificacao', width: 30,  sortable: true, align: 'center'},
         {display: 'Nome',   name: 'nome',          width: 100, sortable: true, align: 'left'},
         {display: 'NPDV',   name: 'npdv',          width: 30,  sortable: true, align: 'right'},
         {display: 'Rep. Sugerido', name: 'reparteSugerido', width: 50, sortable: true, align: 'right'},
         {display: 'LEG',    name: 'leg',           width: 20, sortable: true, align: 'center'},
         {display: 'Cota Nova', name: 'cotaNova',   width: 40, sortable: true, align: 'right', hide: true},
//         {display: 'Média.VDA', name: 'mediaVenda', width: 60, sortable: true, align: 'right'},
         {display: 'Último. Reparte', name: 'ultimoReparte', width: 80, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte1',      width: 23, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda1',        width: 23, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte2',      width: 23, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda2',        width: 23, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte3',      width: 23, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda3',        width: 23, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte4',      width: 23, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda4',        width: 23, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte5',      width: 23, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda5',        width: 23, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte6',      width: 23, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda6',        width: 23, sortable: true, align: 'right'}];
    },
    
    modeloParcial : function() {
        return [{display: 'Cota',   name: 'cota',          width: 33,  sortable: true, align: 'right'},
         {display: 'Class.', name: 'classificacao', width: 30,  sortable: true, align: 'center'},
         {display: 'Nome',   name: 'nome',          width: 100, sortable: true, align: 'left'},
         {display: 'NPDV',   name: 'npdv',          width: 30,  sortable: true, align: 'right'},
         {display: 'Rep. Sugerido', name: 'reparteSugerido', width: 50, sortable: true, align: 'right'},
         {display: 'LEG',    name: 'leg',           width: 20, sortable: true, align: 'center'},
         {display: 'Juram.', name: 'juramento',     width: 40, sortable: true, align: 'right'},
         {display: 'Cota Nova', name: 'cotaNova',   width: 40, sortable: true, align: 'right', hide: true},
//         {display: 'Média.VDA', name: 'mediaVenda', width: 50, sortable: true, align: 'right'},
         {display: 'Último. Reparte', name: 'ultimoReparte', width: 50, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte1',      width: 40, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda1',        width: 40, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte2',      width: 40, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda2',        width: 40, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte3',      width: 40, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda3',        width: 40, sortable: true, align: 'right'},
         {display: 'REP', name: 'reparte4',      width: 40, sortable: true, align: 'right'},
         {display: 'VDA', name: 'venda4',        width: 40, sortable: true, align: 'right'}];
    },

    sortGrid : function (sortname, sortorder) {
        var $table, settings, sortAtribute;
        $table = $(arguments.callee.caller.arguments).parents('.flexigrid').find('div.bDiv:first table');
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

    init : function(_id, _faixaDe, _faixaAte, _tipoExibicao){

        $('#baseEstudoGridParcial').on('blur', 'tr td input:text', function(event){
            $('#total_reparte_sugerido')
                .add('#total_reparte_sugerido_cabecalho')
                .text(
                $('#baseEstudoGridParcial tr td input:text').map(function(){
                    return parseInt(this.value, 10);
                }).toArray().reduce(function(a,b){
                        return a+b;
                    }));
        }).on('keyup', 'tr td input:text', function(event){
            if(event.which === 13 || event.which === 9) {
                $(event.currentTarget)
                    .parents('tr').next('tr')
                    .find('input:text').focus()
                    .select();
            }
        }).on('click', 'tr td input:text', function(event){
            $(event.currentTarget).select();
        });

        analiseParcialController.tipoExibicao = _tipoExibicao;
        analiseParcialController.inicializarDetalhes();
        analiseParcialController.carregarEdicoesBaseEstudo(_id);

        var parameters = [];
        parameters.push({name: 'id', value: _id});
        parameters.push({name: 'faixaDe', value: _faixaDe});
        parameters.push({name: 'faixaAte', value: _faixaAte});
        parameters.push({name: 'codigoProduto', value: $('#codigoProduto').val()});
        parameters.push({name: 'numeroEdicao', value: $('#numeroEdicao').val()});
        
        if(typeof(histogramaPosEstudo_cotasRepMenorVenda)!="undefined"){
        	parameters.push({name: "numeroCotaStr", value: histogramaPosEstudo_cotasRepMenorVenda});
        }

        var modelo = _tipoExibicao == 'NORMAL' ? analiseParcialController.modeloNormal() : analiseParcialController.modeloParcial();
        $('#baseEstudoGridParcial').flexigrid({
            preProcess : analiseParcialController.preProcessGrid,
            url : analiseParcialController.path + '/distribuicao/analise/parcial/init',
            params: parameters,
            dataType : 'json',
            colModel : modelo,
            width: 950,
            height: 200,
            colMove: false,
            showToggleBtn: false,
            sortorder: 'desc',
            sortname: 'reparteSugerido',
            onChangeSort: analiseParcialController.sortGrid,
            onSuccess: analiseParcialController.onSuccessReloadGrid
        });

        /*analiseParcialController.tempo = new Date();
        $.ajax({
            url: analiseParcialController.path + '/distribuicao/analise/parcial/init',
            type: 'POST',
            data: parameters,
            global: false
        }).success(function(result){
            console.log(result);
            console.log((new Date())-analiseParcialController.tempo);
        });*/

        $('#liberar').click(function(event){
            $.post(analiseParcialController.path +'/distribuicao/analise/parcial/liberar', {'id': $('#estudoId').val()},function(){
                analiseParcialController.exibirMsg('SUCCESS', ['Estudo liberado com sucesso!']);
            });
            event.preventDefault();
        });

        $('#cotasNaoSelec').flexigrid({
            preProcess : analiseParcialController.preProcessGridNaoSelec,
            url: analiseParcialController.path + '/distribuicao/analise/parcial/cotasQueNaoEntraramNoEstudo/filtrar',
            dataType : 'json',
            colModel : [{display: 'Cota',   name: 'numeroCota',       width: 40,  sortable: true, align: 'left'},
                        {display: 'Nome',   name: 'nomeCota',       width: 160, sortable: true, align: 'left'},
                        {display: 'Motivo', name: 'motivo',     width: 160, sortable: true, align: 'left'},
                        {display: 'Qtde',   name: 'quantidade', width: 60,  sortable: true, align: 'center'}],
            width : 490,
            height : 200,
            sortorder:'desc',
            sortname:'cota'
        });

        analiseParcialController.cotasQueNaoEntraramNoEstudo();
    },
    
    preProcessGridNaoSelec : function(resultado) {
        $.each(resultado.rows, function(i, value) {
            if (typeof value.cell.nomeCota === 'undefined') {
                value.cell.nomeCota = '';
            }

            value.cell.quantidade = '<input type="text" id="inputQuantidade'+ value.cell.numeroCota +'" style="width: 50px;" value="'+
                value.cell.quantidade +'" onblur="analiseParcialController.atualizaQuantidadeTotal('+ value.cell.numeroCota +');" ' +
                ' numeroCota="'+ value.cell.numeroCota +'" />';
        });
        return resultado;
    },
    
    atualizaQuantidadeTotal : function(numeroCota) {
        var $quantidade = $('#inputQuantidade' + numeroCota);
        if (!isNaN($quantidade.val())) {
            var $saldoReparteNaoSelec = $('#saldoReparteNaoSelec');
            var saldo = parseInt($saldoReparteNaoSelec.html(), 10);
            saldo = saldo - parseInt($quantidade.val(), 10);
            $saldoReparteNaoSelec.html(saldo);
        }
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
        $("#cotasQueNaoEntraramNoEstudo_cota").blur(filtrarCotasQueNaoEntraramNoEstudo);
        $("#cotasQueNaoEntraramNoEstudo_nome").blur(filtrarCotasQueNaoEntraramNoEstudo);
        $("#cotasQueNaoEntraramNoEstudo_motivo").change(filtrarCotasQueNaoEntraramNoEstudo);
        $("#cotasQueNaoEntraramNoEstudo_elementos").change(filtrarCotasQueNaoEntraramNoEstudo);
    },
    
    verCapa : function() {
        $("#dialog-detalhes").dialog({
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

        $("#cotasNaoSelec tr td input").each(function (key, value) {
            if (value.value > 0) {
                data.push({name: 'cotas[' + key + '].numeroCota', value: value.id.replace(/\D/g, '')});
                data.push({name: 'cotas[' + key + '].quantidade', value: value.value});
            }
        });

        return data;
    },

    exibirCotasQueNaoEntraramNoEstudo : function() {
        $('#saldoReparteNaoSelec').html($('#saldo_reparte').html());

        analiseParcialController.cotasQueNaoEntraramNoEstudo();

        $("#dialog-cotas-estudos").dialog({
            resizable : false,
            height : 530,
            width : 550,
            modal : true,
            buttons : {
                "Confirmar" : function() {
                    $(this).dialog("close");
                    $.postJSON(analiseParcialController.path + '/distribuicao/analise/parcial/mudarReparteLote',
                        analiseParcialController.getCotasComReparte(),
                        function(){
                            $('#baseEstudoGridParcial').flexReload();
                            $('#saldo_reparte').text($('#saldoReparteNaoSelec').text());
                    });
                },
                "Cancelar" : function() {
                    $(this).dialog("close");
                }
            }
        });
    },
    
    selecionarElementos : function(tipo, optionsId) {
        $.getJSON(analiseParcialController.path +"/componentes/elementos", [{name: "tipo", value: tipo}, {name: "estudo", value: $("#estudoId").val()}], function(data) {
            var options = $("#"+ optionsId);
            options.empty();
            options.append($("<option />").val('TODOS').text('Selecione...'));
            $.each(data, function() {
                if (this.tipo === 'bairro') {
                    options.append($("<option />").val(this.tipo +"_"+ this.value).text(this.value));
                } else {
                    options.append($("<option />").val(this.tipo +"_"+ this.id).text(this.value));
                }
            });
        });
    },

    apresentarOpcoesOrdenarPor : function(opcao) {
        if (opcao === "selecione") {
            $("#opcoesOrdenarPor").hide();
        } else {
            $(".label").hide();
            $("#label_"+ opcao).show();
            $("#opcoesOrdenarPor").show();
        }
    },

    filtrarOrdenarPor : function(estudo) {
        $("#baseEstudoGridParcial").flexOptions({
            params: [{name:'filterSortName', value: $("#filtroOrdenarPor").val()},
                     {name:'filterSortFrom', value: $("#ordenarPorDe").val()},
                     {name:'filterSortTo',   value: $("#ordenarPorAte").val()},
                     {name:'id',             value: estudo},
                     {name:'elemento',       value: $("#elementos :selected").val()}]
        }).flexReload();
        if (event) {
            event.preventDefault();
        }
        return false;
    },

    subirEdicaoBase : function(index) {
        var temp = analiseParcialController.edicoesBase[index - 1];
        analiseParcialController.edicoesBase[index - 1] = analiseParcialController.edicoesBase[index];
        analiseParcialController.edicoesBase[index] = temp;
        
        $('#codigoProduto'+ (index - 1)).val(analiseParcialController.edicoesBase[index - 1].codigoProduto);
        $('#nomeProduto'+ (index - 1)).val(analiseParcialController.edicoesBase[index - 1].nomeProduto);
        $('#numeroEdicao'+ (index - 1)).val(analiseParcialController.edicoesBase[index - 1].numeroEdicao);
        
        $('#codigoProduto'+ index).val(analiseParcialController.edicoesBase[index].codigoProduto);
        $('#nomeProduto'+ index).val(analiseParcialController.edicoesBase[index].nomeProduto);
        $('#numeroEdicao'+ index).val(analiseParcialController.edicoesBase[index].numeroEdicao);
    },

    descerEdicaoBase : function(index) {
        var temp = analiseParcialController.edicoesBase[index + 1];
        analiseParcialController.edicoesBase[index + 1] = analiseParcialController.edicoesBase[index];
        analiseParcialController.edicoesBase[index] = temp;
        
        $('#codigoProduto'+ (index + 1)).val(analiseParcialController.edicoesBase[index + 1].codigoProduto);
        $('#nomeProduto'+ (index + 1)).val(analiseParcialController.edicoesBase[index + 1].nomeProduto);
        $('#numeroEdicao'+ (index + 1)).val(analiseParcialController.edicoesBase[index + 1].numeroEdicao);
        
        $('#codigoProduto'+ index).val(analiseParcialController.edicoesBase[index].codigoProduto);
        $('#nomeProduto'+ index).val(analiseParcialController.edicoesBase[index].nomeProduto);
        $('#numeroEdicao'+ index).val(analiseParcialController.edicoesBase[index].numeroEdicao);
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
    url: analiseParcialController.path +'/distribuicao/analise/parcial/carregarDetalhesCota',
    dataType : 'json',
    autoload: false,
    colModel : [{display: 'Código',   name: 'id',                     width: 40,  sortable: true, align: 'left'},
                {display: 'Tipo',     name: 'descricaoTipoPontoPDV',  width: 90,  sortable: true, align: 'left'},
                {display: '% Fat.',   name: 'porcentagemFaturamento', width: 30,  sortable: true, align: 'right'},
                {display: 'Princ.',   name: 'principal',              width: 30,  sortable: true, align: 'center'},
                {display: 'Endereço', name: 'endereco',               width: 420, sortable: true, align: 'left'}],
                width : 690,
                height : 200
});

$("#prodCadastradosGrid").flexigrid({
    colModel : [{display: 'Código',  name: 'codigo',  width: 70,  sortable: true, align: 'left'},
                {display: 'Produto', name: 'produto', width: 180, sortable: true, align: 'left'},
                {display: 'Edição',  name: 'edicao',  width: 70,  sortable: true, align: 'left'},
                {display: 'Ordenar', name: 'ordenar', width: 60,  sortable: true, align: 'left'}],
                width : 490,
                height : 225
});

$(".edicaoProdCadastradosGrid").flexigrid({
    colModel : [{display: 'Edição',      name: 'edicao',       width: 45,  sortable: true, align: 'left'},
                {display: 'Data Lancto', name: 'dtLancamento', width: 100, sortable: true, align: 'center'},
                {display: 'Reparte',     name: 'reparte',      width: 40,  sortable: true, align: 'right'},
                {display: 'Venda',       name: 'venda',        width: 40,  sortable: true, align: 'right'},
                {display: 'Status',      name: 'status',       width: 110, sortable: true, align: 'left'},
                {display: 'Capa',        name: 'capa',         width: 30,  sortable: true, align: 'center'},
                {display: '',            name: 'sel',          width: 30,  sortable: true, align: 'center'}],
                width : 500,
                height : 200
});

//@ sourceURL=analiseParcial.js
