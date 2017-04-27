(function() {
    'use strict';

    angular
        .module('labratApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('participant-my-suffix', {
            parent: 'entity',
            url: '/participant-my-suffix?page&sort&search',
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
        .state('participant-my-suffix-detail', {
            parent: 'participant-my-suffix',
            url: '/participant-my-suffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'labratApp.participant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/participant/participant-my-suffix-detail.html',
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
                        name: $state.current.name || 'participant-my-suffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('participant-my-suffix-detail.edit', {
            parent: 'participant-my-suffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participant-my-suffix-dialog.html',
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
        .state('participant-my-suffix.new', {
            parent: 'participant-my-suffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participant-my-suffix-dialog.html',
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
                    $state.go('participant-my-suffix', null, { reload: 'participant-my-suffix' });
                }, function() {
                    $state.go('participant-my-suffix');
                });
            }]
        })
        .state('participant-my-suffix.edit', {
            parent: 'participant-my-suffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participant-my-suffix-dialog.html',
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
                    $state.go('participant-my-suffix', null, { reload: 'participant-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('participant-my-suffix.delete', {
            parent: 'participant-my-suffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/participant/participant-my-suffix-delete-dialog.html',
                    controller: 'ParticipantMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Participant', function(Participant) {
                            return Participant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('participant-my-suffix', null, { reload: 'participant-my-suffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
