package com.apploop.face.changer.app.manager

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform

/**
 * The Google Mobile Ads SDK provides the User Messaging Platform (Google's IAB Certified consent
 * management platform) as one solution to capture consent for users in GDPR impacted countries.
 * This is an example and you can choose another consent management platform to capture consent.
 */
class GoogleMobileAdsConsentManager private constructor(context: Context) {
  companion object {
    @Volatile private var instance: GoogleMobileAdsConsentManager? = null

    fun getInstance(context: Context) =
      instance
        ?: synchronized(this) {
          instance ?: GoogleMobileAdsConsentManager(context).also { instance = it }
        }
  }

  private val consentInformation: ConsentInformation =
    UserMessagingPlatform.getConsentInformation(context)

  /** Interface definition for a callback to be invoked when consent gathering is complete. */
  fun interface OnConsentGatheringCompleteListener {
    fun consentGatheringComplete(error: FormError?)
  }

  /** Helper variable to determine if the app can request ads. */
  val canRequestAds: Boolean
    get() = consentInformation.canRequestAds()

  /** Helper variable to determine if the privacy options form is required. */
  val isPrivacyOptionsRequired: Boolean
    get() =
      consentInformation.privacyOptionsRequirementStatus ==
        ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

  /**
   * Helper method to call the UMP SDK methods to request consent information and load/show a
   * consent form if necessary.
   */
  fun gatherConsent(
    activity: Activity,
    onConsentGatheringCompleteListener: OnConsentGatheringCompleteListener
  ) {
    // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
    val debugSettings =
      ConsentDebugSettings.Builder(activity)
         .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        // Check your logcat output for the hashed device ID e.g.
        // "Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("ABCDEF012345")" to use
        // the debug functionality.
        .addTestDeviceHashedId("9E802CC88B00B58F3A3D8F8F054F405A")
        .build()

    val params = ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()
//    val params = ConsentRequestParameters.Builder()
//      .setTagForUnderAgeOfConsent(false)
//      .build()

    // Requesting an update to consent information should be called on every app launch.
    consentInformation.requestConsentInfoUpdate(
      activity,
      params,
      {
        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
          // Consent has been gathered.
          onConsentGatheringCompleteListener.consentGatheringComplete(formError)
        }
      },
      { requestConsentError ->
        onConsentGatheringCompleteListener.consentGatheringComplete(requestConsentError)
      }
    )
  }

  /** Helper method to call the UMP SDK method to show the privacy options form. */
  fun showPrivacyOptionsForm(
    activity: Activity,
    onConsentFormDismissedListener: OnConsentFormDismissedListener
  ) {
    UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
  }
}
