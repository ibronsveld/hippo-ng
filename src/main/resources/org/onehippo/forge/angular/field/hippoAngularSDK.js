'use strict';
var sdkApp = angular.module('hippoAngularSDK', []);

sdkApp.directive('ngField', function () {
  return {
    restrict: 'A',
    scope: {
      mode: '@'
    },
    controller: ['$scope', '$timeout', '$log', '$element', '$http', '$mdDialog', function ($scope, $timeout, $log, $element, $http, $mdDialog) {

      this.lastUpdate = function () {
        return $scope.lastUpdate;
      }

      this.lastLoad = function () {
        return $scope.lastLoad;
      }

      this.lastError = function () {
        return $scope.errors[0];
      }

      this.getMode = function () {
        return $scope.mode;
      }

      this.loadModel = function () {
        $log.debug("Loading model");
        var queryUrl = $element.attr('getModel');
        if (queryUrl != undefined) {
          return $http.post(queryUrl).then(
              function (response) {
                $scope.lastLoad = new Date().toLocaleString();
                return response;
              },
              function (httpError) {
                // translate the error
                throw httpError.status + " : " +
                httpError.data;
              }
          )
        }
      }

      this.updateModel = function (model) {
        $log.debug("Updating model");
        var queryUrl = $element.attr('setModel');
        if (queryUrl != undefined) {
          return $http.post(queryUrl, model).then(
              function (response) {
                $scope.lastUpdate = new Date().toLocaleString();
                return response;
              },
              function (httpError) {
                // translate the error
                throw httpError.status + " : " +
                httpError.data;
              }
          );
        }
      };

      this.getCallback = function (callbackName) {
        return $element.attr(callbackName);
      }

      this.showDialog = function (templateDialog, controllerFn) {
        return $mdDialog.show({
          controller: controllerFn,
          templateUrl: templateDialog,
          parent: angular.element(document.body)
        });
      };

      this.switchPerspective = function (name, action, option) {
        $log.debug("Switching perspectives");
        var callback = $element.attr('switchPerspective');
        if (callback != undefined) {

          $log.debug("Calling action " + action + " for name " + name + " with options " + JSON.stringify(option));
          callback = callback.replace('__ACTION', action);
          callback = callback.replace('__OPTION', option);
          callback = callback.replace('__NAME', name);

          eval(callback);
        }
      }
    }]
  };
});

sdkApp.directive('ngPerspective', function () {
  return {
    restrict: 'A',
    scope: {
      mode: '='
    },
    controller: ['$scope', '$timeout', '$log', '$element', '$http', function ($scope, $timeout, $log, $element, $http) {

      this.getMode = function () {
        return $scope.mode;
      }

      this.getUserSessionInfo = function () {
        $log.debug("Loading userInfo");
        var queryUrl = this.getCallback('getUserSessionInfo');
        if (queryUrl != undefined) {
          return $http.post(queryUrl);
        }
      }

      this.getCallback = function (callbackName) {
        return $element.attr(callbackName);
      }
    }]
  };
});