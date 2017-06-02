(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('InterventionMySuffixDetailController', InterventionMySuffixDetailController);

    InterventionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Intervention', 'Lease'];

    function InterventionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Intervention, Lease) {
        var vm = this;

        vm.intervention = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('labratApp:interventionUpdate', function(event, result) {
            vm.intervention = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
