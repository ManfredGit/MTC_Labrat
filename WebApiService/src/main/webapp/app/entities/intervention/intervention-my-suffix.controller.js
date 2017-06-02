(function() {
    'use strict';

    angular
        .module('labratApp')
        .controller('InterventionMySuffixController', InterventionMySuffixController);

    InterventionMySuffixController.$inject = ['Intervention'];

    function InterventionMySuffixController(Intervention) {

        var vm = this;

        vm.interventions = [];

        loadAll();

        function loadAll() {
            Intervention.query(function(result) {
                vm.interventions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
