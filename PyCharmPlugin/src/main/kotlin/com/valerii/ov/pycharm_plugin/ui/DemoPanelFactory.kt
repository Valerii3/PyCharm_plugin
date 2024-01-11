package com.valerii.ov.pycharm_plugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.Insets
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.SwingUtilities


class DemoPanelFactory : ToolWindowFactory {
    companion object {
        private var instance: DemoPanelFactory? = null

        fun getInstance(): DemoPanelFactory? {
            return instance
        }
    }

    private lateinit var textArea: JTextArea


    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // Create a simple UI for the tool window
        val panel = JPanel(BorderLayout())
        textArea = JTextArea("Please, select any method or function you want to understand more deeply.").apply {
            lineWrap = true            // Enable line wrapping
            wrapStyleWord = true       // Enable word wrapping
            isEditable = false         // Make the text area non-editable
            margin = Insets(5, 5, 5, 5) // Optionally add some margin
        }
        panel.add(JScrollPane(textArea), BorderLayout.CENTER)

        // Add the panel to the tool window
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)

        instance = this
    }

    fun updateTextArea(text: String) {
        SwingUtilities.invokeLater {
            textArea.text = text
        }
    }
}