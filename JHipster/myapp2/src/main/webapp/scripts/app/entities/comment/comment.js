'use strict';

angular.module('myapp2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('comment', {
                parent: 'entity',
                url: '/comments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myapp2App.comment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/comment/comments.html',
                        controller: 'CommentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('comment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('comment.detail', {
                parent: 'entity',
                url: '/comment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myapp2App.comment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/comment/comment-detail.html',
                        controller: 'CommentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('comment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Comment', function($stateParams, Comment) {
                        return Comment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('comment.new', {
                parent: 'comment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/comment/comment-dialog.html',
                        controller: 'CommentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    content: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('comment', null, { reload: true });
                    }, function() {
                        $state.go('comment');
                    })
                }]
            })
            .state('comment.edit', {
                parent: 'comment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/comment/comment-dialog.html',
                        controller: 'CommentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Comment', function(Comment) {
                                return Comment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('comment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('comment.delete', {
                parent: 'comment',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/comment/comment-delete-dialog.html',
                        controller: 'CommentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Comment', function(Comment) {
                                return Comment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('comment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
