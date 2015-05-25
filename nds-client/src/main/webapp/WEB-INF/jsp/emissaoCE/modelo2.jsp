<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>STG - Sistema Treelog de Gestão</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<style type="text/css">

body{font-size:11px!important;}
td{padding-left:3px;padding-right:3px;}
.titulo{ color:#333; font-size:10px;!important;}
.dadosNota{ color:#000;}

.quebraPaginaEmissao{
			page-break-after: always;
			table-layout: fixed;
			margin-top:30px;
		}
.capaImgBox {
	display: block;
	width: 115px;
	float: left;
	border: solid 1px #000; 
	margin: 1px 2px;
}
.capaImg{
	width:110px;
	height:143px;
}
#painelCapas{
	width: 850px;
	display: block;
	margin: 0 auto;
}

.resumosTotais td {
	font-size:14px;
}

.cabecalho{
	line-height: 10px;
}

</style>
<script language="javascript" type="text/javascript">
function imprimir(){
	$( "#btImpressao" ).hide();
	window.print();
}

</script>
</head>

<body>


<c:forEach items="${cotasEmissao}" var="cotaEmissao">

<c:forEach items="${cotaEmissao.paginasProduto}" var="paginaProduto" varStatus="loopPaginaProduto">

<div class="quebraPaginaEmissao">

<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px; margin-top:10px;">
  <tr>
    <td width="121" rowspan="6" valign="top"><span>
    	<img src="${pageContext.request.contextPath}/cadastro/distribuidor/logo" width="110" height="115" alt="Novo Distrib"  /></span>
    </td>
    <td colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;"><span class="cabecalho">Razão Social<br />
    </span></td>
    <td width="237" rowspan="2" align="center" style="border-left:1px solid #000;"><span class="cabecalho" style="font-size:13px!important;"><strong>CHAMADA DE ENCALHE</strong></span></td>
  </tr>
  <tr>
    <td  colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.razaoSocial}</span></td>
  </tr>
  <tr>
    <td width="172" style="border-left:1px solid #000;"><span class="cabecalho">Endereço<br />
    </span></td>
    <th width="25">&nbsp;</th>
    <th width="75">&nbsp;</th>
    <td width="130" align="center"><span class="cabecalho">CNPJ</span></td>
    <td rowspan="4" align="center" style="border-left:1px solid #000;vertical-align: top;"><h2>Documento Número: ${cotaEmissao.idChamEncCota}</h2></td>
  </tr>
  <tr>
    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;">
    	<span class="dadosNota">${dadosDistribuidor.endereco}<br /></span>
    </td>
    <td style="border-bottom:1px solid #000;"><span class="dadosNota"></span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
  </tr>
  <tr height="18" style="vertical-align: middle;">
    <td style="border-left:1px solid #000;"><span class="cabecalho">Cidade</span></td>
    <td align="center"><span class="cabecalho">UF</span></td>
    <td align="center"><span class="cabecalho">CEP</span></td>
    <td align="center"><span class="cabecalho">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
  </tr>
</table>

<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
    </span></td>
    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CPF/CNPJ</span></td>
  </tr>
  <tr>
    <td height="20" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;">
    	<span class="dadosNota" style="font-size:13px!important;"><strong>${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()} </strong></span>
    </td>
    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
  </tr>
  <tr>
    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço </span></td>
    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
    <td style="border-right:1px solid #000;border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
  <tr>
    <td width="196" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
    <td width="196" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
    <td width="458" style="border-right:1px solid #000;"><span class="titulo">Box / Roteiro / Rota</span></td>
  </tr>
  <tr>
    <td align="center" style="border-left:1px solid #000;font-size:13px!important;border-bottom:1px solid #000;"><span class="dadosNota"><strong>${dataRecolhimento}</strong></span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.nomeBox} / Roteiro: ${cotaEmissao.nomeRoteiro} / Rota: ${cotaEmissao.nomeRota}</span></td>
  </tr>
</table>

<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top:5px;">
  <tr class="class_linha_3">
    <td colspan="7" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;">&nbsp;</td>
    <td colspan="3" align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000;border-right:1px solid #000;"><strong>Qtde.</strong></td>
    <td colspan="2" align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
  </tr>
  <tr class="class_linha_3">
    <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Código</strong></td>
    <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Produto</strong></td>
    <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Edição</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Seq</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Lancto</strong></td>
    <td class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Nota Envio</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>TR</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Reparte</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong> Devol.</strong></td>
    <td align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Venda</strong></td>
    <td align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Desc R$</strong></td>
    <td align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000;"><strong>Vlr. Venda R$</strong></td>
  </tr>
  
  <c:forEach items="${paginaProduto}" var="produto" varStatus="status">
           
  
  <tr class="class_linha_${status.index%2==0?1:2}">
  
    <td width="58" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; height:30px ">${produto.codigoProduto}</td>
    <td width="230" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; "><strong>${produto.nomeProduto}</strong></td>
    <td width="34" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; "><strong>${produto.edicao}</strong></td>
    
    <c:if test="${produto.produtoDuplicadoDetalheNota}">
    	<td colspan="9" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px;border-right:1px solid #000;" >
    		${produto.descricaoNotaEnvio}
    	</td>
    </c:if>
    
    <c:if test="${!produto.produtoDuplicadoDetalheNota}">
    
	    <td width="34" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px;border-right:1px solid #000;  "><strong>${produto.sequencia}</strong></td>
	    <td width="25" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.dataLancamento}</td>
	    <td width="70" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.numeroNotaEnvio}</td>
	    <td width="22" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.inicialTipoRecolhimento}</td>
	    <td width="51" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.reparte}</td>
	    
	    <td width="95" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">
	    	<c:if test="${produto.apresentaQuantidadeEncalhe}">
	              ${produto.quantidadeDevolvida}
	        </c:if>
	    </td>
	    
	    <td width="95" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">
	    	<c:if test="${produto.apresentaQuantidadeEncalhe}">
	    		${produto.vendido}
	    	</c:if>
	    </td>
	
	    <td width="70" align="right" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">
	    	<fmt:formatNumber value="${produto.precoVenda.subtract(produto.precoVenda.multiply(produto.vlrDesconto).divide(100))}" maxFractionDigits="4" minFractionDigits="4" />
	    </td>

	    <td width="95" align="right" style="border-bottom:1px solid #000;padding-left:5px; border-right:1px solid #000; ">
	    	<c:if test="${produto.apresentaQuantidadeEncalhe}">
	    		<fmt:formatNumber value="${produto.vlrPrecoComDesconto.multiply(produto.vendido)}" maxFractionDigits="2" minFractionDigits="2" />
	    	</c:if>
	    </td>
    
    </c:if>
    
  </tr>
 
 </c:forEach>
 
</table>

<c:if test="${loopPaginaProduto.last && !cotaEmissao.quebraTotalizacaoUltimaPagina}">

<table width="850" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td><table width="300" class="resumosTotais" border="0" align="right" cellpadding="1" cellspacing="1">
      <tr>
        <td align="right">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
      <tr>
        <td width="168" align="right"><strong>Total Bruto R$</strong></td>
        <td width="121" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
      </tr>
      <tr>
        <td align="right"><strong> Total Desconto R$</strong></td>
        <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
      </tr>
      <tr>
        <td align="right"><strong>Total Líquido R$</strong></td>
        <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
      </tr>
    </table></td>
  </tr>
</table>

</c:if> 

</div>


<c:if test="${loopPaginaProduto.last && cotaEmissao.quebraTotalizacaoUltimaPagina}">

<div class="quebraPaginaEmissao">

<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px; margin-top:10px;">
  <tr>
    <td width="121" rowspan="6" valign="top"><span>
    	<img src="${pageContext.request.contextPath}/cadastro/distribuidor/logo" width="110" height="115" alt="Novo Distrib"  /></span>
    </td>
    <td colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;"><span class="cabecalho">Razão Social<br />
    </span></td>
    <td width="237" rowspan="2" align="center" style="border-left:1px solid #000;"><span class="cabecalho" style="font-size:13px!important;"><strong>CHAMADA DE ENCALHE</strong></span></td>
  </tr>
  <tr>
    <td  colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.razaoSocial}</span></td>
  </tr>
  <tr>
    <td width="172" style="border-left:1px solid #000;"><span class="cabecalho">Endereço<br />
    </span></td>
    <th width="25">&nbsp;</th>
    <th width="75">&nbsp;</th>
    <td width="130" align="center"><span class="cabecalho">CNPJ</span></td>
    <td rowspan="4" align="center" style="border-left:1px solid #000;vertical-align: top;"><h2>Documento Número: ${cotaEmissao.idChamEncCota}</h2></td>
  </tr>
  <tr>
    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;">
    	<span class="dadosNota">${dadosDistribuidor.endereco}<br /></span>
    </td>
    <td style="border-bottom:1px solid #000;"><span class="dadosNota"></span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
  </tr>
  <tr height="18" style="vertical-align: middle;">
    <td style="border-left:1px solid #000;"><span class="cabecalho">Cidade</span></td>
    <td align="center"><span class="cabecalho">UF</span></td>
    <td align="center"><span class="cabecalho">CEP</span></td>
    <td align="center"><span class="cabecalho">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
  </tr>
</table>

<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" >
  <tr>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
    </span></td>
    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CPF/CNPJ</span></td>
  </tr>
  <tr>
    <td height="20" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;">
    	<span class="dadosNota" style="font-size:13px!important;"><strong>${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()} </strong></span>
    </td>
    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
  </tr>
  <tr>
    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço </span></td>
    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
    <td style="border-right:1px solid #000;border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
  </tr>
</table>
<table width="850" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
  <tr>
    <td width="196" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
    <td width="196" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
    <td width="458" style="border-right:1px solid #000;"><span class="titulo">Box / Roteiro / Rota</span></td>
  </tr>
  <tr>
    <td align="center" style="border-left:1px solid #000;font-size:13px!important;border-bottom:1px solid #000;"><span class="dadosNota"><strong>${dataRecolhimento}</strong></span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.nomeBox} / Roteiro: ${cotaEmissao.nomeRoteiro} / Rota: ${cotaEmissao.nomeRota}</span></td>
  </tr>
</table>

<table width="850" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td><table width="300" class="resumosTotais" border="0" align="right" cellpadding="1" cellspacing="1">
      <tr>
        <td align="right">&nbsp;</td>
        <td align="right">&nbsp;</td>
      </tr>
      <tr>
        <td width="168" align="right"><strong>Total Bruto R$</strong></td>
        <td width="121" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
      </tr>
      <tr>
        <td align="right"><strong> Total Desconto R$</strong></td>
        <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
      </tr>
      <tr>
        <td align="right"><strong>Total Líquido R$</strong></td>
        <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
      </tr>
    </table></td>
  </tr>
</table>

</div>
	
	
</c:if>


<c:if test="${withCapa && loopPaginaProduto.last}">
	
	<c:if test="${!personalizada}">
		
		<c:forEach items="${capasPaginadas}" var="paginaCapa" varStatus="status">	
			
			<div class="quebraPaginaEmissao" id=paginaCapas>	

				<div id="painelCapas"> 
			
					<div style="width: inherit; text-align: center;">
							<span class="titulo" style="font-size:11px!important;">Capas</span>			
					</div>	 
					
					<c:forEach items="${paginaCapa}" var="capa" varStatus="status">	
					
					<div class="capaImgBox">			
						<div style="width: inherit; text-align: center;">
							<strong>${capa.sequenciaMatriz}</strong>				
						</div>			
						<div style="width: inherit; text-align: center;">
							<img class="capaImg" src="<c:url value='/capa/tratarNoImage/${capa.id}'></c:url>"/>
						</div>
					</div>	
					
					</c:forEach>
					
			    </div>
	    
			   <br clear="all"/>
			
			</div> 

			
	    </c:forEach>
		
	</c:if>
	
	<c:if test="${personalizada}">
	
		<c:forEach items="${cotaEmissao.paginasCapa}" var="paginaCapa" varStatus="status">
			
			<div class="quebraPaginaEmissao" id=paginaCapas>	
	
				<div id="painelCapas"> 
		
					<div style="width: inherit; text-align: center;">
							<span class="titulo" style="font-size:11px!important;">Capas</span>			
					</div>	 
			
					<c:forEach items="${paginaCapa}" var="capa" varStatus="status">	
			
					<div class="capaImgBox">			
						<div style="width: inherit; text-align: center;">
							<strong>${capa.sequenciaMatriz}</strong>				
						</div>			
						<div style="width: inherit; text-align: center;">
							<img class="capaImg" src="<c:url value='/capa/tratarNoImage/${capa.id}'></c:url>"/>
						</div>
					</div>	
			
					</c:forEach>
					
			    </div> 
	    
			  	<br clear="all"/>
			
			</div> 
			
	    </c:forEach>
    
    </c:if>
	

</c:if>


</c:forEach>

</c:forEach>

</body>

</html>
