'use strict';

angular.module('myapp2App')
    .factory('CommentSearch', function ($resource) {
        return $resource('api/_search/comments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
