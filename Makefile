
update-examples:
	./gradlew --daemon ribbonize
	cp ./example-custom/build/generated/ribbonizer/res/localDebug/mipmap-xxhdpi/ic_launcher.png ic-debug.png
	cp ./example-custom/build/generated/ribbonizer/res/localBeta/mipmap-xxhdpi/ic_launcher.png ic-beta.png
