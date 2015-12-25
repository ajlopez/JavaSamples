'use strict';

angular.module('myapp2App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


