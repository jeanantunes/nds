<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>

<script type="text/javascript">

$(function() {
	produtoEdicaoController.init();
});
</script>

<style>
label{ vertical-align:super;}
#produtoEdicaoController-dialog-novo,
#produtoEdicaoController-dialog-capa, 
#produtoEdicaoController-dialog-excluir-capa,
#produtoEdicaoController-dialog-excluir{
	display:none;
}
#produtoEdicaoController-dialog-novo label{width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px;}
.prodLinhas{display:none;}
.ui-tabs .ui-tabs-panel {
   padding: 6px!important;
}
.ldForm{float:left; width:666px!important; margin-left:15px;}
fieldset {
    margin-right:0px!important;
}
.ldPesq{float:left; width:210px;}
</style>
</head>

<body>

<div id="produtoEdicaoController-dialog-capa" title="Incluir Imagem Capa">
	<br />
	<p><input type="file" size="30" id="produtoEdicaoController-file01" name="file01" /></p>
</div>

<div id="produtoEdicaoController-dialog-excluir-capa" title="Excluir Capa">
	<p>Confirma a exclus&atilde;o desta Capa?</p>
</div>

<div id="produtoEdicaoController-dialog-excluir" title="Excluir Edi&ccedil;&atilde;o">
	<p>Confirma a exclus&atilde;o desta Edi&ccedil;&atilde;o?</p>
</div>

<!--  INICIO POPUP CADASTRO EDICAO -->
<form id="produtoEdicaoController-formUpload" name="formUpload" method="post" enctype="multipart/form-data">

<div id="produtoEdicaoController-dialog-novo" title="Incluir Nova Edi&ccedil;&atilde;o">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialogMensagemNovo" name="messageDialog"/>
	</jsp:include> 
	
		<div id="produtoEdicaoController-tabEdicoes">
			<ul>
				<li><a id="produtoEdicaoController-tabIdentificacao" href="#produtoEdicaoController-tabEdicoes-1">Identifica&ccedil;&atilde;o</a></li>
				<li><a id="produtoEdicaoController-tabCaractLancto" href="#produtoEdicaoController-tabEdicoes-2">Caracter&iacute;sticas do Lan&ccedil;amento</a></li>
				<li><a id="produtoEdicaoController-tabSegmentacao" href="#produtoEdicaoController-tabEdicoes-3">Segmenta&ccedil;&atilde;o</a></li>
			</ul>
			
			<div id="produtoEdicaoController-tabEdicoes-1">
				<input type="hidden" id="produtoEdicaoController-idProdutoEdicao" name="idProdutoEdicao" />
				<div class="ldPesq">
					<fieldset id="produtoEdicaoController-pesqProdutos" style="width:200px!important;">
						<legend>Produtos Pesquisados</legend>
						<table class="prodsPesqGrid"></table>
					</fieldset>
					
					<span class="bt_novos"><a href="javascript:;" onclick="produtoEdicaoController.salvarProdutoEdicao(false);"><img src="${pageContext.request.contextPath}/images/ico_add.gif" border="0" /></a></span>
				</div>
				
				<div class="ldForm">
					<fieldset style="width:655px!important; margin-bottom:5px;">
						<legend>Identifica&ccedil;&atilde;o</legend>
						<table width="648" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="181">C&oacute;digo:</td>
									<td width="100" colspan="3"><input type="text" name="codigoProdutoEdicao" id="produtoEdicaoController-codigoProdutoEdicao" style="width:100px;" /></td>
									
									<td width="90">&nbsp;</td>
									<td width="108">&nbsp;</td>
									<td width="153" rowspan="8" align="center">
										
										<div id="produtoEdicaoController-div_imagem_capa">
											<img alt="Capa" src="" width="144" height="185" />
										</div>
										
										<br clear="all" />
										<a href="javascript:;" onclick="produtoEdicaoController.popup_excluir_capa();">
											<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir Capa" width="15" height="15" hspace="5" vspace="3" border="0" />
										</a>
									</td>
								</tr>
								<tr>
									<td>Nome Publica&ccedil;&atilde;o:</td>
									<td colspan="5"><input type="text" name="nomePublicacao" id="produtoEdicaoController-nomePublicacao" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td>Nome Comercial Produto:</td>
									<td colspan="5"><input type="text" name="nomeComercialProduto" id="produtoEdicaoController-nomeComercialProduto" style="width:340px;" /></td>
								</tr>
								<tr>
									<td>Fornecedor:</td>
									<td colspan="5"><input type="text" name="nomeFornecedor" id="produtoEdicaoController-nomeFornecedor" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td>Situa&ccedil;&atilde;o:</td>
									<td colspan="5"><input type="text" name="situacao" id="produtoEdicaoController-situacao" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td>Edi&ccedil;&atilde;o:</td>
									<td><input type="text" name="numeroEdicao" id="produtoEdicaoController-numeroEdicao" style="width:50px;" /></td>
									<td>PED:</td>
									<td><input type="text" name="ped" id="produtoEdicaoController-ped" style="width:50px;" /></td>
									<td>Pct. Padr&atilde;o:</td>
									<td><input type="text" name="pacotePadrao" id="produtoEdicaoController-pacotePadrao" style="width:50px;" /></td>
								</tr>
							
								<tr>
									<td>Tipo de Lan&ccedil;amento:</td>
									<td colspan="3">
										<select name="tipoLancamento" id="produtoEdicaoController-tipoLancamento" style="width:160px;" >
											<option value="">Selecione...</option>
											<option value="LANCAMENTO">Lan&ccedil;amento</option>
											<option value="PARCIAL">Ed. Parcial</option>
											<option value="RELANCAMENTO">Relan&ccedil;amento</option>
											<option value="REDISTRIBUICAO">Redistribui&ccedil;&atilde;o</option>
											<option value="SUPLEMENTAR">Supl. Compuls</option>
										</select>
									</td>
									<td>N&ordm; Lancto:</td>
									<td><input type="text" name="numeroLancamento" id="produtoEdicaoController-numeroLancamento" style="width:50px;" maxlength="9" /></td>
								</tr>
								<tr>
									<td>Capa da Edi&ccedil;&atilde;o:</td>
									<td colspan="5"><input type="file" name="imagemCapa" id="produtoEdicaoController-imagemCapa" style="width:340px;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:220px!important; margin-bottom:2px; float:right; margin-right:0px;">
						<legend>Reparte</legend>
						<table width="190" border="0" cellspacing="1" cellpadding="2">
							<thead />
							<tbody>
								<tr>
									<td width="103">Previsto:</td>
									<td width="80"><input type="text" name="repartePrevisto" id="produtoEdicaoController-repartePrevisto" style="width:80px; float:left;" /></td>
								</tr>
								<tr>
									<td>Exp. Venda(%):</td>
									<td><input type="text" name="expectativaVenda" id="produtoEdicaoController-expectativaVenda" style="width:80px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td>Promocional:</td>
									<td><input type="text" name="repartePromocional" id="produtoEdicaoController-repartePromocional" style="width:80px; float:left;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:350px!important; margin-bottom:2px; float:left;">
						<legend>Pre&ccedil;o da Capa</legend>
						<table width="300" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="60">Previsto:</td>
									<td width="90"><input type="text" name="precoPrevisto" id="produtoEdicaoController-precoPrevisto" style="width:70px; float:left;" /></td>
									<td width="30">Real:</td>
									<td width="120"><input type="text" name="precoVenda" id="produtoEdicaoController-precoVenda" style="width:70px; text-align:right;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:350px!important; margin-bottom:2px; float:left;">
						<legend>Data Lan&ccedil;amento</legend>
						<table width="300" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="60">Previsto:</td>
									<td width="90"><input type="text" name="dataLancamentoPrevisto" id="produtoEdicaoController-dataLancamentoPrevisto" style="width:70px;" /></td>
									<td width="30">Real:</td>
									<td width="120"><input type="text" name="dataLancamento" id="produtoEdicaoController-dataLancamento" style="width:70px; text-align:center;" disabled="disabled" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>	
				    <fieldset style="width: 653px !important; margin-bottom: 2px; float: left;">
				     <legend>Data Recolhimento</legend>
					   	<table border="0" cellSpacing="1" cellPadding="1" width="562">
					      <tbody><tr>
					        <td width="60">Previsto:</td>
					        <td width="90"><input style="width: 70px; float: left;" id="produtoEdicaoController-dataRecolhimentoPrevisto" name="dataRecolhimentoPrevisto" type="text"></td>
					        <td width="30" align="right">Real:</td>
					        <td width="90"><input style="width: 70px; text-align: right;" id="produtoEdicaoController-dataRecolhimentoReal" disabled="disabled" name="dataRecolhimentoReal" type="text"></td>
					        <td width="170" align="right">Semana de Recolhimento:</td>
					        <td width="180"><input style="width: 70px; float: left;" id="produtoEdicaoController-semanaRecolhimento" disabled="disabled" name="semanaRecolhimento" type="text"></td>
					      </tr>
					      </tbody>
					    </table>
				    </fieldset>
				</div>
				<br clear="all" />
			</div>
			
			<div id="produtoEdicaoController-tabEdicoes-2">
				<div class="ldPesq">
					<fieldset id="produtoEdicaoController-pesqProdutos" style="width:200px!important;">
						<legend>Produtos Pesquisados</legend>
						<table class="prodsPesqGrid"></table>
					</fieldset>
					
					<span class="bt_novos"><a href="javascript:;" onclick="produtoEdicaoController.salvarProdutoEdicao();" rel="tipsy" title="Incluir Novo Produto"><img src="${pageContext.request.contextPath}/images/ico_add.gif" border="0" /></a></span>
				</div>
				
				<div class="ldForm">
					<fieldset style="width:350px!important; margin-bottom:5px;">
						<legend>Caracter&iacute;sticas do L&ccedil;amento</legend>
						<table width="345" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="145">Categoria:</td>
									<td width="193">
										<select name="select2" id="produtoEdicaoController-select2" style="width:180px;" >
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Cod. de Barras:</td>
									<td><input type="text" name="codigoDeBarras" id="produtoEdicaoController-codigoDeBarras" style="width:180px;" maxlength="18" /></td>
								</tr>
								<tr>
									<td>Cod. Barras Corporativo:</td>
									<td><input type="text" name="codigoDeBarrasCorporativo" id="produtoEdicaoController-codigoDeBarrasCorporativo" maxlength="25" style="width:180px;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:250px!important; margin-bottom:5px; float:right;">
						<legend>Tipos de Desconto</legend>
						<table width="250" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td colspan="2"><input type="text" name="descricaoDesconto" id="produtoEdicaoController-descricaoDesconto" style="width:235px;" /></td>
								</tr>
								<tr>
									<td>Desconto:</td>
									<td><input type="text" name="desconto" id="produtoEdicaoController-desconto" style="width:113px;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:250px!important; float:right; margin-bottom:5px;">
						<legend>Caracter&iacute;stica F&iacute;sica</legend>
						<table width="202" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="139">Peso:</td>
									<td><input type="text" name="peso" id="produtoEdicaoController-peso" style="width:80px;" /></td>
								</tr>
								<tr>
									<td>Descri&ccedil;&atilde;o Produto:</td>
									<td><input type="text" name="descricaoProduto" id="produtoEdicaoController-descricaoProduto" style="width:80px;" /></td>
								</tr>							
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:350px!important; float:left; margin-bottom:5px;">
						<legend>Outros</legend>
						<table width="330" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="130" height="24">Chamada de Capa:</td>
									<td width="193"><input type="text" name="chamadaCapa" id="produtoEdicaoController-chamadaCapa" style="width:190px;" /></td>
								</tr>
								<tr>
									<td height="24">Regime Recolhimento:</td>
									<td>
										<select name="parcial" id="produtoEdicaoController-parcial" style="width:190px;" >
											<option value="">Selecione...</option>
											<option value="true">Parcial</option>
											<option value="false">Normal</option>
										</select>
									</td>
								</tr>
								<tr>
									<td height="24">Brinde:</td>
									<td><input type="checkbox" name="possuiBrinde" id="produtoEdicaoController-possuiBrinde" /></td>
								</tr>
								<tr class="descBrinde" style="display:none;">
						       	    <td height="24">Descri&ccedil;&atilde;o Brinde:</td>
						       	    <td><input type="text" name="descricaoBrinde" id="produtoEdicaoController-descricaoBrinde" style="width:190px;" /></td>
						     	</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:640px!important; float:left; margin-bottom:5px;">
						<legend>Texto Boletim Informativo</legend>
						<table width="600" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="600"><textarea name="boletimInformativo" id="produtoEdicaoController-boletimInformativo" rows="5" style="width:610px;"></textarea></td>
							</tbody>
						</table>
					</fieldset>
					<br clear="all" />
				</div>
				<br clear="all" />
			</div>
			
			<div id="produtoEdicaoController-tabEdicoes-3">
				<div class="ldPesq">
					<fieldset id="produtoEdicaoController-pesqProdutos" style="width:200px!important;">
						<legend>Produtos Pesquisados</legend>
						<table class="prodsPesqGrid"></table>
					</fieldset>
					
					<span class="bt_novos"><a href="javascript:;" onclick="produtoEdicaoController.salvarProdutoEdicao();" rel="tipsy" title="Incluir Novo Produto"><img src="${pageContext.request.contextPath}/images/ico_add.gif" border="0" /></a></span>
				</div>
				
				<div class="ldForm">
					<fieldset style="width:410px!important;">
						<legend>P&uacute;blico Alvo</legend>
						<table width="400" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="112">Classe Social:</td>
									<td width="281">
										<select name="select4" id="produtoEdicaoController-select7" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Sexo:</td>
									<td>
										<select name="select4" id="produtoEdicaoController-select8" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Faixa-Et&aacute;ria:</td>
									<td>
										<select name="select4" id="produtoEdicaoController-select10" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:410px!important;">
						<legend>Outros</legend>
						<table width="400" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="112">Tema Principal:</td>
									<td width="281">
										<select name="select" id="produtoEdicaoController-select5" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Tema Secund&aacute;rio:</td>
									<td>
										<select name="select" id="produtoEdicaoController-select6" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
							</tbody>
						</table>
					</fieldset>
				</div>
				<br clear="all" />
			</div>
		</div>
</div>
</form>
<!--  FIM POPUP CADASTRO EDICAO -->

	<div class="areaBts">
		<div class="area">
			<span class="bt_novos" title="Novo">
				<a href="javascript:;" onclick="produtoEdicaoController.novaEdicao();" rel="tipsy" title="Incluir Nova Edição"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/></a>
			</span>
		</div>
	</div>
	
	<div class="linha_separa_fields">&nbsp;</div>
		
	<!-- INICIO FILTRO PESQUISA -->
	
	<fieldset class="fieldFiltro">
		
		<input type="hidden" id="produtoEdicaoController-codigoProduto" name="codigoProduto" value="" />
		
		<legend>Pesquisar Produto</legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			
			<thead/>
			
			<tbody>
			
				<tr>
				
					<td width="75">C&oacute;digo:</td>
					
					<td width="65">
					
						<input type="text" name="pCodigoProduto" id="produtoEdicaoController-pCodigoProduto" maxlength="255" 
								style="width:60px;" 
								onchange="produtoEdicaoController.pesquisarPorCodigoProduto('#produtoEdicaoController-pCodigoProduto', '#produtoEdicaoController-pNome', false,
										undefined,
										undefined);" />
					</td>
					
					<td width="57">Produto:</td>
					
					<td width="170">
					
						<input type="text" name="pNomeProduto" id="produtoEdicaoController-pNome" maxlength="255" 
								style="width:150px;"
								onkeyup="produtoEdicaoController.autoCompletarPorNome('#produtoEdicaoController-pNome', false);"
								onblur="produtoEdicaoController.pesquisarPorNome('#produtoEdicaoController-pCodigoProduto', '#produtoEdicaoController-pNome', false,
									undefined,
									undefined);" />
					
					</td>
					
					<td width="93">Per&iacute;odo Lcto:</td>
	                <td width="106"><input type="text" name="pDateLanctoDe" id="produtoEdicaoController-pDateLanctoDe" style="width:60px;"/></td>
	                <td width="26">At&eacute;:</td>
	                <td width="110"><input type="text" name="pDateLanctoAte" id="produtoEdicaoController-pDateLanctoAte" style="width:60px;"/></td>
					<td width="22">&nbsp;</td>
					<td width="57">Situa&ccedil;&atilde;o:</td>
					<td width="113">
						<select name="select" id="produtoEdicaoController-pSituacaoLancamento" name="pSituacaoLancamento" style="width:130px;">
							<option value="" selected="selected">Selecione...</option>
							<option value="Transmitido">Transmitido</option>
							<option value="Previsto">Previsto</option>
							<option value="C&aacute;lculo Solicitado">C&aacute;lculo Solicitado</option>
							<option value="Calculado">Calculado</option>
							<option value="Furo">Furo</option>
							<option value="Emitido">Emitido</option>
							<option value="Liberar C&aacute;lculo">Liberar C&aacute;lculo</option>
							<option value="Confirmado">Confirmado</option>
							<option value="Lan&ccedil;ado">Lan&ccedil;ado</option>
							<option value="Em Recolhimento">Em Recolhimento</option>
							<option value="Recolhido">Recolhido</option>
							<option value="Fechado">Fechado</option>
						</select>
					</td>
		
				</tr>
		
				<tr>
		
					<td>C&oacute;d. Barras:</td>
					<td colspan="3" ><input type="text" name="pCodigoDeBarras" id="produtoEdicaoController-pCodigoDeBarras" style="width:290px;" maxlength="25" /></td>						
					<td>Pre&ccedil;o (R$) de:</td>
	                <td><input type="text" name="pPrecoDe" id="produtoEdicaoController-pPrecoDe" style="width:60px; text-align:right;"/></td>
	                <td>At&eacute;:</td>
	                <td><input type="text" name="pPrecoAte" id="produtoEdicaoController-pPrecoAte" style="width:60px;text-align:right;"/></td>
					<td align="right"><input type="checkbox" name="pBrinde" id="produtoEdicaoController-pBrinde" value=""/></td>
					<td><label for="pBrinde">Brinde</label></td>
					<td><span class="bt_novos"><a href="javascript:;" onclick="produtoEdicaoController.pesquisarEdicoes();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
		
				</tr>
		
			</tbody>
		
		</table>
		
	</fieldset>
	<!-- FIM FILTRO PESQUISA -->
	
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<!-- INICIO GRID RESULTADO -->
	<div class="grids" style="display:none;">
	
		<fieldset class="fieldGrid">
			<legend>Edi&ccedil;&otilde;es do Produto<span id="produtoEdicaoController-labelNomeProduto" /></legend>
			<table class="edicoesGrid"></table>
		</fieldset>
	
	</div>
	<!-- FIM GRID RESULTADO -->
		
</body>

</html>
