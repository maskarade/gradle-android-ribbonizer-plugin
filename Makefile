.PHONY: test
test:
	./gradlew jar
	cd examples && ./gradlew check --info
	cd examples && ./gradlew ribbonize --info

.PHONY: publish
publish:
	./gradlew publish

.PHONY: update-examples
update-examples:
	./gradlew jar
	cd examples && ./gradlew ribbonize --info
	cp examples/simple/build/generated/ribbonizer/res/debug/mipmap-xxxhdpi/ic_launcher.png ic-debug.png
	cp examples/custom/build/generated/ribbonizer/res/localBeta/mipmap-xxxhdpi/ic_launcher.png ic-beta.png
