<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu_superior.css" />
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<style type="text/css">

body{font-size:25px!important;}
td{padding-left:3px;padding-right:3px;font-size:13px!important;}
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
	margin: 1px;
}
.capaImg{
	width:110px;
	height:143px;
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

<table width="850" border="0" cellspacing="0" cellpadding="0"> 
  <tr>
    <td width="680" valign="top">
    
    <table width="680" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:5px; margin-top:5px;"> 
  		<tr>
    		<td width="121" rowspan="6" valign="top">
    		<span class="logo">
    			<img src="${pageContext.request.contextPath}/cadastro/distribuidor/logo" width="110" height="70" alt="Novo Distrib"  />
    		</span>
    		</td>
    		<td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;">
    			<span class="titulo">Razão Social<br /></span>
    		</td>
    		<td width="237" align="center" style="border-left:1px solid #000;">
    			<span class="titulo" style="font-size:13px!important;">
    				<strong>CHAMADA DE ENCALHE</strong>
    			</span>
    		</td>
    	</tr>
  		<tr>
    		<td height="26" colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;">
    			<span class="dadosNota"><font size="2"><b>${dadosDistribuidor.razaoSocial}</b></font></span>
    		</td>
    		<td rowspan="2" align="center" style="border-left:1px solid #000;">      
      			<H3>Documento Número: <font size="4">${cotaEmissao.idChamEncCota}</font></H3>
      
			</td>
    	</tr>
	  <tr>
	    <td width="172" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
	    </span></td>
	    <th width="25">&nbsp;</th>
	    <th width="75">&nbsp;</th>
	    <td width="130" align="center"><span class="titulo">CNPJ</span></td>
	  </tr>
	  <tr>
	    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
	    </span></td>
	    <td style="border-bottom:1px solid #000;"><span class="dadosNota">    </span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
	    <td style="border-left:1px solid #000;">&nbsp;</td>
	    </tr>
	  <tr>
	    <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
	    <td align="center"><span class="titulo">UF</span></td>
	    <td align="center"><span class="titulo">CEP</span></td>
	    <td align="center"><span class="titulo">Inscrição Estadual</span></td>
	    <td style="border-left:1px solid #000;">&nbsp;</td>
	    </tr>
	  <tr>
	    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
	    <td align="center" style="border-left:1px solid #000;">&nbsp;</td>
	    </tr>
  </table>
  
<table width="680" border="0" cellpadding="0" cellspacing="0" > 
  <tr>
    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
    </span></td>
    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CPF/CNPJ</span></td>
  </tr>
  <tr>
    <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota"><font size="2"><b>${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</b></font></span></td>
    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
  </tr>
  <tr>
    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço  </span></td>
    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
    <td style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
  </tr>
  <tr>
    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
  </tr>
</table> 
<table width="680" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:5px;"> 
  <tr>
    <td width="179" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
    <td width="161" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
    <td width="420" style="border-right:1px solid #000;"><span class="titulo">Box / Roteiro / Rota</span></td>
  </tr>
  <tr>
    <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dataRecolhimento}</span></td>
    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} / Roteiro: ${cotaEmissao.nomeRoteiro} / Rota: ${cotaEmissao.nomeRota}</span></td>
  </tr>
</table> 

<table width="680" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;"> 
            <tr class="class_linha_3">
              <td width="30" align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Seq</strong></td>
              <td width="62" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Código</strong></td>
              <td width="195" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Produto</strong></td>
              <td width="43" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Edição</strong></td>
              <td width="30" align="center" class="relatorios" style="border-left:1px solid #000;padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>TR</strong></td>
              <td width="80" align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Data Lancto</strong></td>
              <td width="80" align="right" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Preço Desc R$</strong></td>
              <td width="37" align="center" class="relatorios" style="padding-left:5px; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Reparte</strong></td>
              <td width="50" align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Qtde. Dev</strong></td>
  </tr>
            <tr class="class_linha_3">
              <td colspan="10" class="relatorios" style="padding-left:10px; border-left:1px solid #000; border-bottom:1px solid #000; border-right:1px solid #000;"><strong>Código de Barras</strong></td>
            </tr>
           
           
			<c:forEach items="${paginaProduto}" var="produto" varStatus="status">
           
            <tr class="class_linha_${status.index%2==0?1:2}">
              <td width="30" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.sequencia}</td>
              <td width="62" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.codigoProduto}</td>
              <td width="195" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; "><font size="2"><strong>${produto.nomeProduto}</strong></font></td>
              <td width="43" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; "><font size="2"><strong>${produto.edicao}</strong></font></td>
              <td width="30" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.inicialTipoRecolhimento}</td>
              <td width="80" align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">${produto.dataLancamento}</td>
              <td width="80" align="right" style="border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">
              	
              	<fmt:formatNumber value="${produto.precoVenda.subtract(produto.precoVenda.multiply(produto.vlrDesconto).divide(100))}" maxFractionDigits="4" minFractionDigits="4" />
              	
              </td>
              <td width="37" align="center" style="border-bottom:1px solid #000;padding-left:5px; "><font size="2"><strong>${produto.reparte}</strong></font></td>
              
              <td width="50" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">
              	
              	<c:if test="${produto.apresentaQuantidadeEncalhe}">
              		<strong>${produto.quantidadeDevolvida}</strong>
              	</c:if>
              	
              </td>
              
            </tr>
            <tr class="class_linha_${status.index%2==0?1:2}">
              <td colspan="2" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">${produto.codigoBarras}</td>
              <td style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">${produto.nomeProduto}</td>
              <td colspan="7" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;border-right:1px solid #000;">${produto.descricaoNotaEnvio}</td>
            </tr>
            
            </c:forEach>
            
</table> 

<c:if test="${loopPaginaProduto.last && !cotaEmissao.quebraTotalizacaoUltimaPagina}">

<table width="680" border="0" cellpadding="1" cellspacing="1">
  <tr>
    <td>&nbsp;</td>
    <td align="right">&nbsp;</td>
    <td align="right">&nbsp;</td>
  </tr>
  <tr>
    <td width="430">&nbsp;</td>
    <td width="203" align="right"><strong>Valor Reparte R$</strong></td>
    <td width="117" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Valor Desconto R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Valor Reparte Líquido R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Valor Encalhe R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrEncalhe}</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><strong>Total Líquido R$</strong></td>
    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrTotalLiquido}</td>
  </tr>
</table>

</c:if>

    </td>
    
    
    <td style="width:10px; border-left:1px dotted #000;">&nbsp;</td>
    
    <td width="30" valign="top">
    
    <table width="375" border="0" cellpadding="0" cellspacing="0" style=" margin-top:5px;">
      <tr>
        <td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">Razão Social<br />
        </span></td>
        </tr>
      <tr>
        <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota"><font size="2"><b>${dadosDistribuidor.razaoSocial}</b></font></span></td>
        </tr>
      <tr>
        <td width="199" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
        </span></td>
        <th width="23">&nbsp;</th>
        <th width="67">&nbsp;</th>
        <td width="121" align="center" style="border-right:1px solid #000;"><span class="titulo">CNPJ</span></td>
      </tr>
      <tr>
        <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
        </span></td>
        <td style="border-bottom:1px solid #000;"><span class="dadosNota"></span></td>
        <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
        </tr>
      <tr>
        <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
        <td align="center"><span class="titulo">UF</span></td>
        <td align="center"><span class="titulo">CEP</span></td>
        <td align="center" style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
        </tr>
      <tr>
        <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
        <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
        <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
        <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
        </tr>
    </table>
    
    <table width="375" border="0" cellpadding="0" cellspacing="0" style="border-top:1px solid #000; margin-bottom:5px;margin-top:5px;">
        <tr>
          <td width="207" height="24" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
          <td width="203" align="center" style="border-left:1px solid #000;border-right:1px solid #000;">Data Emissão</td>
        </tr>
        <tr>
          <td height="22" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dataRecolhimento}</span></td>
          <td align="center" style="border-bottom:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
        </tr>
        <tr>
          <td height="18" colspan="2" style="border-left:1px solid #000;border-right:1px solid #000;"><span class="titulo">Cliente</span></td>
        </tr>
        <tr>
          <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota"><font size="2"><b>${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</b></font></span></td>
        </tr>
							<tr>
          <td height="17" colspan="2" style="border-left:1px solid #000;border-right:1px solid #000;"><span class="titulo">Box / Roteiro / Rota</span></td>
        </tr>
        <tr>
          <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} / Roteiro: ${cotaEmissao.nomeRoteiro} / Rota: ${cotaEmissao.nomeRota}</span></td>
        </tr>
    </table>
    
      <table width="375" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;">
        <tr class="class_linha_3">
          <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;" height="50"><strong>Seq</strong></td>
          <td colspan="2" align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Produto</strong></td>
          <td align="center" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;"><strong>Reparte</strong></td>
          <td align="center" width="70" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-top:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;"><strong>Qtde. Dev</strong></td>
        </tr>
        <tr class="class_linha_3">
          <td class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000; "><strong>Código</strong></td>
          <td width="70" align="left" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000;"><strong>Edição</strong></td>
          <td width="100" align="left" class="relatorios" style="padding-left:5px; border-left:1px solid #000; border-bottom:1px solid #000;"><strong>Preço Desc R$</strong></td>
          <td colspan="2" class="relatorios" width="86" style="padding-left:5px; border-left:1px solid #000; border-left:1px solid #000; border-bottom:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
        </tr>
                
        <c:forEach items="${paginaProduto}" var="produto" varStatus="status">
        
	        <tr class="class_linha_1">
	          <td width="61" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; ">${produto.sequencia}</td>
	          <td colspan="2" width="224" style="border-left:1px solid #000;border-bottom:1px solid #000;padding-left:5px; "><font size="2"><strong>${produto.nomeProduto}</strong></font></td>
	          <td width="61" align="center" style="border-left:1px solid #000; border-bottom:1px solid #000;padding-left:5px; "><font size="2"><strong>${produto.reparte}</strong></font></td>
	          <td width="67" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;padding-left:5px; ">
				<c:if test="${produto.apresentaQuantidadeEncalhe}">
              		<strong>${produto.quantidadeDevolvida}</strong>
              	</c:if>
	          </td>
	        </tr>
	        <tr class="class_linha_1">
	          <td style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">${produto.codigoProduto}</td>
	          <td style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;"><font size="2"><strong>${produto.edicao}</strong></font></td>
	          <td align="right" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;">
	          
	          	<fmt:formatNumber value="${produto.precoVenda.subtract(produto.precoVenda.multiply(produto.vlrDesconto).divide(100))}" maxFractionDigits="4" minFractionDigits="4" />
	          
	          </td>
	          <td colspan="2" align="center" style="border-left:1px solid #000;padding-left:5px; border-bottom:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;">&nbsp;</td>
	        </tr>
               
        </c:forEach>
        
    </table>
    
   <c:if test="${loopPaginaProduto.last && !cotaEmissao.quebraTotalizacaoUltimaPagina}">
	 <table width="375" border="0" cellpadding="1" cellspacing="1">
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right">&nbsp;</td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td width="375">&nbsp;</td>
	    <td width="203" align="right"><strong>Valor Reparte R$</strong></td>
	    <td width="117" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong> Desconto %</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td  width="375" align="right"><strong>Valor Reparte Líquido R$</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong>Encalhe</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrEncalhe}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong>Total Líquido R$</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrTotalLiquido}</td>
	  </tr>
	</table>
  </c:if>     
       
       </td>
  </tr>
  
</table> 

</div> 

<c:if test="${loopPaginaProduto.last && cotaEmissao.quebraTotalizacaoUltimaPagina}">
	
<div class="quebraPaginaEmissao"> 

<table width="850" border="0" cellspacing="0" cellpadding="0"> 

  <tr>
  
  		<td width="680" valign="top">
    
        <table width="680" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:5px; margin-top:5px;"> 
  		<tr>
    		<td width="121" rowspan="6" valign="top">
    		<span class="logo">
    			<img src="${pageContext.request.contextPath}/cadastro/distribuidor/logo" width="110" height="70" alt="Novo Distrib"  />
    		</span>
    		</td>
    		<td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;">
    			<span class="titulo">Razão Social<br /></span>
    		</td>
    		<td width="237" align="center" style="border-left:1px solid #000;">
    			<span class="titulo" style="font-size:13px!important;">
    				<strong>CHAMADA DE ENCALHE</strong>
    			</span>
    		</td>
    	</tr>
  		<tr>
    		<td height="26" colspan="4" style="border-left:1px solid #000; border-bottom:1px solid #000;">
    			<span class="dadosNota"><font size="2"><b>${dadosDistribuidor.razaoSocial}</b></font></span>
    		</td>
    		<td rowspan="2" align="center" style="border-left:1px solid #000;">      
      			<H3>Documento Número: <font size="4">${cotaEmissao.idChamEncCota}</font></H3>
      
			</td>
    	</tr>
	  <tr>
	    <td width="172" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
	    </span></td>
	    <th width="25">&nbsp;</th>
	    <th width="75">&nbsp;</th>
	    <td width="130" align="center"><span class="titulo">CNPJ</span></td>
	  </tr>
	  <tr>
	    <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
	    </span></td>
	    <td style="border-bottom:1px solid #000;"><span class="dadosNota">    </span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
	    <td style="border-left:1px solid #000;">&nbsp;</td>
	    </tr>
	  <tr>
	    <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
	    <td align="center"><span class="titulo">UF</span></td>
	    <td align="center"><span class="titulo">CEP</span></td>
	    <td align="center"><span class="titulo">Inscrição Estadual</span></td>
	    <td style="border-left:1px solid #000;">&nbsp;</td>
	    </tr>
	  <tr>
	    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
	    <td align="center" style="border-left:1px solid #000;">&nbsp;</td>
	    </tr>
  </table>
  
	<table width="680" border="0" cellpadding="0" cellspacing="0" > 
	  <tr>
	    <td height="16" colspan="4" style="border-left:1px solid #000; border-right:1px solid #000; border-top:1px solid #000;"><span class="titulo">Cliente<br />
	    </span></td>
	    <td width="144" style="border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">CPF/CNPJ</span></td>
	  </tr>
	  <tr>
	    <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota"><font size="2"><b>${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</b></font></span></td>
	    <td style=" border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cnpj}</span></td>
	  </tr>
	  <tr>
	    <td width="376" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço  </span></td>
	    <td width="109" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
	    <td width="50" align="center" style="border-right:1px solid #000;"><span class="titulo">UF</span></td>
	    <td width="81" align="center" style="border-right:1px solid #000;"><span class="titulo">CEP</span></td>
	    <td style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
	  </tr>
	  <tr>
	    <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.endereco}</span></td>
	    <td style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.cidade}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.uf}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.cep}</span></td>
	    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.inscricaoEstadual}</span></td>
	  </tr>
	</table> 
	<table width="680" border="0" cellpadding="0" cellspacing="0" style="margin-bottom:5px;"> 
	  <tr>
	    <td width="179" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
	    <td width="161" align="center" style="border-right:1px solid #000;border-left:1px solid #000;"><span class="titulo">Data de Emissão</span></td>
	    <td width="420" style="border-right:1px solid #000;"><span class="titulo">Box / Roteiro / Rota</span></td>
	  </tr>
	  <tr>
	    <td align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataRecolhimento}</span></td>
	    <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;border-left:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
	    <td style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} / Roteiro: ${cotaEmissao.nomeRoteiro} / Rota: ${cotaEmissao.nomeRota}</span></td>
	  </tr>
	</table> 
    
	<table width="680" border="0" cellpadding="1" cellspacing="1">
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right">&nbsp;</td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td width="430">&nbsp;</td>
	    <td width="203" align="right"><strong>Valor Reparte R$</strong></td>
	    <td width="117" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong> Desconto %</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong>Valor Reparte Líquido R$</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong>Encalhe</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrEncalhe}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong>Total Líquido R$</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrTotalLiquido}</td>
	  </tr>
	</table>    
    
    </td>
    
	<td style="width:10px; border-left:1px dotted #000;">&nbsp;</td>
    
    <td width="30" valign="top">
    
    <table width="375" border="0" cellpadding="0" cellspacing="0" style=" margin-top:5px;">
      <tr>
        <td height="16" colspan="4" style="border-left:1px solid #000; border-top:1px solid #000;border-right:1px solid #000;"><span class="titulo">Razão Social<br />
        </span></td>
        </tr>
      <tr>
        <td height="26" colspan="4" style="border-left:1px solid #000;border-right:1px solid #000; border-bottom:1px solid #000;"><span class="dadosNota"><font size="2"><b>${dadosDistribuidor.razaoSocial}</b></font></span></td>
        </tr>
      <tr>
        <td width="199" height="15" style="border-left:1px solid #000;"><span class="titulo">Endereço<br />
        </span></td>
        <th width="23">&nbsp;</th>
        <th width="67">&nbsp;</th>
        <td width="121" align="center" style="border-right:1px solid #000;"><span class="titulo">CNPJ</span></td>
      </tr>
      <tr>
        <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.endereco}<br />
        </span></td>
        <td style="border-bottom:1px solid #000;"><span class="dadosNota"></span></td>
        <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cnpj}</span></td>
        </tr>
      <tr>
        <td style="border-left:1px solid #000;"><span class="titulo">Cidade</span></td>
        <td align="center"><span class="titulo">UF</span></td>
        <td align="center"><span class="titulo">CEP</span></td>
        <td align="center" style="border-right:1px solid #000;"><span class="titulo">Inscrição Estadual</span></td>
        </tr>
      <tr>
        <td style="border-left:1px solid #000;border-bottom:1px solid #000;"><span  class="dadosNota">${dadosDistribuidor.cidade}</span></td>
        <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.uf}</span></td>
        <td align="center" style="border-bottom:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.cep}</span></td>
        <td align="center" style="border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${dadosDistribuidor.inscricaoEstatual}</span></td>
        </tr>
    </table>
    
    <table width="375" border="0" cellpadding="0" cellspacing="0" style="border-top:1px solid #000; margin-bottom:5px;margin-top:5px;">
        <tr>
          <td width="207" height="24" align="center" style="border-left:1px solid #000;"><span class="titulo">Data de Recolhimento</span></td>
          <td width="203" align="center" style="border-left:1px solid #000;border-right:1px solid #000;">Data Emissão</td>
        </tr>
        <tr>
          <td height="22" align="center" style="border-left:1px solid #000;border-bottom:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataRecolhimento}</span></td>
          <td align="center" style="border-bottom:1px solid #000;border-left:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">${cotaEmissao.dataEmissao}</span></td>
        </tr>
        <tr>
          <td height="18" colspan="2" style="border-left:1px solid #000;border-right:1px solid #000;"><span class="titulo">Cliente</span></td>
        </tr>
        <tr>
          <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota"><font size="2"><b>${cotaEmissao.numCota} - ${cotaEmissao.nomeCota.toUpperCase()}</b></font></span></td>
        </tr>
							<tr>
          <td height="17" colspan="2" style="border-left:1px solid #000;border-right:1px solid #000;"><span class="titulo">Box / Roteiro / Rota</span></td>
        </tr>
        <tr>
          <td colspan="2" style="border-left:1px solid #000;border-bottom:1px solid #000;border-right:1px solid #000;"><span class="dadosNota">Box: ${cotaEmissao.box} / Roteiro: ${cotaEmissao.nomeRoteiro} / Rota: ${cotaEmissao.nomeRota}</span></td>
        </tr>
    </table>    
    
	<table width="375" border="0" cellpadding="1" cellspacing="1">
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right">&nbsp;</td>
	    <td align="right">&nbsp;</td>
	  </tr>
	  <tr>
	    <td width="375">&nbsp;</td>
	    <td width="203" align="right"><strong>Valor Reparte R$</strong></td>
	    <td width="117" align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparte}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong> Desconto %</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrComDesconto}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td  width="375" align="right"><strong>Valor Reparte Líquido R$</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrReparteLiquido}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong>Encalhe</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrEncalhe}</td>
	  </tr>
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right"><strong>Total Líquido R$</strong></td>
	    <td align="right" style="border-bottom:1px solid #000;">${cotaEmissao.vlrTotalLiquido}</td>
	  </tr>
	</table>
	    
    </td>
    
   </tr>
   
</table>

</div>
	
</c:if>

<c:if test="${withCapa && loopPaginaProduto.last}">

	<c:if test="${!personalizada}">
	
		<c:forEach items="${capasPaginadas}" var="paginaCapa">
		
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
	
		<c:forEach items="${cotaEmissao.paginasCapa}" var="paginaCapa">
	
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
