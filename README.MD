# Hippo CMS Angular Support (hippo-ng)
This project can be used by developers looking to create custom features in Hippo CMS document editor. The purpose of
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
If needed, the plugin can be extended to have customized data storage on JCR level. This is done by modifying the
JcrModelSerializer.
## IMPORTANT: Upgrading the plugin to the 1.1 release version
If you are using any version before the latest release version, there are some specific things concerning upgrades 
you must take into account. Most of the changes are caused by refactoring during the addition of perspectives 
in the plugin.
### Changes in class names
Some of the classes were promoted to more generic classes, leading to changes in class names and packages. Next to this, all packages have been renamed to remove the _onehippo_ reference, 
to reflect the proper ownership of this plugin. Check the Installation documentation to see what the correct names should be.  
### Changes in JCR properties
This is probably one of the biggest changes. A lot of the properties were still name _angular.field.NAME_. Part of the cleanup was
the renaming of these properties to something that reflects the more generic nature of these. See the configuration page in the wiki for more details.
### Changes in the SDK
The SDK has been changed to support perspectives as well. It has been renamed to _hngSdk_ and the _executeCall_ method has been deprecated.
In the next release of the SDK it will be removed. To replace this, two new utility methods are provided:
####hngSdk.get(methodName)
Executes a get request for the specified method. Returns a promise. 
####hngSdk.post(methodName, data)
Executes a post request for the specified method, sending the _data_ object in the body. Returns a promise. 

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History
TODO: Write history
## Credits
TODO: Write credits
## License
TODO: Write license