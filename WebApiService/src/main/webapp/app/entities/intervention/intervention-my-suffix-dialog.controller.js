(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('InterventionMySuffixDialogController', InterventionMySuffixDialogController);

    InterventionMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Intervention', 'Lease'];

    function InterventionMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Intervention, Lease) {
        var vm = this;

        vm.intervention = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.leases = Lease.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.intervention.id !== null) {
                Intervention.update(vm.intervention, onSaveSuccess, onSaveError);
            } else {
                Intervention.save(vm.intervention, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('labratApp:interventionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
