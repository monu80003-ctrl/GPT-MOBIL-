package com.monu.aiphonecontrol

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.view.ViewGroup
import android.graphics.Typeface

class MainActivity : Activity() {

    private lateinit var status: TextView
    private lateinit var command: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = (16 * resources.displayMetrics.density).toInt()

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
        }

        val title = TextView(this).apply {
            text = "MONU AI PHONE CONTROL"
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
        }

        status = TextView(this).apply {
            text = "पहले Accessibility Service चालू करें।"
            textSize = 15f
            setPadding(0, pad, 0, pad)
        }

        command = EditText(this).apply {
            hint = "उदाहरण: open com.android.settings"
            minLines = 2
        }

        val settings = Button(this).apply {
            text = "Accessibility सेटिंग खोलें"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        val run = Button(this).apply {
            text = "कमांड चलाएँ"
            setOnClickListener { runCommand() }
        }

        val help = TextView(this).apply {
            text = """
                कमांड उदाहरण:

                open com.android.settings
                open com.google.android.youtube
                tap 540 1200
                swipe 540 1500 540 500 400
                type नमस्ते

                यह शुरुआती लोकल प्रोटोटाइप है।
                ChatGPT से सुरक्षित रिमोट कनेक्शन अभी शामिल नहीं है।
            """.trimIndent()
            textSize = 14f
            setPadding(0, pad, 0, 0)
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(settings)

        layout.addView(
            command,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        layout.addView(run)
        layout.addView(help)

        setContentView(layout)
    }

    private fun runCommand() {
        val raw = command.text.toString().trim()

        if (raw.isEmpty()) return

        val service = PhoneAccessibilityService.instance

        if (service == null) {
            status.text =
                "सेवा चालू नहीं है। Accessibility सेटिंग में MONU Phone Control सक्षम करें।"
            return
        }

        status.text = service.executeCommand(raw)
    }
}
