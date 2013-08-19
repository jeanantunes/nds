/**
 * Rewrite settings to be exported from the design doc
 */
module.exports = [
    {
        from: '/:val'
        , to: '_list/embedded/full'
        , query: {
            include_docs: 'true'
            , startkey: [':val']
            , endkey: [':val', {}]
        }
    }
];
