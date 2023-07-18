package com.frami.ui.common.videopicker

import android.os.Environment

class Constants {
    companion object {

        const val appFolderName = "Frami"
        const val recordedFiles = "Audio_Files"
        const val imagesFolder = "Images"
        const val faviconFolder = "Favicons"
        const val meaningImagesFolder = "MeaningImages"
        const val canvasFolder = "Drawings"
        const val SavedFolder = "Saved Video"
        const val DownloadedFolder = "Downloaded Video"
        const val quotesFolder = "Quotes"
        const val documentsFolder = "Documents"
        const val magDocOnlineFolder = "MagDoc Online Books"
        const val offlinePages = "OfflinePages"
        const val userProfilePhotoName = "user_image.png"

        // sort video
        const val user_short_video_username = "pref_user_short_video_username"
        const val user_short_video_profile_key = "pref_user_short_video_profile"
        const val user_short_last_updated_username = "pref_user_short_last_updated_username"

        const val PostTypePublic = "Public"
        const val PostTypePrivate = "Private"
        const val PostTypeFriends = "Friends"
        const val sortTrimVideo = "TrimVideo"
        const val sortCompressVideo = "CompressVideo"
        const val exoPlayerCacheVideo = "ExoPlayerCacheVideo"
        const val sortVideoPosterImage = "VideoPoster"
        const val sortVideoAudioFile = "AudioFile"
        const val pdfBookPosterImage = "PDFBookPoster"
        const val sortVideoShared = "ShareVideo"
        const val sortVideoAudioMergeFile = "AudioVideoMergeFile"
//        const val sortVideoAddTextVideo = "AddTextVideo"
//        const val sortVideoGIFVideo = "GIFVideo"
//        const val sortVideoMergeVideo = "MergeVideo"
        const val SampleAssert = "SampleAssert"
        const val FileSize: Long = 512000000 // 512 MB
        const val VideoFileDuration: Long = 5000 // 5 sec
        const val sortVideoLikeTypeComment: String = "Comment"
        const val sortVideoLikeTypeVideo: String = "Video"
        const val sortVideoShareTypeVideo: String = "Shared"
        const val sortVideoUserFollowingType: String = "Following"
        const val sortVideoData: String = "key_short_video_data"
        const val sortVideoActivityCountLimit: Int = 3
        const val sortVideoFollowingCountLimit: Int = 3
        const val HASHTAG: String = "HASHTAG"
        const val AUDIODATA: String = "AUDIODATA"

        val USER_PROFILE_PATH =
            Environment.getExternalStorageDirectory().toString() + "/" + appFolderName + "/" + userProfilePhotoName
        val OFFLINE_WEBPAGES_FOLDER =
            Environment.getExternalStorageDirectory().toString() + "/" + appFolderName + "/" + offlinePages + "/"

        // Shared Preference Constants
        const val IS_SIGN_UP_COMPLETE = "sp_is_sign_up_complete"


        // Browser Suggestion View Types
        const val SUGGESTION_HISTORY = 0
        const val SUGGESTION_WEB = 1
        const val SUGGESTION_CLIPBOARD = 2
        const val SUGGESTION_CURRENT_URL = 3
        const val SUGGESTION_YOUTUBE_SEARCH = 4
        const val SUGGESTION_SITE_SUGGEST = 5
        const val FETCH_NAMESPACE = "MagtappDownloads"
        const val FETCH_GROUP_ID = 45654646
        const val BROWSER_STATE_KEY = "WEBVIEW_CHROMIUM_STATE"

        // Permission Check Request Codes
        const val FILE_WRITE_PERMISSION = 3452
        const val LOCATION_PERMISSION = 3453

        // Sing Up
        const val GOOGLE_SIGN_UP = 2
        const val PHONE_SIGN_UP = 3

        // New Updates Circle Indicator.
        const val WORD_HISTORY_UPDATE = "pref_word_history_update_indicator"
        const val QUICK_HELP_UPDATE = "pref_word_history_update_indicator"
        const val READING_MODE_UPDATE = "pref_word_history_update_indicator"

        const val DEFAULT_USER_LANG = "user_default_language_mtapp"
        const val DEFAULT_USER_LANG_CODE = "user_default_language_code_mtapp"
        const val ENGLISH_COMING_SOON = "Coming soon"
        const val LANG_HINDI = "hindi"
        const val HINDI_COMING_SOON = "जल्द आ रहा है"
        const val LANG_KANNADA = "kannada"
        const val KANNADA_COMING_SOON = "ಶೀಘ್ರದಲ್ಲೇ ಬರಲಿದೆ"
        const val LANG_BENGALI = "bengali"
        const val BENGALI_COMING_SOON = "শীঘ্রই আসছে"
        const val LANG_GUJARATI = "gujarati"
        const val GUJARATI_COMING_SOON = "ટૂક સમયમાં આવી રહ્યું છે"
        const val LANG_MARATHI = "marathi"
        const val MARATHI_COMING_SOON = "लवकरच येत आहे"
        const val LANG_NEPALI = "nepali"
        const val NEPALI_COMING_SOON = "आउदैछ"
        const val LANG_PUNJABI = "punjabi"
        const val PUNJABI_COMING_SOON = "ਆਨ ਵਾਲੀ"
        const val LANG_TAMIL = "tamil"
        const val TAMIL_COMING_SOON = "விரைவில்"
        const val LANG_MALAYALAM = "malayalam"
        const val MALAYALAM_COMING_SOON = "ഉടൻ വരുന്നു"
        const val LANG_TELUGU = "telugu"
        const val TELUGU_COMING_SOON = "త్వరలో"
        const val LANG_URDU = "urdu"
        const val URDU_COMING_SOON = "جلد آرہا ہے"
        const val ODIA_URDU = "odia"
        const val ODIA_COMING_SOON = "କମିଙ୍ଗ ସୂଂ"


        const val PREF_KEY_DEVICEID = "pref_key_deviceid"
        const val PREF_KEY_TOKEN = "pref_key_token"
        const val PREF_REFRESH_TOKEN = "pref_key_refresh_token"
        const val PREF_RAZORPAY_KEY = "pref_key_razorpay"
        const val PREF_KEY_USERCODE = "pref_key_usercode"
        const val PREF_KEY_USERTYPE = "pref_key_usertype"
        const val PREF_KEY_USERLOGGEDIN = "pref_key_userloggedin"
        const val PREF_KEY_USER_SKIP_LOGIN_FLOW = "pref_key_user_skip_login_flow"
        const val PREF_KEY_PROFILE_TUTORIAL_SHOWN = "pref_key_profile_tutorial_shown"
        const val PREF_KEY_MAGTAPP_MODE_TUTORIAL_SHOWN = "pref_key_mtapp_mode_tutorial_shown"
        const val PREF_KEY_MAGTAPP_MODE_DOC_TUTORIAL_SHOWN = "pref_key_mtapp_mode_doc_tutorial_shown"
        const val PREF_KEY_DESKTOP_MODE = "pref_key_browser_desktop_mode"
        const val PREF_KEY_APP_DARK_MODE = "pref_key_app_dark_mode"
        const val PREF_KEY_NOTES_GRID_VIEW: String = "pref_key_notes_grid_view"
        const val PREF_KEY_NOTES_CURRENT_CAT: String = "pref_key_notes_current_cat"
        const val PREF_KEY_BOOK_REPORT_PHONE: String = "pref_key_book_report_phone"
        const val PREF_KEY_BOOK_REPORT_EMAIL: String = "pref_key_book_report_email"
        const val PREF_KEY_PLAYED_GAME: String = "pref_key_played_game"
        const val KEY_PROFILE_IMAGE = "pref_key_profike_image"
        const val KEY_PROFILE_NAME = "pref_key_profike_name"
        const val KEY_PROFILE_EMAIL = "pref_key_profike_email"
        const val KEY_STANDARD_TAB_URL = "pref_key_standard_tab_url"
        const val KEY_PRIVATE_TAB_URL = "pref_key_private_tab_url"
        const val KEY_DEFAULT_SEARCH_ENGINE = "pref_key_def_search_engine"
        const val KEY_DEFAULT_SEARCH_ENGINE_NAME = "pref_key_def_search_engine_n"
        const val KEY_WEB_BROWSER_DARK_MODE = "pref_key_web_browser_dark_mode"
        const val KEY_WEB_BROWSER_BOTTOM_MEANING = "pref_key_web_browser_bottom_meaning"
        const val KEY_HOME_CONTINUE_READING_SHOW = "pref_key_home_countinue_reading_show"
        const val KEY_BOOK_QUIZ_LIST_SCROLLING_ANIMATION = "pref_key_book_quiz_list_scrolling_animation"
        const val KEY_FAKE_WORD = "pref_key_fack_word"
        const val KEY_ANIMATE_FAB = "pref_key_animate_fab"
        const val KEY_LOCAL_NEWS = "pref_key_local_news"
        const val KEY_LAST_NEWS_DATE = "pref_key_last_news_date"
        const val PREF_KEY_MUSIC_NOTICE = "pref_key_music_notice"
        const val PREF_KEY_CHAT_FIRST_TIME = "pref_key_chat_first_time"
        const val DEFAULT_USER_LANG_NEWS: String = "pref_key_default_user_news"
        const val PREF_KEY_HAPTIC_FEEDBACK_ON: String = "pref_key_haptic_feedback_on"
        const val PREF_KEY_AUTO_START_CHECKED: String = "pref_key_auto_start_checked"
        const val PREF_KEY_DB_PASS_TYPE: String = "pref_key_db_pass_type"
        const val PREF_KEY_TOKEN_ENC: String = "pref_key_token_enc"
        const val PREF_KEY_DEVICE_ID_ENC: String = "pref_key_device_id_enc"
        const val PREF_KEY_OS_ID: String = "pref_key_os_id"
        const val PREF_KEY_OS_UPDATED: String = "pref_key_os_id_updated"
        const val PREF_KEY_FIREBASE_TOKEN: String = "pref_firebase_token"
        const val PREF_KEY_FCM_TOKEN: String = "pref_key_fcm_token"
        const val PREF_KEY_FIREBASE_TOKEN_T: String = "pref_firebase_token_t"
        const val PREF_KEY_IS_PDF_OPENED: String = "pref_key_is_doc_opened"
        const val PREF_KEY_IS_MAPP_MODE_MAGDOCS: String = "magtapp_mode_in_pdf"
        const val PREF_KEY_IS_MAPP_MODE_BROWSER: String = "magtapp_mode_in_browser"
        const val PREF_KEY_TO_SHOW_MAPP_MODE_BROWSER: String = "pref_key_to_show_magtapp_mode"
        const val PREF_KEY_APPLICATION_CLOSED: String = "pref_key_application_closed"
        const val PREF_KEY_TRANSLATE_ALL_STARTED: String = "pref_key_translate_all_started"
        const val PREF_KEY_MAGDOC_CATEGORY: String = "pref_key_magdoc_category"
        const val PREF_BROWSER_LAST_TAB: String = "pref_browser_last_tab"
        const val PREF_KEY_AMAZON_TAG_URL: String = "pref_key_amazon_tag_url"
        const val PREF_PROFILE_PLAY_GAME_URL: String = "pref_profile_play_game_url"

        const val PREF_KEY_WHATSAPP_NUMBER_1: String = "pref_key_whatsapp_number_1"
        const val PREF_KEY_WHATSAPP_NUMBER_2: String = "pref_key_whatsapp_number_2"
        const val PREF_KEY_WHATSAPP_NUMBER_3: String = "pref_key_whatsapp_number_3"
        const val PREF_KEY_TAB_INVERT_BRANDS: String = "pref_key_tab_inverted_brands"
        const val PREF_KEY_TAB_INVERT_MODEL: String = "pref_key_tab_inverted_model"
        const val PREF_KEY_TAB_EXCLUDED_MODEL: String = "pref_key_tab_excluded_model"
        const val PREF_KEY_TAB_EXCLUDED_URLS: String = "pref_key_tab_excluded_urls"
        const val PREF_KEY_LAST_RECENT_COURSE: String = "pref_key_last_recent_course"
        const val PREF_KEY_LAST_RECENT_BOOK: String = "pref_key_last_recent_book"

        const val PERIODIC_DATA_SYNC_WORK = "periodic_data_sync_work"
        const val PERIODIC_DATA_SYNC_WORK_INTENT = "intent_periodic_data_sync_work"
        const val PERIODIC_DATA_RESTORE_WORK_INTENT = "intent_periodic_data_restore_work"
        const val PERIODIC_SYNC_LAST_RAN = "periodic_sync_last_ran"
        const val PERIODIC_SYNC_STATUS = "periodic_sync_status"
        const val PERIODIC_DATA_SYNC_WORK_TAG = "periodic_data_sync_work_tag"
        const val PERIODIC_DATA_UPLOAD_SHORT_VIDEO = "periodic_upload_short_video"

        const val PREF_KEY_HOME_SCREEN_WALLPAPER: String = "pref_key_home_screen_wallpaper"
        const val HOME_SCREEN_WALLPAPER_FILE_NAME: String = "wallpaper_file"
        const val IS_HOME_SCREEN_WALLPAPER_SAVED: String = "home_screen_wallpaper_saved"
        const val DEFAULT_HOME_WALLPAPER: String = "https://source.unsplash.com/random/432x768/?nature,water,sun,ocean,space,wildlife,forest"
        const val PREF_KEY_IS_CHAT_INTRO_SHOWN: String = "pref_key_is_chat_intro_shown"
        const val PREF_KEY_DEFAULT_OPEN_STORE: String = "pref_default_open_store"
        const val PREF_KEY_IS_BROWSER_INTRO_SHOWN: String = "pref_key_is_browser_intro_shown"
        const val PREF_KEY_IS_MAGDOC_INTRO_SHOWN: String = "pref_key_is_magdoc_intro_shown"
        const val PREF_LAST_UPDATE_INFO_SHOWN: String = "pref_key_last_update_info_shown"
        const val PREF_KEY_TO_SHOW_AD: String = "pref_key_to_show_ad"
        const val PREF_KEY_TO_ENABLE_AD_PREFETCHING: String = "pref_key_to_enable_ad_prefetching"
        const val PREF_KEY_TO_SHOW_INTERSTITIAL_AD_HOME: String = "pref_key_to_show_interstitial_ad_home"
        const val PREF_KEY_TO_SHOW_INTERSTITIAL_AD_PDF: String = "pref_key_to_show_interstitial_ad_pdf"
        const val PREF_KEY_TO_SHOW_BD_REWARD_AD: String = "pref_key_to_show_bd_reward_ad"
        const val PREF_KEY_TO_SHOW_BD_INTERSTITIAL_AD: String = "pref_key_to_show_bd_interstitial_ad"
        const val PREF_KEY_BD_REWARD_AD_ID: String = "pref_key_bd_reward_ad_id"
        const val PREF_KEY_BD_INTERSTITIAL_AD_ID: String = "pref_key_bd_interstitial_ad_id"
        const val PREF_KEY_IS_OWN_USER: String = "pref_key_is_own_user"
        const val PREF_KEY_MAGTAPP_NEW_URL: String = "pref_key_magtapp_new_url"
        const val PREF_KEY_MAGTAPP_YOUTUBE_VIDEO: String = "pref_key_magtapp_youtube_video"
        const val PREF_KEY_MAGTAPP_FEATURED_COURSE: String = "pref_key_magtapp_featured_course"
        const val PREF_KEY_NATIVE_AD_UNIT_ID: String = "pref_key_native_ad_unit_id"
        const val PREF_KEY_BANNER_AD_APPNEXT: String = "pref_key_banner_ad_appnext"
        const val PREF_KEY_START_APP_AD_ID: String = "pref_key_start_app_ad_id"
        const val PREF_KEY_ADMOB_INTERSTITIAL_AD: String = "pref_key_admob_interstitial_ad"
        const val PREF_KEY_SHORTS_NATIVE_AD: String = "pref_key_shorts_native_ad"
        const val PREF_KEY_SHORTS_SHARE_NATIVE_AD: String = "pref_key_shorts_share_native_ad"
        const val PREF_KEY_SHORTS_SHARE_BANNER_AD: String = "pref_key_shorts_share_banner_ad"
        const val PREF_KEY_UNIVERSAL_AD_CONFIG: String = "pref_key_universal_ad_config"
        // Ad Units based on placements
        // Magtapp global banner
        const val PREF_KEY_ADMOB_BANNER_AD: String = "pref_key_admob_banner_ad" // Global Banner
        // New Native Ad
        const val PREF_KEY_NATIVE_NEWS_AD_UNIT_ID: String = "pref_key_native_news_ad_unit_id"
        // Game native Ad
        const val PREF_KEY_GAME_NATIVE_AD_UNIT_ID: String = "pref_key_game_native_ad_unit_id"
        // Mag Doc Big ad - Between Rv and Book File Download
        const val PREF_KEY_MAGDOCB_NATIVE_AD_UNIT_ID: String = "pref_key_magdocb_native_ad_unit_id"
        // Mag Doc Small - Between offline doc rv && Book Info
        const val PREF_KEY_MAGDOCS_NATIVE_AD_UNIT_ID: String = "pref_key_magdocs_native_ad_unit_id"
        // Course Big - B/w course category and sub-cat rv
        const val PREF_KEY_COURSEB_NATIVE_AD_UNIT_ID: String = "pref_key_courseb_native_ad_unit_id"
        // Course Small - B/W course content
        const val PREF_KEY_COURSES_NATIVE_AD_UNIT_ID: String = "pref_key_courses_native_ad_unit_id"
        // Meaning Screen top
        const val PREF_KEY_MEANINGT_NATIVE_AD_UNIT_ID: String = "pref_key_meaningt_native_ad_unit_id"
        // Meaning Screen Bottom
        const val PREF_KEY_MEANINGB_NATIVE_AD_UNIT_ID: String = "pref_key_meaningb_native_ad_unit_id"
        const val PREF_KEY_REVISION_MODE_CHECK: String = "pref_key_revision_mode_check"

        // Global Native Ad Big (Video)
        const val PREF_KEY_ADMOB_GLOBAL_NATIVE_VIDEO_AD: String = "pref_key_admob_global_native_video_ad"
        // Global Native Ad Small (Image)
        const val PREF_KEY_ADMOB_GLOBAL_NATIVE_IMAGE_AD: String = "pref_key_admob_global_native_image_ad"
        // Global Banner (Banner)
        const val PREF_KEY_ADMOB_GLOBAL_BANNER_AD: String = "pref_key_admob_global_native_banner_ad"

        const val PREF_KEY_REFERRAL_ADDED: String = "pref_key_referral_added"
        const val PREF_KEY_NEWLY_JOINED_USER: String = "pref_key_newly_joined_user"
        const val PREF_KEY_SHORTS_HOME_ICON: String = "pref_key_shorts_home_icon"
        const val PREF_KEY_SHORTS_SPONSER_LINK: String = "pref_key_shorts_sponser_link"
        const val PREF_KEY_REFERRAL_KEY: String = "pref_key_referral_key"
        const val PREF_KEY_DEFAULT_NEWS_CATEGORIES: String = "pref_key_default_home_categories_v5"
        const val PREF_KEY_QUIZ_CATEGORIES_ORDER: String = "pref_key_quiz_categories_order"
        const val PREF_KEY_TRANSLATION_COUNT: String = "pref_key_translation_count"
        const val PREF_KEY_TRANSLATION_COUNTED: String = "pref_key_translation_counted"
        const val PREF_KEY_APP_INSTALL_DATE: String = "pref_key_app_install_date"
        const val CONFIG_KEY_RAZORPAY_KEY: String = "config_key_razorpay_key"
        const val PREF_KEY_SHORTS_CUSTOM_PAGINATION_CALL: String = "pref_key_shorts_custom_pagination_call"
        const val PREF_KEY_SHORTS_IMAGE_SCALE: String = "pref_key_shorts_image_scale"
        const val PREF_KEY_SHORTS_VIDEO_SCALE: String = "pref_key_shorts_video_scale"
        const val PREF_KEY_SHORTS_PROFILE_PIC_CHANGED: String = "pref_key_shorts_profile_pic_changed"
        const val PREF_KEY_SAVED_SEARCH_USER_LIST: String = "pref_key_saved_search_user_list"
        const val PREF_KEY_LAST_SPEED_TEST: String = "pref_key_last_speed_test"
        const val PREF_KEY_LAST_INTERNET_SPEED: String = "pref_key_last_internet_speed"
        const val PREF_KEY_COPY_TO_VISUAL_MEANIG: String = "pref_key_copy_to_visual_meaning"
        const val PREF_KEY_PDF_READER_SHOW_ADS_TIME: String = "pref_key_pdf_reader_show_ads_time"
        const val PREF_KEY_REMOTE_CONFIG_BOOK_CATEGORY: String = "pref_key_remote_config_book_category"
        const val PREF_KEY_IS_BATCH_LOGGIN_ENABLED: String = "pref_key_is_batch_logging_enabled"

        const val KEY_DEFAULT_TTS_LANGUAGE = "pref_key_def_tts_lang"
        const val KEY_DEFAULT_TTS_VOICE = "pref_key_def_tts_voice"
        const val KEY_DEFAULT_TTS_SPEECH = "pref_key_default_tts_speed"
        const val KEY_DEFAULT_TTS_PITCH = "pref_default_tts_pitch"
        const val PREF_KEY_READING_HIGHLIGHT = "pref_key_reading_highlight"
        const val PREF_KEY_OFFLINE_TRANSLATIONS: String = "pref_key_offline_translations"
        const val PREF_KEY_OFFLINE_DICTIONARIES: String = "pref_key_offline_dictionaries"

        // New Update Preferences
        const val IS_ONLINE_BOOKS_CLICKED = "pref_is_clicked_online_books"
        const val IS_VIDEO_COURSES_CLICKED = "pref_is_clicked_video_course"
        const val IS_MAGIC_TRANSLATE_CLICKED = "pref_is_clicked_magic_translate"
        const val IS_IDIOM_CLICKED = "pref_is_clicked_idiom"
        const val PREF_KEY_SITE_SUGGEST_URL: String = "pref_key_site_suggest_url"

        // Firebase Config Keys
        const val FIREBASE_PREF_STORE_SLIDER = "magtapp_book_store_banner"
        const val FIREBASE_PREF_DOCUMENT_SLIDER = "magtapp_document_banner"
        const val FIREBASE_PREF_COURSE_SLIDER = "magtapp_course_banner"
        const val FIREBASE_PROMO_TEXT_COURSE = "firebase_promo_text_course"
        const val FIREBASE_PROMO_TEXT_HOME = "firebase_promo_text_home"
        const val FIREBASE_MAAGTAPP_HOME_ICON = "magtapp_home_magtapp_icon"

        //short video Firebase Config Keys
        const val FIREBASE_PREF_HASH_HOME_SLIDER = "magtapp_hash_banner"
        const val FIREBASE_PREF_EARNING_SLIDER = "magtapp_earnings_banner"

        // Browser preferences
        const val PREF_LAST_TAB_HOME = "pref_last_tab_home"
        const val PREF_LAST_TAB_POSITION = "pref_last_tab_positon"
        const val PREF_LAST_TAB_STATE = "pref_last_tab_state"
        const val PREF_LAST_TAB_URL = "pref_last_tab_url"
        const val PREF_LAST_TABS = "pref_last_tabs"
        const val PREF_TOTAL_TABS = "pref_total_tabs"
        const val WORD_JS_BROWSER = "(function(){" +
                "var txt = window.getSelection().toString(); " +
                "return txt.replace(/['\"]+/g, '')" +
                "})()"
        const val PREF_BROWSER_SPEED_DIAL = "pref_browser_speed_dial"
        const val PREF_BROWSER_OTHER_SITE = "pref_browser_other_site"
        const val SPEED_DIAL_LIMIT = 12
        const val MAGTAPP_OTHER_SITE_LIMIT = 15

        /*Player*/
        const val PLAYBACK_CHANNEL_ID = "playback_channel"
        const val PLAYBACK_NOTIFICATION_ID = 1
        const val MEDIA_SESSION_TAG = "audio_demo"
        const val DOWNLOAD_CHANNEL_ID = "download_channel"
        const val DOWNLOAD_NOTIFICATION_ID = 2

        // Idioms Constants

        // const val EVENT_NO_IDIOMS_FOUND = 7770
        const val EVENT_IDIOMS_NO_INTERNET = 7771
        const val EVENT_IDIOMS_NO_MORE_LEFT = 7772
        const val EVENT_IDIOMS_SOMETHING_WENT_WRONG = 7773
        const val EVENT_IDIOMS_REPORT_FAILED = 7774
        const val EVENT_IDIOMS_REPORT_SUCCESS = 7775
        const val EVENT_IDIOMS_LOADING = 7776
        const val EVENT_IDIOMS_LOADING_COMPLETE = 7777
        const val EVENT_IDIOMS_NOT_FOUND = 7778
        const val IDIOM_VM_SEARCH_KEY = "sss_search"
        const val IDIOM_CAT_PREFIX = "mt_id_cat"
        const val IDIOM_CAT_ALL = "general"
        const val IDIOM_CAT_PREFIX_PAGE = "mt_id_cat_page"
        const val IDIOM_CAT_NO_MORE = "mt_id_cat_no_more"

        // Course events and constants

        const val EVENT_COURSE_IMPORT = 7800
        const val EVENT_VIDEOS_LOADING = 7801
        const val EVENT_COURSE_LOADING = 7802
        const val EVENT_VIDEOS_LOADING_COMPLETE = 7803
        const val EVENT_COURSE_LOADING_COMPLETE = 7804
        const val EVENT_VIDEOS_SOMETHING_WENT_WRONG = 7805
        const val EVENT_COURSE_SOMETHING_WENT_WRONG = 7806
        const val EVENT_COURSE_SEARCH_SOMETHING_WENT_WRONG = 7807
        const val EVENT_COURSE_SEARCH_NO_INTERNET = 7808
        const val EVENT_RECENT_COURSE_NO_INTERNET = 7809
        const val EVENT_RECENT_COURSE_FAILED_TO_LOAD = 7810
        const val EVENT_NEW_RECENT_COURSE_FOUND = 7811
        const val EVENT_NEW_RECENT_BOOKS_FOUND = 7812
        const val EVENT_COURSE_HOME_CATEGORY_LOADED = 1813
        const val EVENT_COURSE_SUB_CATEGORY_LOADED = 1814
        const val EVENT_RECENT_COURSE_NOTIFICATION_LOADED = 1815
        const val EVENT_COURSE_CREATOR_LOADED = 1816
        const val EVENT_COURSE_CATEGORY_LOADED = 1817
        const val EVENT_BOOKS_CATEGORY_LOADED = 1818
        const val EVENT_BOOKS_NOTIFICATION_FAIL = 1819
        const val EVENT_BOOKS_NOTIFICATION_NOT_FOUND = 1820
        const val EVENT_BOOKS_UPLOADED_NOTIFICATION_FAIL = 1822
        const val EVENT_BOOKS_UPLOADED_NOTIFICATION_NOT_FOUND = 1821

        // Idioms Constants Ends here

        // Translations events
        const val EVENT_TRANSLATION_FAILED = 7901
        const val EVENT_TRANSLATION_STARTED = 7902
        const val EVENT_TRANSLATION_COMPLETED = 7903
        const val EVENT_TRANSLATION_NO_SUGGESTION = 7904
        const val EVENT_TRANSLATION_CHAR_LIMIT = 7905

        // Translate All Service Events and constants
        const val INTENT_FILTER_BACKEND_TO_FRONT = "trans_all_backend_to_front"
        const val INTENT_FILTER_FRONT_TO_BACKEND = "trans_all_front_to_backend"
        const val EXTRA_START_FIND_VIEW = "to_find_view"
        const val EXTRA_CORD_X = "cord_x"
        const val EXTRA_CORD_Y = "cord_y"
        const val EXTRA_TO_HIDE_VIEW = "to_hide_view"
        const val EXTRA_IS_TURN_OFF_SERVICE = "is_turn_off"
        const val EXTRA_IS_TURN_OFF_FRONTEND_SERVICE = "is_turn_off_frontend"
        const val EXTRA_LANGUAGE_CHANGE = "change_service_language"
        const val EXTRA_NOTIFY_BROWSER_STATE_CHANGE = "change_browser_state"
        const val DEFAULT_SOURCE_LANGUAGE = "default_source_language"
        const val DEFAULT_TARGET_LANGUAGE = "default_target_language"
        const val EXTRA_TRANS_ALL_SOURCE_LANGUAGE = "trans_all_source_language"
        const val EXTRA_TRANS_ALL_TARGET_LANGUAGE = "trans_all_target_language"

        // MagDoc Online events
        const val CONTENT_TYPE_CHAPTER = "Chapter"
        const val CONTENT_TYPE_SOLUTION = "Solution"
        const val EVENT_MAG_DOC_O_NO_SOLUTION = 8000
        const val EVENT_MAG_DOC_O_NO_CHAPTER = 8001
        const val EVENT_MAG_DOC_O_NO_CHAPTER_NO_SOLUTION = 80001
        const val EVENT_MAG_DOC_O_SOLUTION_LOADED = 8002
        const val EVENT_MAG_DOC_O_CHAPTER_LOADED = 8003
        const val EVENT_MAG_DOC_O_REFRESH_FAILED = 8004
        const val EVENT_MAG_DOC_REVIEW_FAILED = 8005
        const val EVENT_MAG_DOC_PURCHASE_VERIFICATION_FAILED = 8006
        const val EVENT_MAG_DOC_PURCHASE_CREATE_FAILED = 8007

        // sort video
        const val GET_UPLOAD_VIDEO_URL_FAIL = 9001
        const val UPLOAD_VIDEO_FAIL = 9002
        const val GET_PROFILE_FAIL = 9003
        const val UPDATE_PROFILE_FAIL = 9004
        const val PROFILE_VERIFICSTION_FAIL = 9005
        const val Get_VERIFICSTION_STATUS_FAIL = 9006
        const val GET_FOR_YOU_VIDEO_LIST_ERROR = 9007
        const val GET_NEAR_ME_VIDEO_LIST_ERROR = 9008
        const val GET_FOLLOWING_VIDEO_LIST_ERROR = 9009

        const val GET_FOR_YOU_VIDEO_LIST_NO_DATA = 90007
        const val GET_NEAR_ME_VIDEO_LIST_NO_DATA = 90008
        const val GET_FOLLOWING_VIDEO_LIST_NO_DATA = 90009

        const val GET_MIGHT_LIKE_BOOK_ERROR = 2021

        const val GET_HASHTAG_VIDEO_LIST_ERROR = 9010
        const val GET_MYPROFILE_VIDEO_LIST_ERROR = 9011
        const val GET_MYPROFILE_LIKE_VIDEO_LIST_ERROR = 9012
        const val GET_USERPROFILE_VIDEO_LIST_ERROR = 9013
        const val GET_USERPROFILE_LIKE_VIDEO_LIST_ERROR = 9014
        const val VIDEO_DONATION_CREATE_FAILED = 9015
        const val VIDEO_DONATION_VERIFICATION_FAILED = 9016
        const val GET_AUDIO_CATEGORIES_STATUS_FAIL = 9017
        const val GET_VIDEO_CATEGORIES_STATUS_FAIL = 9018
        const val GET_AUDIO_VIDEO_STATUS_FAIL = 9019
        const val GET_MORE_VIDEO_LIST_ERROR = 9020
        const val GET_HASHTAG_VIDEO_LIST_SUCCESS = 9020
        const val GET_MY_PAYMENT_INFO_SUCCESS = 9020
        const val GET_CATEGORY_VIDEO_STATUS_FAIL = 9021
        const val GET_ALL_HASHTAG_VIDEO_STATUS_FAIL = 9022
        const val GET_HASHTAG_HOME_TOP_STATUS_FAIL = 9023
        const val GET_USER_INFO_USERNAME_STATUS_FAIL = 9024
        const val USER_SEARCH_NOT_FOUND = 9025
        const val USER_SEARCH_FOUND = 9026
        const val HASH_SEARCH_NOT_FOUND = 9027
        const val HASH_SEARCH_FOUND = 9028
        const val NEAR_ME_LOAD_NEXT = 9029
        const val NEAR_ME_LOAD_STOP = 9030
        const val GET_TOP_FOLLOWING_STATUS_FAIL = 9031
        const val GET_MYPROFILE_LIKE_VIDEO_NO_MORE_DATA = 9032

        const val NO_DONATIONS_FOUND = 9033
        const val DONATIONS_LOADED = 9034
        const val DONATIONS_LOADING_FAILED = 9035

        const val SHORTS_FOR_YOU_ALREADY_UPDATED = 9036
        const val SHORTS_NEAR_ME_ALREADY_UPDATED = 9037
        const val SHORTS_FOLLOWING_ALREADY_UPDATED = 9038

        const val GET_BOOK_CATEGORIES_STATUS_FAIL = 6000
        const val GET_BOOK_SUB_CATEGORIES_STATUS_FAIL = 6001
        const val UPDATE_BOOK_CREATOR_PROFILE_FAIL = 6002

        const val BOOK_SEARCH_FOUND = 60001
        const val BOOK_SEARCH_NOT_FOUND = 60002

        const val GET_NEWS_VIDEO_LIST_ERROR = 60003
        const val GET_NEWS_VIDEO_LIST_NODATA = 60004
        const val GET_CATEGORY_NEWS_LIST_ERROR = 60010
        const val GET_CATEGORY_NEWS_LIST_NODATA = 60011
        const val GET_BOOKCATEGORY_ERROR = 60012
        const val GET_BOOKCATEGORY_NODATA = 60013

        const val EVENT_QUIZ_CATEGORY_LOADED = 60005
        const val EVENT_QUIZ_CATEGORY_FAIL = 60006

        const val EVENT_QUIZ_ALL_CATEGORY_LOADED = 60010
        const val EVENT_QUIZ_ALL_CATEGORY_FAIL = 60011

        const val EVENT_QUIZ_BY_CATEGORY_LOADED = 60007
        const val EVENT_QUIZ_BY_CATEGORY_FAIL = 60008
        const val EVENT_QUIZ_BY_CATEGORY_NOT_FOUND = 60009

        const val EVENT_CHAT_BOT_QUESTIONS_FAIL = 7001
        const val EVENT_CHAT_BOT_QUESTIONS_NOT_FOUND = 7002

        // Translate All Videos
        const val PREF_TRANSLATE_ALL_VIDEO_XIOMI = "pref_translate_all_video_xiomi"
        const val PREF_TRANSLATE_ALL_VIDEO_OPPO = "pref_translate_all_video_oppo"
        const val PREF_TRANSLATE_ALL_VIDEO_VIVO = "pref_translate_all_video_vivo"
        const val PREF_TRANSLATE_ALL_VIDEO_SAMSUNG = "pref_translate_all_video_samsung"
        const val PREF_TRANSLATE_ALL_VIDEO_REALME = "pref_translate_all_video_realme"
        const val PREF_TRANSLATE_ALL_VIDEO_DEFAULT = "pref_translate_all_video_default"

        // AdBlocker Preferences
        const val PREF_TOTAL_ADS_BLOCKED = "pref_total_ads_blocked"
        const val PREF_TOTAL_AD_BLOCKER_ENABLED = "pref_total_ad_blocker_enabled"
        const val PREF_TOTAL_PAGE_VISITED = "pref_total_page_visited"
        const val PREF_TOTAL_ADS_SIZE_SAVED = "pref_total_ads_size_saved"
        const val PREF_TOTAL_ADS_TIME_SAVED = "pref_total_ads_time_saved"

        const val DEFAULT_TRANSLATE_ALL_VIDEO_XIOMI = "https://1drv.ws/v/s!Au8m3aBdK6L1gp9n6wMlbCWM7d1nRw?e=Pn5dmY"
        const val DEFAULT_TRANSLATE_ALL_VIDEO_OPPO = "https://1drv.ws/v/s!Au8m3aBdK6L1gp9kyWkN-uhe5cRM6A?e=smSIon"
        const val DEFAULT_TRANSLATE_ALL_VIDEO_VIVO = "https://1drv.ws/v/s!Au8m3aBdK6L1gp9hqOV56l2JAZKEjw?e=Jfxidm"
        const val DEFAULT_TRANSLATE_ALL_VIDEO_SAMSUNG = "https://1drv.ws/v/s!Au8m3aBdK6L1gp9l9wGN1AsYI7sQeQ?e=GmPC0h"
        const val DEFAULT_TRANSLATE_ALL_VIDEO_REALME = "https://1drv.ws/v/s!Au8m3aBdK6L1gp9gYGOJywSUOaHTdg?e=Byc5D6"
        const val DEFAULT_TRANSLATE_ALL_VIDEO_DEFAULT = "https://1drv.ws/v/s!Au8m3aBdK6L1gp9n6wMlbCWM7d1nRw?e=Pn5dmY"
        const val DEFAULT_AMAZON_TAG_URL = "magtapp-21"

        const val WEBPAGE_ACTION_LOADING_SERVICE = "webpage_action_loading_service"
        const val WEBPAGE_ACTION_PAUSE_SERVICE = "webpage_action_pause_service"
        const val WEBPAGE_ACTION_PLAY_SERVICE = "webpage_action_play_service"
        const val WEBPAGE_ACTION_STOP_SERVICE = "webpage_action_stop_service"
        const val WEBPAGE_ACTION_START_SERVICE = "webpage_action_start_service"
        const val WEBPAGE_NOTIFICATION_CHANNEL_ID = "webpage_reading_channel"
        const val WEBPAGE_NOTIFICATION_CHANNEL_name = "webpage_reading"
        const val WEBPAGE_NOTIFICATION_ID = 1297

        //pdf audio service
        const val ACTION_TTS_SPEED = "ACTION_TTS_SPEED"
        const val ACTION_TTS_PITCH = "ACTION_TTS_PITCH"
        const val ACTION_TTS_VOICE = "ACTION_TTS_VOICE"
        const val ACTION_TTS_LANGUAGE = "ACTION_TTS_LANGUAGE"
        const val ACTION_PLAY_OR_PAUSE_SERVICE = "ACTION_PLAY_OR_PAUSE_SERVICE"
        const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
        const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val ACTION_CLEAR_LINE_HIGHLIGHT = "ACTION_CLEAR_LINE_HIGHLIGHT"
        const val ACTION_NEXT_LINE_SERVICE = "ACTION_NEXT_LINE_SERVICE"
        const val ACTION_PREV_LINE_SERVICE = "ACTION_PREV_LINE_SERVICE"
        const val ACTION_PREV_PAGE_SERVICE = "ACTION_PREV_PAGE_SERVICE"
        const val ACTION_NEXT_PAGE_SERVICE = "ACTION_NEXT_PAGE_SERVICE"
        const val ACTION_REFRESH = "ACTION_REFRESH"
        const val ACTION_REFRESH_COMPLETE = "ACTION_REFRESH_COMPLETE"

        const val NOTIFICATION_CHANNEL_ID = "webpage_reading_channel"
        const val NOTIFICATION_CHANNEL_name = "webpage_reading"
        const val NOTIFICATION_ID = 1297

        enum class LoginType {
            NORMAl {
                override fun toString(): String {
                    return "normal"
                }
            },
            FACEBOOK {
                override fun toString(): String {
                    return "facebook"
                }
            },
            GOOGLE {
                override fun toString(): String {
                    return "google"
                }
            },
            GUEST {
                override fun toString(): String {
                    return "guest"
                }
            }
        }

        enum class ChatBotToolbarType{
            NORMAL,
            SHARE_AND_DELETE,
            JUST_DELETE
        }

        enum class MagDocType {
            PDF {
                override fun toString(): String {
                    return "pdf"
                }
            },
            DOC {
                override fun toString(): String {
                    return "doc"
                }
            },
            DOCX {
                override fun toString(): String {
                    return "docx"
                }
            },
            PPT {
                override fun toString(): String {
                    return "ppt"
                }
            },
            PPTX {
                override fun toString(): String {
                    return "pptx"
                }
            },
            XLS {
                override fun toString(): String {
                    return "xls"
                }
            },
            XLSX {
                override fun toString(): String {
                    return "xlsx"
                }
            },
            EPUB {
                override fun toString(): String {
                    return "epub"
                }
            },
            ALL {
                override fun toString(): String {
                    return "all"
                }
            }
        }

        enum class DocumentSortByActions(val value: Int) {
            SORT_BY_NAME(1),
            SORT_BY_BIG_TO_SMALL(2),
            SORT_BY_SMALL_TO_BIG(3),
            SORT_BY_MOD_TIME_RECENT(4),
            SORT_BY_MOD_TIME_LAST(5),
            SORT_BY_NAME_DESC(6)
        }

        enum class MagNoteActionType {
            ADD {
                override fun toString(): String {
                    return "add_note"
                }
            },
            UPDATE {
                override fun toString(): String {
                    return "update_note"
                }
            },
            DELETE {
                override fun toString(): String {
                    return "delete_note"
                }
            },
            VIEW {
                override fun toString(): String {
                    return "view_note"
                }
            },
            PLAY_MUSIC {
                override fun toString(): String {
                    return "play_music"
                }
            },
            PAUSE_MUSIC {
                override fun toString(): String {
                    return "pause_music"
                }
            },
            DELETE_MUSIC {
                override fun toString(): String {
                    return "delete_music"
                }
            },
            ADD_DRAWING {
                override fun toString(): String {
                    return "add_drawing"
                }
            },
            ADD_FROM_GALLERY {
                override fun toString(): String {
                    return "add_from_gallery"
                }
            },
            ADD_FROM_CAMERA {
                override fun toString(): String {
                    return "add_from_camera"
                }
            },
            ADD_RECORD_AUDIO {
                override fun toString(): String {
                    return "add_record_audio"
                }
            },
            DELETE_IMAGE {
                override fun toString(): String {
                    return "delete_image"
                }
            },
            FILTER_CATEGORY_WISE {
                override fun toString(): String {
                    return "filter_category_wise"
                }
            }

        }

        enum class MagDocActionType {
            FILTER_CATEGORY_WISE {
                override fun toString(): String {
                    return "filter_category_wise"
                }
            },
            SEARCH {
                override fun toString(): String {
                    return "search_mag_doc"
                }
            }
        }

        enum class MagSavedActionType {
            DELETE_HISTORY {
                override fun toString(): String {
                    return "delete_history"
                }
            },
            DELETE_OFFLINE_PAGE {
                override fun toString(): String {
                    return "delete_offline_page"
                }
            },
            DELETE_BOOKMARK_PAGE {
                override fun toString(): String {
                    return "delete_bookmark_page"
                }
            },
            DELETE_SAVED_WORD {
                override fun toString(): String {
                    return "delete_saved_word"
                }
            }
        }

        enum class DownloadFileType {
            APK {
                override fun toString(): String {
                    return "apk"
                }
            },
            XAPK {
                override fun toString(): String {
                    return "xapk"
                }
            },
            MP3 {
                override fun toString(): String {
                    return "mp3"
                }
            },
            WAV {
                override fun toString(): String {
                    return "wav"
                }
            },
            _3GP {
                override fun toString(): String {
                    return "3gp"
                }
            },
            MP4 {
                override fun toString(): String {
                    return "mp4"
                }
            },
            AVI {
                override fun toString(): String {
                    return "avi"
                }
            },
            FLV {
                override fun toString(): String {
                    return "flv"
                }
            },
            JPEG {
                override fun toString(): String {
                    return "jpeg"
                }
            },
            JPG {
                override fun toString(): String {
                    return "jpg"
                }
            },
            PNG {
                override fun toString(): String {
                    return "png"
                }
            },
            XLS {
                override fun toString(): String {
                    return "xls"
                }
            },
            XLSX {
                override fun toString(): String {
                    return "xlsx"
                }
            },
            DOC {
                override fun toString(): String {
                    return "doc"
                }
            },
            DOCX {
                override fun toString(): String {
                    return "docx"
                }
            }
        }

        const val OPEN_TAB_CHANGER = 345
        const val CLOSE_TAB_CHANGER = 346
        const val DEFAULT_TAB_TITLE = "Magtapp Tab"
        const val DEFAULT_WEB_URL = "https://www.google.com"
        const val DEFAULT_OFFLINE_PAGE_TITLE = "Offline saved page"
        const val DEFAULT_TAB_STATE_IS_INCOGNITO = false
        const val BROWSER_MOBILE_AGENET =
            "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.93 Mobile Safari/537.36"
        const val BROWSER_APP_BASIC_AGENT = "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.93 Mobile Safari/537.36"
        const val BROWSER_DESKTOP_AGENET =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36"


        const val GG_ADS_NATIVE_WORD_MEANING_ACTIVITY: String = "word_meaning_activity_native"
        const val GG_ADS_NATIVE_CHAT_BOT_ADAPTER: String = "chat_bot_adapter_native"
        const val GG_ADS_NATIVE_COURSE_CATEGORY_ADAPTER: String = "course_category_adapter_native"
        const val GG_ADS_NATIVE_MAGDOC_BOOK_CATEGORY_ADAPTER: String = "magdocbook_category_adapter_native"
        const val GG_ADS_NATIVE_GAME_CATEGORY_ADAPTER: String = "game_category_adapter_native"
        const val GG_ADS_NATIVE_DOWNLOAD_ACTIVITY: String = "magdocfile_download_activity_native"
        const val GG_ADS_NATIVE_SHORT_VIDEO_EDITING_DIALOG: String = "short_video_editing_dialog_native"
        const val GG_ADS_NATIVE_HOME_NEWS_LIST_ADAPTER: String = "home_news_list_adapter_native"

        const val GG_ADS_BANNER_TRANSLATE_ALL_ACCESSIBIITY_DIALOG: String = "transate_all_accessibility_dialog_banner"
        const val GG_ADS_BANNER_SINGE_DOWNLOAD_ADAPTER: String = "single_download_adapter_banner"
        const val GG_ADS_SINGLE_IDIOM_ADAPTER: String = "single_idiom_adapter_banner"
        const val GG_ADS_BANNER_COURSE_LECTURE: String = "course_lecture_adapter_banner"
        const val GG_ADS_BANNER_TAP_HISTORY: String = "tap_history_adapter_banner"
        const val GG_ADS_BANNER_TRANSLATION_ADAPTER: String = "traslations_adapter_banner"
        const val GG_ADS_BANNER_MAGDOCBOOK_CONTENT_ADAPTER: String = "magdocbook_content_adapter_banner"
        const val GG_ADS_BANNER_MAGDOCBOOK_ACITIVITY: String = "magdocbook_activity_banner"
        const val GG_ADS_BANNER_PDF_ACTIVITY: String = "pdf_activity_banner"
        const val GG_ADS_BANNER_MAGDOC_ADAPTER: String = "magdoc_adapter_banner"
        const val GG_ADS_BANNER_COURSE_INFO_ACTIVITY: String = "course_info_activity_banner"
        const val GG_ADS_BANNER_SINGLE_IDIOM_ACTIVITY: String = "single_idiom_activity_banner"
        const val GG_ADS_BANNER_QUIZ_DETAIL_ACTIVITY: String = "quiz_detail_activity_banner"
        const val GG_ADS_BANNER_QUIZ_QUESTION_ACTIVITY: String = "quiz_question_activity_banner"
        const val GG_ADS_BANNER_QUIZ_RESULT_ACTIVITY: String = "quiz_result_activity_banner"
        const val GG_ADS_BANNER_TRANSLATION_TEXT_ACTIVITY: String = "translation_text_activity_banner"
        const val GG_ADS_BANNER_IMAGE_CROP_RESULT_FRAGMENT: String = "image_crop_result_fragment_banner"

        const val GG_ADS_REWARD_MAGDOCFILE_DOWNLOAD_ACTIVITY: String = "magdocfile_download_activity_reward"
        const val GG_ADS_INTERSTITIAL_MAGDOCFILE_DOWNLOAD_ACTIVITY: String = "magdocfile_download_activity_interstitial"
        const val GG_ADS_REWARD_PDF_ACTIVITY: String = "pdf_activity_reward"
        const val GG_ADS_REWARD_TIMER_PDF_ACTIVITY: String = "pdf_activity_timer_reward"
        const val GG_ADS_INTERSTITIAL_PDF_ACTIVITY: String = "pdf_activity_interstitial"
        const val GG_ADS_INTERSTITIAL_TIMER_PDF_ACTIVITY: String = "pdf_activity_timer_interstitial"
        const val GG_ADS_INTERSTITIAL_MAIN_DASHBOARD_ACTIVITY: String = "main_dashboard_activity_interstitial"

        const val GG_ADS_VIDEO_HOME_SHORT_ACTIVITY: String = "home_shorts_activity_video"
    }
}