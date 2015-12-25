'use strict';

angular.module('myappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


