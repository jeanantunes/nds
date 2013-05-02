<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/analiseParcial.js"></script>
<script language="javascript" type="text/javascript">
$(function() {   
    $('.legendas').tipsy({gravity: $.fn.tipsy.autoNS});
  });

function mostraDados(){
	$('.detalhesDados').show();
	}
function escondeDados(){
	$('.detalhesDados').hide();
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
.detalhesDados{position:absolute; display:none; background:#fff; border:1px solid #ccc; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); }
.inputBaseNumero{width: 60px;}
.inputBaseNome{width: 170px;}
.paddingTotais td {padding-right: 3px;}
.bt_novos{width: 0px;}
</style>

</head>

<body>
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Base de Estudo < evento > com < status >.</b></p>
	</div>
    
    	<div class="detalhesDados">
    	  <table width="976" border="0" cellpadding="2" cellspacing="2" class="dadosTab" id="tabelaDetalheAnalise">
    	    <tr id="edicoes">
    	      <td class="class_linha_1"><strong>Edição:</strong></td>
  	        </tr>
    	    <tr id="dataLancamentos">
    	      <td width="165" class="class_linha_2"><strong>Data Lançamento:</strong></td>
  	        </tr>
    	    <tr id="repartes">
    	      <td class="class_linha_1"><strong>Reparte:</strong></td>
  	        </tr>
    	    <tr id="vendas">
    	      <td class="class_linha_2"><strong>Venda:</strong></td>
  	        </tr>
  	    </table>
    	</div>
		<fieldset class="classFieldset">
			<legend> Pesquisar </legend>
			<input type="hidden" id="produtoEdicaoId" value="${estudoCota.estudo.produtoEdicao.id}" /> <input type="hidden" id="faixaDe"
				value="${faixaDe}" />
			<input type="hidden" id="faixaAte" value="${faixaAte}" />
			<input type="hidden" id="numeroEdicao" value="${estudoCota.estudo.produtoEdicao.numeroEdicao}" />
			<input type="hidden" id="codigoProduto" value="${estudoCota.estudo.produtoEdicao.produto.codigo}" />
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="51">Código:</td>
					<td width="65">${estudoCota.estudo.produtoEdicao.produto.codigo}</td>
					<td width="54">Produto:</td>
					<td width="210">${estudoCota.estudo.produtoEdicao.produto.nomeComercial}</td>
					<td width="45">Edição:</td>
					<td width="140">${estudoCota.estudo.produtoEdicao.numeroEdicao}</td>
					<td width="45">Estudo:</td>
					<td width="141"><span id="estudoId">${estudoCota.estudo.id}</span></td>
					<td width="83">Nro. da Parcial:</td>
					<td width="65"></td>
				</tr>
			</table>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="75">Classificação:</td>
					<td width="91">${estudoCota.estudo.produtoEdicao.produto.tipoClassificacaoProduto.descricao}</td>
					<td width="60">Segmento:</td>
					<td width="86">${estudoCota.estudo.produtoEdicao.produto.tipoSegmentoProduto.descricao}</td>
					<td width="78">Ordenar por:</td>
					<td width="143"><select name="select5" id="filtroOrdenarPor" style="width: 138px;"
						onchange="analiseParcialController.apresentarOpcoesOrdenarPor(this.value);">
							<option selected="selected" value="selecione">Selecione...</option>
							<option value="reparte">Reparte</option>
							<option value="ranking">Ranking</option>
							<option value="percentual_de_venda">% de Venda</option>
							<option value="reducao_de_reparte">R de Reparte</option>
					</select></td>
					<td width="55">Reparte:</td>
					<td width="49"><input type="text" name="textfield6" id="textfield6" style="width: 40px;" /></td>
					<td width="72">Abrangência:</td>
					<td width="32">${estudoCota.estudo.produtoEdicao.produto.percentualAbrangencia}</td>
					<td width="84">Pacote Padrão:</td>
					<td width="64"><input type="text" name="textfield6" id="textfield7" style="width: 30px;" /></td>
				</tr>
			</table>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="84">Componente:</td>
					<td width="188"><select id="componentes" name="componentes" style="width: 170px;"
						onchange="analiseParcialController.selecionarElementos(this.value, 'elementos')">
							<option value="null" selected="selected">Selecione...</option>
							<option value="tipo_ponto_venda">Tipo de Ponto de Venda</option>
							<option value="gerador_de_fluxo">Gerador de Fluxo</option>
							<option value="bairro">Bairro</option>
							<option value="regiao">Região</option>
							<option value="cotas_a_vista">Cotas A Vista</option>
							<option value="cotas_novas">Cotas Novas</option>
							<option value="area_influencia">Área de Influência</option>
							<option value="distrito">Distrito</option>
					</select></td>
					<td width="60">Elemento:</td>
					<td width="179"><select id="elementos" name="elementos" style="width: 170px;"
						onchange="analiseParcialController.filtrarOrdenarPor(${estudoCota.estudo.id})">
							<option selected="selected">Selecione...</option>
					</select></td>
					<td width="271"><span id="opcoesOrdenarPor" style="display: none;" class="label"> <span id="label_reparte"
							style="display: none;" class="label"> Reparte: </span> <span id="label_reducao_de_reparte" style="display: none;"
							class="label"> % Dê: </span> <span id="label_ranking" style="display: none;" class="label"> Ranking: </span> <span
							id="label_percentual_de_venda" style="display: none;" class="label"> % Venda: </span> <input id="ordenarPorDe" type="text"
							style="width: 60px;" /> Até <input id="ordenarPorAte" type="text" style="width: 60px;" /> Exs. <a
							href="javascript:filtrarOrdenarPor(${estudoCota.estudo.id});"> <img
								src="${pageContext.request.contextPath}/images/ico_check.gif" alt="Confirmar" border="0" />
						</a>
					</span></td>
					<td width="35" align="center"><a href="javascript:;" onclick="mostraDados();"><img
							src="${pageContext.request.contextPath}/images/ico_boletos.gif" title="Exibir Detalhes" width="19" height="15" border="0" /></a></td>
					<td width="97"><span class="bt_novos"> <a href="javascript:;" onclick="analiseParcialController.verCapa();">
								<img src="${pageContext.request.contextPath}/images/ico_detalhes.png" alt="Ver Capa" hspace="5" border="0" /> Ver Capa
						</a>
					</span></td>
				</tr>
			</table>
		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="classFieldset">
			<legend>Base de Estudo / Análise</legend>
			<div class="grids" style="display: block;">
				<c:if test="${tipoExibicao == 'NORMAL'}">
					<table width="950" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td width="505" align="right"><strong>Edições Base:</strong></td>
							<td width="67" align="center" class="header_table" id="edicao_base_1">1</td>
							<td width="67" align="center" class="header_table" id="edicao_base_2">2</td>
							<td width="67" align="center" class="header_table" id="edicao_base_3">3</td>
							<td width="67" align="center" class="header_table" id="edicao_base_4">4</td>
							<td width="67" align="center" class="header_table" id="edicao_base_5">5</td>
							<td width="67" align="center" class="header_table" id="edicao_base_6">6</td>
							<td width="13" align="center">&nbsp;</td>
						</tr>
					</table>
				</c:if>
				<c:if test="${tipoExibicao == 'PARCIAL'}">
					<table width="950" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td width="505" align="right"><strong>Edições Base:</strong></td>
							<td width="103" align="center" class="header_table" id="edicao_base_1">3ª Parcial</td>
							<td width="100" align="center" class="header_table" id="edicao_base_2">2ª Parcial</td>
							<td width="100" align="center" class="header_table" id="edicao_base_3">1ª Parcial</td>
							<td width="100" align="center" class="header_table" id="edicao_base_4">Acumulado</td>
							<td width="20" align="center">&nbsp;</td>
						</tr>
					</table>
				</c:if>
				<table class="baseEstudoGrid" id="baseEstudoGridParcial"></table>

				<c:if test="${tipoExibicao == 'NORMAL'}">
					<table width="950" border="0" cellspacing="2" cellpadding="2">
						<tr class="class_linha_1 paddingTotais">
							<td width="80">Qtde Cotas:</td>
							<td width="145" id="total_de_cotas">0</td>
							<td width="58" align="right" id="total_reparte">0</td>
							<td width="25" align="right">&nbsp;</td>
							<td width="48" align="right" id="total_juramento">0</td>
							<td width="57" align="right" id="total_media_venda">0</td>
							<td width="56" align="right" id="total_ultimo_reparte">0</td>
							<td width="29" align="right" id="total_reparte1">0</td>
							<td width="29" align="right" id="total_venda1"><span class="vermelho">0</span></td>
							<td width="29" align="right" id="total_reparte2">0</td>
							<td width="29" align="right" id="total_venda2"><span class="vermelho">0</span></td>
							<td width="29" align="right" id="total_reparte3">0</td>
							<td width="29" align="right" id="total_venda3"><span class="vermelho">0</span></td>
							<td width="29" align="right" id="total_reparte4">0</td>
							<td width="29" align="right" id="total_venda4"><span class="vermelho">0</span></td>
							<td width="29" align="right" id="total_reparte5">0</td>
							<td width="29" align="right" id="total_venda5"><span class="vermelho">0</span></td>
							<td width="29" align="right" id="total_reparte6">0</td>
							<td width="29" align="right" id="total_venda6"><span class="vermelho">0</span></td>
							<td width="10" align="right">&nbsp;</td>
						</tr>
					</table>
				</c:if>
				<c:if test="${tipoExibicao == 'PARCIAL'}">
					<table width="950" border="0" cellspacing="2" cellpadding="2">
						<tr class="class_linha_1 paddingTotais">
							<td width="80">Qtde Cotas:</td>
							<td width="145" id="total_de_cotas">0</td>
							<td width="58" align="right" id="total_reparte">0</td>
							<td width="25" align="right">&nbsp;</td>
							<td width="48" align="right" id="total_juramento">0</td>
							<td width="57" align="right" id="total_media_venda">0</td>
							<td width="56" align="right" id="total_ultimo_reparte">0</td>
							<td width="46" align="right" id="total_reparte1">0</td>
							<td width="46" align="right" id="total_venda1"><span class="vermelho">0</span></td>
							<td width="46" align="right" id="total_reparte2">0</td>
							<td width="46" align="right" id="total_venda2"><span class="vermelho">0</span></td>
							<td width="46" align="right" id="total_reparte3">0</td>
							<td width="46" align="right" id="total_venda3"><span class="vermelho">0</span></td>
							<td width="46" align="right" id="total_reparte4">0</td>
							<td width="46" align="right" id="total_venda4"><span class="vermelho">0</span></td>
							<td width="15" align="right">&nbsp;</td>
						</tr>
					</table>
				</c:if>
				<span class="bt_novos" title="Imprimir"> <a href="exportar?fileType=PDF&id=${estudoCota.estudo.id}"> <img
						src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" /> Imprimir
				</a>
				</span> <span class="bt_novos" title="Gerar Arquivo"> <a href="exportar?fileType=XLS&id=${estudoCota.estudo.id}"> <img
						src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /> Arquivo
				</a>
				</span> <span class="bt_novos"> <a href="javascript:return false;" id="liberar"> <img
						src="${pageContext.request.contextPath}/images/ico_distribuicao_bup.gif" alt="Liberar" hspace="5" border="0" /> Liberar
				</a>
				</span> <span class="bt_novos"> <a href="javascript:;" onclick=""> <img
						src="${pageContext.request.contextPath}/images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" /> Voltar
				</a>
				</span> <span class="bt_novos"> <a href="javascript:;" onclick="analiseParcialController.exibirCotasQueNaoEntraramNoEstudo();">
						<img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" alt="Cotas que não entraram no Estudo" hspace="5"
						border="0" /> Cotas que não entraram no Estudo
				</a>
				<c:if test="${tipoExibicao == 'NORMAL'}">
					</span> <span class="bt_novos"> <a href="javascript:;" onclick="analiseParcialController.mudarBaseVisualizacao();"> <img
							src="${pageContext.request.contextPath}/images/ico_atualizar.gif" alt="Mudar Base de Visualização" hspace="5" border="0" />
							Mudar Base de Visualização
					</a>
				</c:if>
				</span>
				<span style="font-weight: bold; font-size: 10px;">Saldo à Distribuir:</span>
				<span id="saldo_reparte" style="font-weight: bold; font-size: 10px;">${estudoCota.estudo.reparteDistribuir}</span>
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
					<td width="161"><input type="text" name="textfield"
						id="textfield" style="width: 60px;" /></td>
					<td width="46">Nome:</td>
					<td width="209"><input type="text" name="textfield2"
						id="textfield2" style="width: 185px;" /></td>
				</tr>
				<tr>
					<td>Motivo:</td>
					<td colspan="3"><select name="select" id="select"
						style="width: 408px">
							<option selected="selected">Todas as Cotas</option>
					</select></td>
				</tr>
			</table>

		</fieldset>
		<fieldset style="width: 500px !important; margin-top: 10px;">
			<legend>Componentes</legend>
			<table width="503" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="67">Componente:</td>
					<td width="185"><select name="componenteCotasNaoSelec"
						id="componenteCotasNaoSelec" style="width: 170px;">
					</select></td>
					<td width="52">Elemento:</td>
					<td width="186"><select name="elementoCotasNaoSelec"
						id="elementoCotasNaoSelec" style="width: 170px;">
					</select></td>
				</tr>
			</table>
		</fieldset>
		<fieldset style="width: 500px !important; margin-top: 10px;">
			<legend>Cotas que não entraram no estudo</legend>
			<table class="cotasEstudoGrid" id="cotasNaoSelec"></table>
			<div style="float: right; margin-top: 5px; margin-right: 60px;">
				<strong>Saldo:</strong> <span id="saldoReparteNaoSelec">999</span>
			</div>
		</fieldset>
	</div>

	<div id="dialog-mudar-base" title="Mudar Base de Visualização" style="display: none;">

		<fieldset style="width: 500px !important;">
			<legend>Base Produto</legend>

			<table width="500" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="45"><strong>Estudo:</strong></td>
					<td width="76">${estudoCota.estudo.id}</td>
					<td width="43"><strong>Código:</strong></td>
					<td width="42">${estudoCota.estudo.produtoEdicao.produto.codigo}</td>
					<td width="47"><strong>Produto:</strong></td>
					<td width="117">${estudoCota.estudo.produtoEdicao.produto.nome}</td>
					<td width="40"><strong>Edição:</strong></td>
					<td width="49">${estudoCota.estudo.produtoEdicao.numeroEdicao}</td>
				</tr>
			</table>
			<table width="500" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="104"><strong>Chamada de Capa:</strong></td>
					<td width="385">${estudoCota.estudo.produtoEdicao.chamadaCapa}</td>
				</tr>
			</table>

		</fieldset>

		<fieldset style="width: 500px !important; margin-top: 10px;">
			<legend>Produtos Cadastrados</legend>
			<table class="prodCadastradosGrid" id="prodCadastradosGrid"></table>
		</fieldset>
	</div>

	<div id="dialog-cotas-detalhes" title="Pontos de Vendas"
		style="display: none;">

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
					<td width="78"><strong>Nome:</strong></td>
					<td width="289"><span id="nomeCotaD"></span></td>
					<td width="58"><strong>Tipo:</strong></td>
					<td width="137"><span id="tipoCotaD"></span></td>
				</tr>
				<tr>
					<td><strong>Ranking:</strong></td>
					<td><span id="rankingCotaD"></span></td>
					<td><strong>Faturamento:</strong></td>
					<td><span id="faturamentoCotaD"></span></td>
					<td><strong>Mês/Ano:</strong></td>
					<td><span id="mesAnoCotaD"></span></td>
				</tr>
			</table>
		</fieldset>

		<fieldset style="width: 690px !important; margin-top: 5px;">
			<legend>MIX</legend>

			<table width="686" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="48"><strong>Código:</strong></td>
					<td width="52">${estudoCota.estudo.produtoEdicao.produto.codigo}</td>
					<td width="54"><strong>Produto:</strong></td>
					<td width="511">${estudoCota.estudo.produtoEdicao.nomeComercial}</td>
				</tr>
			</table>
			<table width="686" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="55"><strong>Rep.Mín.:</strong></td>
					<td width="41">05</td>
					<td width="60"><strong>Rep. Máx.:</strong></td>
					<td width="79">9.999</td>
					<td width="49"><strong>Usuário:</strong></td>
					<td width="141">Rodrigue</td>
					<td width="105"><strong>Data Manutenção.:</strong></td>
					<td width="115">28/03/2012 10:56</td>
				</tr>
			</table>
		</fieldset>
	</div>
	
	<div id="dialog-detalhes" title="Capa">
		<img src="${pageContext.request.contextPath}/capa/getCapaEdicaoJson?codigoProduto=${estudoCota.estudo.produtoEdicao.produto.codigo}&numeroEdicao=${estudoCota.estudo.produtoEdicao.numeroEdicao}" width="235" height="314" />
	</div>

	<script type="text/javascript">
		 $(function(){
			analiseParcialController.init('${estudoCota.estudo.id}', '${faixaDe}', '${faixaAte}', '${tipoExibicao}'); 
		 });
 	</script>
  </body>
</html>