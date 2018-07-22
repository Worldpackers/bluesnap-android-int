package com.bluesnap.android.demoapp;

import android.support.test.runner.AndroidJUnit4;

import com.bluesnap.androidapi.models.SdkRequest;
import com.bluesnap.androidapi.services.BSPaymentRequestException;
import com.bluesnap.androidapi.services.BlueSnapService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by sivani on 17/07/2018.
 */

@RunWith(AndroidJUnit4.class)

public class MinimalBillingWithShippingTests extends EspressoBasedTest {
    @After
    public void keepRunning() throws InterruptedException {
        Thread.sleep(1000);
    }

    @Before
    public void setup() throws InterruptedException, BSPaymentRequestException {
        SdkRequest sdkRequest = new SdkRequest(55.5, "USD");
        sdkRequest.setShippingRequired(true);
        setupAndLaunch(sdkRequest);
        onView(withId(R.id.newCardButton)).perform(click());
        defaultCountry = BlueSnapService.getInstance().getUserCountry(this.mActivity.getApplicationContext());
    }

    /**
     * This test verifies that the all credit card fields are displayed as they should
     * when choosing new credit card.
     */
    @Test
    public void new_credit_cc_info_visibility_validation() throws InterruptedException {
        NewCardVisibilityTesterCommon.new_credit_cc_info_visibility_validation();
    }

    /**
     * This test verifies that the all billing contact info fields are displayed
     * according to full billing when choosing new credit card.
     */
    @Test
    public void new_credit_billing_contact_info_visibility_validation() throws InterruptedException {
        NewCardVisibilityTesterCommon.new_credit_contact_info_visibility_validation(R.id.billingViewComponent, false, false);
    }

    /**
     * This test verifies that the all shipping contact info fields are displayed
     * according to shipping enabled when choosing new credit card.
     */
    @Test
    public void new_credit_shipping_contact_info_visibility_validation() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.new_credit_contact_info_visibility_validation(R.id.newShoppershippingViewComponent, true, false);
    }

    /**
     * This test verifies that the country image matches the shopper's country
     * when first entering billing info.
     * (according to its location, or us by default)
     */
    @Test
    public void default_country_view_validation_in_billing() throws InterruptedException, IOException {
        NewCardVisibilityTesterCommon.default_country_view_validation(applicationContext, defaultCountry, R.id.billingViewComponent);
    }

    /**
     * This test checks whether the zip field is visible to the user or not, according
     * to the default Country (the one that is chosen when entering billing).
     */
    @Test
    public void default_country_zip_view_validation_in_billing() throws InterruptedException {
        NewCardVisibilityTesterCommon.default_country_zip_view_validation(defaultCountry, R.id.billingViewComponent);
    }

    /**
     * This test verifies that the country image matches the shopper's country
     * when first entering shipping info.
     * (according to its location, or us by default)
     */
    @Test
    public void default_country_view_validation_in_shipping() throws InterruptedException, IOException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.default_country_view_validation(applicationContext, defaultCountry, R.id.newShoppershippingViewComponent);
    }

    /**
     * This test verifies that the country image changes as expected, according
     * to different choices in shipping info.
     */
    @Test
    public void changing_country_view_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.changing_country_view_validation(R.id.newShoppershippingViewComponent);
    }

    /**
     * This test checks whether the zip field is visible to the user or not, according
     * to the default Country (the one that is chosen when entering shipping).
     */
    @Test
    public void default_country_zip_view_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.default_country_zip_view_validation(defaultCountry, R.id.newShoppershippingViewComponent);
    }

    /**
     * This test checks whether the zip field is visible to the user or not, according
     * to different choices of countries in shipping info.
     */
    @Test
    public void changing_country_zip_view_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.changing_country_zip_view_validation(R.id.newShoppershippingViewComponent);
    }

    /**
     * This test checks whether the state field is visible to the user or not, according
     * to the default Country (the one that is chosen when entering shipping).
     * If the country is USA, Canada or Brazil, then it should be visible,
     * o.w. it doesn't.
     */
    @Test
    public void default_country_state_view_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.default_country_state_view_validation(R.id.newShoppershippingViewComponent, defaultCountry);
    }

    /**
     * This test checks whether the state field is visible to the user or not, according
     * to different choices of countries in shipping info.
     * If the country is USA, Canada or Brazil, then it should be visible,
     * o.w. it doesn't.
     */
    @Test
    public void changing_country_state_view_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.changing_country_state_view_validation(R.id.newShoppershippingViewComponent);
    }

    /**
     * This test verifies that changing the country in billing
     * doesn't change the country in shipping as well.
     */
    @Test
    public void country_changes_per_billing_validation() throws InterruptedException {
        NewCardVisibilityTesterCommon.country_changes_per_fragment_validation(true, false, false);
    }

    /**
     * This test verifies that changing the country in shipping
     * doesn't change the country in billing as well.
     */
    @Test
    public void country_changes_per_shipping_validation() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        NewCardVisibilityTesterCommon.country_changes_per_fragment_validation(false, true, false);
    }

    /**
     * This test verifies that an invalid error appears for every
     * field when leaving it empty (without entering at all)
     */
    @Test
    public void empty_fields_invalid_error_validation_in_shipping() throws InterruptedException {
        //Continue to shipping
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.empty_fields_invalid_error_validation(R.id.newShoppershippingViewComponent, true, false, R.id.shippingButtonComponentView);
    }


    /**
     * This test verifies the invalid error appearance for the name
     * input field in shipping.
     * In all cases we check validity by clicking on another field
     * It covers the following:
     * Click the field and leave it empty
     * Entering an invalid name- less than 2 words or less than 2 characters
     * Entering a valid name
     * Entering an invalid name after entering a valid one
     */
    @Test
    public void name_invalid_error_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.name_invalid_error_validation(R.id.newShoppershippingViewComponent, false, R.id.input_zip);
    }

    /**
     * This test verifies the invalid error appearance for the name
     * input field in shipping.
     * In all cases we check validity by pressing the Ime button
     * It covers the following:
     * Click the field and leave it empty
     * Entering an invalid name- less than 2 words or less than 2 characters
     * Entering a valid name
     * Entering an invalid name after entering a valid one
     */
    @Test
    public void name_invalid_error_validation_using_ime_button_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.name_invalid_error_validation(R.id.newShoppershippingViewComponent, true, 0);

    }

    /**
     * This test verifies the invalid error appearance for the zip
     * input field in shipping.
     * In all cases we check validity by clicking on another field
     * It covers the following:
     * Click the field and leave it empty
     * Entering an invalid zip- invalid characters
     * Entering a valid zip
     * Entering an invalid zip after entering a valid one
     */
    @Test
    public void zip_invalid_error_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.zip_invalid_error_validation(R.id.newShoppershippingViewComponent, false, R.id.input_name);
    }

    /**
     * This test verifies the invalid error appearance for the zip
     * input field in shipping.
     * In all cases we check validity by pressing the Ime button
     * It covers the following:
     * Click the field and leave it empty
     * Entering an invalid zip- invalid characters
     * Entering a valid zip
     * Entering an invalid zip after entering a valid one
     */
    @Test
    public void zip_invalid_error_validation_using_ime_button_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.zip_invalid_error_validation(R.id.newShoppershippingViewComponent, true, 0);
    }

    /**
     * This test verifies the invalid error appearance for the city
     * input field in shipping.
     * In all cases we check validity by clicking on another field
     * It covers the following:
     * Click the field and leave it empty
     * Entering an invalid city- less than 2 characters
     * Entering a valid city
     * Entering an invalid city after entering a valid one
     */
    @Test
    public void city_invalid_error_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.city_invalid_error_validation(R.id.newShoppershippingViewComponent, false, R.id.input_address);
    }

    /**
     * This test verifies the invalid error appearance for the city
     * input field in shipping.
     * In all cases we check validity by pressing the Ime button
     * It covers the following:
     * Click the field and leave it empty
     * Entering an invalid city- less than 2 characters
     * Entering a valid city
     * Entering an invalid city after entering a valid one
     */
    @Test
    public void city_invalid_error_validation_using_ime_button_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.city_invalid_error_validation(R.id.newShoppershippingViewComponent, true, 0);
    }

    /**
     * This test verifies the invalid error appearance for the address
     * input field in shipping.
     * In all cases we check validity by clicking on another field
     * It covers the following:
     * Click the field and leave it empty
     * Entering an invalid address- invalid characters
     * Entering a valid address
     * Entering an invalid address after entering a valid one
     */
    @Test
    public void address_invalid_error_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.address_invalid_error_validation(R.id.newShoppershippingViewComponent, false, R.id.input_city);
    }

    /**
     * This test verifies that the invalid state error disappears
     * after entering a state.
     */
    @Test
    public void state_invalid_error_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.state_invalid_error(R.id.newShoppershippingViewComponent, R.id.shippingButtonComponentView);
    }

    /**
     * This test verifies the ime action button works as it should
     * in shipping contact info
     */
    @Test
    public void check_ime_action_button_in_shipping_contact_info() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.check_ime_action_button_in_contact_info(defaultCountry, R.id.newShoppershippingViewComponent, true, false);
    }

    /**
     * This test verifies that the shipping contact info is saved when
     * going back to billing and entering the shipping once again.
     */
    @Test
    public void contact_info_saved_validation_in_shipping() throws InterruptedException {
        TestUtils.continue_to_shipping_in_new_card(defaultCountry, false, false);
        ContactInfoTesterCommon.contact_info_saved_validation(false, R.id.newShoppershippingViewComponent, true, false);
    }

    /**
     * This test verifies that the credit card line info is saved when
     * continuing to shipping and going back to billing,
     * while using the back button.
     */
    @Test
    public void cc_card_info_saved_validation() throws InterruptedException {
        CreditCardLineTesterCommon.cc_card_info_saved_validation(defaultCountry, false, false);
    }

}
