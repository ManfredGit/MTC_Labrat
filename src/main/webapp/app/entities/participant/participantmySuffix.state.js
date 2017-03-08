(function() {
    'use strict';

    angular
        .module('labratApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('participantmySuffix', {
            parent: 'entity',
            url: '/participantmySuffix?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'labratApp.participant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participant/participantsmySuffix.html',
                    controller: 'ParticipantMySuffixController',
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
                    $translatePartialLoader.addPart('participant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('participantmySuffix-detail', {
            parent: 'participantmySuffix',
            url: '/participantmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'labratApp.participant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participant/participantmySuffix-detail.html',
                    controller: 'ParticipantMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('participant');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Participant', function($stateParams, Participant) {
                    return Participant.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'participantmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('participantmySuffix-detail.edit', {
            parent: 'participantmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participantmySuffix-dialog.html',
                    controller: 'ParticipantMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Participant', function(Participant) {
                            return Participant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('participantmySuffix.new', {
            parent: 'participantmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participantmySuffix-dialog.html',
                    controller: 'ParticipantMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                phoneNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('participantmySuffix', null, { reload: 'participantmySuffix' });
                }, function() {
                    $state.go('participantmySuffix');
                });
            }]
        })
        .state('participantmySuffix.edit', {
            parent: 'participantmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participantmySuffix-dialog.html',
                    controller: 'ParticipantMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Participant', function(Participant) {
                            return Participant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('participantmySuffix', null, { reload: 'participantmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('participantmySuffix.delete', {
            parent: 'participantmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participantmySuffix-delete-dialog.html',
                    controller: 'ParticipantMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Participant', function(Participant) {
                            return Participant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('participantmySuffix', null, { reload: 'participantmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
