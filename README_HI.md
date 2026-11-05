# MONU AI PHONE CONTROL — शुरुआती Android प्रोटोटाइप

यह Android 11+ के लिए एक स्थानीय प्रोटोटाइप है। यह Accessibility Service के जरिए ऐप खोलने, टैप, स्वाइप, फोकस्ड टेक्स्ट फ़ील्ड में टेक्स्ट भरने, Back और Home जैसी सीमित कार्रवाइयाँ कर सकता है।

## महत्वपूर्ण सीमा
यह अभी ChatGPT से जुड़ा हुआ नहीं है। इस ZIP में ऐसा कोई प्लगइन या क्लाउड ब्रिज नहीं है जो इस ChatGPT बातचीत को सीधे आपके फोन से जोड़ दे। ChatGPT से वास्तविक रिमोट कंट्रोल के लिए अलग सुरक्षित relay/backend, authentication, device pairing और अनुमति-आधारित command approval बनाना होगा। इसे पूर्ण AI फोन कंट्रोल न समझें।

## Android Studio से APK बनाना
1. ZIP को कंप्यूटर पर extract करें।
2. Android Studio में MONU_AI_PHONE_CONTROL फ़ोल्डर खोलें।
3. Gradle sync पूरा होने दें।
4. Build > Build Bundle(s) / APK(s) > Build APK(s) चुनें।
5. APK फोन में इंस्टॉल करें।

## फोन पर
1. ऐप खोलें।
2. Accessibility सेटिंग खोलें।
3. Installed services में MONU Phone Control खोजें और स्वयं सक्षम करें।
4. वापस ऐप में आकर कमांड चलाएँ।

## कमांड उदाहरण
- `open com.android.settings`
- `open com.google.android.youtube`
- `tap 540 1200`
- `swipe 540 1500 540 500 400`
- `type नमस्ते`
- `back`
- `home`

टैप के coordinates फोन के स्क्रीन रिज़ॉल्यूशन पर निर्भर हैं। Accessibility अनुमति बहुत शक्तिशाली होती है; केवल भरोसेमंद ऐप को दें।
