'use strict';

angular.module('myapp2App')
    .factory('Employee', function ($resource, DateUtils) {
        return $resource('api/employees/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
