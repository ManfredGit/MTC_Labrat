(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('LeaseMySuffixDeleteController',LeaseMySuffixDeleteController);

    LeaseMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lease'];

    function LeaseMySuffixDeleteController($uibModalInstance, entity, Lease) {
        var vm = this;

        vm.lease = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lease.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
