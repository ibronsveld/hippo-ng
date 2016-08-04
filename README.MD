# Hippo CMS Angular Support (hippo-ng)
This project can be used by developers looking to create custom features in Hippo CMS document editor. The purpose of
this plugin is to remove the (more) complex logic from the presentation and implementation of the actual field.
## Installation
The first step would be to build the plugin, which can be done by cloning the project and running the `mvn install` command.
After, open the CMS pom.xml and add the following lines:
```xml
<dependency>
   <groupId>org.onehippo.forge.angular</groupId>
    <artifactId>hippo-ng</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>
```

This will enable the CMS to load the dependencies required. This will also add the field to the CMS document type editor,
as a generic way to add your own fields to the document.
## Using and extending the plugin
More information can be found in the docs section.
### Adding custom behaviours to the field
More information can be found in the docs section.
### Adding custom data access
If needed, the plugin can be extended to have customized data storage on JCR level. This is done by modifying the
JcrModelSerializer.
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