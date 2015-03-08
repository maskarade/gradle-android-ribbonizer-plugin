# A Template Project on Gradle plugins for Android [![Build Status](https://travis-ci.org/gfx/gradle-plugin-template.svg)](https://travis-ci.org/gfx/gradle-plugin-template)

This is a template project on Gradle plugins for Android. The `plugin/` directory is the main
module that implements a Gradle plugin, including a plugin class, an extension class, and a task
class.

```
plugin/   - The main module of a Gradle plugin
example/  - An example android application that uses this plugin
buildSrc/ - A helper module to use this plugin in example modules
```

## Gradle Properties

Copy `gradle.properties.sample` into `~/.gradle/gradle.properties` and set correct values.

Note that `PGP_KEY_ID` is the value that `gpg --list-secret-keys` shows.

## Caveats

### Resources in plugin.jar

Reading resources from `plugin.jar` is extremely unstable so you'll see a method in
[FooTask.groovy](https://github.com/gfx/gradle-plugin-template/blob/master/plugin%2Fsrc%2Fmain%2Fgroovy%2Fcom%2Fgithub%2Fgfx%2Fgradle_plugin_template%2FFooTask.groovy):

```groovy
String getResourceContent(String filename) {
    URL templateFileUrl = getClass().getClassLoader().getResource(filename)
    if (templateFileUrl == null) {
        throw new GradleException("[${this.tag}] File not found: $filename")
    }

    try {
        return templateFileUrl.openStream().getText("UTF-8")
    } catch (IOException e) {
        // fallback to read JAR directly
        def connection = templateFileUrl.openConnection() as JarURLConnection
        def jarFile = connection.jarFileURL.toURI()
        ZipFile zip
        try {
            zip = new ZipFile(new File(jarFile))
        } catch (FileNotFoundException ex) {
            project.logger.warn("[${this.tag}] No plugin.jar. run `./gradlew plugin:jar` first.")
            throw ex
        }
        return zip.getInputStream((zip.getEntry(filename))).getText("UTF-8")
    }
}
```

It looks strange but works anyway. I don't know whether it is intended or not.

## See Also

* [Gradle User Guide](http://www.gradle.org/docs/current/userguide/userguide.html)
  * [Writing Custom Task Classes](http://www.gradle.org/docs/current/userguide/custom_tasks.html)
  * [Writing Custom Plugins](http://www.gradle.org/docs/current/userguide/custom_plugins.html)

## Author And License

Copyright 2014, FUJI Goro (gfx) <gfuji@cpan.org>. All rights reserved.

This library may be copied only under the terms of the Apache License 2.0, which may be found in the distribution.
