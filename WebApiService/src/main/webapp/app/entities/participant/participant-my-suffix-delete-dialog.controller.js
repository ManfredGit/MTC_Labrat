(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('ParticipantMySuffixDeleteController',ParticipantMySuffixDeleteController);

    ParticipantMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Participant'];

    function ParticipantMySuffixDeleteController($uibModalInstance, entity, Participant) {
        var vm = this;

        vm.participant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Participant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
