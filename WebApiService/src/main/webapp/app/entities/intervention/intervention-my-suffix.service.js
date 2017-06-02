(function() {
    'use strict';
    angular
        .module('labratApp')
        .factory('Intervention', Intervention);

    Intervention.$inject = ['$resource', 'DateUtils'];

    function Intervention ($resource, DateUtils) {
        var resourceUrl =  'api/interventions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
