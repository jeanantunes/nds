/**
 * Show functions to be exported from the design doc.
 */
exports.por_logradouro = {
    index: function(doc) {
        var ret=new Document();
        ret.add(doc.LOG_NO);
        return ret
    }
};
