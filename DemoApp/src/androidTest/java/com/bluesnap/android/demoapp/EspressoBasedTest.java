package com.bluesnap.android.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleCallback;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.UiDevice;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import com.bluesnap.androidapi.models.PriceDetails;
import com.bluesnap.androidapi.models.SdkRequest;
import com.bluesnap.androidapi.services.BSPaymentRequestException;
import com.bluesnap.androidapi.services.BlueSnapService;
import com.bluesnap.androidapi.services.BluesnapServiceCallback;
import com.bluesnap.androidapi.services.TaxCalculator;
import com.bluesnap.androidapi.services.TokenProvider;
import com.bluesnap.androidapi.services.TokenServiceCallback;
import com.bluesnap.androidapi.views.activities.BluesnapCheckoutActivity;

import org.junit.Rule;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.bluesnap.android.demoapp.DemoToken.SANDBOX_PASS;
import static com.bluesnap.android.demoapp.DemoToken.SANDBOX_TOKEN_CREATION;
import static com.bluesnap.android.demoapp.DemoToken.SANDBOX_URL;
import static com.bluesnap.android.demoapp.DemoToken.SANDBOX_USER;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.containsString;


/**
 *
 */
public class EspressoBasedTest {
    NumberFormat df;
    public String merchantToken;
    protected RandomTestValuesGenerator randomTestValuesGenerator = new RandomTestValuesGenerator();
    protected IdlingResource tokenProgressBarIR;
    protected IdlingResource transactionMessageIR;
    private static final String TAG = EspressoBasedTest.class.getSimpleName();
    private boolean isSdkRequestIsNull = false;
    protected String defaultCountry;
    protected String checkoutCurrency = "USD";
    protected double purchaseAmount = TestUtils.round_amount(randomTestValuesGenerator.randomDemoAppPrice());
    private double taxPercent = randomTestValuesGenerator.randomTaxPrecentage() / 100;
    protected double taxAmount = TestUtils.round_amount(purchaseAmount * taxPercent);

    public Context applicationContext;
//    private static final IdlingRegistry INSTANCE = new IdlingRegistry();

    @Rule
    public ActivityTestRule<BluesnapCheckoutActivity> mActivityRule = new ActivityTestRule<>(
            BluesnapCheckoutActivity.class, false, false);
    protected BluesnapCheckoutActivity mActivity;

    //    @Before
    public void doSetup() throws InterruptedException, BSPaymentRequestException {
        try {
            wakeUpDeviceScreen();
        } catch (RemoteException e) {
            fail("Could not wake up device");
            e.printStackTrace();
        }
        randomTestValuesGenerator = new RandomTestValuesGenerator();
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(60, TimeUnit.SECONDS);

        //Wake up device again in case token fetch took to much time
        try {
            wakeUpDeviceScreen();
        } catch (RemoteException e) {
            fail("Could not wake up device");
            e.printStackTrace();
        }
    }

    public void setupAndLaunch(SdkRequest sdkRequest) throws InterruptedException, BSPaymentRequestException {
        doSetup();
        setNumberFormat();
        sdkRequest.setTaxCalculator(new TaxCalculator() {
            @Override
            public void updateTax(String shippingCountry, String shippingState, PriceDetails priceDetails) {
                if ("us".equalsIgnoreCase(shippingCountry)) {
                    Double taxRate = taxPercent;
                    if ("ma".equalsIgnoreCase(shippingState)) {
                        taxRate = 0.1;
                    }
                    priceDetails.setTaxAmount(priceDetails.getSubtotalAmount() * taxRate);
                } else {
                    priceDetails.setTaxAmount(0D);
                }
            }
        });

        setSDKToken();
        Intent intent = new Intent();
        BlueSnapService.getInstance().setSdkRequest(sdkRequest);
        mActivityRule.launchActivity(intent);
        mActivity = mActivityRule.getActivity();
        applicationContext = mActivity.getApplicationContext();
        defaultCountry = BlueSnapService.getInstance().getUserCountry(this.mActivity.getApplicationContext());
    }

    public void setSDKToken() throws InterruptedException {
        try {
            URL myURL = new URL(SANDBOX_URL + SANDBOX_TOKEN_CREATION);
            HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
            String userCredentials = SANDBOX_USER + ":" + SANDBOX_PASS;
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), 0));
            myURLConnection.setRequestProperty("Authorization", basicAuth);
            myURLConnection.setRequestMethod("POST");
            myURLConnection.connect();
            int responseCode = myURLConnection.getResponseCode();
            String locationHeader = myURLConnection.getHeaderField("Location");
            merchantToken = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        } catch (IOException e) {
            fail("Network error obtaining token:" + e.getMessage());
            e.printStackTrace();
        }

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        final TokenProvider tokenProvider = new TokenProvider() {
                            @Override
                            public void getNewToken(final TokenServiceCallback tokenServiceCallback) {
                                new TokenServiceInterface() {
                                    @Override
                                    public void onServiceSuccess() {
                                        //change the expired token
                                        tokenServiceCallback.complete(merchantToken);
                                    }

                                    @Override
                                    public void onServiceFailure() {
                                    }
                                };
                            }
                        };
                        BlueSnapService.getInstance().setup(merchantToken, tokenProvider, null, new BluesnapServiceCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "Service finish setup");
                                isSdkRequestIsNull = true;
                            }

                            @Override
                            public void onFailure() {
                                fail("Service could not finish setup");
                                isSdkRequestIsNull = true;
                            }
                        });

                    }
                });
        while (BlueSnapService.getInstance().getBlueSnapToken() == null) {
            Log.d(TAG, "Waiting for token setup");
            Thread.sleep(2000);

        }

        while (BlueSnapService.getInstance().getsDKConfiguration() == null) {
            Log.d(TAG, "Waiting for SDK configuration to finish");
            Thread.sleep(2000);

        }

        while (!isSdkRequestIsNull) {
            Log.d(TAG, "Waiting for SDK configuration to finish");
            Thread.sleep(500);

        }
    }


    public void checkToken() {
        try {
            onView(withText(containsString("Cannot obtain token"))).check(matches(isDisplayed()));
            fail("No token from server");
        } catch (NoMatchingViewException e) {
            //view not displayed logic
        }
    }

    //@After
    public void detectIfNoToken() {
        IdlingPolicies.setMasterPolicyTimeout(60, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(60, TimeUnit.SECONDS);
        checkToken();

    }


    public void wakeUpDeviceScreen() throws RemoteException {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        uiDevice.wakeUp();
        ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback(new ActivityLifecycleCallback() {
            @Override
            public void onActivityLifecycleChanged(Activity activity, Stage stage) {
                //if (stage == Stage.PRE_ON_CREATE) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                // }
            }
        });
    }

    public void setNumberFormat() {
        df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);
    }

}
