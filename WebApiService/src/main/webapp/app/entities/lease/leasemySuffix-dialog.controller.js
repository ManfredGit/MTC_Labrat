(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('LeaseMySuffixDialogController', LeaseMySuffixDialogController);

    LeaseMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lease', 'Device', 'Participant'];

    function LeaseMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lease, Device, Participant) {
        var vm = this;

        vm.lease = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.devices = Device.query();
        vm.participants = Participant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lease.id !== null) {
                Lease.update(vm.lease, onSaveSuccess, onSaveError);
            } else {
                Lease.save(vm.lease, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('labratApp:leaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
