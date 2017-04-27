(function() {
    'use strict';

    angular
        .module('labratApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lease-my-suffix', {
            parent: 'entity',
            url: '/lease-my-suffix?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'labratApp.lease.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lease/leasesmySuffix.html',
                    controller: 'LeaseMySuffixController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lease');
                    $translatePartialLoader.addPart('leaseStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lease-my-suffix-detail', {
            parent: 'lease-my-suffix',
            url: '/lease-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'labratApp.lease.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lease/lease-my-suffix-detail.html',
                    controller: 'LeaseMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lease');
                    $translatePartialLoader.addPart('leaseStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lease', function($stateParams, Lease) {
                    return Lease.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'lease-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('lease-my-suffix-detail.edit', {
            parent: 'lease-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lease/lease-my-suffix-dialog.html',
                    controller: 'LeaseMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lease', function(Lease) {
                            return Lease.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lease-my-suffix.new', {
            parent: 'lease-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lease/lease-my-suffix-dialog.html',
                    controller: 'LeaseMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDate: null,
                                endDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lease-my-suffix', null, { reload: 'lease-my-suffix' });
                }, function() {
                    $state.go('lease-my-suffix');
                });
            }]
        })
        .state('lease-my-suffix.edit', {
            parent: 'lease-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lease/lease-my-suffix-dialog.html',
                    controller: 'LeaseMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lease', function(Lease) {
                            return Lease.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lease-my-suffix', null, { reload: 'lease-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lease-my-suffix.delete', {
            parent: 'lease-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lease/lease-my-suffix-delete-dialog.html',
                    controller: 'LeaseMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lease', function(Lease) {
                            return Lease.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('lease-my-suffix', null, { reload: 'lease-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
