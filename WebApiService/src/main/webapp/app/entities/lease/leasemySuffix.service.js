(function() {
    'use strict';
    angular
        .module('labratApp')
        .factory('Lease', Lease);

    Lease.$inject = ['$resource', 'DateUtils'];

    function Lease ($resource, DateUtils) {
        var resourceUrl =  'api/leases/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
