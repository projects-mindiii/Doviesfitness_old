package com.doviesfitness.ui.setting.faq

data class FAQModel(
     val `data`: List<Data>,
     val settings: Settings
) {
     data class Data(
          val faq_answer: String = "",
          val faq_id: String = "",
          val faq_question: String = "",
          var isOpen: String? = "NO"
     )

     data class Settings(
          val fields: List<String>,
          val message: String,
          val success: String
     )
}