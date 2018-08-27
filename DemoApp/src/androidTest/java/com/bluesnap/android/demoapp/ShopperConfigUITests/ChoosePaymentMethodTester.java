package com.bluesnap.android.demoapp.ShopperConfigUITests;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.bluesnap.android.demoapp.EspressoBasedTest;
import com.bluesnap.android.demoapp.R;
import com.bluesnap.androidapi.models.SdkRequestShopperRequirements;
import com.bluesnap.androidapi.services.BSPaymentRequestException;
import com.bluesnap.androidapi.views.activities.BluesnapChoosePaymentMethodActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by sivani on 27/08/2018.
 */

public class ChoosePaymentMethodTester extends EspressoBasedTest {
    private static final String RETURNING_SHOPPER_ID_FULL_BILLING_WITH_SHIPPING_WITH_EMAIL = "29632264";
    private static final String RETURNING_SHOPPER_ID_FULL_BILLING_WITH_SHIPPING = "29632268";

    public ChoosePaymentMethodTester() {
        super(true, "?shopperId=" + RETURNING_SHOPPER_ID_FULL_BILLING_WITH_SHIPPING_WITH_EMAIL);
    }

    @Rule
    public ActivityTestRule<BluesnapChoosePaymentMethodActivity> mActivityRule = new ActivityTestRule<>(
            BluesnapChoosePaymentMethodActivity.class, false, false);

    @Before
    public void setup() throws InterruptedException, BSPaymentRequestException {
        SdkRequestShopperRequirements sdkRequest = new SdkRequestShopperRequirements(true, true, true);
        setupAndLaunch(sdkRequest);  //choose EUR as base currency

    }

    @Test
    public void choose_exist_card_visibility_test() {
        //choose existing credit card
        onData(anything()).inAdapterView(withId(R.id.oneLineCCViewComponentsListView)).atPosition(0).perform(click());
        currency_hamburger_button_visibility_in_credit_card();
        submit_button_visibility_and_content();

        onView(Matchers.allOf(withId(R.id.editButton), isDescendantOfA(withId(R.id.billingViewSummarizedComponent)))).perform(click());
        currency_hamburger_button_visibility_in_billing();
        Espresso.pressBack();


        onView(Matchers.allOf(withId(R.id.editButton), isDescendantOfA(withId(R.id.shippingViewSummarizedComponent)))).perform(click());
        currency_hamburger_button_visibility_in_shipping();
        Espresso.pressBack();
    }

    @Test
    public void choose_exist_card_submit_test() {


    }

    //    @Test
    public void choose_new_card_test() {
        //choose new credit card
        onView(withId(R.id.newCardButton)).perform(click());
    }

    /**
     * This test verifies that the "Submit" button is visible and contains
     * the correct content
     */
    public void submit_button_visibility_and_content() {
        ShopperConfigVisibilityTesterCommon.submit_button_visibility_and_content("submit_button_visibility_and_content", R.id.returningShppoerCCNFragmentButtonComponentView);
    }

    /**
     * This test verifies that the hamburger button is not displayed in credit card
     */
    public void currency_hamburger_button_visibility_in_credit_card() {
        ShopperConfigVisibilityTesterCommon.currency_hamburger_button_visibility("currency_hamburger_button_visibility");
    }

    /**
     * This test verifies that the hamburger button is not displayed in billing
     */
    public void currency_hamburger_button_visibility_in_billing() {
        ShopperConfigVisibilityTesterCommon.currency_hamburger_button_visibility("currency_hamburger_button_visibility");
    }

    /**
     * This test verifies that the hamburger button is not displayed in shipping
     */
    public void currency_hamburger_button_visibility_in_shipping() {
        ShopperConfigVisibilityTesterCommon.currency_hamburger_button_visibility("currency_hamburger_button_visibility");
    }

}
