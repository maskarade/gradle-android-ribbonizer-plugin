package com.github.gfx.ribbonizer.test

import com.github.gfx.ribbonizer.plugin.RibbonizerTask
import spock.lang.Specification

public class RibbonizerTaskTest extends Specification {
    def "resourceFilePattern"() {
        expect:
        RibbonizerTask.resourceFilePattern(resName) == pattern

        where:
        resName | pattern
        "@drawable/ic_launcher" | "drawable*/ic_launcher.*"
        "@mipmap/icon" | "mipmap*/icon.*"
    }
}