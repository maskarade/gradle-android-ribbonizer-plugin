
update-examples:
	./gradlew ribbonize
	cp ./example-simple/build/generated/ribbonizer/res/debug/mipmap-xxhdpi/ic_launcher.png ic-debug.png
	cp ./example-custom/build/generated/ribbonizer/res/localBeta/mipmap-xxhdpi/ic_launcher.png ic-beta.png
