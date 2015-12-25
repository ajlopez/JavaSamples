 'use strict';

angular.module('myapp2App')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-myapp2App-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-myapp2App-params')});
                }
                return response;
            }
        };
    });
