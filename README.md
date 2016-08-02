# Hippo CMS Angular Support (hippo-ng)
This project can be used by developers looking to create custom features in Hippo CMS environment. The purpose of
this plugin is to hide the CMS specific complexity from the presentation and implementation of the actual.
## Quick Start Installation
The first step would be to build the plugin, which can be done by cloning the project and running the `mvn install` command.
After this, open the CMS pom.xml and add the following lines:
```xml
<dependency>
   <groupId>org.onehippo.forge.angular</groupId>
    <artifactId>hippo-ng</artifactId>
   <version>1.0.1-SNAPSHOT</version>
</dependency>
```

This will enable the CMS to load the dependencies required.  
## Using and extending the plugin
More information can be found in the wiki.
### Adding custom behaviours to the plugin
More information can be found in the wiki.
### Adding custom data access
If needed, the plugin can be extended to have customized data storage on JCR level. This is done by modifying the
JcrModelSerializer. More information can be found in the wiki.
## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature` 
3. Commit your changes: `git commit -am 'Add some feature'` 
4. Push to the branch: `git push origin my-new-feature` 
5. Submit a pull request :D

## History
This project has been created by Ivo Bronsveld as part of a initiative to make it easier to create plugins for the Hippo CMS editing environment. 
## Credits
TODO: Write credits
## License
TODO: Write license