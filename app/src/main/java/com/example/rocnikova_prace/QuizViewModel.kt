package com.example.rocnikova_prace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocnikova_prace.data.local.entities.GroupEntity
import com.example.rocnikova_prace.data.model.QuestionItem
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.data.repository.QuestionRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import java.util.UUID

class QuizViewModel(private val repository: QuestionRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    companion object {
        private const val PROGRAMMING_GROUP_ID_EN = "900ea6f4-57fc-4d55-b8bb-f36b7626db2a"
        private const val PROGRAMMING_GROUP_ID_CS = "bbb630e3-4149-466c-b42b-5c5af0414304"
    }

    fun createQuizNotes(userNotes: String, onSuccess: (String) -> Unit) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val languageInfo = Locale.getDefault().language
        val isCzech = languageInfo == "cs" || languageInfo == "sk"

        val prompt = if (isCzech) {
            """
            Jsi zkušený učitel. Vytvoř aspoň 40 kvízových otázek na následující téma.
            Vymysli také vhodný a výstižný název pro tento kvíz.
            Odpověď MUSÍ být výhradně v platném JSON formátu (objekt bez Markdown bloku, žádný další text).
            
            Použij mix 3 typů otázek ("type"), přičemž zhruba 70 % otázek musí být typu "multiple_choice" a zbytek náhodně rozděl mezi "open" a "fill_blank":
            1. "multiple_choice" - musí mít pole "options" (přesně 4 možnosti) a pole "correctAnswers" (čísla 0 až 3 označující VŠECHNY správné odpovědi, může jich být více).
            2. "open" - má "question" a textovou položku "answer" představující vzorově správnou odpověď.
            3. "fill_blank" - má "question" (s vynechaným místem označeným "___") a "answer" se slovem k doplnění.
            
            Struktura musí přesně odpovídat tomuto formátu:
            {
              "title": "Název kvízu",
              "questions": [
                {
                  "type": "multiple_choice",
                  "question": "Text otázky?",
                  "options": ["A) Možnost 1", "B) Možnost 2", "C) Možnost 3", "D) Možnost 4"],
                  "correctAnswers": [0, 2]
                },
                {
                  "type": "open",
                  "question": "Stručně popište...",
                  "answer": "Krátké vysvětlení problému"
                },
                {
                  "type": "fill_blank",
                  "question": "Hlavní město ČR je ___.",
                  "answer": "Praha"
                }
              ]
            }
            Mluv pouze česky a buď stručný. Vytvoř aspoň 40 otázek.
            
            Zadané téma:
            $userNotes
        """.trimIndent()
        } else {
            """
            You are an experienced teacher. Create at least 40 quiz questions on the following topic.
            Also invent a suitable and descriptive title for this quiz.
            The response MUST be exclusively in valid JSON format (an object without Markdown block, no other text).
            
            Use a mix of 3 types of questions ("type"), with about 70% being "multiple_choice" and the rest randomly divided between "open" and "fill_blank":
            1. "multiple_choice" - must have an "options" array (exactly 4 options) and a "correctAnswers" array (numbers 0 to 3 indicating ALL correct answers, there can be multiple).
            2. "open" - has "question" and a text item "answer" representing the correct answer.
            3. "fill_blank" - has a "question" (with a blank space denoted by "___") and an "answer" with the word to fill in.
            
            The structure must exactly match this format:
            {
              "title": "Quiz Title",
              "questions": [
                {
                  "type": "multiple_choice",
                  "question": "Question text?",
                  "options": ["A) Option 1", "B) Option 2", "C) Option 3", "D) Option 4"],
                  "correctAnswers": [0, 2]
                },
                {
                  "type": "open",
                  "question": "Briefly describe...",
                  "answer": "Short explanation of the issue"
                },
                {
                  "type": "fill_blank",
                  "question": "The capital of the UK is ___.",
                  "answer": "London"
                }
              ]
            }
            Speak only English and be concise. Create at least 40 questions.
            
            Topic:
            $userNotes
        """.trimIndent()
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val textResponse = withContext(Dispatchers.IO) {
                    val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key=$apiKey")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true

                    val jsonBody = JSONObject().apply {
                        put("contents", JSONArray().apply {
                            put(JSONObject().apply {
                                put("parts", JSONArray().apply {
                                    put(JSONObject().apply {
                                        put("text", prompt)
                                    })
                                })
                            })
                        })
                    }

                    val requestBodyStr = jsonBody.toString()
                    OutputStreamWriter(connection.outputStream).use { it.write(requestBodyStr) }

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                        val responseJson = JSONObject(responseText)
                        val candidates = responseJson.optJSONArray("candidates")
                        val firstCandidate = candidates?.optJSONObject(0)
                        val content = firstCandidate?.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        val firstPart = parts?.optJSONObject(0)
                        firstPart?.optString("text") ?: "[]"
                    } else {
                        val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "No error stream content"
                        throw Exception("HTTP Error $responseCode: $errorText")
                    }
                }

                val cleanJson = textResponse.trim().removePrefix("```json").removeSuffix("```").trim()
                val rootObject = JSONObject(cleanJson)
                val defaultTitle = if (isCzech) "Kvíz na zadané téma" else "Quiz on topic"
                val quizTitle = rootObject.optString("title", defaultTitle)
                val jsonArray = rootObject.getJSONArray("questions")

                val currentUser = SupabaseClient.client.auth.currentUserOrNull()?.id
                    ?: throw Exception(if (isCzech) "Uživatel není přihlášen!" else "User is not logged in!")

                val groupId = UUID.randomUUID().toString()
                val newGroup = GroupEntity(
                    id = groupId,
                    userId = currentUser,
                    name = quizTitle,
                    description = if (isCzech) "Automaticky vygenerovaný kvíz na zadané téma." else "Automatically generated quiz on the topic."
                )

                repository.saveGroup(newGroup)

                for (i in 0 until jsonArray.length()) {
                    val qObj = jsonArray.getJSONObject(i)
                    val type = qObj.optString("type", "multiple_choice")
                    val qText = qObj.getString("question")

                    val questionItem: QuestionItem = when (type) {
                        "open" -> {
                            QuestionItem.Open(
                                id = UUID.randomUUID().toString(),
                                groupId = groupId,
                                userId = currentUser,
                                question = qText,
                                answer = qObj.optString("answer", ""),
                                isExpanded = false
                            )
                        }
                        "fill_blank" -> {
                            QuestionItem.FillBlank(
                                id = UUID.randomUUID().toString(),
                                groupId = groupId,
                                userId = currentUser,
                                question = qText,
                                answer = qObj.optString("answer", ""),
                                isExpanded = false
                            )
                        }
                        else -> {
                            val optsArray = qObj.optJSONArray("options") ?: JSONArray()
                            val answersList = List(4) { idx -> if (idx < optsArray.length()) optsArray.getString(idx) else "" }

                            val correctIndicesList = MutableList(4) { false }
                            val correctAnswersArray = qObj.optJSONArray("correctAnswers")
                            if (correctAnswersArray != null) {
                                for (j in 0 until correctAnswersArray.length()) {
                                    val cIdx = correctAnswersArray.optInt(j, -1)
                                    if (cIdx in 0..3) {
                                        correctIndicesList[cIdx] = true
                                    }
                                }
                            } else {
                                val cIdx = qObj.optInt("correctAnswer", -1)
                                if (cIdx in 0..3) {
                                    correctIndicesList[cIdx] = true
                                }
                            }

                            QuestionItem.MultipleChoice(
                                id = UUID.randomUUID().toString(),
                                groupId = groupId,
                                userId = currentUser,
                                question = qText,
                                answers = answersList,
                                correctIndices = correctIndicesList,
                                isExpanded = false
                            )
                        }
                    }

                    repository.saveQuestion(questionItem)
                }
                _isLoading.value = false
                onSuccess(groupId)

            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun importSchoolQuestions(onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val languageCode = Locale.getDefault().language.lowercase(Locale.ROOT)
                val targetGroupId = if (languageCode == "cs" || languageCode == "sk") {
                    PROGRAMMING_GROUP_ID_CS
                } else {
                    PROGRAMMING_GROUP_ID_EN
                }

                repository.importGlobalGroupById(targetGroupId)
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _isLoading.value = false
                e.printStackTrace()
                onError(e.message ?: "Import failed")
            }
        }
    }
}