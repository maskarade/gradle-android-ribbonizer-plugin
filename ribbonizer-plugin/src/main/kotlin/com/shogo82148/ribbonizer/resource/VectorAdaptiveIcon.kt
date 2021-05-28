package com.shogo82148.ribbonizer.resource

import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class VectorAdaptiveIcon(private val file: File) : Resource() {
    val document: Document
    init {
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        document = documentBuilder.parse(file)
    }

    override fun apply(filter: Filter) {
        filter.apply(this)
    }

    override fun save(file: File) {
        val tfFactory = TransformerFactory.newInstance()
        val tf = tfFactory.newTransformer()

        tf.setOutputProperty("indent", "yes")
        tf.setOutputProperty("encoding", "UTF-8")
        tf.transform(DOMSource(document), StreamResult(file));
    }
}