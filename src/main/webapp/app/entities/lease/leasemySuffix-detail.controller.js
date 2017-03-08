(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('LeaseMySuffixDetailController', LeaseMySuffixDetailController);

    LeaseMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lease', 'Device', 'Participant'];

    function LeaseMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Lease, Device, Participant) {
        var vm = this;

        vm.lease = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('labratApp:leaseUpdate', function(event, result) {
            vm.lease = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
