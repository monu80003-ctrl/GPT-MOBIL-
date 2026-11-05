package com.monu.aiphonecontrol

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class PhoneAccessibilityService : AccessibilityService() {
    companion object { @Volatile var instance: PhoneAccessibilityService? = null }

    override fun onServiceConnected() { instance = this }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() { }
    override fun onDestroy() {
        if (instance === this) instance = null
        super.onDestroy()
    }

    fun executeCommand(raw: String): String {
        val parts = raw.split(" ", limit = 6)
        return try {
            when (parts[0].lowercase()) {
                "open" -> {
                    if (parts.size < 2) return "उदाहरण: open com.android.settings"
                    val intent = packageManager.getLaunchIntentForPackage(parts[1])
                        ?: return "इस package का ऐप नहीं मिला: ${parts[1]}"
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    "ऐप खोलने का अनुरोध भेजा: ${parts[1]}"
                }
                "tap" -> {
                    if (parts.size < 3) return "उदाहरण: tap 540 1200"
                    val x = parts[1].toFloat(); val y = parts[2].toFloat()
                    val path = Path().apply { moveTo(x, y) }
                    dispatch(path, 80)
                    "टैप भेजा: $x, $y"
                }
                "swipe" -> {
                    if (parts.size < 5) return "उदाहरण: swipe 540 1500 540 500 400"
                    val x1 = parts[1].toFloat(); val y1 = parts[2].toFloat()
                    val x2 = parts[3].toFloat(); val y2 = parts[4].toFloat()
                    val duration = if (parts.size >= 6) parts[5].toLong() else 400L
                    val path = Path().apply { moveTo(x1, y1); lineTo(x2, y2) }
                    dispatch(path, duration)
                    "स्वाइप भेजा गया।"
                }
                "type" -> {
                    val text = raw.substringAfter(" ", "")
                    if (text.isBlank()) return "उदाहरण: type नमस्ते"
                    val root = rootInActiveWindow ?: return "सक्रिय स्क्रीन उपलब्ध नहीं है।"
                    val node = findFocusedEditable(root) ?: return "कोई टेक्स्ट बॉक्स फोकस में नहीं है।"
                    val args = Bundle().apply { putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text) }
                    val ok = node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
                    if (ok) "टेक्स्ट दर्ज किया गया।" else "टेक्स्ट दर्ज नहीं हो सका।"
                }
                "back" -> { performGlobalAction(GLOBAL_ACTION_BACK); "Back भेजा गया।" }
                "home" -> { performGlobalAction(GLOBAL_ACTION_HOME); "Home भेजा गया।" }
                else -> "कमांड समझ नहीं आया। open / tap / swipe / type / back / home इस्तेमाल करें।"
            }
        } catch (e: Exception) {
            "कमांड असफल: ${e.message ?: "अज्ञात त्रुटि"}"
        }
    }

    private fun findFocusedEditable(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (node.isEditable && node.isFocused) return node
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findFocusedEditable(child)
            if (found != null) return found
        }
        return null
    }

    private fun dispatch(path: Path, duration: Long) {
        val stroke = GestureDescription.StrokeDescription(path, 0, duration.coerceIn(50, 3000))
        val gesture = GestureDescription.Builder().addStroke(stroke).build()
        dispatchGesture(gesture, null, null)
    }
}
