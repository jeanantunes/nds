var tap = require('tap');
var cradle = require('cradle');

var c = new(cradle.Connection);
var db = c.database('dbcep');
var app = require('../lib/app');

tap.test('CouchDB Teste Conexao', function(t) {
    db.exists(function (err, exists) {
        t.notOk(err, "Sem problemas ao conectar no couch");
        t.ok(exists, "Banco de Dados existe");
        t.end()
    });
})

tap.test('Carregar a CouchApp', function(t) {
    t.doesNotThrow(load_app, 'Sem Problemas para carregar o app.js')
    t.end()

    function load_app() {
        var app = require('../lib/app')
    }
})

tap.test('A Funcao List', function(t) {

    var embedded = app.lists.embedded;

    t.type(embedded, 'function', 'Testando a existencia da lista embedded');

  //t.equal(full(doc, null_req), mock, '"Hello world" by default');
  //t.equal(hello(doc, john_req), 'Hello, John Doe', 'Supports ?who query string')
    t.end()
})

tap.test('A Funcao View', function(t) {

    var map = app.views.full.map;

    t.type(map, 'function', 'Testando a existencia do map de CEP');

    db.view('cep/full', function (err, res) {
//      console.log(JSON.stringify(res.length));
        t.equal(res.length, 6, "teste de retorno da view");
        t.end()
    });
})

