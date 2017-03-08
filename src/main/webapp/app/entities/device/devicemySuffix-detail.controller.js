(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('DeviceMySuffixDetailController', DeviceMySuffixDetailController);

    DeviceMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Device'];

    function DeviceMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Device) {
        var vm = this;

        vm.device = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('labratApp:deviceUpdate', function(event, result) {
            vm.device = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
