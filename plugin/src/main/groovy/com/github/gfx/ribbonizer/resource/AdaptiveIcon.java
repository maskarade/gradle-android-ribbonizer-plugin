package com.github.gfx.ribbonizer.resource;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;

public class AdaptiveIcon extends Resource {
    private File file;
    public File getFile() {
        return file;
    }

    public AdaptiveIcon(File file) throws SAXException, ParserConfigurationException, IOException {
        this.file = file;
        GPathResult iconXml = new XmlSlurper().parse(file);
        GPathResult foregroundNode = (GPathResult) iconXml.getProperty("foreground");
        String foreground = String.valueOf(foregroundNode.getProperty("@android:drawable"));
    }

    @Override
    public void apply(Filter filter) {
    }

    @Override
    public void save(File dir) throws IOException {

    }
}
