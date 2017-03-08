(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('ParticipantMySuffixDialogController', ParticipantMySuffixDialogController);

    ParticipantMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Participant'];

    function ParticipantMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Participant) {
        var vm = this;

        vm.participant = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.participant.id !== null) {
                Participant.update(vm.participant, onSaveSuccess, onSaveError);
            } else {
                Participant.save(vm.participant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('labratApp:participantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
