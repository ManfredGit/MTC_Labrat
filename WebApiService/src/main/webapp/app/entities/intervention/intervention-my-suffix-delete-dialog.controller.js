(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('InterventionMySuffixDeleteController',InterventionMySuffixDeleteController);

    InterventionMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'Intervention'];

    function InterventionMySuffixDeleteController($uibModalInstance, entity, Intervention) {
        var vm = this;

        vm.intervention = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Intervention.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
