'use strict';

angular.module('myapp2App')
    .factory('EmployeeSearch', function ($resource) {
        return $resource('api/_search/employees/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
