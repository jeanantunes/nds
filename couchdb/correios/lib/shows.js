/**
 * Show functions to be exported from the design doc.
 */

exports.not_found = function (doc, req) {
    return {
        title: '404 - Not Found',
        content: '404.html'
    };
};
