package com.doviesfitness.temp.modal

import com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background.DownloadedModal
import java.io.Serializable

/**Created by Yashank Rathore on 18,December,2020 yashank.mindiii@gmail.com **/

data class VideoCategoryModal(
                              val stream_workout_description: String,
                              val stream_workout_id: String,
                              val stream_workout_image: String,
                              val stream_workout_image_url: String,
                              val stream_workout_name: String,
                              val download_list: ArrayList<DownloadedVideoModal>
                            ): Serializable {
    override fun toString(): String {
        return "VideoCategoryModal(stream_workout_description='$stream_workout_description', stream_workout_id='$stream_workout_id', stream_workout_image='$stream_workout_image', stream_workout_image_url='$stream_workout_image_url', stream_workout_name='$stream_workout_name', download_list=$download_list)"
    }
}