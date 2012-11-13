/**
 * Show functions to be exported from the design doc.
 */

exports.full = {
    map: function (doc) {
        var obj = doc._id.split('/');
        var type = obj[0];
        var id = obj[1];

        if (type === 'logradouro') {
            emit([doc.cep, 1, ''], { _id: doc._id });
            emit([doc.cep, 2, 'localidade'], { _id: doc.localidade._id });
            emit([doc.cep, 3, 'bairroInicial'], { _id: doc.bairroInicial._id });
            emit([doc.cep, 4, 'bairroFinal'], { _id: doc.bairroFinal._id });
        }

        if (type === 'localidade') {
            emit([doc.cep, 1, ''], { _id: doc._id });
        }
    }
};


exports.ufs = {
    map: function (doc) {
        if (doc.tipoDocumento === 'uf') {
            emit(doc._id, null);
        }
    }
};

exports.monitoramento = {
    map: function(doc) {
        if (doc.erro == null) {
            emit(doc.tipoDocumento, 1);
        }
    },
    reduce: function (key, values, rereduce) {
        return sum(values);
    }
};
