<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>

<script language="javascript" type="text/javascript">
	$(function(){
		produtoEdicaoController.init();
	});
</script>
<style>
label{ vertical-align:super;}
#dialog-novo label{width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px;}
.prodLinhas{display:none;}
.ui-tabs .ui-tabs-panel {
   padding: 6px!important;
}
.ldForm{float:left; width:652px!important; margin-left:15px;}
fieldset {
    margin-right:0px!important;
}
.ldPesq{float:left; width:210px;}
</style>
</head>

<body>

<form action="/cadastro/edicao" id="incluir_capa_form">
<div id="dialog-capa" title="Incluir Imagem Capa" style="display:none;">
	<br />
	<p><input type="file" size="30" id="file01" name="file01" /></p>
</div>
</form>

<form action="/cadastro/edicao" id="excluir_capa_form">
<div id="dialog-excluir-capa" title="Excluir Capa" style="display:none;">
	<p>Confirma a exclus&atilde;o desta Capa?</p>
</div>
</form>

<form action="/cadastro/edicao" id="excluir_form">
<div id="dialog-excluir" title="Excluir Edi&ccedil;&atilde;o">
	<p>Confirma a exclus&atilde;o desta Edi&ccedil;&atilde;o?</p>
</div>
</form>

<form action="/cadastro/edicao" id="novo_form">
<div id="dialog-novo" title="Incluir Nova Edi&ccedil;&atilde;o">
	
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialogMensagemNovo" name="messageDialog"/>
	</jsp:include> 
	
	<form id="formUpload" name="formUpload" method="post" enctype="multipart/form-data" >
		<div id="tabEdicoes">
			<ul>
				<li><a id="tabIdentificacao" href="#tabEdicoes-1">Identifica&ccedil;&atilde;o</a></li>
				<li><a id="tabCaractLancto" href="#tabEdicoes-2">Caracter&iacute;sticas do Lan&ccedil;amento</a></li>
				<li><a id="tabSegmentacao" href="#tabEdicoes-3">Segmenta&ccedil;&atilde;o</a></li>
			</ul>
			
			<div id="tabEdicoes-1">
				<input type="hidden" id="idProdutoEdicao" name="idProdutoEdicao" />
				<div class="ldPesq">
					<fieldset id="pesqProdutos" style="width:200px!important;">
						<legend>Produtos Pesquisados</legend>
						<table class="prodsPesqGrid"></table>
					</fieldset>
					
					<span class="bt_add"><a href="javascript:;" onclick="produtoEdicaoController.salvaUmaEdicao();">Incluir Novo</a></span>
				</div>
				
				<div class="ldForm">
					<fieldset style="width:655px!important; margin-bottom:5px;">
						<legend>Identifica&ccedil;&atilde;o</legend>
						<table width="648" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="181"><strong>C&oacute;digo:</strong></td>
									<td width="100" colspan="3"><input type="text" name="codigoProdutoEdicao" id="codigoProdutoEdicao" style="width:100px;" /></td>
									
									<td width="90">&nbsp;</td>
									<td width="108">&nbsp;</td>
									<td width="153" rowspan="8" align="center">
										
										<div id="div_imagem_capa">
											<img alt="Capa" src="" width="144" height="185" />
										</div>
										
										<br clear="all" />
										<a href="javascript:;" onclick="produtoEdicaoController.popup_excluir_capa();">
											<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir Capa" width="15" height="15" hspace="5" vspace="3" border="0" />
										</a>
									</td>
								</tr>
								<tr>
									<td><strong>Nome Publica&ccedil;&atilde;o:</strong></td>
									<td colspan="5"><input type="text" name="nomePublicacao" id="nomePublicacao" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td><strong>Nome Comercial Produto:</strong></td>
									<td colspan="5"><input type="text" name="nomeComercialProduto" id="nomeComercialProduto" style="width:340px;" /></td>
								</tr>
								<tr>
									<td><strong>Fornecedor:</strong></td>
									<td colspan="5"><input type="text" name="nomeFornecedor" id="nomeFornecedor" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td><strong>Situa&ccedil;&atilde;o:</strong></td>
									<td colspan="5"><input type="text" name="situacao" id="situacao" style="width:340px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td><strong>Edi&ccedil;&atilde;o:</strong></td>
									<td><input type="text" name="numeroEdicao" id="numeroEdicao" style="width:50px;" /></td>
									<td><strong>PED:</strong></td>
									<td><input type="text" name="ped" id="ped" style="width:50px;" /></td>
									<td><strong>Pct. Padr&atilde;o:</strong></td>
									<td><input type="text" name="pacotePadrao" id="pacotePadrao" style="width:50px;" /></td>
								</tr>
							
								<tr>
									<td><strong>Tipo de Lan&ccedil;amento:</strong></td>
									<td colspan="3">
										<select name="tipoLancamento" id="tipoLancamento" style="width:160px;" >
											<option value="">Selecione...</option>
											<option value="LANCAMENTO">Lan&ccedil;amento</option>
											<option value="PARCIAL">Ed. Parcial</option>
											<option value="RELANCAMENTO">Relan&ccedil;amento</option>
											<option value="REDISTRIBUICAO">Redistribui&ccedil;&atilde;o</option>
											<option value="SUPLEMENTAR">Supl. Compuls</option>
										</select>
									</td>
									<td><strong>N&ordm; Lancto:</strong></td>
									<td><input type="text" name="numeroLancamento" id="numeroLancamento" style="width:50px;" maxlength="9" /></td>
								</tr>
								<tr>
									<td><strong>Capa da Edi&ccedil;&atilde;o:</strong></td>
									<td colspan="5"><input type="file" name="imagemCapa" id="imagemCapa" style="width:340px;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:220px!important; margin-bottom:2px; float:right; margin-right:0px;">
						<legend>Reparte</legend>
						<table width="190" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="103"><strong>Previsto:</strong></td>
									<td width="80"><input type="text" name="repartePrevisto" id="repartePrevisto" style="width:80px; float:left;" /></td>
								</tr>
								<tr>
									<td><strong>Exp. Venda(%):</strong></td>
									<td><input type="text" name="expectativaVenda" id="expectativaVenda" style="width:80px;" disabled="disabled" /></td>
								</tr>
								<tr>
									<td><strong>Promocional:</strong></td>
									<td><input type="text" name="repartePromocional" id="repartePromocional" style="width:80px; float:left;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:350px!important; margin-bottom:2px; float:left;">
						<legend>Pre&ccedil;o da Capa</legend>
						<table width="309" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="76"><strong>Previsto:</strong></td>
									<td width="99"><input type="text" name="precoPrevisto" id="precoPrevisto" style="width:70px; float:left;" /></td>
									<td width="51"><strong>Real:</strong></td>
									<td width="70"><input type="text" name="precoVenda" id="precoVenda" style="width:70px; text-align:right;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:350px!important; margin-bottom:2px; float:left;">
						<legend>Data Lan&ccedil;amento</legend>
						<table width="309" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="76"><strong>Previsto:</strong></td>
									<td width="99"><input type="text" name="dataLancamentoPrevisto" id="dataLancamentoPrevisto" style="width:70px; float:left;" /></td>
									<td width="51"><strong>Real:</strong></td>
									<td width="70"><input type="text" name="dataLancamento" id="dataLancamento" style="width:70px; text-align:right;" disabled="disabled" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>	
				    <fieldset style="width: 630px !important; margin-bottom: 2px; float: left;">
				     <legend>Data Recolhimento</legend>
					   	<table border="0" cellSpacing="1" cellPadding="1" width="562">
					      <tbody><tr>
					        <td width="60">Previsto:</td>
					        <td width="91"><input style="width: 70px; float: left;" id="dataRecolhimentoPrevisto" name="dataRecolhimentoPrevisto" type="text"></td>
					        <td width="48" align="right">Real:</td>
					        <td width="79"><input style="width: 70px; text-align: right;" id="dataRecolhimentoReal" disabled="disabled" name="dataRecolhimentoReal" type="text"></td>
					        <td width="180" align="right">Semana de Recolhimento:</td>
					        <td width="85"><input style="width: 70px; float: left;" id="semanaRecolhimento" disabled="disabled" name="semanaRecolhimento" type="text"></td>
					      </tr>
					      </tbody>
					    </table>
				    </fieldset>
				</div>
				<br clear="all" />
			</div>
			
			<div id="tabEdicoes-2">
				<div class="ldPesq">
					<fieldset id="pesqProdutos" style="width:200px!important;">
						<legend>Produtos Pesquisados</legend>
						<table class="prodsPesqGrid"></table>
					</fieldset>
					
					<span class="bt_add"><a href="javascript:;" onclick="produtoEdicaoController.salvaUmaEdicao();">Incluir Novo</a></span>
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
										<select name="select2" id="select2" style="width:180px;" >
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td>Cod. de Barras:</td>
									<td><input type="text" name="codigoDeBarras" id="codigoDeBarras" style="width:180px;" /></td>
								</tr>
								<tr>
									<td>Cod. Barras Corporativo:</td>
									<td><input type="text" name="codigoDeBarrasCorporativo" id="codigoDeBarrasCorporativo" style="width:180px;" /></td>
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
									<td colspan="2"><input type="text" name="descricaoDesconto" id="descricaoDesconto" style="width:235px;" /></td>
								</tr>
								<tr>
									<td>Desconto:</td>
									<td><input type="text" name="desconto" id="desconto" style="width:113px;" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset style="width:250px!important; float:right; margin-bottom:5px;">
						<legend>Caracter&iacute;stica F&iacute;sica</legend>
						<table width="152" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="59">Peso:</td>
									<td width="86"><input type="text" name="peso" id="peso" style="width:80px;" /></td>
								</tr>
								<tr>
									<td width="59">Descri&ccedil;&atilde;o Produto:</td>
									<td width="86"><input type="text" name="descricaoProduto" id="descricaoProduto" style="width:80px;" /></td>
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
									<td width="193"><input type="text" name="chamadaCapa" id="chamadaCapa" style="width:190px;" /></td>
								</tr>
								<tr>
									<td height="24">Regime Recolhimento:</td>
									<td>
										<select name="parcial" id="parcial" style="width:190px;" >
											<option value="">Selecione...</option>
											<option value="true">Parcial</option>
											<option value="false">Normal</option>
										</select>
									</td>
								</tr>
								<tr>
									<td height="24">Brinde:</td>
									<td><input type="checkbox" name="possuiBrinde" id="possuiBrinde" /></td>
								</tr>
								<tr class="descBrinde" style="display:none;">
						       	    <td height="24">Descri&ccedil;&atilde;o Brinde:</td>
						       	    <td><input type="text" name="descricaoBrinde" id="descricaoBrinde" style="width:190px;" /></td>
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
									<td width="600"><textarea name="boletimInformativo" id="boletimInformativo" rows="5" style="width:610px;"></textarea></td>
							</tbody>
						</table>
					</fieldset>
					<br clear="all" />
				</div>
				<br clear="all" />
			</div>
			
			<div id="tabEdicoes-3">
				<div class="ldPesq">
					<fieldset id="pesqProdutos" style="width:200px!important;">
						<legend>Produtos Pesquisados</legend>
						<table class="prodsPesqGrid"></table>
					</fieldset>
					
					<span class="bt_add"><a href="javascript:;" onclick="produtoEdicaoController.salvaUmaEdicao();">Incluir Novo</a></span>
				</div>
				
				<div class="ldForm">
					<fieldset style="width:410px!important;">
						<legend>P&uacute;blico Alvo</legend>
						<table width="400" border="0" cellspacing="1" cellpadding="1">
							<thead />
							<tbody>
								<tr>
									<td width="112"><strong>Classe Social:</strong></td>
									<td width="281">
										<select name="select4" id="select7" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><strong>Sexo:</strong></td>
									<td>
										<select name="select4" id="select8" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><strong>Faixa-Et&aacute;ria:</strong></td>
									<td>
										<select name="select4" id="select10" style="width:250px;" disabled="disabled">
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
									<td width="112"><strong>Tema Principal:</strong></td>
									<td width="281">
										<select name="select" id="select5" style="width:250px;" disabled="disabled">
											<option selected="selected">Selecione...</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><strong>Tema Secund&aacute;rio:</strong></td>
									<td>
										<select name="select" id="select6" style="width:250px;" disabled="disabled">
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
	</form>
</div>
</form>

<form action="/cadastro/edicao" id="pesquisar_form">
<div class="corpo">
	
	<br clear="all"/>
	<br />

	<div class="container">
	
		<fieldset class="classFieldset">
			<input type="hidden" id="codigoProduto" name="codigoProduto" value="" />
			<legend>Pesquisar Produto</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<thead/>
				<tbody>
					<tr>
						<td width="74">C&oacute;digo:</td>
						<td width="81">
							<input type="text" name="pCodigoProduto" id="pCodigoProduto" maxlength="255" 
									style="width:80px;" 
									onchange="produtoEdicaoController.pesquisarPorCodigoProduto('#pCodigoProduto', '#pNomeProduto', false,
											undefined,
											undefined);" />
						</td>
						<td width="48">Produto:</td>
						<td width="167">
							<input type="text" name="pNomeProduto" id="pNomeProduto" maxlength="255" 
									style="width:160px;"
									onkeyup="produtoEdicaoController.autoCompletarPorNomeProduto('#pNomeProduto', false);"
									onblur="produtoEdicaoController.pesquisarPorNomeProduto('#pCodigoProduto', '#pNomeProduto', false,
										undefined,
										undefined);" />
						</td>
						<td width="86">Per&iacute;odo Lcto:</td>
		                <td width="103"><input type="text" name="pDateLanctoDe" id="pDateLanctoDe" style="width:80px;"/></td>
		                <td width="22">At&eacute;:</td>
		                <td width="108"><input type="text" name="pDateLanctoAte" id="pDateLanctoAte" style="width:80px;"/></td>
						<td width="20">&nbsp;</td>
						<td width="52">Situa&ccedil;&atilde;o:</td>
						<td width="133">
							<select name="select" id="pSituacaoLancamento" name="pSituacaoLancamento" style="width:130px;">
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
						<td colspan="3" ><input type="text" name="pCodigoDeBarras" id="pCodigoDeBarras" style="width:300px;"/></td>						
						<td>Pre&ccedil;o (R$) de:</td>
		                <td><input type="text" name="pPrecoDe" id="pPrecoDe" style="width:80px; text-align:right;"/></td>
		                <td>At&eacute;:</td>
		                <td><input type="text" name="pPrecoAte" id="pPrecoAte" style="width:80px;text-align:right;"/></td>
						<td align="right"><input type="checkbox" name="pBrinde" id="pBrinde" value=""/></td>
						<td><label for="pBrinde">Brinde</label></td>
						<td><span class="bt_pesquisar"><a href="javascript:;" onclick="produtoEdicaoController.pesquisarEdicoes();">Pesquisar</a></span></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<fieldset class="classFieldset">
			<legend>Edi&ccedil;&otilde;es do Produto<span id="labelNomeProduto" /></legend>
			<div class="grids" style="display:none;">
				<table class="edicoesGrid"></table>
			</div>
			
			<span class="bt_novos" title="Novo">
				<a href="javascript:;" onclick="produtoEdicaoController.novaEdicao();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a>
			</span>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
	</div>
</div>
</form>
</body>
