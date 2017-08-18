# The revision history of android-ribbonizer-plugin

## v2.1.0 2017/08/19

* Allow null values for filters and ribbon labels
* Added `largeRibbon` option to increase visibility in launcher icon foreground assets

## v2.0.0 2017/07/21

No code change from v1.1.0; the reason of bumping the version is that v1.1.0 has breaking changes in the artifact id (`s/plugin/ribbonizer-plugin/`).

## v1.1.0 2017/07/21

https://github.com/gfx/gradle-android-ribbonizer-plugin/compare/v1.0.0...v1.1.0

* Change the artifact id from `plugin` to `ribbonizer-plugin`
* [#16](https://github.com/gfx/gradle-android-ribbonizer-plugin/pull/16) Support for android:roundIcon launcher images
* [#19](https://github.com/gfx/gradle-android-ribbonizer-plugin/pull/19) Exclude XML resources

## v1.0.0 2016/11/16

* Add `customColorRibbonFilter(variant, iconFile, color)` (#13)

## v0.6.0 - 2016-06-16

* Add `forcedVariantsNames` (#11)

## v0.5.0 - 2015-11-19

* Build with Android Gradle plugin v1.5.0

## v0.4.0 - 2015-06-05

* [Add RibbonizerExtension#grayScaleFilter by kazy1991 · Pull Request #4 · gfx/gradle-android-ribbonizer-plugin](https://github.com/gfx/gradle-android-ribbonizer-plugin/pull/4)
* Add ways to add icon names (see `example-custom/build.gradle` for example)

## v0.3.0 - 2015-04-15

* Fix "AndroidManifest.xml not found" errors

## v0.2.0 - 2015-04-12

* Add `example-custom` to show how to use the `ribbonizer` extension block

## v0.1.1 - 2015-04-03

* Fix a crash caused by an Android Studio issue

## v0.1.0 - 2015-03-29

* Initial Release
