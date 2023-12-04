package com.doviesfitness.temp

import com.doviesfitness.temp.modal.DownloadedVideoModal
import com.doviesfitness.ui.bottom_tabbar.stream_tab.model.DownloadVideoModel

/**Created by Yashank Rathore on 25,December,2020 yashank.mindiii@gmail.com **/

interface TempInter {
    fun startDownload(list: ArrayList<DownloadedVideoModal>?)
}