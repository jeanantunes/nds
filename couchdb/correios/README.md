test

    http://localhost:5984/dbcep/_design/cep/_rewrite/04101001

.kansorc

    exports.env = {
        // the default env will be used when you just do "kanso push"
        'default': {
            db: 'http://admin:foda-se@localhost:5984/dbcep'
        },
        // you can also define named environments this one will
        // be used when you do "kanso push production"
        'production': {
            db: 'http://username:password@production-hostname:5984/production-db',
            // tells the module package to minify modules.js
            minify: true,
            // useful when running behind a virtual host and
            // you want to force the baseURL to something
            baseURL: '/foo'
        }
    };

