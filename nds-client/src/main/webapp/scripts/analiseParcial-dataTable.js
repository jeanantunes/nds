function dataTableInit() {

    $('.dt-container').on('change', 'input.reparteSugerido', function () {
        var tdNode = $(this).closest('td')[0];
        var tdPosition = oTable.fnGetPosition(tdNode);
        oTable.fnGetData(tdNode); //valor anterior
        oTable.fnUpdate(this.value, tdPosition);
    });

    var reparteSugerido  = function ( data, type, full ) {
        if (type === "display") {
            return '<input reducaoReparte="#redReparte" reparteInicial="#repEstudo" reparteAtual="#value" numeroCota="#numeroCota" value="#value" class="reparteSugerido" />'
                .replace(/#numeroCota/g, full.cell.cota || 0)
                .replace(/#value/g, data)
                .replace(/#repEstudo/g, full.cell.reparteEstudo || 0)
                .replace(/#redReparte/g, analiseParcialController.calculaPercentualReducaoReparte(full.cell.reparteEstudo || 0, data));
        }
        return data;
    };

    $('.dt-container').show();
    window.oTable = $('#dt-test').dataTable( {
//                "sDom": '<"H"lr>t<"F"ip>',
//                "bJQueryUI": true,
        "sDom": 'rtip',
        "bAutoWidth": false,
        "sScrollY": "200px",
        "bPaginate": false,
        "bScrollCollapse": false,
        "bProcessing": true,
        "aaSorting": [[ 5, "desc" ]],
        "sServerMethod": "POST",
        "sAjaxSource": "distribuicao/analise/parcial/init",
        "sAjaxDataProp": "rows",
        "fnServerParams": function ( aoData ) {
            aoData.push({name: 'id', value: $('#estudoId').val()});
            aoData.push({name: 'codigoProduto', value: $('#codigoProduto').val()});
            aoData.push({name: 'numeroEdicao', value: $('#numeroEdicao').val()});
        },
        "aoColumnDefs": [
            { "aTargets": [ 0 ],  "sClass": "tright",       "sTitle": "Cota",      "mData": "cell.cota" },
            { "aTargets": [ 1 ],  "sClass": "tcenter",      "sTitle": "Class.",    "mData": "cell.classificacao" },
            { "aTargets": [ 2 ],  "sClass": "tleft",        "sTitle": "Nome",      "mData": "cell.nome", "sWidth": "130px" },
            { "aTargets": [ 3 ],  "sClass": "tright",       "sTitle": "NPDV",      "mData": "cell.npdv" },
            { "aTargets": [ 4 ],  "sClass": "tright tbold", "sTitle": "Últ. Rep.", "mData": "cell.ultimoReparte", "sWidth": "40px" },
            { "aTargets": [ 5 ],  "sClass": "tright tbold", "sTitle": "Rep. Sug.", "mData": "cell.reparteSugerido", "mRender": reparteSugerido, "sWidth": "40px" },
            { "aTargets": [ 6 ],  "sClass": "tcenter",      "sTitle": "LEG",       "mData": "cell.leg", "sWidth": "20px" },
            { "aTargets": [ 7 ],  "sClass": "tright",       "sTitle": "REP",       "mData": "cell.edicoesBase.0.reparte", "sDefaultContent": "" },
            { "aTargets": [ 8 ],  "sClass": "tright tred",  "sTitle": "VDA",       "mData": "cell.edicoesBase.0.venda",   "sDefaultContent": "" },
            { "aTargets": [ 9 ],  "sClass": "tright",       "sTitle": "REP",       "mData": "cell.edicoesBase.1.reparte", "sDefaultContent": "" },
            { "aTargets": [ 10 ], "sClass": "tright tred",  "sTitle": "VDA",       "mData": "cell.edicoesBase.1.venda",   "sDefaultContent": "" },
            { "aTargets": [ 11 ], "sClass": "tright",       "sTitle": "REP",       "mData": "cell.edicoesBase.2.reparte", "sDefaultContent": "" },
            { "aTargets": [ 12 ], "sClass": "tright tred",  "sTitle": "VDA",       "mData": "cell.edicoesBase.2.venda",   "sDefaultContent": "" },
            { "aTargets": [ 13 ], "sClass": "tright",       "sTitle": "REP",       "mData": "cell.edicoesBase.3.reparte", "sDefaultContent": "" },
            { "aTargets": [ 14 ], "sClass": "tright tred",  "sTitle": "VDA",       "mData": "cell.edicoesBase.3.venda",   "sDefaultContent": "" },
            { "aTargets": [ 15 ], "sClass": "tright",       "sTitle": "REP",       "mData": "cell.edicoesBase.4.reparte", "sDefaultContent": "" },
            { "aTargets": [ 16 ], "sClass": "tright tred",  "sTitle": "VDA",       "mData": "cell.edicoesBase.4.venda",   "sDefaultContent": "" },
            { "aTargets": [ 17 ], "sClass": "tright",       "sTitle": "REP",       "mData": "cell.edicoesBase.5.reparte", "sDefaultContent": "" },
            { "aTargets": [ 18 ], "sClass": "tright tred",  "sTitle": "VDA",       "mData": "cell.edicoesBase.5.venda",   "sDefaultContent": "" }
        ],
        "fnFooterCallback": function ( nRow, aaData, iStart, iEnd, aiDisplay ) {

            var qtdCotas = aiDisplay.length,
                iUltimoReparte = 0,
                iReparteSugerido = 0,
                aRepartes = [0,0,0,0,0,0],
                aVendas = [0,0,0,0,0,0];

            for ( var i=iStart ; i<iEnd ; i++ )
            {
                iUltimoReparte += aaData[ aiDisplay[i] ].cell.ultimoReparte * 1;
                iReparteSugerido += aaData[ aiDisplay[i] ].cell.reparteSugerido * 1;
                aRepartes[0] += aaData[ aiDisplay[i] ].cell.edicoesBase[0].reparte * 1;
                aVendas[0] += aaData[ aiDisplay[i] ].cell.edicoesBase[0].venda * 1;
                aRepartes[1] += aaData[ aiDisplay[i] ].cell.edicoesBase[1].reparte * 1;
                aVendas[1] += aaData[ aiDisplay[i] ].cell.edicoesBase[1].venda * 1;
                aRepartes[2] += aaData[ aiDisplay[i] ].cell.edicoesBase[2].reparte * 1;
                aVendas[2] += aaData[ aiDisplay[i] ].cell.edicoesBase[2].venda * 1;
                aRepartes[3] += aaData[ aiDisplay[i] ].cell.edicoesBase[3].reparte * 1;
                aVendas[3] += aaData[ aiDisplay[i] ].cell.edicoesBase[3].venda * 1;
                aRepartes[4] += aaData[ aiDisplay[i] ].cell.edicoesBase[4].reparte * 1;
                aVendas[4] += aaData[ aiDisplay[i] ].cell.edicoesBase[4].venda * 1;
                aRepartes[5] += aaData[ aiDisplay[i] ].cell.edicoesBase[5].reparte * 1;
                aVendas[5] += aaData[ aiDisplay[i] ].cell.edicoesBase[5].venda * 1;
            }

            /* Modify the footer row to match what we want */
            var nCells = nRow.getElementsByTagName('th');
            nCells[1].innerHTML = qtdCotas;
            nCells[3].innerHTML = iUltimoReparte;
            nCells[4].innerHTML = iReparteSugerido;
            nCells[6].innerHTML = aRepartes[0];
            nCells[7].innerHTML = aVendas[0];
            nCells[7].classList.add("vermelho");
            nCells[8].innerHTML = aRepartes[1];
            nCells[9].innerHTML = aVendas[1];
            nCells[9].classList.add("vermelho");
            nCells[10].innerHTML = aRepartes[2];
            nCells[11].innerHTML = aVendas[2];
            nCells[11].classList.add("vermelho");
            nCells[12].innerHTML = aRepartes[3];
            nCells[13].innerHTML = aVendas[3];
            nCells[13].classList.add("vermelho");
            nCells[14].innerHTML = aRepartes[4];
            nCells[15].innerHTML = aVendas[4];
            nCells[15].classList.add("vermelho");
            nCells[16].innerHTML = aRepartes[5];
            nCells[17].innerHTML = aVendas[5];
            nCells[17].classList.add("vermelho");
        }
    });

    $("#dt-test_wrapper tfoot input").keyup( function () {
        /* Filter on the column (the index) of this element */
        var index = $("#dt-test_wrapper tfoot input").index(this);
        if (index == 2) {
            oTable.fnFilter( this.value, index );
        } else {
            oTable.fnFilter( this.value.length>0?'^'+this.value+'$':'', index, true, false );
        }
    } );
}

//@ sourceURL=analiseParcial-dataTable.js
