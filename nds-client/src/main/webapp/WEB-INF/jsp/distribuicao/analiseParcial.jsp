<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numberformatter-1.2.3.min.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.tinysort.min.js"></script>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/analiseParcial.js"></script>
<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.dataTables.css">
<!-- DataTables -->
<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/scripts/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/scripts/analiseParcial-dataTable.js"></script>
<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/scripts/usuario/usuario.js"></script>

<script language="javascript" type="text/javascript">

$(function() {
    $('.legendas').tipsy({gravity: $.fn.tipsy.autoNS});
//    $('.asterisco').tipsy({live: true, gravity: $.fn.tipsy.autoNS, title: function(){return 'Reparte Alterado';}});

    var estudoCopiado = $("#reparteCopiado").val();
    
    if(estudoCopiado != "" ) {
    	$("#tdEstudoCopiado").show();
    	$("#spanReparteCopiado").text(estudoCopiado);
    } else {
    	
    	$("#tdEstudoCopiado").hide();
    }
    
});

function mostraDados_analiseParcial() {
	
	analiseParcialController.montarDadosDetalhesEdicoesBases();
}
function escondeDados_analiseParcial() {
	
	$('.detalhesDados-analiseParcial').hide();
}
	
</script>

<style>
.gridScroll tr:hover{background:#FFC}
.analiseRel tbody{height:100px; overflow:auto;}
.analiseRel tr:hover{background:#FFC;}
.class_tpdv{width:55px;}
.class_novaCota{width:32px;}
.class_cota{width:40px;}
.class_nome{width:90px;}
.class_npdv{width:30px;}
.class_media{width:35px; color:#F00; font-weight:bold;}
.class_vlrs{width:35px;}
.class_vda{width:35px; color:#F00; font-weight:bold;}
.detalhesDados-analiseParcial{position:absolute; display:none; background:#fff; margin-left: 265px; border:1px solid #ccc; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); width: 978px;}

<c:if test="${estudo.idEstudoOrigemCopia != null}">
</c:if>
#tabelaDetalheAnalise tr { line-height: normal; }
.class_linha_impar {background:#f0f0f0; }
.class_linha_par {background:#f8f8f8; }

.icoEditarEB, .icoExcluirEB, .icoMoverEB { margin: 3px; cursor: pointer; }
.inputCodigoEB, .inputEdicaoEB { width: 70px; }
.inputProdutoEB { width: 170px; }
.icoMoverEB { cursor: grab; cursor: -moz-grab; cursor: -webkit-grab; }
.icoMoverEB:active { cursor: grabbing; cursor: -moz-grabbing; cursor: -webkit-grabbing; }

/*.inputBaseNumero{width: 60px;}
.inputBaseNome{width: 170px;}*/

table.filtro td span {font-weight: normal;}
.tableTotais {margin: 1px 0 3px; border: 1px solid white; border-collapse: collapse;}
.dadosTab td:first-child { width: 130px; text-align: right; padding-right: 5px; }
.dadosTab td {line-height: 20px;}

<c:if test="${tipoExibicao == 'NORMAL'}">
.paddingTotais td {padding: 0 3px; text-align: right; width: 35px; border: 1px solid white;}
table.dadosTab { margin-left: 320px;}
.dadosTab td { width: 82px; text-align: center; }
</c:if>
<c:if test="${tipoExibicao == 'PARCIAL'}">
.paddingTotais td {padding: 0 3px; text-align: right; width: 45px; border: 1px solid white;}
table.dadosTab { margin-left: 370px;}
.dadosTab td { width: 100px; text-align: center; }
.dadosTab td:nth-child(5) {display: none;}
.dadosTab td:nth-child(6) {display: none;}
.dadosTab td:nth-child(7) {display: none;}
</c:if>

.paddingTotais td#lbl_qtd_cotas {text-align: left; width: 82px;}
.paddingTotais td#total_de_cotas {text-align: left; width: 197px;}
.paddingTotais td#total_ultimo_reparte {width: 55px;}
.paddingTotais td#total_reparte_sugerido {width: 56px;}
.paddingTotais td#lbl_legenda {width: 24px;}
.paddingTotais td#total_reparte_origem {width: 56px;}
.paddingTotais td#total_juramento {width: 44px;}
.linkNomeCota { text-decoration: underline; cursor: pointer; }
.editaRepartePorPDV { text-decoration: underline; cursor: pointer; }
.asteriscoCotaNova:after { content: "*"; font-size: 150%; font-weight: bold; position: absolute; left: 43px; }
.asterisco:after { content: "*"; font-size: 150%; font-weight: bold; position: absolute; margin-left: 2px; }
.reparteSugerido { width: 100%; font-weight: bold; text-align: right; box-sizing: border-box; -moz-box-sizing: border-box; -webkit-box-sizing: border-box; }
#baseEstudoGridParcial td[abbr^="venda"] {color: red;}
#baseEstudoGridParcial td[abbr^="venda"],
#baseEstudoGridParcial td[abbr^="ultimoReparte"],
#baseEstudoGridParcial td[abbr^="reparteSugerido"] {font-weight: bold;}
/*#baseEstudoGridParcial td div { position: relative; }*/
.repartePDV {width: 35px; text-align: right;}
#prodCadastradosGrid tbody tr {display: block !important;}
.sortable-placeholder {height: 33px !important; line-height: 30px !important; border: 1px solid orange !important;}
.classFieldset {width: 1040px !important;}
</style>

    <br clear="all"/>
    <br />

    <div class="container">

     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all">
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Base de Estudo < evento > com < status >.</b></p>
	</div>
		
    	<div class="detalhesDados-analiseParcial" style="margin-left: 0px;">
            
            <div style="float: right;">
            	<a href="javascript:;" onclick="escondeDados_analiseParcial();">
            		<img src="images/ico_excluir.gif" alt="Fechar" width="15" height="15" border="0" />
            	</a>
           	</div>
           	
            <table border="0" cellpadding="2" cellspacing="2" style="margin-left: 319px;" class="dadosTab" id="tabelaDetalheAnalise">
                <tr class="class_linha_impar" id="analiseParcialPopUpCodProduto"></tr>
                <tr class="class_linha_par" id="analiseParcialPopUpNomeProduto"></tr>
                <tr class="class_linha_impar" id="analiseParcialPopUpNumeroEdicao"></tr>
                <tr class="class_linha_par" id="analiseParcialPopUpDatalancamento"></tr>
                <tr class="class_linha_impar" id="analiseParcialPopUpReparte"></tr>
                <tr class="class_linha_par" id="analiseParcialPopUpVenda"></tr>
  	        </table>
  	        
        </div>
        
		<fieldset class="classFieldset">
			<legend> Pesquisar </legend>
			<input type="hidden" id="produtoEdicaoId" value="${estudo.produtoEdicao.id}" />
			<input type="hidden" id="usedMinMaxMix" value="${estudo.minMaxMix}" />
			<input type="hidden" id="faixaDe" value="${faixaDe}" />
			<input type="hidden" id="faixaAte" value="${faixaAte}" />
			<input type="hidden" id="reparteCopiado" value="${reparteCopiado}" />
			<input type="hidden" id="numeroEdicao" value="${estudo.produtoEdicao.numeroEdicao}" />
			<input type="hidden" id="estudoId" value="${estudo.id}" />
			<input type="hidden" id="codigoProduto" value="${estudo.produtoEdicao.produto.codigo}" />
			<input type="hidden" id="produtoId" value="${estudo.produtoEdicao.produto.id}" />
			<input type="hidden" id="tipoSegmentoProduto" value="${estudo.produtoEdicao.produto.tipoSegmentoProduto.id}" />
			<input type="hidden" id="estudoOrigem" value="${estudo.idEstudoOrigemCopia}" />
			<input type="hidden" id="dataLancamentoEdicao" value="${dataLancamentoEdicao}" />
			<input type="hidden" id="tipoClassificacaoProdutoId" value="${estudo.produtoEdicao.tipoClassificacaoProduto.id}" />
			<input type="hidden" id="tipoClassificacaoProdutoDescricao" value="${estudo.produtoEdicao.tipoClassificacaoProduto.descricao}" />
			<input type="hidden" id="numeroPeriodo" value="${lancamento.periodoLancamentoParcial.numeroPeriodo}" />
			<input type="hidden" id="dataLancamentoParcial1" value="${parcial0}" />
			<input type="hidden" id="dataLancamentoParcial2" value="${parcial1}" />
			<input type="hidden" id="dataLancamentoParcial3" value="${parcial2}" />
			
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td>Código: <span>${estudo.produtoEdicao.produto.codigo}</span></td>
					<td>Produto: <span>${estudo.produtoEdicao.nomeComercial}</span></td>
					<td>Edição: <span>${estudo.produtoEdicao.numeroEdicao}</span></td>
					<td>Estudo: <span>${estudo.id}</span></td>
                    <c:if test="${tipoExibicao != 'NORMAL'}">
                        <td>Nro. da Parcial: <span>${lancamento.periodoLancamentoParcial.numeroPeriodo}</span></td>
                    </c:if>
				</tr>
            </table>
            <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
                <tr>
                    <td>Status do Estudo: <span id="status_estudo">${estudo.isLiberado()?'Liberado':'Não Liberado'}</span></td>
                    <td>Data de Lancamento: <span><fmt:formatDate value="${lancamento.dataLancamentoDistribuidor}" /></span></td>
                    <td>Reparte Distribuido: <span id="total_reparte_estudo_cabecalho">${estudo.qtdeReparte}</span></td>
                    <td id="tdEstudoCopiado">Reparte Copiado: <span id="spanReparteCopiado">${reparteCopiado}</span></td>
                    <td>Pacote Padrão: <span>${estudo.produtoEdicao.pacotePadrao}</span></td>
                </tr>
            </table>
            <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td>Classificação: <span>${estudo.produtoEdicao.tipoClassificacaoProduto.descricao}</span></td>
					<td>Segmento: <span>${estudo.produtoEdicao.produto.tipoSegmentoProduto.descricao}</span></td>
					<td>Filtrar por:</td>
					<td><select name="select5" id="filtroOrdenarPor" style="width: 145px;"
						onchange="analiseParcialController.apresentarOpcoesOrdenarPor(this.value);">
							<option value="" selected="selected">Selecione...</option>
							<option value="reparte">Reparte</option>
							<option value="ranking">Ranking</option>
							<option value="n_maiores">N Maiores</option>
							<option value="percentual_de_venda">% de Venda</option>
							<option value="reducao_de_reparte">% Variação de Reparte</option>
							<option value="numero_cota">Cota</option>
					</select></td>
					<%--<td>Reparte: <input type="text" name="textfield6" id="textfield6" style="width: 40px;" /></td>--%>
					<td>Abrangência: <span id="abrangencia"></span></td>
				</tr>
            </table>
            <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td>Componente:</td>
					<td>
						<select id="componentes" name="componentes" style="width: 170px;"
						    onchange="analiseParcialController.selecionarElementos(this.value, 'elementos')">
							<option value="" selected="selected">Selecione...</option>
							<option value="tipo_ponto_venda">Tipo de Ponto de Venda</option>
							<option value="gerador_de_fluxo">Gerador de Fluxo</option>
							<option value="bairro">Bairro</option>
							<option value="regiao">Região</option>
							<option value="cotas_a_vista">Cotas A Vista</option>
							<option value="cotas_novas">Cotas Novas</option>
							<option value="area_influencia">Área de Influência</option>
							<option value="distrito">Distrito</option>
                            <option value="tipo_distribuicao_cota">Tipo de Distribuição Cota</option>
						</select>
					</td>
					<td>Elemento:</td>
					<td><select id="elementos" name="elementos" style="width: 170px;"
						onchange="analiseParcialController.filtrarOrdenarPorElemento(${estudo.id})">
							<option value="" selected="selected">Selecione...</option>
					</select></td>
					<td>
                        	<span id="opcoesOrdenarPor" style="display: none;" class="label">
                            
                            <span id="label_numero_cota" style="display: none;" class="label"> Cota: </span>
                            <span id="label_reparte" style="display: none;" class="label"> Reparte: </span>
                            <span id="label_reducao_de_reparte" style="display: none;" class="label"> % De: </span>
                            <span id="label_ranking" style="display: none;" class="label"> Reparte: </span>
                            <span id="label_n_maiores" style="display: none;" class="label"> Ranking: </span>
                            <span id="label_percentual_de_venda" style="display: none;" class="label"> % Venda: </span>
                            
                            <input id="ordenarPorDe" type="text" style="width: 60px;" /> Até 
                            <input id="ordenarPorAte" type="text" style="width: 60px;" />
                            
                            <span id="labelAte_numero_cota" style="display: none;" class="label"> </span>
                            <span id="labelAte_reparte" style="display: none;" class="label"> Exs. </span>
                            <span id="labelAte_reducao_de_reparte" style="display: none;" class="label"> % </span>
                            <span id="labelAte_ranking" style="display: none;" class="label"> Exs. </span>
                            <span id="labelAte_n_maiores" style="display: none;" class="label"> Pos. </span>
                            <span id="labelAte_percentual_de_venda" style="display: none;" class="label"> % </span>
                            
                            &nbsp;
                            
                            <a href="javascript:analiseParcialController.filtrarOrdenarPor(${estudo.id});">
                                <img src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Confirmar" border="0" />
						    </a>
					    </span>
                    </td>
					<td align="center">
                        <a href="javascript:;" onclick="mostraDados_analiseParcial();">
                            <img src="${pageContext.request.contextPath}/images/ico_boletos.gif" title="Exibir Detalhes" width="19" height="15" border="0" />
                        </a>
                    </td>
					<td>
                        <span class="bt_novos">
                            <a href="javascript:;" onclick="analiseParcialController.verCapa();">
								<img src="${pageContext.request.contextPath}/images/ico_detalhes.png" alt="Ver Capa" hspace="5" border="0" />
                                <span>Ver Capa</span>
						    </a>
					    </span>
                    </td>
                    <td>
                        <span class="bt_novos">
                            <a href="javascript:;" onclick="analiseParcialController.alterarVisualizacaoGrid();">
                                <img src="${pageContext.request.contextPath}/images/ico_change.png" title="Alterar Visualização do Grid" hspace="5" border="0" /> <%--Alterar Visualização do Grid--%>
                            </a>
                        </span>
                        <%--<span class="bt_novos" title="Base Inicial">
                            <a href="javascript:;" onclick="analiseParcialController.restauraBaseInicial();">
                                <span class="ui-icon ui-icon-arrowreturnthick-1-w"></span>
                            </a>
                        </span>--%>
                    </td>
				</tr>
			</table>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="classFieldset">
			<legend>Base de Estudo / Análise</legend>
			<div class="grids" style="display: block;">
				<table class="baseEstudoGrid" id="baseEstudoGridParcial"></table>

				<c:if test="${tipoExibicao == 'NORMAL'}">
					<table border="0" cellspacing="0" cellpadding="0" class="tableTotais">
						<tr class="class_linha_1 paddingTotais">
							<td id="lbl_qtd_cotas">Qtde Cotas:</td>
							<td id="total_de_cotas">0</td>
                            <td id="total_ultimo_reparte">0</td>
                            <td id="total_reparte_sugerido">0</td>
                            <td id="lbl_legenda">&nbsp;</td>
                            <c:if test="${estudo.idEstudoOrigemCopia != null}">
                                <td id="total_reparte_origem">0</td>
                            </c:if>
							<td id="total_reparte1">0</td>
							<td id="total_venda1" class="vermelho">0</td>
							<td id="total_reparte2">0</td>
							<td id="total_venda2" class="vermelho">0</td>
							<td id="total_reparte3">0</td>
							<td id="total_venda3" class="vermelho">0</td>
							<td id="total_reparte4">0</td>
							<td id="total_venda4" class="vermelho">0</td>
							<td id="total_reparte5">0</td>
							<td id="total_venda5" class="vermelho">0</td>
							<td id="total_reparte6">0</td>
							<td id="total_venda6" class="vermelho">0</td>
						</tr>
					</table>
				</c:if>
				<c:if test="${tipoExibicao == 'PARCIAL'}">
					<table border="0" cellspacing="0" cellpadding="0" class="tableTotais">
						<tr class="class_linha_1 paddingTotais">
							<td id="lbl_qtd_cotas">Qtde Cotas:</td>
							<td id="total_de_cotas">0</td>
                            <td id="total_ultimo_reparte">0</td>
                            <td id="total_reparte_sugerido">0</td>
							<td id="lbl_legenda">&nbsp;</td>
							<td id="total_juramento">0</td>
							<%--<td align="right" id="total_media_venda">0</td>--%>
							<td id="total_reparte1">0</td>
							<td id="total_venda1" class="vermelho">0</td>
							<td id="total_reparte2">0</td>
							<td id="total_venda2" class="vermelho">0</td>
							<td id="total_reparte3">0</td>
							<td id="total_venda3" class="vermelho">0</td>
							<td id="total_reparte4">0</td>
							<td id="total_venda4" class="vermelho">0</td>
						</tr>
					</table>
				</c:if>
				<span class="bt_novos" title="Imprimir">
                    <a href="${pageContext.request.contextPath}/distribuicao/analise/parcial/exportar?fileType=PDF&id=${estudo.id}">
                        <img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> Imprimir
				    </a>
				</span>
                <span class="bt_novos" title="Gerar Arquivo">
                    <a href="${pageContext.request.contextPath}/distribuicao/analise/parcial/exportar?fileType=XLS&id=${estudo.id}&tipoExibicao=${tipoExibicao}&numeroParcial=${lancamento.periodoLancamentoParcial.numeroPeriodo}">
                        <img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /> Arquivo
				    </a>
				</span>

				<c:choose>
					<c:when test="${lancamentoComEstudoLiberado}">
						<span class="bt_novos">
		                    <a href="javascript:return false;" id="naoLiberar">
		                        <img src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" alt="Liberar" hspace="5" border="0" /> Liberar
						    </a>
						</span>
					</c:when>
					<c:otherwise>
						<span class="bt_novos">
		                    <a href="javascript:return false;" id="liberar">
		                        <img src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" alt="Liberar" hspace="5" border="0" /> Liberar
						    </a>
						</span>
					</c:otherwise>
				</c:choose>
				
                <span class="bt_novos">
                    <a href="javascript:;" id="botaoVoltarTelaAnalise" onclick="$('#workspace').tabs('remove', $('#workspace').tabs('option', 'selected')); selectTabTitle('Analise de Estudos');">
                        <img src="${pageContext.request.contextPath}/images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" /> Voltar
				    </a>
				</span>
                <span class="bt_novos">
                    <a href="javascript:;" onclick="analiseParcialController.exibirCotasQueNaoEntraramNoEstudo();">
						<img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" alt="Cotas que não entraram no Estudo" hspace="5" border="0" /> Cotas que não entraram no Estudo
				    </a>
                </span>
            <c:if test="${tipoExibicao == 'NORMAL'}">
                <span class="bt_novos">
                    <a href="javascript:;" onclick="analiseParcialController.mudarBaseVisualizacao();">
                        <img src="${pageContext.request.contextPath}/images/ico_atualizar.gif" alt="Mudar Base de Visualização" hspace="5" border="0" />
                        <span>Mudar Base de Visualização</span>
					</a>
				</span>
            </c:if>
                <%--<br/>--%>
				<span style="font-weight: bold; font-size: 10px;" onclick="dataTableInit()">Saldo à Distribuir:</span>
				<span id="saldo_reparte" style="font-weight: bold; font-size: 10px;">${estudo.sobra == null ? 0 : estudo.sobra}</span>
			</div>
		</fieldset>
	</div>
     </div>

	<div id="dialog-cotas-estudos" title="Cotas que não entraram no estudo"
		style="display: none;">
		<fieldset style="width: 500px !important;">
			<legend>Pesquisar Cota</legend>

			<table width="500" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="63">Cota:</td>
					<td width="161">
						<input type="text" name="cotasQueNaoEntraramNoEstudo_cota" id="cotasQueNaoEntraramNoEstudo_cota" style="width: 60px;"/>
                    </td>
					<td width="46">Nome:</td>
					<td width="209">
						<input type="text" name="cotasQueNaoEntraramNoEstudo_nome" id="cotasQueNaoEntraramNoEstudo_nome" style="width: 185px;"/>
                    </td>
                    <td><span class="classPesquisar" onclick="analiseParcialController.cotasQueNaoEntraramNoEstudo();" style="margin: 0 5px;">
                    <a href="javascript:;">&nbsp;</a></span></td>
				</tr>
				<tr>
					<td>Motivo:</td>
					<td colspan="4">
						<select name="cotasQueNaoEntraramNoEstudo_motivo" id="cotasQueNaoEntraramNoEstudo_motivo" style="width: 408px">
							<option value="TODOS" selected="selected">Todas as Cotas</option>
                            <c:forEach items="${classificacaoCotaList}" var="classificacaoCota">
                                <c:if test="${classificacaoCota.codigo eq 'SM' || classificacaoCota.codigo eq 'VZ' || classificacaoCota.codigo eq 'FR'
                                || classificacaoCota.codigo eq 'CL' || classificacaoCota.codigo eq 'GN' || classificacaoCota.codigo eq 'SH'
                                || classificacaoCota.codigo eq 'SS' || classificacaoCota.codigo eq 'FN'}">
                                    <option value="<c:out value="${classificacaoCota.codigo}"/>"><c:out value="${classificacaoCota.texto}"/></option>
                                </c:if>
                            </c:forEach>
						</select>
					</td>
				</tr>
			</table>

		</fieldset>
		<fieldset style="width: 500px !important; margin-top: 10px;">
			<legend>Componentes</legend>
			<table width="503" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="67">Componente:</td>
					<td width="185">
						<select name="componenteCotasNaoSelec" id="componenteCotasNaoSelec" style="width: 170px;"
						    onchange="analiseParcialController.selecionarElementos(this.value, 'cotasQueNaoEntraramNoEstudo_elementos')">
							<option value="" selected="selected">Selecione...</option>
							<option value="tipo_ponto_venda">Tipo de Ponto de Venda</option>
							<option value="gerador_de_fluxo">Gerador de Fluxo</option>
							<option value="bairro">Bairro</option>
							<option value="regiao">Região</option>
							<option value="cotas_a_vista">Cotas A Vista</option>
							<option value="area_influencia">Área de Influência</option>
							<option value="distrito">Distrito</option>
							<option value="tipo_distribuicao_cota">Tipo de Distribuição Cota</option>
						</select>
					</td>
					<td width="52">Elemento:</td>
					<td width="186">
						<select id="cotasQueNaoEntraramNoEstudo_elementos" name="elementoCotasNaoSelec" style="width: 170px;">
							<option value="" selected="selected">Selecione...</option>
						</select>
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset style="width: 500px !important; margin-top: 10px;">
			<legend>Cotas que não entraram no estudo</legend>
			<table class="cotasEstudoGrid" id="cotasNaoSelec"></table>
			<div style="float: right; margin-top: 5px; margin-right: 60px;">
				<strong>Saldo:</strong> <span id="saldoReparteNaoSelec">0</span>
			</div>
		</fieldset>
	</div>

	<div id="dialog-mudar-base" title="Mudar Base de Visualização" style="display: none;">

		<fieldset style="width: 600px !important;">
			<legend>Base Produto</legend>

			<table width="600" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="45"><strong>Estudo:</strong></td>
					<td width="76">${estudo.id}</td>
					<td width="43"><strong>Código:</strong></td>
					<td width="42">${estudo.produtoEdicao.produto.codigo}</td>
					<td width="47"><strong>Produto:</strong></td>
					<td width="117">${estudo.produtoEdicao.produto.nome}</td>
					<td width="40"><strong>Edição:</strong></td>
					<td width="49">${estudo.produtoEdicao.numeroEdicao}</td>
				</tr>
			</table>
			<table width="600" border="0" cellpadding="2" cellspacing="1">
				<tr>
                    <td width="40"><strong>Class.:</strong></td>
                    <td width="49">${estudo.produtoEdicao.tipoClassificacaoProduto.descricao}</td>
					<td width="104"><strong>Chamada de Capa:</strong></td>
					<td width="385">${estudo.produtoEdicao.chamadaCapa}</td>
                    <td><a href="javascript:;" onclick="popup_edicoes_produto()"><img src="${pageContext.request.contextPath}/images/ico_editar.gif" alt="Adicionar Edição" border="0"/></a></td>
				</tr>
			</table>

		</fieldset>

		<fieldset style="width: 600px !important; margin-top: 10px;">
			<legend>Produtos Cadastrados</legend>
			<table class="prodCadastradosGrid" id="prodCadastradosGrid"></table>
		</fieldset>
	</div>

	<div id="dialog-cotas-detalhes" title="Pontos de Vendas" style="display: none;">

		<fieldset style="width: 690px !important; margin-top: 5px;">
			<legend>Cotas Cadastradas</legend>
			<table class="cotasDetalhesGrid" id="cotasDetalhesGrid"></table>
		</fieldset>

		<fieldset style="width: 690px !important; margin-top: 5px;">
			<legend>Cota</legend>

			<table width="686" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="50"><strong>Cota:</strong></td>
					<td width="43"><span id="numeroCotaD"></span></td>
					<td width="117"><strong>Nome:</strong></td>
					<td width="289"><span id="nomeCotaD"></span></td>
					<td width="58"><strong>Tipo:</strong></td>
					<td width="137"><span id="tipoCotaD"></span></td>
				</tr>
				<tr>
					<td><strong>Ranking:</strong></td>
					<td><span id="rankingCotaD"></span></td>
					<td><strong>Fat. médio mensal:</strong></td>
					<td><span id="faturamentoCotaD"></span></td>
					<td><strong>Período:</strong></td>
					<td><span id="mesAnoCotaD"></span></td>
				</tr>
			</table>
		</fieldset>

		<fieldset style="width: 690px !important; margin-top: 5px;" id="dados-mix">
			<legend>MIX</legend>

			<table width="686" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td style="width: 130px;"><strong>Código: </strong> ${estudo.produtoEdicao.produto.codigo}</td>
					<td><strong>Produto: </strong> ${estudo.produtoEdicao.nomeComercial}</td>
				</tr>
			</table>
			<table width="686" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td><strong>Rep.Mín.: </strong><span id="mixRepMin">05</span></td>
					<td><strong>Rep. Máx.: </strong><span id="mixRepMax">9.999</span></td>
					<td><strong>Usuário: </strong><span id="mixUsuario">Rodrigue</span></td>
					<td style="width: 160px;"><strong>Data Manutenção: </strong><span id="mixDataAlteracao">28/03/2012 10:56</span></td>
				</tr>
			</table>
		</fieldset>
        <fieldset style="width: 690px !important; margin-top: 5px;" id="dados-fixacao">
            <legend>Fixação</legend>

            <table width="686" border="0" cellpadding="2" cellspacing="1">
                <tr>
                    <td style="width: 130px;"><strong>Código: </strong> ${estudo.produtoEdicao.produto.codigo}</td>
                    <td><strong>Produto: </strong> ${estudo.produtoEdicao.nomeComercial}</td>
                </tr>
            </table>
            <table width="686" border="0" cellpadding="2" cellspacing="1">
                <tr>
                    <td><strong>Ed. Inicial: </strong><span id="fxEdicaoInicial">05</span></td>
                    <td><strong>Ed. Final: </strong><span id="fxEdicaoFinal">10</span></td>
                    <td><strong>Ed. Atendidas: </strong><span id="fxEdicoesAtendidas">6</span></td>
                    <td><strong>Qtde. Edições: </strong><span id="fxQuantidadeEdicoes">6</span></td>
                    <td><strong>Exemplares: </strong><span id="fxQuantidadeExemplares">20</span></td>
                    <td style="width: 160px;"><strong>Data Manutenção: </strong><span id="fxDataAlteracao">28/03/2012 10:56</span></td>
                </tr>
            </table>
        </fieldset>
	</div>

    <div id="AP_dialog-defineReparte" title="Define Reparte por PDV" style="display:none;">
        <fieldset style="width:605px !important;">
            <legend>Dados da Cota</legend>
            <table width="500" border="0" cellspacing="1" cellpadding="1">
                <tr>
                    <td width="42"><strong>Cota:</strong></td>
                    <td width="92"><span class="numeroCota">1223</span></td>
                    <td width="44"><strong>Nome:</strong></td>
                    <td width="155"><span class="nomeCota">Antonio José da Silva</span></td>
                    <%--<td width="151">&nbsp;</td>--%>
                </tr>
            </table>

        </fieldset>
        <br clear="all" />
        <fieldset style="width:605px !important; margin-top:10px;">
            <legend>Dados do Produto</legend>
            <table width="500" border="0" cellspacing="1" cellpadding="1">
                <tr>
                    <td width="42"><strong>Código:</strong></td>
                    <td width="92"><span class="codigoProduto">${estudo.produtoEdicao.produto.codigo}</span></td>
                    <td width="44"><strong>Produto:</strong></td>
                    <td width="155"><span class="nomeProduto">${estudo.produtoEdicao.produto.nomeComercial}</span></td>
                    <td width="44"><strong>Classificação:</strong></td>
                    <td width="155"><span class="tipoClassificacaoProduto">${estudo.produtoEdicao.tipoClassificacaoProduto.descricao}</span></td>
                </tr>
            </table>

        </fieldset>
        <br clear="all" />
        <fieldset style="width:605px !important; margin-top:10px;">
            <legend>PDV da Cota</legend>
            <table class="pdvCotaGrid_AP"></table>
            <table width="600" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="312">&nbsp;
                    </td>
                    <td width="174">&nbsp;</td>
                    <td width="71" align="center">
                    	<span class="reparteCota" id="reparteCota">0</span></td>
                    <td width="43">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="3" align="right">Manter Fixa</td>
                    <td><input name="input2" type="checkbox" value="" /></td>
                </tr>
            </table>
        </fieldset>

    </div>
	
	<form id="form-edicoes-produtos">
	    <div id="dialog-edicoes-produtos" title="Pesquisar Edições de Produto" style="display:none;">
	        <fieldset style="width:700px!important;">
	            <legend>Pesquisar Produto</legend>
	
	            <table width="700" border="0" cellpadding="2" cellspacing="1" class="filtro">
	                <tr>
	                    <td width="36">Código:</td>
	                    <td width="76"><input type="text" class="inputCodigoEB" name="codigoProduto" id="inputCodigoProduto" style="width:60px;" /></td>
	                    <td width="40">Produto:</td>
	                    <td width="180"><input type="text" class="inputProdutoEB" name="nomeProduto" id="inputNomeProduto" style="width:160px;" /></td>
	                    <td width="35">Edição:</td>
	                    <td width="61"><input type="text" class="inputEdicaoEB" name="edicao" id="inputNumeroEdicao" style="width:60px;" /></td>
	                    <td style="width: 80px;">Classificação:</td>
	                    <td>
	                        <select name="idClassificacao" id="filtroClassificacao" style="width:120px;">
	                            <option value="-1">SELECIONE</option>
	                            <c:forEach items="${classificacaoList}" var="tipoClassificacao">
	                                <option value="<c:out value="${tipoClassificacao.id}"/>" ${tipoClassificacao.descricao eq 'NORMAL'? 'selected="selected"' : '' }><c:out value="${tipoClassificacao.descricao}"/></option>
	                            </c:forEach>
	                        </select>
	                    </td>
	                    <td width="47"><span class="classPesquisar" style="margin: 0 5px;"><a href="javascript:;">&nbsp;</a></span></td>
	                </tr>
	            </table>
	
	        </fieldset>
	
	        <fieldset style="width:700px!important; margin-top:10px;">
	            <legend>Edições do Produto</legend>
	            <table id="edicaoProdCadastradosGrid"></table>
	        </fieldset>
	    </div>
	</form>

    <div id="dialog-detalhes" title="Capa">
		<img src="${pageContext.request.contextPath}/capa/getCapaEdicaoJson?codigoProduto=${estudo.produtoEdicao.produto.codigo}&numeroEdicao=${estudo.produtoEdicao.numeroEdicao}" width="235" height="314" />
	</div>
	
	<div id="dialog-confirmacao-cota-suspensa" title="Confirmação Cota suspensa" style="display: none; width: auto; min-height: 30px; height: 30px" >
		<p>Cota Suspensa, deseja adicionar?</p>
	</div>

    <div id="previewImagemCapa" title="Capa" style="display: none;"><img src="" alt="Imagem Capa" width="180" height="250"/></div>

    <script type="text/javascript">
		 $(function(){
			analiseParcialController.init('${estudo.id}', '${faixaDe}', '${faixaAte}', '${tipoExibicao}');
		 });
 	</script>

<style>
    .dt-container {
        clear: both; width: 990px; padding-top: 20px;
        font-family: Arial, Helvetica, sans-serif;
        font-size: 11px;
        color: #000;
        display: none;
    }
    .dt-container table {
        border-collapse: collapse;
        border-spacing: 0;
        border: 1px solid #ccc;
        table-layout: fixed;
        /*word-wrap: break-word;*/
    }
    .dt-container thead th {
        font-weight: bold;
        border-right: 1px solid #ddd;
        border-left: 1px solid #fff;
    }
    .dt-container thead th:first-child {
        border-left: 1px solid #ccc;
    }
    .dt-container tbody {
        overflow-x: hidden !important;
    }
    .dt-container tbody tr:hover {
        background: #d9ebf5 !important;
    }
    .less-padding th {
        padding: 2px 1px !important;
    }
    .dt-container tfoot th {
        text-align: right;
    }
    .dt-container tbody tr.odd {
        background: #f7f7f7;
    }
    .dt-container tbody td {
        /*padding: 3px 10px;*/
        padding: 2px 4px;
        border-right: 1px solid #ddd;
        border-left: 1px solid #fff;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    .dt-container tbody td:first-child {
        border-left: 1px solid #ccc;
    }
    #dt-test_wrapper tfoot input {
        width: 100%;
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        -webkit-box-sizing: border-box;
    }
    td.tred { color: red; font-weight: bold; }
    td.tbold { font-weight: bold; }
    td.tleft { text-align: left; }
    td.tright { text-align: right; }
    td.tcenter { text-align: center; }

    /* Data Table Overwrites */
    table.dataTable thead {
        background: #fafafa url(${pageContext.request.contextPath}/scripts/flexigrid-1.1/css/images/fhbg.gif) repeat-x bottom;
    }
    table.dataTable thead th {
        /*padding: 3px 18px 3px 10px;*/
        padding: 2px 16px 2px 4px;
        border-bottom: 1px solid white;
    }
    table.dataTable tfoot th {
        /*padding: 3px 18px 3px 10px;*/
        padding: 2px 4px;
    }
    table.dataTable tr.odd td.sorting_1 {
        background: #e3e3e3;
        /*border-bottom: 1px solid #e3e3e3;*/
    }
    table.dataTable tr.even td.sorting_1 {
        background: #f3f3f3;
    }
</style>
<div class="dt-container">
    <table id="dt-test" class="dataTable">
        <%--<thead>
            <th>Cota</th>
            <th>Class.</th>
            <th>Nome</th>
            <th>NPDV</th>
            <th>Últ. Rep.</th>
            <th>Rep. Sug.</th>
            <th>LEG</th>
            <th>REP</th>
            <th>VDA</th>
            <th>REP</th>
            <th>VDA</th>
            <th>REP</th>
            <th>VDA</th>
            <th>REP</th>
            <th>VDA</th>
            <th>REP</th>
            <th>VDA</th>
            <th>REP</th>
            <th>VDA</th>
        </thead>--%>
        <tfoot>
            <tr>
                <th colspan="2" style="text-align: left">Qtde Cotas:</th>
                <th style="text-align: left"></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            <tr class="less-padding">
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
                <th><input type="text"/></th>
            </tr>
        </tfoot>
    </table>
</div>