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
  this.getLastChanged = function () {
    return $scope.lastChanged;
  }

  /**
   * Gets the last time the model has been loaded through the API
   * @returns {string|*} localized date and time
   */
  this.getLastUpdate = function () {
    return $scope.lastUpdate;
  }


  /**
   * Gets the mode that is set on the plugin (edit / compare)
   * @returns {string} mode that is set by the CMS
   */
  this.getMode = function () {
    return $scope.mode;
  }

  /**
   * Loads the model as it is currently available in the CMS.
   *
   * @returns {*} promise of the $http.post
   */
  this.loadModel = function () {
    $log.debug("Loading model");
    var queryUrl = $element.attr('getModel');
    if (queryUrl != undefined) {
      return $http.post(queryUrl).then(
          function (response) {
            $scope.lastUpdate = new Date().toLocaleString();
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

  /**
   * This method will update the current model in the CMS with the supplied model object
   * @param model
   * @returns {*} promise of the $http.post
   */
  this.updateModel = function (model) {
    $log.debug("Updating model");
    var queryUrl = $element.attr('setModel');
    if (queryUrl != undefined) {
      return $http.post(queryUrl, model).then(
          function (response) {
            $scope.lastChanged = new Date().toLocaleString();
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

  this.getPluginConfig = function() {
    $log.debug("Get Plugin Configuration");
    var queryUrl = $element.attr('getPluginConfig');
    if (queryUrl != undefined) {
      return $http.get(queryUrl).then(
          function (response) {
            return response;
          },
          function (httpError) {
            // translate the error
            throw httpError.status + " : " +
            httpError.data;
          }
      );
    };
  }

  /**
   * Gets the associated callback from the element. Provided for utility purposes
   * @param callbackName name of the attribute to look for.
   * @returns {*} attribute value
   */
  this.getCallback = function (callbackName) {
    return $element.attr(callbackName);
  };

  /**
   * Executes the get method if it can find the call back
   * @param callbackName
   * @returns {*} promise
   */
  this.executeCall = function (callbackName) {
    var queryUrl = this.getCallback(callbackName);
    if (queryUrl != undefined) {
      return $http.get(queryUrl).then(
          function (response) {
            return response;
          },
          function (httpError) {
            // translate the error
            throw httpError.status + " : " +
            httpError.data;
          }
      );
    }
  }

  /**
   * Shows an Angular Material Dialog
   * @param templateDialog Dialog URL
   * @param controllerFn Controller function
   * @returns {*} promise
   */
  this.showDialog = function (templateDialog, controllerFn) {
    return $mdDialog.show({
      controller: controllerFn,
      templateUrl: templateDialog,
      parent: angular.element(document.body)
    });
  };

  /**
   * Switches perspectives in the CMS by using the configured {@link org.onehippo.forge.angular.perspective.IAngularPerspectiveService}
   * @param name name of the service
   * @param action action to execute (provided to service)
   * @param option option to pass (provided to service)
   *
   */
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

sdkApp.directive('ngField',  function () {
  return {
    restrict: 'A',
    scope: {
      mode: '@'
    },
    controller: ['$scope', '$timeout', '$log', '$element', '$http', '$mdDialog', ngFieldController]
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