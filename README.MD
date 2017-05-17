# Hippo CMS Angular Support (hippo-ng)
This project can be used by developers looking to create custom features in Hippo CMS. The purpose of
this plugin is to remove the (more) complex logic from the presentation and implementation of the actual field.
## Installation
The first step would be to build the plugin, which can be done by cloning the project and running the `mvn install` command.
After, open the CMS pom.xml and add the following lines:
```xml
<dependency>
   <groupId>com.github.ibronsveld.hippo.angular</groupId>
    <artifactId>hippo-ng</artifactId>
   <version>1.1.0</version>
</dependency>
```

This will enable the CMS to load the dependencies required. This will also add the field to the CMS document type editor,
as a generic way to add your own fields to the document.
## Using and extending the plugin
More information can be found in the wiki.
### Adding custom behaviours to the field
More information can be found in the wiki.
### Adding custom data access
If needed, the plugin can be extended to have customized data storage on JCR level. This is done by extending the
JcrModelSerializer. More information can be found in the wiki.
## IMPORTANT: Upgrading the plugin to the 1.1 release version
If you are using any version before the latest release version, there are some specific things concerning upgrades 
you must take into account. Most of the changes are caused by refactoring during the addition of perspectives 
in the plugin.
### Dependency inclusion
Previously, Angular, Angular Material and any other dependencies were included by the plugin. In order to make it easier to 
 manage versions, this has been removed from this version of the plugin. Therefore, it is crucial you add these to the configuration
 of the plugin using the _hippo-ng.javascript.urls_ property.
### Changes in class names
Some of the classes were promoted to more generic classes, leading to changes in class names and packages. Next to this, all packages have been renamed to remove the _onehippo_ reference, 
to reflect the proper ownership of this plugin. Check the Installation documentation to see what the correct names should be.  
### Changes in JCR properties
This is probably one of the biggest changes. A lot of the properties were still name _angular.field.NAME_. Part of the cleanup was
the renaming of these properties to something that reflects the more generic nature of these. See the configuration page in the wiki for more details.
### Changes in the handling of requests
While actively working with the plugin, it quickly became clear that behaviors would run wild (pun intended). In other words, adding
 custom behaviors became a very boring task. To handle this with more ease (and to seperate things even more from the wicket world) a new
 concept has been introduced: the requesthandler. This allows you to register a handler for any request. By doing so, you can
  create just one or more handlers for your project, rather than adding more and more custom behaviors.
  
  These are configured in the settings of the panel, by adding the custom class names in the _hippo-ng.requesthandlers_ property.
### Changes in the SDK
The SDK has been changed to support perspectives as well. It has been renamed to _hngSdk_ and the _executeCall_ method has been deprecated.
In the next release of the SDK it will be removed. The entire :
####hngSdk.post(action, data)
Executes a post request for the specified action, sending the _data_ object in the body. Returns a promise. 

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History
v1.1.0 - First Release
## Plans 
Things that are on my list to pick up:
### Removal of parent directive in favor of using a service
In retrospect, the directive that wrapped the custom directive (for fields) is not really required. Looking to change this for a service later.
## Credits
Most of the work is my own, while some parts are made possible based on the source of Hippo CMS.
## License
This project is licensed based on the EPL-1.0 license. More information can be found here: http://www.eclipse.org/org/documents/epl-v10.php