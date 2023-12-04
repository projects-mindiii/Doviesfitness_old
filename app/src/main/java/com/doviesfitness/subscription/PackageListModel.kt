package com.doviesfitness.subscription

data class PackageListModel(
    val `data`: Data,
    val settings: Settings
) {
    data class Data(
        val get_all_packages: List<GetAllPackage>,
        val get_static_content: List<GetStaticContent>,
        val get_subscription_terms: List<GetSubscriptionTerm>,
        val get_success_story: List<GetSuccessStory>
    ) {
        data class GetAllPackage(
            val android_package_id: String,
            val color_code: String,
            val description: String,
            val ios_package_id: String,
            val package_amount: String,
            val package_master_id: String,
            val package_name: String,
            val package_text: String
        )

        data class GetStaticContent(
            val content: String,
            val image: String,
            val title: String
        )

        data class GetSubscriptionTerm(
            val content: String,
            val title: String
        )

        data class GetSuccessStory(
            val image: String
        )
    }

    data class Settings(
        val fields: List<String>,
        val message: String,
        val success: String
    )
}