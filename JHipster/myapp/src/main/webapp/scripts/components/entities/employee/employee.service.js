'use strict';

angular.module('myappApp')
    .factory('Employee', function ($resource, DateUtils) {
        return $resource('api/employees/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertLocaleDateFromServer(data.created);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.created = DateUtils.convertLocaleDateToServer(data.created);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.created = DateUtils.convertLocaleDateToServer(data.created);
                    return angular.toJson(data);
                }
            }
        });
    });
