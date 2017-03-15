(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('ParticipantMySuffixDetailController', ParticipantMySuffixDetailController);

    ParticipantMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Participant'];

    function ParticipantMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, Participant) {
        var vm = this;

        vm.participant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('labratApp:participantUpdate', function(event, result) {
            vm.participant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
