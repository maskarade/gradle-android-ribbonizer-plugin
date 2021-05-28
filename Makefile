.PHONY: test
test:
	./gradlew jar
	cd examples && ./gradlew check --info

.PHONY: update-examples
update-examples:
	./gradlew jar
	cd examples && ./gradlew ribbonize
	cp examples/simple/build/generated/ribbonizer/res/debug/mipmap-xxxhdpi/ic_launcher.png ic-debug.png
	cp examples/custom/build/generated/ribbonizer/res/localBeta/mipmap-xxxhdpi/ic_launcher.png ic-beta.png