<!-- 

	VIEW REFERENTE A EMS0081
	
 -->

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>meuConteudo</title>

<script type="text/javascript">
$(function() {
    $("#flex1").flexigrid({
            url: '<c:url value="/extratoEdicao/toJSon"/>',
            dataType: 'json',
            colModel : [
                    {display: 'Id', name : 'id', width : 40, sortable : true, align: 'left'},
                    {display: 'Data', name : 'data', width : 40, sortable : true, align: 'left'},
                    {display: 'Movimento', name : 'movimento', width : 150, sortable : true, align: 'left'},
                    {display: 'Entrada', name : 'entrada', width : 150, sortable : true, align: 'left'},
                    {display: 'Saida', name : 'saida', width : 150, sortable : true, align: 'left'},
                    {display: 'Parcial', name : 'parcial', width : 150, sortable : true, align: 'left'}
            ],
            buttons : [
                    {name: 'Edit', bclass: 'edit', onpress : function(){}},
                    {name: 'Delete', bclass: 'delete', onpress : function(){}},
                    {separator: true}
            ],
            searchitems : [
                    {display: 'First Name', name : 'nome'},
                    {display: 'Surname', name : 'email', isdefault: true}
            ],
            sortname: "id",
            sortorder: "asc",
            usepager: true,
            title: "Staff",
            useRp: true,
            rp: 10,
            showTableToggleBtn: false,
            resizable: false,
            width: 700,
            height: 370,
            singleSelect: true
    });
});

</script>

</head>

<body>
	
	<table id="flex1"></table>
	
</body>

</html>