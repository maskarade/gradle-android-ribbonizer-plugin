test:
	./gradlew jar
	cd examples && ./gradlew check --info
