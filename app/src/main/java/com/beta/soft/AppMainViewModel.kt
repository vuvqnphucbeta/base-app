package com.beta.soft

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beta.soft.utils.config.LocalConfigManager
import com.beta.soft.utils.log_event.AudioTransferEvent
import com.beta.soft.utils.log_event.FilesTransferEvent
import com.beta.soft.utils.log_event.HomeEvent
import com.beta.soft.utils.log_event.LanguageEvent
import com.beta.soft.utils.log_event.LogEventUtils
import com.beta.soft.utils.log_event.MainEvent
import com.beta.soft.utils.log_event.MyFileEvent
import com.beta.soft.utils.log_event.OnboardEvent
import com.beta.soft.utils.log_event.PhoneCloneEvent
import com.beta.soft.utils.log_event.PhotoTransferEvent
import com.beta.soft.utils.log_event.SettingEvent
import com.beta.soft.utils.log_event.SplashEvent
import com.beta.soft.utils.log_event.VideoTransferEvent
import com.beta.soft.utils.nav.AppNav
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppMainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val appNav: AppNav
) : ViewModel() {
    /** check network*/
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /** check update*/
    var isShowDialogUpdate by mutableStateOf(false)

    fun checkForceUpdate() {
        viewModelScope.launch {
            val config = LocalConfigManager.getForceUpdateApp()
            val currentVersion = BuildConfig.VERSION_NAME
            isShowDialogUpdate = config.shouldShowDialog(currentVersion)
        }
    }

    /***/
    init {
        checkForceUpdate()
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isConnected.value = true
            }

            override fun onLost(network: Network) {
                _isConnected.value = false
            }
        })
    }

    fun logEventShowSplashScreen() {
        LogEventUtils.trackSplashEvent(SplashEvent.show_splash_screen)
    }

    fun logEventLanguageFirst() {
        LogEventUtils.trackLanguageEvent(LanguageEvent.show_language_screen_first_open)
    }

    fun logEventLanguage() {
        LogEventUtils.trackLanguageEvent(LanguageEvent.show_language_screen)
    }

    fun logOnboard1() {
        LogEventUtils.trackOnboardEvent(OnboardEvent.show_onboard_screen_1)
    }

    fun logOnboard2() {
        LogEventUtils.trackOnboardEvent(OnboardEvent.show_onboard_screen_2)
    }

    fun logOnboard3() {
        LogEventUtils.trackOnboardEvent(OnboardEvent.show_onboard_screen_3)
    }

    fun logEventClickHome() {
        LogEventUtils.trackMainEvent(MainEvent.click_btn_quick_transfer)
    }

    fun logEventClickPhoneClone() {
        LogEventUtils.trackMainEvent(MainEvent.click_btn_phone_clone)
    }

    fun logEventClickMyFile() {
        LogEventUtils.trackMainEvent(MainEvent.click_btn_my_file)
    }

    fun logEventIAP(isFrom: String) {
        val bundle = Bundle().apply {
            putString("isFrom", isFrom.trim())
        }
        LogEventUtils.trackMainEvent(MainEvent.click_btn_iap, bundle)
    }

    fun logEventSetting(isFrom: String) {
        val bundle = Bundle().apply {
            putString("isFrom", isFrom.trim())
        }
        LogEventUtils.trackMainEvent(MainEvent.click_btn_setting, bundle)
    }

    fun logEventShowHome() {
        LogEventUtils.trackHomeEvent(HomeEvent.show_home_screen)
    }

    fun logEventHomePhoto() {
        LogEventUtils.trackHomeEvent(HomeEvent.click_btn_photo_transfer)
    }

    fun logEventHomeAudio() {
        LogEventUtils.trackHomeEvent(HomeEvent.click_btn_audio_transfer)
    }

    fun logEventHomeVideo() {
        LogEventUtils.trackHomeEvent(HomeEvent.click_btn_video_transfer)
    }

    fun logEventHomeFile() {
        LogEventUtils.trackHomeEvent(HomeEvent.click_btn_file_transfer)
    }

    fun logEventShowPhoneClone() {
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.show_phone_clone_screen)
    }

    fun logEventPhoneOld() {
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.click_btn_old_phone)
    }

    fun logEventPhoneNew() {
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.click_btn_new_phone)
    }

    fun logEventShowMyFile() {
        LogEventUtils.trackMyFileEvent(MyFileEvent.show_my_file_screen)
    }

    fun logEventShowSetting() {
        LogEventUtils.trackSettingEvent(SettingEvent.show_setting_screen)
    }

    fun logEventShowPhotoTransfer() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_photo_transfer_screen)
    }

    fun logEventShowAudioTransfer() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_audio_transfer_screen)
    }

    fun logEventShowVideoTransfer() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_video_transfer_screen)
    }

    fun logEventShowFileTransfer() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_file_transfer_screen)
    }

    /** photo */
    fun logEventShowTabSendPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_tab_send_photo_transfer)
    }

    fun logEventShowTabReceivePhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_tab_receive_photo_transfer)
    }

    fun logEventClickUploadPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_btn_upload_photo)
    }

    fun logEventClickQRPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_btn_qr_code_photo_send)
    }

    fun logEventClickQRPhotoRecevice() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_btn_qr_code_photo_receive)
    }

    fun logEventCreateQRPhoto(state: String) {
        val bundle = Bundle().apply {
            putString("state", state.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.create_qr_code_photo, bundle)
    }

    fun logEventCheckRejectQRPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.reject_photo_qr_code)
    }

    fun logEventClickDevicePhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_btn_nearby_devices_photo_send)
    }

    fun logEventClickDevicePhotoRecive() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_btn_nearby_devices_photo_receive)
    }

    fun logEventClickContinuePhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_btn_continue_photo)
    }

    fun logEventSearchPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_searching_photo)
    }

    fun logEventNoDevicePhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_no_device_photo)
    }

    fun logEventDeviceFoundPhoto(listDevice: String) {
        val bundle = Bundle().apply {
            putString("listDevice", listDevice.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_device_found_photo, bundle)
    }

    fun logEventConnectedPhoto(nameDevice: String) {
        val bundle = Bundle().apply {
            putString("nameDevice", nameDevice.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_connected_photo, bundle)
    }

    fun logEventSendingPhoto(sizeFile: String, timeSend: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_sending_photo, bundle)
    }

    fun logEventConnectedRejectPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_connected_reject_photo)
    }

    fun logEventRefuseToSendPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_refuse_to_send_photo)
    }

    fun logEventClickTryAgainRejectPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.try_again_reject_photo)
    }

    fun logEventRejectConnectPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.try_again_reject_connect_photo)
    }

    fun logEventClickCameraScanQRPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_camera_scan_qr_photo)
    }

    fun logEventRecivingPhoto(sizeFile: String, timeSend: String, type: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
            putString("type", type.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_receiving_photo, bundle)
    }

    fun logEventClickAcceptReceivePhoto(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_accept_receive_photo, bundle)
    }

    fun logEventClickDenyReceivePhoto(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_deny_receive_photo, bundle)
    }

    fun logEventShowWaitingForDeviceRequestPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_waiting_for_device_request_photo)
    }

    fun logEventShowDeviceConnectionRequestPhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.show_device_connect_request_photo)
    }

    fun logEventClickAcceptConnectDevicePhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_accept_connect_device_photo)
    }

    fun logEventClickDenyConnectDevicePhoto() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_deny_connect_device_photo)
    }

    fun logEventShowUiConnectionSSPTSend(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(
            PhotoTransferEvent.show_ui_connect_succ_photo_send,
            bundle
        )
    }

    fun logEventShowUiConnectionSSPTDevice(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(
            PhotoTransferEvent.show_ui_connect_succ_photo_device,
            bundle
        )
    }

    fun logEventRecevingSuccessPhoto(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.receiving_success_photo, bundle)
    }

    fun logEventClickContinuePhotoSS(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackPhotoTransferEvent(
            PhotoTransferEvent.click_continue_receiving_photo_ss,
            bundle
        )
    }

    fun logEventClickStartTransferringPTSend() {
        LogEventUtils.trackPhotoTransferEvent(PhotoTransferEvent.click_start_transferring_photo)
    }

    /** audio */
    fun logEventShowTabSendAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_tab_send_audio_transfer)
    }

    fun logEventShowTabReceiveAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_tab_receive_audio_transfer)
    }

    fun logEventClickUploadAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_btn_upload_audio)
    }

    fun logEventClickQRAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_btn_qr_code_audio_send)
    }

    fun logEventClickQRAudioRecive() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_btn_qr_code_audio_receive)
    }

    fun logEventCreateQRAudio(state: String) {
        val bundle = Bundle().apply {
            putString("state", state.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.create_qr_code_audio, bundle)
    }

    fun logEventCheckRejectQRAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.reject_audio_qr_code)
    }

    fun logEventClickDeviceAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_btn_nearby_devices_audio_send)
    }

    fun logEventClickDeviceAudioRecive() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_btn_nearby_devices_audio_receive)
    }

    fun logEventClickContinueAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_btn_continue_audio)
    }

    fun logEventSearchAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_searching_audio)
    }

    fun logEventNoDeviceAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_no_device_audio)
    }

    fun logEventDeviceFoundAudio(listDevice: String) {
        val bundle = Bundle().apply {
            putString("listDevice", listDevice.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_device_found_audio, bundle)
    }

    fun logEventConnectedAudio(nameDevice: String) {
        val bundle = Bundle().apply {
            putString("nameDevice", nameDevice.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_connected_audio, bundle)
    }

    fun logEventSendingAudio(sizeFile: String, timeSend: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_sending_audio, bundle)
    }

    fun logEventConnectedRejectAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_connected_reject_audio)
    }

    fun logEventRefuseToSendAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_refuse_to_send_audio)
    }

    fun logEventClickTryAgainRejectAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.try_again_reject_audio)
    }

    fun logEventRejectConnectAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.try_again_reject_connect_audio)
    }

    fun logEventClickCameraScanQRAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_camera_scan_qr_audio)
    }

    fun logEventRecivingAudio(sizeFile: String, timeSend: String, type: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
            putString("type", type.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_receiving_audio, bundle)
    }

    fun logEventClickAcceptReceiveAudio(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_accept_receive_audio, bundle)
    }

    fun logEventClickDenyReceiveAudio(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_deny_receive_audio, bundle)
    }

    fun logEventShowWaitingForDeviceRequestAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_waiting_for_device_request_audio)
    }

    fun logEventShowDeviceConnectionRequestAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.show_device_connect_request_audio)
    }

    fun logEventClickAcceptConnectDeviceAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_accept_connect_device_audio)
    }

    fun logEventClickDenyConnectDeviceAudio() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_deny_connect_device_audio)
    }

    fun logEventShowUiConnectionSSAuSend(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackAudioTransferEvent(
            AudioTransferEvent.show_ui_connect_succ_audio_send,
            bundle
        )
    }

    fun logEventShowUiConnectionSSAuDevice(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackAudioTransferEvent(
            AudioTransferEvent.show_ui_connect_succ_audio_device,
            bundle
        )
    }

    fun logEventRecevingSuccessAudio(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.receiving_success_audio, bundle)
    }

    fun logEventClickContinueAudioSS(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackAudioTransferEvent(
            AudioTransferEvent.click_continue_receiving_audio_ss,
            bundle
        )
    }

    fun logEventClickStartTransferringAuSend() {
        LogEventUtils.trackAudioTransferEvent(AudioTransferEvent.click_start_transferring_audio)
    }

    /** video */
    fun logEventShowTabSendVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_tab_send_video_transfer)
    }

    fun logEventShowTabReceiveVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_tab_receive_video_transfer)
    }

    fun logEventClickUploadVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_btn_upload_video)
    }

    fun logEventClickQRVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_btn_qr_code_video_send)
    }

    fun logEventClickQRVideoRecive() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_btn_qr_code_video_receive)
    }

    fun logEventCreateQRVideo(state: String) {
        val bundle = Bundle().apply {
            putString("state", state.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.create_qr_code_video, bundle)
    }

    fun logEventCheckRejectQRvideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.reject_video_qr_code)
    }

    fun logEventClickDeviceVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_btn_nearby_devices_video_send)
    }

    fun logEventClickDeviceVideoRecive() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_btn_nearby_devices_video_receive)
    }

    fun logEventClickContinueVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_btn_continue_video)
    }

    fun logEventSearchVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_searching_video)
    }

    fun logEventNoDeviceVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_no_device_video)
    }

    fun logEventDeviceFoundVideo(listDevice: String) {
        val bundle = Bundle().apply {
            putString("listDevice", listDevice.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_device_found_video, bundle)
    }

    fun logEventConnectedVideo(nameDevice: String) {
        val bundle = Bundle().apply {
            putString("nameDevice", nameDevice.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_connected_video, bundle)
    }

    fun logEventSendingVideo(sizeFile: String, timeSend: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_sending_video, bundle)
    }

    fun logEventConnectedRejectVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_connected_reject_video)
    }

    fun logEventRefuseToSendVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_refuse_to_send_video)
    }

    fun logEventClickTryAgainRejectVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.try_again_reject_video)
    }

    fun logEventRejectConnectVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.try_again_reject_connect_video)
    }

    fun logEventClickCameraScanQRVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_camera_scan_qr_video)
    }

    fun logEventRecivingVideo(sizeFile: String, timeSend: String, type: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
            putString("type", type.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_receiving_video, bundle)
    }

    fun logEventClickAcceptReceiveVideo(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_accept_receive_video, bundle)
    }

    fun logEventClickDenyReceiveVideo(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_deny_receive_video, bundle)
    }

    fun logEventShowWaitingForDeviceRequestVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_waiting_for_device_request_video)
    }

    fun logEventShowDeviceConnectionRequestVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.show_device_connect_request_video)
    }

    fun logEventClickAcceptConnectDeviceVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_accept_connect_device_video)
    }

    fun logEventClickDenyConnectDeviceVideo() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_deny_connect_device_video)
    }

    fun logEventShowUiConnectionSSvidSend(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackVideoTransferEvent(
            VideoTransferEvent.show_ui_connect_succ_video_send,
            bundle
        )
    }

    fun logEventShowUiConnectionSSvidDevice(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackVideoTransferEvent(
            VideoTransferEvent.show_ui_connect_succ_video_device,
            bundle
        )
    }

    fun logEventRecevingSuccessVideo(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.receiving_success_video, bundle)
    }

    fun logEventClickContinueVideoSS(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackVideoTransferEvent(
            VideoTransferEvent.click_continue_receiving_video_ss,
            bundle
        )
    }

    fun logEventClickStartTransferringvidSend() {
        LogEventUtils.trackVideoTransferEvent(VideoTransferEvent.click_start_transferring_video)
    }

    /** file */
    fun logEventShowTabSendFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_tab_send_file_transfer)
    }

    fun logEventShowTabReceiveFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_tab_receive_file_transfer)
    }

    fun logEventClickUploadFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_btn_upload_file)
    }

    fun logEventClickQRFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_btn_qr_code_file_send)
    }

    fun logEventClickQRFileRecive() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_btn_qr_code_file_receive)
    }

    fun logEventCreateQRFile(state: String) {
        val bundle = Bundle().apply {
            putString("state", state.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.create_qr_code_file, bundle)
    }

    fun logEventCheckRejectQRFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.reject_file_qr_code)
    }

    fun logEventClickDeviceFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_btn_nearby_devices_file_send)
    }

    fun logEventClickDeviceFileRecive() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_btn_nearby_devices_file_receive)
    }

    fun logEventClickContinueFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_btn_continue_file)
    }

    fun logEventSearchFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_searching_file)
    }

    fun logEventNoDeviceFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_no_device_file)
    }

    fun logEventDeviceFoundFile(listDevice: String) {
        val bundle = Bundle().apply {
            putString("listDevice", listDevice.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_device_found_file, bundle)
    }

    fun logEventConnectedFile(nameDevice: String) {
        val bundle = Bundle().apply {
            putString("nameDevice", nameDevice.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_connected_file, bundle)
    }

    fun logEventSendingFile(sizeFile: String, timeSend: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_sending_file, bundle)
    }

    fun logEventConnectedRejectFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_connected_reject_file)
    }

    fun logEventRefuseToSendFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_refuse_to_send_file)
    }

    fun logEventClickTryAgainRejectFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.try_again_reject_file)
    }

    fun logEventRejectConnectFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.try_again_reject_connect_file)
    }

    fun logEventClickCameraScanQRFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_camera_scan_qr_file)
    }

    fun logEventRecivingFile(sizeFile: String, timeSend: String, type: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
            putString("type", type.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_receiving_file, bundle)
    }

    fun logEventClickAcceptReceiveFile(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_accept_receive_file, bundle)
    }

    fun logEventClickDenyReceiveFile(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_deny_receive_file, bundle)
    }

    fun logEventShowWaitingForDeviceRequestFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_waiting_for_device_request_file)
    }

    fun logEventShowDeviceConnectionRequestFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.show_device_connect_request_file)
    }

    fun logEventClickAcceptConnectDeviceFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_accept_connect_device_file)
    }

    fun logEventClickDenyConnectDeviceFile() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_deny_connect_device_file)
    }

    fun logEventShowUiConnectionSSFileSend(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackFilesTransferEvent(
            FilesTransferEvent.show_ui_connect_succ_file_send,
            bundle
        )
    }

    fun logEventShowUiConnectionSSFileDevice(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackFilesTransferEvent(
            FilesTransferEvent.show_ui_connect_succ_file_device,
            bundle
        )
    }

    fun logEventRecevingSuccessFile(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.receiving_success_file, bundle)
    }

    fun logEventClickContinueFileSS(type: String) {
        val bundle = Bundle().apply {
            putString("type", type.trim())
        }
        LogEventUtils.trackFilesTransferEvent(
            FilesTransferEvent.click_continue_receiving_file_ss,
            bundle
        )
    }

    fun logEventClickStartTransferringFileSend() {
        LogEventUtils.trackFilesTransferEvent(FilesTransferEvent.click_start_transferring_file)
    }

    fun showScreenOldPhone(showList: List<String>) {
        val bundle = Bundle().apply {
            putString("showList", showList.joinToString(separator = ","))
        }
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.show_screen_old_phone, bundle)
    }

    fun clickContinueOldPhone(select: List<String>) {
        val bundle = Bundle().apply {
            putString("select", select.joinToString(separator = ","))
        }
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.click_continue_old_phone, bundle)
    }

    fun logEventCreateQROldPhone(state: String) {
        val bundle = Bundle().apply {
            putString("state", state.trim())
        }
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.create_qr_code_old_phone, bundle)
    }

    fun showScreenNewPhone() {
        val showBtn = "btn_camera_scan_qr"
        val bundle = Bundle().apply {
            putString("showBtn", showBtn.trim())
        }
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.show_screen_new_phone, bundle)
    }

    fun clickCameraClone() {
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.click_btn_camera_scan_qr)
    }

    fun dataCloneOnNewPhone(sizeFile: String, timeSend: String, type: String) {
        val bundle = Bundle().apply {
            putString("sizeFile", sizeFile.trim())
            putString("timeSend", timeSend.trim())
            putString("type", type.trim())
        }
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.data_clone_on_new_phone, bundle)
    }

    fun clickStartClone() {
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.click_btn_start_clone)
    }

    fun showCloning(time: String, state: String) {
        val bundle = Bundle().apply {
            putString("time", time.trim())
            putString("state", state.trim())
        }
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.show_cloning_process, bundle)
    }

    fun showCloneSuccess() {
        val showBtn = "btn_continue_clone_success"
        val bundle = Bundle().apply {
            putString("showBtn", showBtn.trim())
        }
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.show_clone_success, bundle)
    }

    fun clickContinueCloneSuccess() {
        LogEventUtils.trackPhoneCloneEvent(PhoneCloneEvent.click_continue_clone_success)
    }

    fun clickLanguageSetting() {
        LogEventUtils.trackSettingEvent(SettingEvent.click_language_setting)
    }

    fun clickPrivacyPolicy() {
        LogEventUtils.trackSettingEvent(SettingEvent.click_privacy_policy)
    }

    fun clickTermOfUse() {
        LogEventUtils.trackSettingEvent(SettingEvent.click_term_of_use)
    }

    fun clickRateApp() {
        LogEventUtils.trackSettingEvent(SettingEvent.click_rate_app)
    }

    fun clickContactUs() {
        LogEventUtils.trackSettingEvent(SettingEvent.click_contact_us)
    }

    fun clickShareApp() {
        LogEventUtils.trackSettingEvent(SettingEvent.click_share_app)
    }
}