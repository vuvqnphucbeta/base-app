package com.beta.soft.utils.log_event

import androidx.annotation.Keep

enum class SplashEvent(val value: String) {
    show_splash_screen("show_splash_screen")
}
enum class LanguageEvent(val value: String) {
    show_language_screen_first_open("show_language_screen_first_open"),
    show_language_screen("show_language_screen")
}
enum class OnboardEvent(val value: String) {
    show_onboard_screen_1("show_onboard_screen_1"),
    show_onboard_screen_2("show_onboard_screen_2"),
    show_onboard_screen_3("show_onboard_screen_3")
}
enum class MainEvent(val value: String) {
    click_btn_quick_transfer("click_btn_quick_transfer"),
    click_btn_phone_clone("click_btn_phone_clone"),
    click_btn_my_file("click_btn_my_file"),
    click_btn_iap("click_btn_iap"),
    click_btn_setting("click_btn_setting")
}

enum class HomeEvent(val value: String) {
    show_home_screen("show_home_screen"),
    click_btn_photo_transfer("click_btn_photo_transfer"),
    click_btn_audio_transfer("click_btn_audio_transfer"),
    click_btn_video_transfer("click_btn_video_transfer"),
    click_btn_file_transfer("click_btn_file_transfer")
}
enum class PhotoTransferEvent(val value: String) {
    show_photo_transfer_screen("show_photo_transfer_screen"),
    show_tab_send_photo_transfer("show_tab_send_photo_transfer"),
    show_tab_receive_photo_transfer("show_tab_receive_photo_transfer"),
    click_btn_upload_photo("click_btn_upload_photo"),
    click_btn_continue_photo("click_btn_continue_photo"),
    click_btn_qr_code_photo_send("click_btn_qr_code_photo_send"),
    click_btn_qr_code_photo_receive("click_btn_qr_code_photo_receive"),
    create_qr_code_photo("create_qr_code_photo"),
    reject_photo_qr_code("reject_photo_qr_code"),
    click_btn_nearby_devices_photo_send("click_btn_nearby_devices_photo_send"),
    click_btn_nearby_devices_photo_receive("click_btn_nearby_devices_photo_receive"),
    show_searching_photo("show_searching_photo"),
    show_no_device_photo("show_no_device_photo"),
    show_device_found_photo("show_device_found_photo"),
    show_connected_photo("show_connected_photo"),
    show_sending_photo("show_sending_photo"),
    show_connected_reject_photo("show_connected_reject_photo"),
    show_refuse_to_send_photo("show_refuse_to_send_photo"),
    try_again_reject_photo("try_again_reject_photo"),
    try_again_reject_connect_photo("try_again_reject_connect_photo"),
    click_camera_scan_qr_photo("click_camera_scan_qr_photo"),
    click_accept_receive_photo("click_accept_receive_photo"),
    click_deny_receive_photo("click_deny_receive_photo"),
    show_waiting_for_device_request_photo("show_waiting_for_device_request_photo"),
    show_device_connect_request_photo("show_device_connect_request_photo"),
    click_accept_connect_device_photo("click_accept_connect_device_photo"),
    click_deny_connect_device_photo("click_deny_connect_device_photo"),
    show_receiving_photo("show_receiving_photo"),
    show_ui_connect_succ_photo_send("show_ui_connect_succ_photo_send"),
    show_ui_connect_succ_photo_device("show_ui_connect_succ_photo_device"),
    click_start_transferring_photo("click_start_transferring_photo"),
    receiving_success_photo("receiving_success_photo"),
    click_continue_receiving_photo_ss("click_continue_receiving_photo_ss")
}
enum class AudioTransferEvent(val value: String) {
    show_audio_transfer_screen("show_audio_transfer_screen"),
    show_tab_send_audio_transfer("show_tab_send_audio_transfer"),
    show_tab_receive_audio_transfer("show_tab_receive_audio_transfer"),
    click_btn_upload_audio("click_btn_upload_audio"),
    click_btn_continue_audio("click_btn_continue_audio"),
    click_btn_qr_code_audio_send("click_btn_qr_code_audio_send"),
    click_btn_qr_code_audio_receive("click_btn_qr_code_audio_receive"),
    create_qr_code_audio("create_qr_code_audio"),
    reject_audio_qr_code("reject_audio_qr_code"),
    click_btn_nearby_devices_audio_send("click_btn_nearby_devices_audio_send"),
    click_btn_nearby_devices_audio_receive("click_btn_nearby_devices_audio_receive"),
    show_searching_audio("show_searching_audio"),
    show_no_device_audio("show_no_device_audio"),
    show_device_found_audio("show_device_found_audio"),
    show_connected_audio("show_connected_audio"),
    show_sending_audio("show_sending_audio"),
    show_connected_reject_audio("show_connected_reject_audio"),
    show_refuse_to_send_audio("show_refuse_to_send_audio"),
    try_again_reject_audio("try_again_reject_audio"),
    try_again_reject_connect_audio("try_again_reject_connect_audio"),
    click_camera_scan_qr_audio("click_camera_scan_qr_audio"),
    click_accept_receive_audio("click_accept_receive_audio"),
    click_deny_receive_audio("click_deny_receive_audio"),
    show_waiting_for_device_request_audio("show_waiting_for_device_request_audio"),
    show_device_connect_request_audio("show_device_connect_request_audio"),
    click_accept_connect_device_audio("click_accept_connect_device_audio"),
    click_deny_connect_device_audio("click_deny_connect_device_audio"),
    show_receiving_audio("show_receiving_audio"),
    show_ui_connect_succ_audio_send("show_ui_connect_succ_audio_send"),
    show_ui_connect_succ_audio_device("show_ui_connect_succ_audio_device"),
    click_start_transferring_audio("click_start_transferring_audio"),
    receiving_success_audio("receiving_success_audio"),
    click_continue_receiving_audio_ss("click_continue_receiving_audio_ss")
}
enum class VideoTransferEvent(val value: String) {
    show_video_transfer_screen("show_video_transfer_screen"),
    show_tab_send_video_transfer("show_tab_send_video_transfer"),
    show_tab_receive_video_transfer("show_tab_receive_video_transfer"),
    click_btn_upload_video("click_btn_upload_video"),
    click_btn_continue_video("click_btn_continue_video"),
    click_btn_qr_code_video_send("click_btn_qr_code_video_send"),
    click_btn_qr_code_video_receive("click_btn_qr_code_video_receive"),
    create_qr_code_video("create_qr_code_video"),
    reject_video_qr_code("reject_video_qr_code"),
    click_btn_nearby_devices_video_send("click_btn_nearby_devices_video_send"),
    click_btn_nearby_devices_video_receive("click_btn_nearby_devices_video_receive"),
    show_searching_video("show_searching_video"),
    show_no_device_video("show_no_device_video"),
    show_device_found_video("show_device_found_video"),
    show_connected_video("show_connected_video"),
    show_sending_video("show_sending_video"),
    show_connected_reject_video("show_connected_reject_video"),
    show_refuse_to_send_video("show_refuse_to_send_video"),
    try_again_reject_video("try_again_reject_video"),
    try_again_reject_connect_video("try_again_reject_connect_video"),
    click_camera_scan_qr_video("click_camera_scan_qr_video"),
    click_accept_receive_video("click_accept_receive_video"),
    click_deny_receive_video("click_deny_receive_video"),
    show_waiting_for_device_request_video("show_waiting_for_device_request_video"),
    show_device_connect_request_video("show_device_connect_request_video"),
    click_accept_connect_device_video("click_accept_connect_device_video"),
    click_deny_connect_device_video("click_deny_connect_device_video"),
    show_receiving_video("show_receiving_video"),
    show_ui_connect_succ_video_send("show_ui_connect_succ_video_send"),
    show_ui_connect_succ_video_device("show_ui_connect_succ_video_device"),
    click_start_transferring_video("click_start_transferring_video"),
    receiving_success_video("receiving_success_video"),
    click_continue_receiving_video_ss("click_continue_receiving_video_ss")
}
enum class FilesTransferEvent(val value: String) {
    show_file_transfer_screen("show_file_transfer_screen"),
    show_tab_send_file_transfer("show_tab_send_file_transfer"),
    show_tab_receive_file_transfer("show_tab_receive_file_transfer"),
    click_btn_upload_file("click_btn_upload_file"),
    click_btn_continue_file("click_btn_continue_file"),
    click_btn_qr_code_file_send("click_btn_qr_code_file_send"),
    click_btn_qr_code_file_receive("click_btn_qr_code_file_receive"),
    create_qr_code_file("create_qr_code_file"),
    reject_file_qr_code("reject_file_qr_code"),
    click_btn_nearby_devices_file_send("click_btn_nearby_devices_file_send"),
    click_btn_nearby_devices_file_receive("click_btn_nearby_devices_file_receive"),
    show_searching_file("show_searching_file"),
    show_no_device_file("show_no_device_file"),
    show_device_found_file("show_device_found_file"),
    show_connected_file("show_connected_file"),
    show_sending_file("show_sending_file"),
    show_connected_reject_file("show_connected_reject_file"),
    show_refuse_to_send_file("show_refuse_to_send_file"),
    try_again_reject_file("try_again_reject_file"),
    try_again_reject_connect_file("try_again_reject_connect_file"),
    click_camera_scan_qr_file("click_camera_scan_qr_file"),
    click_accept_receive_file("click_accept_receive_file"),
    click_deny_receive_file("click_deny_receive_file"),
    show_waiting_for_device_request_file("show_waiting_for_device_request_file"),
    show_device_connect_request_file("show_device_connect_request_file"),
    click_accept_connect_device_file("click_accept_connect_device_file"),
    click_deny_connect_device_file("click_deny_connect_device_file"),
    show_receiving_file("show_receiving_file"),
    show_ui_connect_succ_file_send("show_ui_connect_succ_file_send"),
    show_ui_connect_succ_file_device("show_ui_connect_succ_file_device"),
    click_start_transferring_file("click_start_transferring_file"),
    receiving_success_file("receiving_success_file"),
    click_continue_receiving_file_ss("click_continue_receiving_file_ss")
}
enum class PhoneCloneEvent(val value: String) {
    show_phone_clone_screen("show_phone_clone_screen"),
    click_btn_old_phone("click_btn_old_phone"),
    show_screen_old_phone("show_screen_old_phone"),
    click_continue_old_phone("click_continue_old_phone"),
    create_qr_code_old_phone("create_qr_code_old_phone"),
    click_btn_new_phone("click_btn_new_phone"),
    show_screen_new_phone("show_screen_new_phone"),
    click_btn_camera_scan_qr("click_btn_camera_scan_qr"),
    data_clone_on_new_phone("data_clone_on_new_phone"),
    click_btn_start_clone("click_btn_start_clone"),
    show_cloning_process("show_cloning_process"),
    show_clone_success("show_clone_success"),
    click_continue_clone_success("click_continue_clone_success")
}
enum class MyFileEvent(val value: String) {
    show_my_file_screen("show_my_file_screen")
}

enum class SettingEvent(val value: String) {
    show_setting_screen("show_setting_screen"),
    click_language_setting("click_language_setting"),
    click_privacy_policy("click_privacy_policy"),
    click_term_of_use("click_term_of_use"),
    click_rate_app("click_rate_app"),
    click_contact_us("click_contact_us"),
    click_share_app("click_share_app")
}

@Keep
enum class ExitAppEvent(val eventName: String) {
    exit_app("exit_app")
}