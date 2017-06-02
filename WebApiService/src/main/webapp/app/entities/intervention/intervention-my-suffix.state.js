(function() {
    'use strict';

    angular
        .module('labratApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('intervention-my-suffix', {
            parent: 'entity',
            url: '/intervention-my-suffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'labratApp.intervention.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/intervention/interventionsmySuffix.html',
                    controller: 'InterventionMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('intervention');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('intervention-my-suffix-detail', {
            parent: 'intervention-my-suffix',
            url: '/intervention-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'labratApp.intervention.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/intervention/intervention-my-suffix-detail.html',
                    controller: 'InterventionMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('intervention');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Intervention', function($stateParams, Intervention) {
                    return Intervention.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'intervention-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('intervention-my-suffix-detail.edit', {
            parent: 'intervention-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/intervention/intervention-my-suffix-dialog.html',
                    controller: 'InterventionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Intervention', function(Intervention) {
                            return Intervention.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('intervention-my-suffix.new', {
            parent: 'intervention-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/intervention/intervention-my-suffix-dialog.html',
                    controller: 'InterventionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                startTime: null,
                                endTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('intervention-my-suffix', null, { reload: 'intervention-my-suffix' });
                }, function() {
                    $state.go('intervention-my-suffix');
                });
            }]
        })
        .state('intervention-my-suffix.edit', {
            parent: 'intervention-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/intervention/intervention-my-suffix-dialog.html',
                    controller: 'InterventionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Intervention', function(Intervention) {
                            return Intervention.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('intervention-my-suffix', null, { reload: 'intervention-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('intervention-my-suffix.delete', {
            parent: 'intervention-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/intervention/intervention-my-suffix-delete-dialog.html',
                    controller: 'InterventionMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Intervention', function(Intervention) {
                            return Intervention.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('intervention-my-suffix', null, { reload: 'intervention-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
