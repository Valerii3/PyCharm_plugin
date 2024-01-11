package com.valerii.ov.pycharm_plugin.dev

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.PyFunction

class ExplainMethodAction : AnAction() {

    private var selectedFunctionCode: String? = null

    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR)
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)

        if (editor == null || psiFile == null) {
            return
        }

        val elementAtCaret = psiFile.findElementAt(editor.caretModel.offset)
        val functionElement = PsiTreeUtil.getParentOfType(elementAtCaret,PyFunction::class.java)

        if (functionElement != null) {
            selectedFunctionCode = functionElement.text
            val localFunctionCode = selectedFunctionCode

            // Ensure localFunctionCode is not null before using it
            if (localFunctionCode != null) {
                sendCodeToOpenAI(localFunctionCode, "YOUR OPENAI API KEY HERE")
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val presentation = event.presentation
        presentation.isEnabledAndVisible = false

        val editor = event.getData(CommonDataKeys.EDITOR) ?: return
        val psiFile = event.getData(CommonDataKeys.PSI_FILE) ?: return
        val elementAtCaret = psiFile.findElementAt(editor.caretModel.offset)

        if (PsiTreeUtil.getParentOfType(elementAtCaret, PyFunction::class.java) != null) {
            presentation.isEnabledAndVisible = true
        }
    }
}

