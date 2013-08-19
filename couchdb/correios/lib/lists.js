/**
 * List functions to be exported from the design doc.
 */

exports.embedded = function (head, req) {
// specify that we're providing a JSON response
    provides('html', function() {
        // create an array for our result set
        var results = [];

        while (row = getRow()) {
            if (undefined == row.doc) {
                if (!req.query.include_docs) {
                    throw (['error', 'expected parameter', 'precisa incluir ?include_docs=false']);
                } else {
                    debug_results = JSON.stringify(results);
                    debug_row = JSON.stringify(row);
//                    log(['error', 'object not found', 'results:['+debug_results+'] \r\n row['+debug_row+']']);
                }
            } else {
                var obj = row.doc._id.split('/');
                var type = obj[0];
                var id = obj[1];
                if (undefined != id) {
                    if (type === 'logradouro') {
                        results.push(row.doc);
                    } else {
                        results[results.length-1][row.key[2]] = row.doc
                    }
                }
            }
        }

        // make sure to stringify the results :)
        send(JSON.stringify(
            {
                total_rows: results.length
                , offset: parseInt( req.query.skip || '0' )
                , rows: results
//                , log: req
            })
        );
    });
};
