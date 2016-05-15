'use strict';
var sdkApp = angular.module('hippoAngularSDK', []);


/**
 * This is the controller field providing the SDK to the actual plugin
 * @param $scope dependency
 * @param $timeout
 * @param $log
 * @param $element
 * @param $http
 * @param $mdDialog
 */
function ngFieldController ($scope, $timeout, $log, $element, $http, $mdDialog) {

  /**
   * Gets the last time the model has been updated through the API
   * @returns {string|*} localized date
   */
  this.lastUpdate = function () {
    return $scope.lastUpdate;
  }

  /**
   * Gets the last time the model has been loaded through the API
   * @returns {string|*} localized date and time
   */
  this.lastLoad = function () {
    return $scope.lastLoad;
  }


  this.lastError = function () {
    return $scope.errors[0];
  }

  /**
   * Gets the mode that is set on the plugin (edit /)
   * @returns {*}
   */
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
}

sdkApp.directive('ngField', ['$scope', '$timeout', '$log', '$element', '$http', '$mdDialog', function () {
  return {
    restrict: 'A',
    scope: {
      mode: '@'
    },
    controller: ngFieldController
  };
}]);

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